package com.example.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int READ_PERMISSION = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            boolean hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (!hasReadPermission || !hasWritePermission) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PERMISSION);
            } else {
                checkAndInstallApp();
            }
        } else {
            checkAndInstallApp();
        }
//        findViewById(R.id.btn_send_log).setOnClickListener(this);
//        Utils.appendLog(this, "LifeCycle Event: onCreate");
        progressDialog = new ProgressDialog(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            boolean packageInstalls = getPackageManager().canRequestPackageInstalls();
//            if (packageInstalls) {
//                Toast.makeText(this, "Request Install success!!!", Toast.LENGTH_SHORT).show();
//            } else {
//                startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))));
//            }
//        }
    }

    private void checkAndInstallApp() {

        new MaterialDialog.Builder(this)
                .content("App has new version, update now?")
                .onPositive((dialog, which) -> {
                    downloadAPK();
                })
                .positiveText("OK")
                .negativeText("No")
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    private void downloadAPK() {
        progressDialog.setTitle("Upload log");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        String fileName = "baApp.apk";
        String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        AndroidNetworking
                .download("http://hienlt.online:8080/web/apk/ba-v1.0.apk", downloadDir, fileName)
                .setTag("downloadAPK")
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        int percent = (int) ((bytesDownloaded / (float) totalBytes) * 100);
                        progressDialog.setProgress(percent);
                    }
                }).startDownload(new DownloadListener() {
            @Override
            public void onDownloadComplete() {
                progressDialog.dismiss();
                installApkFile(new File(downloadDir, fileName));
            }

            @Override
            public void onError(ANError anError) {
                progressDialog.dismiss();
                Log.d(TAG, "error", anError);
                Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void installApkFile(File file) {
        if (!file.exists()) {
            Toast.makeText(this, "APK file not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            this.grantUriPermission(this.getPackageName(), contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utils.appendLog(this, "LifeCycle Event: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.appendLog(this, "LifeCycle Event: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.appendLog(this, "LifeCycle Event: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.appendLog(this, "LifeCycle Event: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.appendLog(this, "LifeCycle Event: onDestroy");
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat dateFormatterToServer = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String fileName = "bat-log-" + dateFormatterToServer.format(new Date()) + ".txt";
        String dir = getFilesDir().getAbsolutePath() + File.separator + "BATCADS";
        File logFile = new File(dir + File.separator + fileName);
        if (logFile.exists()) {
            pushLog(logFile);
        } else {
            Toast.makeText(this, "Log file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void pushLog(File logFile) {
        progressDialog.setTitle("Upload log");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
        AndroidNetworking.upload("http://hienlt.online:8080/web/upload.php")
                .addMultipartFile("file", logFile)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        progressDialog.setProgress((int) ((bytesUploaded / (float) totalBytes) * 100));
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    checkAndInstallApp();
                } else {
                    Toast.makeText(this, "The app was not allowed to read in your storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
