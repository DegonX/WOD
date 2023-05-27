package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum Blesses {
    Citrine(R.color.citrine, "Attack", "HP"),
    Tourmaline(R.color.tourmaline, "Attack", "MP"),
    Sapphire(R.color.sapphire, "Attack", "Defense"),
    Carnelian(R.color.carnelian, "Attack", "Critical Rate"),
    Rhodonite(R.color.rhodonite, "Attack", "Critical Damage"),
    Garnet(R.color.garnet, "Defense", "Critical Damage"),
    Sunstone(R.color.sunstone, "Defense", "HP"),
    Moonstone(R.color.moonstone, "Defense", "MP");

    public final int Color;
    public final String Add1;
    public final String Add2;

    Blesses(int color, String add1, String add2) {
        Color = color;
        Add1 = add1;
        Add2 = add2;
    }
}
