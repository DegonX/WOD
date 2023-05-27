package com.degonx.warriorsofdeagon.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.degonx.warriorsofdeagon.GameData.SkillsData;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.R;

import java.util.List;

public class SkillsAdapter extends BaseAdapter {

    Context context;
    List<Skills> Skills;

    public SkillsAdapter(Context c, List<Skills> skills) {
        Skills = skills;
        context = c;
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
        if (Skills.get(position).skillID > 0)
            imageList.setImageResource(SkillsData.getSkillImage(Skills.get(position).skillID));
        else if (Skills.get(position).skillID == -1)
            imageList.setImageResource(R.drawable.hppot);
        else if (Skills.get(position).skillID == -2)
            imageList.setImageResource(R.drawable.mppot);
        else if (Skills.get(position).skillID == -10)
            imageList.setImageResource(R.drawable.skillc);
        imageList.setLayoutParams(new GridView.LayoutParams(100, 100));
        return imageList;
    }
}
