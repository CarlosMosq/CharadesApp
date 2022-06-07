package com.company.charadesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeckAdapter extends BaseAdapter {
    private final List<ModelClassCard> list;
    private final Context context;

    public DeckAdapter(List<ModelClassCard> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(list == null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_model, viewGroup, false);

        LinearLayout topBar, bottomBar;
        TextView topPoints, bottomPoints, cardTitle, descriptionText, category, cardNbr;
        ImageView topCorner, bottomCorner;

        topBar = view.findViewById(R.id.topBar);
        bottomBar = view.findViewById(R.id.bottomBar);
        topPoints = view.findViewById(R.id.topPoints);
        bottomPoints = view.findViewById(R.id.bottomPoints);
        cardTitle = view.findViewById(R.id.cardTitle);
        descriptionText = view.findViewById(R.id.descriptionText);
        category = view.findViewById(R.id.category);
        cardNbr = view.findViewById(R.id.cardNbr);
        topCorner = view.findViewById(R.id.topCorner);
        bottomCorner = view.findViewById(R.id.bottomCorner);

        CompletableFuture.runAsync(() -> {
            topBar.setBackgroundColor(returnColor(list.get(i).getPoints()));
            bottomBar.setBackgroundColor(returnColor(list.get(i).getPoints()));
            topPoints.setText(list.get(i).getPoints());
            bottomPoints.setText(list.get(i).getPoints());
            cardTitle.setText(list.get(i).getTitle());
            descriptionText.setText(list.get(i).getDescription());
            category.setText(list.get(i).getCategory());
            cardNbr.setText(list.get(i).getItem());
            topCorner.setImageResource(returnCategoryImg(list.get(i).getCategory()));
            bottomCorner.setImageResource(returnCategoryImg(list.get(i).getCategory()));
        });

        return view;
    }

    private int returnCategoryImg(String category) {
        switch (category) {
            case "Memes" : return R.drawable.ic_losango;
            case "Celebridades/Personagens" : return R.drawable.ic_heart;
            case "Figuras Históricas" : return R.drawable.ic_spades;
            case "País" : return R.drawable.ic_clubs;
            case "Qualquer Coisa" : return R.drawable.ic_outline_diamond_24;
            default: return R.drawable.ic_losango;
        }
    }

    private int returnColor(String points) {
        switch (points) {
            case "1" : return R.color.game_blue;
            case "2" : return R.color.game_green;
            case "3" : return R.color.game_red;
            case "4" : return R.color.game_purple;
            default: return R.color.dark_blue;
        }
    }
}
