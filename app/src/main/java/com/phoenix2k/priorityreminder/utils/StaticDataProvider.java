package com.phoenix2k.priorityreminder.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.phoenix2k.priorityreminder.BuildConfig;
import com.phoenix2k.priorityreminder.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Pushpan on 24/07/15.
 */
public class StaticDataProvider {
    /**
     * Use this to generate JSON files for all the responses when the user browses the application
     * This creates the response JSON files from reading the responses are creates a unique filename
     * to load it next time when the Application is run wih Mockdata
     */
//    public static final boolean GENERATE_MOCKUP = false && BuildConfig.DEBUG;
    /**
     * Use this to load the static JSON files from the internal memory under MOCK_DATA_FOLDER
     * false to load it from assets folder
     */
//    public static final boolean LOAD_FROM_INTERNAL_MEMORY = false;
    private static final String MOCK_DATA_FOLDER = "mockdata";
    /**
     * This file is use to store the mapping of all the dynamic requests and their filenames
     * After detecting the file user can make chnages inside the JSON file to test specific
     * situations. MOCK_HASHED_NAME_FILE should not ne changed as it is automatically generated
     * and modified by the app
     */
    private static final String MOCK_HASHED_NAME_FILE = "hashed_name_file.json";

    private static StaticDataProvider mInstance;
    private Context mContext;
    private File mMockDataDir;
    private boolean mUseSDCard = true;
    private boolean mEnableStaticEngine = false;
    private boolean mGenerateCacheDebugOption = true;
    private boolean mUseAssetCacheDebugOption = true;

    public static StaticDataProvider init(Context context) {
        if (mInstance == null) {
            mInstance = new StaticDataProvider(context);
        }
        return mInstance;
    }

    public static void deInit(Context context) {
        mInstance = null;
    }

    public static StaticDataProvider getLiveInstance() {
        return mInstance;
    }

    public static StaticDataProvider newInstanceFromLiveInstance() throws Exception {
        return new StaticDataProvider(getLiveInstance().mContext);
    }

    public static boolean hasLiveInstance() {
        return mInstance != null;
    }

    public StaticDataProvider(Context context) {
        this.mContext = context;
        if (mUseSDCard) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                //handle case of no SDCARD present
                mMockDataDir = new File(mContext.getFilesDir(), MOCK_DATA_FOLDER);
            } else {
                String dir = Environment.getExternalStorageDirectory() + File.separator + mContext.getString(R.string.app_name) + "_" + MOCK_DATA_FOLDER;
                //create folder
                mMockDataDir = new File(dir); //folder name
            }
        } else {
            mMockDataDir = new File(mContext.getFilesDir(), MOCK_DATA_FOLDER);
        }
        if (!mMockDataDir.exists()) {
            mMockDataDir.mkdir();
        }
    }

    private String getTextFromAsset(String filename) {
        String text = null;
        AssetManager am = mContext.getAssets();
        InputStream is;
        try {
            is = am.open(filename);
        } catch (IOException e1) {
            e1.printStackTrace();
            return text;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader f = new BufferedReader(inputStreamReader);
        String body = "";
        String line = "";

        do {
            if (line != null) {
                body += line;
            }

            try {
                line = f.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                line = null;
            }
        } while (line != null);
        text = body;
        text = formattedText(text);
        return text;
    }

    public String getTextFromInternalMemory(String fileName) {
        String text = null;
        FileInputStream fis = null;
        File file = new File(mMockDataDir, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fis = new FileInputStream(file);
        } catch (IOException e1) {
            e1.printStackTrace();
            return text;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis);
        BufferedReader f = new BufferedReader(inputStreamReader);
        String body = "";
        String line = "";

        do {
            if (line != null) {
                body += line;
            }

            try {
                line = f.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                line = null;
            }
        } while (line != null);
        text = body;
        text = formattedText(text);
        return text;
    }

    public JSONObject getJSONFor(String filename) {
        JSONObject json = null;
        String text = null;
        if (BuildConfig.DEBUG) {
            Log.i(getClass().getName(), "Using Static Data for file" + filename);
        }
        if (!mUseAssetCacheDebugOption) {
            text = getTextFromInternalMemory(filename);
        } else {
            text = getTextFromAsset(filename);
        }
        if (text == null) {
            return json;
        }
        text = formattedText(text);
        try {
            json = new JSONObject(text);
        } catch (JSONException er) {
            er.printStackTrace();
        }
        return json;
    }

    public static String formattedText(String actualText) {
        if (actualText != null) {
            actualText = actualText.replaceAll("\t", "").replaceAll("\n",
                    "<br/>");
        }
        return actualText;
    }

    public byte[] getDataBytesFor(String filename) {
        JSONObject json = null;
        String text = null;
        if (BuildConfig.DEBUG) {
            Log.i(getClass().getName(), "Using Static JSON for file" + filename);
        }
        if (!mUseAssetCacheDebugOption) {
            text = getTextFromInternalMemory(filename);
        } else {
            text = getTextFromAsset(filename);
        }
        if (text == null || text.trim().length() == 0) {
            return null;
        }
        text = formattedText(text);
        try {
            json = new JSONObject(text);
        } catch (JSONException er) {
            er.printStackTrace();
        }
        return json.toString().getBytes();
    }

    public void generateMockup(String fileName, byte[] receivedData) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(mMockDataDir, fileName));
            fos.write(receivedData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void checkAndAddToHashedJSON(String fileName, String request) {
        File hashedJSONFile = new File(mMockDataDir, MOCK_HASHED_NAME_FILE);
        if (!hashedJSONFile.exists()) {
            try {
                hashedJSONFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        JSONObject hashedJSON = getJSONFor(MOCK_HASHED_NAME_FILE);
        if (hashedJSON == null) {
            hashedJSON = new JSONObject();
        }
        if (!hashedJSON.has(fileName)) {
            try {
                hashedJSON.put(fileName, request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            generateMockup(MOCK_HASHED_NAME_FILE, hashedJSON.toString().getBytes());
        }
    }

    public static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
            pos = str.indexOf(c, pos + 1);
        return pos;
    }

    private static String getHashedFileNameFromFrom1(String dynamicRequest) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(dynamicRequest.getBytes());
        return new String(messageDigest.digest());
    }


    private static String getHashedFileNameFromFrom(String dynamicRequest) {
        int hash = 7;
        for (int i = 0; i < dynamicRequest.length(); i++) {
            hash = hash * 31 + dynamicRequest.charAt(i);
        }
        return hash + "";
    }

    public StaticDataProvider setEnableStaticEngine(boolean enable) {
        mEnableStaticEngine = enable;
        return this;
    }

    public boolean isUsingEnableStaticEngine() {
        return mEnableStaticEngine;
    }

    public StaticDataProvider setGenerateCacheDebugOption(boolean enable) {
        mGenerateCacheDebugOption = enable;
        return this;
    }

    public boolean shouldGenerateCacheForDebug() {
        return isUsingEnableStaticEngine() && mGenerateCacheDebugOption;
    }

    public StaticDataProvider setUseAssetCacheDebugOption(boolean enable) {
        mUseAssetCacheDebugOption = enable;
        return this;
    }

    public boolean shouldUseDataFromAsset() {
        return isUsingEnableStaticEngine() && !shouldGenerateCacheForDebug() && mUseAssetCacheDebugOption;
    }

    public boolean shouldUseDataFromMemoryCard() {
        return isUsingEnableStaticEngine() && !shouldGenerateCacheForDebug() && !mUseAssetCacheDebugOption;
    }

    public StaticDataProvider setUseSDCard(boolean enable) {
        mUseSDCard = enable;
        return this;
    }
}
