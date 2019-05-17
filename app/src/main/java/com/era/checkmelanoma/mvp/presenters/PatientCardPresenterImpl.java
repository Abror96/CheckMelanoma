package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.PatientCardContract;
import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;

import java.util.ArrayList;

public class PatientCardPresenterImpl implements PatientCardContract.Presenter, PatientCardContract.Interactor.OnFinishedListener {

    private PatientCardContract.View view;
    private PatientCardContract.Interactor interactor;

    public PatientCardPresenterImpl(PatientCardContract.View view, PatientCardContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onGetResearchesListCalled(Long patient_id, int page, int cntList, String token) {
        interactor.getResearchesList(this, patient_id, page, cntList, token);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished(ArrayList<ResearchesResponse.Object> researchesList) {
        if (view != null) {
            view.onGetResearchesListSuccess(researchesList);
            view.hideProgress();
        }
    }

    @Override
    public void onFailure(String message) {
        if (view != null) {
            view.showSnackbar(message);
            view.hideProgress();
        }
    }
}
