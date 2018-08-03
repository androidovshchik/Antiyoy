package io.androidovshchik.antiyoy.gameplay.name_generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

public class NameGenerator {
    boolean capitalize = true;
    Random defRandom = new Random();
    HashMap<String, String> groups = new HashMap();
    ArrayList<String> masks = new ArrayList();
    Random random;

    public String generateName() {
        return generateName(this.defRandom);
    }

    public String generateName(Random random) {
        this.random = random;
        StringBuffer stringBuffer = new StringBuffer();
        String mask = getRandomMask();
        for (int i = 0; i < mask.length(); i++) {
            String key = "" + mask.charAt(i);
            String group = getGroup(key);
            if (group == null) {
                stringBuffer.append(key);
            } else {
                stringBuffer.append(getRandomSymbol(group));
            }
        }
        if (this.capitalize) {
            capitalizeFirstLetter(stringBuffer);
        }
        return stringBuffer.toString();
    }

    private String getRandomSymbol(String src) {
        StringTokenizer tokenizer = new StringTokenizer(src, " ");
        ArrayList<String> tokens = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return (String) tokens.get(this.random.nextInt(tokens.size()));
    }

    private String getGroup(String key) {
        return (String) this.groups.get(key);
    }

    private String getRandomMask() {
        return (String) this.masks.get(this.random.nextInt(this.masks.size()));
    }

    private void capitalizeFirstLetter(StringBuffer buffer) {
        buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
    }

    public void setCapitalize(boolean capitalize) {
        this.capitalize = capitalize;
    }

    public void addGroup(String key, String symbols) {
        this.groups.put(key, symbols);
    }

    public void addMask(String mask) {
        this.masks.add(mask);
    }

    public void clearMasks() {
        this.masks.clear();
    }

    public void clearGroups() {
        this.groups.clear();
    }

    public void setMasks(ArrayList<String> masks) {
        this.masks = masks;
    }

    public void setGroups(HashMap<String, String> groups) {
        this.groups = groups;
    }
}
