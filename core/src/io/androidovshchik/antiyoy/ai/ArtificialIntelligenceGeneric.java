package io.androidovshchik.antiyoy.ai;

import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.GameController;
import io.androidovshchik.antiyoy.gameplay.Hex;
import io.androidovshchik.antiyoy.gameplay.Province;

public abstract class ArtificialIntelligenceGeneric extends ArtificialIntelligence {
    public static final int MAX_EXTRA_FARM_COST = 80;

    ArtificialIntelligenceGeneric(GameController gameController, int color) {
        super(gameController, color);
    }

    protected void spendMoney(Province province) {
        tryToBuildTowers(province);
        tryToBuildFarms(province);
        tryToBuildUnits(province);
    }

    protected void tryToBuildFarms(Province province) {
        if (province.getExtraFarmCost() <= 80) {
            while (province.hasMoneyForFarm() && isOkToBuildNewFarm(province)) {
                Hex hex = findGoodHexForFarm(province);
                if (hex != null) {
                    this.gameController.fieldController.buildFarm(province, hex);
                } else {
                    return;
                }
            }
        }
    }

    protected boolean isOkToBuildNewFarm(Province srcProvince) {
        if (srcProvince.money <= srcProvince.getCurrentFarmPrice() * 2 && findHexThatNeedsTower(srcProvince) != null) {
            return false;
        }
        return true;
    }

    protected int getArmyStrength(Province province) {
        int sum = 0;
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hex.containsUnit()) {
                sum += hex.unit.strength;
            }
        }
        return sum;
    }

    protected Hex findGoodHexForFarm(Province province) {
        if (!hasProvinceGoodHexForFarm(province)) {
            return null;
        }
        Hex hex;
        do {
            hex = (Hex) province.hexList.get(this.random.nextInt(province.hexList.size()));
        } while (!isHexGoodForFarm(hex));
        return hex;
    }

    protected boolean hasProvinceGoodHexForFarm(Province province) {
        Iterator it = province.hexList.iterator();
        while (it.hasNext()) {
            if (isHexGoodForFarm((Hex) it.next())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isHexGoodForFarm(Hex hex) {
        if (!hex.isFree()) {
            return false;
        }
        if (hex.hasThisObjectNearby(3) || hex.hasThisObjectNearby(6)) {
            return true;
        }
        return false;
    }
}
