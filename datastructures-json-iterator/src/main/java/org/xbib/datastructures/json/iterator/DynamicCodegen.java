package org.xbib.datastructures.json.iterator;

import org.xbib.datastructures.json.iterator.spi.Decoder;
import javassist.*;

class DynamicCodegen {

    static ClassPool pool = ClassPool.getDefault();

    static {
        pool.insertClassPath(new ClassClassPath(Decoder.class));
    }

    public static Decoder gen(String cacheKey, String source) throws Exception {
        Decoder decoder;
        CtClass ctClass = pool.makeClass(cacheKey);
        ctClass.setInterfaces(new CtClass[]{pool.get(Decoder.class.getName())});
        CtMethod staticMethod = CtNewMethod.make(source, ctClass);
        ctClass.addMethod(staticMethod);
        CtMethod interfaceMethod = CtNewMethod.make("" +
                "public Object decode(org.xbib.datastructures.json.iterator.JsonIterator iter) {" +
                "return decode_(iter);" +
                "}", ctClass);
        ctClass.addMethod(interfaceMethod);
        decoder = (Decoder) ctClass.toClass().newInstance();
        return decoder;
    }

    public static void enableStreamingSupport() throws Exception {
        CtClass ctClass = pool.makeClass("org.xbib.datastructures.json.iterator.IterImpl");
        ctClass.setSuperclass(pool.get(IterImplForStreaming.class.getName()));
        ctClass.toClass();
    }
}
