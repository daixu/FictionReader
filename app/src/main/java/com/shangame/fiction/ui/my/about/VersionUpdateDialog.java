package com.shangame.fiction.ui.my.about;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.net.response.VersionCheckResponse;

/**
 * Create by Speedy on 2018/9/26
 */
public class VersionUpdateDialog extends DialogFragment implements View.OnClickListener{

    private TextView tvNewVersion;
    private TextView tvUpdateInfo;
    private Button btnUpdate;
    private Button btnPostponeUpdate;

    private VersionCheckResponse versionCheckResponse;

    private UpdateVersionListener updateVersionListener;


    public static final VersionUpdateDialog newInstance(Activity activity,VersionCheckResponse versionCheckResponse){
        VersionUpdateDialog versionUpdateDialog = new VersionUpdateDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("VersionCheckResponse",versionCheckResponse);
        versionUpdateDialog.setArguments(bundle);
        return versionUpdateDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        versionCheckResponse = getArguments().getParcelable("VersionCheckResponse");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_version_update,container,false);

        tvNewVersion = view.findViewById(R.id.tvNewVersion);
        tvUpdateInfo = view.findViewById(R.id.tvUpdateInfo);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnPostponeUpdate = view.findViewById(R.id.btnPostponeUpdate);

        btnUpdate.setOnClickListener(this);
        btnPostponeUpdate.setOnClickListener(this);

        tvNewVersion.setText(getString(R.string.new_version_name,versionCheckResponse.remark));
        tvUpdateInfo.setText(versionCheckResponse.content);

        if(versionCheckResponse.updatetype == 2){
            btnPostponeUpdate.setVisibility(View.GONE);
            setCancelable(false);
        }else{

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = (int) ScreenUtils.dpToPx(getActivity(),300f);
        getDialog().getWindow().setLayout(width,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnPostponeUpdate){
            dismiss();
        }else if(v.getId() == R.id.btnUpdate){
            dismiss();
            if(updateVersionListener != null){
                updateVersionListener.updateVersion(versionCheckResponse.dowurl);
            }
        }
    }


    public void setUpdateVersionListener(UpdateVersionListener updateVersionListener) {
        this.updateVersionListener = updateVersionListener;
    }

    public interface UpdateVersionListener{
        void updateVersion(String url);
    }



}
