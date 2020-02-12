package com.shangame.fiction.ui.bookstore;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * 完本
 */
public class BookEndActivity extends AppCompatActivity implements View.OnClickListener {

    private int bookStoreChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_layout);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        bookStoreChannel = getIntent().getIntExtra("bookStoreChannel", BookStoreChannel.GIRL);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        if (bookStoreChannel == BookStoreChannel.GIRL) {
            tvPublicTitle.setText("女生");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, BookEndFragment.newInstance(BookStoreChannel.GIRL));
            fragmentTransaction.commitAllowingStateLoss();
        } else if (bookStoreChannel == BookStoreChannel.BOY) {
            tvPublicTitle.setText("男生");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, BookEndFragment.newInstance(BookStoreChannel.BOY));
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            findViewById(R.id.titleLayout).setVisibility(View.GONE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new ChoicenessBookEndFragment());
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }
}
