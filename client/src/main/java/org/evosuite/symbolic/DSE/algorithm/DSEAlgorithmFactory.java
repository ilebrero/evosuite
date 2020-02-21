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
package org.evosuite.symbolic.DSE.algorithm;

import org.evosuite.symbolic.DSE.algorithm.implementations.DSEAlgorithmPaper1;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory of DSE Algorithms
 *
 * @author ilebrero
 */
public class DSEAlgorithmFactory {
    private final static Map<DSEAlgorithmType, Supplier<DSEAlgorithm>> algorithms = new HashMap();

    private static final String DSE_ALGORITHM_TYPE_NOT_PROVIDED = "A DSE algorithm type must be provided";

    static {
        algorithms.put(
                DSEAlgorithmType.PAPER1, DSEAlgorithmPaper1::new
        );
    }

    public static DSEAlgorithm getDSEAlgorithm(DSEAlgorithmType dseAlgorithmType) {
        if (dseAlgorithmType == null) {
            throw new IllegalArgumentException(DSE_ALGORITHM_TYPE_NOT_PROVIDED);
        }

        Supplier<DSEAlgorithm> dseAlgorithm = algorithms.get(dseAlgorithmType);
        return dseAlgorithm.get();
    }
}

