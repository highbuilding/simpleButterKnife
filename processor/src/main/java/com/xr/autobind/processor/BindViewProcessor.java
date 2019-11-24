package com.xr.autobind.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xr.autobind.annotation.BindView;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class BindViewProcessor extends BaseProcessor {
    private Messager messager;
    private Elements elementUtils;
    private Filer mFiler;
    private Types mTypeUtils;

    /**
     * `
     * 初始化操作
     *
     * @param processingEnvironment
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTypeUtils = processingEnvironment.getTypeUtils();
        elementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    @Override
    protected void addAnnotation(Set<Class<? extends Annotation>> classSet) {
        classSet.add(BindView.class);

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> bindViewElements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        ArrayList<FindTargetView> findTargetViews = new ArrayList<>();
        TypeElement enclosingElement = null;
        for (Element element : bindViewElements) {
            // 1.获取包名
            PackageElement packageElement = elementUtils.getPackageOf(element);
            String pkName = packageElement.getQualifiedName().toString();
            // 2.获取包装类类型
            enclosingElement = (TypeElement) element.getEnclosingElement();
            String enclosingName = enclosingElement.getQualifiedName().toString();
            // 因为BindView只作用于filed，所以这里可直接进行强转
            VariableElement bindViewElement = (VariableElement) element;
            // 3.获取注解的成员变量名
            String bindViewFiledName = bindViewElement.getSimpleName().toString();
            // 3.获取注解的成员变量类型
            String bindViewFiledClassType = bindViewElement.asType().toString();

            //4.获取注解元数据
            BindView bindView = element.getAnnotation(BindView.class);
            int id = bindView.value();
            // note(String.format("%s %s = %d", bindViewFiledClassType, bindViewFiledName, id));

            findTargetViews.add(new FindTargetView(bindViewFiledName, id));
        }
        if (findTargetViews.size() > 0) {
            createFile(enclosingElement, findTargetViews);
        }
        return false;
    }

    private void createFile(TypeElement enclosingElement, ArrayList<FindTargetView> targetViews) {
        String pkName = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
        String parentClassName = elementUtils.getTypeElement(enclosingElement.getQualifiedName()).getSimpleName().toString();

        String className = parentClassName + "_bindView";

        ClassName parentFullClassName = ClassName.get(enclosingElement);
        MethodSpec.Builder constructBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(parentFullClassName, "target", Modifier.FINAL);
        for (FindTargetView item : targetViews) {
            constructBuilder
                    .addStatement("target.$N = target.findViewById(" + item.id + ")", item.name);
        }
        MethodSpec construct = constructBuilder.build();


        TypeSpec bindView = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(construct)
                .build();

        JavaFile javaFile = JavaFile.builder(pkName, bindView)
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class FindTargetView {
        private String name;
        private int id;

        public FindTargetView(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }


}
