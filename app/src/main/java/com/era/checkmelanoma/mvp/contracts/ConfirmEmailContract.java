package com.era.checkmelanoma.mvp.contracts;

public interface ConfirmEmailContract {

    interface View {

        void showSnackbar(String message);

        void onConfirmEmailSuccess();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void onDestroy();

        void onConfirmEmailClicked(String email, String code);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished();

            void onFailure(String message);

        }

        void confirmEmail(OnFinishedListener onFinishedListener, String email, String code);

    }

}
