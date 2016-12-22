package com.github.beansoftapp.android.router.demo.app.action;

import com.github.beansoftapp.android.router.action.HAbstractAction;
import com.github.beansoftapp.android.router.annotation.Action;

/**
 * IM 登出动作.
 * Created by beansoft on 16/12/22.
 */
@Action(value = {"IMLogoutAction"})
public class IMLogoutAction extends HAbstractAction<Void> {
    public Void action() {
        System.out.println( "IMLogoutAction" );
        return null;
    }
}
