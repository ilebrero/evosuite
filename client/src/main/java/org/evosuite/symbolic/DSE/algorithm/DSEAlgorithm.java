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

import org.evosuite.symbolic.DSE.ConcolicEngine;
import org.evosuite.symbolic.PathCondition;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.solver.Solver;
import org.evosuite.symbolic.solver.SolverEmptyQueryException;
import org.evosuite.symbolic.solver.SolverErrorException;
import org.evosuite.symbolic.solver.SolverFactory;
import org.evosuite.symbolic.solver.SolverParseException;
import org.evosuite.symbolic.solver.SolverResult;
import org.evosuite.symbolic.solver.SolverTimeoutException;
import org.evosuite.symbolic.solver.SolverUtils;
import org.evosuite.testcase.DefaultTestCase;
import org.evosuite.testcase.TestCase;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
   * General Structure of a DSE Algorithm
   *
   * @author ilebrero
   */
public abstract class DSEAlgorithm extends DSEBaseAlgorithm {

    private static final Logger logger = LoggerFactory.getLogger(DSEAlgorithm.class);

    private static final String SOLVER_ERROR_DEBUG_MESSAGE = "Solver threw an exception when running: {}";
    private static final String SOLVER_QUERY_STARTED_MESSAGE = "Solving query with {} constraints";

    /**
     * A cache of previous results from the constraint solver
     * */
    protected final Map<Set<Constraint<?>>, SolverResult> queryCache
            = new HashMap<Set<Constraint<?>>, SolverResult>();

    private ConcolicEngine engine;
    private Solver solver;

    public DSEAlgorithm() {
        this(
            new ConcolicEngine(),
            SolverFactory.getInstance().buildNewSolver()
        );
    }

    public DSEAlgorithm(ConcolicEngine engine, Solver solver) {
        this.engine = engine;
        this.solver = solver;
    }

//    TODO: only static methods for, will extend later on
    public void algorithm(Method method) {
        List<TestCase> generatedTests = new ArrayList();

        // Build initial testCase input
        generatedTests.add(
                buildInitialTestCase(method)
        );

        HashSet<Set<Constraint<?>>> alreadyGeneratedPathConditions = new HashSet();
        while (shouldKeepSearching(generatedTests)) {

            // Do a concolic execution on an arbitrary test case
            TestCase currentConcreteTest = getCurrentIterationBasedTestCase(generatedTests).clone();
            final PathCondition currentPathCondition = engine.execute((DefaultTestCase) currentConcreteTest);

            alreadyGeneratedPathConditions.add(
                normalizePathCondition(currentPathCondition)
            );

            PathCondition nextPathToExplore = getNextPathToExplore(currentPathCondition);
            List<Constraint<?>> query = SolverUtils.buildQuery(nextPathToExplore);
            Set<Constraint<?>> normalizedQueryConstraints = normalize(query);

            if (!shouldSkipCurrentPath(alreadyGeneratedPathConditions, normalizedQueryConstraints)) {
                // TODO: check if this is really neded, problable the SMT language is asking for this.
                query.addAll(
                    SolverUtils.createBoundsForQueryVariables(query)
                );

                logger.debug(SOLVER_QUERY_STARTED_MESSAGE, query.size());
                SolverResult smtQueryResult = solveQuery(query);
                analizeResults(smtQueryResult, generatedTests, currentConcreteTest);
            }
        }
    }


    private void analizeResults(SolverResult smtQueryResult, List<TestCase> generatedTests, TestCase currentConcreteTest) {
        // TODO: completar
    }

     public TestSuiteChromosome generateSolution() {
         // TODO: completar
         return null;
     }

    private SolverResult solveQuery(List<Constraint<?>> SMTQuery) {
        SolverResult smtQueryResult = null;
        try {
            smtQueryResult = solver.solve(SMTQuery);
        } catch (SolverTimeoutException
                | SolverParseException
                | SolverEmptyQueryException
                | SolverErrorException
                | IOException e) {
            logger.debug(SOLVER_ERROR_DEBUG_MESSAGE, e.getMessage());
        }
        return smtQueryResult;
    }

     protected abstract boolean shouldKeepSearching(List<TestCase> generatedTests);
     protected abstract boolean shouldSkipCurrentPath(HashSet<Set<Constraint<?>>> alreadyGeneratedPathConditions, Set<Constraint<?>> constraintSet);

     protected abstract TestCase getCurrentIterationBasedTestCase(List<TestCase> generatedTests);
     protected abstract TestCase buildInitialTestCase(Method method);

     protected abstract PathCondition getNextPathToExplore(PathCondition currentPathCondition);

     protected abstract Set<Constraint<?>> normalize(List<Constraint<?>> query);
     protected abstract Set<Constraint<?>> normalizePathCondition(PathCondition currentPathCondition);

}
