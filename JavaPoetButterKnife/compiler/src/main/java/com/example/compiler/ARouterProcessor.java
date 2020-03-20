package com.example.compiler;

import com.example.annotation.ARouter;
import com.example.compiler.util.Constants;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ARouterProcessor extends AbstractProcessor {
    private Elements elementUtils;
    private Messager messager;
    private Filer filer;//文件生成
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils=processingEnv.getElementUtils();
        messager=processingEnv.getMessager();
        filer=processingEnv.getFiler();

    }

//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();
//        //支持的注解类型
//    }
//
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return super.getSupportedSourceVersion();
//    }
//
//    @Override
//    public Set<String> getSupportedOptions() {
//        return super.getSupportedOptions();
//    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty())return false;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName().toString();
            messager.printMessage(Diagnostic.Kind.NOTE,"被注解的类有:"+className);
            String finalClassName=className+"$$ARouter";

            try{
                JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + finalClassName);
                Writer writer = sourceFile.openWriter();
                writer.write("package "+packageName+";\n");
                writer.write("public class  "+finalClassName+"{\n");
                writer.write("public void hello(String path) {\n}\n} ");

                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
