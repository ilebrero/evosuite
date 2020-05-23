/**
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
package org.evosuite.symbolic.solver;

import static org.evosuite.symbolic.SymbolicObserverTest.printConstraints;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.symbolic.PathConditionNode;
import org.evosuite.symbolic.dse.ConcolicEngine;
import org.evosuite.symbolic.PathCondition;
import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.expr.Variable;
import org.evosuite.symbolic.expr.bv.IntegerConstant;
import org.evosuite.symbolic.expr.bv.IntegerVariable;
import org.evosuite.symbolic.vm.ConstraintFactory;
import org.evosuite.symbolic.vm.ExpressionFactory;
import org.evosuite.testcase.DefaultTestCase;

public abstract class DefaultTestCaseConcolicExecutor {

	public static Collection<Constraint<?>> execute(DefaultTestCase tc) {
		List<PathConditionNode> pc = getPathCondition(tc);

		Set<Variable<?>> variables = new HashSet<Variable<?>>();
		Collection<Constraint<?>> constraints = new LinkedList<Constraint<?>>();
		for (PathConditionNode condition : pc) {
			constraints.addAll(condition.getSupportingConstraints());
			Constraint<?> constraint = condition.getConstraint();
			constraints.add(constraint);
			variables.addAll(constraint.getVariables());
		}
		for (Variable<?> variable : variables) {
			if (variable instanceof IntegerVariable) {
				IntegerVariable integerVariable = (IntegerVariable) variable;
				IntegerConstant minValue = ExpressionFactory.buildNewIntegerConstant(integerVariable.getMinValue());
				IntegerConstant maxValue = ExpressionFactory.buildNewIntegerConstant(integerVariable.getMaxValue());
				constraints.add(ConstraintFactory.gte(integerVariable, minValue));
				constraints.add(ConstraintFactory.lte(integerVariable, maxValue));
			}
		}

		return constraints;
	}

	private static List<PathConditionNode> getPathCondition(DefaultTestCase tc) {
		Properties.CLIENT_ON_THREAD = true;
		Properties.PRINT_TO_SYSTEM = true;
		Properties.TIMEOUT = 5000;
		Properties.CONCOLIC_TIMEOUT = 5000000;

		System.out.println("TestCase=");
		System.out.println(tc.toCode());

		PathCondition pc = new ConcolicEngine().execute(tc);
		List<PathConditionNode> branch_conditions = pc.getPathConditionNodes();

		printConstraints(branch_conditions);
		return branch_conditions;
	}

}
