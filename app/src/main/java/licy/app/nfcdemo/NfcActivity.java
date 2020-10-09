package licy.app.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;

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


        mActivityMainBinding.rgIndex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mActivityMainBinding.etText.setText("");
            }
        });

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
            try {
                int blockIndex = 1;
                if (mActivityMainBinding.rbIndex1.isChecked()) {
                    blockIndex = 1;
                } else if (mActivityMainBinding.rbIndex2.isChecked()) {
                    blockIndex = 2;
                }
                String trim = mActivityMainBinding.etText.getText().toString().trim();
                trim = StringUtil.fillRight(trim, " ", 16);
                byte[] bytes = trim.getBytes(StandardCharsets.UTF_8);
                if (M1CardUtils.writeBlock(mTag, blockIndex, bytes)) {
                    Log.e("M1CardUtils", "写入成功");
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("M1CardUtils", "写入失败");
                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "请保持NFC标签与手机紧贴~", Toast.LENGTH_SHORT).show();
            }
        });

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

}