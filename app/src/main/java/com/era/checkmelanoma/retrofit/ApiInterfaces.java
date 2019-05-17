package com.era.checkmelanoma.retrofit;

import com.era.checkmelanoma.retrofit.models.responses.AddPatientResponse;
import com.era.checkmelanoma.retrofit.models.responses.AddResearchResponse;
import com.era.checkmelanoma.retrofit.models.responses.CommonResponse;
import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;
import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterfaces {

    // send email
    @POST("user/reg")
    Call<CommonResponse> sendEmail(@Query("mail") String mail,
                                   @Query("password") String password);

    // confirm email & register
    @POST("user/mailconfirm")
    Call<CommonResponse> confirmEmail(@Query("mail") String mail,
                                      @Query("emailCode") String code);

    // auth
    @POST("user/auth")
    Call<CommonResponse> auth(@Query("mail") String mail,
                              @Query("password") String password);

    // get patients list
    @GET("patient/getall")
    Call<PatientsResponse> getPatientsList(@Header("Authorization") String token,
                                           @Query("family") String family,
                                           @Query("name") String name,
                                           @Query("patronymic") String patronymic,
                                           @Query("page") int page,
                                           @Query("cntList") int cntList);

    // add patient
    @POST("patient/create")
    Call<AddPatientResponse> addPatient(@Header("Authorization") String token,
                                        @Query("family") String surname,
                                        @Query("name") String name,
                                        @Query("patronymic") String patronymic,
                                        @Query("sex") String sex,
                                        @Query("date_birth") Long date_of_birth);

    // add research
    @Multipart
    @POST("analysis/create")
    Call<AddResearchResponse> addResearch(@Header("Authorization") String token,
                                          @Part MultipartBody.Part file,
                                          @Part("patients_id") RequestBody patients_id,
                                          @Part("subject_study") RequestBody subject_study,
                                          @Part("charset") RequestBody charset);

    // get researches list
    @GET("analysis/getall")
    Call<ResearchesResponse> getResearches(@Header("Authorization") String token,
                                           @Query("patient_id") Long patient_id,
                                           @Query("page") int page,
                                           @Query("cntList") int cntList);

}
