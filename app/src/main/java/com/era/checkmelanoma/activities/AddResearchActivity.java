package com.era.checkmelanoma.activities;

import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MultipartBody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.databinding.ActivityAddResearchBinding;
import com.era.checkmelanoma.mvp.contracts.AddResearchContract;
import com.era.checkmelanoma.mvp.interactors.AddResearchInteractorImpl;
import com.era.checkmelanoma.mvp.presenters.AddResearchPresenterImpl;
import com.era.checkmelanoma.retrofit.models.responses.AddResearchResponse;
import com.era.checkmelanoma.utils.BottomGenderFragment;
import com.era.checkmelanoma.utils.IOnBtnPressed;
import com.era.checkmelanoma.utils.PrefConfig;
import com.era.checkmelanoma.utils.RequestPermissionHandler;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.era.checkmelanoma.utils.CompressAndConvertBitmap.compressBitmap;
import static com.era.checkmelanoma.utils.Constants.initProgressDialog;

public class AddResearchActivity extends AppCompatActivity implements IOnBtnPressed, AddResearchContract.View {

    private RequestPermissionHandler mRequestPermissionHandler;
    private ActivityAddResearchBinding binding;
    private BottomGenderFragment bottomGenderFragment;
    private String patient_gender = "";
    private ProgressDialog progressDialog;
    private PrefConfig prefConfig;
    private AddResearchContract.Presenter presenter;
    private int patient_id;

    private Bitmap photo;
    private MultipartBody.Part multipart_photo = null;
    private Map<Integer, String> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_research);
        setSupportActionBar(binding.toolbar);

        progressDialog = initProgressDialog(this, "Идёт загрузка");
        prefConfig = new PrefConfig(this);
        presenter = new AddResearchPresenterImpl(this, new AddResearchInteractorImpl());
        mRequestPermissionHandler = new RequestPermissionHandler();

        photos = prefConfig.getPhotos();

        if (getIntent().getIntExtra("id", -1) != -1) {
            patient_id = getIntent().getIntExtra("id", -1);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }

        binding.choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        BottomGenderFragment.setBtnClickListener(this);
        binding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomGenderFragment = BottomGenderFragment.getInstance();
                bottomGenderFragment.show(getSupportFragmentManager(), "gender");
            }
        });

        binding.createResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String researchPlace = binding.researchPlace.getText().toString().trim();
                if (!researchPlace.isEmpty() && multipart_photo != null) {
                    presenter.onAddResearchClicked(prefConfig.getToken(), multipart_photo, String.valueOf(patient_id), researchPlace);
                } else Snackbar.make(binding.mainView, "Выберите фото и заполните область исследования", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private String getEncodedPhoto() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void checkPermission() {
        mRequestPermissionHandler.requestPermission(this, 777,
                new RequestPermissionHandler.RequestPermissionListener() {
                    @Override
                    public void onSuccess() {
                        ImagePicker.create(AddResearchActivity.this)
                                .returnMode(ReturnMode.ALL)
                                .folderMode(true)
                                .single()
                                .includeVideo(false)
                                .start();
                    }
                    @Override
                    public void onFailed() {
                    }
                }
        );
    }

    // handling after permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermissionHandler.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
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
        Snackbar.make(binding.mainView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAddResearchSuccess(AddResearchResponse.Object research) {
        photos.put(research.getId(), getEncodedPhoto());
        prefConfig.setPhotos(photos);
        int benign = Integer.parseInt(String.valueOf(Math.round(research.getBenign()*100)));
        int malignant = Integer.parseInt(String.valueOf(Math.round(research.getMalignant()*100)));
        String diagnosis = benign > malignant ? "Доброкачественная" : "Злокачественная";

        Intent intent = new Intent(this, ResearchCardActivity.class);
        intent.putExtra("percent", benign > malignant ? benign : malignant);
        intent.putExtra("diagnosis", diagnosis);
        intent.putExtra("researchPlace", research.getSubjectStudy());
        intent.putExtra("research_id", research.getId());
        startActivityForResult(intent, 4983);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    // handling selected image
    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            String filePath = image.getPath();
            if (filePath != null) {
                photo = BitmapFactory.decodeFile(filePath);
                multipart_photo = compressBitmap(photo, "image", getApplicationContext());
                binding.chosePhoto.setImageBitmap(photo);
                binding.addPhotoTxt.setVisibility(View.GONE);
            } else {
                Snackbar.make(binding.mainView, "Не удалось загрузить фото", Snackbar.LENGTH_LONG).show();
            }
        }
        if (requestCode == 4983) {
            finish();
        }
    }
}
