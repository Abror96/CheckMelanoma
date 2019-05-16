package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.MainContract;
import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;

import java.util.ArrayList;

public class MainPresenterImpl implements MainContract.Presenter, MainContract.Interactor.OnFinishedListener {

    private MainContract.View view;
    private MainContract.Interactor interactor;

    public MainPresenterImpl(MainContract.View view, MainContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onGetPatientList(String token, String family, String name, String patronymic, int page, int cntList) {
        interactor.getPatientsList(this, token, family, name, patronymic, page, cntList);
        if (view != null) view.showProgress();
    }

    @Override
    public void onAddPatientClicked(String token, String family, String name, String patronymic, String sex, Long date_birth) {
        interactor.addPatient(this, token, family, name, patronymic, sex, date_birth);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished(ArrayList<PatientsResponse.Object> patientsList) {
        if (view != null) {
            view.onGetPatientListSuccess(patientsList);
            view.hideProgress();
        }
    }

    @Override
    public void onFinished() {
        if (view != null) {
            view.onAddPatientSuccess();
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
