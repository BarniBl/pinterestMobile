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

import java.util.ArrayList;

public class StaggeredRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredRecyclerViewAdapter.ViewHolder> {

    String tagForTest = "recyclerAdapter";

    private ArrayList<String> pinTitles = new ArrayList<>();
    private ArrayList<String> pinImagesUrls = new ArrayList<>();
    private Context pinContext;

    public StaggeredRecyclerViewAdapter(Context context, ArrayList<String> titles, ArrayList<String> imagesUrls) {
        pinTitles = titles;
        pinImagesUrls = imagesUrls;
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

        holder.title.setText(pinTitles.get(position));

        if (pinTitles.get(position) == "") {
            holder.title.setVisibility(View.GONE);
        }

        Glide.with(pinContext)
                .load(pinImagesUrls.get(position))
                .placeholder(R.drawable.logo)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tagForTest, "click" + pinTitles.get(position));
                Toast.makeText(pinContext, pinTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pinImagesUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.pin_card_view_for_list_image);
            title = (TextView)itemView.findViewById(R.id.pin_card_view_for_list_text);
        }
    }
}
