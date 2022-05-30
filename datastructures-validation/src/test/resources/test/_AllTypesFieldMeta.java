/*
 * Copyright (C) 2018-2022 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test;

public class _AllTypesFieldMeta {

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<test.AllTypesField> STRINGVALUE = new org.xbib.datastructures.validation.meta.StringConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "stringValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.String> toValue() {
			return x -> x.stringValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesField> BOOLEANVALUE = new org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "booleanValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Boolean> toValue() {
			return x -> x.booleanValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesField> BOOLEANPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "booleanPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Boolean> toValue() {
			return x -> x.booleanPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesField> CHARACTERVALUE = new org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "characterValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Character> toValue() {
			return x -> x.characterValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesField> CHARACTERPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "characterPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Character> toValue() {
			return x -> x.characterPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesField> BYTEVALUE = new org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "byteValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Byte> toValue() {
			return x -> x.byteValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesField> BYTEPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "bytePrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Byte> toValue() {
			return x -> x.bytePrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesField> SHORTVALUE = new org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "shortValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Short> toValue() {
			return x -> x.shortValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesField> SHORTPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "shortPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Short> toValue() {
			return x -> x.shortPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesField> INTEGERVALUE = new org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "integerValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Integer> toValue() {
			return x -> x.integerValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesField> INTEGERPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "integerPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Integer> toValue() {
			return x -> x.integerPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesField> LONGVALUE = new org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "longValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Long> toValue() {
			return x -> x.longValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesField> LONGPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "longPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Long> toValue() {
			return x -> x.longPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesField> FLOATVALUE = new org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "floatValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Float> toValue() {
			return x -> x.floatValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesField> FLOATPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "floatPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Float> toValue() {
			return x -> x.floatPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesField> DOUBLEVALUE = new org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "doubleValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Double> toValue() {
			return x -> x.doubleValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesField> DOUBLEPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "doublePrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.lang.Double> toValue() {
			return x -> x.doublePrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BigIntegerConstraintMeta<test.AllTypesField> BIGINTEGERVALUE = new org.xbib.datastructures.validation.meta.BigIntegerConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "bigIntegerValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.math.BigInteger> toValue() {
			return x -> x.bigIntegerValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BigDecimalConstraintMeta<test.AllTypesField> BIGDECIMALVALUE = new org.xbib.datastructures.validation.meta.BigDecimalConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "bigDecimalValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.math.BigDecimal> toValue() {
			return x -> x.bigDecimalValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LocalDateConstraintMeta<test.AllTypesField> LOCALDATEVALUE = new org.xbib.datastructures.validation.meta.LocalDateConstraintMeta<test.AllTypesField>() {

		@Override
		public String name() {
			return "localDateValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesField, java.time.LocalDate> toValue() {
			return x -> x.localDateValue;
		}
	};
}