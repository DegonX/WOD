package com.degonx.warriorsofdeagon.Enums.ItemsEnums;

import com.degonx.warriorsofdeagon.R;

public enum ShopItems {

    HP_POTION("HP Potion", R.drawable.hppot, "Potions that restore 2000 HP", 50),
    MP_POTION("MP Potion", R.drawable.mppot, "Potions that restore 2000 MP", 50);

     final String itemName;
     final int itemImage;
     final String itemDescription;
     final int itemPrice;

    ShopItems(String itemName, int itemImage, String itemDescription, int itemPrice) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemImage() {
        return itemImage;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getItemPrice() {
        return itemPrice;
    }
}
