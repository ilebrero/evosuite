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
package org.evosuite.symbolic;

import org.evosuite.symbolic.expr.Constraint;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Utils related to the path condition
 *
 * @author Ignacio Lebrero
 */
public class PathConditionUtils {

	public static final String NEW_PATH_CONDITION_CANNOT_BE_NULL 						 = "New path condition cannot be null.";
	public static final String QUERY_CANNOT_BE_NULL_EXCEPTION_MESSAGE 			 = "Query cannot be null.";
	public static final String QUERIES_CANNOT_BE_NULL_EXCEPTION_MESSAGE 		 = "Queries cannot be null.";
	public static final String TOTAL_AMOUNT_OF_PATHS_HAS_TO_BE_HIGHER_THAN_0 = "Total amount of paths has to be higher than 0.";
	public static final String EXPECTED_PREFIX_PATH_CONDITION_CANNOT_BE_NULL = "Expected prefix path condition cannot be null.";
	public static final String PATH_AMOUNTS_CANNOT_BE_NEGATIVE = "Path amounts cannot be negative";


	/**
	 * Returns true if the constraints in the query are a subset of any of the constraints in the set
	 * of queries.
	 *
	 * @param query
	 * @param queries
	 * @return
	 */
	public static boolean isConstraintSetSubSetOf(Set<Constraint<?>> query, Collection<Set<Constraint<?>>> queries) {
		if (query == null) throw new IllegalArgumentException(QUERY_CANNOT_BE_NULL_EXCEPTION_MESSAGE);
		if (queries == null) throw new IllegalArgumentException(QUERIES_CANNOT_BE_NULL_EXCEPTION_MESSAGE);

		for (Set<Constraint<?>> pathCondition : queries) {
			if (pathCondition.containsAll(query)) {
				return true;
			}
		}

		return false;
	}

  /**
	 * Checks whether the current path condition explored has diverged from it's supposed path.
	 * We simply check that the original path condition from which the new test was created
	 * is a prefix of the actual path being explored.
	 *
	 * TODO: see how to cite this paper properly.
	 * Idea taken from: An empirical investigation into path divergences for
	 * 									concolic execution using CREST,
	 *  								Ting Chen, Xiaodong Lin, Jin Huang, Abel Bacchus and Xiaosong Zhang
	 *
	 *  TODO: In the future we can support explaining which condition diverged.
	 *  		And Maybe talk about what happened, it's doesn't seem to be trivial to
	 *  	    check where the divergence generated but there may be an aprox. technique that
	 *  	    may give us some information about it.
	 *
	 * @param expectedPrefixPathCondition
	 * @param newPathCondition
	 * @return
	 */
	public static boolean hasPathConditionDiverged(PathCondition expectedPrefixPathCondition, PathCondition newPathCondition) {
		if (expectedPrefixPathCondition == null) throw new IllegalArgumentException(
			EXPECTED_PREFIX_PATH_CONDITION_CANNOT_BE_NULL);

		if (newPathCondition == null) throw new IllegalArgumentException(
			NEW_PATH_CONDITION_CANNOT_BE_NULL);

		List<PathConditionNode> expectedPrefixPathConditionNodes = expectedPrefixPathCondition.getPathConditionNodes();
		List<PathConditionNode> newPathConditionNodes = newPathCondition.getPathConditionNodes();

		// If the path is is less than the current one, we are in a trivial divergence
		if (expectedPrefixPathCondition.size() > newPathConditionNodes.size()) return true;

		for (int currentBranchConditionIndex = 0; currentBranchConditionIndex < expectedPrefixPathConditionNodes.size(); ++currentBranchConditionIndex) {
			PathConditionNode expectedPrefixPathConditionNode = expectedPrefixPathConditionNodes.get(currentBranchConditionIndex);
			PathConditionNode newPathConditionNode = newPathConditionNodes.get(currentBranchConditionIndex);

			// if the expectedPrefix path is not a prefix of the new one, there's a divergence
			if (!expectedPrefixPathConditionNode.equals(newPathConditionNode)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates the path divergence vs total paths ratio.
	 *
	 * @param pathDivergeAmount
	 * @param totalPathsAmount
	 * @return
	 */
	public static double calculatePathDivergenceRatio(int pathDivergeAmount, int totalPathsAmount) {
		if (totalPathsAmount == 0) throw new ArithmeticException(TOTAL_AMOUNT_OF_PATHS_HAS_TO_BE_HIGHER_THAN_0);
		if (pathDivergeAmount < 0 || totalPathsAmount < 0) throw new IllegalArgumentException(PATH_AMOUNTS_CANNOT_BE_NEGATIVE);

		double divergenceAmount = pathDivergeAmount;
		double pathsAmount = totalPathsAmount;

		return divergenceAmount / pathsAmount;
	}
}
