package co.il.jabrutouch.daf_yomi_manager;

import android.content.Context;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import co.il.model.model.DafHyomi;
import co.il.model.model.TodayDafYomiDetailes;


public class DafYomiCalculator {


    private static final String KINIM = "Kinim";
    private static final String TAMID = "Tamid";
    private static final String MIDOT = "Midot";



    /**
     * get today daf yomi
     * @param context Context
     * @return String
     */
    public TodayDafYomiDetailes getTodayDafYomi(Context context) {

        long datesFromDateStart = getDateDifference();
        DafHyomi dafHyomi = getAllDafYomi(context);
        long toadyDafYomiInt = Objects.requireNonNull(dafHyomi).getDafYomiDetails().getDafStart() + datesFromDateStart;
        int getTheWholeNUmber = (int) toadyDafYomiInt / dafHyomi.getDafYomiDetails().getAllPages();
        long toadyDafYomiFronNewSederInt = toadyDafYomiInt - (getTheWholeNUmber * dafHyomi.getDafYomiDetails().getAllPages());

        return (getCorrectDaf(dafHyomi, toadyDafYomiFronNewSederInt));

    }




    /**
     * get the dates difference between today to the daf yomi startDate
     * @return long
     */
    private long getDateDifference() {

        Calendar cal1 = Calendar.getInstance(Locale.getDefault());
        Calendar cal2 = Calendar.getInstance();

//        cal1.set(2019, Calendar.OCTOBER, 20); // can set any day to check the daf // change calender cal1 like cal2
        cal2.set(2018, Calendar.SEPTEMBER, 10);


        long millis1 = cal1.getTimeInMillis();
        long millis2 = cal2.getTimeInMillis();

        long diff = millis2 - millis1;

        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays * -1;
    }




    /**
     * get dafYomi object from locale file in asset
     * @param context Context
     * @return dafYomi object
     */
    private DafHyomi getAllDafYomi(Context context) {

        try {
            Gson gson = new Gson();
            String txt = convertStreamToString(Objects.requireNonNull(context).getAssets().open("daf_yomi_json.txt"));
            return gson.fromJson(txt, DafHyomi.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     * read the file and convert the text to string
     * @param is file location
     * @return String
     * @throws Exception e
     */
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }




    /**
     * Iteration on the array of the DafYomi object and calculator the cornet daf yomi
     * @param dafHyomi DafHyomi object
     * @param toadyDafYomiFromNewSederInt long
     * @return String
     */
    private TodayDafYomiDetailes getCorrectDaf(DafHyomi dafHyomi, long toadyDafYomiFromNewSederInt) {

        int counter = 0;


        for (int i = 0; i < dafHyomi.getSeder().size(); i++) {

            for (int j = 0; j < dafHyomi.getSeder().get(i).getMasechtot().size(); j++) {

                counter += dafHyomi.getSeder().get(i).getMasechtot().get(j).getPages();

                if (toadyDafYomiFromNewSederInt <= counter) {

                    String todayMasechet = dafHyomi.getSeder().get(i).getMasechtot().get(j).getMasechet();
                    int todayMasechetDaf = (int) (dafHyomi.getSeder().get(i).getMasechtot().get(j).getPages() - (counter - toadyDafYomiFromNewSederInt)) + 1;

                    if (todayMasechet.equals(KINIM)) {
                        todayMasechetDaf += dafHyomi.getDafYomiDetails().getKinnim();
                    }

                    if (todayMasechet.equals(TAMID)) {
                        todayMasechetDaf += dafHyomi.getDafYomiDetails().getTamid();
                    }

                    if (todayMasechet.equals(MIDOT)) {
                        todayMasechetDaf += dafHyomi.getDafYomiDetails().getMidot();
                    }

                    TodayDafYomiDetailes todayDafYomiDetailes = new TodayDafYomiDetailes();
                    todayDafYomiDetailes.setMasechetName(todayMasechet);
                    todayDafYomiDetailes.setMasechetPage(String.valueOf(todayMasechetDaf));

                    return todayDafYomiDetailes;

                }

            }

        }


        return null;
    }


}
