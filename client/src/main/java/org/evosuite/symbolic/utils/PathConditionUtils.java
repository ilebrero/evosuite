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
package org.evosuite.symbolic.utils;

import org.evosuite.symbolic.BranchCondition;
import org.evosuite.symbolic.PathCondition;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Utils for path condition related things
 * TODO: move to a depency injection when updated the project with guice
 *
 * @author ignacio lebrero
 */
public abstract class PathConditionUtils {
//    public static PathCondition negatenthBranchCondition(PathCondition pathCondition, int branchConditionPosition) {
//        List<BranchCondition> branchConditions = pathCondition.getBranchConditions();
//        BranchCondition nthBranchCondition = branchConditions.get(branchConditionPosition);
//
//        try {
//            BranchCondition negatedBranchCondition = nthBranchCondition.getClass().getConstructor().;
//        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
}