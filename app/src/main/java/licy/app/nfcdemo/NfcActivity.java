package licy.app.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import licy.app.nfcdemo.bean.DictBean;
import licy.app.nfcdemo.databinding.ActivityMainBinding;

/**
 * @author kuan
 * Created on 2019/2/25.
 * @description
 */
public class NfcActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private Tag mTag;
    private ActivityMainBinding mActivityMainBinding;
    private List<DictBean.DataBean> mDicts;
    private DictRvAdapter mDictRvAdapter;
    protected LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        mNfcAdapter = M1CardUtils.isNfcAble(this);
        M1CardUtils.setPendingIntent(PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0));
        mTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        TextView textView = findViewById(R.id.tv_content);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        initRv();

//        mActivityMainBinding.rgIndex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                mActivityMainBinding.etText.setText("");
//            }
//        });

        //读卡
        mActivityMainBinding.btnRead.setOnClickListener(v -> {
            if (M1CardUtils.hasCardType(mTag, this, "MifareClassic")) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    M1CardUtils.readCard(mTag);
                    String tagFirst = M1CardUtils.readCard(mTag, 1);
                    String tagSecond = M1CardUtils.readCard(mTag, 2);
                    String tags = tagFirst.trim() + tagSecond.trim();
                    textView.setText(tags);
                    Toast.makeText(this, "读取成功" + tags, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "读取失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 写卡
        mActivityMainBinding.btnWrite.setOnClickListener(v -> {
            writeToNfc(mActivityMainBinding.etText.getText().toString().trim());
        });

    }

    private void writeToNfc(String content) {
        try {

            if (!TextUtils.isEmpty(content)) {

                List<String> array = new ArrayList<>();

                int length = content.length();
                int arraySize = length / 16;
                if (arraySize > 15) {
                    Log.e("M1CardUtils", "待写入数据超过可写入长度");
                    Toast.makeText(this, "待写入数据超过可写入长度", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < arraySize; i++) {
                    String blockContent = content.substring(i * 16, (i + 1) * 16);
                    byte[] bytes = blockContent.getBytes(StandardCharsets.UTF_8);
                    boolean result = M1CardUtils.writeBlock(mTag, i + 1, bytes);
                    if (!result) {
                        Log.e("M1CardUtils", "写入失败");
                        Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int lastContentLength = length % 16;
                if (lastContentLength > 0) {
                    String blockContent = StringUtil.fillRight(content.substring(arraySize * 16), " ", 16);
                    byte[] bytes = blockContent.getBytes(StandardCharsets.UTF_8);
                    boolean result = M1CardUtils.writeBlock(mTag, arraySize + 1, bytes);
                    if (result) {
                        mActivityMainBinding.tvContent.setText(content);
                        Log.e("M1CardUtils", "写入成功");
                        Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("M1CardUtils", "写入失败");
                        Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                    }
                }

            }


//            if (content.length() >= 16) {
//                String first = content.substring(0, 16);
//                byte[] bytesFirst = first.getBytes(StandardCharsets.UTF_8);
//                boolean boolFirst = M1CardUtils.writeBlock(mTag, 1, bytesFirst);
//
//                String second = content.substring(16);
//                second = StringUtil.fillRight(second, " ", 16);
//                byte[] bytesSecond = second.getBytes(StandardCharsets.UTF_8);
//                boolean boolSecond = M1CardUtils.writeBlock(mTag, 2, bytesSecond);
//                if (boolFirst && boolSecond) {
//                    mActivityMainBinding.tvContent.setText(content);
//                    Log.e("M1CardUtils", "写入成功");
//                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e("M1CardUtils", "写入失败");
//                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                content = StringUtil.fillRight(content, " ", 16);
//                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
//                // 清除第二块区的数据
//                String empty = StringUtil.fillRight("", " ", 16);
//                byte[] bytesEmpty = empty.getBytes(StandardCharsets.UTF_8);
//                M1CardUtils.writeBlock(mTag, 2, bytesEmpty);
//
//                if (M1CardUtils.writeBlock(mTag, 1, bytes)) {
//                    mActivityMainBinding.tvContent.setText(content);
//                    Log.e("M1CardUtils", "写入成功");
//                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e("M1CardUtils", "写入失败");
//                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "请保持NFC标签与手机紧贴~", Toast.LENGTH_SHORT).show();
        } finally {
            hideLoadingDialog();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mNfcAdapter = M1CardUtils.isNfcAble(this);
        M1CardUtils.setPendingIntent(PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0));
        Log.e("readCard", "onNewIntent");
        mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, M1CardUtils.getPendingIntent(),
                    null, null);
        }
    }

    private void initRv() {
        getStrokeData();
        mDictRvAdapter = new DictRvAdapter(R.layout.adapter_rv_dict, mDicts);
        mActivityMainBinding.rvDict.setLayoutManager(new LinearLayoutManager(this));
        mActivityMainBinding.rvDict.setAdapter(mDictRvAdapter);

        mActivityMainBinding.btnStroke.setOnClickListener(v -> {
            getStrokeData();
            mDictRvAdapter.notifyDataSetChanged();
        });
        mActivityMainBinding.btnChestPain.setOnClickListener(v -> {
            getChestPainData();
            mDictRvAdapter.notifyDataSetChanged();
        });
        mActivityMainBinding.btnTrauma.setOnClickListener(v -> {
            getTraumaData();
            mDictRvAdapter.notifyDataSetChanged();
        });

        mDictRvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                showLoadingDialog();
                writeToNfc(mDicts.get(position).getValue());
            }
        });
    }

    private void getStrokeData() {
        if (mDicts == null) {
            mDicts = new ArrayList<>();
        } else {
            mDicts.clear();
        }
        mDicts.add(new DictBean.DataBean("出车时间", "depart120time"));
        mDicts.add(new DictBean.DataBean("抵达现场时间", "arrivescenetime"));
        mDicts.add(new DictBean.DataBean("离开现场时间", "levavescenetime"));
        mDicts.add(new DictBean.DataBean("到达医院时间", "arrivehospitaltime"));
        mDicts.add(new DictBean.DataBean("首次医疗接触时间", "fmctime"));
        mDicts.add(new DictBean.DataBean("采血时间", "bloodcollectiontime"));
        mDicts.add(new DictBean.DataBean("开始静脉溶栓时间", "thrombolyticstaticpushtime"));
    }

    private void getTraumaData() {
        if (mDicts == null) {
            mDicts = new ArrayList<>();
        } else {
            mDicts.clear();
        }
        mDicts.add(new DictBean.DataBean("出车时间", "depart120time"));
        mDicts.add(new DictBean.DataBean("抵达现场时间", "arrivescenetime"));
        mDicts.add(new DictBean.DataBean("离开现场时间", "levavescenetime"));
        mDicts.add(new DictBean.DataBean("到达医院时间", "arrivehospitaltime"));
        mDicts.add(new DictBean.DataBean("首次医疗接触时间", "fmctime"));
        mDicts.add(new DictBean.DataBean("发病现场：静脉通路时间", "preemergencyvenouschanneltime"));
        mDicts.add(new DictBean.DataBean("发病现场：气管插管时间", "preemergencytracheacannulatime"));
        mDicts.add(new DictBean.DataBean("发病现场：心肺复苏时间", "preemergencycprtime"));
        mDicts.add(new DictBean.DataBean("急诊现场：静脉通路时间", "inemergencyvenouschanneltime"));
        mDicts.add(new DictBean.DataBean("急诊现场：气管插管时间", "inemergencytracheacannulatime"));
        mDicts.add(new DictBean.DataBean("急诊现场：心肺复苏时间", "inemergencycprtime"));
    }

    private void getChestPainData() {
        if (mDicts == null) {
            mDicts = new ArrayList<>();
        } else {
            mDicts.clear();
        }
        mDicts.add(new DictBean.DataBean("出车时间", "depart120time"));
        mDicts.add(new DictBean.DataBean("抵达现场时间", "arrivescenetime"));
        mDicts.add(new DictBean.DataBean("离开现场时间", "levavescenetime"));
        mDicts.add(new DictBean.DataBean("到达医院时间", "arrivehospitaltime"));
        mDicts.add(new DictBean.DataBean("首次医疗接触时间", "fmctime"));
        mDicts.add(new DictBean.DataBean("开始静脉溶栓时间", "afterthrombolysisbegintime"));
        mDicts.add(new DictBean.DataBean("初始药物：阿司匹林", "acsaspirintime"));
        mDicts.add(new DictBean.DataBean("初始药物：氯呲格雷", "acschlorpyridintime"));
        mDicts.add(new DictBean.DataBean("初始药物：替格瑞洛", "acstigrilotime"));
        mDicts.add(new DictBean.DataBean("初始药物：术前抗凝", "acsanticoagulantmedicinetime"));
    }


    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog.Builder(NfcActivity.this)
                    .setShowMessage(false)
                    .setCancelable(false)
                    .setCancelOutside(false)
                    .setMessage("加载中...")
                    .create();
        }
        if (mLoadingDialog.isShowing()) {
            return;
        }
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}