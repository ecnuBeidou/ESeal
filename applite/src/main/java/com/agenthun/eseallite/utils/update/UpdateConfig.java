package com.agenthun.eseallite.utils.update;

import android.content.Context;

import com.agenthun.eseallite.R;
import com.agenthun.eseallite.model.ResponseResult;
import com.agenthun.eseallite.model.Version;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pekingopera.versionupdate.ParseData;
import com.pekingopera.versionupdate.UpdateHelper;
import com.pekingopera.versionupdate.bean.Update;
import com.pekingopera.versionupdate.type.RequestType;

import java.lang.reflect.Type;

/**
 * Created by wayne on 10/11/2016.
 */

public class UpdateConfig {

    /**
     * Update check via Http Get
     */
    public static void initGet(Context context) {
        UpdateHelper.init(context);

        String url = "http://www.freight-track.com/update/updatecheck3.aspx";
        UpdateHelper.getInstance()
                .setMethod(RequestType.get)
                .setCheckUrl(url)
                .setDialogLayout(R.layout.dialog_update)
                .setCheckJsonParser(new ParseData() {
                    @Override
                    public Update parse(String response) {
                        Update update = new Update();

                        ResponseResult<Version> result;

                        GsonBuilder gson = new GsonBuilder();
                        Type resultType = new TypeToken<ResponseResult<Version>>() {
                        }.getType();

                        try {
                            result = gson.create().fromJson(response, resultType);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return update;
                        }

                        Version version = result.getEntity();

                        update.setUpdateUrl(version.getUpdateUrl());
                        update.setVersionCode(version.getVersionCode());
                        update.setApkSize(version.getApkSize());
                        update.setVersionName(version.getVersionName());
                        update.setUpdateContent(version.getUpdateContent());
                        update.setForce(version.isForce());

                        return update;
                    }
                });
    }
}