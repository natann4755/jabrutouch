package co.il.jabrutouch.ui.main.message_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.ErrorResponse;
import co.il.model.model.MessageObject;
import co.il.model.model.Result;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;




public class WifiReceiver extends BroadcastReceiver {


    private Context context;



    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected()) {

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = Objects.requireNonNull(wifiManager).getConnectionInfo();
            String ssid = wifiInfo.getSSID();


            sendMessagesFromLocalStorage();
        }
    }





    private void sendMessagesFromLocalStorage() {

        Gson gson = new Gson();

        Type type = new TypeToken<List<MessageObject>>() {
        }.getType();
        List<MessageObject> recentMessages = gson.fromJson(UserManager.getMessageForOffline(context), type);

        if (recentMessages != null && recentMessages.size() > 0) {

            Toast.makeText(context, context.getResources().getString(R.string.send_offline_for_messages), Toast.LENGTH_LONG).show();

            for (int i = 0; i < recentMessages.size(); i++) {


                RequestManager.sendMessage(UserManager.getToken(context), recentMessages.get(i)).subscribe(new Observer<Result<MessageObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<MessageObject> messagesObject2Result) {

                    }

                    @Override
                    public void onError(Throwable e) {

                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(context, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }
        }

        UserManager.setMessageForOffline(null, context);

    }
}
