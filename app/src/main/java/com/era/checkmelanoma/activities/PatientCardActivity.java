package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.adapters.PatientsAdapter;
import com.era.checkmelanoma.adapters.ResearchesAdapter;
import com.era.checkmelanoma.databinding.ActivityPatientCardBinding;
import com.era.checkmelanoma.mvp.contracts.PatientCardContract;
import com.era.checkmelanoma.mvp.interactors.PatientCardInteractorsImpl;
import com.era.checkmelanoma.mvp.presenters.PatientCardPresenterImpl;
import com.era.checkmelanoma.retrofit.models.responses.Research;
import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;
import com.era.checkmelanoma.utils.PrefConfig;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class PatientCardActivity extends AppCompatActivity implements PatientCardContract.View, SwipeRefreshLayout.OnRefreshListener {

    private ActivityPatientCardBinding binding;
    private String patient_name;
    private String patient_date;
    private int patient_id;
    private String patient_sex;
    private Long date_of_birth = 0L;
    private int page = 0;
    private ArrayList<ResearchesResponse.Object> researchArrayList;
    private CustomLinearLayoutManager customLinearLayoutManager;
    private PrefConfig prefConfig;
    private ResearchesAdapter researchesAdapter;
    private PatientCardContract.Presenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_card);
        setSupportActionBar(binding.toolbar);

        prefConfig = new PrefConfig(this);
        presenter = new PatientCardPresenterImpl(this, new PatientCardInteractorsImpl());
        progressDialog = initProgressDialog(this, "Идёт загрузка");
        binding.pullToRefresh.setOnRefreshListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }

        if (getIntent().getStringExtra("name") != null) {

            patient_id = getIntent().getIntExtra("id", -1);
            patient_name = getIntent().getStringExtra("surname") + " " +
                    getIntent().getStringExtra("name").substring(0,1).toUpperCase() + ". " +
                    getIntent().getStringExtra("patronymic").substring(0,1).toUpperCase()+".";
            patient_date = getIntent().getStringExtra("date");
            patient_sex = getIntent().getStringExtra("sex");
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date parsedDate = dateFormat.parse(patient_date);
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                date_of_birth = timestamp.getTime();
            } catch(Exception e) { //this generic but you can control another types of exception
                // look the origin of excption
            }

            binding.patientName.setText(patient_name);
            binding.patientDate.setText(getDifferenceBetweenDates(date_of_birth));
            Log.d("LOGGERR", "onCreate: " + patient_sex);
            binding.patientGender.setText(patient_sex.equals("М") ? "Мужской" : "Женский");
        }

        progressDialog.show();
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

        binding.researchesRecycler.setLayoutManager(customLinearLayoutManager);

        researchArrayList = new ArrayList<>();
        researchesAdapter = new ResearchesAdapter(researchArrayList, this, binding.researchesRecycler, this, patient_id);
        binding.researchesRecycler.setAdapter(researchesAdapter);

        researchesAdapter.setOnLoadMoreListener(new ResearchesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                researchArrayList.add(null);
                researchesAdapter.notifyItemInserted(researchArrayList.size() - 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            researchArrayList.remove(researchArrayList.size() - 1);
                            researchesAdapter.notifyItemRemoved(researchArrayList.size());
                        } catch (Exception ignored) {
                        }
                        page = page + 1;
                        presenter.onGetResearchesListCalled(Long.parseLong(String.valueOf(patient_id)), page, 10, prefConfig.getToken());
                    }
                }, 500);
            }
        });
    }

    private void reloadData() {
        page = 0;
        if (researchArrayList.size() > 0) {
            reInitRecyclerView();
        }
        presenter.onGetResearchesListCalled(Long.parseLong(String.valueOf(patient_id)), page, 10, prefConfig.getToken());
    }

    private String getDifferenceBetweenDates(Long bdate) {
        Date firstDate = new Date(bdate);
        Date curDate = new Date();
        curDate.getTime();

        long timeDiff = Math.abs(firstDate.getTime() - curDate.getTime());
        String diff = String.valueOf(Math.round(TimeUnit.MILLISECONDS.toHours(timeDiff) / 8760));
        int iTens = Integer.parseInt(diff) % 10;
        String sYears = Integer.toString(Integer.parseInt(diff))
                + " " + ((iTens == 1) ? "год" : ((iTens < 5 & iTens != 0) ? "года" : "лет"));
        return sYears;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_research:
                Intent intent = new Intent(getApplicationContext(), AddResearchActivity.class);
                intent.putExtra("id", patient_id);
                startActivityForResult(intent, 3975);
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
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onGetResearchesListSuccess(ArrayList<ResearchesResponse.Object> researchesList) {
        for (int i = 0; i < researchesList.size(); i++) {
            researchArrayList.add(researchesList.get(i));
            researchesAdapter.notifyDataSetChanged();
            researchesAdapter.setLoaded();
        }
    }

    @Override
    public void showProgress() {
        blockListView();
    }

    @Override
    public void hideProgress() {
        unblockListView();
        progressDialog.dismiss();
        binding.pullToRefresh.setRefreshing(false);
    }

    public void blockListView() {
        binding.researchesRecycler.setClickable(false);
        customLinearLayoutManager.setScrollEnabled(false);
    }

    public void unblockListView() {
        binding.researchesRecycler.setClickable(true);
        customLinearLayoutManager.setScrollEnabled(true);
    }

    @Override
    public void onRefresh() {
        reloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3975) {
            reloadData();
        }
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
}
