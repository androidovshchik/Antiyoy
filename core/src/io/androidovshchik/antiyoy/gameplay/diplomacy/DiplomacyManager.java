package io.androidovshchik.antiyoy.gameplay.diplomacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.FieldController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.menu.SingleMessages;
import io.androidovshchik.antiyoy.menu.diplomacy_element.DiplomacyElement;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.object_pool.ObjectPoolYio;

public class DiplomacyManager {
    public ArrayList<DiplomaticContract> contracts = new ArrayList();
    public ArrayList<DiplomaticCooldown> cooldowns = new ArrayList();
    public ArrayList<DiplomaticEntity> entities = new ArrayList();
    FieldController fieldController;
    public DiplomaticLog log = new DiplomaticLog(this);
    ObjectPoolYio<DiplomaticContract> poolContracts;
    ObjectPoolYio<DiplomaticCooldown> poolCooldowns;
    ObjectPoolYio<DiplomaticEntity> poolEntities;

    class C01001 extends ObjectPoolYio<DiplomaticEntity> {
        C01001() {
        }

        public DiplomaticEntity makeNewObject() {
            return new DiplomaticEntity(DiplomacyManager.this);
        }
    }

    class C01012 extends ObjectPoolYio<DiplomaticContract> {
        C01012() {
        }

        public DiplomaticContract makeNewObject() {
            return new DiplomaticContract();
        }
    }

    class C01023 extends ObjectPoolYio<DiplomaticCooldown> {
        C01023() {
        }

        public DiplomaticCooldown makeNewObject() {
            return new DiplomaticCooldown();
        }
    }

    public DiplomacyManager(FieldController fieldController) {
        this.fieldController = fieldController;
        initPools();
    }

    private void initPools() {
        this.poolEntities = new C01001();
        this.poolContracts = new C01012();
        this.poolCooldowns = new C01023();
    }

    public void onEndCreation() {
        if (GameRules.diplomacyEnabled) {
            updateEntities();
            clearContracts();
            clearCooldowns();
            this.log.clear();
            DiplomacyElement diplomacyElement = Scenes.sceneDiplomacy.diplomacyElement;
            if (diplomacyElement != null) {
                diplomacyElement.updateAll();
            }
        }
    }

    private void clearCooldowns() {
        Iterator it = this.cooldowns.iterator();
        while (it.hasNext()) {
            this.poolCooldowns.add((DiplomaticCooldown) it.next());
        }
        this.cooldowns.clear();
    }

    private void clearContracts() {
        Iterator it = this.contracts.iterator();
        while (it.hasNext()) {
            this.poolContracts.add((DiplomaticContract) it.next());
        }
        this.contracts.clear();
    }

    public void checkForSingleMessage() {
        if (!SingleMessages.diplomacyWinConditions) {
            SingleMessages.diplomacyWinConditions = true;
            SingleMessages.save();
            Scenes.sceneDiplomacy.create();
            Scenes.sceneDiplomacy.hide();
            Scenes.sceneDipMessage.create();
            Scenes.sceneDipMessage.dialog.setMessage("win_conditions", "diplomatic_win_conditions");
        }
    }

    public void onDiplomacyButtonPressed() {
        this.fieldController.gameController.selectionController.deselectAll();
        if (this.log.hasSomethingToRead()) {
            Scenes.sceneDiplomaticLog.create();
        } else {
            Scenes.sceneDiplomacy.create();
        }
    }

    void onEntityDied(DiplomaticEntity deadEntity) {
        dropEntityRelationsToDefault(deadEntity);
        cancelContractsWithEntity(deadEntity);
    }

    private void dropEntityRelationsToDefault(DiplomaticEntity deadEntity) {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            DiplomaticEntity entity = (DiplomaticEntity) it.next();
            if (entity != deadEntity) {
                makeNeutral(deadEntity, entity);
            }
        }
    }

    private void cancelContractsWithEntity(DiplomaticEntity deadEntity) {
        for (int i = this.contracts.size() - 1; i >= 0; i--) {
            DiplomaticContract diplomaticContract = (DiplomaticContract) this.contracts.get(i);
            if (diplomaticContract.contains(deadEntity)) {
                removeContract(diplomaticContract);
            }
        }
    }

    private void updateEntities() {
        clearEntities();
        for (int color = 0; color < GameRules.colorNumber; color++) {
            DiplomaticEntity next = (DiplomaticEntity) this.poolEntities.getNext();
            next.setColor(color);
            next.updateCapitalName();
            next.setHuman(this.fieldController.gameController.isPlayerTurn(color));
            this.entities.add(next);
        }
        initEntityRelations();
    }

    private void randomizeRelations() {
        int size = this.entities.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                double randomValue = YioGdxGame.random.nextDouble();
                if (randomValue < 0.4d) {
                    makeFriends((DiplomaticEntity) this.entities.get(i), (DiplomaticEntity) this.entities.get(j));
                } else if (randomValue < 0.55d) {
                    makeEnemies((DiplomaticEntity) this.entities.get(i), (DiplomaticEntity) this.entities.get(j));
                }
            }
        }
    }

    private void initEntityRelations() {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            ((DiplomaticEntity) it.next()).initRelations();
        }
    }

    private void clearEntities() {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            this.poolEntities.add((DiplomaticEntity) it.next());
        }
        this.entities.clear();
    }

    public int getDiplomaticWinner() {
        if (!isThereAtLeastOneDiplomaticWinner()) {
            return -1;
        }
        DiplomaticEntity bestEntity = null;
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            DiplomaticEntity entity = (DiplomaticEntity) it.next();
            if (entity.hasOnlyFriends() && (bestEntity == null || entity.getNumberOfLands() > bestEntity.getNumberOfLands())) {
                bestEntity = entity;
            }
        }
        return bestEntity.color;
    }

    boolean isThereAtLeastOneDiplomaticWinner() {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            if (((DiplomaticEntity) it.next()).hasOnlyFriends()) {
                return true;
            }
        }
        return false;
    }

    public void onUserClickedContextIcon(int selectedColor, int action) {
        DiplomaticEntity mainEntity = getMainEntity();
        DiplomaticEntity selectedEntity = getEntity(selectedColor);
        switch (action) {
            case 0:
                requestBetterRelations(mainEntity, selectedEntity);
                return;
            case 1:
                Scenes.sceneConfirmDislike.create();
                Scenes.sceneConfirmDislike.dialog.setSelectedEntity(selectedEntity);
                return;
            case 2:
                Scenes.sceneConfirmBlackMarkDialog.create();
                Scenes.sceneConfirmBlackMarkDialog.dialog.setSelectedEntity(selectedEntity);
                return;
            case 3:
                Scenes.sceneDipInfoDialog.create();
                Scenes.sceneDipInfoDialog.dialog.setEntities(mainEntity, selectedEntity);
                return;
            default:
                return;
        }
    }

    public void onUserRequestedBlackMark(DiplomaticEntity selectedEntity) {
        makeBlackMarked(getMainEntity(), selectedEntity);
    }

    public void makeBlackMarked(DiplomaticEntity initiator, DiplomaticEntity entity) {
        this.log.addMessage(DipMessageType.black_marked, initiator, entity);
        addContract(2, initiator, entity);
        onRelationsChanged();
    }

    public void requestedFriendship(DiplomaticEntity sender, DiplomaticEntity recipient) {
        if (getMainEntity() == sender) {
            this.log.addMessage(DipMessageType.friendship_proposal, sender, recipient);
            showLetterSentNotification();
        } else if (recipient.acceptsFriendsRequest(sender)) {
            makeFriends(sender, recipient);
        }
    }

    public void onUserRequestedToMakeRelationsWorse(DiplomaticEntity selectedEntity) {
        onEntityRequestedToMakeRelationsWorse(getMainEntity(), selectedEntity);
    }

    void onEntityRequestedToMakeRelationsWorse(DiplomaticEntity initiator, DiplomaticEntity entity) {
        int previousRelation = initiator.getRelation(entity);
        if (previousRelation == 1) {
            punishFriendshipTraitor(initiator, entity);
        }
        requestWorseRelations(initiator, entity);
        int relation = initiator.getRelation(entity);
        if (relation != previousRelation && relation == 2) {
            onWarStarted(initiator, entity);
        }
    }

    private void punishFriendshipTraitor(DiplomaticEntity initiator, DiplomaticEntity entity) {
        if (initiator.getStateBalance() > 0) {
            addContract(3, initiator, entity);
            onRelationsChanged();
        }
    }

    public void onEntityRequestedToStopWar(DiplomaticEntity initiator, DiplomaticEntity entity) {
        addContract(1, initiator, entity);
        makeNeutral(initiator, entity);
        initiator.pay(calculatePayToStopWar(initiator, entity));
    }

    public void onUserRequestedToStopWar(DiplomaticEntity user, DiplomaticEntity recipient) {
        this.log.addMessage(DipMessageType.stop_war, user, recipient);
        showLetterSentNotification();
    }

    public void onContractExpired(DiplomaticContract contract) {
        removeContract(contract);
        if (contract.type == 0) {
            int relation = contract.one.getRelation(contract.two);
            this.log.addMessage(DipMessageType.friendship_ended, contract.one, contract.two);
            if (relation == 1) {
                makeNeutral(contract.one, contract.two);
            }
        }
    }

    public void updateEntityAliveStatus(int color) {
        if (GameRules.diplomacyEnabled) {
            DiplomaticEntity entity = getEntity(color);
            if (entity != null) {
                entity.updateAlive();
            }
        }
    }

    public void onTurnStarted() {
        if (GameRules.diplomacyEnabled) {
            this.log.checkToClearAbuseMessages();
            DiplomacyElement diplomacyElement = Scenes.sceneDiplomacy.diplomacyElement;
            if (diplomacyElement != null) {
                diplomacyElement.onTurnStarted();
            }
            if (this.fieldController.gameController.isPlayerTurn()) {
                onHumanTurnStarted();
            } else {
                onAiTurnStarted();
            }
            moveCooldowns();
        }
    }

    private void onAiTurnStarted() {
        aiProcessMessages();
        if (YioGdxGame.random.nextInt(8) == 0) {
            performAiToHumanFriendshipProposal();
        }
        if (YioGdxGame.random.nextInt(4) == 0) {
            performAiToHumanBlackMark();
        }
    }

    private void aiProcessMessages() {
        DiplomaticEntity mainEntity = getMainEntity();
        for (int i = this.log.messages.size() - 1; i >= 0; i--) {
            DiplomaticMessage message = (DiplomaticMessage) this.log.messages.get(i);
            if (message.recipient == mainEntity) {
                switch (message.type) {
                    case friendship_proposal:
                        if (isFriendshipPossible(message.sender, message.recipient)) {
                            makeFriends(message.sender, message.recipient);
                            break;
                        }
                        break;
                    case stop_war:
                        onEntityRequestedToStopWar(message.sender, message.recipient);
                        break;
                }
                this.log.removeMessage(message);
            }
        }
    }

    private void moveCooldowns() {
        if (this.fieldController.gameController.turn == 0) {
            Iterator it = this.cooldowns.iterator();
            while (it.hasNext()) {
                ((DiplomaticCooldown) it.next()).decreaseCounter();
            }
            checkToRemoveCooldowns();
        }
    }

    private void checkToRemoveCooldowns() {
        if (this.fieldController.gameController.turn == 0) {
            for (int i = this.cooldowns.size() - 1; i >= 0; i--) {
                DiplomaticCooldown cooldown = (DiplomaticCooldown) this.cooldowns.get(i);
                if (cooldown.isReady()) {
                    removeCooldown(cooldown);
                }
            }
        }
    }

    private void removeCooldown(DiplomaticCooldown cooldown) {
        this.cooldowns.remove(cooldown);
        this.poolCooldowns.addWithCheck(cooldown);
    }

    public boolean checkForStopWarCooldown(DiplomaticEntity one, DiplomaticEntity two) {
        Iterator it = this.cooldowns.iterator();
        while (it.hasNext()) {
            DiplomaticCooldown cooldown = (DiplomaticCooldown) it.next();
            if (cooldown.type == 0 && cooldown.contains(one) && cooldown.contains(two) && !cooldown.isReady()) {
                return false;
            }
        }
        return true;
    }

    private void onHumanTurnStarted() {
    }

    public void performAiToHumanBlackMark() {
        DiplomaticEntity aiEntity = findAiEntityThatIsCloseToWin();
        if (aiEntity != null) {
            DiplomaticEntity randomHumanEntity = getRandomHumanEntity();
            if (randomHumanEntity != null && aiEntity.getRelation(randomHumanEntity) != 1 && !randomHumanEntity.isBlackMarkedWith(aiEntity)) {
                this.log.addMessage(DipMessageType.black_marked, aiEntity, randomHumanEntity);
                makeBlackMarked(aiEntity, randomHumanEntity);
            }
        }
    }

    public boolean performAiToHumanFriendshipProposal() {
        DiplomaticEntity humanEntity = getRandomHumanEntity();
        if (humanEntity == null || humanEntity.isOneFriendAwayFromDiplomaticVictory() || !humanEntity.alive) {
            return false;
        }
        for (int i = 0; i < 25; i++) {
            DiplomaticEntity randomEntity = getRandomEntity();
            if (randomEntity.alive && !randomEntity.isHuman() && !randomEntity.isOneFriendAwayFromDiplomaticVictory() && humanEntity.getRelation(randomEntity) == 0 && humanEntity.acceptsFriendsRequest(randomEntity)) {
                this.log.addMessage(DipMessageType.friendship_proposal, randomEntity, humanEntity);
                return true;
            }
        }
        return false;
    }

    public DiplomaticEntity findAiEntityThatIsCloseToWin() {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            DiplomaticEntity entity = (DiplomaticEntity) it.next();
            if (!entity.isHuman() && entity.alive && entity.isOneFriendAwayFromDiplomaticVictory()) {
                return entity;
            }
        }
        return null;
    }

    public void onTurnEnded() {
        if (GameRules.diplomacyEnabled) {
            DiplomaticEntity entity = getEntity(this.fieldController.gameController.turn);
            entity.updateAlive();
            checkToChangeRelations();
            this.log.removeMessagesByRecipient(entity);
            if (this.fieldController.gameController.turn == 0) {
                onFirstPlayerTurnEnded();
            }
        }
    }

    private void checkToChangeRelations() {
        DiplomaticEntity mainEntity = getMainEntity();
        if (!mainEntity.isHuman()) {
            mainEntity.thinkAboutChangingRelations();
        }
    }

    void onFirstPlayerTurnEnded() {
        for (int i = this.contracts.size() - 1; i >= 0; i--) {
            ((DiplomaticContract) this.contracts.get(i)).onFirstPlayerTurnEnded();
        }
        DiplomacyElement diplomacyElement = Scenes.sceneDiplomacy.diplomacyElement;
        if (diplomacyElement != null) {
            diplomacyElement.onFirstPlayerTurnEnded();
        }
    }

    public boolean canUnitAttackHex(int unitStrength, int unitColor, Hex hex) {
        if (isHexSingle(hex)) {
            return true;
        }
        DiplomaticEntity one = getEntity(unitColor);
        DiplomaticEntity two = getEntity(hex.colorIndex);
        if (one == null || two == null || one.getRelation(two) == 2) {
            return true;
        }
        return false;
    }

    private boolean isHexSingle(Hex hex) {
        for (int dir = 0; dir < 6; dir++) {
            Hex adjacentHex = hex.getAdjacentHex(dir);
            if (adjacentHex != null && adjacentHex != this.fieldController.emptyHex && adjacentHex.sameColor(hex)) {
                return false;
            }
        }
        return true;
    }

    public boolean isProvinceAllowedToBuildUnit(Province province, int unitStrength) {
        if (getEntity(province.getColor()).isAtWar()) {
            return true;
        }
        if (unitStrength > 1) {
            return false;
        }
        if (numberOfPeasantsInProvince(province) > 4) {
            return false;
        }
        return true;
    }

    private int numberOfPeasantsInProvince(Province province) {
        int c = 0;
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit() && hex.unit.strength == 1) {
                c++;
            }
        }
        return c;
    }

    public int calculateDotationsForFriendship(DiplomaticEntity initiator, DiplomaticEntity entity) {
        int money1 = entity.getStateBalance() * entity.getNumberOfFriends();
        int money2 = initiator.getStateBalance() * initiator.getNumberOfFriends();
        int cutValue = (int) (0.2d * ((double) ((float) Math.max(money1, money2))));
        int difference = Math.abs(money1 - money2);
        if (difference < cutValue || difference < 5) {
            return 0;
        }
        return money1 <= money2 ? -cutValue : cutValue;
    }

    public int getProvinceDotations(Province province) {
        return (int) (province.getIncomeCoefficient() * ((float) getEntity(province.getColor()).getStateDotations()));
    }

    public DiplomaticEntity getRandomHumanEntity() {
        if (!isAtLeastOneHumanEntity()) {
            return null;
        }
        DiplomaticEntity randomEntity;
        do {
            randomEntity = getRandomEntity();
        } while (!randomEntity.isHuman());
        return randomEntity;
    }

    private boolean isAtLeastOneHumanEntity() {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            if (((DiplomaticEntity) it.next()).isHuman()) {
                return true;
            }
        }
        return false;
    }

    public DiplomaticEntity getMainEntity() {
        return getEntity(this.fieldController.gameController.turn);
    }

    public DiplomaticEntity getRandomEntity() {
        return (DiplomaticEntity) this.entities.get(YioGdxGame.random.nextInt(this.entities.size()));
    }

    public void showLetterSentNotification() {
        Scenes.sceneNotification.showNotification("letter_sent");
    }

    void requestBetterRelations(DiplomaticEntity initiator, DiplomaticEntity two) {
        int relation = initiator.getRelation(two);
        if (relation == 2) {
            if (canWarBeStopped(initiator, two)) {
                Scenes.sceneStopWarDialog.create();
                Scenes.sceneStopWarDialog.dialog.setEntities(initiator, two);
            } else {
                Scenes.sceneDipMessage.create();
                Scenes.sceneDipMessage.dialog.setMessage(two.capitalName, "refuse_stop_war");
            }
        }
        if (relation != 0) {
            return;
        }
        if (isFriendshipPossible(initiator, two)) {
            Scenes.sceneFriendshipDialog.create();
            Scenes.sceneFriendshipDialog.dialog.setEntities(initiator, two);
            return;
        }
        Scenes.sceneDipMessage.create();
        Scenes.sceneDipMessage.dialog.setMessage(two.capitalName, "refuse_friendship");
    }

    boolean isFriendshipPossible(DiplomaticEntity one, DiplomaticEntity two) {
        if (!one.isOneFriendAwayFromDiplomaticVictory() && !two.isOneFriendAwayFromDiplomaticVictory() && one.acceptsFriendsRequest(two) && two.acceptsFriendsRequest(one)) {
            return true;
        }
        return false;
    }

    boolean canWarBeStopped(DiplomaticEntity one, DiplomaticEntity two) {
        if (checkForStopWarCooldown(one, two) && one.acceptsToStopWar(two) && two.acceptsToStopWar(one)) {
            return true;
        }
        return false;
    }

    void requestWorseRelations(DiplomaticEntity initiator, DiplomaticEntity two) {
        int relation = two.getRelation(initiator);
        if (relation == 1) {
            this.log.addMessage(DipMessageType.friendship_canceled, initiator, two);
            makeNeutral(two, initiator);
        }
        if (relation == 0) {
            this.log.addMessage(DipMessageType.war_declaration, initiator, two);
            makeEnemies(initiator, two);
        }
    }

    private void onWarStarted(DiplomaticEntity initiator, DiplomaticEntity one) {
        punishAggressor(initiator, one);
        addCooldown(0, 10, initiator, one);
    }

    private void punishAggressor(DiplomaticEntity initiator, DiplomaticEntity one) {
        for (Entry<DiplomaticEntity, Integer> entry : initiator.relations.entrySet()) {
            DiplomaticEntity entity = (DiplomaticEntity) entry.getKey();
            if (one.isFriendTo(entity)) {
                requestWorseRelations(initiator, entity);
            }
        }
    }

    DiplomaticCooldown addCooldown(int type, int counter, DiplomaticEntity one, DiplomaticEntity two) {
        DiplomaticCooldown next = (DiplomaticCooldown) this.poolCooldowns.getNext();
        next.setType(type);
        next.setCounter(counter);
        next.setOne(one);
        next.setTwo(two);
        this.cooldowns.add(next);
        if (this.cooldowns.size() > 25) {
            this.cooldowns.remove(0);
        }
        return next;
    }

    DiplomaticContract addContract(int contractType, DiplomaticEntity initiator, DiplomaticEntity entity) {
        DiplomaticContract next = (DiplomaticContract) this.poolContracts.getNext();
        next.setOne(entity);
        next.setTwo(initiator);
        next.setType(contractType);
        next.setDotations(getDotationsByContractType(contractType, initiator, entity));
        next.setExpireCountDown(DiplomaticContract.getDurationByType(contractType));
        this.contracts.add(next);
        return next;
    }

    int getDotationsByContractType(int contractType, DiplomaticEntity initiator, DiplomaticEntity two) {
        switch (contractType) {
            case 0:
                return calculateDotationsForFriendship(initiator, two);
            case 1:
                return calculateReparations(initiator, two);
            case 3:
                return calculateTraitorFine(initiator);
            default:
                return 0;
        }
    }

    public int calculateTraitorFine(DiplomaticEntity initiator) {
        return (-initiator.getStateBalance()) / 3;
    }

    void removeContract(int contractType, DiplomaticEntity one, DiplomaticEntity two) {
        DiplomaticContract contract = findContract(contractType, one, two);
        if (contract != null) {
            removeContract(contract);
        }
    }

    private void removeContract(DiplomaticContract contract) {
        this.poolContracts.add(contract);
        this.contracts.remove(contract);
    }

    public int calculateReparations(DiplomaticEntity initiator, DiplomaticEntity two) {
        int stateBalance = initiator.getStateBalance();
        if (stateBalance >= 5 && two.getStateBalance() >= 10) {
            return (-stateBalance) / 2;
        }
        return 0;
    }

    public int calculatePayToStopWar(DiplomaticEntity initiator, DiplomaticEntity two) {
        return (int) Math.min(0.6d * ((double) initiator.getStateFullMoney()), 0.5d * ((double) two.getStateFullMoney()));
    }

    public DiplomaticContract findContract(int type, DiplomaticEntity one, DiplomaticEntity two) {
        Iterator it = this.contracts.iterator();
        while (it.hasNext()) {
            DiplomaticContract contract = (DiplomaticContract) it.next();
            if (contract.equals(one, two, type)) {
                return contract;
            }
        }
        return null;
    }

    public void setRelation(DiplomaticEntity one, DiplomaticEntity two, int relation) {
        switch (relation) {
            case 0:
                makeNeutral(one, two);
                return;
            case 1:
                makeFriends(one, two);
                return;
            case 2:
                makeEnemies(one, two);
                return;
            default:
                return;
        }
    }

    public void makeFriends(DiplomaticEntity initiator, DiplomaticEntity entity) {
        if (initiator.alive && entity.alive && initiator.getRelation(entity) != 1) {
            addContract(0, initiator, entity);
            removeContract(1, initiator, entity);
            initiator.setRelation(entity, 1);
            entity.setRelation(initiator, 1);
            onRelationsChanged();
        }
    }

    public void makeNeutral(DiplomaticEntity one, DiplomaticEntity two) {
        if (one.alive && two.alive && one.getRelation(two) != 0) {
            one.setRelation(two, 0);
            two.setRelation(one, 0);
            removeContract(0, one, two);
            onRelationsChanged();
        }
    }

    public boolean makeEnemies(DiplomaticEntity initiator, DiplomaticEntity entity) {
        if (!initiator.alive || !entity.alive || initiator.getRelation(entity) == 2) {
            return false;
        }
        initiator.setRelation(entity, 2);
        entity.setRelation(initiator, 2);
        removeContract(0, initiator, entity);
        removeContract(1, initiator, entity);
        onRelationsChanged();
        return true;
    }

    public void onRelationsChanged() {
        DiplomacyElement diplomacyElement = Scenes.sceneDiplomacy.diplomacyElement;
        if (diplomacyElement != null) {
            diplomacyElement.onRelationsChanged();
        }
        if (GameRules.fogOfWarEnabled) {
            this.fieldController.fogOfWarManager.updateFog();
        }
    }

    public DiplomaticEntity getEntity(int color) {
        Iterator it = this.entities.iterator();
        while (it.hasNext()) {
            DiplomaticEntity entity = (DiplomaticEntity) it.next();
            if (entity.color == color) {
                return entity;
            }
        }
        return null;
    }

    public void showCooldownsInConsole(int colorFilter) {
        System.out.println();
        System.out.println("DiplomacyManager.showCooldownsInConsole");
        DiplomaticEntity entity = getEntity(colorFilter);
        Iterator it = this.cooldowns.iterator();
        while (it.hasNext()) {
            DiplomaticCooldown cooldown = (DiplomaticCooldown) it.next();
            if (entity == null || cooldown.contains(entity)) {
                System.out.println("- " + cooldown);
            }
        }
    }

    public void showContractsInConsole(int colorFilter) {
        System.out.println();
        System.out.println("DiplomacyManager.showContractsInConsole");
        DiplomaticEntity entity = getEntity(colorFilter);
        Iterator it = this.contracts.iterator();
        while (it.hasNext()) {
            DiplomaticContract contract = (DiplomaticContract) it.next();
            if (entity == null || contract.contains(entity)) {
                System.out.println("- " + contract);
            }
        }
    }
}
