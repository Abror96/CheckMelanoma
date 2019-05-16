package com.era.checkmelanoma.mvp.contracts;

import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;

import java.util.ArrayList;

public interface MainContract {

    interface View {

        void showSnackbar(String message);

        void onGetPatientListSuccess(ArrayList<PatientsResponse.Object> patientsList);

        void onAddPatientSuccess();

        void showProgress();

        void hideProgress();

    }

    interface Presenter {

        void onDestroy();

        void onGetPatientList(String token, String family, String name, String patronymic, int page, int cntList);

        void onAddPatientClicked(String token, String family, String name, String patronymic,
                                 String sex, Long date_birth);

    }

    interface Interactor {

        interface OnFinishedListener {

            void onFinished(ArrayList<PatientsResponse.Object> patientsList);

            void onFinished();

            void onFailure(String message);

        }

        void getPatientsList(OnFinishedListener onFinishedListener, String token, String family,
                             String name, String patronymic, int page, int cntList);

        void addPatient(OnFinishedListener onFinishedListener, String token, String family,
                        String name, String patronymic, String sex, Long date_birth);

    }

}
