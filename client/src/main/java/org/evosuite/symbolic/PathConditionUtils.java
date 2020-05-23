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
 * Utils referee to the path condition
 *
 * @author Ignacio Lebrero
 */
public class PathConditionUtils {
    /**
	   * Returns true if the constraints in the query are a subset of any of the constraints in the set
	   * of queries.
	   *
	   * @param query
	   * @param queries
	   * @return
	   */
    public static boolean isConstraintSetSubSetOf(
    	Set<Constraint<?>> query,
		Collection<Set<Constraint<?>>> queries)
	{
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
	 * 		concolic execution using CREST,
	 *  	Ting Chen, Xiaodong Lin, Jin Huang, Abel Bacchus and Xiaosong Zhang
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
		List<PathConditionNode> expectedPrefixPathConditionNodes = expectedPrefixPathCondition.getPathConditionNodes();
		List<PathConditionNode> newPathConditionNodes = newPathCondition.getPathConditionNodes();

		// If the path is is less than the current one, we are in a divergence
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
		double divergenceAmount = pathDivergeAmount;
		double pathsAmount = totalPathsAmount;

		return pathDivergeAmount / totalPathsAmount;
	}
}
