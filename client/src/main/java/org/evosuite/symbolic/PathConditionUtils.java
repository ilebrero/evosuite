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
package org.evosuite.symbolic;

import org.evosuite.symbolic.expr.Constraint;
import org.evosuite.symbolic.expr.Expression;
import org.evosuite.symbolic.expr.Variable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *  For now it holds the logic for creating the next path to explore.
 *  Notice that it's a bit mixed with creating the query for the smt solver, it can be
 *  reworked when creating the DSE exclusive entry.
 *  TODO: in the future it could a good idea to avoid using static objects and move
 * 		 to a dependency injection schema.
 *
 * @author ilebrero
 *
 */
public abstract class PathConditionUtils {
	/**
	 * Creates a Solver query give a branch condition
	 * TODO: Deciding which is the next path to explore is done here, refactor
	 *
	 * @param pc
	 * @param conditionIndexToNegate
	 * @return
	 */
	public static List<Constraint<?>> buildNextPathToExploreConstraints(PathCondition pc, int conditionIndexToNegate) {
		// negate target branch condition
		if (conditionIndexToNegate < 0 || conditionIndexToNegate >= pc.getBranchConditions().size()) {
			throw new IndexOutOfBoundsException("The position " + conditionIndexToNegate + " does not exists");
		}

		List<Constraint<?>> query = new LinkedList<Constraint<?>>();
		for (int i = 0; i < conditionIndexToNegate; i++) {
			BranchCondition b = pc.get(i);
			query.addAll(b.getSupportingConstraints());
			query.add(b.getConstraint());
		}

		BranchCondition targetBranch = pc.get(conditionIndexToNegate);
		Constraint<?> negation = targetBranch.getConstraint().negate();
		query.addAll(targetBranch.getSupportingConstraints());
		query.add(negation);

		// Compute cone of influence reduction
		List<Constraint<?>> simplified_query = reduce(query);

		return simplified_query;
	}


	/**
	 * Apply cone of influence reduction to constraints with respect to the last
	 * constraint in the list
	 *
	 * @param constraints
	 * @return
	 */
	private static List<Constraint<?>> reduce(List<Constraint<?>> constraints) {

		Constraint<?> target = constraints.get(constraints.size() - 1);
		Set<Variable<?>> dependencies = getVariables(target);

		LinkedList<Constraint<?>> coi = new LinkedList<Constraint<?>>();
		if (dependencies.size() <= 0)
			return coi;

		coi.add(target);

		for (int i = constraints.size() - 2; i >= 0; i--) {
			Constraint<?> constraint = constraints.get(i);
			Set<Variable<?>> variables = getVariables(constraint);
			for (Variable<?> var : dependencies) {
				if (variables.contains(var)) {
					dependencies.addAll(variables);
					coi.addFirst(constraint);
					break;
				}
			}
		}
		return coi;
	}

	/**
	 * Determine the set of variable referenced by this constraint
	 *
	 * @param constraint
	 * @return
	 */
	private static Set<Variable<?>> getVariables(Constraint<?> constraint) {
		Set<Variable<?>> variables = new HashSet<Variable<?>>();
		getVariables(constraint.getLeftOperand(), variables);
		getVariables(constraint.getRightOperand(), variables);
		return variables;
	}

	/**
	 * Recursively determine constraints in expression
	 *
	 * @param expr
	 *            a {@link org.evosuite.symbolic.expr.Expression} object.
	 * @param variables
	 *            a {@link java.util.Set} object.
	 */
	private static void getVariables(Expression<?> expr, Set<Variable<?>> variables) {
		variables.addAll(expr.getVariables());
	}
}
