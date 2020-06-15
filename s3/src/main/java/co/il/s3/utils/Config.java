package co.il.s3.utils;

import android.content.Context;

import com.amazonaws.regions.Regions;

import java.util.Objects;

public class Config {

    static final String PULL_ID = "us-east-1:afda3af8-a474-4e2d-9b8f-806ce98699a4";
    static final String BUCKET_NAME = "jabrutouch-cms-media";
    public static final String PATH_NAME = "/jabrutoch/";
    static Regions REGION = Regions.US_WEST_2;

    public static String getPathName(Context context){
        return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + PATH_NAME;
    }
}
