package com.shangame.fiction.ui.task;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.storage.model.DateBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/4/3
 */
public class ChangeDateFragment extends BottomSheetDialogFragment {

    private RecyclerView mRecyclerView;

    private MyAdapter myAdapter;

    private RedPacketActivity redPacketActivity;


    private DateBean currentDateBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_date,null);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);

        redPacketActivity = (RedPacketActivity) getActivity();
        currentDateBean = redPacketActivity.getCurrentDateBean();

        List<DateBean> date = computeDate();

        myAdapter.addAll(date);
        myAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private List<DateBean> computeDate(){

        List<DateBean> mouthList = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month= calendar.get(Calendar.MONTH)+1;

            if( currentDateBean == null){
                currentDateBean = new DateBean(year,month);
                redPacketActivity.setCurrentDateBean(currentDateBean);
            }

            for (int i = month; i >0 ; i--) {
                mouthList.add(new DateBean(year,i));
            }
            year--;
            for (int i = 12; i > 0 ; i--) {
                mouthList.add(new DateBean(year,i));
            }

        }else{

            mouthList.add(new DateBean(2019,3));
            mouthList.add(new DateBean(2019,2));
            mouthList.add(new DateBean(2019,1));
        }
        return mouthList;
    }


    class MyAdapter extends WrapRecyclerViewAdapter<DateBean,MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.date_item,viewGroup,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final DateBean time = getItem(i);
            myViewHolder.tvTime.setText(time.toString());

            if(time.equals(currentDateBean)){
                myViewHolder.ivCheck.setVisibility(View.VISIBLE);
            }else{
                myViewHolder.ivCheck.setVisibility(View.GONE);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentDateBean = time;
                    redPacketActivity.setCurrentDateBean(currentDateBean);
                    notifyDataSetChanged();
                    dismiss();
                    redPacketActivity.resetPage();
                    redPacketActivity.loadData(currentDateBean.toString2(),1);
                }
            });

        }
    }






    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        ImageView ivCheck;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            ivCheck = itemView.findViewById(R.id.ivCheck);
        }
    }


}
