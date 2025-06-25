package com.mixpush.honor;

import com.hihonor.push.sdk.HonorMessageService;
import com.hihonor.push.sdk.HonorPushDataMsg;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.mixpush.core.MixPushMessage;
import android.util.Log;

/**
 * 荣耀推送消息服务
 * 继承HonorMessageService，处理荣耀推送消息
 */
public class MixPushHonorMessageService extends HonorMessageService {
    public static final String TAG = "HonorPushProvider";
    private MixPushHandler handler = MixPushClient.getInstance().getHandler();

    /**
     * Device token发生变化时的回调
     * @param token 新的推送token
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "荣耀推送Token更新: " + token);
        
        if (handler != null) {
            // 通知MixPush Token已更新
            MixPushPlatform platform = new MixPushPlatform(HonorPushProvider.HONOR, token);
            handler.getPushReceiver().onRegisterSucceed(getApplicationContext(), platform);
        }
    }

    /**
     * 接收透传消息方法
     * @param honorPushDataMsg 荣耀推送数据消息
     */
    @Override
    public void onMessageReceived(HonorPushDataMsg honorPushDataMsg) {
        super.onMessageReceived(honorPushDataMsg);
        
        Log.d(TAG, "收到荣耀推送消息: " + honorPushDataMsg.getData());
        
        if (handler == null) {
            Log.e(TAG, "MixPushHandler is null");
            return;
        }

        try {
            MixPushMessage mixPushMessage = new MixPushMessage();
            mixPushMessage.setPlatform(HonorPushProvider.HONOR);
            
            // 获取消息数据
            String data = honorPushDataMsg.getData();
            
            // 荣耀推送主要用于透传消息
            mixPushMessage.setPassThrough(true);
            
            // 设置消息数据
            mixPushMessage.setPayload(data);
            
            // 回调透传消息接收器
            handler.getPassThroughReceiver().onReceiveMessage(getApplicationContext(), mixPushMessage);
            
        } catch (Exception e) {
            Log.e(TAG, "处理荣耀推送消息失败", e);
        }
    }
} 