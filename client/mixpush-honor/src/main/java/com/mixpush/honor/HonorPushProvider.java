package com.mixpush.honor;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.hihonor.push.sdk.HonorPushClient;
import com.hihonor.push.sdk.HonorPushCallback;
import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;

/**
 * 荣耀推送Provider实现
 * 基于荣耀推送SDK (HONOR PUSH)
 */
public class HonorPushProvider extends BaseMixPushProvider {
    public static final String HONOR = "honor";
    public static final String TAG = "HonorPushProvider";
    public static String regId;

    MixPushHandler handler = MixPushClient.getInstance().getHandler();

    @Override
    public void register(Context context, RegisterType type) {
        syncGetToken(context);
    }

    @Override
    public void unRegister(Context context) {
        // 荣耀推送暂不支持反注册
    }

    @Override
    public boolean isSupport(Context context) {
        // 检查Android版本
        if (android.os.Build.VERSION.SDK_INT < 19) {
            return false;
        }
        
        // 检查厂商信息
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (!manufacturer.contains("honor")) {
            return false;
        }
        
        // 检查荣耀推送服务可用性
        try {
            boolean available = HonorPushClient.getInstance().checkSupportHonorPush(context);
            if (!available) {
                handler.getLogger().log(TAG, "荣耀推送不可用");
                return false;
            }
            return true;
        } catch (Exception e) {
            handler.getLogger().log(TAG, "检查荣耀推送可用性失败", e);
            return false;
        }
    }

    @Override
    public String getPlatformName() {
        return HonorPushProvider.HONOR;
    }

    @Override
    public String getRegisterId(Context context) {
        syncGetToken(context);
        return regId;
    }

    /**
     * 异步获取荣耀推送Token
     */
    private void syncGetToken(final Context context) {
        // 使用荣耀推送SDK 8.3版本的异步接口
        HonorPushClient.getInstance().getPushToken(new HonorPushCallback<String>() {
            @Override
            public void onSuccess(String token) {
                regId = token;
                Log.d(TAG, "获取荣耀推送Token成功: " + regId);
                
                // 回调注册成功
                if (!TextUtils.isEmpty(regId)) {
                    Log.d(TAG, "荣耀推送注册成功，RegId: " + regId);
                    MixPushPlatform mixPushPlatform = new MixPushPlatform(HonorPushProvider.HONOR, regId);
                    MixPushClient.getInstance().getHandler().getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                }
            }

            @Override
            public void onFailure(int errorCode, String errorMsg) {
                handler.getLogger().log(TAG, "荣耀推送获取Token失败 ErrorCode: " + errorCode + ", ErrorMsg: " + errorMsg +
                    " https://developer.hihonor.com/consumer/cn/doc/development/HMSCore-References/error-code");
            }
        });
    }
} 