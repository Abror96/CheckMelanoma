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

import java.util.ArrayList;

public class PatientCardActivity extends AppCompatActivity {

    private ActivityPatientCardBinding binding;
    private String patient_name;
    private String patient_date;
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
            patient_name = getIntent().getStringExtra("name");
            patient_date = getIntent().getStringExtra("date");
            binding.patientName.setText(patient_name);
            binding.patientDate.setText(patient_date);
        }

        researchArrayList = new ArrayList<>();
        getResearchData();

        ResearchesAdapter adapter = new ResearchesAdapter(researchArrayList, this);
        binding.researchesRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.researchesRecycler.setAdapter(adapter);
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
