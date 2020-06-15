package co.il.vimeo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.vimeo.networking.Configuration;
import com.vimeo.networking.GsonDeserializer;
import com.vimeo.networking.VimeoClient;
import com.vimeo.networking.callbacks.AuthCallback;
import com.vimeo.networking.callbacks.ModelCallback;
import com.vimeo.networking.model.User;
import com.vimeo.networking.model.Video;
import com.vimeo.networking.model.error.VimeoError;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class VimeoHelper {




    private final Context mContext;

    public VimeoHelper(Context context) {

        this.mContext = context;

        init();
        authenticateWithClientCredentials();


    }





    private void init() {

        /**
         * @param clientId      The client id provided to you from <a href="https://developer.vimeo.com/apps/">the developer console</a>
         * @param clientSecret  The client secret provided to you from <a href="https://developer.vimeo.com/apps/">the developer console</a>
         * @param scope         Space separated list of <a href="https://developer.vimeo.com/api/authentication#scopes">scopes</a>
         *                      <p/>
         *                      Example: "private public create"
         * @param accountStore  (Optional, Recommended) An implementation that can be used to interface with Androids <a href="http://developer.android.com/reference/android/accounts/AccountManager.html">Account Manager</a>
         */
        Configuration.Builder configBuilder = new Configuration.Builder("51af0b08631e881b571b09b930b75450289d5c0f",
                "CfRix2rnsOGZXApBicVA8N5ZEWTUC+nN0CGOzTqulRzst8/T3ptLcerRyBisn4Bzuc6Y761yYQ2wkNGAkTYLMw6lK8FI8Td7Rle3ctZpo484zv6yxp1bnWX5gqCq20sf",
                "private public create")
                .setCacheDirectory(mContext.getCacheDir())
                .setGsonDeserializer(new GsonDeserializer());
        VimeoClient.initialize(configBuilder.build());
        // VimeoClient is now ready for use

    }





    private void authenticateWithClientCredentials() {

        VimeoClient.getInstance().authorizeWithClientCredentialsGrant(new AuthCallback() {
            @Override
            public void success() {
                String accessToken = VimeoClient.getInstance().getVimeoAccount().getAccessToken();
                Log.d("Client success ", accessToken);
                fetchNetworkContent("https://api.vimeo.com/videos/308253521");

            }

            @Override
            public void failure(VimeoError error) {
                String errorMessage = error.getDeveloperMessage();
                Log.d("Client failure ", errorMessage);
            }
        });
    }





    private void fetchNetworkContent(String uri) {

        VimeoClient.getInstance().fetchNetworkContent(uri, new ModelCallback<Video>(Video.class) {
            @Override
            public void success(Video video) {
                // use the video
                Log.d("vimeo: ", "success");
                downloadVideo(video.link);

            }

            @Override
            public void failure(VimeoError error) {
                // voice the error
                Log.d("vimeo: ", "failure");

            }
        });


    }





    private void downloadVideo(final String link) {

        Thread thread = new Thread() {
            @Override
            public void run() {

                InputStream in = null;
                BufferedInputStream inStream = null;
                FileOutputStream out = null;
                HttpURLConnection connection = null;

                try {
                    File FileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES);

                    URL url = new URL(link);

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    connection.connect();

                    //Code stops here with vimeo video but not with popeye video.


                    out = new FileOutputStream(new File(FileDir, "aa.mp4"));
                    in = connection.getInputStream();
                    inStream = new BufferedInputStream(in, 1024 * 5);
                    byte[] buffer = new byte[1024 * 5];

                    int len;
                    while ((len = inStream.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //catch and finally { flush, close and disconnection}

            }
        };

        thread.start();


    }


}
