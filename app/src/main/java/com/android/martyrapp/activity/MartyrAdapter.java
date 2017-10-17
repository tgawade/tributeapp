package com.android.martyrapp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.martyrapp.model.Martyr;
import com.android.martyrapp.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MartyrAdapter extends RecyclerView.Adapter<MartyrAdapter.MyViewHolder> {
    private Context context;
    private List<Martyr> martyrList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, year, genre;
        public ImageView martyrImage;

        public MyViewHolder(View view,final Context ctx) {
            super(view);
            context = ctx;
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            year = (TextView) view.findViewById(R.id.year);
            martyrImage = (ImageView) view.findViewById(R.id.imageView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Martyr martyr = martyrList.get(getAdapterPosition());
                    Intent intent = new Intent(context, MartyrDetailActivity.class);
                    intent.putExtra("martyr", martyr);
                    ctx.startActivity(intent);
                }
            });
        }
        @Override
        public void onClick(View v){}
    }
    public MartyrAdapter(Context context, List<Martyr> martyrList) {
        this.context = context;
        this.martyrList = martyrList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);
        return new MyViewHolder(itemView,context);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Martyr martyr = martyrList.get(position);
        holder.title.setText(martyr.getTitle());
        holder.genre.setText(martyr.getGenre());
        holder.year.setText(martyr.getYear());
        holder.martyrImage.setMaxHeight(400);
        holder.martyrImage.setMaxWidth(400);
        Picasso.with(context)
                .load(Constants.S3_IMG_PATH + martyr.getImageName())
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.martyrImage);
    }
    @Override
    public int getItemCount(){
        return martyrList.size();
    }
}
