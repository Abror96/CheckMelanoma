package com.era.checkmelanoma.mvp.presenters;

import android.util.Log;

import com.era.checkmelanoma.mvp.contracts.AddResearchContract;
import com.era.checkmelanoma.retrofit.models.responses.AddResearchResponse;

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
        Log.d("LOGGERR", "onAddResearchClicked: " + subject_study);
        RequestBody patientIdRb = RequestBody.create(MediaType.parse("text/plain"), patient_id);
        RequestBody subjectStudyRb = RequestBody.create(MediaType.parse("text/plain"), subject_study);

        interactor.addResearchClicked(this, token, file, patientIdRb, subjectStudyRb);
        if (view != null) view.showProgress();
    }

    @Override
    public void onFinished(AddResearchResponse.Object research) {
        if (view != null) {
            view.onAddResearchSuccess(research);
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
