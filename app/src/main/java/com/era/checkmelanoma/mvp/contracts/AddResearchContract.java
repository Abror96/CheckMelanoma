package com.era.checkmelanoma.mvp.contracts;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface AddResearchContract {

    interface View {

        void showSnackbar(String message);

        void onAddResearchSuccess();

    }

    interface Presenter {

        void onDestroy();

        void onAddResearchClicked(String token, MultipartBody.Part file, String patient_id, String subject_study);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished();

            void onFailure(String message);

        }

        void addResearchClicked(OnFinishedListener onFinishedListener, String token,
                                MultipartBody.Part file, RequestBody patient_id,
                                RequestBody subject_study);

    }

}
