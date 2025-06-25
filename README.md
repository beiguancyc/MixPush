# MixPush - 统一推送SDK

MixPush是一个统一的Android推送SDK，支持集成多个厂商推送平台，包括小米、华为、荣耀、魅族、OPPO、vivo和苹果推送服务。

## 支持的推送平台

- ✅ 小米推送 (MiPush)
- ✅ 华为推送 (Huawei Push)
- ✅ **荣耀推送 (Honor Push)** - 新增支持
- ✅ 魅族推送 (Meizu Push)  
- ✅ OPPO推送 (OPPO Push)
- ✅ vivo推送 (vivo Push)
- ✅ 苹果推送 (APNs)

## 特别说明 - 荣耀推送支持

从v1.0.0版本开始，MixPush新增对荣耀推送的支持。这对于**荣耀Magic OS 8.0+设备**来说非常重要，因为荣耀设备已不再支持华为推送服务。

### 荣耀推送特性
- 支持荣耀Magic OS 8.0及以上系统
- 自动检测荣耀设备
- 透传消息和通知消息支持
- 完整的Token管理和消息处理

## 快速开始

### 1. 添加依赖

在你的项目根目录的 `build.gradle` 文件中添加JitPack仓库：

```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

在应用模块的 `build.gradle` 中添加依赖：

```gradle
dependencies {
    // MixPush核心库
    implementation 'com.github.beiguancyc.MixPush:mixpush-core:v1.0.0'
    
    // 根据需要添加对应的推送平台
    implementation 'com.github.beiguancyc.MixPush:mixpush-mi:v1.0.0'
    implementation 'com.github.beiguancyc.MixPush:mixpush-huawei:v1.0.0'
    implementation 'com.github.beiguancyc.MixPush:mixpush-honor:v1.0.0'  // 新增荣耀推送
    implementation 'com.github.beiguancyc.MixPush:mixpush-meizu:v1.0.0'
    implementation 'com.github.beiguancyc.MixPush:mixpush-oppo:v1.0.0'
    implementation 'com.github.beiguancyc.MixPush:mixpush-vivo:v1.0.0'
}
```

### 2. 初始化MixPush

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // 初始化MixPush
        MixPushClient.getInstance().init(this, new MixPushHandler() {
            @Override
            public PushReceiver getPushReceiver() {
                return new MyPushReceiver();
            }
            
            @Override
            public PassThroughReceiver getPassThroughReceiver() {
                return new MyPassThroughReceiver();
            }
            
            @Override
            public Logger getLogger() {
                return new MyLogger();
            }
        });
    }
}
```

### 3. 实现推送接收器

```java
public class MyPushReceiver implements PushReceiver {
    @Override
    public void onRegisterSucceed(Context context, MixPushPlatform platform) {
        Log.d("MixPush", "注册成功: " + platform.getPlatform() + ", Token: " + platform.getRegId());
        // 将token发送到你的服务器
    }
    
    @Override
    public void onRegisterFailed(Context context, MixPushPlatform platform) {
        Log.e("MixPush", "注册失败: " + platform.getPlatform());
    }
    
    @Override
    public void onReceiveMessage(Context context, MixPushMessage message) {
        Log.d("MixPush", "收到推送消息: " + message.getContent());
    }
    
    @Override
    public void onNotificationMessageClicked(Context context, MixPushMessage message) {
        Log.d("MixPush", "点击通知: " + message.getContent());
    }
}
```

### 4. 荣耀推送特殊配置

如果你的应用需要支持荣耀设备，需要额外配置：

#### 4.1 添加荣耀推送配置文件

从[荣耀开发者平台](https://developer.honor.com/)下载 `mcs-services.json` 配置文件并放置到 `app/` 目录下。

#### 4.2 配置AndroidManifest.xml

```xml
<application>
    <!-- 荣耀推送App ID -->
    <meta-data
        android:name="com.hihonor.push.app_id"
        android:value="你的荣耀AppID" />
</application>
```

## 设备支持检测

MixPush会自动检测设备并选择合适的推送平台：

- **荣耀设备**: 优先使用荣耀推送 (Honor Push)
- **华为设备**: 使用华为推送 (Huawei Push)  
- **小米设备**: 使用小米推送 (MiPush)
- **其他设备**: 根据厂商自动选择对应推送服务

## 版本历史

### v1.0.0 (2024-12-XX)
- ✨ 新增荣耀推送 (Honor Push) 支持
- 🔧 支持荣耀Magic OS 8.0+设备
- 📱 自动检测荣耀设备并使用荣耀推送服务
- 📚 完善荣耀推送集成文档

## 注意事项

1. **荣耀设备重要提醒**: 荣耀Magic OS 8.0+设备不再支持华为推送，必须使用荣耀推送服务
2. 每个推送平台都需要在对应的开发者平台进行配置
3. 建议根据你的目标用户群体选择需要集成的推送平台
4. 测试时请在真实设备上进行，模拟器可能无法正常工作

## 许可证

```
Copyright (C) 2024 MixPush

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 联系我们

如果你在使用过程中遇到问题，请在[GitHub Issues](https://github.com/beiguancyc/MixPush/issues)中提交。

