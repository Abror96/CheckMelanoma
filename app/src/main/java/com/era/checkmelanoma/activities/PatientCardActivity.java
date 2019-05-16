package com.era.checkmelanoma.activities;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.adapters.ResearchesAdapter;
import com.era.checkmelanoma.databinding.ActivityPatientCardBinding;
import com.era.checkmelanoma.retrofit.models.responses.Research;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PatientCardActivity extends AppCompatActivity {

    private ActivityPatientCardBinding binding;
    private String patient_name;
    private String patient_date;
    private int patient_id;
    private String patient_sex;
    private Long date_of_birth = 0L;
    private ArrayList<Research> researchArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_card);
        setSupportActionBar(binding.toolbar);

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
            binding.patientGender.setText(patient_sex.equals("М") ? "Мужской" : "Женский");
        }

        researchArrayList = new ArrayList<>();
        getResearchData();

        ResearchesAdapter adapter = new ResearchesAdapter(researchArrayList, this);
        binding.researchesRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.researchesRecycler.setAdapter(adapter);
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

    private void getResearchData() {
        researchArrayList.add(new Research(1, "Колено", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 27));
        researchArrayList.add(new Research(2, "Спина", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 72));
        researchArrayList.add(new Research(3, "Шея", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 43));
        researchArrayList.add(new Research(4, "Локоть", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 83));
        researchArrayList.add(new Research(5, "Стопа", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 99));
        researchArrayList.add(new Research(6, "Бедро", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 34));
        researchArrayList.add(new Research(7, "Грудь", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 73));
        researchArrayList.add(new Research(8, "Живот", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 68));
        researchArrayList.add(new Research(9, "Плечо", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 4));
        researchArrayList.add(new Research(10, "Ладонь", 1557904926000L, "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b1/Nevus.jpg/250px-Nevus.jpg", 89));
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
                startActivity(intent);
                break;
            case R.id.more:
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
