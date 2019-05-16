package com.era.checkmelanoma.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.adapters.PatientsAdapter;
import com.era.checkmelanoma.databinding.ActivityMainBinding;
import com.era.checkmelanoma.mvp.contracts.MainContract;
import com.era.checkmelanoma.mvp.interactors.MainInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.MainPresenterImpl;
import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;
import com.era.checkmelanoma.utils.BottomGenderFragment;
import com.era.checkmelanoma.utils.IOnBtnPressed;
import com.era.checkmelanoma.utils.PrefConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class MainActivity extends AppCompatActivity implements MainContract.View, SwipeRefreshLayout.OnRefreshListener, IOnBtnPressed {

    private ActivityMainBinding binding;

    private ArrayList<PatientsResponse.Object> patientArrayList;
    private Calendar dateAndTime;
    private String current_date;
    private long curDateLong;
    private String date_of_birth;
    private TextView tv_date_of_birth;
    private PrefConfig prefConfig;
    private ProgressDialog progressDialog;
    private MainContract.Presenter presenter;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private PatientsAdapter patientsAdapter;
    private int page = 0;
    private TextView patient_gender;
    private BottomGenderFragment bottomGenderFragment;
    private String patient_gender_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        prefConfig = new PrefConfig(this);
        progressDialog = initProgressDialog(this, "Идёт загрузка");
        presenter = new MainPresenterImpl(this, new MainInteractorImpl());
        dateAndTime = Calendar.getInstance();
        binding.pullToRefresh.setOnRefreshListener(this);

        reInitRecyclerView();
        reloadData();

    }

    private void reInitRecyclerView() {
        customLinearLayoutManager = new CustomLinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return super.canScrollVertically();
            }
        };

        binding.patientsRecycler.setLayoutManager(customLinearLayoutManager);

        patientArrayList = new ArrayList<>();
        patientsAdapter = new PatientsAdapter(patientArrayList, this, binding.patientsRecycler, this);
        binding.patientsRecycler.setAdapter(patientsAdapter);

        patientsAdapter.setOnLoadMoreListener(new PatientsAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                patientArrayList.add(null);
                patientsAdapter.notifyItemInserted(patientArrayList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            patientArrayList.remove(patientArrayList.size() - 1);
                            patientsAdapter.notifyItemRemoved(patientArrayList.size());
                        } catch (Exception ignored) {
                        }
                        page = page + 1;
                        presenter.onGetPatientList(prefConfig.getToken(), "", "", "", page, 15);
                    }
                }, 500);
            }
        });
    }

    private void reloadData() {
        page = 0;
        if (patientArrayList.size() > 0) {
            reInitRecyclerView();
        }
        presenter.onGetPatientList(prefConfig.getToken(), "", "", "", page, 15);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_patient:
                curDateLong = 0;
                patient_gender_str = "";
                openAddPatientDialog();
                break;
            case R.id.more:
                break;
        }
        return true;
    }

    private void openAddPatientDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_patient_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText surname = dialogView.findViewById(R.id.surname);
        final EditText name = dialogView.findViewById(R.id.name);
        final EditText patronymic = dialogView.findViewById(R.id.patronymic);
        patient_gender = dialogView.findViewById(R.id.gender);
        tv_date_of_birth = dialogView.findViewById(R.id.date_of_birth);
        Button add_patient = dialogView.findViewById(R.id.add_patient);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        tv_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        BottomGenderFragment.setBtnClickListener(this);
        patient_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomGenderFragment = BottomGenderFragment.getInstance();
                bottomGenderFragment.show(getSupportFragmentManager(), "gender");
            }
        });

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String surname_str = surname.getText().toString().trim();
                String name_str = name.getText().toString().trim();
                String patronymic_str = patronymic.getText().toString().trim();
                if (!surname_str.isEmpty() && !name_str.isEmpty() && !patronymic_str.isEmpty()
                        && !patient_gender_str.isEmpty() && curDateLong != 0) {
                    presenter.onAddPatientClicked(prefConfig.getToken(), surname_str, name_str, patronymic_str, patient_gender_str, curDateLong);

                    alertDialog.dismiss();
                } else Snackbar.make(binding.mainView, "Заполните все поля", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void openDatePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

            Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            curDateLong = calendar.getTimeInMillis();
            String timeStamp = new SimpleDateFormat("dd-MM-yyyy").format(curDateLong);
            if (tv_date_of_birth != null) {
                tv_date_of_birth.setText(timeStamp);
            }
        }
    };

    // установка начальных даты и времени
    public void setInitialDateTime() {
        current_date = DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
    }

    @Override
    public void showSnackbar(String message) {
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onGetPatientListSuccess(ArrayList<PatientsResponse.Object> patientsList) {
        for (int i = 0; i < patientsList.size(); i++) {
            patientArrayList.add(patientsList.get(i));
            patientsAdapter.notifyDataSetChanged();
            patientsAdapter.setLoaded();
        }
    }

    @Override
    public void onAddPatientSuccess() {
        reloadData();
        Snackbar.make(binding.mainView, "Пациент успешно добавлен", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        blockListView();
    }

    @Override
    public void hideProgress() {
        unblockListView();
        binding.pullToRefresh.setRefreshing(false);
    }

    public void blockListView() {
        binding.patientsRecycler.setClickable(false);
        customLinearLayoutManager.setScrollEnabled(false);
    }

    public void unblockListView() {
        binding.patientsRecycler.setClickable(true);
        customLinearLayoutManager.setScrollEnabled(true);
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    @Override
    public void onManClicked() {
        patient_gender_str = "М";
        patient_gender.setText("Мужской");
        bottomGenderFragment.dismiss();
    }

    @Override
    public void onWomanClicked() {
        patient_gender_str = "М";
        patient_gender.setText("Женский");
        bottomGenderFragment.dismiss();
    }

    public class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}
