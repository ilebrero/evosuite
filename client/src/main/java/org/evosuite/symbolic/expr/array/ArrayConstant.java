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

import java.util.HashSet;
import java.util.Set;

public final class ArrayConstant {

	public static final class IntegerArrayConstant extends AbstractExpression<Object> implements ArrayValue.IntegerArrayValue {

		public IntegerArrayConstant(ReferenceExpression symbolicArrayReference) {
			super(symbolicArrayReference.getConcreteValue(), 1, false);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return getConcreteValue().toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ArrayConstant.IntegerArrayConstant) {
				ArrayConstant.IntegerArrayConstant v = (ArrayConstant.IntegerArrayConstant) obj;
				return this.concreteValue.equals(v.concreteValue);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getConcreteValue().hashCode();
		}

		@Override
		public Set<Variable<?>> getVariables() {
			return new HashSet();
		}

		@Override
		public <K, V> K accept(ExpressionVisitor<K, V> v, V arg) {
			return v.visit(this, arg);
		}
	}

	public static final class RealArrayConstant extends AbstractExpression<Object> implements ArrayValue.RealArrayValue {

		public RealArrayConstant(ReferenceExpression symbolicArrayReference) {
			super(symbolicArrayReference.getConcreteValue(), 1, false);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return getConcreteValue().toString();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ArrayConstant.RealArrayConstant) {
				ArrayConstant.RealArrayConstant v = (ArrayConstant.RealArrayConstant) obj;
				return this.concreteValue.equals(v.concreteValue);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getConcreteValue().hashCode();
		}

		@Override
		public Set<Variable<?>> getVariables() {
			return new HashSet();
		}

		@Override
		public <K, V> K accept(ExpressionVisitor<K, V> v, V arg) {
			return v.visit(this, arg);
		}
	}
}

