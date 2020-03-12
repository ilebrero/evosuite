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
package org.evosuite.symbolic.DSE.algorithm.listener.implementations;

import org.evosuite.ga.Chromosome;
import org.evosuite.symbolic.DSE.algorithm.DSEBaseAlgorithm;
import org.evosuite.symbolic.DSE.algorithm.listener.StoppingCondition;

import java.io.Serializable;

/**
 * Adaptation of {@link org.evosuite.ga.stoppingconditions.StoppingConditionImpl} for the DSE module.
 *
 * @author Ignacio Lebrero
 */
public abstract class StoppingConditionImpl implements StoppingCondition, Serializable {

    private static final long serialVersionUID = 6062248039291236657L;

    public StoppingConditionImpl() {
        reset();
    }


    @Override
    public void iteration(DSEBaseAlgorithm algorithm) {
        //Nothing
    }

    @Override
    public void searchFinished(DSEBaseAlgorithm algorithm) {
        //Nothing
    }

    @Override
    public void fitnessEvaluation(Chromosome individual) {
        //Nothing
    }

    @Override
    public void searchStarted(DSEBaseAlgorithm algorithm) {
        //Nothing
    }
}
