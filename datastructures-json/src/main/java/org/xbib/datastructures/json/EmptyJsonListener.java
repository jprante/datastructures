package org.xbib.datastructures.json;

import java.util.Deque;

public class EmptyJsonListener implements JsonDeserializer {

    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public void onNull() {

    }

    @Override
    public void onTrue() {

    }

    @Override
    public void onFalse() {

    }

    @Override
    public void onKey(CharSequence key) {

    }

    @Override
    public void onValue(CharSequence value) {

    }

    @Override
    public void onLong(Long value) {

    }

    @Override
    public void onDouble(Double value) {

    }

    @Override
    public void beginList() {

    }

    @Override
    public void endList() {

    }

    @Override
    public void beginMap() {

    }

    @Override
    public void endMap() {

    }

    @Override
    public Deque<Node<?>> getStack() {
        return null;
    }

    @Override
    public Node<?> getResult() {
        return null;
    }
}
