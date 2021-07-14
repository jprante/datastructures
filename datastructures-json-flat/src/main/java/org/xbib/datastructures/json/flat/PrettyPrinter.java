package org.xbib.datastructures.json.flat;

import java.util.Stack;


class PrettyPrinter implements Visitor {

    private enum Type {TOP, ARRAY, OBJECT}

    private static class Context {
        private final Type type;
        private int count = 0;

        Context(Type type) {
            this.type = type;
        }
    }

    private final StringBuilder builder;
    private final Stack<Context> context;
    private final String indent;

    PrettyPrinter(String indent) {
        builder = new StringBuilder();
        context = new Stack<>();
        context.push(new Context(Type.TOP));
        this.indent = indent;
    }

    @Override
    public void onNull() {
        append("null");
    }

    @Override
    public void onBoolean(boolean value) {
        append(Boolean.toString(value));
    }

    @Override
    public void onNumber(String value) {
        append(value);
    }

    @Override
    public void onString(String value) {
        append(String.format("\"%s\"", StringUtil.escape(value)));
    }

    @Override
    public void beginArray() {
        append("[");
        context.push(new Context(Type.ARRAY));
    }

    @Override
    public void endArray() {
        if (current().type != Type.ARRAY) {
            throw new IllegalStateException("not inside array");
        }
        if (current().count > 0) {
            if (indent != null) {
                builder.append("\n");
                builder.append(indent.repeat(Math.max(0, context.size() - 2)));
            }
        }
        builder.append("]");
        context.pop();
    }

    @Override
    public void beginObject() {
        append("{");
        context.push(new Context(Type.OBJECT));
    }

    @Override
    public void endObject() {
        if (current().type != Type.OBJECT) {
            throw new IllegalStateException("not inside object");
        }
        if (current().count % 2 == 1) {
            throw new IllegalStateException("unbalanced object");
        }
        if (current().count > 0) {
            if (indent != null) {
                builder.append("\n");
                for (int i = 2; i < context.size(); i++) {
                    builder.append(indent);
                }
            }
        }
        builder.append("}");
        context.pop();
    }

    @Override
    public String toString() {
        if (context.size() > 1) {
            throw new IllegalStateException("unbalanced json");
        }
        return builder.toString();
    }

    private void append(String value) {
        if (current().type == Type.TOP) {
            if (current().count > 0) {
                throw new IllegalStateException("multiple toplevel values");
            }
        } else if (current().type == Type.ARRAY) {
            if (current().count > 0) {
                builder.append(",");
            }
            if (indent != null) {
                builder.append("\n");
                builder.append(indent.repeat(Math.max(0, context.size() - 1)));
            }
        } else if (current().type == Type.OBJECT) {
            if (current().count % 2 == 0) {
                if (current().count > 0) {
                    builder.append(",");
                }
                if (indent != null) {
                    builder.append("\n");
                    builder.append(indent.repeat(Math.max(0, context.size() - 1)));
                }
            } else {
                builder.append(":");
                if (indent != null) {
                    builder.append(" ");
                }
            }
        }
        builder.append(value);
        current().count++;
    }

    private Context current() {
        return context.peek();
    }

}
