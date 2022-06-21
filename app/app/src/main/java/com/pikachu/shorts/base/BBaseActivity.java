package com.pikachu.shorts.base;

import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

import com.pikachu.shorts.App;

public abstract class BBaseActivity<T extends ViewBinding> extends BaseActivity<T> /*implements CustomAdapt*//*屏幕适配*/ {


    public BaseAppManager appManager;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        //setWindowBlack();
        initBActivity(savedInstanceState);
        appManager = App.getAppManager();
        appManager.addActivity(this);
    }


    public void finish(Class<?> cls) {
        appManager.finishActivity(cls);
    }


    protected abstract void initBActivity(Bundle savedInstanceState);


    //////////////// 屏幕适配 //////////////////////////
    //按true宽适配  false按高适配
/*    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 720;
    }*/
}
