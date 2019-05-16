package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityRegistrationBinding;
import com.era.checkmelanoma.mvp.contracts.RegistrationContract;
import com.era.checkmelanoma.mvp.interactors.RegistrationInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.RegistrationPresenterImpl;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

    private ActivityRegistrationBinding binding;
    private ProgressDialog progressDialog;
    private RegistrationContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        progressDialog = initProgressDialog(this, "Идёт загрузка");
        presenter = new RegistrationPresenterImpl(this, new RegistrationInteractorImpl());

        binding.sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                String confirm_password = binding.etConfirmPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty() && !confirm_password.isEmpty()) {
                    if (password.equals(confirm_password)) {
                        presenter.onSendEmailClicked(email, password);
                    } else Snackbar.make(binding.mainView, "Пароли должны совпадать", Snackbar.LENGTH_LONG).show();
                } else Snackbar.make(binding.mainView, "Все поля должны быть заполнены", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSendEmailSuccess() {
        Intent intent = new Intent(this, ConfirmEmailActivity.class);
        intent.putExtra("email", binding.etEmail.getText().toString().trim());
        startActivity(intent);
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
