package co.il.jabrutouch.user_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import java.util.Set;



public class ConfigFile {

    private static ConfigFile mConfigF;

    private SharedPreferences mConfigFile;



    /**
     * Default {@link SharedPreferences}
     */

    public static ConfigFile getInstance(Context context) {

        if (mConfigF == null) {

            mConfigF = new ConfigFile(context);
        }

        return mConfigF;
    }



    public ConfigFile(Context context) {
        mConfigFile = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ConfigFile(Context context, String name) {
        mConfigFile = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }



    public void setProperty(String key, String value) {
        Editor editor = open();
        editor.putString(key, value);
        editor.apply();
    }


    public void setProperty(String key, int value) {
        Editor editor = open();
        editor.putInt(key, value);
        editor.apply();
    }


    public void setProperty(String key, long value) {
        Editor editor = open();
        editor.putLong(key, value);
        editor.apply();
    }


    public void setProperty(String key, float value) {
        Editor editor = open();
        editor.putFloat(key, value);
        editor.apply();
    }


    public void setProperty(String key, boolean value) {
        Editor editor = open();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public String getProperty(String key, String defValue) {
        return mConfigFile.getString(key, defValue);
    }


    public int getProperty(String key, int defValue) {
        return mConfigFile.getInt(key, defValue);
    }


    public long getProperty(String key, long defValue) {
        return mConfigFile.getLong(key, defValue);
    }


    public float getProperty(String key, float defValue) {
        return mConfigFile.getFloat(key, defValue);
    }


    public boolean getProperty(String key, boolean defValue) {
        return mConfigFile.getBoolean(key, defValue);
    }

    public Set<String> getProperties(String key, Set<String> defValue) {
        return mConfigFile.getStringSet(key, defValue);
    }


    private Editor open() {
        return mConfigFile.edit();
    }


    public SharedPreferences getPreferences() {
        return mConfigFile;
    }


    public boolean deleteAll() {
        Editor editor = open();
        editor.clear();
        return editor.commit();
    }


    public void setOnFileChangedListener(OnSharedPreferenceChangeListener changeListener) {
        mConfigFile.registerOnSharedPreferenceChangeListener(changeListener);
    }

}
