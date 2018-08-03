package io.androidovshchik.antiyoy.menu.save_slot_selector;

public class SaveSlotInfo {
    public String description;
    public String key;
    public String name;

    public String getDescription() {
        if (this.description == null) {
            return "";
        }
        return this.description;
    }

    public String toString() {
        return "[Slot: key='" + this.key + "' name='" + this.name + "' description='" + this.description + "' ]";
    }
}
