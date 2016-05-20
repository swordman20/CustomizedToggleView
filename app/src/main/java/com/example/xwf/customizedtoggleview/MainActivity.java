package com.example.xwf.customizedtoggleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ToggleView tgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tgView = (ToggleView) findViewById(R.id.tg);
        //设置一个开关的背景
        tgView.setToggleBackgroundResource(R.drawable.switch_background);
        //设置开关的滑动块的背景
        tgView.setToggleSlidgroundResource(R.drawable.switch_background);
        //设置开关的状态
//        tgView.setToggleState(false);

        //设置开关的监听状态
        tgView.setOnToggleStateListener(new ToggleView.OnToggleStateListener() {
            @Override
            public void onToggleChange(boolean state) {
                if (state) {
                    Toast.makeText(getApplicationContext(), "当前为开", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "当前为关", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
