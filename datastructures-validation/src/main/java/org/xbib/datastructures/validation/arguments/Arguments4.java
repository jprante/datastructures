package org.xbib.datastructures.validation.arguments;

import org.xbib.datastructures.validation.fn.Function4;
import org.xbib.datastructures.validation.jsr305.Nullable;

public class Arguments4<A1, A2, A3, A4> extends Arguments3<A1, A2, A3> {

	protected final A4 arg4;

	Arguments4(@Nullable A1 arg1, @Nullable A2 arg2, @Nullable A3 arg3,
			@Nullable A4 arg4) {
		super(arg1, arg2, arg3);
		this.arg4 = arg4;
	}

	@Nullable
	public final A4 arg4() {
		return this.arg4;
	}

	public final <X> X map(
			Function4<? super A1, ? super A2, ? super A3, ? super A4, ? extends X> mapper) {
		return mapper.apply(arg1, arg2, arg3, arg4);
	}
}
