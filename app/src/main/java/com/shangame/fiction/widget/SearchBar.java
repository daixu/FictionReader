package com.shangame.fiction.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/8/2
 */
public class SearchBar extends LinearLayout implements View.OnClickListener {

    private EditText etSearch;

    private OnQueryTextListener onQueryTextListener;

    private OnCancelLinstener onCancelLinstener;

    public SearchBar(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.public_search_bar, this);
        etSearch = findViewById(R.id.etSearch);
        findViewById(R.id.ivSearchDelete).setOnClickListener(this);
        findViewById(R.id.tvSearchCancel).setOnClickListener(this);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (onQueryTextListener != null) {
                        String queryText = textView.getText().toString();
                        onQueryTextListener.onQueryTextSubmit(queryText);
                    }
                    return true;
                }
                return false;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (onQueryTextListener != null) {
                    String queryText = editable.toString();
                    onQueryTextListener.onQueryTextChange(queryText);
                }
            }
        });
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivSearchDelete) {
            etSearch.setText(null);
        } else if (id == R.id.tvSearchCancel) {
            if (onCancelLinstener != null) {
                onCancelLinstener.onCancel();
            }
        }
    }

    public void setOnQueryTextListener(OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
    }

    public void setOnCancelLinstener(OnCancelLinstener onCancelLinstener) {
        this.onCancelLinstener = onCancelLinstener;
    }

    public String getSearchWords() {
        return etSearch.getText().toString();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextChange(String queryText);

        boolean onQueryTextSubmit(String queryText);
    }

    public interface OnCancelLinstener {
        void onCancel();
    }
}
