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

import org.evosuite.symbolic.expr.bv.IntegerValue;
import org.evosuite.symbolic.expr.fp.RealValue;
import org.evosuite.symbolic.expr.ref.ReferenceConstant;
import org.evosuite.symbolic.expr.ref.ReferenceExpression;
import org.evosuite.symbolic.expr.ref.ReferenceVariable;
import org.evosuite.symbolic.expr.str.StringValue;
import org.evosuite.symbolic.vm.heap.SymbolicArray;
import org.evosuite.symbolic.vm.heap.SymbolicArrayImpl;
import org.evosuite.symbolic.vm.heap.SymbolicInputArray;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Arrays implementation using lazy variables initialization for arrays that are inputs for the SUT.
 *
 * @author Ignacio Lebrero
 */
public class LazyArraysImpl implements SymbolicHeapArraySection {

  /**
   * Symbolic Arrays Memory model
   *
   * TODO: Implement Strings and References
   */
	private final Map<ReferenceExpression, SymbolicArray> symb_arrays = new HashMap();

  @Override
  public ReferenceExpression arrayLoad(ReferenceExpression symb_array, IntegerValue symb_index, ReferenceExpression symb_value) {
		int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents;

//		if (symb_value.isString()) {
//			return arrayLoadString(symb_array, symb_index, symb_value);
//		} else {
			symb_array_contents = getOrCreateSymbolicArray(symb_array);
//		}
//
		return (ReferenceExpression) symb_array_contents.get(conc_index);
	}

	@Override
	public IntegerValue arrayLoad(ReferenceExpression symb_array, IntegerValue symb_index, IntegerValue symbolicValue) {
		int conc_index = Math.toIntExact(symb_index.getConcreteValue());

		SymbolicArray symb_array_contents = getOrCreateSymbolicArray(symb_array);
		return (IntegerValue) symb_array_contents.get(conc_index);
	}

  @Override
	public RealValue arrayLoad(ReferenceExpression symb_array, IntegerValue symb_index, RealValue symbolicValue) {
		int conc_index = Math.toIntExact(symb_index.getConcreteValue());

		SymbolicArray symb_array_contents = getOrCreateSymbolicArray(symb_array);
		return (RealValue) symb_array_contents.get(conc_index);
	}

  @Override
  public StringValue arrayLoad(ReferenceExpression symb_array, IntegerValue symb_index, StringValue symb_value) {
		int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents;

		symb_array_contents = getOrCreateSymbolicArray(symb_array);
		StringValue arrayContent = (StringValue) symb_array_contents.get(conc_index);

//		//In case the array created a new String variable, we create the reference to it.
//		if (! inv_symb_strings_expressions.keySet().contains(arrayContent)) {
//
//			symb_strings_expressions.put(
//				symb_value,
//				arrayContent
//			);
//
//			inv_symb_strings_expressions.put(
//				arrayContent,
//				symb_value
//			);
//		} else {
//			symb_value = inv_symb_strings_expressions.get(arrayContent);
//		}

		return symb_value;
	}
  @Override
	public void arrayStore(Object conc_array, ReferenceExpression symb_array, IntegerValue symb_index,
												 IntegerValue symb_value) {
    int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents = getOrCreateSymbolicArray(symb_array);

		symb_array_contents.set(conc_index, symb_value);
	}

	@Override
	public void arrayStore(Object conc_array, ReferenceExpression symb_array, IntegerValue symb_index,
												 RealValue symb_value) {
    int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents = getOrCreateSymbolicArray(symb_array);
		symb_array_contents.set(conc_index, symb_value);
	}

	@Override
	public void arrayStore(Object conc_array, ReferenceExpression symb_array, IntegerValue symb_index,
												 StringValue symb_value) {
    int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents = getOrCreateSymbolicArray(symb_array);
		symb_array_contents.set(conc_index, symb_value);
	}

	@Override
	public void arrayStore(Object conc_array, ReferenceExpression symb_array, IntegerValue symb_index,
												 ReferenceExpression symb_value) {
    int conc_index = Math.toIntExact(symb_index.getConcreteValue());
		SymbolicArray symb_array_contents;

//		if (symb_value.isString()) {
//			arrayStore(conc_array, symb_array, conc_index, symb_value);
//			symb_array_contents = getOrCreateSymbolicArray(symb_array, String.class);
//			// NOTE: as strings are immutable in Java, we represent it as a constant.
//			//       In case an operation takes place, a new array will be created.
//			symb_array_contents.set(conc_index, new StringConstant((String) symb_array.getConcreteValue()));
////			getOrCreateSymbolicField(symb_array, String.valueOf(conc_index));
//		} else {
			symb_array_contents = getOrCreateSymbolicArray(symb_array);
			symb_array_contents.set(conc_index, symb_value);
//		}
	}
//
//  @Override
//  public void createVariableArray(ReferenceVariable symbolicArrayReference) {
//    SymbolicArray symbolicArray = getOrCreateSymbolicArray(symbolicArrayReference);
//
//		if (symbolicArray.getClass().getName().equals(SymbolicInputArray.class)) {
//			// This should never happens
//			throw new IllegalArgumentException("Symbolic array already upgraded, please check the symbolic heap state.");
//		}
//
//		symb_arrays.put(symbolicArrayReference, new SymbolicInputArray(symbolicArray, symbolicArrayReference.getName()));
//  }

  @Override
  public ReferenceVariable createVariableArray(Object concreteArray, int instanceId, String arrayName) {
    ReferenceVariable symbolicArrayVariableReference = new ReferenceVariable(
      Type.getType(concreteArray.getClass()),
      instanceId,
      arrayName,
      concreteArray
    );

    SymbolicArray symbolicArray = getOrCreateSymbolicArray(symbolicArrayVariableReference);
    symb_arrays.put(symbolicArrayVariableReference, new SymbolicInputArray(symbolicArray, arrayName));

    return symbolicArrayVariableReference;
  }

  @Override
  public ReferenceConstant createConstantArray(Type arrayType, int instanceId) {
    ReferenceConstant symbolicArrayVariableReference = new ReferenceConstant(
      arrayType,
      instanceId
    );

    SymbolicArray symbolicArray = getOrCreateSymbolicArray(symbolicArrayVariableReference);
    assert(symbolicArray != null);

    return symbolicArrayVariableReference;
  }

  @Override
  public void initializeArrayReference(ReferenceExpression symbolicArrayReference) {
    symb_arrays.put(
      symbolicArrayReference,
      getOrCreateSymbolicArray(symbolicArrayReference)
    );
  }

  /** Creational section */
	private SymbolicArray getOrCreateSymbolicArray(ReferenceExpression symb_array_ref) {
		Type contentType = symb_array_ref.getObjectType().getElementType();
		SymbolicArray symb_array_contents = symb_arrays.get(symb_array_ref);

		if (symb_array_contents == null) {
			symb_array_contents = new SymbolicArrayImpl(contentType);
			symb_arrays.put(symb_array_ref, symb_array_contents);
		}

		return symb_array_contents;
	}
}
