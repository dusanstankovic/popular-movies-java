package dev.dstankovic.popularmoviesjava.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.dstankovic.popularmoviesjava.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    OnMovieClickListener mOnMovieClickListener;
    CircleImageView mCircleImageView;
    TextView mCategoryTitle;

    public CategoryViewHolder(@NonNull View itemView, OnMovieClickListener mOnMovieClickListener) {
        super(itemView);

        this.mOnMovieClickListener = mOnMovieClickListener;
        mCircleImageView = itemView.findViewById(R.id.category_image);
        mCategoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mOnMovieClickListener.onCategoryClick(mCategoryTitle.getText().toString());
    }
}
