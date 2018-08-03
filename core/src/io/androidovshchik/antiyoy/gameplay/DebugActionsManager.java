package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.campaign.CampaignProgressManager;
import io.androidovshchik.antiyoy.gameplay.diplomacy.DiplomacyManager;
import io.androidovshchik.antiyoy.gameplay.name_generator.CityNameGenerator;
import io.androidovshchik.antiyoy.gameplay.replays.ReplaySaveSystem;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.ButtonYio;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;

public class DebugActionsManager {
    private final GameController gameController;

    public DebugActionsManager(GameController gameController) {
        this.gameController = gameController;
    }

    public void debugActions() {
        doShowSuspiciousStuff();
    }

    private void doShowSuspiciousStuff() {
        System.out.println();
        System.out.println("DebugActionsManager.doShowSuspiciousStuff");
        System.out.println("cooldowns.size() = " + this.gameController.fieldController.diplomacyManager.cooldowns.size());
        this.gameController.snapshotManager.showInConsole();
    }

    private void doTestStringBuilder() {
        int i;
        StringBuilder builder = new StringBuilder();
        for (i = 0; i < 5; i++) {
            builder.append(i).append(" ");
        }
        String one = builder.toString();
        builder.setLength(0);
        for (i = 0; i < 5; i++) {
            builder.append(i * 3).append(" ");
        }
        String two = builder.toString();
        System.out.println();
        System.out.println("DebugActionsManager.doTestStringBuilder");
        System.out.println("one = " + one);
        System.out.println("two = " + two);
    }

    private void doTakeSnapshot() {
        this.gameController.takeSnapshot();
    }

    private DiplomacyManager getDiplomacyManager() {
        return this.gameController.fieldController.diplomacyManager;
    }

    private void doShowDiplomaticCooldownsInConsole() {
        getDiplomacyManager().showCooldownsInConsole(this.gameController.turn);
    }

    private void doGetSomeFriendshipProposals() {
        getDiplomacyManager().performAiToHumanFriendshipProposal();
    }

    private void doShowTurnStartDialog() {
        Scenes.sceneTurnStartDialog.create();
        Scenes.sceneTurnStartDialog.dialog.setColor(0);
    }

    private void doShowGameRules() {
        System.out.println();
        System.out.println("DebugActionsManager.doShowGameRules");
        System.out.println("GameRules.campaignMode = " + GameRules.campaignMode);
        System.out.println("CampaignProgressManager.getInstance().currentLevelIndex = " + CampaignProgressManager.getInstance().currentLevelIndex);
        System.out.println("GameRules.slayRules = " + GameRules.slayRules);
        System.out.println("GameRules.userLevelMode = " + GameRules.userLevelMode);
        System.out.println("GameRules.editorFog = " + GameRules.editorFog);
        System.out.println("GameRules.editorDiplomacy = " + GameRules.editorDiplomacy);
    }

    private void doShowDiplomaticMessage() {
        Scenes.sceneDipMessage.create();
        Scenes.sceneDipMessage.dialog.setMessage("Message", "HJdas hjashdk ahsdkj aha hsdja hkjas hkash jkdah kjash dkjsahd kah kjah dkjah dkjhaskjd hsk hhsdk asda");
    }

    private void doShowDiplomaticContracts() {
        getDiplomacyManager().showContractsInConsole(0);
    }

    private void doGenerateMultipleCityNames() {
        int i;
        System.out.println();
        System.out.println("DebugActionsManager.generateMultipleCityNames");
        CityNameGenerator instance = CityNameGenerator.getInstance();
        ArrayList<Hex> activeHexes = this.gameController.fieldController.activeHexes;
        for (i = 0; i < 10; i++) {
            System.out.println("- " + instance.generateName((Hex) activeHexes.get(YioGdxGame.random.nextInt(activeHexes.size()))));
        }
        ArrayList<String> allNames = new ArrayList();
        Iterator it = activeHexes.iterator();
        while (it.hasNext()) {
            allNames.add(instance.generateName((Hex) it.next()));
        }
        int duplicates = 0;
        for (i = 0; i < allNames.size(); i++) {
            for (int j = i + 1; j < allNames.size(); j++) {
                if (((String) allNames.get(i)).equals(allNames.get(j))) {
                    duplicates++;
                }
            }
        }
        boolean hasDuplicates = duplicates > 0;
        System.out.println("hasDuplicates = " + hasDuplicates);
        if (hasDuplicates) {
            System.out.println("duplicates = " + duplicates);
        }
    }

    private void doShowNotification() {
        Scenes.sceneNotification.showNotification("debug notification");
    }

    private void doShowSnapshotsInConsole() {
        this.gameController.snapshotManager.showInConsole();
    }

    private void doShowRuleset() {
        System.out.println();
        System.out.println("DebugActionsManager.doShowRuleset");
        System.out.println("GameRules.slayRules = " + GameRules.slayRules);
        System.out.println("simpleName = " + this.gameController.ruleset.getClass().getSimpleName());
    }

    private void checkIfSomeProvincesAreDoubledInList() {
        ArrayList<Province> provinces = this.gameController.fieldController.provinces;
        for (int i = 0; i < provinces.size(); i++) {
            for (int j = 0; j < provinces.size(); j++) {
                Province A = (Province) provinces.get(i);
                Province B = (Province) provinces.get(j);
                if (i != j && A.equals(B)) {
                    System.out.println("found shit!");
                }
            }
        }
    }

    private void doReplaySystemStuff() {
        ReplaySaveSystem instance = ReplaySaveSystem.getInstance();
        instance.clearKeys();
        instance.saveReplay(this.gameController.replayManager.getReplay());
    }

    private void doShowReplayManager() {
        this.gameController.replayManager.showInConsole();
    }

    private void doShowSnapshots() {
        this.gameController.snapshotManager.showInConsole();
    }

    private void doShowStatistics() {
        this.gameController.matchStatistics.showInConsole();
    }

    private void doGiveEverybodyLotOfMoney() {
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            province.money += ButtonYio.DEFAULT_TOUCH_DELAY;
        }
    }

    private void doShowAllProvincesMoney() {
        System.out.println("DebugActionsManager.doShowAllProvincesMoney:");
        Iterator it = this.gameController.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            System.out.println(this.gameController.fieldController.getColorName(province.getColor()) + ": " + province.money + " + " + province.getBalance());
        }
        System.out.println();
    }

    private void doShowColorStuff() {
        System.out.println();
        System.out.println("FieldController.NEUTRAL_LANDS_INDEX = " + FieldController.NEUTRAL_LANDS_INDEX);
        System.out.println("colorIndexViewOffset = " + this.gameController.colorIndexViewOffset);
        System.out.println("GameRules.colorNumber = " + GameRules.colorNumber);
        for (int i = 0; i < GameRules.colorNumber; i++) {
            System.out.println(i + " -> " + this.gameController.ruleset.getColorIndexWithOffset(i));
        }
    }

    private void doForceWin() {
        doCaptureRandomHexes();
    }

    private void doCaptureRandomHexes() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (this.gameController.getRandom().nextDouble() > 0.5d) {
                this.gameController.fieldController.setHexColor(activeHex, 0);
            }
        }
    }

    private void doShowActiveHexesString() {
        System.out.println("" + this.gameController.getGameSaver().getActiveHexesString());
    }
}
