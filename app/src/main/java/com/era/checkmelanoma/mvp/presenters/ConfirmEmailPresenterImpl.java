package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.ConfirmEmailContract;

public class ConfirmEmailPresenterImpl implements ConfirmEmailContract.Presenter, ConfirmEmailContract.Interactor.OnFinishedListener {

    private ConfirmEmailContract.View view;
    private ConfirmEmailContract.Interactor interactor;

    public ConfirmEmailPresenterImpl(ConfirmEmailContract.View view, ConfirmEmailContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onConfirmEmailClicked(String email, String code) {
        interactor.confirmEmail(this, email, code);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished() {
        if (view != null) {
            view.onConfirmEmailSuccess();
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
