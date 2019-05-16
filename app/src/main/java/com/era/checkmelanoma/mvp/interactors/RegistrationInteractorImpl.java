package com.era.checkmelanoma.mvp.interactors;

import android.util.Log;

import com.era.checkmelanoma.mvp.contracts.RegistrationContract;
import com.era.checkmelanoma.retrofit.ApiClient;
import com.era.checkmelanoma.retrofit.ApiInterfaces;
import com.era.checkmelanoma.retrofit.models.responses.CommonResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationInteractorImpl implements RegistrationContract.Interactor {

    private ApiInterfaces apiService =
            ApiClient.getInstance().create(ApiInterfaces.class);
    private String TAG = "LOGGERR Reg";

    @Override
    public void sendEmail(final OnFinishedListener onFinishedListener, String email, String password) {

        Call<CommonResponse> sendEmailCall = apiService.sendEmail(
                email,
                password
        );

        sendEmailCall.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    CommonResponse commonResponse = response.body();
                    if (commonResponse.getStatus().equals("OK")) {
                        onFinishedListener.onFinished();
                        Log.d(TAG, "sendEmail: " + response.body().getStatus());
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
