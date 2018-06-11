package com.nine.Wechat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hanks.htextview.scale.ScaleTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements TVContent {


    @BindView(R.id.fly_now)
    Button flyNow;
    @BindView(R.id.textView)
    ScaleTextView textView;
    private zDialog zDialog;
    public String data;//用于保存输入的添加好友的内容
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    public String getContent() {

        return data;
    }

    @OnClick({R.id.fly_now,R.id.textView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fly_now:
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                Bundle bundle=new Bundle();
                bundle.putString("data",data);
                startActivity(intent,bundle);
                break;
            case R.id.textView:
                zDialog=new zDialog(MainActivity.this,R.style.dialog_theme);


                zDialog.setOnNoClickListener(new zDialog.onNoClickListener() {
                    @Override
                    public void onNoClickListener() {
                        Toast.makeText(getApplicationContext(),"将使用默认请求",Toast.LENGTH_SHORT).show();
                        zDialog.dismiss();
                    }
                });
                zDialog.setOnYesClickListener(new zDialog.onYesClickListener() {
                    @Override
                    public void onYesClickListener(String str) {
                        Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                        data=str;
                        zDialog.dismiss();
                    }
                });
                zDialog.show();
                zDialog.setCancelable(false);
                break;
        }
    }

}
