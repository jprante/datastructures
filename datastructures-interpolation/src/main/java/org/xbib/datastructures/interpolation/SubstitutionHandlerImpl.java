package org.xbib.datastructures.interpolation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SubstitutionHandlerImpl<T> implements SubstitutionHandler<T>, Interpolating<T> {

    protected Substitutor<T> substitutor;

    public SubstitutionHandlerImpl() {
    }

    public void handleWith(Substitutor<T> substitutor) {
        this.substitutor = substitutor;
    }

    protected abstract Pattern getPattern();

    protected abstract String getCaptured(String found);

    @Override
    public List<Substitution> interpolate(String toInterpolate, T arg) {
        List<Substitution> substitutions = new ArrayList<Substitution>();
        if (substitutor != null) {
            Matcher m = getPattern().matcher(toInterpolate);
            while (m.find()) {
                String found = m.group(1);
                String captured = getCaptured(found);
                String substitution = substitutor.substitute(captured, arg);
                substitutions.add(new Substitution(found, substitution, m.start(), m.end()));
            }
        }
        return substitutions;
    }
}
