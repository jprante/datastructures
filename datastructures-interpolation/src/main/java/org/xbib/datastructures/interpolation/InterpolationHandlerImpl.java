package org.xbib.datastructures.interpolation;

import java.util.ArrayList;
import java.util.List;

public class InterpolationHandlerImpl<T> implements InterpolationHandler<T>, Interpolating<T> {

    private final String characterClass;

    private PrefixHandlerImpl<T> prefixHandler;

    private EnclosureOpeningHandlerImpl<T> enclosureOpeningHandler;

    public InterpolationHandlerImpl() {
        this(null);
    }

    public InterpolationHandlerImpl(String characterClass) {
        this.characterClass = characterClass;
    }

    @Override
    public PrefixHandler<T> prefixedBy(String prefix) {
        PrefixHandlerImpl<T> prefixHandler = new PrefixHandlerImpl<T>(prefix, characterClass);
        this.prefixHandler = prefixHandler;
        return prefixHandler;
    }

    @Override
    public EnclosureOpeningHandler<T> enclosedBy(String opening) {
        EnclosureOpeningHandlerImpl<T> enclosureOpeningHandler =
                new EnclosureOpeningHandlerImpl<T>(opening, characterClass);
        this.enclosureOpeningHandler = enclosureOpeningHandler;
        return enclosureOpeningHandler;
    }

    @Override
    public List<Substitution> interpolate(String toInterpolate, T arg) {
        List<Substitution> substitutions = new ArrayList<Substitution>();
        if (prefixHandler != null) {
            substitutions.addAll(prefixHandler.interpolate(toInterpolate, arg));

        } else if (enclosureOpeningHandler != null) {
            substitutions.addAll(enclosureOpeningHandler.getEnclosureClosingHandler()
                    .interpolate(toInterpolate, arg));
        }
        return substitutions;
    }
}
