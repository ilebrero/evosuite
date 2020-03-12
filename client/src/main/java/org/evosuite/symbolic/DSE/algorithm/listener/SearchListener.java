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
package org.evosuite.symbolic.DSE.algorithm.listener;

import org.evosuite.ga.Chromosome;
import org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm;

/**
 * Same SearchListener as the GA algorithm.
 *
 * @author Ignacio Lebrero
 */
public interface SearchListener {
    /**
	 * Called when a new search is started
	 *
	 * @param algorithm a {@link org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm} object.
	 */
	public void searchStarted(DSEBaseAlgorithm algorithm);

	/**
	 * Called after each iteration of the search
	 *
	 * @param algorithm a {@link org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm} object.
	 */
	public void iteration(DSEBaseAlgorithm algorithm);

	/**
	 * Called after the last iteration
	 *
	 * @param algorithm a {@link org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm} object.
	 */
	public void searchFinished(DSEBaseAlgorithm algorithm);

	/**
	 * Called after every single fitness evaluation
	 *
	 * @param individual a {@link org.evosuite.ga.Chromosome} object.
	 */
	public void fitnessEvaluation(Chromosome individual);

	/**
	 * Called before a chromosome is mutated
	 *
	 * @param individual a {@link org.evosuite.ga.Chromosome} object.
	 */
	public void modification(Chromosome individual);
}
