package com.era.checkmelanoma.mvp.contracts;

import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;

import java.util.ArrayList;

public interface PatientCardContract {

    interface View {

        void showSnackbar(String message);

        void onGetResearchesListSuccess(ArrayList<ResearchesResponse.Object> researchesList);

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void onDestroy();

        void onGetResearchesListCalled(Long patient_id, int page, int cntList, String token);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished(ArrayList<ResearchesResponse.Object> researchesList);

            void onFailure(String message);

        }

        void getResearchesList(OnFinishedListener onFinishedListener, Long patient_id, int page, int cntList, String token);

    }

}
