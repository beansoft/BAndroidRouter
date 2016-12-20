package com.github.beansoftapp.android.router.demo.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.beansoftapp.android.router.HRouter;
import com.github.beansoftapp.android.router.action.HAbstractCallback;
import com.github.beansoftapp.android.router.action.HCallback;

import java.net.HttpCookie;
import java.util.List;

import static android.R.attr.path;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HRouter.setScheme("app");// 设置跳转的schema
        HRouter.setup("Base", "Other");

        Object value = HRouter.action("haction://action/test");
        Toast.makeText(this, "Action执行结果:" + value, Toast.LENGTH_SHORT).show();

        value = HRouter.action("haction://action/test?a=b");
        Toast.makeText(this, "Action带参数执行结果:" + value, Toast.LENGTH_SHORT).show();

        HRouter.action("haction://action/test?a=b", new HAbstractCallback<String>() {
            @Override
            public void start() {

            }

            @Override
            public void complete() {

            }

            @Override
            public void failure(Throwable exception) {

            }

            @Override
            public void ok(String o, Object response) {

            }
        });
    }

    public void testRouterApp(View view) {
        String path = "app://client/my/test?a=b&name=%E5%88%98Sir";
        if(!HRouter.open(this, path)) {
            Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
        }
    }

    // 跳转到模块1
    public void testRouterModule1(View view) {
        String path = "app://client/module1/test?a=b&name=张三";
        if(!HRouter.open(this, path)) {
            Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
        }
    }
}
