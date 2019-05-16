package com.era.checkmelanoma.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.activities.PatientCardActivity;
import com.era.checkmelanoma.mvp.contracts.MainContract;
import com.era.checkmelanoma.retrofit.models.responses.PatientsResponse;

import java.util.ArrayList;

public class PatientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PatientsResponse.Object> patientArrayList;
    private Context context;
    private MainContract.View view;
    private RecyclerView patientsRecycler;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public PatientsAdapter(ArrayList<PatientsResponse.Object> patientArrayList, Context context, RecyclerView patientsRecycler, MainContract.View view) {
        this.patientArrayList = patientArrayList;
        this.context = context;
        this.patientsRecycler = patientsRecycler;
        this.view = view;

        // load more
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) patientsRecycler.getLayoutManager();
        patientsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if ((!isLoading && totalItemCount <=
                        (lastVisibleItem + visibleThreshold) &
                        totalItemCount > 14)) {

                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new PatientsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_item, viewGroup, false));
        } else if (viewType == TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progressbar, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PatientsViewHolder) {
            PatientsViewHolder viewHolder = (PatientsViewHolder) holder;

            viewHolder.name.setText(getItem(position).getFamily() + " " + getItem(position).getName() + " " + getItem(position).getPatronymic());
            viewHolder.date_of_birth.setText("Год рождения: " + getItem(position).getDateBirth().substring(0, 10));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PatientCardActivity.class);
                    intent.putExtra("id", getItem(position).getId());
                    intent.putExtra("surname", getItem(position).getFamily());
                    intent.putExtra("name", getItem(position).getName());
                    intent.putExtra("patronymic", getItem(position).getPatronymic());
                    intent.putExtra("date", getItem(position).getDateBirth().substring(0, 10));
                    intent.putExtra("sex", getItem(position).getSex());
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemProgressbar);
        }
    }

    private PatientsResponse.Object getItem(int position) {
        return patientArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return patientArrayList.size();
    }

    class PatientsViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date_of_birth;

        public PatientsViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.patient_name);
            date_of_birth = itemView.findViewById(R.id.patient_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return patientArrayList.get(position) == null ? TYPE_LOADING : TYPE_ITEM;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    public void setLoaded() {
        isLoading = false;
    }
}
