package com.example.wangduwei.demos.plugin.classloader;

import dalvik.system.DexClassLoader;

/**
 * <p>
 *
 * @author : wangduwei
 * @since : 2020/4/27  13:02
 **/
public class MyClassLoader extends DexClassLoader {

    public MyClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }


    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);

    }
}
