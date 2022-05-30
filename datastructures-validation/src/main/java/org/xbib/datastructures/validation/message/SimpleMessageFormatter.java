package org.xbib.datastructures.validation.message;

import java.text.MessageFormat;
import java.util.Locale;

public class SimpleMessageFormatter implements MessageFormatter {
	@Override
	public String format(String messageKey, String defaultMessageFormat, Object[] args,
			Locale locale) {
		return new MessageFormat(defaultMessageFormat, locale).format(args);
	}
}
