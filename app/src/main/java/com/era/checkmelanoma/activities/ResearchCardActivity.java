package com.era.checkmelanoma.activities;

import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityResearchCardBinding;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class ResearchCardActivity extends AppCompatActivity {

    private ActivityResearchCardBinding binding;
    private int research_precent = 0;
    private int mBackIndex;
    private int mSeries1Index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_research_card);
        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }

        if (getIntent().getIntExtra("percent", -1) != -1) {
            research_precent = getIntent().getIntExtra("percent", -1);
        }

        setChart();
    }

    private void setChart() {
        // Create required data series on the DecoView
        createBackSeries();
        createDataSeries1();

        // Setup events to be fired on a schedule
        createEvents();
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#3d4959"))
                .setRange(0, 100, 0)
                .setInitialVisibility(true)
                .setLineWidth(50f)
                .build();

        mBackIndex = binding.chart.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#ACC174"))
                .setRange(0, 100, 0)
                .setInitialVisibility(false)
                .setLineWidth(50f)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                binding.percent.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = binding.chart.addSeries(seriesItem);
    }

    private void createEvents() {
        binding.chart.executeReset();

        binding.chart.addEvent(new DecoEvent.Builder(100)
                .setIndex(mBackIndex)
                .setDuration(2000)
                .setDelay(100)
                .build());

        binding.chart.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(2000)
                .setDelay(850)
                .build());

        binding.chart.addEvent(new DecoEvent.Builder(research_precent)
                .setIndex(mSeries1Index)
                .setDelay(2850)
                .build());

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
}
