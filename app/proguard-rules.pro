# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5  # 指定代码的压缩级别
#【混淆时不会产生形形色色的类名 】  # 是否使用大小写混合
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses
#【指定不去忽略非公共的库类。 】
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontshrink
#【不预校验】
-dontpreverify
-verbose
#【优化】
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontobfuscate
-dontoptimize

-ignorewarnings


-dontwarn org.springframework.**
-dontwarn com.j256.ormlite.**
-dontwarn org.codehaus.jackson.**
-dontwarn me.himanshusoni.quantityview.**
-dontwarn com.squareup.otto.**
-dontwarn com.baidu.**
-dontwarn com.marshalchen.ultimaterecyclerview.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.nineoldandroids.**
-dontwarn com.fasterxml.jackson.databind.**
-dontwarn com.liulishuo.magicprogresswidget.**
-dontwarn com.daimajia.slider.library.**
-dontwarn me.iwf.photopicker.**
-dontwarn in.srain.cube.views.ptr.**
-dontwarn de.hdodenhof.circleimageview.**
-dontwarn okio.**

-keepattributes *Annotation*
-keep class com.zczczy.microwarehouse.model.** { *;}
-keep class com.zczczy.microwarehouse.dao.** { *;}
-keep class org.springframework.** { *;}
-keep class com.j256.ormlite.** { *;}
-keep class com.google.gson.** { *;}
-keep class org.codehaus.jackson.** { *;}
-keep class me.himanshusoni.quantityview.** { *;}
-keep class com.squareup.otto.** { *;}
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class com.marshalchen.ultimaterecyclerview.** { *;}

# nineoldandroids
-keep interface com.nineoldandroids.view.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class com.fasterxml.jackson.databind.** { *; }
-keep class com.liulishuo.magicprogresswidget.** { *; }
-keep class com.daimajia.slider.library.** { *; }
-keep class me.iwf.photopicker.** { *; }
-keep class in.srain.cube.views.ptr.** { *; }
-keep class de.hdodenhof.circleimageview.** { *; }
-keep class okio.** { *; }


-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}




-keep public class * extends android.app.Application
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#-keep public class * extends android.app.Fragment
#-keep public class * extends android.app.Activity
#-keep public class * extends android.support.v7.app.AppCompatActivity
#-keep public class * extends android.support.v4.app.Fragment
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.support.v4.**


# support-v7-appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class com.zczczy.leo.microwarehouse.viewgroup.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
# support-design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}




#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt








