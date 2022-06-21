package com.pikachu.shorts.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.pikachu.shorts.base.BaseBottomSheetDialog;
import com.pikachu.shorts.databinding.UiLoadBinding;

public class ToastUtils {


    private static Toast toast;

    public static void showToast(Context context, String msg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static BaseBottomSheetDialog<UiLoadBinding> showLoadDialog(Context context, String msg) {

        return new BaseBottomSheetDialog<UiLoadBinding>(context) {
                    @Override
                    protected void onViewCreate(UiLoadBinding binding) {
                        setCancel(false);
                        setCanSlide(false);
                        binding.dT1.setText(msg);
                    }
                };
    }


}
