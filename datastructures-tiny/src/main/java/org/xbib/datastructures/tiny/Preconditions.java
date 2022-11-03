package org.xbib.datastructures.tiny;

public abstract class Preconditions {

    public Preconditions() {
    }

    public static void checkArgument(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void checkState(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalStateException(errorMessage);
        }
    }

    public static void checkElementIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(badElementIndex(index, size));
        }
    }

    private static String badElementIndex(int index, int size) {
        if (index < 0) {
            return format("index (%s) must not be negative", index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return format("index (%s) must be less than size (%s)", index, size);
        }
    }

    private static String format(String template, Object... args) {
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }
        return builder.toString();
    }
}
