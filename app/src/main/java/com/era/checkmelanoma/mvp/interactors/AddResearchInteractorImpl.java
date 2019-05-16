package com.era.checkmelanoma.mvp.interactors;

import android.util.Log;

import com.era.checkmelanoma.mvp.contracts.AddResearchContract;
import com.era.checkmelanoma.retrofit.ApiClient;
import com.era.checkmelanoma.retrofit.ApiInterfaces;
import com.era.checkmelanoma.retrofit.models.responses.CommonResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddResearchInteractorImpl implements AddResearchContract.Interactor {

    private ApiInterfaces apiService =
            ApiClient.getInstance().create(ApiInterfaces.class);
    private String TAG = "LOGGERR AddResearch";

    @Override
    public void addResearchClicked(final OnFinishedListener onFinishedListener, String token,
                                   MultipartBody.Part file, RequestBody patient_id, RequestBody subject_study) {

        Call<CommonResponse> sendEmailCall = apiService.addResearch(
                "Basic " + token,
                file,
                patient_id,
                subject_study
        );

        sendEmailCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse.getStatus().equals("OK")) {
                        onFinishedListener.onFinished();
                        Log.d(TAG, "auth: " + response.body().getStatus());
                    } else if (commonResponse.getStatus().toLowerCase().equals("error")) {
                        onFinishedListener.onFailure(commonResponse.getError());
                    }
                } else onFinishedListener.onFailure("Произошла ошибка сервера "+ statusCode +". Попытайтесь снова");
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure("Произошла ошибка сервера. Попытайтесь снова");
            }
        });
    }
}
