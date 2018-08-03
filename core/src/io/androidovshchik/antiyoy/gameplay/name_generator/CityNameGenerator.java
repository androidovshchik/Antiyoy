package io.androidovshchik.antiyoy.gameplay.name_generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.stuff.LanguagesManager;

public class CityNameGenerator {
    private static CityNameGenerator instance = null;
    HashMap<String, String> groups = new HashMap();
    ArrayList<String> masks = new ArrayList();
    NameGenerator nameGenerator = new NameGenerator();

    public static void initialize() {
        instance = null;
    }

    public static CityNameGenerator getInstance() {
        if (instance == null) {
            instance = new CityNameGenerator();
        }
        return instance;
    }

    public void load() {
        loadMasks();
        loadGroups();
    }

    private void loadGroups() {
        StringTokenizer tokenizer = new StringTokenizer(LanguagesManager.getInstance().getString("city_name_gen_groups"), "#");
        this.groups.clear();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int indexOfSeparator = token.indexOf(58);
            this.groups.put(token.substring(0, indexOfSeparator), token.substring(indexOfSeparator + 1));
        }
    }

    private void loadMasks() {
        StringTokenizer tokenizer = new StringTokenizer(LanguagesManager.getInstance().getString("city_name_gen_masks"), " ");
        this.masks.clear();
        while (tokenizer.hasMoreTokens()) {
            this.masks.add(tokenizer.nextToken());
        }
    }

    public String generateName() {
        return this.nameGenerator.generateName();
    }

    public String generateName(Hex capitalHex) {
        Random random = new Random((long) (capitalHex.index1 + (capitalHex.index2 * 53)));
        this.nameGenerator.setMasks(this.masks);
        this.nameGenerator.setGroups(this.groups);
        this.nameGenerator.setCapitalize(true);
        return this.nameGenerator.generateName(random);
    }
}
