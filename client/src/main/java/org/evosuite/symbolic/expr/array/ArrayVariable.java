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
package org.evosuite.symbolic.expr.array;

import org.evosuite.symbolic.expr.AbstractExpression;
import org.evosuite.symbolic.expr.ExpressionVisitor;
import org.evosuite.symbolic.expr.Variable;
import org.evosuite.symbolic.expr.ref.ReferenceExpression;
import org.evosuite.testcase.variable.ArrayReference;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

 /**
   * Represents an instance of an array.
   *
   * @author Ignacio Lebrero
   */
public final class ArrayVariable {

   public static final class IntegerArrayVariable extends AbstractExpression<Object> implements
    ArrayValue.IntegerArrayValue, Variable<Object> {

     private final ArrayReference arrayReference;
     private final ReferenceExpression symbolicArrayReference;

     public IntegerArrayVariable(ArrayReference arrayReference, ReferenceExpression symbolicArrayReference) {
       super(symbolicArrayReference.getConcreteValue(), 1, true);

       this.arrayReference = arrayReference;
       this.symbolicArrayReference = symbolicArrayReference;
     }

     public Set<Variable<?>> getVariables() {
       Set<Variable<?>> variables = new HashSet<Variable<?>>();
       variables.add(this);
       return variables;
     }

     public String toString() {
       return arrayReference.getName();
     }

     @Override
     public <K, V> K accept(ExpressionVisitor<K, V> v, V arg) {
       return v.visit(this, arg);
     }

     @Override
     public String getName() {
       return this.arrayReference.getName();
     }

     @Override
     public Object getMinValue() {
       // Arrays doesn't contains min value, this shouldn't be called
       return null;
     }

     @Override
     public Object getMaxValue() {
       // Arrays doesn't contains min value, this shouldn't be called
       return null;
     }

     public void setConcreteValue(Object concreteValue) {
       this.concreteValue = concreteValue;
     }

     public ArrayReference getArrayReference() {
       return arrayReference;
     }

     public ReferenceExpression getSymbolicArrayReference() {
       return symbolicArrayReference;
     }
   }

   public static final class RealArrayVariable extends AbstractExpression<Object> implements
    ArrayValue.RealArrayValue, Variable<Object> {

     private final ArrayReference arrayReference;
     private final ReferenceExpression symbolicArrayReference;

     public RealArrayVariable(ArrayReference arrayReference, ReferenceExpression symbolicArrayReference) {
       super(symbolicArrayReference.getConcreteValue(), 1, true);

       this.arrayReference = arrayReference;
       this.symbolicArrayReference = symbolicArrayReference;
     }

     public Set<Variable<?>> getVariables() {
       Set<Variable<?>> variables = new HashSet<Variable<?>>();
       variables.add(this);
       return variables;
     }

     public String toString() {
       return arrayReference.getName();
     }

     @Override
     public <K, V> K accept(ExpressionVisitor<K, V> v, V arg) {
       return v.visit(this, arg);
     }

     @Override
     public String getName() {
       return this.arrayReference.getName();
     }

     @Override
     public Object getMinValue() {
       // Arrays doesn't contains min value, this shouldn't be called
       return null;
     }

     @Override
     public Object getMaxValue() {
       // Arrays doesn't contains min value, this shouldn't be called
       return null;
     }

     public void setConcreteValue(Object concreteValue) {
       this.concreteValue = concreteValue;
     }

     public ArrayReference getArrayReference() {
       return arrayReference;
     }

     public ReferenceExpression getSymbolicArrayReference() {
       return symbolicArrayReference;
     }
   }

 }
