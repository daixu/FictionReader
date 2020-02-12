package com.shangame.fiction.ui.bookstore;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

public class GirlPageActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_layout);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("女生");

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new GirlPageFragment());
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivPublicBack){
            finish();
        }
    }

}
