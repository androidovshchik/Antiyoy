package io.androidovshchik.antiyoy.menu.scenes.editor;

import yio.tro.antiyoy.gameplay.rules.GameRules;
import yio.tro.antiyoy.menu.ButtonYio;
import yio.tro.antiyoy.menu.CheckButtonYio;
import yio.tro.antiyoy.menu.MenuControllerYio;
import yio.tro.antiyoy.menu.behaviors.Reaction;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class SceneEditorChecks extends AbstractEditorPanel {
    private double bSize;
    private ButtonYio basePanel;
    private double bottom;
    private CheckButtonYio chkDiplomacy;
    private CheckButtonYio chkFog;
    private double pHeight;
    private final Reaction rbSaveValues = new C01411();
    private double yOffset;

    class C01411 extends Reaction {
        C01411() {
        }

        public void perform(ButtonYio buttonYio) {
            SceneEditorChecks.this.saveValues();
        }
    }

    public SceneEditorChecks(MenuControllerYio menuControllerYio) {
        super(menuControllerYio);
        initMetrics();
    }

    private void initMetrics() {
        this.bottom = SceneEditorInstruments.ICON_SIZE;
        this.pHeight = 0.25d;
        this.yOffset = 0.02d;
        this.bSize = 0.06d;
    }

    public void create() {
        createBasePanel();
        createCheckButtons();
        loadValues();
    }

    private void createCheckButtons() {
        double hSize = GraphicsYio.convertToHeight(0.045d);
        double chkX = 0.88d - 0.045d;
        double chkY = this.pHeight - 0.008d;
        double delta = hSize + SceneEditorInstruments.ICON_SIZE;
        this.chkFog = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 921);
        this.chkFog.setTouchPosition(generateRectangle(0.05d, chkY - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkFog.setAnimation(6);
        this.chkFog.setReaction(this.rbSaveValues);
        chkY -= delta;
        this.chkDiplomacy = CheckButtonYio.getCheckButton(this.menuControllerYio, generateSquare(chkX, chkY - (hSize / 2.0d), hSize), 922);
        this.chkDiplomacy.setTouchPosition(generateRectangle(0.05d, chkY - (1.5d * hSize), 0.9d, hSize * 3.0d));
        this.chkDiplomacy.setAnimation(6);
        this.chkDiplomacy.setReaction(this.rbSaveValues);
        chkY -= delta;
    }

    private void saveValues() {
        GameRules.editorFog = this.chkFog.isChecked();
        GameRules.editorDiplomacy = this.chkDiplomacy.isChecked();
    }

    private void loadValues() {
        this.chkFog.setChecked(GameRules.editorFog);
        this.chkDiplomacy.setChecked(GameRules.editorDiplomacy);
    }

    private void createBasePanel() {
        this.basePanel = this.buttonFactory.getButton(generateRectangle(0.0d, this.bottom, 1.0d, this.pHeight), 920, null);
        if (this.basePanel.notRendered()) {
            this.basePanel.cleatText();
            this.basePanel.addEmptyLines(1);
            this.basePanel.addTextLine(getString("fog_of_war"));
            this.basePanel.addEmptyLines(1);
            this.basePanel.addTextLine(getString("diplomacy"));
            this.basePanel.addEmptyLines(1);
            this.basePanel.setTextOffset(0.07f * GraphicsYio.width);
            this.basePanel.loadCustomBackground("gray_pixel.png");
            this.basePanel.setIgnorePauseResume(true);
            this.menuControllerYio.buttonRenderer.renderButton(this.basePanel);
        }
        this.basePanel.setTouchable(false);
        this.basePanel.setAnimation(6);
        this.basePanel.disableTouchAnimation();
        this.basePanel.enableRectangularMask();
        this.basePanel.setShadow(true);
    }

    public void hide() {
        destroyByIndex(920, 929);
        this.chkFog.destroy();
        this.chkDiplomacy.destroy();
    }

    public boolean isCurrentlyOpened() {
        return this.basePanel.appearFactor.get() == 1.0f;
    }
}
