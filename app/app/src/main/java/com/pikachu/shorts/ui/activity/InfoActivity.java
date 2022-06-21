package com.pikachu.shorts.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pikachu.shorts.App;
import com.pikachu.shorts.R;
import com.pikachu.shorts.adapter.IndexRecyclerViewAdapter;
import com.pikachu.shorts.base.BBaseActivity;
import com.pikachu.shorts.base.BaseBottomSheetDialog;
import com.pikachu.shorts.cls.DataCls;
import com.pikachu.shorts.cls.ShDataCls;
import com.pikachu.shorts.databinding.ActivityInfoBinding;
import com.pikachu.shorts.databinding.UiDialogBinding;
import com.pikachu.shorts.databinding.UiDialogEditBinding;
import com.pikachu.shorts.databinding.UiLoadBinding;
import com.pikachu.shorts.tool.IndexCmp;
import com.pikachu.shorts.tool.IndexInterface;
import com.pikachu.shorts.tool.InfoInterface;
import com.pikachu.shorts.utils.AESCBCUtils;
import com.pikachu.shorts.utils.OtherUtils;
import com.pikachu.shorts.utils.ToastUtils;

public class InfoActivity extends BBaseActivity<ActivityInfoBinding> implements InfoInterface {

    private IndexCmp instance;
    @SuppressLint("StaticFieldLeak")
    public  BaseBottomSheetDialog<UiLoadBinding> dialog2;
    private BaseBottomSheetDialog<UiDialogBinding> dialog1;
    private DataCls.InfoBean info;

    @Override
    protected void initBActivity(Bundle savedInstanceState) {
        binding.pkTor.setTitle(R.string.app_info);
        setSupportActionBar(binding.pkTor);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        instance = IndexCmp.getInstance(InfoActivity.this, this);
        instance.startLookInfo(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5());
        dialog2 = ToastUtils.showLoadDialog(InfoActivity.this, "同步中....");
        dialog2.binding.dP1.setVisibility(View.VISIBLE);
        dialog2.binding.dRoot.setVisibility(View.GONE);
        dialog2.show();
        clickInfo();
    }


    private void clickInfo() {
        binding.infoLin9.setOnClickListener(view -> binding.infoS1.setChecked(!binding.infoS1.isChecked()));
        binding.infoLin12.setOnClickListener(view -> binding.infoS2.setChecked(!binding.infoS2.isChecked()));
        binding.infoLin15.setOnClickListener(view -> binding.infoS3.setChecked(!binding.infoS3.isChecked()));

        binding.infoLin1.setOnClickListener(view ->
                startDialog(binding.infoT1, "设置数据库地址", "数据库地址", 100, -1, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin2.setOnClickListener(view ->
                startDialog(binding.infoT2, "设置数据库用户名", "数据库用户名", 50, -1, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin3.setOnClickListener(view ->
                startDialog(binding.infoT3, "设置数据库用户密码", "数据库用户密码", 50, -1, InputType.TYPE_TEXT_VARIATION_PASSWORD, false));
        binding.infoLin4.setOnClickListener(view ->
                startDialog(binding.infoT4, "设置数据库名", "数据库名", 50, -1, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin5.setOnClickListener(view ->
                startDialog(binding.infoT5, "设置数据库表名", "数据库表名", 50, -1, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin6.setOnClickListener(view ->
                startDialog(binding.infoT6, "设置短链长度", "短链长度", 2, 1, InputType.TYPE_CLASS_NUMBER, true));
        binding.infoLin7.setOnClickListener(view ->
                startDialog(binding.infoT7, "设置短链出错跳转地址", "短链出错跳转", -1, 100, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin8.setOnClickListener(view ->
                startDialog(binding.infoT8, "设置短链禁用跳转地址", "短链禁用跳转", -1, 100, InputType.TYPE_CLASS_TEXT, false));
        binding.infoLin10.setOnClickListener(view ->
                startDialog(binding.infoT9, "设置链接过期时间", "链接过期时间", 3, 10, InputType.TYPE_CLASS_NUMBER, true));
        binding.infoLin11.setOnClickListener(view ->
                startDialog(binding.infoT10, "设置加密密钥", "加密密钥", 20, -1, InputType.TYPE_CLASS_TEXT, true));
        binding.infoLin13.setOnClickListener(view ->
                startDialog(binding.infoT11, "设置管理员账号", "管理员账号", 20, -1, InputType.TYPE_CLASS_TEXT, true));
        binding.infoLin14.setOnClickListener(view ->
                startDialog(binding.infoT12, "设置管理员密码", "管理员密码", 20, -1, InputType.TYPE_CLASS_TEXT, true));
    }


    //toolbar菜单
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单点击事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1_1:
                showToast(getString(R.string.item1_1));
                DataCls.InfoBean data = getData();
                if (data == null)
                    break;
                instance.startEditInfo(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), data);
                dialog2.binding.dT1.setText("保存中....");
                dialog2.binding.dP1.setVisibility(View.VISIBLE);
                dialog2.binding.dRoot.setVisibility(View.GONE);
                dialog2.show();
                break;
 /*           case R.id.item1_3:
                showToast(getString(R.string.item1_3));

                break;*/
            case android.R.id.home:
                sActivity();
        }
        return false;
    }

    private DataCls.InfoBean getData() {
        try {
            DataCls.InfoBean infoBean = new DataCls.InfoBean();
            infoBean.setPk_servername(binding.infoT1.getText().toString());
            infoBean.setPk_username(binding.infoT2.getText().toString());
            infoBean.setPk_password(binding.infoT3.getText().toString());
            infoBean.setPk_data(binding.infoT4.getText().toString());
            infoBean.setPk_data_table(binding.infoT5.getText().toString());
            infoBean.setPk_key_len(Integer.parseInt(binding.infoT6.getText().toString()));
            infoBean.setPk_err_url(binding.infoT7.getText().toString());
            infoBean.setPk_open_url(binding.infoT8.getText().toString());
            infoBean.setPk_time(Integer.parseInt(binding.infoT9.getText().toString()));
            infoBean.setPk_key(binding.infoT10.getText().toString());
            infoBean.setPk_root_user(binding.infoT11.getText().toString());
            infoBean.setPk_root_pass(binding.infoT12.getText().toString());
            infoBean.setPk_debug(binding.infoS1.isChecked());
            infoBean.setPk_is_open(binding.infoS2.isChecked());
            infoBean.setPk_is_htaccess(binding.infoS3.isChecked());
            return infoBean;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 编辑对话框
     *
     * @param textView      要设置的textView
     * @param title         标题
     * @param eHint         编辑框提示文字
     * @param limit         最长多少
     * @param type          InputType.TYPE_CLASS_TEX ....
     * @param isSpecialChar 是否开启特殊字符检测
     */
    public void startDialog(TextView textView, String title, String eHint, int limit, int minNmu, int type, boolean isSpecialChar) {
        dialog1 = new BaseBottomSheetDialog<UiDialogBinding>(context) {

            @SuppressLint("SetTextI18n")
            @Override
            protected void onViewCreate(UiDialogBinding binding) {
                binding.dT1.setText(title);
                String s = textView.getText().toString();
                binding.dE1.setText(s);
                binding.dE1.setHint(eHint);
                binding.dE1.setInputType(type);
                binding.dCn.setOnClickListener(view -> dismiss());
                binding.dOk.setOnClickListener(view -> {
                    String text = binding.dE1.getText().toString();
                    if (text.equals("")) {
                        showToast("不能为空");
                        return;
                    }

                    if (text.length() > limit && limit != -1) {
                        showToast("太长了，最长" + limit);
                        return;
                    }

                    if (minNmu != -1){
                        int i = 0;
                        try {
                            i = Integer.parseInt(text);
                            if (i < minNmu){
                                showToast("太小了，最小" + minNmu);
                                return;
                            }
                        }catch (Exception e){
                            i = text.length();
                            if (i >= minNmu){
                                showToast("太长了，最长" + minNmu);
                                return;
                            }
                        }
                    }
                    if (OtherUtils.isSpecialChar(text) && isSpecialChar) {
                        showToast("不能包含特殊字符");
                        return;
                    }
                    textView.setText(text);
                    dismiss();
                });

            }
        };
        dialog1.show();
    }


    @Override
    public void lookInfoError(DataCls dataCls) {
        dialog2.dismiss();
        showToast("同步失败：" + dataCls.getCode());
        finish();
    }


    @Override
    public void lookInfoOk(DataCls dataCls) {
        showToast("同步成功");
        info = dataCls.getInfo();
        setViewText(info);
        dialog2.dismiss();
        MainActivity.shortHost = dataCls.getShortX();
    }

    @Override
    public void editInfoError(DataCls dataCls) {
        dialog2.dismiss();
        showToast("保存失败：" + dataCls.getCode());
    }

    @Override
    public void editInfoOk(DataCls dataCls) {
        dialog2.dismiss();
        showToast("保存成功");
        info = dataCls.getInfo();
        setViewText(info);
        App.shDataCls.setKey(info.getPk_key());
        String userMd5 = info.getPk_root_user() + info.getPk_root_pass();
        userMd5 = AESCBCUtils.Md5(userMd5, true);
        App.shDataCls.setUserMd5(userMd5);
        App.writeInfo(App.shDataCls);
        MainActivity.shortHost = dataCls.getShortX();
    }


    @SuppressLint("SetTextI18n")
    private void setViewText(DataCls.InfoBean info) {
        binding.infoT1.setText(info.getPk_servername() + "");
        binding.infoT2.setText(info.getPk_username() + "");
        binding.infoT3.setText(info.getPk_password() + "");
        binding.infoT4.setText(info.getPk_data() + "");
        binding.infoT5.setText(info.getPk_data_table() + "");
        binding.infoT6.setText(info.getPk_key_len() + "");
        binding.infoT7.setText(info.getPk_err_url() + "");
        binding.infoT8.setText(info.getPk_open_url() + "");
        binding.infoT9.setText(info.getPk_time() + "");
        binding.infoT10.setText(info.getPk_key() + "");
        binding.infoT11.setText(info.getPk_root_user() + "");
        binding.infoT12.setText(info.getPk_root_pass() + "");
        binding.infoS1.setChecked(info.getPk_debug());
        binding.infoS2.setChecked(info.getPk_is_open());
        binding.infoS3.setChecked(info.getPk_is_htaccess());
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            sActivity();
        return false;
    }
    private void sActivity(){
        if (info == null){
            finish();
            return;
        }
        DataCls.InfoBean data = getData();
        if (data == null){
            finish();
            return;
        }
        if (!info.isContrast(data)) {
            dialog2.binding.dP1.setVisibility(View.GONE);
            dialog2.binding.dRoot.setVisibility(View.VISIBLE);
            dialog2.binding.dT1.setText("你已修改配置，是否保存？");

            dialog2.binding.dB1.setText("保存");
            dialog2.binding.dB1.setOnClickListener(view -> {
                dialog2.binding.dP1.setVisibility(View.VISIBLE);
                dialog2.binding.dRoot.setVisibility(View.GONE);

                instance.startEditInfo(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), data);
                dialog2.binding.dT1.setText("保存中....");
                dialog2.setCancel(false);
                dialog2.setCanSlide(false);
            });

            dialog2.binding.dB2.setText("退出");
            dialog2.binding.dB2.setOnClickListener(view -> finish());
            dialog2.setCancel(true);
            dialog2.setCanSlide(true);
            dialog2.show();
        }else {
            finish();
        }

    }

}