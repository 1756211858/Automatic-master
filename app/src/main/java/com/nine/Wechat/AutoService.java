package com.nine.Wechat;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Delayed;

import static android.content.ContentValues.TAG;
/*
            手动点击item
 */
public class AutoService extends AccessibilityService {
    private static boolean prepos=true; //当前的页面
    private int page = 1;//默认页数
    private static int index = 0;
    private int eventType;
    private String data;//接受传递来的请求内容
    private static String className=null;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        eventType = event.getEventType();
        className = event.getClassName().toString();

        switch (className) {
            case "com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI":
                //addFriends();
                Log.e("mlog","addfriends");
                break;
            case "com.tencent.mm.plugin.profile.ui.ContactInfoUI":
                greet();
                break;
        }

}
    @Override
    public void onInterrupt() {

    }
    /*
            当服务绑定成功
    */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        SharedPreferences settings = getSharedPreferences("Wechat_content", 0);
        data=settings.getString("content","嘤嘤嘤 我就是9岁嘛");
    }

    /*
        点击添加附近的人item
         */
    private void addFriends() {
     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             index += 1;
             AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
             List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cjc");
             if (list.size()!=0) {
                 List<AccessibilityNodeInfo> username=nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cjp");
                 if(username.size()!=0) {
                     if (username.get(index).getParent() != null) {
                         username.get(index).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                         username.get(index).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                     }
                 }

             } else if (index == list.size()) {
                         //滑动
                         AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(index);
                         node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

             }
         }
     },300);
    }

    /*
    详细资料
     */
    private void greet() {
new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        List<AccessibilityNodeInfo> infos = getRootInActiveWindow().findAccessibilityNodeInfosByText("打招呼");
        if(infos.size()>0&&prepos) {
            for (AccessibilityNodeInfo item : infos) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inputHello(data);
                    }
                },200);
            }
        }else{
            index++;
            prepos=true;
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }
},400);

    }
    /*
     * 发送请求
     */
    private void sendIt() {

        List<AccessibilityNodeInfo> infos =  getRootInActiveWindow().findAccessibilityNodeInfosByText("发送");
        for (AccessibilityNodeInfo item : infos) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            prepos=false;
        }

    }

    /**
     * 自动输入打招呼内容
     */
    private void inputHello(String hello) {

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        //找到当前获取焦点的view
        AccessibilityNodeInfo target = nodeInfo.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (target == null) {
            Log.d(TAG, "inputHello: null");
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", hello);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
        target.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        /*
        发送请求
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendIt();
            }
        },400);
    }

}
