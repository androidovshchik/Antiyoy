package io.androidovshchik.antiyoy.menu.diplomacy_element;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import io.androidovshchik.antiyoy.SoundControllerYio;
import io.androidovshchik.antiyoy.factor_yio.FactorYio;
import io.androidovshchik.antiyoy.gameplay.ClickDetector;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticContract;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomaticEntity;
import io.androidovshchik.antiyoy.menu.InterfaceElement;
import io.androidovshchik.antiyoy.menu.MenuControllerYio;
import io.androidovshchik.antiyoy.menu.render.MenuRender;
import io.androidovshchik.antiyoy.stuff.Fonts;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.RectangleYio;
import io.androidovshchik.antiyoy.stuff.Yio;
import io.androidovshchik.antiyoy.stuff.object_pool.ObjectPoolYio;
import io.androidovshchik.antiyoy.stuff.scroll_engine.ScrollEngineYio;

public class DiplomacyElement extends InterfaceElement {
    public FactorYio appearFactor = new FactorYio();
    ClickDetector clickDetector = new ClickDetector();
    DeIcon clickedIcon = null;
    DeItem clickedItem = null;
    PointYio currentTouch = new PointYio();
    public BitmapFont descFont = Fonts.smallerMenuFont;
    float hook;
    private float iconRadius;
    private float iconTouchOffset;
    public ArrayList<DeIcon> icons;
    public RectangleYio internalBackground = new RectangleYio();
    float itemHeight;
    public ArrayList<DeItem> items = new ArrayList();
    public final DeLabel label = new DeLabel(this);
    PointYio lastTouch = new PointYio();
    MenuControllerYio menuControllerYio;
    ObjectPoolYio<DeItem> poolItems;
    public RectangleYio position = new RectangleYio();
    ScrollEngineYio scrollEngineYio;
    DeItem selectedItem = null;
    private ArrayList<DiplomaticContract> tempContracts = new ArrayList();
    public BitmapFont titleFont = Fonts.gameFont;
    public RectangleYio topCover = new RectangleYio();
    float topLabelHeight;
    boolean touched = false;
    boolean touchedScrollArea = false;
    public RectangleYio viewPosition = new RectangleYio();

    class C01131 extends ObjectPoolYio<DeItem> {
        C01131() {
        }

        public DeItem makeNewObject() {
            return new DeItem(DiplomacyElement.this);
        }
    }

    public DiplomacyElement(MenuControllerYio menuControllerYio, int id) {
        super(id);
        this.menuControllerYio = menuControllerYio;
        initPool();
        initMetrics();
        initScrollEngine();
        initIcons();
    }

    private void initIcons() {
        this.icons = new ArrayList();
        addIcon(0);
        addIcon(1);
        addIcon(2);
        addIcon(3);
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            icon.setTouchOffset(this.iconTouchOffset);
            icon.setRadius(this.iconRadius);
        }
    }

    private void addIcon(int action) {
        DeIcon deIcon = new DeIcon(this);
        deIcon.setAction(action);
        this.icons.add(deIcon);
    }

    private void initPool() {
        this.poolItems = new C01131();
    }

    private void initScrollEngine() {
        this.scrollEngineYio = new ScrollEngineYio();
        this.scrollEngineYio.setSlider(0.0d, 0.0d);
        updateScrollEngineLimits();
        this.scrollEngineYio.setFriction(0.02d);
        this.scrollEngineYio.setSoftLimitOffset((double) (0.05f * GraphicsYio.width));
    }

    private void updateScrollEngineLimits() {
        this.scrollEngineYio.setLimits(0.0d, getScrollLimit());
    }

    private double getScrollLimit() {
        return (double) ((((float) this.items.size()) * this.itemHeight) - this.topLabelHeight);
    }

    private void initMetrics() {
        this.itemHeight = 0.2f * GraphicsYio.width;
        this.topLabelHeight = this.itemHeight / 2.0f;
        this.iconRadius = 0.42f * this.topLabelHeight;
        this.iconTouchOffset = this.iconRadius;
    }

    private void updateItems() {
        clearItems();
        GameController gameController = getGameController();
        for (DiplomaticEntity relationEntity : getDiplomacyManager(gameController).getEntity(gameController.turn).relations.keySet()) {
            addItem(relationEntity);
        }
        updateStatuses();
        sortItems();
    }

    private void updateStatuses() {
        DiplomacyManager diplomacyManager = getDiplomacyManager(getGameController());
        DiplomaticEntity mainEntity = diplomacyManager.getMainEntity();
        if (mainEntity.isHuman()) {
            Iterator it = this.items.iterator();
            while (it.hasNext()) {
                DeItem item = (DeItem) it.next();
                DiplomaticEntity relationEntity = diplomacyManager.getEntity(item.colorIndex);
                item.setStatus(convertRelationIntoStatus(mainEntity, relationEntity));
                item.setBlackMarkEnabled(mainEntity.isBlackMarkedWith(relationEntity));
                item.setDescriptionString(getItemDescription(mainEntity, relationEntity));
            }
        }
    }

    private DiplomacyManager getDiplomacyManager(GameController gameController) {
        return gameController.fieldController.diplomacyManager;
    }

    private GameController getGameController() {
        return this.menuControllerYio.yioGdxGame.gameController;
    }

    private String getItemDescription(DiplomaticEntity mainEntity, DiplomaticEntity relationEntity) {
        if (!relationEntity.alive) {
            return LanguagesManager.getInstance().getString("dead");
        }
        updateTempContracts(mainEntity, relationEntity);
        if (this.tempContracts.size() == 0) {
            return LanguagesManager.getInstance().getString(getRelationStringKey(mainEntity, relationEntity));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LanguagesManager.getInstance().getString(getRelationStringKey(mainEntity, relationEntity)));
        Iterator it = this.tempContracts.iterator();
        while (it.hasNext()) {
            DiplomaticContract contract = (DiplomaticContract) it.next();
            stringBuilder.append(" [");
            stringBuilder.append(contract.getDotationsStringFromEntityPerspective(mainEntity));
            stringBuilder.append(", ");
            stringBuilder.append(contract.getExpireCountDown());
            stringBuilder.append("x]");
        }
        return stringBuilder.toString();
    }

    private void updateTempContracts(DiplomaticEntity mainEntity, DiplomaticEntity relationEntity) {
        this.tempContracts.clear();
        DiplomacyManager diplomacyManager = getDiplomacyManager(getGameController());
        DiplomaticContract contract = diplomacyManager.findContract(1, mainEntity, relationEntity);
        if (contract != null) {
            this.tempContracts.add(contract);
        }
        contract = diplomacyManager.findContract(0, mainEntity, relationEntity);
        if (contract != null) {
            this.tempContracts.add(contract);
        }
        contract = diplomacyManager.findContract(3, mainEntity, relationEntity);
        if (contract != null) {
            this.tempContracts.add(contract);
        }
    }

    private String getRelationStringKey(DiplomaticEntity mainEntity, DiplomaticEntity relationEntity) {
        switch (mainEntity.getRelation(relationEntity)) {
            case 0:
                return "neutral";
            case 1:
                return "friend";
            case 2:
                return "enemy";
            default:
                return "-";
        }
    }

    private void sortItems() {
        Collections.sort(this.items);
    }

    private void addItem(DiplomaticEntity relationEntity) {
        DeItem deItem = (DeItem) this.poolItems.getNext();
        deItem.setColorIndex(relationEntity.color);
        deItem.setTitle(relationEntity.capitalName);
        this.items.add(deItem);
    }

    private int convertRelationIntoStatus(DiplomaticEntity mainEntity, DiplomaticEntity relationEntity) {
        if (!relationEntity.alive) {
            return 3;
        }
        switch (mainEntity.getRelation(relationEntity)) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                Yio.printStackTrace();
                return 3;
        }
    }

    private void clearItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            this.poolItems.add((DeItem) it.next());
        }
        this.items.clear();
    }

    void updateMetrics() {
        float currentY = (((float) this.position.height) - this.topLabelHeight) - this.itemHeight;
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            DeItem item = (DeItem) it.next();
            item.position.width = this.position.width;
            item.position.height = (double) this.itemHeight;
            item.delta.f144x = 0.0f;
            item.delta.f145y = currentY;
            currentY -= this.itemHeight;
        }
    }

    public void move() {
        moveFactors();
        updateViewPosition();
        this.scrollEngineYio.move();
        updateHook();
        moveItems();
        moveIcons();
        this.label.move();
        updateInternalBackgroundPosition();
        updateCover();
    }

    private void moveIcons() {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            ((DeIcon) it.next()).move();
        }
    }

    private void updateCover() {
        this.topCover.setBy(this.viewPosition);
        this.topCover.height = (double) this.topLabelHeight;
        this.topCover.f147y = (this.viewPosition.f147y + this.viewPosition.height) - this.topCover.height;
    }

    private void updateInternalBackgroundPosition() {
        this.internalBackground.f146x = this.viewPosition.f146x;
        this.internalBackground.f147y = this.viewPosition.f147y;
        this.internalBackground.width = this.viewPosition.width;
        this.internalBackground.height = this.viewPosition.height - ((double) this.topLabelHeight);
    }

    private void moveFactors() {
        if (this.appearFactor.hasToMove()) {
            this.appearFactor.move();
        }
    }

    private void updateHook() {
        this.hook = (float) this.scrollEngineYio.getSlider().f107a;
    }

    private void moveItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            DeItem item = (DeItem) it.next();
            item.move();
            if (!this.touched) {
                item.moveSelection();
            }
        }
    }

    private void updateViewPosition() {
        this.viewPosition.setBy(this.position);
        if (this.appearFactor.get() < 1.0f) {
            RectangleYio rectangleYio = this.viewPosition;
            rectangleYio.f147y -= (double) ((float) ((((double) (1.0f - this.appearFactor.get())) * 1.05d) * this.position.height));
        }
    }

    public FactorYio getFactor() {
        return this.appearFactor;
    }

    public void destroy() {
        this.appearFactor.destroy(1, 2.2d);
        onDestroy();
    }

    private void onDestroy() {
    }

    public void appear() {
        this.appearFactor.setValues(0.01d, 0.0d);
        this.appearFactor.appear(3, 1.25d);
        onAppear();
    }

    private void onAppear() {
        this.label.setVisible(true);
        this.selectedItem = null;
        updateAll();
        dropSelections();
        this.scrollEngineYio.resetToBottom();
    }

    public void onTurnStarted() {
        updateItems();
    }

    public void updateAll() {
        updateItems();
        updateMetrics();
        updateScrollEngineLimits();
    }

    public boolean isVisible() {
        return this.appearFactor.get() > 0.0f;
    }

    public boolean checkToPerformAction() {
        if (this.clickedItem != null) {
            performItemClickAction();
            this.clickedItem = null;
            return true;
        } else if (this.clickedIcon == null) {
            return false;
        } else {
            performIconClickAction();
            this.clickedIcon = null;
            return true;
        }
    }

    private void performIconClickAction() {
        getDiplomacyManager(getGameController()).onUserClickedContextIcon(this.selectedItem.colorIndex, this.clickedIcon.action);
    }

    private void performItemClickAction() {
    }

    public void onRelationsChanged() {
        updateStatuses();
        showIcons();
    }

    public void onFirstPlayerTurnEnded() {
        updateStatuses();
    }

    private void resetToLabel() {
        dropSelections();
        this.label.setVisible(true);
    }

    public boolean isTouchable() {
        return true;
    }

    private void updateCurrentTouch(int screenX, int screenY) {
        this.lastTouch.setBy(this.currentTouch);
        this.currentTouch.set((double) screenX, (double) screenY);
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!isVisible()) {
            return false;
        }
        boolean z;
        updateCurrentTouch(screenX, screenY);
        this.touched = ((double) this.currentTouch.f145y) < this.position.f147y + this.position.height;
        if (((double) this.currentTouch.f145y) < (this.position.f147y + this.position.height) - ((double) this.topLabelHeight)) {
            z = true;
        } else {
            z = false;
        }
        this.touchedScrollArea = z;
        if (this.touched) {
            this.clickDetector.touchDown(this.currentTouch);
            if (this.touchedScrollArea) {
                this.scrollEngineYio.updateCanSoftCorrect(false);
                checkToSelectItems();
            } else {
                checkToSelectIcons();
            }
        } else {
            destroy();
        }
        return true;
    }

    private void checkToSelectIcons() {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            if (icon.isTouched(this.currentTouch)) {
                icon.select();
            }
        }
    }

    private void checkToSelectItems() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            DeItem item = (DeItem) it.next();
            if (item.isTouched(this.currentTouch)) {
                item.select();
            }
        }
    }

    public boolean touchDrag(int screenX, int screenY, int pointer) {
        if (this.touched) {
            updateCurrentTouch(screenX, screenY);
            if (this.touchedScrollArea) {
                this.scrollEngineYio.setSpeed((double) (this.currentTouch.f145y - this.lastTouch.f145y));
            }
            this.clickDetector.touchDrag(this.currentTouch);
        }
        return this.touched;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        updateCurrentTouch(screenX, screenY);
        if (this.touchedScrollArea) {
            this.scrollEngineYio.updateCanSoftCorrect(true);
        }
        if (!this.touched) {
            return false;
        }
        this.touched = false;
        this.clickDetector.touchUp(this.currentTouch);
        if (!this.clickDetector.isClicked()) {
            return true;
        }
        onClick();
        return true;
    }

    private void onClick() {
        if (this.touchedScrollArea) {
            onClickInsideScrollArea();
        } else {
            onClickIcons();
        }
    }

    private void onClickIcons() {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            if (icon.isTouched(this.currentTouch)) {
                onIconClicked(icon);
            }
        }
    }

    private void onIconClicked(DeIcon icon) {
        SoundControllerYio.playSound(SoundControllerYio.soundPressButton);
        this.clickedIcon = icon;
    }

    private void onClickInsideScrollArea() {
        if (this.internalBackground.isPointInside(this.currentTouch, 0.0f)) {
            this.scrollEngineYio.setSpeed(0.0d);
            Iterator it = this.items.iterator();
            while (it.hasNext()) {
                DeItem item = (DeItem) it.next();
                if (item.isTouched(this.currentTouch)) {
                    onItemClicked(item);
                }
            }
        }
    }

    private void onItemClicked(DeItem item) {
        SoundControllerYio.playSound(SoundControllerYio.soundPressButton);
        if (item.keepSelection) {
            resetToLabel();
            return;
        }
        dropSelections();
        if (item.status != 3) {
            this.clickedItem = item;
            this.selectedItem = item;
            this.clickedItem.select();
            this.clickedItem.setKeepSelection(true);
            showIcons();
        }
    }

    void dropSelections() {
        Iterator it = this.items.iterator();
        while (it.hasNext()) {
            ((DeItem) it.next()).setKeepSelection(false);
        }
        resetIconsVisibility();
    }

    private void resetIconsVisibility() {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            ((DeIcon) it.next()).visible = false;
        }
    }

    void showIcons() {
        if (this.selectedItem != null && this.appearFactor.get() != 0.0f) {
            this.label.setVisible(false);
            resetIconsVisibility();
            GameController gameController = getGameController();
            DiplomacyManager diplomacyManager = getDiplomacyManager(gameController);
            DiplomaticEntity selectedEntity = diplomacyManager.getEntity(this.selectedItem.colorIndex);
            int relation = selectedEntity.getRelation(diplomacyManager.getEntity(gameController.turn));
            boolean blackMarked = diplomacyManager.getMainEntity().isBlackMarkedWith(selectedEntity);
            switch (relation) {
                case 0:
                    enableIcon(0);
                    enableIcon(1);
                    if (!blackMarked) {
                        enableIcon(2);
                    }
                    enableIcon(3);
                    break;
                case 1:
                    enableIcon(1);
                    enableIcon(3);
                    break;
                case 2:
                    enableIcon(0);
                    if (!blackMarked) {
                        enableIcon(2);
                        break;
                    }
                    break;
            }
            alignIcons();
            appearIcons();
        }
    }

    private void appearIcons() {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            ((DeIcon) it.next()).appear();
        }
    }

    private void alignIcons() {
        float iDelta = (this.iconTouchOffset * 2.0f) + (this.iconRadius * 2.0f);
        float currentX = (float) ((this.position.width / 2.0d) - ((double) ((iDelta * ((float) (getNumberOfVisibleIcons() - 1))) / 2.0f)));
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            if (icon.visible) {
                icon.delta.f144x = currentX;
                icon.delta.f145y = (float) (this.position.height - ((double) (this.topLabelHeight / 2.0f)));
                currentX += iDelta;
            }
        }
    }

    int getNumberOfVisibleIcons() {
        int c = 0;
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            if (((DeIcon) it.next()).visible) {
                c++;
            }
        }
        return c;
    }

    void enableIcon(int action) {
        getIcon(action).visible = true;
    }

    DeIcon getIcon(int action) {
        Iterator it = this.icons.iterator();
        while (it.hasNext()) {
            DeIcon icon = (DeIcon) it.next();
            if (icon.action == action) {
                return icon;
            }
        }
        return null;
    }

    public boolean onMouseWheelScrolled(int amount) {
        if (amount == 1) {
            this.scrollEngineYio.giveImpulse(0.02d * ((double) GraphicsYio.width));
        } else if (amount == -1) {
            this.scrollEngineYio.giveImpulse(-0.02d * ((double) GraphicsYio.width));
        }
        return true;
    }

    public void setTouchable(boolean touchable) {
    }

    public boolean isButton() {
        return false;
    }

    public boolean needToRenderInternalBackground() {
        return this.scrollEngineYio.isOverTop() || this.scrollEngineYio.isBelowBottom();
    }

    public void setPosition(RectangleYio position) {
        this.position.setBy(position);
        onPositionChanged();
    }

    private void onPositionChanged() {
        updateMetrics();
        this.scrollEngineYio.setSlider(0.0d, this.position.height - ((double) this.itemHeight));
        updateScrollEngineLimits();
    }

    public MenuRender getRenderSystem() {
        return MenuRender.renderDiplomacyElement;
    }
}
