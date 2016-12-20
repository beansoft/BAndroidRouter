package com.github.beansoftapp.android.router.demo.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.beansoftapp.android.router.annotation.Router;
import com.github.beansoftapp.android.router.util.BundleUtil;

@Router(value = {"client/a", "client/b", "client/my/test"})
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Bundle bundle = getIntent().getExtras();

        ((TextView)findViewById(R.id.bundleContent)).setText(this.getClass() +
                "\n" + BundleUtil.bundle2string(bundle));
    }

}