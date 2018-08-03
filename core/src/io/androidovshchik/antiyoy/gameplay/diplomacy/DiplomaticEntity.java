package io.androidovshchik.antiyoy.gameplay.diplomacy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import io.androidovshchik.antiyoy.YioGdxGame;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;
import io.androidovshchik.antiyoy.gameplay.name_generator.CityNameGenerator;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class DiplomaticEntity implements ReusableYio {
    public boolean alive;
    public String capitalName;
    public int color;
    DiplomacyManager diplomacyManager;
    public boolean human;
    public HashMap<DiplomaticEntity, Integer> relations = new HashMap();

    public DiplomaticEntity(DiplomacyManager diplomacyManager) {
        this.diplomacyManager = diplomacyManager;
    }

    public void reset() {
        this.color = -1;
        this.capitalName = null;
        this.relations.clear();
        this.human = false;
        this.alive = true;
    }

    void initRelations() {
        this.relations.clear();
        Iterator it = this.diplomacyManager.entities.iterator();
        while (it.hasNext()) {
            DiplomaticEntity entity = (DiplomaticEntity) it.next();
            if (entity != this) {
                this.relations.put(entity, Integer.valueOf(0));
            }
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isMain() {
        return this.color == this.diplomacyManager.fieldController.gameController.turn;
    }

    public boolean isHuman() {
        return this.human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public void setRelation(DiplomaticEntity entity, int relation) {
        this.relations.put(entity, Integer.valueOf(relation));
    }

    public int getRelation(DiplomaticEntity entity) {
        if (this.relations.containsKey(entity)) {
            return ((Integer) this.relations.get(entity)).intValue();
        }
        return -1;
    }

    public int getStateBalance() {
        int balance = 0;
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == this.color) {
                balance += province.getBalance();
            }
        }
        return balance;
    }

    public int getStateFullMoney() {
        int money = 0;
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == this.color) {
                money += province.money;
            }
        }
        return money;
    }

    public void pay(int amount) {
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == this.color) {
                province.money = (int) (((float) province.money) - (((float) amount) * province.getIncomeCoefficient()));
            }
        }
    }

    public int getStateDotations() {
        int dotations = 0;
        Iterator it = this.diplomacyManager.contracts.iterator();
        while (it.hasNext()) {
            DiplomaticContract contract = (DiplomaticContract) it.next();
            if (contract.contains(this)) {
                switch (contract.type) {
                    case 0:
                        dotations += contract.getDotationsFromEntityPerspective(this);
                        break;
                    case 1:
                        dotations += contract.getDotationsFromEntityPerspective(this);
                        break;
                    case 3:
                        dotations += contract.getDotationsFromEntityPerspective(this);
                        break;
                    default:
                        break;
                }
            }
        }
        return dotations;
    }

    boolean isAtWar() {
        for (Integer intValue : this.relations.values()) {
            if (intValue.intValue() == 2) {
                return true;
            }
        }
        return false;
    }

    public void updateCapitalName() {
        Hex capital;
        Province largestProvince = getLargestProvince(this.color);
        if (largestProvince != null) {
            capital = largestProvince.getCapital();
        } else {
            capital = this.diplomacyManager.fieldController.getRandomActivehex();
        }
        this.capitalName = CityNameGenerator.getInstance().generateName(capital);
    }

    private Province getLargestProvince(int colorIndex) {
        Province largestProvince = null;
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == colorIndex && (largestProvince == null || province.hexList.size() > largestProvince.hexList.size())) {
                largestProvince = province;
            }
        }
        return largestProvince;
    }

    public int getNumberOfFriends() {
        int c = 0;
        for (Entry<DiplomaticEntity, Integer> entry : this.relations.entrySet()) {
            if (((DiplomaticEntity) entry.getKey()).alive && ((Integer) entry.getValue()).intValue() == 1) {
                c++;
            }
        }
        return c;
    }

    public int getNumberOfMutualFriends(DiplomaticEntity entity) {
        int c = 0;
        for (Entry<DiplomaticEntity, Integer> entry : this.relations.entrySet()) {
            if (((DiplomaticEntity) entry.getKey()).alive && ((Integer) entry.getValue()).intValue() == 1 && entity.getRelation((DiplomaticEntity) entry.getKey()) == 1) {
                c++;
            }
        }
        return c;
    }

    boolean acceptsFriendsRequest(DiplomaticEntity initiator) {
        if (isAnyFriendBlackMarkedWithHim(initiator) || initiator.isAnyFriendBlackMarkedWithHim(this) || isBlackMarkedWith(initiator)) {
            return false;
        }
        if (this.human) {
            return true;
        }
        if (this.diplomacyManager.calculateDotationsForFriendship(initiator, this) >= -10) {
            return true;
        }
        return false;
    }

    private boolean isAnyFriendBlackMarkedWithHim(DiplomaticEntity entity) {
        for (Entry<DiplomaticEntity, Integer> entry : this.relations.entrySet()) {
            DiplomaticEntity friendEntity = (DiplomaticEntity) entry.getKey();
            if (friendEntity.alive && friendEntity.isFriendTo(this) && friendEntity.isBlackMarkedWith(entity)) {
                return true;
            }
        }
        return false;
    }

    void updateAlive() {
        boolean previousAliveState = this.alive;
        this.alive = false;
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            if (((Province) it.next()).getColor() == this.color) {
                this.alive = true;
                break;
            }
        }
        if (previousAliveState != this.alive && !this.alive) {
            onDeath();
        }
    }

    private void onDeath() {
        this.diplomacyManager.onEntityDied(this);
    }

    void thinkAboutChangingRelations() {
        if (YioGdxGame.random.nextInt(3) == 0) {
            tryToFindFriend();
        } else if (YioGdxGame.random.nextInt(9) == 0) {
            tryToStartWar();
        }
    }

    private void tryToStartWar() {
        int i = 0;
        while (i < 10) {
            DiplomaticEntity randomEntity = this.diplomacyManager.getRandomEntity();
            if (randomEntity == this || !randomEntity.alive || getRelation(randomEntity) != 0 || this.diplomacyManager.findContract(1, this, randomEntity) != null) {
                i++;
            } else if (isGoodIdeaToAttackEntity(randomEntity)) {
                this.diplomacyManager.log.addMessage(DipMessageType.war_declaration, this, randomEntity);
                this.diplomacyManager.onEntityRequestedToMakeRelationsWorse(this, randomEntity);
                return;
            } else {
                return;
            }
        }
    }

    private boolean isGoodIdeaToAttackEntity(DiplomaticEntity entity) {
        if (YioGdxGame.random.nextDouble() < 0.05d) {
            return true;
        }
        if (entity.getStateBalance() > getStateBalance() * 2) {
            return false;
        }
        if (entity.getStateFullMoney() > getStateFullMoney() * 5) {
            return false;
        }
        if (entity.getNumberOfFriends() > getNumberOfFriends() + 1) {
            return false;
        }
        return true;
    }

    private void tryToFindFriend() {
        int i = 0;
        while (i < 10) {
            DiplomaticEntity randomEntity = this.diplomacyManager.getRandomEntity();
            if (randomEntity == this || !randomEntity.alive || randomEntity.isHuman() || getRelation(randomEntity) != 0) {
                i++;
            } else if (this.diplomacyManager.isFriendshipPossible(this, randomEntity)) {
                this.diplomacyManager.makeFriends(this, randomEntity);
                return;
            } else {
                return;
            }
        }
    }

    boolean acceptsToStopWar(DiplomaticEntity entity) {
        if (this.human) {
            return true;
        }
        int stateFullMoney = getStateFullMoney();
        if (stateFullMoney < 15) {
            return true;
        }
        int pay = this.diplomacyManager.calculatePayToStopWar(entity, this);
        if (pay < stateFullMoney / 4) {
            return false;
        }
        if (pay < 15) {
            return false;
        }
        return true;
    }

    boolean isOneFriendAwayFromDiplomaticVictory() {
        int c = 0;
        for (Entry<DiplomaticEntity, Integer> entry : this.relations.entrySet()) {
            if (((DiplomaticEntity) entry.getKey()).alive && ((Integer) entry.getValue()).intValue() != 1) {
                c++;
            }
        }
        return c == 1;
    }

    boolean isFriendTo(DiplomaticEntity entity) {
        return getRelation(entity) == 1;
    }

    int getNumberOfLands() {
        int c = 0;
        Iterator it = this.diplomacyManager.fieldController.provinces.iterator();
        while (it.hasNext()) {
            Province province = (Province) it.next();
            if (province.getColor() == this.color) {
                c += province.hexList.size();
            }
        }
        return c;
    }

    public boolean isBlackMarkedWith(DiplomaticEntity entity) {
        return this.diplomacyManager.findContract(2, this, entity) != null;
    }

    boolean hasOnlyFriends() {
        for (Entry<DiplomaticEntity, Integer> entry : this.relations.entrySet()) {
            if (((DiplomaticEntity) entry.getKey()).alive && ((Integer) entry.getValue()).intValue() != 1) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "[Entity: " + this.capitalName + " (" + this.diplomacyManager.fieldController.getColorName(this.color) + ")]";
    }
}
