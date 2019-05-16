package com.era.checkmelanoma.mvp.contracts;

public interface RegistrationContract {

    interface View {

        void showSnackbar(String message);

        void onSendEmailSuccess();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void onDestroy();

        void onSendEmailClicked(String email, String password);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished();

            void onFailure(String message);

        }

        void sendEmail(OnFinishedListener onFinishedListener, String email, String password);

    }

}
