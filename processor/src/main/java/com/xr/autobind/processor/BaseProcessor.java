package com.xr.autobind.processor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.lang.model.SourceVersion;

public abstract class BaseProcessor extends AbstractProcessor {
    protected abstract void addAnnotation(Set<Class<? extends Annotation>> classSet);

    @Override
    public final SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        Set<Class<? extends Annotation>> classSet = new HashSet<>();
        addAnnotation(classSet);

        Set<String> nameSet = new HashSet<>();
        for (Class<? extends Annotation> clazz : classSet) {
            nameSet.add(clazz.getCanonicalName());
        }
        return nameSet;
    }

}
