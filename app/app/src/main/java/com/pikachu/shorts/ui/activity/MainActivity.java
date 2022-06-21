package com.pikachu.shorts.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pikachu.shorts.App;
import com.pikachu.shorts.R;
import com.pikachu.shorts.adapter.IndexRecyclerViewAdapter;
import com.pikachu.shorts.base.BBaseActivity;
import com.pikachu.shorts.base.BaseBottomSheetDialog;
import com.pikachu.shorts.cls.DataCls;
import com.pikachu.shorts.cls.ShDataCls;
import com.pikachu.shorts.databinding.ActivityMainBinding;
import com.pikachu.shorts.databinding.UiDeleteBinding;
import com.pikachu.shorts.databinding.UiDialogBinding;
import com.pikachu.shorts.databinding.UiDialogEditBinding;
import com.pikachu.shorts.databinding.UiLoadBinding;
import com.pikachu.shorts.databinding.UiMeBinding;
import com.pikachu.shorts.tool.IndexCmp;
import com.pikachu.shorts.tool.IndexInterface;
import com.pikachu.shorts.utils.AESCBCUtils;
import com.pikachu.shorts.utils.EquipmentUtils;
import com.pikachu.shorts.utils.OtherUtils;
import com.pikachu.shorts.utils.ToastUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

@SuppressLint("SetTextI18n")
public class MainActivity extends BBaseActivity<ActivityMainBinding> implements IndexInterface, IndexRecyclerViewAdapter.OnItemClickListener, OnRefreshListener, OnLoadMoreListener {

    @SuppressLint("StaticFieldLeak")
    private BaseBottomSheetDialog<UiDialogBinding> dialog;
    @SuppressLint("StaticFieldLeak")
    private BaseBottomSheetDialog<UiLoadBinding> dialog2;
    private IndexCmp instance;
    private EditText editText;
    private BaseBottomSheetDialog<UiDialogEditBinding> dialog11;
    private String editAppUrl;
    private int page = 0, num = 6;
    private boolean isRefresh;
    private IndexRecyclerViewAdapter adapter;
    private String userMd5;
    private String keyEdit;
    private BaseBottomSheetDialog<UiMeBinding> meDialog;
    private BaseBottomSheetDialog<UiDeleteBinding> deleteDialog;
    private BaseBottomSheetDialog<UiDialogBinding> dialog4;
    public static String shortHost = "http://pkpk.run/s/?";

    @Override
    protected void initBActivity(Bundle savedInstanceState) {
        binding.pkTor.setTitle(R.string.app_name);
        setSupportActionBar(binding.pkTor);
        instance = IndexCmp.getInstance(this, this);
        initDialog();
        initInfo();

    }

    private void initDialog() {
        dialog = new BaseBottomSheetDialog<UiDialogBinding>(context) {
            @Override
            protected void onViewCreate(UiDialogBinding binding) {
                setCancel(false);
                setCanSlide(false);
                binding.dE2.setVisibility(View.VISIBLE);
                binding.dE3.setVisibility(View.VISIBLE);
                binding.dE4.setVisibility(View.VISIBLE);
                binding.dT1.setText("请配置数据");
                binding.dE1.setHint("域名，可填写完整API地址");
                binding.dCn.setText("退出软件");
                binding.dCn.setOnClickListener(view -> finish());
                binding.dOk.setOnClickListener(view -> {
                    String host = binding.dE1.getText().toString();
                    String name = binding.dE2.getText().toString();
                    String pas = binding.dE3.getText().toString();
                    String key = binding.dE4.getText().toString();

                    if (!OtherUtils.isUrl(host)) {
                        host = "http://" + host + "/api/";
                    }
                   /* if (!OtherUtils.isUrl(host)) {
                        showToast("非法域名，是否已加http:// 或 https;//");
                        return;
                    }*/

                    if (name.equals("")) {
                        showToast("非法用户名");
                        return;
                    }
                    if (pas.equals("")) {
                        showToast("非法密码");
                        return;
                    }
                    if (key.equals("")) {
                        showToast("非法密钥");
                        return;
                    }
                    String md5 = AESCBCUtils.Md5(name + pas, true);
                    ShDataCls shDataCls = new ShDataCls();
                    shDataCls.setAppUrl(host);
                    shDataCls.setUserMd5(md5);
                    shDataCls.setKey(/*App.initialKey*/key);
                    App.shDataCls = App.writeInfo(shDataCls);
                    AESCBCUtils.init(/*App.shDataCls.getKey()*/key);
                    login(host, md5);
                    dismiss();
                });
            }
        };
        dialog2 = ToastUtils.showLoadDialog(this, "登陆中....");
    }

    private void initInfo() {
        adapter = new IndexRecyclerViewAdapter(this, shortHost, this);
        binding.pkRe.setAdapter(adapter);
        binding.pkRe.setLayoutManager(new LinearLayoutManager(this));
        binding.pkSw.setOnRefreshListener(this);
        binding.pkSw.setOnLoadMoreListener(this);
        App.shDataCls = App.readInfo();
        if (App.shDataCls == null || App.shDataCls.getAppUrl() == null || App.shDataCls.getAppUrl().equals("")) {
            dialog.show();
            return;
        }
        if (!App.isLogin) {
            login(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5());
        }
    }

    private void login(String url, String nameMd5) {
        dialog2.show();
        instance.startLogin(url, nameMd5);
    }

    //toolbar菜单
    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单点击事件
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(InfoActivity.class, App.shDataCls);
                break;
            case R.id.item2:
                if (dialog4 == null)
                    dialog4 = new BaseBottomSheetDialog<UiDialogBinding>(context) {

                        @Override
                        protected void onViewCreate(UiDialogBinding binding) {
                            binding.dE2.setVisibility(View.VISIBLE);
                            binding.dE3.setVisibility(View.VISIBLE);
                            binding.dE4.setVisibility(View.VISIBLE);
                            binding.dT1.setText("重新配置API地址");
                            binding.dE1.setText(App.shDataCls.getAppUrl());
                            binding.dE1.setHint("API地址");
                            binding.dE1.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                            binding.dE2.setHint("管理员账号，留空则不更换");
                            binding.dE3.setHint("管理员密码，留空则不更换");
                            binding.dE4.setHint("密钥，留空则不更换");
                            binding.dCn.setOnClickListener(view -> dismiss());
                            binding.dOk.setOnClickListener(view -> {
                                String text = binding.dE1.getText().toString();
                                String user = binding.dE2.getText().toString();
                                String psw = binding.dE3.getText().toString();
                                String key = binding.dE4.getText().toString();

                                if (text.equals("")) {
                                    showToast("不能为空");
                                    return;
                                }
                                if (!OtherUtils.isUrl(text)) {
                                    showToast("非法网址");
                                    return;
                                }

                                editAppUrl = text;
                                reDig2("重置中...");

                                userMd5 = App.shDataCls.getUserMd5();
                                if (!user.equals("") && !psw.equals("")) {
                                    userMd5 = AESCBCUtils.Md5(user + psw, true);
                                }
                                keyEdit = App.shDataCls.getKey();
                                if (!key.equals("")) {
                                    AESCBCUtils.init(key);
                                    keyEdit = key;
                                }
                                instance.startHost(text, userMd5);
                                dismiss();
                            });

                        }
                    };
                dialog4.show();
                break;
            case R.id.item3:
                if (meDialog == null)
                    meDialog = new BaseBottomSheetDialog<UiMeBinding>(this) {
                        @Override
                        protected void onViewCreate(UiMeBinding binding) {
                            binding.dT2.setText(String.format(getString(R.string.me_editApp), "\n", EquipmentUtils.getAppVersionName(context), "\n"));
                            binding.meT1.setOnClickListener(view ->
                                    OtherUtils.jumpBrowser(context, "https://github.com/pikachu0621/"));
                            binding.meT2.setOnClickListener(view ->
                                    OtherUtils.jumpBrowser(context, "http://pkpk.run"));
                            binding.meT3.setOnClickListener(view -> {
                                try {
                                    OtherUtils.jumpQQ(context, "2825436553");
                                } catch (Exception e) {
                                    OtherUtils.copy(context, "2825436553");
                                    showToast("跳转失败，可能未安装QQ，已复制QQ号");
                                }
                            });
                            binding.meT4.setOnClickListener(view -> {
                                OtherUtils.copy(context, "Pikachu_WeChat");
                                showToast("已复制微信");
                            });
                        }
                    };
                meDialog.show();
                break;
            case R.id.item4:
                dialog2.dismiss();
                dialog2.binding.dB3.setVisibility(View.GONE);
                dialog2.binding.dP1.setVisibility(View.GONE);
                dialog2.binding.dRoot.setVisibility(View.VISIBLE);
                dialog2.binding.dT1.setText("安全退出，下次需要手动登录，是否继续？");

                dialog2.binding.dB1.setText("继续");
                dialog2.binding.dB1.setOnClickListener(view -> {
                    dialog2.binding.dP1.setVisibility(View.VISIBLE);
                    dialog2.binding.dRoot.setVisibility(View.GONE);
                    //App.shDataCls.setAppUrl("");
                    App.shDataCls.setUserMd5("");
                    App.writeInfo(App.shDataCls);
                    finish();
                    //System.exit(0);
                });

                dialog2.binding.dB2.setText("取消");
                dialog2.binding.dB2.setOnClickListener(view -> {
                    dialog2.binding.dP1.setVisibility(View.VISIBLE);
                    dialog2.binding.dRoot.setVisibility(View.GONE);
                    dialog2.dismiss();
                });
                dialog2.setCancel(true);
                dialog2.setCanSlide(true);
                dialog2.show();
                break;
        }
        return false;
    }

    @Override
    public void loginError(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
        dialog.binding.dE1.setText(App.shDataCls.getAppUrl());
        dialog.binding.dE4.setText(App.shDataCls.getKey());
        dialog.show();
    }

    @Override
    public void loginOK(DataCls dataCls) {
        showToast("登录成功");
        shortHost = dataCls.getShortX();
        dialog2.dismiss();
        binding.pkSw.autoRefresh();
    }

    @Override
    public void shortError(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
    }

    @Override
    public void shortOk(DataCls dataCls) {
        showToast("生成成功");
        dialog2.dismiss();
        shortHost = dataCls.getShortX();
        dialog2.binding.dT1.setText(dataCls.getShortX());
        if (editText != null) {
            editText.setText(dataCls.getShortX());
        }
        dialog2.binding.dRoot.setVisibility(View.GONE);
        dialog2.binding.dP1.setVisibility(View.GONE);
        dialog2.binding.dB3.setVisibility(View.VISIBLE);
        dialog2.binding.dB3.setOnClickListener(view -> {
            OtherUtils.copy(context, dataCls.getShortX());
            dialog2.binding.dP1.setVisibility(View.VISIBLE);
            dialog2.binding.dB3.setVisibility(View.GONE);
            dialog2.dismiss();
        });
        dialog2.setCancel(true);
        dialog2.setCanSlide(true);
        dialog2.show();
        binding.pkSw.autoRefresh();
    }

    @Override
    public void offAndOnError(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
    }

    @Override
    public void offAndOnOk(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
        shortHost = dataCls.getShortX();
        binding.pkSw.autoRefresh();
    }

    @Override
    public void lookNumError(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
    }

    @Override
    public void lookNumOk(DataCls dataCls) {
        showToast("查询成功：" + dataCls.getAmount());
        shortHost = dataCls.getShortX();
        dialog2.dismiss();
    }

    @Override
    public void oneShortError(DataCls dataCls) {
        showToast(dataCls.getMsg());
        dialog2.dismiss();
    }

    @Override
    public void oneShortOk(DataCls dataCls) {
        //showToast("查询成功：" + dataCls.getAmount());
        List<DataCls.DataBean> data = dataCls.getData();
        if (data.size() <= 0) {
            showToast("查询失败：请重试");
            dialog2.dismiss();
            return;
        }
        dialog2.dismiss();
        DataCls.DataBean dataBean = data.get(0);
        shortHost = dataCls.getShortX();
        dialog11 = new BaseBottomSheetDialog<UiDialogEditBinding>(context) {

            @SuppressLint("SetTextI18n")
            @Override
            protected void onViewCreate(UiDialogEditBinding binding) {
                binding.dE1.setText(dataBean.getLong_url());
                binding.dE2.setText(dataBean.getShort_url());
                binding.dE3.setText(dataBean.getUrl_visits() + "");
                binding.dE3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String s = editable.toString();
                        if (s.equals(""))
                            binding.dE3.setText("0");
                    }
                });
                binding.dT2.setText("生成时间：" + dataBean.getCreate_time());
                binding.dT3.setText("数据库ID：" + dataBean.getId());
                binding.dSw1.setChecked(dataBean.getIs_open() == 1);
                binding.dSw2.setChecked(dataBean.getIs_win() == 1);
                binding.dLin1.setOnClickListener(view ->
                        binding.dSw1.setChecked(!binding.dSw1.isChecked()));
                binding.dLin2.setOnClickListener(view ->
                        binding.dSw2.setChecked(!binding.dSw2.isChecked()));
                binding.dCn.setOnClickListener(view -> dismiss());
                binding.dOk.setOnClickListener(view -> {
                    String longUrl = binding.dE1.getText().toString();
                    String shortUrl = binding.dE2.getText().toString();
                    String amount = binding.dE3.getText().toString();
                    binding.dSw1.isChecked();
                    binding.dSw2.isChecked();
                    if (longUrl.equals("")) {
                        showToast("原链接不能为空");
                        return;
                    }
                    if (shortUrl.equals("")) {
                        showToast("短链ID不能为空");
                        return;
                    }
                    if (!OtherUtils.isUrl(longUrl)) {
                        showToast("非法网址");
                        return;
                    }
                    if (shortUrl.length() > 50) {
                        showToast("短链ID太长，最长50");
                        return;
                    }
                    if (OtherUtils.isSpecialChar(shortUrl)) {
                        showToast("短链ID不能包含特殊字符");
                        return;
                    }
                    if (amount.length() > 10) {
                        showToast("访问量太大了吧！");
                        return;
                    }

                    long amount1 = Long.parseLong(amount);
                    instance.startEditShort(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(),
                            dataBean.getId(), longUrl, shortUrl,
                            binding.dSw1.isChecked(), binding.dSw2.isChecked(), amount1);
                    dismiss();
                    reDig2("保存中...");
                });

            }
        };
        dialog11.show();


    }

    @Override
    public void editError(DataCls dataCls) {
        showToast("修改失败：" + dataCls.getMsg());
        dialog2.dismiss();
        if (dialog11 != null) {
            dialog11.show();
        }
    }

    @Override
    public void editOk(DataCls dataCls) {
        showToast(dataCls.getMsg());
        shortHost = dataCls.getShortX();
        dialog2.dismiss();
        if (editText != null)
            editText.setText(dataCls.getShortX());
        binding.pkSw.autoRefresh();
    }

    @Override
    public void reError(DataCls dataCls) {
        showToast("修改失败：" + dataCls.getCode());
        AESCBCUtils.init(App.shDataCls.getKey());
        dialog2.dismiss();
    }

    @Override
    public void reOk(DataCls dataCls) {
        showToast("修改成功");
        dialog2.dismiss();
        shortHost = dataCls.getShortX();
        App.shDataCls.setAppUrl(editAppUrl);
        App.shDataCls.setUserMd5(userMd5);
        App.shDataCls.setKey(keyEdit);
        App.writeInfo(App.shDataCls);
        binding.pkSw.autoRefresh();
    }

    @Override
    public void deleteError(DataCls dataCls) {
        showToast("删除失败：" + dataCls.getCode());
        dialog2.dismiss();
    }

    @Override
    public void deleteOk(DataCls dataCls) {
        showToast("删除成功");
        dialog2.dismiss();
        shortHost = dataCls.getShortX();
        binding.pkSw.autoRefresh();
    }

    @Override
    public void onClickShort(EditText editText, int position) {
        String urlLong = editText.getText().toString();
        if (urlLong.equals("")) {
            showToast("不能为空");
            return;
        }
        if (!OtherUtils.isUrl(urlLong)) {
            showToast("非法网址");
            return;
        }
        this.editText = editText;
        reDig2("生成中...");
        instance.startShort(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), urlLong);
    }

    @Override
    public void onClickOffAndOn(EditText editText, int position) {
        String urlLong = editText.getText().toString();
        if (urlLong.equals("")) {
            showToast("不能为空");
            return;
        }
        if (!OtherUtils.isUrl(urlLong)) {
            showToast("非法网址");
            return;
        }
        this.editText = editText;
        reDig2("处理中...");
        instance.startOffAndOn(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), urlLong);

    }

    @Override
    public void onClickLookNum(EditText editText, int position) {
        String urlLong = editText.getText().toString();
        if (urlLong.equals("")) {
            showToast("不能为空");
            return;
        }
        if (!OtherUtils.isUrl(urlLong)) {
            showToast("非法网址");
            return;
        }
        this.editText = editText;
        reDig2("查询中...");
        instance.startLookNum(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), urlLong);

    }

    @Override
    public void onClickEdit(EditText editText, int position) {
        String urlLong = editText.getText().toString();
        if (urlLong.equals("")) {
            showToast("不能为空");
            return;
        }
        if (!OtherUtils.isUrl(urlLong)) {
            showToast("非法网址");
            return;
        }
        this.editText = editText;
        reDig2("解析中...");
        instance.startLookOneShort(App.shDataCls.getAppUrl(), App.shDataCls.getUserMd5(), urlLong);

    }

    @Override
    public void onClickItem(DataCls.DataBean dataBean, int position) {
        reDig2("解析中...");
        instance.startLookOneShort(App.shDataCls.getAppUrl(),
                App.shDataCls.getUserMd5(), dataBean.getShort_url());
    }

    @Override
    public boolean onLongClickItem(DataCls.DataBean dataBean, int position) {
        deleteDialog = new BaseBottomSheetDialog<UiDeleteBinding>(this) {
            @Override
            protected void onViewCreate(UiDeleteBinding binding) {
                binding.dCn.setOnClickListener(view -> dismiss());
                binding.dOk.setOnClickListener(view -> {
                    dismiss();
                    reDig2("删除中...");
                    instance.startDeleteShort(App.shDataCls.getAppUrl(),
                            App.shDataCls.getUserMd5(),
                            dataBean.getShort_url());
                });

            }
        };
        deleteDialog.show();
        return true;
    }

    @Override
    public void onClickItemTitle(DataCls.DataBean dataBean, int position) {
        OtherUtils.copy(this, shortHost + dataBean.getShort_url());
        showToast("短链复制成功");
    }


    @Override
    public void loadShortError(DataCls dataCls) {
        dialog2.dismiss();
        showToast("获取失败：" + dataCls.getCode());
        if (isRefresh) {
            page = 0;
            binding.pkSw.finishRefresh(false);
        } else {
            //page = 0;
            binding.pkSw.finishLoadMore(false);
        }
    }

    @Override
    public void loadShortOk(DataCls dataCls) {
        if (isRefresh) {
            binding.pkSw.finishRefresh(true);
            page = 0;
            adapter.refresh(dataCls.getData(),  shortHost);
            binding.pkSw.resetNoMoreData();
        } else {
            binding.pkSw.finishLoadMore(true);
            if (dataCls.getData().size() > 0) {
                adapter.refresh(dataCls.getData());
                page ++;
            } else {
                binding.pkSw.finishLoadMoreWithNoMoreData();
            }
        }
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        instance.startReadShortData(App.shDataCls.getAppUrl(),
                App.shDataCls.getUserMd5(), 0, num);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isRefresh = false;
        instance.startReadShortData(App.shDataCls.getAppUrl(),
                App.shDataCls.getUserMd5(), page + 1, num);
    }


    private void reDig2(String str) {
        dialog2.binding.dP1.setVisibility(View.VISIBLE);
        dialog2.binding.dRoot.setVisibility(View.GONE);
        dialog2.binding.dB3.setVisibility(View.GONE);
        dialog2.binding.dT1.setText(str);
        dialog2.setCancel(false);
        dialog2.setCanSlide(false);
        dialog2.show();
    }
}




















