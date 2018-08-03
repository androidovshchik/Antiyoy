package io.androidovshchik.antiyoy.gameplay.diplomacy;

import io.androidovshchik.antiyoy.stuff.object_pool.ReusableYio;

public class DiplomaticMessage implements ReusableYio {
    public String arg1;
    public String arg2;
    public String arg3;
    public DiplomaticEntity recipient;
    public DiplomaticEntity sender;
    public DipMessageType type;

    public DiplomaticMessage() {
        reset();
    }

    public String toString() {
        return "[Message: " + this.type.name() + " " + this.sender + " " + this.recipient + "]";
    }

    public int getSenderColor() {
        if (this.sender == null) {
            return -1;
        }
        return this.sender.color;
    }

    public int getRecipientColor() {
        if (this.recipient == null) {
            return -1;
        }
        return this.recipient.color;
    }

    public boolean equals(DiplomaticMessage message) {
        if (this.type == message.type && this.sender == message.sender && this.recipient == message.recipient) {
            return true;
        }
        return false;
    }

    public void reset() {
        this.type = null;
        this.sender = null;
        this.recipient = null;
        this.arg1 = "-1";
        this.arg2 = "-1";
        this.arg3 = "-1";
    }

    public String getKey() {
        return this.type.name() + getSenderColor() + getRecipientColor();
    }

    public void setType(DipMessageType type) {
        this.type = type;
    }

    public void setSender(DiplomaticEntity sender) {
        this.sender = sender;
    }

    public void setRecipient(DiplomaticEntity recipient) {
        this.recipient = recipient;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(String arg3) {
        this.arg3 = arg3;
    }
}
