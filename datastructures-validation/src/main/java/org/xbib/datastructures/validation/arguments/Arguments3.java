package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.fn.Function3;
import org.xbib.datastructures.validation.jsr305.Nullable;

public class Arguments3<A1, A2, A3> extends Arguments2<A1, A2> {

	protected final A3 arg3;

	Arguments3(@Nullable A1 arg1, @Nullable A2 arg2, @Nullable A3 arg3) {
		super(arg1, arg2);
		this.arg3 = arg3;
	}

	@Nullable
	public final A3 arg3() {
		return this.arg3;
	}

	public final <X> X map(
			Function3<? super A1, ? super A2, ? super A3, ? extends X> mapper) {
		return mapper.apply(arg1, arg2, arg3);
	}
}
