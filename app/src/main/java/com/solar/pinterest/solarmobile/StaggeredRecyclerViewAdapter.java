package com.solar.pinterest.solarmobile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    String tagForTest = "recyclerAdapter";

    private List<DataSourse.DataItem> dataForList;
    private Context pinContext;

    public StaggeredRecyclerViewAdapter(Context context, List<DataSourse.DataItem> nData) {
        dataForList = nData;
        pinContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_card_view_for_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(tagForTest, "-----called");

        holder.title.setText(dataForList.get(position).title);

        if (dataForList.get(position).title == "") {
            holder.title.setVisibility(View.GONE);
        }

        Glide.with(pinContext)
                .load(dataForList.get(position).imageUrl)
                .placeholder(R.color.silver_gray)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tagForTest, "click" + dataForList.get(position).title);
                Toast.makeText(pinContext, dataForList.get(position).title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataForList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pin_card_view_for_list_image);
            title = itemView.findViewById(R.id.pin_card_view_for_list_text);
        }
    }
}
