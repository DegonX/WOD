package com.degonx.warriorsofdeagon.Objects;

import android.widget.ImageView;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Portal;

public class Portals {

    private final ImageView portalImage;
    private final Areas portalArea;
    private final int portalX;
    private final int portalY;

    public Portals(Portal portal, ImageView portalImage) {
        this.portalImage = portalImage;
        portalArea = portal.getPortalArea();
        portalX = portal.getPortalX();
        portalY = portal.getPortalY();
    }

    public ImageView getPortalImage() {
        return portalImage;
    }

    public Areas getPortalArea() {
        return portalArea;
    }

    public int getPortalX() {
        return portalX;
    }

    public int getPortalY() {
        return portalY;
    }
}
