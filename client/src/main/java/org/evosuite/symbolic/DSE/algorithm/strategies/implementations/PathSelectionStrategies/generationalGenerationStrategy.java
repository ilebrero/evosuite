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
package org.evosuite.symbolic.DSE.algorithm.strategies.implementations.PathSelectionStrategies;

import org.evosuite.symbolic.BranchCondition;
import org.evosuite.symbolic.DSE.algorithm.strategies.PathSelectionStrategy;
import org.evosuite.symbolic.PathCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * SAGE based strategy, creating all the children negating all the branches of the current path condition.
 * TODO: TEST THIS
 *
 * @author ignacio lebrero
 */
public class generationalGenerationStrategy implements PathSelectionStrategy {
    @Override
    public List<PathCondition> generateChildren(PathCondition currentPathCondition) {
        List<PathCondition> generatedChildren = new ArrayList<>();
        List<BranchCondition> acumulatedBranchConditions = new ArrayList<>();

        for (BranchCondition currentBranchCondition : currentPathCondition.getBranchConditions()) {
            acumulatedBranchConditions.add(currentBranchCondition.getNegatedVersion());

            // adds the negated last condition
            generatedChildren.add(
                new PathCondition(
                        new ArrayList(acumulatedBranchConditions)
                )
            );

            // replaces the negated branch condition with the original one for continuing generating
            acumulatedBranchConditions.set(
                acumulatedBranchConditions.size() - 1,
                currentBranchCondition
            );
        }

        return generatedChildren;
    }
}
