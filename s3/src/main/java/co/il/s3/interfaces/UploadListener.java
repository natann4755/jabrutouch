package co.il.s3.interfaces;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;

import java.io.File;

/**
 * Created by droiter on 12/27/2016.
 *
 * A UploadListener used to receive notification.
 */

public interface UploadListener {

    /**
     * Called When uploading begins or continues after paused
     *
     * @param id The transfer id of the transfer to be observed.
     * @param fileLocation The file where the data to upload exists.
     */
    void onUploadStart(int id, File fileLocation);

    /**
     * Called when more bytes are transferred.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param bytesCurrent Bytes transferred currently.
     * @param bytesTotal The total bytes to be transferred.
     */
    void onProgressChanged(int id, long bytesCurrent, long bytesTotal);

    /**
     * Called when the upload is currently on hold and  waiting for continue automatic.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param state The new state of the transfer.
     * @param fileLocation The file where the data to upload exists.
     */
    void onUploadWaiting(int id, TransferState state, File fileLocation);

    /**
     * Called when the upload is paused or canceled.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param state The new state of the transfer.
     * @param fileLocation The file where the data to upload exists.
     */
    void onUploadReceivedStop(int id, TransferState state, File fileLocation);

    /**
     * Called when the upload is completed.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param link The new URL of the upload object in S3.
     */
    void onUploadFinish(int id, String link);

    /**
     * Called when the upload has failed.
     *
     * @param id The transfer id of the transfer to be observed.
     * @param fileLocation The file where the data to upload exists.
     * @param ex The exception.
     */
    void onUploadError(int id, File fileLocation, Exception ex);

}