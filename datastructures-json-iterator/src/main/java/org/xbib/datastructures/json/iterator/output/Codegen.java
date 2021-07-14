package org.xbib.datastructures.json.iterator.output;

import org.xbib.datastructures.json.iterator.spi.ClassInfo;
import org.xbib.datastructures.json.iterator.spi.Encoder;
import org.xbib.datastructures.json.iterator.spi.Extension;
import org.xbib.datastructures.json.iterator.spi.GenericsHelper;
import org.xbib.datastructures.json.iterator.spi.JsonException;
import org.xbib.datastructures.json.iterator.spi.JsoniterSpi;
import org.xbib.datastructures.json.iterator.spi.TypeLiteral;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

class Codegen {

    static CodegenAccess.StaticCodegenTarget isDoingStaticCodegen;
    // only read/write when generating code with synchronized protection
    private final static Map<String, CodegenResult> generatedSources = new HashMap<String, CodegenResult>();
    private volatile static Map<String, Encoder.ReflectionEncoder> reflectionEncoders = new HashMap<String, Encoder.ReflectionEncoder>();

    public static Encoder.ReflectionEncoder getReflectionEncoder(String cacheKey, Type type) {
        Encoder.ReflectionEncoder encoder = CodegenImplNative.NATIVE_ENCODERS.get(type);
        if (encoder != null) {
            return encoder;
        }
        encoder = reflectionEncoders.get(cacheKey);
        if (encoder != null) {
            return encoder;
        }
        synchronized (Codegen.class) {
            encoder = reflectionEncoders.get(cacheKey);
            if (encoder != null) {
                return encoder;
            }
            ClassInfo classInfo = new ClassInfo(type);
            encoder = ReflectionEncoderFactory.create(classInfo);
            HashMap<String, Encoder.ReflectionEncoder> copy = new HashMap<String, Encoder.ReflectionEncoder>(reflectionEncoders);
            copy.put(cacheKey, encoder);
            reflectionEncoders = copy;
            return encoder;
        }
    }

    public static Encoder getEncoder(String cacheKey, Type type) {
        Encoder encoder = JsoniterSpi.getEncoder(cacheKey);
        if (encoder != null) {
            return encoder;
        }
        return gen(cacheKey, type);
    }

    private static synchronized Encoder gen(final String cacheKey, Type type) {
        Encoder encoder = JsoniterSpi.getEncoder(cacheKey);
        if (encoder != null) {
            return encoder;
        }
        List<Extension> extensions = JsoniterSpi.getExtensions();
        for (Extension extension : extensions) {
            encoder = extension.createEncoder(cacheKey, type);
            if (encoder != null) {
                JsoniterSpi.addNewEncoder(cacheKey, encoder);
                return encoder;
            }
        }
        encoder = CodegenImplNative.NATIVE_ENCODERS.get(type);
        if (encoder != null) {
            JsoniterSpi.addNewEncoder(cacheKey, encoder);
            return encoder;
        }
        addPlaceholderEncoderToSupportRecursiveStructure(cacheKey);
        try {
            EncodingMode mode = JsoniterSpi.getCurrentConfig().encodingMode();
            if (mode != EncodingMode.REFLECTION_MODE) {
                Type originalType = type;
                type = chooseAccessibleSuper(type);
                if (Object.class == type) {
                    throw new JsonException("dynamic code can not serialize private class: " + originalType);
                }
            }
            ClassInfo classInfo = new ClassInfo(type);
            if (Map.class.isAssignableFrom(classInfo.clazz) && classInfo.typeArgs.length > 1) {
                MapKeyEncoders.registerOrGetExisting(classInfo.typeArgs[0]);
            }
            if (mode == EncodingMode.REFLECTION_MODE) {
                encoder = ReflectionEncoderFactory.create(classInfo);
                return encoder;
            }
            if (isDoingStaticCodegen == null) {
                try {
                    encoder = (Encoder) Class.forName(cacheKey).newInstance();
                    return encoder;
                } catch (Exception e) {
                    if (mode == EncodingMode.STATIC_MODE) {
                        throw new JsonException("static gen should provide the encoder we need, but failed to create the encoder", e);
                    }
                }
            }
            CodegenResult source = genSource(cacheKey, classInfo);
            try {
                generatedSources.put(cacheKey, source);
                if (isDoingStaticCodegen == null) {
                    encoder = DynamicCodegen.gen(classInfo.clazz, cacheKey, source);
                } else {
                    staticGen(classInfo.clazz, cacheKey, source);
                }
                return encoder;
            } catch (Exception e) {
                String msg = "failed to generate encoder for: " + type + " with " + Arrays.toString(classInfo.typeArgs) + ", exception: " + e;
                msg = msg + "\n" + source;
                throw new JsonException(msg, e);
            }
        } finally {
            JsoniterSpi.addNewEncoder(cacheKey, encoder);
        }
    }

    private static void addPlaceholderEncoderToSupportRecursiveStructure(final String cacheKey) {
        JsoniterSpi.addNewEncoder(cacheKey, new Encoder() {
            @Override
            public void encode(Object obj, JsonStream stream) throws IOException {
                Encoder encoder = JsoniterSpi.getEncoder(cacheKey);
                if (this == encoder) {
                    for(int i = 0; i < 30; i++) {
                        encoder = JsoniterSpi.getEncoder(cacheKey);
                        if (this == encoder) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new JsonException(e);
                            }
                        } else {
                            break;
                        }
                    }
                    if (this == encoder) {
                        throw new JsonException("internal error: placeholder is not replaced with real encoder");
                    }
                }
                encoder.encode(obj, stream);
            }
        });
    }

    private static Type chooseAccessibleSuper(Type type) {
        Type[] typeArgs = new Type[0];
        Class clazz;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            clazz = (Class) pType.getRawType();
            typeArgs = pType.getActualTypeArguments();
        } else {
            clazz = (Class) type;
        }
        if (Modifier.isPublic(clazz.getModifiers())) {
            return type;
        }
        clazz = walkSuperUntilPublic(clazz.getSuperclass());
        if (typeArgs.length == 0) {
            return clazz;
        } else {
            return GenericsHelper.createParameterizedType(typeArgs, null, clazz);
        }
    }

    private static Class walkSuperUntilPublic(Class clazz) {
        if (Modifier.isPublic(clazz.getModifiers())) {
            return clazz;
        }
        return walkSuperUntilPublic(clazz.getSuperclass());
    }

    public static CodegenResult getGeneratedSource(String cacheKey) {
        return generatedSources.get(cacheKey);
    }

    private static void staticGen(Class clazz, String cacheKey, CodegenResult source) throws IOException {
        createDir(cacheKey);
        String fileName = cacheKey.replace('.', '/') + ".java";
        FileOutputStream fileOutputStream = new FileOutputStream(new File(isDoingStaticCodegen.outputDir, fileName));
        try {
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            try {
                staticGen(clazz, cacheKey, writer, source);
            } finally {
                writer.close();
            }
        } finally {
            fileOutputStream.close();
        }
    }

    private static void staticGen(Class clazz, String cacheKey, OutputStreamWriter writer, CodegenResult source) throws IOException {
        String className = cacheKey.substring(cacheKey.lastIndexOf('.') + 1);
        String packageName = cacheKey.substring(0, cacheKey.lastIndexOf('.'));
        writer.write("package " + packageName + ";\n");
        writer.write("public class " + className + " implements org.xbib.datastructures.json.iterator.spi.Encoder {\n");
        writer.write(source.generateWrapperCode(clazz));
        writer.write(source.toString());
        writer.write("}\n");
    }

    private static void createDir(String cacheKey) {
        String[] parts = cacheKey.split("\\.");
        File parent = new File(isDoingStaticCodegen.outputDir);
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            File current = new File(parent, part);
            current.mkdir();
            parent = current;
        }
    }

    private static CodegenResult genSource(String cacheKey, ClassInfo classInfo) {
        Class clazz = classInfo.clazz;
        if (clazz.isArray()) {
            return CodegenImplArray.genArray(cacheKey, classInfo);
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return CodegenImplMap.genMap(cacheKey, classInfo);
        }
        if (Collection.class.isAssignableFrom(clazz)) {
            return CodegenImplArray.genCollection(cacheKey, classInfo);
        }
        if (clazz.isEnum()) {
            return CodegenImplNative.genEnum(clazz);
        }
        return CodegenImplObject.genObject(classInfo);
    }

    public static void staticGenEncoders(TypeLiteral[] typeLiterals, CodegenAccess.StaticCodegenTarget staticCodegenTarget) {
        isDoingStaticCodegen = staticCodegenTarget;
        for (TypeLiteral typeLiteral : typeLiterals) {
            gen(typeLiteral.getEncoderCacheKey(), typeLiteral.getType());
        }
    }
}
