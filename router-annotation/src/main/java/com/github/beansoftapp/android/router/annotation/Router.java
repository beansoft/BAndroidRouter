package com.github.beansoftapp.android.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
/**
 * @Router(value = {"path1", "path2", "a/b/c"})
 * 最终生成的代码:
 * HBRouterMapping模块名
 *
public class HRouterMappingBase {
public static final void map() {
com.github.beansoftapp.android.router.HRouter.map("client/aa2",  TestActivity.class, false, "1.0", true, "", "");
com.github.beansoftapp.android.router.HRouter.map("client/bb",  TestActivity.class, false, "1.0", true, "", "");
com.github.beansoftapp.android.router.HRouter.map("client/my/test",  TestActivity.class, false, "1.0", true, "", "");
}
}
 *
 *
 */
public @interface Router {
    boolean isPublic() default true;

    boolean login() default false;

    String[] value();

    String version() default "1.0.0";
}
