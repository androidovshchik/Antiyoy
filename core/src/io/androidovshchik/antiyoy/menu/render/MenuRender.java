package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.gameplay.game_view.GameView;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.menu.MenuViewYio;

public abstract class MenuRender {
    static ArrayList<MenuRender> list = new ArrayList();
    public static RenderCheckButton renderCheckButton = new RenderCheckButton();
    public static RenderDiplomacyElement renderDiplomacyElement = new RenderDiplomacyElement();
    public static RenderDiplomaticDialog renderDiplomaticDialog = new RenderDiplomaticDialog();
    public static RenderDiplomaticLogPanel renderDiplomaticLogPanel = new RenderDiplomaticLogPanel();
    public static RenderFastConstructionPanel renderFastConstructionPanel = new RenderFastConstructionPanel();
    public static RenderFireworksElement renderFireworksElement = new RenderFireworksElement();
    public static RenderLevelSelector renderLevelSelector = new RenderLevelSelector();
    public static RenderNotificationElement renderNotificationElement = new RenderNotificationElement();
    public static RenderReplaySelector renderReplaySelector = new RenderReplaySelector();
    public static RenderSaveSlotSelector renderSaveSlotSelector = new RenderSaveSlotSelector();
    public static RenderScrollableList renderScrollableList = new RenderScrollableList();
    public static RenderSlider renderSlider = new RenderSlider();
    public static RenderSpecialThanksDialog renderSpecialThanksDialog = new RenderSpecialThanksDialog();
    public static RenderSpeedPanel renderSpeedPanel = new RenderSpeedPanel();
    public static RenderTurnStartDialog renderTurnStartDialog = new RenderTurnStartDialog();
    protected SpriteBatch batch;
    protected Color f104c;
    public float f105h;
    protected MenuViewYio menuViewYio;
    public float shadowOffset;
    public float f106w;

    public abstract void loadTextures();

    public abstract void renderFirstLayer(InterfaceElement interfaceElement);

    public abstract void renderSecondLayer(InterfaceElement interfaceElement);

    public abstract void renderThirdLayer(InterfaceElement interfaceElement);

    public MenuRender() {
        list.listIterator().add(this);
    }

    public static void updateRenderSystems(MenuViewYio menuViewYio) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ((MenuRender) it.next()).update(menuViewYio);
        }
    }

    void update(MenuViewYio menuViewYio) {
        this.menuViewYio = menuViewYio;
        this.batch = menuViewYio.batch;
        this.f104c = this.batch.getColor();
        this.f106w = menuViewYio.f103w;
        this.f105h = menuViewYio.f102h;
        this.shadowOffset = (float) ((int) (0.01d * ((double) this.f105h)));
        loadTextures();
    }

    public GameView getGameView() {
        return this.menuViewYio.yioGdxGame.gameView;
    }
}
