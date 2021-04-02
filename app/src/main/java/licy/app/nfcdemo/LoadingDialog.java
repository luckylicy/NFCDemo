package licy.app.nfcdemo;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * LoadingDialog
 * description: TODO
 *
 * @author : Licy
 * @date : 2020/9/14
 * email ：licy3051@qq.com
 */
public class LoadingDialog extends Dialog {

    private boolean isTimerFinish = false;
    private boolean isCallDismiss = false;

    private CountDownTimer mCountDownTimer = new CountDownTimer(100, 100) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            synchronized (LoadingDialog.this) {
                isTimerFinish = true;
                if (isCallDismiss) {
                    dismiss();
                }
            }
        }
    };

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        super.show();
        mCountDownTimer.start();
    }

    @Override
    public void dismiss() {
        synchronized (LoadingDialog.this) {
            isCallDismiss = true;
            if (isTimerFinish) {
                super.dismiss();
            }
        }
    }

    /**
     * 创建静态内部类Builder，将dialog的部分属性封装进该类
     */
    public static class Builder {

        private Context context;
        //提示信息
        private String message;
        //是否展示提示信息
        private boolean isShowMessage = true;
        //是否按返回键取消
        private boolean isCancelable = true;
        //是否取消
        private boolean isCancelOutside = false;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示信息
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置是否显示提示信息
         *
         * @param isShowMessage
         * @return
         */
        public Builder setShowMessage(boolean isShowMessage) {
            this.isShowMessage = isShowMessage;
            return this;
        }

        /**
         * 设置是否可以按返回键取消
         *
         * @param isCancelable
         * @return
         */
        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         *
         * @param isCancelOutside
         * @return
         */
        public Builder setCancelOutside(boolean isCancelOutside) {
            this.isCancelOutside = isCancelOutside;
            return this;
        }

        //创建Dialog
        public LoadingDialog create() {

            LayoutInflater inflater = LayoutInflater.from(context);


            View view = inflater.inflate(R.layout.dialog_loading, null);
            // 设置带自定义主题的dialog
            LoadingDialog loadingDailog = new LoadingDialog(context, R.style.XyjLoadingDialogStyle);
            loadingDailog.setContentView(view);
            loadingDailog.setCancelable(isCancelable);
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside);
            return loadingDailog;
        }
    }

}

    
    
       
    