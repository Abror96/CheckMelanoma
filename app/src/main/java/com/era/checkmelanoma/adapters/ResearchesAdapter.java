package com.era.checkmelanoma.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.era.checkmelanoma.R;
import com.era.checkmelanoma.activities.ResearchCardActivity;
import com.era.checkmelanoma.retrofit.models.responses.Research;

import java.util.ArrayList;

import static com.era.checkmelanoma.utils.Constants.getDate;

public class ResearchesAdapter extends RecyclerView.Adapter<ResearchesAdapter.ResearchesViewHolder> {

    private ArrayList<Research> researchArrayList;
    private Context context;

    public ResearchesAdapter(ArrayList<Research> researchArrayList, Context context) {
        this.researchArrayList = researchArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ResearchesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ResearchesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.patient_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResearchesViewHolder holder, final int position) {
        holder.name.setText(getItem(position).getPlace());
        holder.date.setText(getDate(getItem(position).getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ResearchCardActivity.class);
                intent.putExtra("percent", getItem(position).getResearch_percent());
                context.startActivity(intent);
            }
        });
    }

    private Research getItem(int position) {
        return researchArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return researchArrayList.size();
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
}
