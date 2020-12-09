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
