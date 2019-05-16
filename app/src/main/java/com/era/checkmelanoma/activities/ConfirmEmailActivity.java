package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityConfirmEmailBinding;
import com.era.checkmelanoma.mvp.contracts.ConfirmEmailContract;
import com.era.checkmelanoma.mvp.interactors.ConfirmEmailInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.ConfirmEmailPresenterImpl;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class ConfirmEmailActivity extends AppCompatActivity implements ConfirmEmailContract.View {

    private ActivityConfirmEmailBinding binding;
    private ProgressDialog progressDialog;
    private ConfirmEmailContract.Presenter presenter;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm_email);

        progressDialog = initProgressDialog(this, "Идёт загрузка");
        presenter = new ConfirmEmailPresenterImpl(this, new ConfirmEmailInteractorImpl());

        if (getIntent().getStringExtra("email") != null) email = getIntent().getStringExtra("email");

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.etCode.getText().toString().trim();
                if (!code.isEmpty()) {
                    presenter.onConfirmEmailClicked(email, code);
                } else Snackbar.make(binding.mainView, "Введите код", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConfirmEmailSuccess() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
