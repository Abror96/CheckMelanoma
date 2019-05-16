package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.RegistrationContract;

public class RegistrationPresenterImpl implements RegistrationContract.Presenter, RegistrationContract.Interactor.OnFinishedListener {

    private RegistrationContract.View view;
    private RegistrationContract.Interactor interactor;

    public RegistrationPresenterImpl(RegistrationContract.View view, RegistrationContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onSendEmailClicked(String email, String password) {
        interactor.sendEmail(this, email, password);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished() {
        if (view != null) {
            view.onSendEmailSuccess();
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
