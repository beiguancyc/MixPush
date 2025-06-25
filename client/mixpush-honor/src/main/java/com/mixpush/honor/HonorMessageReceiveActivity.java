package com.mixpush.honor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 荣耀推送消息接收Activity
 * 处理用户点击推送通知后的跳转逻辑
 */
public class HonorMessageReceiveActivity extends Activity {
    private static final String TAG = "HonorPushProvider";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if (intent != null) {
            handlePushMessage(intent);
        }
        
        // 透明Activity，处理完消息后立即关闭
        finish();
    }

    /**
     * 处理推送消息点击
     * @param intent 包含推送消息数据的Intent
     */
    private void handlePushMessage(Intent intent) {
        try {
            MixPushHandler handler = MixPushClient.getInstance().getHandler();
            MixPushMessage mixPushMessage = new MixPushMessage();
            mixPushMessage.setPlatform(HonorPushProvider.HONOR);
            
            // 从Intent中获取推送数据
            Uri uri = intent.getData();
            if (uri != null) {
                // 解析URI参数
                Set<String> paramNames = uri.getQueryParameterNames();
                Map<String, String> params = new HashMap<>();
                for (String paramName : paramNames) {
                    params.put(paramName, uri.getQueryParameter(paramName));
                }
                mixPushMessage.setPayload(params.toString());
            }
            
            // 获取通知标题和内容
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mixPushMessage.setTitle(extras.getString("title", ""));
                mixPushMessage.setDescription(extras.getString("content", ""));
                
                // 将所有额外数据添加到payload中
                String existingPayload = mixPushMessage.getPayload();
                Map<String, String> payload = new HashMap<>();
                
                // 如果已有payload，先保留
                if (existingPayload != null && !existingPayload.isEmpty()) {
                    payload.put("existing", existingPayload);
                }
                
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    if (value instanceof String) {
                        payload.put(key, (String) value);
                    }
                }
                mixPushMessage.setPayload(payload.toString());
            }
            
            mixPushMessage.setPassThrough(false); // 这是通知栏消息点击
            
            Log.d(TAG, "荣耀推送通知被点击，处理消息: " + mixPushMessage.getTitle());
            
            // 回调消息接收处理器
            handler.getPassThroughReceiver().onReceiveMessage(this, mixPushMessage);
            
        } catch (Exception e) {
            Log.e(TAG, "处理荣耀推送消息点击失败", e);
        }
    }
} 