package com.pikachu.shorts.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.pikachu.shorts.utils.LogsUtils;
import com.pikachu.shorts.utils.ToastUtils;
import com.pikachu.shorts.utils.ViewBindingUtils;

import java.io.Serializable;


/**
 * build 加
 *
 *
 * buildFeatures {
 * viewBinding true
 * }
 *
 *
 */

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    public final static String START_STR = "PIKACHU";
    protected static final String TAG = "---" + BaseActivity.class.getCanonicalName();
    protected Context context;
    protected T binding;

    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setImmersionBar().init();
        binding = ViewBindingUtils.create(getClass(), LayoutInflater.from(this));
        setContentView(binding.getRoot());
        context = this;
        initActivity(savedInstanceState);
    }


    protected abstract void initActivity(Bundle savedInstanceState);


    public void showToast(String msg) {
        ToastUtils.showToast(this, msg);
    }

    public void showLog(String tag, String msg) {
        LogsUtils.showLog(tag, msg);
    }

    public void showLog(String msg) {
        LogsUtils.showLog(msg);
    }


    /////////////////////////////////////// 状态栏 /////////////////////////////////////////////////

    /**
     * 重写此处 更改状态栏颜色
     * <!-- 导入包 -->
     * implementation 'com.gyf.immersionbar:immersionbar:3.0.0'
     * implementation 'com.gyf.immersionbar:immersionbar-components:3.0.0' // fragment快速实现（可选）
     *
     * <!-- 地址 -->
     * https://github.com/gyf-dev/ImmersionBar
     *
     * <!-- 全面屏 -->
     * 在manifest的Application节点中加入
     * android:resizeableActivity="true"
     *
     * <!--适配华为（huawei）刘海屏-->
     * <meta-data
     * android:name="android.notch_support"
     * android:value="true"/>
     *
     * <!--适配小米（xiaomi）刘海屏-->
     * <meta-data
     * android:name="notch.config"
     * android:value="portrait|landscape" />
     *
     * <!--androidx-->
     * android.enableJetifier=true
     *
     * @return ImmersionBar
     */
    protected ImmersionBar setImmersionBar() {
        return ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true); // 解决布局与状态栏重叠问题
    }

    /**
     * 设置状态栏为白色  字体为黑色
     */
    protected void setWindowBlack() {
        ImmersionBar.with(this)
                .barColor(android.R.color.white)
                .statusBarDarkFont(true)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true) // 解决布局与状态栏重叠问题
                .init();

    }

    /**
     * 设置状态栏为黑色 字体为白色
     */
    protected void setWindowWhite() {
        ImmersionBar.with(this)
                .barColor(android.R.color.black)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(true)
                .init();
    }


    //设置 状态栏字体 导航栏图标 为黑色
    protected void setWindowTextBlack() {
        ImmersionBar.with(this)
                .statusBarDarkFont(true) //  true 深色
                .fitsSystemWindows(true)
                .navigationBarDarkIcon(true)
                .init();
    }

    //设置 状态栏字体 导航栏图标 为白色
    protected void setWindowTextWhite() {
        ImmersionBar.with(this)
                .statusBarDarkFont(false) //  true 深色
                .fitsSystemWindows(false)
                .fitsSystemWindows(true)
                .init();
    }


    //设置 全屏
    protected void setWindowFullScreen() {
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
    }


    //设置 布局占用状态栏
    protected void setViewToWindow() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .init();
    }

    // 设置状态栏颜色
    protected ImmersionBar setBarColor(@ColorRes int color, boolean isDarkFont ) {
        return ImmersionBar.with(this)
                .barColor(color)
                .statusBarDarkFont(isDarkFont)  // 状态栏字体深色或亮色   true 深色
                .fitsSystemWindows(true); // 解决布局与状态栏重叠问题
    }


/////////////////////////////////////// 状态栏 /////////////////////////////////////////////////


/////////////////////////////////////// 数据获取 /////////////////////////////////////////////////


    /**
     * 页面跳转
     */
    protected void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }


    /**
     * 带数据页面跳转 序列化
     */
    protected void startActivity(Class<?> clz, Serializable cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转  int
     */
    protected void startActivity(Class<?> clz, int cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 string
     */
    protected void startActivity(Class<?> clz, String cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }

    /**
     * 带数据页面跳转 float
     */
    protected void startActivity(Class<?> clz, float cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }


    /**
     * 带数据页面跳转 long
     */
    protected void startActivity(Class<?> clz, long cls) {
        Intent intent = new Intent(BaseActivity.this, clz);
        intent.putExtra(START_STR, cls);
        startActivity(intent);
    }


    /**
     * 简单的  Activity 接收信息
     */
    public String getStringExtra(String key) {
        intent = getIntent();
        return intent.getStringExtra(key);
    }

    public int getIntExtra(String key, int defaultValue) {
        intent = getIntent();
        return intent.getIntExtra(key, defaultValue);
    }


    public long getLongExtra(String key, int defaultValue) {
        intent = getIntent();
        return intent.getLongExtra(key, defaultValue);
    }

    public float getFloatExtra(String key, float defaultValue) {
        intent = getIntent();
        return intent.getFloatExtra(key, defaultValue);
    }

    public <t> t getSerializableExtra(String key, Class<t> cls) {
        return cls.cast(getIntent().getSerializableExtra(key));
    }




    /////////////////   回传跳转 /////////////////////////////////////
    // 回传跳转
    public void startActivityForResult(Class<?> clz, int req){
        Intent intent = new Intent(BaseActivity.this, clz);
        startActivityForResult(intent, req); // 可以使用 最近 回传方式 Activity Results API
    }

    public void finish(int req) {
        setResult(req, getIntent());
        finish();
    }















    public String getStringExtra() {
        return getStringExtra(START_STR);
    }

    public int getIntExtra() {
        return getIntExtra(START_STR, 0);
    }

    public long getLongExtra() {
        return getLongExtra(START_STR, -1);
    }

    public float getFloatExtra() {
        return getFloatExtra(START_STR, 0F);
    }

    public <t> t getSerializableExtra(Class<t> cls) {
        return getSerializableExtra(START_STR, cls);
    }


}
