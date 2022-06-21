package com.pikachu.shorts.base;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


public abstract class BaseAdapter<T extends BaseAdapter.ViewHolder>  extends RecyclerView.Adapter<T> {




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder( ViewBinding cls) {
            super(cls.getRoot());
        }
    }




    public void setSpanCount(GridLayoutManager gridLayoutManager, int... itemType) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                for (int it : itemType){
                    if (getItemViewType(position) == it)
                        return gridLayoutManager.getSpanCount();
                }
                return 1;
            }
        });
    }


    /**
     * 设置 独占一行
     * @param gridLayoutManager 网格管理器
     * @param positionStart 开始位置
     * @param positionEnd 结束位置
     */
    public void setSpanCount(GridLayoutManager gridLayoutManager, int positionStart, int positionEnd) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position>= positionStart && position <= positionEnd)
                        return gridLayoutManager.getSpanCount();
                return 1;
            }
        });
    }




}
