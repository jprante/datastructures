package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.fn.Function1;
import org.xbib.datastructures.validation.jsr305.Nullable;

public class Arguments1<A1> {

	protected final A1 arg1;

	Arguments1(@Nullable A1 arg1) {
		this.arg1 = arg1;
	}

	@Nullable
	public final A1 arg1() {
		return this.arg1;
	}

	public final <X> X map(Function1<? super A1, ? extends X> mapper) {
		return mapper.apply(arg1);
	}
}
