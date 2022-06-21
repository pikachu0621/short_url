package com.pikachu.shorts.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.pikachu.shorts.R;
import com.pikachu.shorts.base.BaseAdapter;
import com.pikachu.shorts.cls.DataCls;
import com.pikachu.shorts.databinding.UiConItemBinding;
import com.pikachu.shorts.databinding.UiTopItemBinding;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * author : pikachu
 * date   : 2021/7/30 17:37
 * version: 1.0
 */
public class IndexRecyclerViewAdapter extends BaseAdapter<BaseAdapter.ViewHolder> {


    public static final int TOP_TYPE = 0x1;
    private final Context context;
    private String shortHost;
    private List<DataCls.DataBean> dataCls;
    private final OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onClickShort(EditText editText, int position);

        void onClickOffAndOn(EditText editText, int position);

        void onClickLookNum(EditText editText, int position);

        void onClickEdit(EditText editText, int position);

        void onClickItem(DataCls.DataBean dataBean, int position);  // 点击item\

        boolean onLongClickItem(DataCls.DataBean dataBean, int position);  // 长按item

        void onClickItemTitle(DataCls.DataBean dataBean, int position);  // 点击title
    }


    public IndexRecyclerViewAdapter(OnItemClickListener onItemClickListener, String shortHost, Context context) {
        dataCls = new ArrayList<>();
        dataCls.add(0, null);
        this.shortHost = shortHost;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }

    public void refresh(List<DataCls.DataBean> dataCls, String shortHost) {
        if (dataCls == null) {
            return;
        }
        dataCls.add(0, null);
        this.dataCls.clear();
        this.dataCls = dataCls;
        this.shortHost = shortHost;
        notifyDataSetChanged();
    }


    public void refresh(List<DataCls.DataBean> dataCls) {
        if (dataCls == null) {
            return;
        }
        this.dataCls.addAll(dataCls);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TOP_TYPE)
            return new ViewHolderTop(UiTopItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        return new ViewHolderContent(UiConItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (getItemViewType(position) == TOP_TYPE) {
            ViewHolderTop holder1 = (ViewHolderTop) holder;
            holder1.binding.pkLin1.setOnClickListener(view -> onItemClickListener.onClickShort(holder1.binding.pkEdit, position));
            holder1.binding.pkLin2.setOnClickListener(view -> onItemClickListener.onClickOffAndOn(holder1.binding.pkEdit, position));
            holder1.binding.pkLin3.setOnClickListener(view -> onItemClickListener.onClickLookNum(holder1.binding.pkEdit, position));
            holder1.binding.pkLin4.setOnClickListener(view -> onItemClickListener.onClickEdit(holder1.binding.pkEdit, position));
            return;
        }
        DataCls.DataBean dataBean = dataCls.get(position);
        ViewHolderContent holder2 = (ViewHolderContent) holder;
        holder2.binding.cT1.setText(dataBean.getShort_url());

        holder2.binding.cT2.setText("ID:\n长链:\n可访问:\n防红:\n访问量:\n生成日期:");
        holder2.binding.cT3.setText(
                        "[ "+dataBean.getId() + " ]\n" +
                        "[ "+constraintStr(dataBean.getLong_url(), 30) + " ]\n" +
                        "[ "+(dataBean.getIs_open() == 1 ? "是" : "否") + " ]\n" +
                        "[ "+(dataBean.getIs_win() == 1 ? "启用" : "禁用") + " ]\n" +
                        "[ "+dataBean.getUrl_visits() + " ]\n" +
                        "[ "+dataBean.getCreate_time() + " ]");

        holder2.binding.cT1.setTextColor(dataBean.getIs_open() == 1?context.getResources().getColor(R.color.time2):0xFFFF0000);

        holder2.binding.getRoot().setOnClickListener(view -> onItemClickListener.onClickItem(dataBean,position));
        holder2.binding.getRoot().setOnLongClickListener(view -> onItemClickListener.onLongClickItem(dataBean,position));

        holder2.binding.cT1.setOnClickListener(view -> onItemClickListener.onClickItemTitle(dataBean,position));
    }


    @Override
    public int getItemCount() {
        return dataCls.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TOP_TYPE;
        return super.getItemViewType(position);
    }


    public static class ViewHolderTop extends ViewHolder {

        public UiTopItemBinding binding;

        public ViewHolderTop(ViewBinding cls) {
            super(cls);
            binding = (UiTopItemBinding) cls;
        }
    }


    public static class ViewHolderContent extends ViewHolder {
        public UiConItemBinding binding;

        public ViewHolderContent(ViewBinding cls) {
            super(cls);
            binding = (UiConItemBinding) cls;
        }
    }

    //约束 字符串
    public String constraintStr(String str, int max) {
        if (str.length() <= max)
            return str;
        String substring = str.substring(0, max);
        return substring + "...";
    }

}
