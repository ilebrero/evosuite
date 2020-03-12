/**
 * Copyright (C) 2010-2020 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.symbolic.DSE.algorithm;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.stoppingconditions.StoppingCondition;
import org.evosuite.symbolic.DSE.DSEStatistics;
import org.evosuite.symbolic.PathCondition;
import org.evosuite.symbolic.PathConditionUtils;
import org.evosuite.testcase.TestCase;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract superclass of DSE algorithms
 *
 * @author Ignacio Lebrero
 */
public abstract class DSEBaseAlgorithm<T extends Chromosome> {

	/** Logger Messages */
	private static final String PATH_DIVERGENCE_FOUND_WARNING_MESSAGE = "Warning | Path condition diverged";
	private static final String ONLY_LINE_CRITERIA_SUPPORTED_EXCEPTION_MESSAGE = "Error | Only supported line coverage for the moment. Please check your selected critera";

	private static final Logger logger = LoggerFactory.getLogger(DSEBaseAlgorithm.class);

	protected final TestSuiteChromosome testSuite = new TestSuiteChromosome();

	/** DSE statistics */
	protected final DSEStatistics statisticsLogger;

	/** Fitness Functions */
	protected transient List<TestSuiteFitnessFunction> fitnessFunctions = new ArrayList();

	/** List of conditions on which to end the search */
	protected transient Set<StoppingCondition> stoppingConditions = new HashSet();

	public DSEBaseAlgorithm(DSEStatistics dseStatistics) {
		this.statisticsLogger = dseStatistics;
	}

	/**
	 * Add new fitness function (i.e., for new mutation)
	 *
	 * @param function
	 */
	public void addFitnessFunction(TestSuiteFitnessFunction function) {
		fitnessFunctions.add(function);
	}

	/**
	 * Add new fitness functions
	 *
	 * @param functions
	 */
	public void addFitnessFunctions(List<TestSuiteFitnessFunction> functions) {
		for (TestSuiteFitnessFunction function : functions)
			this.addFitnessFunction(function);
	}

	/**
	 * Get currently used fitness function
	 * @return
	 */
	public TestSuiteFitnessFunction getFitnessFunction() {
		return fitnessFunctions.get(0);
	}

	/**
	 * Get all used fitness function
	 * @return
	 */
	public List<TestSuiteFitnessFunction> getFitnessFunctions() {
		return fitnessFunctions;
	}

	/**
	 * Calculates current test suite fitness
	 */
	public void calculateFitness() {
		logger.debug("Calculating fitness for current test suite");

		for (TestSuiteFitnessFunction fitnessFunction : fitnessFunctions) {
			fitnessFunction.getFitness(testSuite);
		}
	}

	/**
	 * Calculates current test suite fitness
	 */
	public double getFitness() {
		return testSuite.getFitness();
	}

	/**
	 * Determine whether any of the stopping conditions hold
	 *
	 * @return a boolean.
	 */
	public boolean isFinished() {
		for (StoppingCondition c : stoppingConditions) {
			if (c.isFinished())
				return true;
		}
		return false;
	}

	/**
	 * Returns the progress of the search.
	 *
	 * @return a value [0.0, 1.0]
	 */
	protected double progress() {
		long totalbudget = 0;
		long currentbudget = 0;

		for (StoppingCondition sc : this.stoppingConditions) {
			if (sc.getLimit() != 0) {
				totalbudget += sc.getLimit();
				currentbudget += sc.getCurrentValue();
			}
		}

		return (double) currentbudget / (double) totalbudget;
	}

    /**
     * Checks whether the current executed path condition diverged from the original one.
     * TODO: Maybe we can give some info about the PathCondition that diverged later on
	 *
     * @param currentPathCondition
     */
    protected void checkPathConditionDivergence(PathCondition currentPathCondition, PathCondition expectedPathCondition) {
    	statisticsLogger.reportNewPathExplored();

        if (PathConditionUtils.hasPathConditionDiverged(expectedPathCondition, currentPathCondition)) {
            logger.debug(PATH_DIVERGENCE_FOUND_WARNING_MESSAGE);
        	statisticsLogger.reportNewPathDivergence();
        }
    }

	/**
	 * Score calculation is based on fitness improvement against the current testSuite.
	 *
	 * @param newTestCase
	 * @return
	 */
    protected double getTestScore(TestCase newTestCase) {
    	double oldCoverage;
    	double newCoverage;

    	oldCoverage = testSuite.getCoverage();

		testSuite.addTest(newTestCase);
        calculateFitness();

        newCoverage = getFitness();

		testSuite.deleteTest(newTestCase);
		calculateFitness();

		return newCoverage - oldCoverage;
    }

}