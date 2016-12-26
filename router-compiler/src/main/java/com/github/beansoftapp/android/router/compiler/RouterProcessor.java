package com.github.beansoftapp.android.router.compiler;

import com.github.beansoftapp.android.router.annotation.Action;
import com.github.beansoftapp.android.router.annotation.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.github.beansoftapp.android.router.annotation.Router",
        "com.github.beansoftapp.android.router.annotation.Action"})
@SupportedOptions({"targetModuleName"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouterProcessor extends AbstractProcessor {
    private String targetModuleName = "";// 目标模块名

    private StringBuilder routerMappings = new StringBuilder();
    private StringBuilder actionMappings = new StringBuilder();

    // 此方法的代码可以用Annotation @SupportedAnnotationTypes 来简化
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        HashSet<String> annotationTypes = new HashSet<String>();
//        annotationTypes.add(Router.class.getCanonicalName());
//        annotationTypes.add(Action.class.getCanonicalName());
//        return annotationTypes;
//    }

    public RouterProcessor() {
        System.out.println(" ****** RouterProcessor 创建 ");// 只会构造一次.
    }

    @Override
    /**
     * Processes a set of annotation types on type elements
     * originating from the prior round and returns whether or not
     * these annotation types are claimed by this processor.  If {@code
     * true} is returned, the annotation types are claimed and subsequent
     * processors will not be asked to process them; if {@code false}
     * is returned, the annotation types are unclaimed and subsequent
     * processors may be asked to process them.  A processor may
     * always return the same boolean value or may vary the result
     * based on chosen criteria.
     *
     * <p>The input set will be empty if the processor supports {@code
     * "*"} and the root elements have no annotations.  A {@code
     * Processor} must gracefully handle an empty set of annotations.
     *
     * @param annotations the annotation types requested to be processed
     * @param roundEnv  environment for information about the current and prior round
     * @return whether or not the set of annotation types are claimed by this processor
     */
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println(" ****** 处理 handle RouterProcessor 数量: "+annotations.size());
        System.out.println(" ****** 当前路径:  " + new File(".").getAbsolutePath());
        // 每个模块都会处理结束一次
        System.out.println(" ****** RouterProcessor 处理结束 " + roundEnv.processingOver());

        if(targetModuleName.length() == 0) {
            throw new RuntimeException("targetModuleName为空");
        }

        if(roundEnv.processingOver()) {
            System.out.println(" ****** actionMappings: " + actionMappings);
            System.out.println(" ****** routerMappings: " + routerMappings);
            if(!new File("./doc").exists()) {
                new File("./doc").mkdir();
            }

            if(actionMappings.length() > 0) {
                try {
                    FileWriter fileWriter = new FileWriter("./doc/" + targetModuleName + "_actions.txt");
                    fileWriter.write(actionMappings.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                new File("./doc/" + targetModuleName + "_actions.txt").delete();
            }

            if(routerMappings.length() > 0) {
                try {
                    FileWriter fileWriter = new FileWriter("./doc/" + targetModuleName + "_routers.txt");
                    fileWriter.write(routerMappings.toString());
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                new File("./doc/" + targetModuleName + "_routers.txt").delete();
            }
        }


        if (annotations.size() == 0) {
            return false;
        }

        // 调试, 打印处理中的类, 可以在Gradle Console中看到如下消息:
        // 注: 处理类: com.github.beansoftapp.android.router.demo.app.TestActivity
        // 注: 处理类: com.github.beansoftapp.android.router.demo.app.action.TestAction

        Messager messager = processingEnv.getMessager();
        for (TypeElement te : annotations) {
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
                messager.printMessage(Diagnostic.Kind.NOTE, "处理类: " + e.toString());
            }
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Router.class);
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("HRouterMapping" + (targetModuleName.length() == 0 ? "Base" : targetModuleName))
                .addModifiers(Modifier.PUBLIC);


        MethodSpec.Builder mapMethodBuild = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class);


        for (Element element : elements) {
            Router routerActivity = element.getAnnotation(Router.class);
            TypeElement typeElement = (TypeElement) element;

            // TODO 检查路由路径重复问题, 编译时报错
            if(routerActivity != null) {
                String[] values = routerActivity.value();
                if(values != null) {
                    routerMappings.append(typeElement.asType()).append(" <--- ");
                    for(String value : values) {
                        mapMethodBuild.addCode("com.github.beansoftapp.android.router.HRouter.map($S,  $T.class, $L, $S, $L, \"\", \"\");\n",
                                value, typeElement.asType(), routerActivity.login(), "1.0", routerActivity.isPublic() );
                        routerMappings.append(value).append("\t\t");
                    }
                    routerMappings.append("\n");
                }
            }
        }

        typeSpec.addMethod(mapMethodBuild.build());

        MethodSpec.Builder mapActionMethodBuild = MethodSpec.methodBuilder("mapAction")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .returns(void.class);


        Set<? extends Element> elementsAction = roundEnv.getElementsAnnotatedWith(Action.class);

        System.out.println("handle Action elements " + elementsAction.size());

        for (Element element : elementsAction) {
            Action actionClass = element.getAnnotation(Action.class);
            TypeElement typeElement = (TypeElement) element;

            // TODO 检查路由路径重复问题, 编译时报错
            if(actionClass != null) {
                String[] values = actionClass.value();

                if (values != null) {
                    actionMappings.append(typeElement.asType()).append(" <--- ");
                    for (String value : values) {
                        mapActionMethodBuild.addCode("com.github.beansoftapp.android.router.HRouter.mapAction($S,  $T.class);\n",
                                value, typeElement.asType());
                        actionMappings.append(value).append("\t\t");;
                    }
                    actionMappings.append("\n");
                }
            }
        }

        typeSpec.addMethod(mapActionMethodBuild.build());

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
            System.out.println(key + " = 模块 " + map.get(key));
        }
    }

}