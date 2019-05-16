package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.AuthContract;

public class AuthPresenterImpl implements AuthContract.Presenter, AuthContract.Interactor.OnFinishedListener {

    private AuthContract.View view;
    private AuthContract.Interactor interactor;

    public AuthPresenterImpl(AuthContract.View view, AuthContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onAuthClicked(String email, String password) {
        interactor.auth(this, email, password);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished(String token) {
        if (view != null) {
            view.onAuthSuccess(token);
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
