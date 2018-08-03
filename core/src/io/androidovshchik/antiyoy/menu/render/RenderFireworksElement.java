package io.androidovshchik.antiyoy.menu.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.Iterator;
import yio.tro.antiyoy.menu.FireworksElement.FeParticle;
import yio.tro.antiyoy.menu.FireworksElement.FireworksElement;
import yio.tro.antiyoy.menu.InterfaceElement;
import yio.tro.antiyoy.stuff.GraphicsYio;

public class RenderFireworksElement extends MenuRender {
    private FireworksElement fireworksElement;
    TextureRegion[] pTextures;

    public void loadTextures() {
        String[] names = new String[]{"man0", "man1", "man2", "man3", "grave", "house", "palm", "pine", "tower"};
        this.pTextures = new TextureRegion[names.length];
        for (int i = 0; i < names.length; i++) {
            this.pTextures[i] = GraphicsYio.loadTextureRegion("field_elements/" + names[i] + "_lowest.png", false);
        }
    }

    public void renderFirstLayer(InterfaceElement element) {
    }

    public void renderSecondLayer(InterfaceElement element) {
        this.fireworksElement = (FireworksElement) element;
        Iterator it = this.fireworksElement.particles.iterator();
        while (it.hasNext()) {
            renderSingleParticle((FeParticle) it.next());
        }
    }

    private void renderSingleParticle(FeParticle particle) {
        if (particle.viewRadius != 0.0f) {
            GraphicsYio.drawFromCenterRotated(this.batch, getParticleTexture(particle), (double) particle.position.f144x, (double) particle.position.f145y, (double) particle.viewRadius, particle.viewAngle);
        }
    }

    private TextureRegion getParticleTexture(FeParticle particle) {
        switch (particle.viewType) {
            case 1:
                return getGameView().manTextures[1].getLowest();
            case 2:
                return getGameView().manTextures[2].getLowest();
            case 3:
                return getGameView().manTextures[3].getLowest();
            case 4:
                return getGameView().graveTexture.getLowest();
            case 5:
                return getGameView().houseTexture.getLowest();
            case 6:
                return getGameView().palmTexture.getLowest();
            case 7:
                return getGameView().pineTexture.getLowest();
            case 8:
                return getGameView().towerTexture.getLowest();
            default:
                return getGameView().manTextures[0].getLowest();
        }
    }

    public void renderThirdLayer(InterfaceElement element) {
    }
}
