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
package org.evosuite.symbolic.DSE.algorithm.strategies.implementations.TestCaseSelectionStrategies;

import org.evosuite.symbolic.DSE.algorithm.strategies.TestCaseSelectionStrategy;
import org.evosuite.testcase.TestCase;

import java.util.List;

/**
 * Strategy for retrieving the last generated tests case for symbolic execution in the current iteration.
 *
 * @author ignacio lebrero
 */
public class LastTestCaseSelectionStrategy implements TestCaseSelectionStrategy {
    @Override
    public TestCase getCurrentIterationBasedTestCase(List<TestCase> generatedTests) {
        return generatedTests.get(
                generatedTests.size() - 1
        );
    }
}
