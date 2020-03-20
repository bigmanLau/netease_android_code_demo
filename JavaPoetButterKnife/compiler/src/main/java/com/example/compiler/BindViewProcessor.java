package com.example.compiler;

import com.example.annotation.BindView;
import com.example.compiler.util.Constants;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
//gradlew --no-daemon -Dorg.gradle.debug=true :app:clean :app:compileDebugJavaWithJavac
@AutoService(Processor.class)
@SupportedAnnotationTypes({Constants.BINDERVIEW_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class BindViewProcessor extends AbstractProcessor {
    private Elements elementUitls;
    private Types typeUtils;
    private Messager messager;
    private Filer filer;

    private Map<TypeElement, List<Element>> tempBindViewMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUitls = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        messager.printMessage(Diagnostic.Kind.NOTE, "init finish,start process annotation ..........");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (set.isEmpty()) return false;
        Set<? extends Element> bindViewElements = roundEnv.getElementsAnnotatedWith(BindView.class);
        if (!bindViewElements.isEmpty()) {
            valueOfMap(bindViewElements);
            try {
                createJavaFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void createJavaFile() throws IOException {
        if (tempBindViewMap.isEmpty()) return;


        for (Map.Entry<TypeElement, List<Element>> entry : tempBindViewMap.entrySet()) {
            //获取当前类名
            ClassName className = ClassName.get(entry.getKey());

            //生成一个名为target参数 类型就是上面的typeName
            ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(entry.getKey()), Constants.TARGET_PARAMETER_NAME)
                    .build();//target

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.BIND_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(parameterSpec);

            for (Element fieldElement : entry.getValue()) {
                String fieldName = fieldElement.getSimpleName().toString();
                //获取BindView上面的id值
                int viewId = fieldElement.getAnnotation(BindView.class).value();
                String methodContent = "$N." + fieldName + " = $N.findViewById($L)";
                //拼接成target.tv=target.findViewById(R.id.tv)
                methodBuilder.addStatement(methodContent, Constants.TARGET_PARAMETER_NAME,
                        Constants.TARGET_PARAMETER_NAME, viewId);
            }

            //找得到所有实现了ViewBinder接口的类
            TypeElement viewBindType = elementUitls.getTypeElement(Constants.VIEWBINDER);
            // 这里需要理解一下 实现接口泛型 ViewBinder<MainActivity> 拼接成这个样子
            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(viewBindType),
                    className);
            if(typeName!=null) {
                JavaFile.builder(className.packageName(),
                        TypeSpec.classBuilder(className.simpleName() + "$$ViewBinder")
                                .addSuperinterface(typeName)  //实现viewBinder接口
                                .addModifiers(Modifier.PUBLIC)
                                .addMethod(methodBuilder.build())
                                .build()

                ).build().writeTo(filer);
            }

        }

    }

    private void valueOfMap(Set<? extends Element> bindViewElements) {
        for (Element element : bindViewElements) {
            //注解在属性之上，属性节点父节点是类节点
            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            if (tempBindViewMap.containsKey(classElement)) {
                tempBindViewMap.get(classElement).add(element);
            } else {
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                tempBindViewMap.put(classElement, fields);
            }

        }
    }
}
