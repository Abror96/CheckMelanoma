package com.era.checkmelanoma.mvp.interactors;

import android.util.Log;

import com.era.checkmelanoma.mvp.contracts.PatientCardContract;
import com.era.checkmelanoma.retrofit.ApiClient;
import com.era.checkmelanoma.retrofit.ApiInterfaces;
import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientCardInteractorsImpl implements PatientCardContract.Interactor {

    private ApiInterfaces apiService =
            ApiClient.getInstance().create(ApiInterfaces.class);
    private String TAG = "LOGGERR PatientCard";

    @Override
    public void getResearchesList(OnFinishedListener onFinishedListener, Long patient_id, int page, int cntList, String token) {

        Call<ResearchesResponse> addResearch = apiService.getResearches(
                "Basic " + token,
                patient_id,
                page,
                cntList
        );

        addResearch.enqueue(new Callback<ResearchesResponse>() {
            @Override
            public void onResponse(Call<ResearchesResponse> call, Response<ResearchesResponse> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    ResearchesResponse researchesResponse = response.body();
                    if (researchesResponse.getStatus().equals("OK")) {
                        onFinishedListener.onFinished((ArrayList<ResearchesResponse.Object>) researchesResponse.getObject());
                        Log.d(TAG, "getResearchesList: " + response.body().getStatus());
                    } else if (researchesResponse.getStatus().toLowerCase().equals("error")) {
                        onFinishedListener.onFailure(researchesResponse.getError());
                    }
                } else onFinishedListener.onFailure("Произошла ошибка сервера "+ statusCode +". Попытайтесь снова");
            }

            @Override
            public void onFailure(Call<ResearchesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure("Произошла ошибка сервера. Попытайтесь снова");
            }
        });
    }
}
