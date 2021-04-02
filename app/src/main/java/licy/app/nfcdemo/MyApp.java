package licy.app.nfcdemo;

import android.app.Application;

import com.pgyer.pgyersdk.PgyerSDKManager;
import com.pgyer.pgyersdk.pgyerenum.FeatureEnum;

/**
 * null.java
 * description: TODO
 *
 * @author : Licy
 * @date : 2021/4/2
 * email ：licy3051@qq.com
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init(this);
    }

    /**
     * 初始化
     *
     * @param application
     */
    private static void init(MyApp application) {
        new PgyerSDKManager.InitSdk()
                //设置上下问对象
                .setContext(application)
                //添加检查新版本
                .enable(FeatureEnum.CHECK_UPDATE)
                .build();
    }
}
