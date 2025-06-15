package com.example.clicklap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clicklap.FullViewProduct;
import com.example.clicklap.R;

import java.util.List;

public class RecommendedProductAdapter extends RecyclerView.Adapter<RecommendedProductAdapter.ProductViewHolder> {

    private Context context;
    private List<FullViewProduct.Product> products;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(FullViewProduct.Product product);
    }

    public RecommendedProductAdapter(Context context, List<FullViewProduct.Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommended_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        FullViewProduct.Product product = products.get(position);

        holder.tvTitle.setText(product.getTitle());
        holder.tvPrice.setText(product.getPrice());
        holder.tvOriginalPrice.setText(product.getOriginalPrice());
        holder.tvRating.setText(String.valueOf(product.getRating()));
        holder.tvReviewCount.setText("(" + product.getReviewCount() + ")");

        // Load product image
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.ivProduct);

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvTitle, tvPrice, tvOriginalPrice, tvRating, tvReviewCount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOriginalPrice = itemView.findViewById(R.id.tv_original_price);
            tvRating = itemView.findViewById(R.id.tv_rating);
            tvReviewCount = itemView.findViewById(R.id.tv_review_count);
        }
    }
}