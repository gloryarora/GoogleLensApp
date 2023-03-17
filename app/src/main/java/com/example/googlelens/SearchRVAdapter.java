package com.example.googlelens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchRVAdapter extends RecyclerView.Adapter<SearchRVAdapter.ViewHolder>{
private Context context;
private ArrayList<SearchRVModal> searchRVModals;

    public SearchRVAdapter(Context context, ArrayList<SearchRVModal> searchRVModals) {
        this.context = context;
        this.searchRVModals = searchRVModals;
    }

    @NonNull
    @Override
    public SearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.search_results_rv_item,parent,false);
    return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRVAdapter.ViewHolder holder, int position) {
        SearchRVModal searchRVModal = searchRVModals.get(position);
        holder.titleTV.setText(searchRVModal.getTitle());
        holder.snippetTV.setText(searchRVModal.getLink());
        holder.desTV.setText(searchRVModal.getSnippet());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(searchRVModal.getLink()));
                context.startActivity(i);
            }
        });
    }



    @Override
    public int getItemCount() {
        return searchRVModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTV,snippetTV,desTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV=itemView.findViewById(R.id.TVTitle);
            snippetTV=itemView.findViewById(R.id.TVSnippet);
            desTV=itemView.findViewById(R.id.TVDescription);


        }
    }
}
