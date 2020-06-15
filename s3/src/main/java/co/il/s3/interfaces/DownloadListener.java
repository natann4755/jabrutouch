package co.il.s3.interfaces;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;

/**
 * Created by droiter on 12/27/2016.
 *
 * A DownloadListener used to receive notification.
 */

public interface DownloadListener {

    /**
     * Called When Downloading begins or continues after paused
     *
     * @param id The transfer id of the transfer to be observed.
     * @param fileLocation The file where the data to Download exists.
     */
    void onDownloadStart(int id, File fileLocation);

    /**
     * Called when more bytes are transferred.
     *
     * @param id The transfer id of the transfer to be observed.
//     * @param bytesCurrent Bytes transferred currently.
//     * @param bytesTotal The total bytes to be transferred.
     */
    void onProgressChanged(int percentDone);

    /**
     * Called when the Download is currently on hold and  waiting for continue automatic.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param state The new state of the transfer.
     * @param fileLocation The file where the data to Download exists.
     */
    void onDownloadWaiting(int id, TransferState state, File fileLocation);

    /**
     * Called when the Download is paused or canceled.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param state The new state of the transfer.
     * @param fileLocation The file where the data to Download exists.
     */
    void onDownloadReceivedStop(int id, TransferState state, File fileLocation);

    /**
     * Called when the Download is completed.
     *  @param id The transfer id of the transfer to be observed.
     * @param link The new URL of the Download object in S3.
     * @param pathName
     */
    void onDownloadFinish(int id, String link, String pathName, String pagePathName);

    /**
     * Called when the Download has failed.
     *

     */
    void onDownloadError();

}