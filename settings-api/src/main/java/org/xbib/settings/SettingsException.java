package org.xbib.settings;

/**
 * A generic failure to handle settings.
 */
@SuppressWarnings("serial")
public class SettingsException extends RuntimeException {

    public SettingsException(String message) {
        super(message);
    }

    public SettingsException(String message, Throwable cause) {
        super(message, cause);
    }
}
