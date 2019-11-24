package com.highbuilding.butterknife;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xr.autobind.annotation.BindView;
import com.xr.viewbind.InjectView;


public class AutoBindActivity extends AppCompatActivity {
    @BindView(R.id.textView1)
    public TextView tv1;

    @BindView(R.id.textView2)
    public TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_bind);
        InjectView.bind(this);
        tv1.setText("l love wj");

    }
}
