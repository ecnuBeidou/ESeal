package com.agenthun.eseal.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agenthun.eseal.R;
import com.agenthun.eseal.bean.updateByRetrofit.UpdateResponse;
import com.agenthun.eseal.connectivity.manager.DownloadCallBack;
import com.agenthun.eseal.connectivity.manager.DownloadSubscriber;
import com.agenthun.eseal.connectivity.manager.RetrofitManager;
import com.agenthun.eseal.connectivity.service.PathType;
import com.agenthun.eseal.utils.VersionHelper;
import com.pekingopera.versionupdate.util.FileUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * @project ESeal
 * @authors agenthun
 * @date 2017/2/22 11:58.
 */

public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";

    @Bind(R.id.app_version_name)
    AppCompatTextView appVersionName;

    @Bind(R.id.app_new_version_hint)
    AppCompatTextView appNewVersionHint;
    @Bind(R.id.app_new_version_name)
    AppCompatTextView appNewVersionName;

/*    @Bind(R.id.elastic_download_view)
    ElasticDownloadView downloadView;*/

    public static AboutFragment newInstance() {

        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        String versionName = VersionHelper.getVersionName(getContext());
        appVersionName.setText(versionName);

        checkUpdateVersion(true);
        return view;
    }

    @OnClick({R.id.update_version_area, R.id.app_introduction, R.id.app_thanks, R.id.app_about_me})
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.update_version_area:
                Log.d(TAG, "onClick() returned: update_version_area");
                checkUpdateVersion(false);
                break;
            case R.id.app_introduction:
                Log.d(TAG, "onClick() returned: app_introduction");
                break;
            case R.id.app_thanks:
                Log.d(TAG, "onClick() returned: app_thanks");
                break;
            case R.id.app_about_me:
                Log.d(TAG, "onClick() returned: app_about_me");
                break;
            default:
                break;
        }
    }

    private void showNoNewVersion() {
        appNewVersionHint.setVisibility(View.GONE);
        appNewVersionName.setVisibility(View.GONE);
    }

    private void showNewVersion(String newVersionName) {
        appNewVersionHint.setVisibility(View.VISIBLE);
        appNewVersionName.setVisibility(View.VISIBLE);
        appNewVersionName.setText(newVersionName);
    }

    private void showCheckUpdateVersionError() {
        showMessage(getString(R.string.error_check_update_version));
    }

    private void showDownloadFileError() {
        showMessage(getString(R.string.error_download_file));
    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        ((TextView) (snackbar.getView().findViewById(R.id.snackbar_text)))
                .setTextColor(ContextCompat.getColor(getContext(), R.color.blue_grey_100));
        snackbar.show();
    }

    private void showMessage(String message, String textActionButton, @Nullable View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG)
                .setAction(textActionButton, onClickListener);
        ((TextView) (snackbar.getView().findViewById(R.id.snackbar_text)))
                .setTextColor(ContextCompat.getColor(getContext(), R.color.blue_grey_100));
        snackbar.show();
    }

    private void showDialog(String title,
                            @Nullable String message,
                            @Nullable String textPositiveButton,
                            @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                            @Nullable String textNegativeButton,
                            @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(title)
                .setMessage(message);
        if (textPositiveButton != null) {
            builder.setPositiveButton(textPositiveButton, onPositiveButtonClickListener);
        }
        if (textNegativeButton != null) {
            builder.setNegativeButton(textNegativeButton, onNegativeButtonClickListener);
        }
        builder.show();
    }

    private void processUpdateVersion(boolean auto, final UpdateResponse.Entity entity) {
        if (entity == null) {
            showCheckUpdateVersionError();
        }
        //有更新版本
        if (entity.getVersionCode() > Integer.parseInt(VersionHelper.getVersionCode(getContext()))) {
            showNewVersion(entity.getVersionName());
        } else {
            showNoNewVersion();
        }

        if (!auto) {
//            entity.setVersionCode(4); //for my test download

            if (entity.getVersionCode() > Integer.parseInt(VersionHelper.getVersionCode(getContext()))) {
                String message = getString(R.string.text_update_latest_version) + entity.getVersionName() + "\n" +
                        getString(R.string.text_update_version_size) +
                        FileUtils.HumanReadableFilesize(entity.getApkSize()) + "\n\n" +
                        getString(R.string.text_update_version_content) + "\n" + entity.getUpdateContent();

                showDialog(getString(R.string.text_found_new_app_version),
                        message,
                        getString(R.string.text_app_update_now),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String packageName = VersionHelper.getPackageName(getContext());
                                String name = packageName.substring(packageName.lastIndexOf('.') + 1, packageName.length())
                                        + "_v_" + entity.getVersionName().replaceAll("\\.", "_").trim();
                                downloadLatestApp(entity.getUpdateUrl(), name);
                            }
                        },
                        getString(R.string.text_app_update_later),
                        null
                );
            } else if (entity.getVersionCode() == Integer.parseInt(VersionHelper.getVersionCode(getContext()))) {
                showMessage(getString(R.string.text_app_already_the_latest_version));
            }
        }
    }

    private void processInstallApk(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private void processUninstallApk(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);
    }

    private void checkUpdateVersion(final boolean auto) {
        RetrofitManager.builder(PathType.ESeal_LITE_UPDATE_SERVICE_URL)
                .checkAppLiteUpdateObservable()
                .subscribe(new Subscriber<UpdateResponse.Entity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!auto) {
                            showCheckUpdateVersionError();
                        } else {
                            Log.d(TAG, "Error - auto checkUpdateVersion");
                        }
                    }

                    @Override
                    public void onNext(UpdateResponse.Entity entity) {
                        processUpdateVersion(auto, entity);
                    }
                });
    }

    private void downloadLatestApp(final String url, final String fileName) {
        RetrofitManager.builder(getContext(), PathType.WEB_SERVICE_V2_TEST)
                .downloadFileObservable(url)
                .subscribe(new DownloadSubscriber<ResponseBody>(getContext(), fileName, new DownloadCallBack() {
                    @Override
                    public void onStart() {
                        showMessage(getString(R.string.text_download_file));
//                        downloadView.startIntro();
                    }

                    @Override
                    public void onProgress(long current, long total) {
                        Log.d(TAG, "downloaded: " + current + "/" + total);
//                        downloadView.setProgress(current / total);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onSuccess(final String path, final String name, long fileSize) {
                        showMessage(getString(R.string.success_download_file),
                                getString(R.string.text_app_install_now),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        processInstallApk(path);
                                    }
                                });
//                        showMessage("下载成功, path: " + path + ", size" + fileSize);
//                        downloadView.success();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showDownloadFileError();
//                        downloadView.fail();
                    }
                }));

        /*        RetrofitManager2.builder(getContext(), PathType.WEB_SERVICE_V2_TEST).downloadFileObservable(url, fileName, new DownloadCallBack() {
            @Override
            public void onStart() {
                showMessage("开始下载");
            }

            @Override
            public void onProgress(long current, long total) {
                Log.d(TAG, "downloaded: " + current + "/" + total);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onSuccess(String path, String name, long fileSize) {
                showMessage("path: " + path + ", size" + fileSize);
            }

            @Override
            public void onError(Throwable e) {
                showDownloadFileError();
            }
        });*/
    }
}
