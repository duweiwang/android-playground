package com.wangduwei.gradle

import javassist.*

class JavassistManager{
     static void injectToMainActivity(String path, CtClass ctClass) {
        ClassPool pool = ClassPool.getDefault()
        CtClass mainActivity
        try {
            pool.appendClassPath(path)
            mainActivity = pool.get("com.example.wangduwei.demos.main.MainActivity")
            if (mainActivity.isFrozen()) {
                mainActivity.defrost()
            }

            CtField ctField = new CtField(ctClass, "device", mainActivity)
            mainActivity.addField(ctField)
            mainActivity.writeFile(path)
        } catch (NotFoundException e) {
            e.printStackTrace()
        } catch (CannotCompileException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }

     static CtClass createDeviceClass(String path) {
        String className = "com.example.wangduwei.demos.main.Device"
        ClassPool pool = ClassPool.getDefault()
        CtClass cc = pool.makeClass(className)

        try {
            CtField ctField = CtField.make("String deviceId;", cc)
            cc.addField(ctField)

            CtConstructor con = CtNewConstructor.make([pool.get("java.lang.String")], null, "{this" +
                    ".deviceId = \"1234567890\";}", cc)
            cc.addConstructor(con)

            CtMethod ctMethod = CtNewMethod.abstractMethod(CtClass.voidType,
                    "update", null, null, cc)
            cc.addMethod(ctMethod)

            CtClass eventListener = createEventListener(path)
            if (eventListener != null) {
//                cc.setInterfaces(new CtClass[]{eventListener})
                cc.setInterfaces([eventListener])
            }

            cc.writeFile(path)
            return cc
        } catch (CannotCompileException e) {
            e.printStackTrace()
        } catch (NotFoundException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
        return null
    }

    private static CtClass createEventListener(String path) {
        String className = "com.example.wangduwei.demos.main.EventListener"
        ClassPool pool = ClassPool.getDefault()
        CtClass cc = pool.makeInterface(className)

        try {
            CtMethod ctMethod = CtNewMethod.abstractMethod(CtClass.voidType,
                    "onCreate", null, null, cc)
            cc.addMethod(ctMethod)

            CtMethod ctMethod1 = CtNewMethod.abstractMethod(CtClass.voidType,
                    "onDestroy", null, null, cc)
            cc.addMethod(ctMethod1)

            cc.writeFile(path)
            return cc
        } catch (CannotCompileException e) {
            e.printStackTrace()
        } catch (NotFoundException e) {
            e.printStackTrace()
        } catch (IOException e) {
            e.printStackTrace()
        }
        return null
    }
}