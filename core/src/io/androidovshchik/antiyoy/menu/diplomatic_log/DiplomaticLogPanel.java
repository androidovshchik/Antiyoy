package io.androidovshchik.antiyoy.menu.diplomatic_log;

import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticLog;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticMessage;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListBehaviorYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ListItemYio;
import io.androidovshchik.antiyoy.menu.scrollable_list.ScrollableListYio;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.RectangleYio;

public class DiplomaticLogPanel extends ScrollableListYio {
    boolean readyToDie = false;
    public RectangleYio titleBackground = new RectangleYio();

    class C01151 implements ListBehaviorYio {
        C01151() {
        }

        public void applyItem(ListItemYio item) {
            DiplomaticLog log = DiplomaticLogPanel.this.menuControllerYio.yioGdxGame.gameController.fieldController.diplomacyManager.log;
            if (item.key.equals("clear")) {
                log.onClearMessagesButtonClicked();
            } else {
                log.onListItemClicked(item.key);
            }
            DiplomaticLogPanel.this.updateItems();
            if (DiplomaticLogPanel.this.items.size() == 0) {
                Scenes.sceneDiplomaticLog.hide();
            }
        }
    }

    public DiplomaticLogPanel(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initBehavior();
    }

    private void initBehavior() {
        setListBehavior(new C01151());
    }

    protected void updateViewPosition() {
        this.viewPosition.setBy(this.position);
        if (this.appearFactor.get() < 1.0f) {
            RectangleYio rectangleYio = this.viewPosition;
            rectangleYio.f147y -= (double) ((float) ((((double) (1.0f - this.appearFactor.get())) * 1.05d) * this.position.height));
        }
    }

    public void move() {
        super.move();
        updateTitleBackground();
        centerClearItem();
    }

    private void centerClearItem() {
        if (this.items.size() != 0) {
            ListItemYio clearItem = findClearItem();
            if (clearItem != null) {
                clearItem.titlePosition.f144x = (float) ((clearItem.position.f146x + (clearItem.position.width / 2.0d)) - ((double) (clearItem.titleWidth / 2.0f)));
            }
        }
    }

    private ListItemYio findClearItem() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ListItemYio item = (ListItemYio) it.next();
            if (item.key.equals("clear")) {
                return item;
            }
        }
        return null;
    }

    private void updateTitleBackground() {
        this.titleBackground.setBy(this.viewPosition);
        this.titleBackground.height = (double) getItemHeight();
        this.titleBackground.f147y = (this.viewPosition.f147y + this.viewPosition.height) - this.titleBackground.height;
    }

    protected void updateLabelPosition() {
        this.labelPosition.f144x = (float) ((this.viewPosition.f146x + (this.viewPosition.width / 2.0d)) - ((double) (this.labelWidth / 2.0f)));
        this.labelPosition.f145y = (float) ((this.viewPosition.f147y + this.viewPosition.height) - ((double) (0.02f * GraphicsYio.width)));
    }

    protected void onAppear() {
        super.onAppear();
        this.readyToDie = false;
    }

    public void updateItems() {
        clearItems();
        GameController gameController = this.menuControllerYio.yioGdxGame.gameController;
        DiplomacyManager diplomacyManager = gameController.fieldController.diplomacyManager;
        DiplomaticEntity mainEntity = diplomacyManager.getMainEntity();
        Iterator it = diplomacyManager.log.messages.iterator();
        while (it.hasNext()) {
            DiplomaticMessage message = (DiplomaticMessage) it.next();
            if (message.recipient == mainEntity) {
                addItem(message.getKey(), LanguagesManager.getInstance().getString(message.type.name()), " ").setBckViewType(gameController.getColorIndexWithOffset(message.getSenderColor()));
            }
        }
        checkToAddClearAllItem();
        moveItems();
    }

    private void checkToAddClearAllItem() {
        if (this.items.size() != 0) {
            addItem("clear", LanguagesManager.getInstance().getString("editor_clear"), " ").setBckViewType(-1);
        }
    }

    protected void updateEdgeRectangles() {
        super.updateEdgeRectangles();
        this.topEdge.height = (double) (1.2f * GraphicsYio.height);
    }

    protected float getItemHeight() {
        return 0.08f * GraphicsYio.height;
    }

    public boolean checkToPerformAction() {
        if (!this.readyToDie) {
            return super.checkToPerformAction();
        }
        this.readyToDie = false;
        Scenes.sceneDiplomaticLog.hide();
        return true;
    }

    protected void onTouchDown() {
        if (!this.touched) {
            this.readyToDie = true;
        }
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderDiplomaticLogPanel;
    }
}
