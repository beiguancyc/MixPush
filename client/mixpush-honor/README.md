# MixPush 荣耀推送模块

本模块为 MixPush 统一推送SDK 提供荣耀推送（HONOR PUSH）支持。

## 概述

荣耀推送服务（HONOR PUSH）是荣耀设备专用的推送服务，独立于华为HMS推送。该模块为荣耀设备提供原生推送能力，确保在荣耀手机上获得最佳的推送体验和到达率。

## 特性

- 🚀 **原生荣耀推送支持**：基于荣耀官方推送SDK
- 📱 **设备自动识别**：自动识别荣耀设备并使用荣耀推送
- 🔔 **完整消息支持**：支持通知栏消息和透传消息
- 🎯 **点击事件处理**：完整的通知点击事件处理
- 🛡️ **异常处理**：完善的错误处理和日志记录

## 前置条件

1. **荣耀开发者账号**：需要在[荣耀开发者平台](https://developer.hihonor.com/)注册账号
2. **创建应用**：在荣耀开发者控制台创建应用并配置推送服务
3. **配置文件**：下载并配置 `mcs-services.json` 文件

## 集成步骤

### 1. 配置项目根build.gradle

首先在项目根目录的 `build.gradle` 文件中添加荣耀推送仓库和插件：

```gradle
buildscript {
    repositories {
        maven { url 'https://developer.hihonor.com/repo' }
    }
    dependencies {
        classpath 'com.hihonor.mcs:asplugin:2.0.1.300'  // 荣耀推送插件（必需）
    }
}

allprojects {
    repositories {
        maven { url 'https://developer.hihonor.com/repo' }
    }
}
```

### 2. 添加依赖

在您的应用模块的 `build.gradle` 文件中：

```gradle
apply plugin: 'com.hihonor.mcs.asplugin'  // 应用荣耀推送插件

dependencies {
    implementation project(path: ':mixpush-core')
    implementation project(path: ':mixpush-honor')
    // 其他推送模块...
}
```

**注意**: 荣耀推送模块内部已包含 `com.hihonor.mcs:push:8.0.12.307` 依赖，无需额外添加。

### 3. 配置文件

将从荣耀开发者控制台下载的 `mcs-services.json` 文件放置到应用模块的 `src/main/` 目录下。

**重要**: `com.hihonor.mcs:asplugin` 插件会自动处理 `mcs-services.json` 配置文件，类似华为推送的 `agconnect-services.json` 处理方式。

### 4. 权限配置

荣耀推送模块会自动添加必要的权限，无需手动配置。

### 5. 初始化

在您的 Application 中初始化 MixPush：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 设置日志处理器
        MixPushClient.getInstance().setLogger(object : MixPushLogger {
            override fun log(tag: String, content: String, throwable: Throwable?) {
                Log.e(tag, content, throwable)
            }
            override fun log(tag: String, content: String) {
                Log.e(tag, content)
            }
        })
        
        // 设置推送消息接收器
        MixPushClient.getInstance().setPushReceiver(MyMixPushReceiver())
        
        // 注册推送服务
        MixPushClient.getInstance().register(this)
    }
}
```

### 6. 消息接收

实现推送消息接收器：

```kotlin
class MyMixPushReceiver : MixPushReceiver() {
    override fun onRegisterSucceed(context: Context, platform: MixPushPlatform) {
        Log.d("MixPush", "注册成功: ${platform.platformName}, RegId: ${platform.regId}")
    }
    
    override fun onRegisterFailed(context: Context, platform: MixPushPlatform) {
        Log.e("MixPush", "注册失败: ${platform.platformName}")
    }
    
    override fun onReceiveMessage(context: Context, message: MixPushMessage) {
        Log.d("MixPush", "收到消息: ${message.title}")
    }
}
```

## 配置说明

### manifestPlaceholders

如果需要在 AndroidManifest 中使用动态配置，可以在 `build.gradle` 中配置：

```gradle
android {
    defaultConfig {
        // 其他配置...
        manifestPlaceholders["HONOR_APP_ID"] = "your_honor_app_id"
    }
}
```

### 混淆配置

荣耀推送模块已包含必要的混淆规则，无需额外配置。

## 技术规格

- **最低SDK版本**：API 19 (Android 4.4)
- **目标设备**：荣耀品牌手机
- **推送类型**：通知栏消息、透传消息
- **配置文件**：mcs-services.json

## 常见问题

### Q: 如何判断设备是否支持荣耀推送？
A: 模块会自动检测设备制造商和荣耀推送服务可用性，无需手动判断。

### Q: 荣耀推送和华为推送的区别？
A: 荣耀推送是独立的推送服务，使用不同的SDK和配置文件，两者不兼容。

### Q: 为什么获取不到Token？
A: 请检查：
1. `mcs-services.json` 文件是否正确配置
2. 应用签名是否与控制台配置一致
3. 设备是否为荣耀品牌且支持荣耀推送服务

### Q: 如何调试推送问题？
A: 建议：
1. 开启详细日志输出
2. 检查网络连接状态
3. 确认应用已正确配置推送权限

## 更新日志

### v1.0.0
- 初始版本
- 支持荣耀推送服务集成
- 支持通知栏消息和透传消息
- 支持消息点击事件处理

## 支持

如有问题，请查看：
- [荣耀开发者文档](https://developer.hihonor.com/consumer/cn/doc/development/HMSCore-Guides/service-introduction-0000001050040060)
- [MixPush 项目文档](../README.md)

## 许可证

本模块遵循项目主许可证。 