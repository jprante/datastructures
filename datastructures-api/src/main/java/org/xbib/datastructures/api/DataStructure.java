package org.xbib.datastructures.api;

import java.time.Instant;
import java.util.function.Consumer;

public interface DataStructure {

    Parser createParser();

    Generator createGenerator(Node<?> root);

    Builder createBuilder();

    Builder createBuilder(Consumer<String> consumer);

    void setRoot(Node<?> root);

    Node<?> getRoot();

    Node<?> getNode(String path);

    boolean set(String path, Object value);

    Boolean getBoolean(String path);

    Byte getByte(String path);

    Short getShort(String path);

    Integer getInteger(String path);

    Long getLong(String path);

    Float getFloat(String path);

    Double getDouble(String path);

    Character getCharacter(String path);

    String getString(String path);

    Instant getInstant(String path);

    TimeValue getAsTime(String setting, TimeValue defaultValue);

    ByteSizeValue getAsBytesSize(String setting, ByteSizeValue defaultValue);
}
