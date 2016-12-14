package com.github.beansoftapp.android.router.compiler;

import com.google.auto.service.AutoService;
import com.github.beansoftapp.android.router.annotation.Router;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    private String targetModuleName = "";// 目标模块名

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Router.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        System.out.println("handle RouterProcessor"+annotations.size());
        if (annotations.size() == 0) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Router.class);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("HRouterMapping" + (targetModuleName.length() == 0 ? "Base" : targetModuleName))
                .addModifiers(Modifier.PUBLIC);


        MethodSpec.Builder mapMethodBuild = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class)
                ;


        List<MethodSpec> methodSpecs = new ArrayList<>();

        for (Element element : elements) {
            Router routerActivity = element.getAnnotation(Router.class);
            TypeElement typeElement = (TypeElement) element;

            // TODO 检查路由路径重复问题, 编译时报错
            if(routerActivity != null) {
                String[] values = routerActivity.value();
                if(values != null) {
                    for(String value : values) {
                        mapMethodBuild.addCode("com.github.beansoftapp.android.router.HRouter.map($S,  $T.class, $L, $S, $L, \"\", \"\");\n",
                                value, typeElement.asType(), routerActivity.login(), "1.0", routerActivity.isPublic() );
                    }
                }
            }
        }

        typeSpec.addMethod(mapMethodBuild.build());


        JavaFile javaFile = JavaFile.builder("com.github.beansoftapp.android.router", typeSpec.build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }



    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Elements elementUtils = processingEnv.getElementUtils();
        Map<String, String> map = processingEnv.getOptions();
        Set<String> keys = map.keySet();
        for (String key : keys) {
            if ("targetModuleName".equals(key)) {
                this.targetModuleName = map.get(key);
            }
            System.out.println(key + " = " + map.get(key));
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }
}