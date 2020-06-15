package co.il.s3.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import co.il.s3.interfaces.DownloadListener;
import co.il.s3.interfaces.UploadListener;

import static co.il.s3.utils.Config.PULL_ID;


public class S3Helper {

    public static final String PAGES_FILE = "pages/";
    private static final String S3_JABRUTOUCH = "https://jabrutouch-cms-media.s3-us-west-2.amazonaws.com/";
    private final Context mContext;
    private CognitoCachingCredentialsProvider credentialsProvider;
    private TransferUtility transferUtility;
    private TransferState mState;
    private Integer idOfTransfer;
    private AmazonS3Client mS3;
    public long bitesCorrent;

    /**
     * Constructs a S3Helper and initializes fields with the given
     * arguments.
     *
     * @param context Get the context for the application.
     * @throws
     */
    public S3Helper(Context context) {

        this.mContext = context;

        init();

    }


    /**
     * @throws   CognitoCachingCredentialsProvider is null
     */
    private void init() {


        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                mContext,
                PULL_ID, // Identity pool ID
                Regions.US_EAST_1 // Region
        );

//        AWSCredentials credentials = new BasicAWSCredentials(Config.AWS_ACCESS_KEY, Config.AWS_SECRET_KEY);
        ClientConfiguration config = new ClientConfiguration();
        config.setSocketTimeout(50000000);
        config.setConnectionTimeout(50000000);
        config.setMaxConnections(500);
        config.setMaxErrorRetry(10);
        mS3 = new AmazonS3Client(credentialsProvider, config);
        mS3.setRegion(Region.getRegion(Config.REGION));
        transferUtility = new TransferUtility(mS3, mContext);


    }


    /**
     * download file from s3
     * @param fileKey String
     * @param downloadListener
     * @param
     */
    public void downloadFile(final Context context, final String fileKey, String pageFileKey, String fileType, final DownloadListener downloadListener) {

        final String pathName = Config.getPathName(context) + fileType + getFIleNameFromFileKEy(fileKey);
        final String pagePathName = Config.getPathName(context) + PAGES_FILE + getFIleNameFromFileKEy(pageFileKey);

        final long totalBytes = 0;
        TransferObserver downloadObserver = transferUtility.download(
                                            Config.BUCKET_NAME,
                                            fileKey,
                                            new File(pathName));


        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {

                    downloadListener.onDownloadFinish(id, state.name(), pathName, pagePathName);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent,long bytesTotal) {


                if (bytesTotal == 0){

                    bitesCorrent = bytesCurrent;

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            URL url = null;
                            try {
                                url = new URL(S3_JABRUTOUCH  + fileKey);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            URLConnection conection = null;
                            try {
                                conection = url.openConnection();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // getting file length
                            int lengthOfFile = conection.getContentLength();

                            float percentDonef = ((float) bitesCorrent / (float) lengthOfFile) * 100;
                            final int percentDone = (int) percentDonef;

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    downloadListener.onProgressChanged(percentDone);
                                }
                            });
                        }
                    });
                    thread.start();

                }else {


                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;

                    downloadListener.onProgressChanged(percentDone);
                }


            }

            @Override
            public void onError(int id, Exception ex) {

                Toast.makeText(context, "Error downloading file " + fileKey, Toast.LENGTH_LONG).show();
                downloadListener.onDownloadError();

            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == downloadObserver.getState()) {
            // Handle a completed upload.


        }


    }



    /**
     * download file from s3
     * @param fileKey String

     */
    public void downloadFilePagesWithoutListener(String fileKey, String fileType, Context context) {


        TransferObserver downloadObserver =
                transferUtility.download(
                        Config.BUCKET_NAME,
                        fileKey,
                        new File(Config.getPathName(context) + fileType + getFIleNameFromFileKEy(fileKey)));



        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;


            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors


            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == downloadObserver.getState()) {
            // Handle a completed upload.


        }


    }




    /**
     * Upload the object and to track the progress of state.
     *
     * @param S3SubFolder  The sub folder to upload.
     * @param S3ObjectPath The path for the uploaded object, can be null.
     * @param fileLocation The file where the data to upload exists.
     */

    public void upload(final String S3SubFolder, final String S3ObjectPath, final File fileLocation, final UploadListener uploadListener) {

        String S3ObjectPathRandom = null;
        TransferObserver observer;

        if (S3ObjectPath == null) {
            S3ObjectPathRandom = System.currentTimeMillis() + fileLocation.getAbsolutePath().substring(fileLocation.getAbsolutePath().lastIndexOf("."));
            observer = transferUtility.upload(Config.BUCKET_NAME + S3SubFolder, S3ObjectPathRandom, fileLocation);
        } else {
            observer = transferUtility.upload(Config.BUCKET_NAME + S3SubFolder, S3ObjectPath, fileLocation);

        }

        final String fS3ObjectPathRandom = S3ObjectPathRandom;
        observer.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {

                idOfTransfer = id;

                mState = state;
                if (S3ObjectPath == null)
                    checkState(id, fileLocation, S3SubFolder, fS3ObjectPathRandom, uploadListener);
                else
                    checkState(id, fileLocation, S3SubFolder, S3ObjectPath, uploadListener);
            }


            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                if (idOfTransfer != null) {

                    idOfTransfer = id;
                }

                try {

                    uploadListener.onProgressChanged(id, bytesCurrent, bytesTotal);
                } catch (IllegalStateException ignored) {
                }


            }

            @Override
            public void onError(int id, Exception ex) {

                if (idOfTransfer != null) {

                    idOfTransfer = id;
                }

                uploadListener.onUploadError(id, fileLocation, ex);


            }
        });


    }



    /**
     * Check the state and call the correct method.
     *  @param id           The transfer id of the transfer to be observed.
     * @param fileLocation The file where the data to upload exists.
     * @param s3SubFolder  The folders location in S3 to this object.
     * @param s3ObjectPath THe object name in S3.
     * @param uploadListener
     */
    private void checkState(int id, File fileLocation, String s3SubFolder, String s3ObjectPath, UploadListener uploadListener) {

        if (uploadListener != null) {

            switch (mState) {

                case IN_PROGRESS:
                    uploadListener.onUploadStart(id, fileLocation);
                    break;

                case WAITING:
                case RESUMED_WAITING:
                case WAITING_FOR_NETWORK:
                    uploadListener.onUploadWaiting(id, mState, fileLocation);
                    break;

                case PAUSED:
                case CANCELED:
                    uploadListener.onUploadReceivedStop(id, mState, fileLocation);
                    break;

                case COMPLETED:

                    idOfTransfer = null;

                    uploadListener.onUploadFinish(id, s3SubFolder + "/" + s3ObjectPath);

                    break;

            }
        } else throw new NullPointerException("UploadListener is null");
    }





    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);


    }


}
