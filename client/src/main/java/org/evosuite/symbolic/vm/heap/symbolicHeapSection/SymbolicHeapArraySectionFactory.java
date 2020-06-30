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
package org.evosuite.symbolic.vm.heap.symbolicHeapSection;

import org.evosuite.Properties;

/**
 * Factory for array memory models used in the symbolic heap.
 *
 * @author Ignacio Lebrero
 */
public class SymbolicHeapArraySectionFactory {
  private final static String DSE_ARRAYS_MEMORY_MODEL_NOT_PROVIDED = "An array memory model type must be provided.";

  public static SymbolicHeapArraySection getSymbolicHeapArray(Properties.DSE_ARRAYS_MEMORY_MODEL_VERSION arraysMemoryModelVersion) {
    if (arraysMemoryModelVersion == null) {
      throw new IllegalArgumentException(DSE_ARRAYS_MEMORY_MODEL_NOT_PROVIDED);
    }

    switch (arraysMemoryModelVersion) {
      case LAZY_VARIABLES:
        return new LazyArraysImpl();
      case ARRAYS_THEORY:
        return new ArrayTheoryImpl();
      default:
        throw new IllegalStateException("Arrays memory model not yet implemented: " + arraysMemoryModelVersion.name());
    }
  }
}
