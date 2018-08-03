package io.androidovshchik.antiyoy.menu;

import yio.tro.antiyoy.menu.render.MenuRender;
import yio.tro.antiyoy.menu.scrollable_list.ScrollableListYio;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SpecialThanksDialog extends ScrollableListYio {
    public SpecialThanksDialog(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderSpecialThanksDialog;
    }

    protected float getItemHeight() {
        return 0.08f * GraphicsYio.height;
    }

    protected void updateEdgeRectangles() {
        super.updateEdgeRectangles();
        this.topEdge.height = (double) (1.2f * GraphicsYio.height);
    }
}
