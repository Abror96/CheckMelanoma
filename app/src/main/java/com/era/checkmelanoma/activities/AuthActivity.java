package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityAuthBinding;
import com.era.checkmelanoma.mvp.contracts.AuthContract;
import com.era.checkmelanoma.mvp.interactors.AuthInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.AuthPresenterImpl;
import com.era.checkmelanoma.utils.PrefConfig;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private ActivityAuthBinding binding;
    private ProgressDialog progressDialog;
    private AuthContract.Presenter presenter;
    private PrefConfig prefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth);

        progressDialog = initProgressDialog(this, "Идёт загрузка");
        presenter = new AuthPresenterImpl(this, new AuthInteractorImpl());
        prefConfig = new PrefConfig(this);

        if (prefConfig.getLoginStatus()) {
            openMainActivity();
        }

        binding.logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    presenter.onAuthClicked(email, password);
                } else Snackbar.make(binding.mainView, "Все поля должны быть заполнены", Snackbar.LENGTH_LONG).show();
            }
        });

        binding.noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAuthSuccess(String token) {
        prefConfig.setLoginStatus(true);
        prefConfig.setToken(token);
        openMainActivity();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
