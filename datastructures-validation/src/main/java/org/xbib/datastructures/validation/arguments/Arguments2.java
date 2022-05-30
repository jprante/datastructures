package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.fn.Function2;
import org.xbib.datastructures.validation.jsr305.Nullable;

public class Arguments2<A1, A2> extends Arguments1<A1> {

	protected final A2 arg2;

	Arguments2(@Nullable A1 arg1, @Nullable A2 arg2) {
		super(arg1);
		this.arg2 = arg2;
	}

	@Nullable
	public final A2 arg2() {
		return this.arg2;
	}

	public final <X> X map(Function2<? super A1, ? super A2, ? extends X> mapper) {
		return mapper.apply(arg1, arg2);
	}
}
