package com.github.beansoftapp.android.router.demo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.beansoftapp.android.router.annotation.Router;

@Router("client/my/testReturn")
public class ActivityWithResult extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_result);
    }

    public void backData(View view) {
        onResultOkClick();
        this.finish();
    }

    private void onResultOkClick() {
        Intent data = new Intent();
        data.putExtra("data", "测试startActivityForResult");
        setResult(RESULT_OK, data);
    }

}
