package com.nine.Wechat;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import static android.content.ContentValues.TAG;

/*
            手动点击item
 */
public class AutoService extends AccessibilityService {
    private static boolean prepos = true; //当前的页面
    private int index = 0;
    private int eventType;
    private String data;//接受传递来的请求内容
    private static String className = null;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        eventType = event.getEventType();
        className = event.getClassName().toString();

                switch (className) {
                    case "com.tencent.mm.plugin.nearby.ui.NearbyFriendsUI":
                        addFriends();
                        Log.e("mlog", "addfriends");
                        break;
                    case "com.tencent.mm.plugin.profile.ui.ContactInfoUI":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                greet();
                            }
                        }, 1000);
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
        data = settings.getString("content", "嘤嘤嘤 我就是9岁嘛");
    }

    /*
        点击添加附近的人item
         */
    private void addFriends() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                List<AccessibilityNodeInfo> username = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/b0p");
                if (index == username.size() - 1) {
                    index = 0;
                    //滑动
                    AccessibilityNodeInfo nodeInfo_ = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> list_ = nodeInfo_.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/cjc");
                    list_.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    Log.d("mlog", "列表人数: " + list_.size());
                } else {
                    username.get(index).getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    index += 1;

                }
            }
        },1000);

    }


    /*
    详细资料
     */
    private void greet() {
        List<AccessibilityNodeInfo> infos = getRootInActiveWindow().findAccessibilityNodeInfosByText("打招呼");
        if (infos.size() > 0 && prepos) {
            for (AccessibilityNodeInfo item : infos) {
                item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        inputHello(data);
                    }
                }, 200);
            }
        } else {
            prepos = true;
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    /*
     * 发送请求
     */
    private void sendIt() {

        List<AccessibilityNodeInfo> infos = getRootInActiveWindow().findAccessibilityNodeInfosByText("发送");
        for (AccessibilityNodeInfo item : infos) {
            item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            prepos = false;
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
        }, 1000);
    }

}
