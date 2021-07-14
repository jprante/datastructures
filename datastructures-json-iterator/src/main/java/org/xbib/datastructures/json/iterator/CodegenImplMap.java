package org.xbib.datastructures.json.iterator;

import org.xbib.datastructures.json.iterator.spi.ClassInfo;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;

import java.lang.reflect.Type;

class CodegenImplMap {

    public static String genMap(ClassInfo classInfo) {
        Type keyType = classInfo.typeArgs[0];
        Type valueType = classInfo.typeArgs[1];
        StringBuilder lines = new StringBuilder();
        append(lines, "{{clazz}} map = ({{clazz}})org.xbib.datastructures.json.iterator.CodegenAccess.resetExistingObject(iter);");
        append(lines, "if (iter.readNull()) { return null; }");
        append(lines, "if (map == null) { map = new {{clazz}}(); }");
        append(lines, "if (!org.xbib.datastructures.json.iterator.CodegenAccess.readObjectStart(iter)) {");
        append(lines, "return map;");
        append(lines, "}");
        append(lines, "do {");
        if (keyType == String.class) {
            append(lines, "java.lang.Object mapKey = org.xbib.datastructures.json.iterator.CodegenAccess.readObjectFieldAsString(iter);");
        } else {
            append(lines, "java.lang.Object mapKey = org.xbib.datastructures.json.iterator.CodegenAccess.readMapKey(\"" +
                    TypeLiteral.create(keyType).getDecoderCacheKey() +"\", iter);");
        }
        append(lines, "map.put(mapKey, {{op}});");
        append(lines, "} while (org.xbib.datastructures.json.iterator.CodegenAccess.nextToken(iter) == ',');");
        append(lines, "return map;");
        return lines.toString()
                .replace("{{clazz}}", classInfo.clazz.getName())
                .replace("{{op}}", CodegenImplNative.genReadOp(valueType));
    }

    private static void append(StringBuilder lines, String str) {
        lines.append(str);
        lines.append("\n");
    }
}
