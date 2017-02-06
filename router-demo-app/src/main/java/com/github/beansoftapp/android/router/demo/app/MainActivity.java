package com.github.beansoftapp.android.router.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.beansoftapp.android.router.HRouter;
import com.github.beansoftapp.android.router.action.HAbstractCallback;


public class MainActivity extends Activity {
    private static final int TEST_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HRouter.setScheme("app");// 设置跳转的schema
        HRouter.setupFromAssets(this);// 自动根据assets目录载入
//        HRouter.setup("Base", "Other"); 手工设置要载入的模块列表
        HRouter.setLoginInterceptor(new DemoLoginInterceptor(getApplicationContext()));

        HRouter.action("haction://IMLogoutAction");

        Object value = HRouter.action("haction://action/test");
        Toast.makeText(this, "Action执行结果:" + value, Toast.LENGTH_SHORT).show();

        value = HRouter.action("haction://action/test", "参数直接传递,例如JSON");
        Toast.makeText(this, "Action执行结果:" + value, Toast.LENGTH_SHORT).show();

        value = HRouter.action("haction://action/test?a=b");
        Toast.makeText(this, "Action带参数执行结果:" + value, Toast.LENGTH_SHORT).show();

        HRouter.action("haction://action/test?a=b", new HAbstractCallback<String>() {
            @Override
            public void start() {
                Toast.makeText(MainActivity.this, "Action执行开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void complete() {
                Toast.makeText(MainActivity.this, "Action执行结束", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ok(String o, Object response) {
                System.out.println("异步调用结束!");
            }
        });
    }

    public void testRouterApp(View view) {
        String path = "app://client/my/test?a=b&name=%E5%88%98Sir";
        if (!HRouter.open(this, path)) {
            Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
        }
    }

    // 发起 startActivityForResult, 参考类: ActivityWithResult.java
    public void testStartActivityForResult(View view) {
        String path = "app://client/my/testReturn";
        if (!HRouter.startActivityForResult(this, path, TEST_REQUEST_CODE)) {
            Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
        }
    }

    // 跳转到模块1
    public void testRouterModule1(View view) {
        String path = "app://client/module1/test?a=b&name=张三";
        Bundle bundle = new Bundle();
        bundle.putInt("test_bundle_int", 1234);
        if (!HRouter.open(this, path, bundle)) {
            Toast.makeText(this, "没有跳转成功, 请检查跳转路径 " + path, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "成功跳转到 " + HRouter.getActivityName(path).getCanonicalName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("数据回传:" + requestCode);
        if(requestCode == TEST_REQUEST_CODE && resultCode == Activity.RESULT_OK && null != data){
            Toast.makeText(this, "数据回传成功: " + data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
        }
    }
}