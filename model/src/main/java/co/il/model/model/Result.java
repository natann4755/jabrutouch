
package co.il.model.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result<T> {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("errors")
    @Expose
    private String errors;
    @SerializedName("success")
    @Expose
    private boolean success;

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", data=" + data +
                ", errorCode=" + errorCode +
                ", errors='" + errors + '\'' +
                ", success=" + success +
                '}';
    }

    /**
     * @return The status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return The data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(T data) {
        this.data = data;
    }


    public int getErrorCode() {

        return errorCode;
    }


    public void setErrorCode(int errorCode) {

        this.errorCode = errorCode;
    }


    public String getErrors() {

        return errors;
    }


    public void setErrors(String errors) {

        this.errors = errors;
    }

    public boolean isSuccess() {

        return status == 1;

    }

}
