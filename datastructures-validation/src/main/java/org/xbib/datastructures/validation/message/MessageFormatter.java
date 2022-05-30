package org.xbib.datastructures.validation.message;

import java.util.Locale;

@FunctionalInterface
public interface MessageFormatter {
	String format(String messageKey, String defaultMessageFormat, Object[] args,
			Locale locale);
}
