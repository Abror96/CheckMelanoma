package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityAddResearchBinding;
import com.era.checkmelanoma.mvp.contracts.AddResearchContract;
import com.era.checkmelanoma.mvp.interactors.AddResearchInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.AddResearchPresenterImpl;
import com.era.checkmelanoma.utils.BottomGenderFragment;
import com.era.checkmelanoma.utils.IOnBtnPressed;
import com.era.checkmelanoma.utils.PrefConfig;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class AddResearchActivity extends AppCompatActivity implements IOnBtnPressed, AddResearchContract.View {

    private ActivityAddResearchBinding binding;
    private BottomGenderFragment bottomGenderFragment;
    private String patient_gender = "";
    private ProgressDialog progressDialog;
    private PrefConfig prefConfig;
    private AddResearchContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_research);
        setSupportActionBar(binding.toolbar);

        progressDialog = initProgressDialog(this, "Идёт загрузка");
        prefConfig = new PrefConfig(this);
        presenter = new AddResearchPresenterImpl(this, new AddResearchInteractorImpl());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }

        BottomGenderFragment.setBtnClickListener(this);
        binding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomGenderFragment = BottomGenderFragment.getInstance();
                bottomGenderFragment.show(getSupportFragmentManager(), "gender");
            }
        });
    }

    @Override
    public void onManClicked() {
        patient_gender = "М";
        binding.gender.setText("Мужской");
        bottomGenderFragment.dismiss();
    }

    @Override
    public void onWomanClicked() {
        patient_gender = "Ж";
        binding.gender.setText("Женский");
        bottomGenderFragment.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.research_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_patient:

                break;
            case R.id.more:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void showSnackbar(String message) {

    }

    @Override
    public void onAddResearchSuccess() {

    }
}
