package com.brijesh.starwars.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brijesh.starwars.R;
import com.brijesh.starwars.data.model.People;
import com.brijesh.starwars.listeners.OnItemClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private final OnItemClickListener onItemClickListener;
    private List<People> peopleList;

    public HomeAdapter(List<People> aPeopleList, OnItemClickListener aOnItemClickListener) {
        peopleList = aPeopleList;
        onItemClickListener = aOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_people_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        People people = peopleList.get(position);
        holder.tvName.setText(people.getName());
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
