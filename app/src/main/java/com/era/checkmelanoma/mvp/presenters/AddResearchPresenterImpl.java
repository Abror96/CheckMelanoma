package com.era.checkmelanoma.mvp.presenters;

import com.era.checkmelanoma.mvp.contracts.AddResearchContract;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddResearchPresenterImpl implements AddResearchContract.Presenter, AddResearchContract.Interactor.OnFinishedListener {

    private AddResearchContract.View view;
    private AddResearchContract.Interactor interactor;

    public AddResearchPresenterImpl(AddResearchContract.View view, AddResearchContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void onAddResearchClicked(String token, MultipartBody.Part file, String patient_id, String subject_study) {
        RequestBody patientIdRb = RequestBody.create(MediaType.parse("multipart/form-data"), patient_id);
        RequestBody subjectStudyRb = RequestBody.create(MediaType.parse("multipart/form-data"), subject_study);

        interactor.addResearchClicked(this, token, file, patientIdRb, subjectStudyRb);
    }

    @Override
    public void onFinished() {
        if (view != null) {
            view.onAddResearchSuccess();
        }
    }

    @Override
    public void onFailure(String message) {
        if (view != null) {
            view.showSnackbar(message);
        }
    }

}
