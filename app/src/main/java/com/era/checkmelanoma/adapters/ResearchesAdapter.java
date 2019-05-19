package com.era.checkmelanoma.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.activities.ResearchCardActivity;
import com.era.checkmelanoma.mvp.contracts.PatientCardContract;
import com.era.checkmelanoma.retrofit.models.responses.Research;
import com.era.checkmelanoma.retrofit.models.responses.ResearchesResponse;

import java.util.ArrayList;

import static com.era.checkmelanoma.utils.Constants.getDate;

public class ResearchesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ResearchesResponse.Object> researchArrayList;
    private Context context;
    private RecyclerView recyclerView;
    private PatientCardContract.View view;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private int patient_id;

    public ResearchesAdapter(ArrayList<ResearchesResponse.Object> researchArrayList, Context context, RecyclerView recyclerView, PatientCardContract.View view, int patient_id) {
        this.researchArrayList = researchArrayList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.view = view;
        this.patient_id = patient_id;

        // load more
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if ((!isLoading && totalItemCount <=
                        (lastVisibleItem + visibleThreshold) &
                        totalItemCount > 9)) {

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
            return new ResearchesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_item, viewGroup, false));
        } else if (viewType == TYPE_LOADING) {
            return new LoadingViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_progressbar, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ResearchesViewHolder) {
            ResearchesViewHolder researchesViewHolder = (ResearchesViewHolder) holder;

            researchesViewHolder.name.setText(getItem(position).getSubjectStudy());
            researchesViewHolder.date.setText("Дата исследования: " + getItem(position).getDateAnalisys().substring(0, 10));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int benign = Integer.parseInt(String.valueOf(Math.round(getItem(position).getBenign()*100)));
                    int malignant = Integer.parseInt(String.valueOf(Math.round(getItem(position).getMalignant()*100)));
                    String diagnosis = benign > malignant ? "Доброкачественная" : "Злокачественная";
                    Log.d("LOGGERR", "Добр: " + benign + " Зло: " + malignant + " Сравнение: " + (benign > malignant));

                    Intent intent = new Intent(context, ResearchCardActivity.class);
                    intent.putExtra("percent", benign > malignant ? benign : malignant);
                    intent.putExtra("diagnosis", diagnosis);
                    intent.putExtra("researchPlace", getItem(position).getSubjectStudy());
                    intent.putExtra("research_id", getItem(position).getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private ResearchesResponse.Object getItem(int position) {
        return researchArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return researchArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return researchArrayList.get(position) == null ? TYPE_LOADING : TYPE_ITEM;
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




    class ResearchesViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;

        public ResearchesViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.patient_name);
            date = itemView.findViewById(R.id.patient_date);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.itemProgressbar);
        }
    }
}
