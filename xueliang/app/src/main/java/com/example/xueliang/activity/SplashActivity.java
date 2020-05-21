package com.example.xueliang.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.base.BasePresenter;
import com.example.xueliang.bean.AppLogoInfoBean;
import com.example.xueliang.bean.AppUpdateInfoBean;
import com.example.xueliang.network.ResponceSubscriber;
import com.example.xueliang.network.RetrofitManager;
import com.example.xueliang.network.RxSchedulerUtils;
import com.example.xueliang.utils.AppUtils;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.SPUtil;
import com.example.xueliang.utils.StringUtils;
import com.example.xueliang.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class SplashActivity extends BaseMvpActivity {
    //跳转延迟1s时间
    private int TIME = 1000;
    private ProgressBar mProgress;
    private TextView mProgressText;
    private AlertDialog downloadDialog;
    private String apkUrl;
    private boolean isFoce;

    @Override
    public BasePresenter setPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        getAppLogoInfo();
    }

    @Override
    public void initListener() {

    }

    public  void getAppLogoInfo () {
        Map<String, Object> params = new HashMap<>();
        RetrofitManager.getDefault().getAppNameAndLogoUrl(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List<AppLogoInfoBean>>() {

                    @Override
                    protected void onSucess(List<AppLogoInfoBean> list) {
                        if (list != null && list.size() > 0) {
                            AppLogoInfoBean appLogoInfoBean = list.get(0);
                            SPUtil.keepAppLogoInfo(appLogoInfoBean);
                        }
                        getApkIsUpdate();
                    }

                    @Override
                    protected void onFail(String message) {
                        super.onFail(message);
                        getApkIsUpdate();
                    }
                });
    }

    public void getApkIsUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("version", AppUtils.getAppVersion());
        RetrofitManager.getDefault().getApkisUpdate(params)
                .compose(RxSchedulerUtils::toSimpleSingle)
                .subscribe(new ResponceSubscriber<List<AppUpdateInfoBean>>() {
                    @Override
                    protected void onSucess(List<AppUpdateInfoBean> list) {
                        Log.e("getApkisUpdate", "list="+list);
                        if (list != null && list.size() > 0) {
                            AppUpdateInfoBean appUpdateInfoBean = list.get(0);
//                            appUpdateInfoBean.setType("1");
                            if ("0".equals(appUpdateInfoBean.getType())){//无需更新
                                skipLogin();
                            }else if ("1".equals(appUpdateInfoBean.getType()) || "2".equals(appUpdateInfoBean.getType())){//需要更新
                                showUpdateDialog(appUpdateInfoBean);
                            }
                        }else {
                            skipLogin();
                        }
                    }

                    @Override
                    protected void onFail(String err) {
                        skipLogin();
                    }
                });
    }


    /**
     * 有更新展示 提示框
     */
    private void showUpdateDialog(AppUpdateInfoBean userInfoEntity) {
        String tipTitle = userInfoEntity.getTipTitle();
        if (StringUtils.isBlank(tipTitle)){
            tipTitle = "更新";
        }
        String tipMessage = userInfoEntity.getTipMessage();

        if (StringUtils.isBlank(tipMessage)){
            tipMessage = "有更新版本了";
        }

        String cancel = "取 消";
        if ("2".equals(userInfoEntity.getType())){
            isFoce = true;
            cancel = null; //强制更新
        }

        DialogUtil.showAlert(mContext, tipTitle, tipMessage,
                "确 定", (dialog, which) -> {
                    //开始下载逻辑
                    apkUrl = userInfoEntity.getUrl();
                    showDownloadDialog();
                    dialog.dismiss();
                }, cancel, (dialog, which) -> {
                    skipLogin();
                    dialog.dismiss();
                }, false);
    }

    private void skipLogin() {
        if (!isFoce){
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }


    /**
     * 显示下载对话框
     */
    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("下载更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.fr_update_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        mProgressText = (TextView) v.findViewById(R.id.update_progress_text);

        builder.setView(v);

        if (false) {
            builder.setCancelable(false);
        } else {
            builder.setCancelable(true);
            builder.setNegativeButton(mContext.getString(R.string.dialog_cancel ), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    interceptFlag = true;
                    skipLogin();
                    dialog.dismiss();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    interceptFlag = true;
                    dialog.dismiss();
                }
            });
        }
        downloadDialog = builder.create();
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.show();

        downloadApk();
    }


    // 下载线程
    private Thread downLoadThread;
    /**
     * 下载apk
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    private static final int DOWN_NOSDCARD = 0;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private static final int DOWN_ERROR = 3;

    @SuppressLint({"ShowToast", "HandlerLeak"})
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    mProgressText.setText(tmpFileSize + "/" + apkFileSize);
                    break;
                case DOWN_OVER:
                    downloadDialog.dismiss();
                    installApk();
                    break;
                case DOWN_NOSDCARD:
                    downloadDialog.dismiss();
                    break;
                case DOWN_ERROR:
                    downloadDialog.dismiss();
                    break;
            }
        };
    };

    private File savePath;
    private File apkFile;
    private File tmpFile;
    private String apkFileSize;
    private String tmpFileSize;
    private int progress;
    // 终止标记
    private boolean interceptFlag;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                //下载的apk文件与包名关联，防止多个app同时更新时apk文件名冲突
//                String packageName = AppUtils.getApplication().getPackageName();
                String apkName =  "xueliang.apk";
                String tmpApk =  "xueliang.tmp";
                // 判断是否挂载了SD卡
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    savePath = AppUtils.getApplication().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//                    AppUtils.getApplication().getExternalStorageDirectory()
                }else {
                    savePath = AppUtils.getApplication().getFilesDir();
                }

                apkFile = new File(savePath, apkName);
                tmpFile = new File(savePath, tmpApk);
                // 是否已下载更新文件
//				if (apkFile.exists()) {
//					mHandler.sendEmptyMessage(DOWN_OVER);
//					return;
//				}

                // 输出临时下载文件
                FileOutputStream fos = new FileOutputStream(tmpFile);

                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                // 显示文件大小格式：2个小数点显示
                DecimalFormat df = new DecimalFormat("0.00");
                // 进度条下面显示的总文件大小
                apkFileSize = df.format((float) length / 1024 / 1024) + "MB";

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    // 进度条下面显示的当前下载文件大小
                    tmpFileSize = df.format((float) count / 1024 / 1024) + "MB";
                    // 当前进度值
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成 - 将临时下载文件转成APK文件
                        if (tmpFile.renameTo(apkFile)) {
                            // 通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                        }
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载

                fos.close();
                is.close();
            } catch (Exception e) {
                mHandler.sendEmptyMessage(DOWN_ERROR);
                e.printStackTrace();
            }

        }
    };


    /**
     * 安装apk
     */
    private void installApk() {
        if (!apkFile.exists()) {
            return;
        }

        boolean haveInstallPermission = true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            haveInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
        }
        if(!haveInstallPermission){
            //权限没有打开则提示用户去手动打开
            ToastUtils.show("你没有安装权限");
            Uri packageURI = Uri.parse("package:"+mContext.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
            startActivityForResult(intent, 1000);
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);

        /**
         * 让安装界面在运行的APP的前面
         */
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri apkUri = null;
        //判断是否是Android7.0以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", new File(apkFile.toString()));
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri =Uri.parse("file://" + apkFile.toString());
        }

        i.setDataAndType(apkUri,
                "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            installApk();
        }
    }
}

