package com.example.clicklap.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clicklap.Fragment.HomeFragment;
import com.example.clicklap.R;

import java.util.List;

public class PromoCarouselAdapter extends RecyclerView.Adapter<PromoCarouselAdapter.PromoViewHolder> {

    private final List<HomeFragment.PromoItem> promoItemList;

    public PromoCarouselAdapter(List<HomeFragment.PromoItem> promoItemList) {
        this.promoItemList = promoItemList;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promo_carousel, parent, false);
        return new PromoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        HomeFragment.PromoItem promoItem = promoItemList.get(position);
        holder.bind(promoItem);
    }

    @Override
    public int getItemCount() {
        return promoItemList != null ? promoItemList.size() : 0;
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView promoImageView;

        public PromoViewHolder(@NonNull View itemView) {
            super(itemView);
            promoImageView = itemView.findViewById(R.id.promo_image);
        }

        public void bind(HomeFragment.PromoItem promoItem) {
            if (promoItem != null) {
                promoImageView.setImageResource(promoItem.imageResource);
            }
        }
    }
}
