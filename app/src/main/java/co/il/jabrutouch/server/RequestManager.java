package co.il.jabrutouch.server;

import java.util.concurrent.TimeUnit;

import co.il.model.model.AnalyticsData;
import co.il.model.model.ChangePassword;
import co.il.model.model.ChangePasswordResponse;
import co.il.model.model.Chats;
import co.il.model.model.DonationData;
import co.il.model.model.GemaraPages;
import co.il.model.model.LessonDonationBy;
import co.il.model.model.LessonLike;
import co.il.model.model.MessageObject;
import co.il.model.model.MishnaMishnaiot;
import co.il.model.model.PagesItem;
import co.il.model.model.Payment;
import co.il.model.model.ProfileData;
import co.il.model.model.SendMail;
import co.il.model.model.SendMailResponse;
import co.il.model.model.User;
import co.il.model.model.UserDonationStatus;
import co.il.model.model.UserUpdate;
import co.il.model.model.masechet_list_model.MasechetList;
import co.il.model.model.RequestLogIn;
import co.il.model.model.RequestSendPhoneNUmber;
import co.il.model.model.RequestSignUp;
import co.il.model.model.RequestValidationCode;
import co.il.model.model.ResponseValidation;
import co.il.model.model.Result;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RequestManager {


    private static final String TOKEN = "token ";


    public static Observable<Result> sendPhoneNumber(RequestSendPhoneNUmber requestSendCode) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendPhoneNumber(requestSendCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<ResponseValidation>> sendValidateCode(RequestValidationCode requestValidationCode) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendValidateCode(requestValidationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<User>> signUp(String userId, RequestSignUp requestSignUp) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.signUp(userId, requestSignUp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<User>> login(RequestLogIn requestLogIn) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.login(requestLogIn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<User>> getUser(String token, String userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getUser(getTokenHeader(token), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result> sendAgain(RequestSendPhoneNUmber requestSendCode) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendAgain(requestSendCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<MasechetList>> getMasechtotList() {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getMasechtotList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<MishnaMishnaiot>> getMishna(String token, String mishnaId, String chapterNumber) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getMishna(getTokenHeader(token), mishnaId, chapterNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<PagesItem>> getGemaraDafYomi(String token, String masechetId, String pageNumber) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getGemaraDafYomi(getTokenHeader(token), masechetId, pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<GemaraPages>> getGemara(String token, String gemaraId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getGemara(getTokenHeader(token), gemaraId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result> sendAnalytics(String token, AnalyticsData analyticsData) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendAnalyticsData(getTokenHeader(token), analyticsData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<SendMailResponse>> sendForgotPassword(SendMail sendMail) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendForgotPassword(sendMail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<Chats>> getAllChats(String token) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getAllChats(getTokenHeader(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }


    public static Observable<Result<MessageObject>> sendMessage(String token, MessageObject messageObject) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendMessage(getTokenHeader(token), messageObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<ProfileData>> getProfileData(String token) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getProfileData(getTokenHeader(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }


    public static Observable<Result<User>> updateUser(String token, UserUpdate userUpdate, String userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.updateUser(getTokenHeader(token), userId, userUpdate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<ChangePasswordResponse>> changePassword(String token, ChangePassword changePassword, String userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.changePassword(getTokenHeader(token), userId, changePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result> deleteUser(String token, String userId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.deleteUser(getTokenHeader(token), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }


    public static Observable<Result<DonationData>> getDonationData(String token) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getDonationData(getTokenHeader(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }


    public static Observable<Result<UserDonationStatus>> getUserDonationStatus(String token) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getUserDonationStatus(getTokenHeader(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(100, TimeUnit.SECONDS);
    }


    public static Observable<Result<Payment>> sendUserPayment(String token, Payment paymentObject) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendUserPayment(getTokenHeader(token), paymentObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }



    public static Observable<Result<LessonDonationBy>> getLessonDonations(String token) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.getLessonDonations(getTokenHeader(token))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }




    public static Observable<Result> sendLikeLesson(String token, LessonLike lessonLike) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        return service.sendLikeForDonation(getTokenHeader(token), lessonLike)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }




    private static String getTokenHeader(String token) {

        return TOKEN + token;
    }


}
