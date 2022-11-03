package org.xbib.datastructures.json.tiny;

import org.xbib.datastructures.api.Node;

import java.util.Deque;

public class EmptyJsonListener implements JsonResult {

    public EmptyJsonListener() {
    }

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
    public void beginCollection() {
    }

    @Override
    public void endCollection() {
    }

    @Override
    public void beginMap() {
    }

    @Override
    public void endMap() {

    }

    public Deque<Node<?>> getStack() {
        return null;
    }

    @Override
    public Node<?> getResult() {
        return null;
    }
}
