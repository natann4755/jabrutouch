package co.il.jabrutouch;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import org.litepal.LitePal;
import co.il.jabrutouch.data_base_manager.DataBaseManager;
import io.fabric.sdk.android.Fabric;



public class MainApplication extends Application {


    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        init();
        createMessageTables();
    }

    private void init() {

        LitePal.initialize(this);

    }


    private void createMessageTables() {

        DataBaseManager dataBaseManager = new DataBaseManager();
        dataBaseManager.createGemaraTables(this);
        dataBaseManager.createMishnaTables(this);

        dataBaseManager.createMessagesTables(this);
    }
}
