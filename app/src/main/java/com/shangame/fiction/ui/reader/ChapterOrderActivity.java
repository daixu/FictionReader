package com.shangame.fiction.ui.reader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.net.response.ChapterOrderConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.contents.BookContentsActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;

public class ChapterOrderActivity extends BaseActivity implements View.OnClickListener,ChapterOrderContracts.View{

    private TextView tvChapterName;
    private TextView tvAccountBalanceCoin;
    private TextView tvNeedCostCoin;
    private TextView tvOriginalCostCoin;
    private Button btnImmediatelyBuy;

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private long bookid;
    private long chapterid;
    private long userid;

    private double readmoney;//用户剩余闪闪币

    private ChapterOrderPresenter chapterOrderPresenter;

    public static final void lunchActivity(Activity activity, long bookid , long chapterid){
        Intent intent = new Intent(activity,ChapterOrderActivity.class);
        intent.putExtra("bookid",bookid);
        intent.putExtra("chapterid",chapterid);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_order);
        initParam();
        initView();
        chapterOrderPresenter = new ChapterOrderPresenter();
        chapterOrderPresenter.attachView(this);

        long userid = UserInfoManager.getInstance(mContext).getUserid();
        if(userid == 0){
            lunchLoginActivity();
        }else{
            chapterOrderPresenter.getChapterOrderConfig(userid,bookid,chapterid);
        }
    }

    private void initParam() {
        userid = UserInfoManager.getInstance(mContext).getUserid();
        bookid = getIntent().getLongExtra("bookid",0);
        chapterid = getIntent().getLongExtra("chapterid",0);
    }


    private void initView() {

        tvChapterName = (TextView) findViewById(R.id.tvChapterName);
        tvAccountBalanceCoin = (TextView) findViewById(R.id.tvAccountBalanceCoin);
        tvNeedCostCoin = (TextView) findViewById(R.id.tvNeedCostCoin);
        tvOriginalCostCoin = (TextView) findViewById(R.id.tvOriginalCostCoin);
        btnImmediatelyBuy = (Button) findViewById(R.id.btnImmediatelyBuy);

        tvOriginalCostCoin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        btnImmediatelyBuy.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,2));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_empty));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }



    @Override
    public void getChapterOrderConfigSuccess(ChapterOrderConfigResponse chapterOrderConfigResponse) {
        tvChapterName.setText(chapterOrderConfigResponse.title);
        tvAccountBalanceCoin.setText(getString(R.string.account_balance_coin,String.valueOf(chapterOrderConfigResponse.readmoney)));

        readmoney = chapterOrderConfigResponse.readmoney;

        myAdapter.addAll(chapterOrderConfigResponse.subdata);
        if(myAdapter.getItemCount() > 0){
            //默认选择第一项
            ChapterOrderConfigResponse.SubdataBean subdataBean = myAdapter.getItem(0);
            subdataBean.isChecked = true;
            String needCostCoin = getString(R.string.need_cost_coin,String.valueOf(subdataBean.disprce));
            SpannableStringBuilder builder = new SpannableStringBuilder(needCostCoin);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),3,needCostCoin.length()-3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tvNeedCostCoin.setText(builder);
            tvOriginalCostCoin.setText(getString(R.string.original_cost_coin,String.valueOf(subdataBean.subprice)));
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void bugChapterOrderSuccess() {
        showToast(getString(R.string.book_order_success));
        ReadActivity.lunchActivity(mActivity,bookid, chapterid);
        finish();
        ActivityStack.popToSpecifyActivity(BookContentsActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LUNCH_LOGIN_ACTIVITY_REQUEST_CODE && requestCode == RESULT_OK){
            chapterOrderPresenter.getChapterOrderConfig(userid,bookid,chapterid);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<ChapterOrderConfigResponse.SubdataBean,ChapterOrderViewHolder> {

        @NonNull
        @Override
        public ChapterOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_config_item,parent,false);
            ChapterOrderViewHolder myViewHolder = new ChapterOrderViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChapterOrderViewHolder holder, int position) {

            final ChapterOrderConfigResponse.SubdataBean subdataBean = getItem(position);
            holder.tvOrderTitle.setText(subdataBean.subtext);
            if(subdataBean.discount == 1){
                holder.tvOrderDiscount.setVisibility(View.GONE);

            }else{
                holder.tvOrderDiscount.setVisibility(View.VISIBLE);
                holder.tvOrderDiscount.setText(mActivity.getString(R.string.discount,String.valueOf(subdataBean.discount)));
            }

            holder.itemView.setSelected(subdataBean.isChecked);
            if(subdataBean.isChecked){
                holder.tvOrderTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
            }else{
                holder.tvOrderTitle.setTextColor(getResources().getColor(R.color.text_normal_color));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChapterOrderConfigResponse.SubdataBean ss ;
                    for (int i = 0; i < getItemCount(); i++) {
                        ss = getItem(i);
                        ss.isChecked = false;
                    }
                    subdataBean.isChecked = true;
                    notifyDataSetChanged();

                    String needCostCoin = getString(R.string.need_cost_coin,String.valueOf(subdataBean.disprce));
                    SpannableStringBuilder builder = new SpannableStringBuilder(needCostCoin);
                    builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),3,needCostCoin.length()-3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tvNeedCostCoin.setText(builder);
                    tvOriginalCostCoin.setText(getString(R.string.original_cost_coin,String.valueOf(subdataBean.subprice)));

                    if(readmoney >= subdataBean.disprce){
                        btnImmediatelyBuy.setText(R.string.immediately_buy);
                    }else{
                        btnImmediatelyBuy.setText(R.string.top_up_and_buy);
                    }
                }
            });
        }

        public ChapterOrderConfigResponse.SubdataBean getCheckedItem(){
            ChapterOrderConfigResponse.SubdataBean ss  = null;
            for (int i = 0; i < getItemCount(); i++) {
                ss = getItem(i);
                if(ss.isChecked){
                    return ss;
                }
            }
            return null;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnImmediatelyBuy:
                ChapterOrderConfigResponse.SubdataBean subdataBean = myAdapter.getCheckedItem();
                if(subdataBean != null){
                    if(readmoney >= subdataBean.disprce){
                        ChapterOrderConfigResponse.SubdataBean   ss = myAdapter.getCheckedItem();
                        chapterOrderPresenter.bugChapterOrder(userid,bookid,chapterid,ss.subnumber, false);
                    }else{
                        Intent intent = new Intent(mActivity,PayCenterActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        chapterOrderPresenter.detachView();
    }
}
