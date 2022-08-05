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
package org.xbib.datastructures.validation.constraint;

import java.time.Clock;
import java.time.ZonedDateTime;

import org.xbib.datastructures.validation.constraint.base.ChronoZonedDateTimeConstraintBase;

/**
 * This is the actual class for constraints on ZonedDateTime.
 */
public class ZonedDateTimeConstraint<T> extends
		ChronoZonedDateTimeConstraintBase<T, ZonedDateTime, ZonedDateTimeConstraint<T>> {

	@Override
	protected ZonedDateTime getNow(Clock clock) {
		return ZonedDateTime.now(clock);
	}

	@Override
	public ZonedDateTimeConstraint<T> cast() {
		return this;
	}
}
