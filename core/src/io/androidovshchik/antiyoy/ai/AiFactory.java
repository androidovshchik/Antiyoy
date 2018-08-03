package io.androidovshchik.antiyoy.ai;

import java.util.ArrayList;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;

public class AiFactory {
    ArrayList<ArtificialIntelligence> aiList;
    private final GameController gameController;

    public AiFactory(GameController gameController) {
        this.gameController = gameController;
    }

    public void createAiList(int difficulty) {
        this.aiList = this.gameController.getAiList();
        this.aiList.clear();
        for (int i = 0; i < GameRules.colorNumber; i++) {
            addAiToList(difficulty, i);
        }
    }

    private void addAiToList(int difficulty, int i) {
        switch (difficulty) {
            case 1:
                this.aiList.add(getNormalAi(i));
                return;
            case 2:
                this.aiList.add(getHardAi(i));
                return;
            case 3:
                this.aiList.add(getExpertAi(i));
                return;
            case 4:
                this.aiList.add(getBalancerAi(i));
                return;
            default:
                this.aiList.add(getEasyAi(i));
                return;
        }
    }

    private ArtificialIntelligence getBalancerAi(int i) {
        if (GameRules.slayRules) {
            return new AiBalancerSlayRules(this.gameController, i);
        }
        return new AiBalancerGenericRules(this.gameController, i);
    }

    private ArtificialIntelligence getExpertAi(int i) {
        if (GameRules.slayRules) {
            return new AiExpertSlayRules(this.gameController, i);
        }
        return new AiExpertGenericRules(this.gameController, i);
    }

    private ArtificialIntelligence getHardAi(int i) {
        if (GameRules.slayRules) {
            return new AiHardSlayRules(this.gameController, i);
        }
        return new AiHardGenericRules(this.gameController, i);
    }

    private ArtificialIntelligence getNormalAi(int i) {
        if (GameRules.slayRules) {
            return new AiNormalSlayRules(this.gameController, i);
        }
        return new AiNormalGenericRules(this.gameController, i);
    }

    private ArtificialIntelligence getEasyAi(int i) {
        return new AiEasy(this.gameController, i);
    }
}
