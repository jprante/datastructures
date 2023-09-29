package org.xbib.datastructures.interpolation;

import java.util.regex.Pattern;

public class EnclosureClosingHandlerImpl<T> extends SubstitutionHandlerImpl<T>
        implements EnclosureClosingHandler<T> {

    private final String opening;
    private final String closing;

    private final Pattern pattern;

    public EnclosureClosingHandlerImpl(String opening, String closing, String characterClass) {
        this.opening = opening;
        this.closing = closing;
        String quotedOpening = Pattern.quote(opening);
        String quotedClosing = Pattern.quote(closing);
        if (characterClass == null) {
            characterClass = "[^" + quotedOpening + quotedClosing + "\\s]+";
        }
        this.pattern = Pattern.compile("(" + quotedOpening + characterClass + quotedClosing + ")");
    }

    @Override
    protected Pattern getPattern() {
        return pattern;
    }

    @Override
    protected String getCaptured(String found) {
        return found.substring(opening.length(), found.length() - closing.length());
    }
}
