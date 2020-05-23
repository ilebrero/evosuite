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
package org.evosuite.testcase.utils;

import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.statements.ArrayStatement;
import org.evosuite.testcase.statements.PrimitiveStatement;
import org.evosuite.testcase.statements.Statement;
import org.evosuite.testcase.variable.ArraySymbolicLengthName;
import org.evosuite.utils.Randomness;
import org.objectweb.asm.Type;
import org.evosuite.symbolic.TestCaseBuilder;
import org.evosuite.testcase.DefaultTestCase;
import org.evosuite.testcase.variable.VariableReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * Test case helper methods.
 *
 * @author Ignacio Lebrero
 */
public class TestCaseUtils {

  private static final Logger logger = LoggerFactory.getLogger(TestCaseUtils.class);

  @SuppressWarnings({ "rawtypes", "unchecked" })
	public static TestCase updateTest(TestCase test, Map<String, Object> updatedValues) {

		TestCase newTest = test.clone();
		newTest.clearCoveredGoals();

		for (String symbolicVariableName : updatedValues.keySet()) {
			Object updateValue = updatedValues.get(symbolicVariableName);
			if (updateValue != null) {
				logger.info("New value: " + symbolicVariableName + ": " + updateValue);

				//It's a dimension of the array's length
				if (ArraySymbolicLengthName.isArraySymbolicLengthVariableName(symbolicVariableName)) {
          ArraySymbolicLengthName arraySymbolicLengthName = new ArraySymbolicLengthName(symbolicVariableName);
          processArrayLengthValue(newTest, arraySymbolicLengthName, (Long) updateValue);
        } else if (updateValue instanceof Long) {
					Long value = (Long) updateValue;

					String name = ((String) symbolicVariableName).replace("__SYM", "");
					// logger.warn("New long value for " + name + " is " +
					// value);
					PrimitiveStatement p = (PrimitiveStatement) getStatement(newTest, name, StatementClassChecker.PRIMITIVE_STATEMENT);
					if (p.getValue().getClass().equals(Character.class)) {
						char charValue = (char) value.intValue();
						p.setValue(charValue);
					} else if (p.getValue().getClass().equals(Long.class)) {
						p.setValue(value);
					} else if (p.getValue().getClass().equals(Integer.class)) {
						p.setValue(value.intValue());
					} else if (p.getValue().getClass().equals(Short.class)) {
						p.setValue(value.shortValue());
					} else if (p.getValue().getClass().equals(Boolean.class)) {
						p.setValue(value.intValue() > 0);
					} else if (p.getValue().getClass().equals(Byte.class)) {
						p.setValue(value.byteValue());

					} else
						logger.warn("New value is of an unsupported type: " + p.getValue().getClass() + updateValue);
				} else if (updateValue instanceof String) {
					String name = ((String) symbolicVariableName).replace("__SYM", "");
					PrimitiveStatement p = getPrimitiveStatement(newTest, name);
					// logger.warn("New string value for " + name + " is " +
					// val);
					assert (p != null) : "Could not find variable " + name + " in test: " + newTest.toCode()
							+ " / Orig test: " + test.toCode() + ", seed: " + Randomness.getSeed();
					if (p.getValue().getClass().equals(Character.class))
						p.setValue((char) Integer.parseInt(updateValue.toString()));
					else
						p.setValue(updateValue.toString());
				} else if (updateValue instanceof Double) {
					Double value = (Double) updateValue;
					String name = ((String) symbolicVariableName).replace("__SYM", "");
					PrimitiveStatement p = getPrimitiveStatement(newTest, name);
					// logger.warn("New double value for " + name + " is " +
					// value);
					assert (p != null) : "Could not find variable " + name + " in test: " + newTest.toCode()
							+ " / Orig test: " + test.toCode() + ", seed: " + Randomness.getSeed();

					if (p.getValue().getClass().equals(Double.class))
						p.setValue(value);
					else if (p.getValue().getClass().equals(Float.class))
						p.setValue(value.floatValue());
					else
						logger.warn("New value is of an unsupported type: " + updateValue);
				} else {
					logger.debug("New value is of an unsupported type: " + updateValue);
				}
			} else {
				logger.debug("New value is null");

			}
		}
		return newTest;

	}

  /**
   * Updates the length of an array
   *
   * @param newTest
   * @param arraySymbolicLengthName
   * @param updateValue
   */
  private static void processArrayLengthValue(TestCase newTest, ArraySymbolicLengthName arraySymbolicLengthName, Long updateValue) {
    ArrayStatement arrayStatement = (ArrayStatement) getStatement(newTest, arraySymbolicLengthName.getArrayReferenceName(), StatementClassChecker.ARRAY_STATEMENT);
    arrayStatement.setLength(
      updateValue.intValue(),
      arraySymbolicLengthName.getDimension()
    );
  }

  /**
   * Builds a default test case for a static target method
   *
   * @param targetStaticMethod
   * @return
   */
  public static DefaultTestCase buildTestCaseWithDefaultValues(Method targetStaticMethod) {
    TestCaseBuilder testCaseBuilder = new TestCaseBuilder();

    Type[] argumentTypes = Type.getArgumentTypes(targetStaticMethod);
    Class<?>[] argumentClasses = targetStaticMethod.getParameterTypes();

    ArrayList<VariableReference> arguments = new ArrayList<VariableReference>();
    for (int i = 0; i < argumentTypes.length; i++) {

      Type argumentType = argumentTypes[i];
      Class<?> argumentClass = argumentClasses[i];

      switch (argumentType.getSort()) {
        case Type.BOOLEAN: {
          VariableReference booleanVariable = testCaseBuilder.appendBooleanPrimitive(false);
          arguments.add(booleanVariable);
          break;
        }
        case Type.BYTE: {
          VariableReference byteVariable = testCaseBuilder.appendBytePrimitive((byte) 0);
          arguments.add(byteVariable);
          break;
        }
        case Type.CHAR: {
          VariableReference charVariable = testCaseBuilder.appendCharPrimitive((char) 0);
          arguments.add(charVariable);
          break;
        }
        case Type.SHORT: {
          VariableReference shortVariable = testCaseBuilder.appendShortPrimitive((short) 0);
          arguments.add(shortVariable);
          break;
        }
        case Type.INT: {
          VariableReference intVariable = testCaseBuilder.appendIntPrimitive(0);
          arguments.add(intVariable);
          break;
        }
        case Type.LONG: {
          VariableReference longVariable = testCaseBuilder.appendLongPrimitive(0L);
          arguments.add(longVariable);
          break;
        }
        case Type.FLOAT: {
          VariableReference floatVariable = testCaseBuilder.appendFloatPrimitive((float) 0.0);
          arguments.add(floatVariable);
          break;
        }
        case Type.DOUBLE: {
          VariableReference doubleVariable = testCaseBuilder.appendDoublePrimitive(0.0);
          arguments.add(doubleVariable);
          break;
        }
        case Type.ARRAY: {
          VariableReference arrayVariable = testCaseBuilder.appendArrayStmt(argumentClass, buildDimensionsArray(argumentType));
          arguments.add(arrayVariable);
          break;
        }
        case Type.OBJECT: {
          if (argumentClass.equals(String.class)) {
            VariableReference stringVariable = testCaseBuilder.appendStringPrimitive("");
            arguments.add(stringVariable);
          } else {
            VariableReference objectVariable = testCaseBuilder.appendNull(argumentClass);
            arguments.add(objectVariable);
          }
          break;
        }
        default: {
          throw new UnsupportedOperationException();
        }
      }
    }

    testCaseBuilder.appendMethod(null, targetStaticMethod,
        arguments.toArray(new VariableReference[] {}));
    DefaultTestCase testCase = testCaseBuilder.getDefaultTestCase();

    return testCase;
  }


  /**
   * Creates the lengths array required to create the array referenes
   *
   * @param argumentType
   * @return
   */
  public static int[] buildDimensionsArray(Type argumentType) {
    int dimensions = argumentType.getDimensions();
    int[] lengths = new int[dimensions];

    for (int dimension = 0; dimension < dimensions; ++dimension) {
      lengths[dimension] = 0;
    }

    return lengths;
  }

  /**
	 * Get the statement that defines this variable
	 *
	 * @param test
	 * @param name
	 * @return
	 */
	public static PrimitiveStatement<?> getPrimitiveStatement(TestCase test, String name) {
		for (Statement statement : test) {

			if (statement instanceof PrimitiveStatement<?>) {
				if (statement.getReturnValue().getName().equals(name))
					return (PrimitiveStatement<?>) statement;
			}
		}
		return null;
	}

	  /**
	 * Get the statement that defines this variable
	 *
	 * @param test
	 * @param name
	 * @param typeCheckFunction
     * @return
	 */
	public static Statement getStatement(TestCase test, String name, StatementClassChecker typeCheckFunction) {
		for (Statement statement : test) {
			if (typeCheckFunction.checkClassType(statement)) {
				if (statement.getReturnValue().getName().equals(name))
					return statement;
			}
		}
		return null;
	}
}
