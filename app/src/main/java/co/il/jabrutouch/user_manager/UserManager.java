package co.il.jabrutouch.user_manager;

import android.content.Context;


public class UserManager {

    public static void setToken(String token, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_TOKEN, token);

    }

    public static String getToken(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_TOKEN, Properties.DEFAULT_TOKEN);

    }

    public static void setUser(String user, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_USER, user);

    }

    public static String getUser(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_USER, Properties.DEFAULT_TOKEN);

    }

    public static void setNotFirstTime(boolean isFirstTime, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.NOT_FIRST_TIME, isFirstTime);

    }

    public static boolean getNotFirstTime(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.NOT_FIRST_TIME, Properties.DEFAULT_NOT_FIRST_TIME);
    }


    public static void setTodayDafYomi(String todayDafYomi, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.DAF_YOMI, todayDafYomi);

    }

    public static String getTodayDafYomi(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.DAF_YOMI, Properties.DEFAULT_DAF_YOMI);
    }


    public static void setMasechtotList(String masechtotList, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_MASECHTOT_LIST, masechtotList);

    }

    public static String getMasechtotList(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_MASECHTOT_LIST, Properties.DEFAULT_TOKEN);

    }

    public static void setFirstTimeInsideTheApp(boolean isFirstTime, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.FIRST_TIME_INSIDE_APP, isFirstTime);

    }


    public static boolean getFirstTimeInsideTheApp(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.FIRST_TIME_INSIDE_APP, Properties.DEFAULT_FIRST_TIME_INSIDE_APP);
    }


    public static void setRecentGemaraPlayed(String jsonGemaraPage, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_RECENT_GEMARA, jsonGemaraPage);

    }

    public static String getRecentGemaraPlayed(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_RECENT_GEMARA, Properties.DEFAULT_TOKEN);

    }


    public static void setRecentMishnaPlayed(String jsonMishnaPage, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_RECENT_MISHNA, jsonMishnaPage);

    }

    public static String getRecentMishnaPlayed(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_RECENT_MISHNA, Properties.DEFAULT_TOKEN);
    }


    public static void setAnalyticsData(String analyticsData, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_ANALYTICS_DATA, analyticsData);

    }

    public static String getAnalyticsData(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_ANALYTICS_DATA, Properties.DEFAULT_TOKEN);
    }

    public static void setDonationIndex(int index, Context context) {
        ConfigFile.getInstance(context).setProperty(Properties.KEY_DONATION_INDEX, index);
    }

    public static int getDonationIndex(Context context) {
        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_DONATION_INDEX, 0);
    }


    public static int getCurrentChat(Context context) {
        return ConfigFile.getInstance(context).getProperty(Properties.CURRENT_CHAT, -1);

    }


    public static void setCurrentChat(Context context,int chatId) {
        ConfigFile.getInstance(context).setProperty(Properties.CURRENT_CHAT, chatId);

    }

    public static boolean getUpdateMainActivity(Context context) {
        return ConfigFile.getInstance(context).getProperty(Properties.UPDATE_MAIN_ACTIVITY, false);

    }


    public static void setUpdateMainActivity(Context context, boolean update) {
        ConfigFile.getInstance(context).setProperty(Properties.UPDATE_MAIN_ACTIVITY, update);

    }

    public static void setMessageForOffline(String jsonMessage, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_MESSAGE_FOR_OFFLINE, jsonMessage);

    }


    public static String getMessageForOffline(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_MESSAGE_FOR_OFFLINE, Properties.DEFAULT_TOKEN);
    }


    public static void setFcmToken(String fcmToken, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_FCM_TOKEN, fcmToken);

    }


    public static String getFcmToken(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_FCM_TOKEN, Properties.DEFAULT_TOKEN);

    }


    public static void setNotFirstTimeInDonationFirstTime(boolean isFirstTimeInDonation, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.NOT_FIRST_TIME_IN_DONATION, isFirstTimeInDonation);

    }

    public static boolean getNotFirstTimeInDonationFirstTime(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.NOT_FIRST_TIME_IN_DONATION, Properties.DEFAULT_NOT_FIRST_TIME_IN_DONATION);
    }



    public static void setDonationData(String donationData, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.KEY_DONATION_DATA, donationData);

    }

    public static String getDonationData(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.KEY_DONATION_DATA, Properties.DEFAULT_TOKEN);

    }


    public static void setInPendingMode(boolean inPendingMode, Context context) {

        ConfigFile.getInstance(context).setProperty(Properties.IN_PENDING_MODE, inPendingMode);

    }

    public static boolean getInPendingMode(Context context) {

        return ConfigFile.getInstance(context)
                .getProperty(Properties.IN_PENDING_MODE, Properties.DEFAULT_IN_PENDING_MODE);
    }
}
