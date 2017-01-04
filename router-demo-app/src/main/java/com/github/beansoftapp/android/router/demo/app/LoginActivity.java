package com.github.beansoftapp.android.router.demo.app;

import android.app.Activity;
import android.os.Bundle;

import com.github.beansoftapp.android.router.annotation.Router;

@Router("user/login")
public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
