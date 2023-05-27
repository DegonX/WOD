package com.degonx.warriorsofdeagon.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.degonx.warriorsofdeagon.GameData.EquipmentsData;
import com.degonx.warriorsofdeagon.Objects.Equipments;

import java.util.List;

public class InventoryAdapter extends BaseAdapter {

    Context context;
    List<Equipments> Items;
    int showItem;

    public InventoryAdapter(Context c, List<Equipments> inveList, int show) {
        Items = inveList;
        context = c;
        showItem = show;
        if (showItem == 2)
            Items.removeIf(eq -> eq.equipSpot > 0);
    }

    public int getCount() {
        return Items.size();
    }

    @Override
    public Object getItem(int position) {
        return Items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageList = new ImageView(context);
        imageList.setPadding(3, 3, 3, 3);
        imageList.setImageResource(EquipmentsData.getEquipmentImage(Items.get(position).equipID));
        imageList.setLayoutParams(new GridView.LayoutParams(100, 100));
        if (showItem == 0)
            if (Items.get(position).equipSpot > 0)
                imageList.setVisibility(View.GONE);
        return imageList;
    }
}
