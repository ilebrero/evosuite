package org.evosuite.dse;

import com.examples.with.different.packagename.dse.invokedynamicdsc.instrument.SingleMethodReference;
import com.examples.with.different.packagename.dse.lambda.ClosureExample;
import com.examples.with.different.packagename.dse.lambda.LambdaExample;
import org.apache.commons.lang3.NotImplementedException;
import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.symbolic.dse.algorithm.ExplorationAlgorithmBase;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DSEInvokeDynamicSystemTest extends DSESystemTestBase {

    /** Lambdas (JDK 8) */

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
	public void testClosure() {
		EvoSuite evosuite = new EvoSuite();
		String targetClass = ClosureExample.class.getCanonicalName();
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

	/** Method references (JDK 8) */

	@Test
	public void testMethodReference() {
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

	@Test
	public void testStreamAPI() {
		throw new NotImplementedException("Implement me!");
	}

	@Test
	public void testAutoBoxingConversions() {
		throw new NotImplementedException("Implement me!");
	}

	/** String concatenation (JDK 9) */

	@Test
	public void testConcatenation() {
		throw new NotImplementedException("Implement me!");
	}

	/** Method Handles (JDK 8) */

	// TODO: complete
}
