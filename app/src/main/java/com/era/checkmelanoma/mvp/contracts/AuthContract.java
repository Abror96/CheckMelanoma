package com.era.checkmelanoma.mvp.contracts;

public interface AuthContract {

    interface View {

        void showSnackbar(String message);

        void onAuthSuccess(String token);

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void onDestroy();

        void onAuthClicked(String email, String password);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished(String token);

            void onFailure(String message);

        }

        void auth(OnFinishedListener onFinishedListener, String email, String password);

    }

}
