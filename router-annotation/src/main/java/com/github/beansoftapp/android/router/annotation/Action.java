package com.github.beansoftapp.android.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)

/**
 * @Action(value = {"app/show_update"})
public class ShowUpdateAction extends HAbstractAction<Void> {
    public Object action() {
        return null;
    }
}
 */
// 此类暂时未用
public @interface Action {
    String[] value();
}