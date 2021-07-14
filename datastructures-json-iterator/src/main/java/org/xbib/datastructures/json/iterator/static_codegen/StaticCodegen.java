package org.xbib.datastructures.json.iterator.static_codegen;

import org.xbib.datastructures.json.iterator.CodegenAccess;
import org.xbib.datastructures.json.iterator.spi.DecodingMode;
import org.xbib.datastructures.json.iterator.JsonIterator;
import org.xbib.datastructures.json.iterator.output.EncodingMode;
import org.xbib.datastructures.json.iterator.output.JsonStream;
import org.xbib.datastructures.json.iterator.spi.JsonException;

import java.io.File;

public class StaticCodegen {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("StaticCodegen configClassName [outputDir]");
            System.out.println("configClassName: like a.b.Config, a class defining what to codegen");
            System.out.println("outputDir: if not specified, will write to source directory of configClass");
            return;
        }
        String configClassName = args[0];
        String configJavaFile = configClassName.replace('.', '/') + ".java";
        String outputDir;
        if (args.length > 1) {
            outputDir = args[1];
        } else {
            if (!new File(configJavaFile).exists()) {
                throw new JsonException("must execute static code generator in the java source code directory which contains: " + configJavaFile);
            }
            outputDir = new File(".").getAbsolutePath();
        }
        Class<?> clazz = Class.forName(configClassName);
        StaticCodegenConfig config = (StaticCodegenConfig) clazz.newInstance();
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
        config.setup();
        CodegenAccess.staticGenDecoders(
                config.whatToCodegen(), new CodegenAccess.StaticCodegenTarget(outputDir));
        org.xbib.datastructures.json.iterator.output.CodegenAccess.staticGenEncoders(
                config.whatToCodegen(), new org.xbib.datastructures.json.iterator.output.CodegenAccess.StaticCodegenTarget(outputDir));
    }
}
