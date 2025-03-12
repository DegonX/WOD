package com.degonx.warriorsofdeagon.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.R;

import java.util.List;

public class SkillsAdapter extends BaseAdapter {

    Context context;
    List<Skills> Skills;

    public SkillsAdapter(Context context, List<Skills> Skills) {
        this.Skills = Skills;
        this.context = context;
    }

    public int getCount() {
        return Skills.size();
    }

    @Override
    public Object getItem(int position) {
        return Skills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageList = new ImageView(context);

        switch (Skills.get(position).skillID) {
            case "HP_POTION_SKILL":
                imageList.setImageResource(R.drawable.hppot);
                break;
            case "MP_POTION_SKILL":
                imageList.setImageResource(R.drawable.mppot);
                break;
            case "SKILL_MENU_BUTTON":
                imageList.setImageResource(R.drawable.skillc);
                break;
            default:
                imageList.setImageResource(SkillsEnum.valueOf(Skills.get(position).skillID).getSkillImage());
                break;
        }

        imageList.setLayoutParams(new GridView.LayoutParams(100, 100));
        return imageList;
    }
}
