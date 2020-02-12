package com.shangame.fiction.ui.bookrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookRackFilterConfigResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.FilterView;

/**
 * 筛选
 * Create by Speedy on 2018/8/1
 */
public class FilteBookActivity extends BaseActivity implements View.OnClickListener,BookFilterContacts.View{


    private FilterView filterViewAll;

    private FilterView filterView1;
    private FilterView filterView2;
    private FilterView filterView3;
    private FilterView filterView4;
    private FilterView filterView5;
    private FilterView filterView6;
    private FilterView filterView7;
    private FilterView filterView8;

    private int statusid;
    private int readecid;
    private int numcid;
    private int malecid;

    private BookFilterPresenter bookFilterPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filte_book);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.filter);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.btnFinish).setOnClickListener(this);

        filterViewAll = (FilterView) findViewById(R.id.filterViewAll);
        filterViewAll.setSelected(true);

        filterView1 = (FilterView) findViewById(R.id.filterView1);
        filterView2 = (FilterView) findViewById(R.id.filterView2);
        filterView3 = (FilterView) findViewById(R.id.filterView3);
        filterView4 = (FilterView) findViewById(R.id.filterView4);
        filterView5 = (FilterView) findViewById(R.id.filterView5);
        filterView6 = (FilterView) findViewById(R.id.filterView6);
        filterView7 = (FilterView) findViewById(R.id.filterView7);
        filterView8 = (FilterView) findViewById(R.id.filterView8);

        filterViewAll.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView1.setSelected(false);
                    filterView2.setSelected(false);
                    filterView3.setSelected(false);
                    filterView4.setSelected(false);
                    filterView5.setSelected(false);
                    filterView6.setSelected(false);
                    filterView7.setSelected(false);
                    filterView8.setSelected(false);

                    statusid = 0;
                    readecid = 0;
                    numcid = 0;
                    malecid = 0;
                }
            }
        });
        filterView1.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterViewAll.setSelected(false);
                    filterView2.setSelected(false);
                    statusid = id;
                }else{
                    statusid = 0;
                }
            }
        });
        filterView2.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView1.setSelected(false);
                    filterViewAll.setSelected(false);
                    statusid = id;
                }else{
                    statusid = 0;
                }
            }
        });
        filterView3.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView4.setSelected(false);
                    filterViewAll.setSelected(false);
                    readecid = id;
                }else{
                    readecid = 0;
                }
            }
        });
        filterView4.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView3.setSelected(false);
                    filterViewAll.setSelected(false);
                    readecid = id;
                }else{
                    readecid = 0;
                }
            }
        });
        filterView5.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView6.setSelected(false);
                    filterViewAll.setSelected(false);
                    numcid = id;
                }else{
                    numcid = 0;
                }
            }
        });
        filterView6.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView5.setSelected(false);
                    filterViewAll.setSelected(false);
                    numcid = id;
                }else{
                    numcid = 0;
                }
            }
        });
        filterView7.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView8.setSelected(false);
                    filterViewAll.setSelected(false);
                    malecid = id;
                }else{
                    malecid = 0;
                }
            }
        });
        filterView8.setOnStateChangeLinstener(new FilterView.OnStateChangeLinstener() {
            @Override
            public void onStateChange(boolean selected,int id) {
                if(selected){
                    filterView7.setSelected(false);
                    filterViewAll.setSelected(false);
                    malecid = id;
                }else{
                    malecid = 0;
                }
            }
        });

        bookFilterPresenter = new BookFilterPresenter();
        bookFilterPresenter.attachView(this);

        bookFilterPresenter.getFilterConfig(UserInfoManager.getInstance(mContext).getUserid());
    }


    @Override
    public void filterBookSuccess(BookRackResponse bookRackResponse) {

    }

    @Override
    public void getFilterConfig(BookRackFilterConfigResponse bookRackFilterConfigResponse) {

        BookRackFilterConfigResponse.FilterItemBean filterItemBean = new BookRackFilterConfigResponse.FilterItemBean();
        filterItemBean.configname = getString(R.string.all);
        filterItemBean.boookcount = bookRackFilterConfigResponse.sumboookcount;

        filterViewAll.setFilterItemBean(filterItemBean);

        filterView1.setFilterItemBean(bookRackFilterConfigResponse.statusdata.get(0));
        filterView2.setFilterItemBean(bookRackFilterConfigResponse.statusdata.get(1));

        filterView3.setFilterItemBean(bookRackFilterConfigResponse.readdata.get(0));
        filterView4.setFilterItemBean(bookRackFilterConfigResponse.readdata.get(1));

        filterView5.setFilterItemBean(bookRackFilterConfigResponse.numberdata.get(0));
        filterView6.setFilterItemBean(bookRackFilterConfigResponse.numberdata.get(1));

        filterView7.setFilterItemBean(bookRackFilterConfigResponse.maledata.get(0));
        filterView8.setFilterItemBean(bookRackFilterConfigResponse.maledata.get(1));

    }





    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }else if(view.getId() == R.id.btnFinish){

            Intent intent = new Intent();
            intent.putExtra("statusid",statusid);
            intent.putExtra("readecid",readecid);
            intent.putExtra("numcid",numcid);
            intent.putExtra("malecid",malecid);

            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookFilterPresenter.detachView();
    }

}
