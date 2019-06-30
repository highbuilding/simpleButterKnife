package com.highbuilding.butterknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.highbuilding.rejectview.ContentView;
import com.highbuilding.rejectview.ViewInject;
import com.highbuilding.rejectview.ViewInjectHelper;

@ContentView(value = R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.tv_test)
    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectHelper.init(this);
        tvTest.setText("ioc test");
    }
}
