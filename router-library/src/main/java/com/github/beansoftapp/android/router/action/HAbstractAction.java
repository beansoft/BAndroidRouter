package com.github.beansoftapp.android.router.action;


/**
 * 抽象的动作类实现.
 * 子类:
 * public class AppCreateAction extends AbstractAction<Void> {
 *  public Object action() {
 *      return null;
 *  }
 *
 * }
 * @param <T> 参数返回结果
 */
public abstract class HAbstractAction<T> implements HAction<T> {

    public T action() {
        return null;
    }

    public T action(Object param) {
        return null;
    }

    public void action(HCallback<T> callback) {
    }

    public void action(Object param, HCallback<T> callback) {
    }
}