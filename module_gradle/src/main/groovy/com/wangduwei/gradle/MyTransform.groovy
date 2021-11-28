package com.wangduwei.gradle

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project

class MyTransform extends Transform {

    private Project mProject

    MyTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return "Wanduwei"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs,
                   Collection<TransformInput> collection1,
                   TransformOutputProvider outputProvider, boolean b) throws IOException, TransformException, InterruptedException {
        System.out.println("--------------transform-----------------")
        mProject.logger.debug("\"--------------transform-----------------\"")
        inputs.each { TransformInput input ->
            try {
                input.jarInputs.each {
                    //这里处理自定义的逻辑
//                    MyInject.injectDir(it.file.getAbsolutePath(), "com", project)
                    BytecodeModifier.injectCode(it.file.getAbsolutePath(),"com.example.wangduwei.demos")
                    // 重命名输出文件（同目录copyFile会冲突）
                    String outputFileName = it.name.replace(".jar", "") + '-' + it.file.path.hashCode()


                    def output = outputProvider.getContentLocation(outputFileName,
                            it.contentTypes,
                            it.scopes,
                            Format.JAR)
                    FileUtils.copyFile(it.file, output)
                }
            } catch (Exception e) {
                mProject.logger.err e.getMessage()
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                //这里处理自定义的逻辑
//                MyInject.injectDir(directoryInput.file.absolutePath, "com", project)
                BytecodeModifier.injectCode(it.file.getAbsolutePath(),"com.example.wangduwei.demos")


                // 获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)

                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
        ClassPool.getDefault().clearImportedPackages()
    }
}