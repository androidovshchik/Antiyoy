package io.androidovshchik.antiyoy.gameplay.diplomacy;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class DiplomacyInfoCondensed implements ReusableYio {
    private static DiplomacyInfoCondensed instance;
    private StringBuilder builder = new StringBuilder();
    String contracts;
    String cooldowns;
    DiplomacyManager diplomacyManager;
    String full;
    String messages;
    String relations;

    public static DiplomacyInfoCondensed getInstance() {
        if (instance == null) {
            instance = new DiplomacyInfoCondensed();
        }
        instance.reset();
        return instance;
    }

    public static void onGeneralInitialization() {
        instance = null;
    }

    public void reset() {
        this.full = null;
        this.relations = null;
        this.diplomacyManager = null;
        this.contracts = null;
        this.cooldowns = null;
        this.messages = null;
    }

    public void update(DiplomacyManager diplomacyManager) {
        this.diplomacyManager = diplomacyManager;
        updateRelations();
        updateContracts();
        updateCooldowns();
        updateMessages();
        updateFull();
    }

    private void updateMessages() {
        this.builder.setLength(0);
        Iterator it = this.diplomacyManager.log.messages.iterator();
        while (it.hasNext()) {
            this.builder.append(getSingleMessageCode((DiplomaticMessage) it.next())).append(",");
        }
        if (this.builder.length() == 0) {
            this.builder.append(" ");
        }
        this.messages = this.builder.toString();
    }

    private String getSingleMessageCode(DiplomaticMessage message) {
        return message.type + " " + message.getSenderColor() + " " + message.getRecipientColor() + " " + message.arg1 + " " + message.arg2 + " " + message.arg3;
    }

    private void updateCooldowns() {
        this.builder.setLength(0);
        Iterator it = this.diplomacyManager.cooldowns.iterator();
        while (it.hasNext()) {
            this.builder.append(getSingleCooldownCode((DiplomaticCooldown) it.next())).append(",");
        }
        if (this.builder.length() == 0) {
            this.builder.append(" ");
        }
        this.cooldowns = this.builder.toString();
    }

    private String getSingleCooldownCode(DiplomaticCooldown cooldown) {
        return cooldown.type + " " + cooldown.counter + " " + cooldown.getOneColor() + " " + cooldown.getTwoColor();
    }

    private void updateContracts() {
        this.builder.setLength(0);
        Iterator it = this.diplomacyManager.contracts.iterator();
        while (it.hasNext()) {
            this.builder.append(getSingleContractCode((DiplomaticContract) it.next())).append(",");
        }
        if (this.builder.length() == 0) {
            this.builder.append(" ");
        }
        this.contracts = this.builder.toString();
    }

    private String getSingleContractCode(DiplomaticContract contract) {
        return contract.type + " " + contract.getOneColor() + " " + contract.getTwoColor() + " " + contract.dotations + " " + contract.expireCountDown;
    }

    public void apply(DiplomacyManager diplomacyManager) {
        this.diplomacyManager = diplomacyManager;
        if (!this.full.equals("-")) {
            applyFull();
            applyRelations();
            applyContracts();
            applyCooldowns();
            applyMessages();
            diplomacyManager.onRelationsChanged();
        }
    }

    private void applyMessages() {
        if (this.messages != null) {
            for (String s : this.messages.split(",")) {
                applySingleMessage(s);
            }
        }
    }

    private void applySingleMessage(String s) {
        String[] split = s.split(" ");
        if (split.length != 0) {
            DipMessageType type = DipMessageType.valueOf(split[0]);
            int color1 = Integer.valueOf(split[1]).intValue();
            int color2 = Integer.valueOf(split[2]).intValue();
            String arg1 = split[3];
            String arg2 = split[4];
            String arg3 = split[5];
            DiplomaticEntity entity1 = this.diplomacyManager.getEntity(color1);
            DiplomaticEntity entity2 = this.diplomacyManager.getEntity(color2);
            if (entity1 != null && entity2 != null) {
                DiplomaticMessage diplomaticMessage = this.diplomacyManager.log.addMessage(type, entity1, entity2);
                if (diplomaticMessage != null) {
                    diplomaticMessage.setArg1(arg1);
                    diplomaticMessage.setArg2(arg2);
                    diplomaticMessage.setArg3(arg3);
                }
            }
        }
    }

    private void applyCooldowns() {
        if (this.cooldowns != null) {
            for (String s : this.cooldowns.split(",")) {
                applySingleCooldown(s);
            }
        }
    }

    private void applySingleCooldown(String s) {
        String[] split = s.split(" ");
        if (split.length != 0) {
            int type = Integer.valueOf(split[0]).intValue();
            int counter = Integer.valueOf(split[1]).intValue();
            int color1 = Integer.valueOf(split[2]).intValue();
            int color2 = Integer.valueOf(split[3]).intValue();
            DiplomaticEntity entity1 = this.diplomacyManager.getEntity(color1);
            DiplomaticEntity entity2 = this.diplomacyManager.getEntity(color2);
            if (entity1 != null && entity2 != null) {
                this.diplomacyManager.addCooldown(type, counter, entity1, entity2);
            }
        }
    }

    private void applyContracts() {
        for (String s : this.contracts.split(",")) {
            applySingleContract(s);
        }
    }

    private void applySingleContract(String s) {
        String[] split = s.split(" ");
        if (split.length != 0) {
            int type = Integer.valueOf(split[0]).intValue();
            int color1 = Integer.valueOf(split[1]).intValue();
            int color2 = Integer.valueOf(split[2]).intValue();
            int dotations = Integer.valueOf(split[3]).intValue();
            int expire = Integer.valueOf(split[4]).intValue();
            DiplomaticEntity entity1 = this.diplomacyManager.getEntity(color1);
            DiplomaticEntity entity2 = this.diplomacyManager.getEntity(color2);
            if (entity1 != null && entity2 != null) {
                DiplomaticContract contract = this.diplomacyManager.findContract(type, entity1, entity2);
                if (contract == null) {
                    contract = this.diplomacyManager.addContract(type, entity1, entity2);
                }
                if (contract.one != entity1) {
                    dotations *= -1;
                }
                contract.setDotations(dotations);
                contract.setExpireCountDown(expire);
            }
        }
    }

    private void applyFull() {
        String[] split = this.full.split("#");
        this.relations = split[0];
        this.contracts = split[1];
        if (split.length > 2) {
            this.cooldowns = split[2];
        } else {
            this.cooldowns = null;
        }
        if (split.length > 3) {
            this.messages = split[3];
        } else {
            this.messages = null;
        }
    }

    private void updateFull() {
        this.full = this.relations + "#" + this.contracts + "#" + this.cooldowns + "#" + this.messages + "#";
    }

    void updateRelations() {
        ArrayList<DiplomaticEntity> entities = this.diplomacyManager.entities;
        this.builder.setLength(0);
        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                this.builder.append(getSingleRelationCode((DiplomaticEntity) entities.get(i), (DiplomaticEntity) entities.get(j))).append(",");
            }
        }
        this.relations = this.builder.toString();
    }

    String getSingleRelationCode(DiplomaticEntity one, DiplomaticEntity two) {
        return one.color + " " + two.color + " " + one.getRelation(two);
    }

    void applyRelations() {
        for (String token : this.relations.split(",")) {
            applySingleRelation(token);
        }
    }

    void applySingleRelation(String token) {
        String[] split = token.split(" ");
        int color1 = Integer.valueOf(split[0]).intValue();
        int color2 = Integer.valueOf(split[1]).intValue();
        int relation = Integer.valueOf(split[2]).intValue();
        this.diplomacyManager.setRelation(this.diplomacyManager.getEntity(color1), this.diplomacyManager.getEntity(color2), relation);
    }

    public String getFull() {
        return this.full;
    }

    public void setFull(String full) {
        this.full = full;
    }
}
