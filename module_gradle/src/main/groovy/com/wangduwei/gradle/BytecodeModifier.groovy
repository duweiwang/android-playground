package com.wangduwei.gradle

import javassist.CtClass

class BytecodeModifier{
    static void injectCode(String path, String packageName) {
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                if (filePath.endsWith(".class")
                        && filePath.contains(packageName)
                        && filePath.contains('MainActivity')
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")) {

//                    LogHelper.debug("path:" + path + ",filePath:" + filePath)

                    // 创建一个Device.class并设置成MainActivity的成员变量
                    CtClass deviceClass = JavassistManager.createDeviceClass(path)
                    if (deviceClass != null) {
                        JavassistManager.injectToMainActivity(path, deviceClass)
                    }
                }
            }
        }
    }
}