package com.example.testview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToggleView mTG = (ToggleView) findViewById(R.id.tg);
        //设置基本属性
        mTG.setToggleBackgroundResource(R.drawable.slide_background);
        mTG.setSlideResource(R.drawable.slide_button);
        //设置状态
        mTG.setToggleState(false);
        //Toggle的状态监听
        mTG.setonToggleStateListener(new ToggleView.OnToggleStateListener() {
            @Override
            public void onState(boolean state) {
                if (state){
                    Toast.makeText(MainActivity.this, "当前为开", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "当前为关", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
