package com.example.lib_processor;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * <p>PageInfo annotation processor
 *
 * @author : wangduwei
 * @since : 2020/1/6  16:00
 **/
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;
    private static final String packageName = "com.example.wangduwei.demos.router";
    private List<FragmentInfo> list = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // // all annotated element

        for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(PageInfo.class)) {
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "---------------" + "Only classes can be annotated with @%s", PageInfo.class.getSimpleName());
                return true;
            }
            // //generate code
            analysisAnnotated(annotatedElement, list);
        }
        writeJava(list);
        return false;
    }


    /**
     * 要处理的注解
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(PageInfo.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    private void analysisAnnotated(Element annotatedElement, List<FragmentInfo> list) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "---------------" + annotatedElement.getSimpleName());
        PageInfo pageInfo = annotatedElement.getAnnotation(PageInfo.class);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "---------------" + pageInfo.description());
        FragmentInfo fragmentInfo = new FragmentInfo(pageInfo.description(), pageInfo.navigationId());
        list.add(fragmentInfo);
    }

    /**
     * 把List数据写进java文件
     *
     * @param list
     */
    private void writeJava(List<FragmentInfo> list) {
        StringBuilder builder = new StringBuilder()
                .append("package " + packageName + ";\n\n")
                .append("import java.util.List;\n")
                .append("import com.example.lib_processor.FragmentInfo;\n")
                .append("import java.util.ArrayList;\n\n")
                .append("public class PageInfoProtocol {\n\n")
                .append("\tpublic static List<FragmentInfo> list = new ArrayList();\n")
                .append("\tstatic {\n");
        for (FragmentInfo fragmentInfo : list) {
            builder.append("\t\tlist.add(new FragmentInfo(\"" + fragmentInfo.getDescription() + "\"," + fragmentInfo.getId() + "));\n");
        }
        builder.append("\t}\n\n")
                .append("\tpublic List<FragmentInfo> getList(){\n")
                .append("\t\treturn list;\n")
                .append("\t}\n")
                .append("}");

        try { // write the file
            JavaFileObject source = mFiler.createSourceFile(packageName + ".PageInfoProtocol");
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "----" + builder.toString());
    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
