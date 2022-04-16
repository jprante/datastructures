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

public class _AllTypesBeanMeta {

	public static final org.xbib.datastructures.validation.meta.StringConstraintMeta<test.AllTypesBean> STRINGVALUE = new org.xbib.datastructures.validation.meta.StringConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "stringValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.String> toValue() {
			return test.AllTypesBean::getStringValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesBean> BOOLEANVALUE = new org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "booleanValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Boolean> toValue() {
			return test.AllTypesBean::getBooleanValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesBean> BOOLEANPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.BooleanConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "booleanPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Boolean> toValue() {
			return test.AllTypesBean::isBooleanPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesBean> CHARACTERVALUE = new org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "characterValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Character> toValue() {
			return test.AllTypesBean::getCharacterValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesBean> CHARACTERPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.CharacterConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "characterPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Character> toValue() {
			return test.AllTypesBean::getCharacterPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesBean> BYTEVALUE = new org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "byteValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Byte> toValue() {
			return test.AllTypesBean::getByteValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesBean> BYTEPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.ByteConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "bytePrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Byte> toValue() {
			return test.AllTypesBean::getBytePrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesBean> SHORTVALUE = new org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "shortValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Short> toValue() {
			return test.AllTypesBean::getShortValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesBean> SHORTPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.ShortConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "shortPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Short> toValue() {
			return test.AllTypesBean::getShortPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesBean> INTEGERVALUE = new org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "integerValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Integer> toValue() {
			return test.AllTypesBean::getIntegerValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesBean> INTEGERPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.IntegerConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "integerPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Integer> toValue() {
			return test.AllTypesBean::getIntegerPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesBean> LONGVALUE = new org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "longValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Long> toValue() {
			return test.AllTypesBean::getLongValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesBean> LONGPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.LongConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "longPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Long> toValue() {
			return test.AllTypesBean::getLongPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesBean> FLOATVALUE = new org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "floatValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Float> toValue() {
			return test.AllTypesBean::getFloatValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesBean> FLOATPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.FloatConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "floatPrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Float> toValue() {
			return test.AllTypesBean::getFloatPrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesBean> DOUBLEVALUE = new org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "doubleValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Double> toValue() {
			return test.AllTypesBean::getDoubleValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesBean> DOUBLEPRIMITIVEVALUE = new org.xbib.datastructures.validation.meta.DoubleConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "doublePrimitiveValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.lang.Double> toValue() {
			return test.AllTypesBean::getDoublePrimitiveValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BigIntegerConstraintMeta<test.AllTypesBean> BIGINTEGERVALUE = new org.xbib.datastructures.validation.meta.BigIntegerConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "bigIntegerValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.math.BigInteger> toValue() {
			return test.AllTypesBean::getBigIntegerValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.BigDecimalConstraintMeta<test.AllTypesBean> BIGDECIMALVALUE = new org.xbib.datastructures.validation.meta.BigDecimalConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "bigDecimalValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.math.BigDecimal> toValue() {
			return test.AllTypesBean::getBigDecimalValue;
		}
	};

	public static final org.xbib.datastructures.validation.meta.LocalDateConstraintMeta<test.AllTypesBean> LOCALDATEVALUE = new org.xbib.datastructures.validation.meta.LocalDateConstraintMeta<test.AllTypesBean>() {

		@Override
		public String name() {
			return "localDateValue";
		}

		@Override
		public java.util.function.Function<test.AllTypesBean, java.time.LocalDate> toValue() {
			return test.AllTypesBean::getLocalDateValue;
		}
	};
}
