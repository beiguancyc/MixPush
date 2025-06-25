package com.mixpush.vivo;

import android.content.Context;
import android.os.Build;

import com.mixpush.core.BaseMixPushProvider;
import com.mixpush.core.MixPushPlatform;
import com.mixpush.core.RegisterType;
import com.mixpush.core.MixPushClient;
import com.mixpush.core.MixPushHandler;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;
import com.vivo.push.PushConfig;
import com.vivo.push.listener.IPushQueryActionListener;
import com.vivo.push.util.VivoPushException;

public class VivoPushProvider extends BaseMixPushProvider {
    MixPushHandler handler = MixPushClient.getInstance().getHandler();
    public static final String VIVO = "vivo";
    public static String TAG = "v-i-v-o";
    String regId;

    @Override
    public void register(Context context, RegisterType type) {
        handler.getLogger().log(TAG, "initialize");
        try {
            PushConfig config = new PushConfig.Builder()
                    .agreePrivacyStatement(true)
                    .build();
            PushClient.getInstance(context).initialize(config);
            PushClient.getInstance(context).turnOnPush(state -> {
                // 开关状态处理， 0代表成功
                if (state == 0) {
                    handler.getLogger().log(TAG, "开启成功");
                } else {
                    handler.getLogger().log(TAG, "开启失败");
                }
            });
            PushClient.getInstance(context).getRegId(new IPushQueryActionListener() {
                @Override
                public void onSuccess(String regid) {
                    regId=regid;
                    //获取成功，回调参数即是当前应用的regid；
                    MixPushPlatform mixPushPlatform = new MixPushPlatform(VivoPushProvider.VIVO, regId);
                    handler.getPushReceiver().onRegisterSucceed(context, mixPushPlatform);
                }

                @Override
                public void onFail(Integer errerCode) {
                    //获取失败，可以结合错误码参考查询失败原因；
                }});
        } catch (VivoPushException e) {
            handler.getLogger().log(TAG, "vivo 初始化失败", e);
        }

    }

    @Override
    public void unRegister(Context context) {
        PushClient.getInstance(context).turnOffPush(new IPushActionListener() {
            @Override
            public void onStateChanged(int state) {
                // 开关状态处理， 0代表成功

            }
        });
    }

    @Override
    public String getRegisterId(Context context) {
        return regId;
    }

    @Override
    public boolean isSupport(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 15) {
            return false;
        }
        String brand = Build.BRAND.toLowerCase();
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (manufacturer.equals("vivo") || brand.contains("vivo") || brand.contains("iqoo")) {
            return PushClient.getInstance(context).isSupport();
        }
        return false;
    }

    @Override
    public String getPlatformName() {
        return VivoPushProvider.VIVO;
    }
}
