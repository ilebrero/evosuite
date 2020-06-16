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
package org.evosuite.utils;

import org.objectweb.asm.Type;

/**
 * Utils for ASM type related operations.
 *
 * @author Ignacio Lebrero
 */
public class TypeUtil {

  public static boolean isValue(Type t) {
    return isBv32(t) || isBv64(t) || isFp32(t) || isFp64(t);
  }

  public static boolean isFp64(Type t) {
    return t.equals(Type.DOUBLE_TYPE);
  }

  public static boolean isFp32(Type t) {
    return t.equals(Type.FLOAT_TYPE);
  }

  public static boolean isBv64(Type t) {
    return t.equals(Type.LONG_TYPE);
  }

  public static boolean isBv32(Type t) {
    return t.equals(Type.CHAR_TYPE) || t.equals(Type.BOOLEAN_TYPE) || t.equals(Type.SHORT_TYPE)
        || t.equals(Type.BYTE_TYPE) || t.equals(Type.INT_TYPE);
  }

  public static boolean isIntegerValue(Type t) {
    return isBv32(t) || isBv64(t);
  }

  public static boolean isRealValue(Type t) {
    return isFp32(t) || isFp64(t);
  }
}
