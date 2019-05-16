package com.era.checkmelanoma.mvp.interactors;

import android.util.Log;

import com.era.checkmelanoma.mvp.contracts.MainContract;
import com.era.checkmelanoma.retrofit.ApiClient;
import com.era.checkmelanoma.retrofit.ApiInterfaces;
import com.era.checkmelanoma.retrofit.models.responses.AddPatientResponse;
import com.era.checkmelanoma.retrofit.models.responses.CommonResponse;
import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainInteractorImpl implements MainContract.Interactor {

    private ApiInterfaces apiService =
            ApiClient.getInstance().create(ApiInterfaces.class);
    private String TAG = "LOGGERR MainActivity";

    @Override
    public void getPatientsList(final OnFinishedListener onFinishedListener, String token,
                                String family, String name, String patronymic, int page, int cntList) {
        Call<PatientsResponse> patientsListCall = apiService.getPatientsList(
                "Basic " + token,
                family,
                name,
                patronymic,
                page,
                cntList
        );

        patientsListCall.enqueue(new Callback<PatientsResponse>() {
            @Override
            public void onResponse(Call<PatientsResponse> call, Response<PatientsResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    PatientsResponse patientsResponse = response.body();
                    if (patientsResponse.getStatus().equals("OK")) {
                        onFinishedListener.onFinished((ArrayList<PatientsResponse.Object>) patientsResponse.getObject());
                        Log.d(TAG, "confirmEmail: " + response.body().getStatus());
                    } else if (patientsResponse.getStatus().toLowerCase().equals("error")) {
                        onFinishedListener.onFailure(patientsResponse.getError());
                    }
                } else onFinishedListener.onFailure("Произошла ошибка сервера "+ statusCode +". Попытайтесь снова");
            }

            @Override
            public void onFailure(Call<PatientsResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure("Произошла ошибка сервера. Попытайтесь снова");
            }
        });
    }

    @Override
    public void addPatient(final OnFinishedListener onFinishedListener, String token, String family,
                           String name, String patronymic, String sex, Long date_birth) {
        Call<AddPatientResponse> addPatientResponseCall = apiService.addPatient(
                "Basic " + token,
                family,
                name,
                patronymic,
                sex,
                date_birth
        );

        addPatientResponseCall.enqueue(new Callback<AddPatientResponse>() {
            @Override
            public void onResponse(Call<AddPatientResponse> call, Response<AddPatientResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    AddPatientResponse addPatientResponse = response.body();
                    if (addPatientResponse.getStatus().equals("OK")) {
                        onFinishedListener.onFinished();
                        Log.d(TAG, "addPatient: " + response.body().getStatus());
                    } else if (addPatientResponse.getStatus().toLowerCase().equals("error")) {
                        onFinishedListener.onFailure(addPatientResponse.getError());
                    }
                } else onFinishedListener.onFailure("Произошла ошибка сервера "+ statusCode +". Попытайтесь снова");
            }

            @Override
            public void onFailure(Call<AddPatientResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure("Произошла ошибка сервера. Попытайтесь снова");
            }
        });
    }
}
