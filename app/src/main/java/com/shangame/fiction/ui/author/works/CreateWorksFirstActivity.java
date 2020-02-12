package com.shangame.fiction.ui.author.works;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;

public class CreateWorksFirstActivity extends BaseActivity implements View.OnClickListener {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.CREATE_WORKS_COMPLETE.equals(action)) {
                finish();
            }
        }
    };

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.CREATE_WORKS_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_works_first);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("创建作品");

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.layout_boy).setOnClickListener(this);
        findViewById(R.id.layout_girl).setOnClickListener(this);

        initReceiver();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.layout_boy: {
                Intent intent = new Intent(CreateWorksFirstActivity.this, CreateWorksCompleteActivity.class);
                intent.putExtra("sex", 0);
                startActivity(intent);
            }
            break;
            case R.id.layout_girl: {
                Intent intent = new Intent(CreateWorksFirstActivity.this, CreateWorksCompleteActivity.class);
                intent.putExtra("sex", 1);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}
