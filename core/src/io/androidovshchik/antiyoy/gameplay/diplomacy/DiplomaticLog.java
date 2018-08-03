package io.androidovshchik.antiyoy.gameplay.diplomacy;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.menu.scenes.Scenes;
import io.androidovshchik.antiyoy.stuff.object_pool.ObjectPoolYio;

public class DiplomaticLog {
    DiplomacyManager diplomacyManager;
    public ArrayList<DiplomaticMessage> messages = new ArrayList();
    ObjectPoolYio<DiplomaticMessage> poolMessages;

    class C01031 extends ObjectPoolYio<DiplomaticMessage> {
        C01031() {
        }

        public DiplomaticMessage makeNewObject() {
            return new DiplomaticMessage();
        }
    }

    public DiplomaticLog(DiplomacyManager diplomacyManager) {
        this.diplomacyManager = diplomacyManager;
        initPools();
    }

    private void initPools() {
        this.poolMessages = new C01031();
    }

    public void onClearMessagesButtonClicked() {
        removeMessagesByRecipient(this.diplomacyManager.getMainEntity());
    }

    public void removeMessagesByRecipient(DiplomaticEntity recipient) {
        for (int i = this.messages.size() - 1; i >= 0; i--) {
            DiplomaticMessage diplomaticMessage = (DiplomaticMessage) this.messages.get(i);
            if (diplomaticMessage.recipient == recipient) {
                removeMessage(diplomaticMessage);
            }
        }
    }

    public void onListItemClicked(String key) {
        DiplomaticMessage message = findMessage(key);
        if (message != null) {
            switch (message.type) {
                case friendship_proposal:
                    Scenes.sceneFriendshipDialog.create();
                    Scenes.sceneFriendshipDialog.dialog.setEntities(message.sender, message.recipient);
                    break;
                case friendship_ended:
                    Scenes.sceneFriendshipDialog.create();
                    Scenes.sceneFriendshipDialog.dialog.setEntities(message.sender, message.recipient);
                    break;
                case stop_war:
                    Scenes.sceneStopWarDialog.create();
                    Scenes.sceneStopWarDialog.dialog.setEntities(message.sender, message.recipient);
                    break;
            }
            removeMessage(message);
        }
    }

    void checkToClearAbuseMessages() {
        DiplomaticEntity mainEntity = this.diplomacyManager.getMainEntity();
        boolean oneFriendAwayFromDiplomaticVictory = mainEntity.isOneFriendAwayFromDiplomaticVictory();
        for (int i = this.messages.size() - 1; i >= 0; i--) {
            DiplomaticMessage diplomaticMessage = (DiplomaticMessage) this.messages.get(i);
            if (diplomaticMessage.recipient == mainEntity && oneFriendAwayFromDiplomaticVictory && diplomaticMessage.type == DipMessageType.friendship_proposal) {
                removeMessage(diplomaticMessage);
            }
        }
    }

    public void removeMessage(DiplomaticMessage message) {
        this.poolMessages.addWithCheck(message);
        this.messages.remove(message);
    }

    public DiplomaticMessage findMessage(String key) {
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            DiplomaticMessage message = (DiplomaticMessage) it.next();
            if (message.getKey().equals(key)) {
                return message;
            }
        }
        return null;
    }

    public boolean hasSomethingToRead() {
        if (!this.diplomacyManager.fieldController.gameController.isPlayerTurn()) {
            return false;
        }
        DiplomaticEntity mainEntity = this.diplomacyManager.getMainEntity();
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            if (((DiplomaticMessage) it.next()).recipient == mainEntity) {
                return true;
            }
        }
        return false;
    }

    public DiplomaticMessage addMessage(DipMessageType type, DiplomaticEntity sender, DiplomaticEntity recipient) {
        DiplomaticMessage next = (DiplomaticMessage) this.poolMessages.getNext();
        next.setType(type);
        next.setSender(sender);
        next.setRecipient(recipient);
        if (containsSimilarMessage(next)) {
            this.poolMessages.add(next);
            return null;
        }
        this.messages.add(next);
        return next;
    }

    private boolean containsSimilarMessage(DiplomaticMessage message) {
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            if (((DiplomaticMessage) it.next()).equals(message)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            this.poolMessages.add((DiplomaticMessage) it.next());
        }
        this.messages.clear();
    }

    public void showInConsole() {
        System.out.println();
        System.out.println("DiplomaticLog.showInConsole");
        Iterator it = this.messages.iterator();
        while (it.hasNext()) {
            System.out.println("- " + ((DiplomaticMessage) it.next()));
        }
    }
}
