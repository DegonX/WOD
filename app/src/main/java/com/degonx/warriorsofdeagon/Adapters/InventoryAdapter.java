package com.degonx.warriorsofdeagon.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Objects.Equipments;

public class InventoryAdapter extends BaseAdapter {

    Context context;
    Equipments[] charEquipments;

    public InventoryAdapter(Context context, Equipments[] charEquipments) {
        this.charEquipments = charEquipments;
        this.context = context;
    }

    public int getCount() {
        return charEquipments.length;
    }

    @Override
    public Object getItem(int position) {
        return charEquipments[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageList = new ImageView(context);
        imageList.setPadding(3, 3, 3, 3);
        imageList.setLayoutParams(new GridView.LayoutParams(100, 100));
        if (charEquipments[position] != null)
            imageList.setImageResource(EquipmentsEnum.valueOf(charEquipments[position].equipmentID).getEquipmentImage());
        return imageList;
    }
}
