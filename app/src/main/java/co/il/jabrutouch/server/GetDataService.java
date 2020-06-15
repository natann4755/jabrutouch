package co.il.jabrutouch.server;

import co.il.model.model.AnalyticsData;
import co.il.model.model.ChangePassword;
import co.il.model.model.ChangePasswordResponse;
import co.il.model.model.ChatObject;
import co.il.model.model.ChatTest;
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
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetDataService {

    @POST("/api/send_code")
    Observable<Result> sendPhoneNumber(@Body RequestSendPhoneNUmber requestSendCode);

    @POST("/api/validate_code")
    Observable<Result<ResponseValidation>> sendValidateCode(@Body RequestValidationCode requestValidationCode);

    @PATCH("/api/sing_up/{userId}/")
    Observable<Result<User>> signUp(@Path ("userId")String userId, @Body RequestSignUp requestSignUp);

    @POST("/api/login")
    Observable<Result<User>> login(@Body RequestLogIn requestLogIn);

    @GET("/api/users/{userId}")
    Observable<Result<User>> getUser(@Header("Authorization") String token, @Path ("userId")String userId);

    @POST("/api/send_again")
    Observable<Result> sendAgain(@Body RequestSendPhoneNUmber requestSendCode);

    @GET("/api/masechtot_list")
    Observable<Result<MasechetList>> getMasechtotList();

    @GET("/api/gemara/masechet/{gemaraId}")
    Observable<Result<GemaraPages>> getGemara(@Header("Authorization") String token, @Path("gemaraId") String gemaraId);

    @GET("/api/mishna/masechet/{masechetId}/chapter/{chapterNumber}")
    Observable<Result<MishnaMishnaiot>> getMishna(@Header("Authorization") String token, @Path("masechetId") String masechetId, @Path("chapterNumber") String chapterNumber);

    @GET("/api/gemara/masechet/{masechetId}/page/{pageNumber}")
    Observable<Result<PagesItem>> getGemaraDafYomi(@Header("Authorization") String token, @Path("masechetId") String masechetId, @Path("pageNumber") String pageNumber);

    @POST("/api/user/analytics/")
    Observable<Result> sendAnalyticsData(@Header("Authorization") String token, @Body AnalyticsData analyticsData);

    @POST("/api/reset_password")
    Observable<Result<SendMailResponse>> sendForgotPassword(@Body SendMail sendMail);

    @GET("/api/messages")
    Observable<Result<Chats>> getAllChats(@Header("Authorization") String token);

    @POST("/api/messages")
    Observable<Result<MessageObject>> sendMessage(@Header("Authorization") String token, @Body MessageObject messageObject);

    @GET("/api/profile_data")
    Observable<Result<ProfileData>> getProfileData(@Header("Authorization") String token);

    @PUT("/api/users/{userId}")
    Observable<Result<User>> updateUser(@Header("Authorization") String token, @Path ("userId")String userId, @Body UserUpdate userUpdate);

    @POST("/api/users/{userId}/change_password/")
    Observable<Result<ChangePasswordResponse>> changePassword(@Header("Authorization") String token, @Path ("userId")String userId, @Body ChangePassword changePassword);

    @DELETE("/api/users/{userId}")
    Observable<Result> deleteUser(@Header("Authorization") String token, @Path ("userId")String userId);

    @GET("/api/donation_data")
    Observable<Result<DonationData>> getDonationData(@Header("Authorization") String token);


    @GET("/api/user/donation")
    Observable<Result<UserDonationStatus>> getUserDonationStatus(@Header("Authorization") String token);

    @POST("/api/user_payment")
    Observable<Result<Payment>> sendUserPayment(@Header("Authorization") String token, @Body Payment payment);

    @GET("/api/lesson_donations")
    Observable<Result<LessonDonationBy>> getLessonDonations(@Header("Authorization") String token);

    @POST("/api/like")
    Observable<Result> sendLikeForDonation(@Header("Authorization") String token, @Body LessonLike lessonLike);

}
