/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
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
package org.evosuite.dse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.examples.with.different.packagename.dse.array.IntegerArrayAssignmentExample;
import com.examples.with.different.packagename.dse.PathDivergeUsingHashExample;
import com.examples.with.different.packagename.dse.array.IntegerArrayAssignmentExample2;
import com.examples.with.different.packagename.dse.array.RealArrayAssignmentExample;
import com.examples.with.different.packagename.dse.array.RealArrayAssignmentExample2;
import com.examples.with.different.packagename.dse.array.StringArrayAssignmentExample;
import com.examples.with.different.packagename.dse.invokedynamicdsc.instrument.SingleMethodReference;
import com.examples.with.different.packagename.dse.lambda.LambdaExample;
import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.Properties.Criterion;
import org.evosuite.Properties.SolverType;
import org.evosuite.Properties.StoppingCondition;
import org.evosuite.Properties.Strategy;
import org.evosuite.SystemTestBase;
import org.evosuite.symbolic.dse.algorithm.ExplorationAlgorithmBase;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.examples.with.different.packagename.dse.Add;
import com.examples.with.different.packagename.dse.array.ArrayLengthExample;
import com.examples.with.different.packagename.dse.BooleanExample;
import com.examples.with.different.packagename.dse.ByteExample;
import com.examples.with.different.packagename.dse.CharExample;
import com.examples.with.different.packagename.dse.DoubleExample;
import com.examples.with.different.packagename.dse.FloatExample;
import com.examples.with.different.packagename.dse.LongExample;
import com.examples.with.different.packagename.dse.Max;
import com.examples.with.different.packagename.dse.Min;
import com.examples.with.different.packagename.dse.MinUnreachableCode;
import com.examples.with.different.packagename.dse.NoStaticMethod;
import com.examples.with.different.packagename.dse.ObjectExample;
import com.examples.with.different.packagename.dse.ShortExample;
import com.examples.with.different.packagename.dse.StringExample;

public class DSEAlgorithmSystemTest extends SystemTestBase {

	@Before
	public void init() {
		Properties.VIRTUAL_FS = true;
		Properties.VIRTUAL_NET = true;
		Properties.LOCAL_SEARCH_PROBABILITY = 1.0;
		Properties.LOCAL_SEARCH_RATE = 1;
		Properties.LOCAL_SEARCH_BUDGET_TYPE = Properties.LocalSearchBudgetType.TESTS;
		Properties.LOCAL_SEARCH_BUDGET = 100;
		Properties.SEARCH_BUDGET = 50000;
		// Properties.CONCOLIC_TIMEOUT = Integer.MAX_VALUE;
		Properties.RESET_STATIC_FIELD_GETS = true;

		String cvc4_path = System.getenv("CVC4_PATH");
		if (cvc4_path != null) {
			Properties.CVC4_PATH = cvc4_path;
		}

		Properties.DSE_SOLVER = SolverType.CVC4_SOLVER;

		Properties.STOPPING_CONDITION = StoppingCondition.MAXTIME;
		Properties.SEARCH_BUDGET = 60 * 60 * 10; // 10 hours
		Properties.MINIMIZATION_TIMEOUT = 60 * 60;
		Properties.ASSERTION_TIMEOUT = 60 * 60;
		// Properties.TIMEOUT = Integer.MAX_VALUE;

		Properties.STRATEGY = Strategy.DSE;
		Properties.SELECTED_DSE_ARRAYS_MEMORY_MODEL_VERSION = Properties.DSE_ARRAYS_MEMORY_MODEL_VERSION.SELECT_STORE_EXPRESSIONS;

		Properties.CRITERION = new Criterion[] { Criterion.BRANCH };

		Properties.MINIMIZE = true;
		Properties.ASSERTIONS = true;

		assumeTrue(Properties.CVC4_PATH != null);
	}

	@Test
	public void testMax() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.SHOW_PROGRESS = true;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(7, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());
	}

	@Test
	public void testAdd() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = Add.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(1, best.getTests().size());

		assertEquals(1, best.getNumOfCoveredGoals());
		assertEquals(1, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testNoStaticMethod() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = NoStaticMethod.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		assertTrue(best.getTests().isEmpty());

		System.out.println("EvolvedTestSuite:\n" + best);

		assertTrue(best.getTests().isEmpty());

		assertEquals(0, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testMin() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Min.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(2, best.getNumOfCoveredGoals());
		assertEquals(1, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testBooleanInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = BooleanExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testShortInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = ShortExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testByteInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = ByteExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testCharInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = CharExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testLongInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = LongExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testDoubleInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = DoubleExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testFloatInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = FloatExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());

	}

	@Test
	public void testUnreachableCode() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = MinUnreachableCode.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(2, best.getTests().size());

		assertEquals(3, best.getNumOfCoveredGoals());
		assertEquals(2, best.getNumOfNotCoveredGoals());
	}

    // Not supported so far
    @Test
	@Ignore
	public void testMaxTestsStoppingCondition() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		Properties.STOPPING_CONDITION = StoppingCondition.MAXTESTS;
		Properties.SEARCH_BUDGET = 1;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(1, best.getTests().size());
	}

// Not supported so far
    @Test
	@Ignore
    public void testMaxFitnessEvaluationStoppingCondition() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		Properties.STOPPING_CONDITION = StoppingCondition.MAXFITNESSEVALUATIONS;
		Properties.SEARCH_BUDGET = 2;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(1, best.getTests().size());
	}

	@Test
    @Ignore
//    Not supported so far
	public void testMaxTimeStoppingCondition() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		Properties.STOPPING_CONDITION = StoppingCondition.MAXTIME;
		Properties.SEARCH_BUDGET = -1;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertTrue(best.getTests().isEmpty());

	}

	@Test
    @Ignore
//    Not supported so far
	public void testMaxStatementsStoppingCondition() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		Properties.STOPPING_CONDITION = StoppingCondition.MAXSTATEMENTS;
		Properties.SEARCH_BUDGET = 1;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(1, best.getTests().size());
	}

	@Test
//    What does stopZero means?
	public void testStopZeroMax() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = Max.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.STOP_ZERO = true;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(7, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());
	}

	@Test
	public void testObjectInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = ObjectExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertTrue(best.getNumOfCoveredGoals() >= 3);

	}

	@Test
	public void testStringInput() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = StringExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertEquals(best.getNumOfCoveredGoals(), 4);
		assertEquals(best.getNumOfNotCoveredGoals(), 1);
	}

	@Test
	public void testArrayLength() {

		EvoSuite evosuite = new EvoSuite();
		String targetClass = ArrayLengthExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(9, best.getNumOfCoveredGoals() );
		assertEquals(2, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testIntegerArrayAssignment() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = IntegerArrayAssignmentExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(2, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testIntegerArrayAssignment2() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = IntegerArrayAssignmentExample2.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(6, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testIntegerArrayAssignmentLazyVariables() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = IntegerArrayAssignmentExample2.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.SELECTED_DSE_ARRAYS_MEMORY_MODEL_VERSION = Properties.DSE_ARRAYS_MEMORY_MODEL_VERSION.LAZY_VARIABLES;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(6, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testRealArrayAssignment() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = RealArrayAssignmentExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(4, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testRealArrayAssignment2() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = RealArrayAssignmentExample2.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(10, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	@Test
	public void testRealArrayAssignmentLazyVariables() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = RealArrayAssignmentExample2.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.SELECTED_DSE_ARRAYS_MEMORY_MODEL_VERSION = Properties.DSE_ARRAYS_MEMORY_MODEL_VERSION.LAZY_VARIABLES;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(10, best.getNumOfCoveredGoals() );
		assertEquals(1, best.getNumOfNotCoveredGoals() );
	}

	/**
	 * TODO: String arrays support not yet implemented
	 */
	@Test
	@Ignore
	public void testStringArrayAssignment() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = StringArrayAssignmentExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertEquals(0, best.getNumOfCoveredGoals() );
		assertEquals(5, best.getNumOfNotCoveredGoals() );
	}

	/**
	 * Given that the concolic engine makes the un-instrumented functions results concrete, the hashing case gets covered..
	 *
	 * See examples on: Patrice Godefroid - Higher-Order Test Generation.
	 */
	@Test
	public void testPathDivergenceWithHashingfunction() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = PathDivergeUsingHashExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };
		Object result = evosuite.parseCommandLine(command);

		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());
		assertTrue(best.getNumOfCoveredGoals() >= 3);
	}

	@Test
	public void testLambda() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = LambdaExample.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.SHOW_PROGRESS = true;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(7, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());
	}

	@Test
	public void testSingleMethodReference() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = SingleMethodReference.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
		Properties.SHOW_PROGRESS = true;

		String[] command = new String[] { "-generateSuiteUsingDSE", "-class", targetClass };

		Object result = evosuite.parseCommandLine(command);
		ExplorationAlgorithmBase dse = getDSEAFromResult(result);
		TestSuiteChromosome best = dse.getGeneratedTestSuite();
		System.out.println("EvolvedTestSuite:\n" + best);

		assertFalse(best.getTests().isEmpty());

		assertEquals(7, best.getNumOfCoveredGoals());
		assertEquals(0, best.getNumOfNotCoveredGoals());
	}
}