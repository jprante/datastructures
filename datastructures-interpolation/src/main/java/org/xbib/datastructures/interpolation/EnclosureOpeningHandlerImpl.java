package org.xbib.datastructures.interpolation;

public class EnclosureOpeningHandlerImpl<T> implements EnclosureOpeningHandler<T> {

    private final String opening;

    private final String characterClass;

    private EnclosureClosingHandlerImpl<T> closingHandler;

    public EnclosureOpeningHandlerImpl(String opening, String characterClass) {
        this.opening = opening;
        this.characterClass = characterClass;
    }

    @Override
    public EnclosureClosingHandler<T> and(String closing) {
        EnclosureClosingHandlerImpl<T> closingHandler =
                new EnclosureClosingHandlerImpl<T>(opening, closing, characterClass);
        this.closingHandler = closingHandler;
        return closingHandler;
    }

    public EnclosureClosingHandlerImpl<T> getEnclosureClosingHandler() {
        return closingHandler;
    }
}
