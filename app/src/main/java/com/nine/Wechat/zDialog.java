package com.nine.Wechat;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

public class zDialog extends Dialog {
    private EditText editText;
    private Button no;
    private Button yes;
    private SharedPreferences sp;
    private onNoClickListener onNoClickListener;
    private onYesClickListener onYesClickListener;
    public zDialog(@NonNull Context context) {
        super(context);
    }

    public zDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        sp = context.getSharedPreferences("Wechat_content", Context.MODE_PRIVATE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dialog);
        initView();
        initEvent();
    }
    private void initView(){
        editText=findViewById(R.id.editText);
        no=findViewById(R.id.no);
        yes=findViewById(R.id.yes);

    }
    private void initEvent(){
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNoClickListener!=null){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("content","嘤嘤嘤 我就是9岁嘛");
                    editor.apply();
                    onNoClickListener.onNoClickListener();
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onYesClickListener!=null) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("content", editText.getText().toString());
                    editor.apply();
                    Toast.makeText(getContext(), "Sucess", Toast.LENGTH_SHORT).show();
                    String value=sp.getString("content","嘤嘤嘤 我就是9岁嘛");
                    onYesClickListener.onYesClickListener(value);

                }
            }
        });
    }
    public void setOnNoClickListener(onNoClickListener onNoClickListener){
        this.onNoClickListener=onNoClickListener;
    }
    public void setOnYesClickListener(onYesClickListener onYesClickListener){
        this.onYesClickListener=onYesClickListener;
    }

    public interface onNoClickListener{
        void onNoClickListener();
    }
    public interface onYesClickListener{
        void onYesClickListener(String str);
    }

}
