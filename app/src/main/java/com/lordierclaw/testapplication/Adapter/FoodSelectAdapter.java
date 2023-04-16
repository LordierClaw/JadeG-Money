package com.lordierclaw.testapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lordierclaw.testapplication.Listener.IFoodSelectListener;
import com.lordierclaw.testapplication.Model.Food;
import com.lordierclaw.testapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodSelectAdapter extends RecyclerView.Adapter<FoodSelectAdapter.ViewHolder> {
    private List<Food> FoodList;
    private IFoodSelectListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_food_select, null);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = FoodList.get(position);
        if (food == null) return;
        holder.bind(food);
        holder.itemView.setOnClickListener(view -> {
            holder.foodCheckbox.setChecked(!holder.foodCheckbox.isChecked());
            if (listener != null) listener.onClick(food);
        });
    }

    public void setFoodSelectOnClickListener(IFoodSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (FoodList == null) return 0;
        return FoodList.size();
    }

    public void setList(List<Food> FoodList) {
        this.FoodList = FoodList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodPrice;
        private CircleImageView foodImage;
        private CheckBox foodCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.adapter_food_select_name);
            foodPrice = itemView.findViewById(R.id.adapter_food_select_price);
            foodImage = itemView.findViewById(R.id.adapter_food_select_image);
            foodCheckbox = itemView.findViewById(R.id.adapter_food_select_checkbox);
        }

        public void bind(Food food) {
            foodName.setText(food.getName());
            String price = String.format("%,d đ", food.getPrice()).replace(",", ".");
            foodPrice.setText(price);
            // TODO: set foodImage by using food.getImageUrl();
            foodCheckbox.setChecked(food.isChecked());
        }
    }
}
