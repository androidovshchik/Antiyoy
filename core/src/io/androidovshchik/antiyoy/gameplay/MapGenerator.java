package io.androidovshchik.antiyoy.gameplay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import io.androidovshchik.antiyoy.gameplay.rules.GameRules;
import io.androidovshchik.antiyoy.stuff.GraphicsYio;
import io.androidovshchik.antiyoy.stuff.PointYio;
import io.androidovshchik.antiyoy.stuff.Yio;

public class MapGenerator {
    static int SMALL_PROVINCE_SIZE = 4;
    protected float boundHeight;
    protected float boundWidth;
    private final DetectorProvince detectorProvince = new DetectorProvince();
    protected int fHeight;
    protected int fWidth;
    protected Hex[][] field;
    protected final GameController gameController;
    protected int f97h;
    protected ArrayList<PointYio> islandCenters;
    protected ArrayList<Link> links;
    protected Random random;
    protected int f98w;

    class Link {
        PointYio p1;
        PointYio p2;

        public Link(PointYio p1, PointYio p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        boolean equals(PointYio p1, PointYio p2) {
            return containsPoint(p1) && containsPoint(p2);
        }

        boolean containsPoint(PointYio p) {
            return this.p1 == p || this.p2 == p;
        }

        boolean equals(Link link) {
            return equals(link.p1, link.p2);
        }
    }

    public MapGenerator(GameController gameController) {
        this.gameController = gameController;
    }

    protected void templateLoop() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
            }
        }
    }

    protected void setValues(Random random, Hex[][] field) {
        this.boundWidth = this.gameController.boundWidth;
        this.boundHeight = this.gameController.boundHeight;
        this.fWidth = this.gameController.fieldController.fWidth;
        this.fHeight = this.gameController.fieldController.fHeight;
        this.f98w = (int) GraphicsYio.width;
        this.f97h = (int) GraphicsYio.height;
        SMALL_PROVINCE_SIZE = 5;
        this.random = random;
        this.field = field;
    }

    public void generateMap(Random random, Hex[][] field) {
        setValues(random, field);
        beginGeneration();
        createLand();
        removeSingleHoles();
        addTrees();
        balanceMap();
        endGeneration();
    }

    protected void balanceMap() {
        checkToFixNoPlayerProblem();
        if (GameRules.colorNumber >= 4) {
            spawnManySmallProvinces();
            cutProvincesToSmallSizes();
            achieveFairNumberOfProvincesForEveryPlayer();
            giveLastPlayersSlightAdvantage();
        }
    }

    private void checkToFixNoPlayerProblem() {
        if (!mapHasAtLeastOnePlayerProvince()) {
            Iterator it = this.gameController.fieldController.activeHexes.iterator();
            while (it.hasNext()) {
                Hex activeHex = (Hex) it.next();
                if (activeHex.colorIndex == 0) {
                    for (int i = 0; i < 6; i++) {
                        Hex adjacentHex = activeHex.getAdjacentHex(i);
                        if (adjacentHex.active) {
                            adjacentHex.colorIndex = 0;
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    private boolean mapHasAtLeastOnePlayerProvince() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.colorIndex == 0 && activeHex.numberOfFriendlyHexesNearby() > 0) {
                return true;
            }
        }
        return false;
    }

    protected void increaseProvince(ArrayList<Hex> provinceList, double power) {
        Iterator it = provinceList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            for (int i = 0; i < 6; i++) {
                Hex adjHex = hex.getAdjacentHex(i);
                if (adjHex.active && !adjHex.sameColor(hex) && this.random.nextDouble() < power) {
                    adjHex.colorIndex = hex.colorIndex;
                }
            }
        }
    }

    protected boolean hexHasEnemiesNear(Hex hex) {
        for (int i = 0; i < 6; i++) {
            Hex adjHex = hex.getAdjacentHex(i);
            if (adjHex.active && !adjHex.sameColor(hex)) {
                return true;
            }
        }
        return false;
    }

    protected void decreaseProvince(ArrayList<Hex> provinceList, double power) {
        Iterator it = provinceList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            if (hexHasEnemiesNear(hex) && this.random.nextDouble() < power) {
                hex.colorIndex = getRandomColor();
            }
        }
    }

    protected void giveDisadvantageToPlayer(int index, double power) {
        clearGenFlags();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.genFlag && activeHex.sameColor(index)) {
                ArrayList<Hex> provinceList = this.detectorProvince.detectProvince(activeHex);
                tagProvince(provinceList);
                decreaseProvince(provinceList, power);
            }
        }
    }

    protected void giveAdvantageToPlayer(int index, double power) {
        clearGenFlags();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.genFlag && activeHex.sameColor(index)) {
                increaseProvince(this.detectorProvince.detectProvince(activeHex), power);
                tagProvince(this.detectorProvince.detectProvince(activeHex));
            }
        }
    }

    protected void giveLastPlayersSlightAdvantage() {
        giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.053d);
        giveAdvantageToPlayer(GameRules.colorNumber - 2, 0.033d);
        if (GameRules.colorNumber >= 5) {
            giveAdvantageToPlayer(2, 0.0165d);
        } else {
            giveAdvantageToPlayer(1, 0.0065d);
            giveAdvantageToPlayer(GameRules.colorNumber - 1, 0.01d);
        }
        giveDisadvantageToPlayer(0, 0.048d);
    }

    protected void spawnManySmallProvinces() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.noProvincesNearby()) {
                spawnProvince(activeHex, 2);
            }
        }
    }

    protected void spawnProvince(Hex spawnHex, int startingPotential) {
        spawnHex.genPotential = startingPotential;
        ArrayList<Hex> propagationList = new ArrayList();
        propagationList.add(spawnHex);
        while (propagationList.size() > 0) {
            Hex hex = (Hex) propagationList.get(0);
            propagationList.remove(0);
            if (this.random.nextInt(startingPotential) <= hex.genPotential) {
                hex.colorIndex = spawnHex.colorIndex;
                if (hex.genPotential != 0) {
                    for (int i = 0; i < 6; i++) {
                        Hex adjHex = hex.getAdjacentHex(i);
                        if (!(propagationList.contains(adjHex) || !adjHex.active || adjHex.colorIndex == spawnHex.colorIndex)) {
                            adjHex.genPotential = hex.genPotential - 1;
                            propagationList.add(adjHex);
                        }
                    }
                }
            }
        }
    }

    protected boolean atLeastOneProvinceIsTooBig() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            if (this.detectorProvince.detectProvince((Hex) it.next()).size() > SMALL_PROVINCE_SIZE) {
                return true;
            }
        }
        return false;
    }

    protected void cutProvincesToSmallSizes() {
        int loopLimit = 100;
        while (atLeastOneProvinceIsTooBig() && loopLimit > 0) {
            Iterator it = this.gameController.fieldController.activeHexes.iterator();
            while (it.hasNext()) {
                ArrayList<Hex> provinceList = this.detectorProvince.detectProvince((Hex) it.next());
                if (provinceList.size() > SMALL_PROVINCE_SIZE) {
                    reduceProvinceSize(provinceList);
                }
            }
            loopLimit--;
        }
    }

    protected int getRandomColorExceptOne(int excludedColor) {
        int color;
        do {
            color = getRandomColor();
        } while (color == excludedColor);
        return color;
    }

    protected Hex findHexToExcludeFromProvince(ArrayList<Hex> provinceList) {
        Hex resultHex = null;
        int minNumber = 0;
        for (int i = 0; i < provinceList.size(); i++) {
            Hex currHex = (Hex) provinceList.get(i);
            int currNumber = currHex.numberOfFriendlyHexesNearby();
            if (resultHex == null || currNumber < minNumber) {
                minNumber = currNumber;
                resultHex = currHex;
            }
        }
        return resultHex;
    }

    protected void reduceProvinceSize(ArrayList<Hex> provinceList) {
        int provinceColor = ((Hex) provinceList.get(0)).colorIndex;
        while (provinceList.size() > SMALL_PROVINCE_SIZE) {
            Hex hex = findHexToExcludeFromProvince(provinceList);
            provinceList.remove(hex);
            hex.colorIndex = getRandomColorExceptOne(provinceColor);
        }
    }

    protected void countProvinces(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = 0;
        }
        clearGenFlags();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (!activeHex.genFlag) {
                ArrayList<Hex> provinceList = this.detectorProvince.detectProvince(activeHex);
                if (provinceList.size() > 1) {
                    int i2 = ((Hex) provinceList.get(0)).colorIndex;
                    numbers[i2] = numbers[i2] + 1;
                    Iterator it2 = provinceList.iterator();
                    while (it2.hasNext()) {
                        ((Hex) it2.next()).genFlag = true;
                    }
                }
            }
        }
    }

    protected int maxDifferenceInNumbers(int[] numbers) {
        int maxDifference = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                int d = Math.abs(numbers[i] - numbers[j]);
                if (d > maxDifference) {
                    maxDifference = d;
                }
            }
        }
        return maxDifference;
    }

    protected int indexOfMax(int[] numbers) {
        int indexMax = 0;
        int maxValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > maxValue) {
                indexMax = i;
                maxValue = numbers[i];
            }
        }
        return indexMax;
    }

    protected int indexOfMin(int[] numbers) {
        int indexMin = 0;
        int minValue = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < minValue) {
                indexMin = i;
                minValue = numbers[i];
            }
        }
        return indexMin;
    }

    protected boolean provinceHasNeighbourWithColor(ArrayList<Hex> provinceList, int color) {
        Iterator it = provinceList.iterator();
        while (it.hasNext()) {
            Hex hex = (Hex) it.next();
            for (int i = 0; i < 6; i++) {
                Hex adjHex = hex.getAdjacentHex(i);
                if (adjHex.active && adjHex.sameColor(color) && adjHex.numberOfFriendlyHexesNearby() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean tryToGiveAwayProvince(ArrayList<Hex> provinceList) {
        int i = 0;
        while (i < GameRules.colorNumber) {
            if (i == ((Hex) provinceList.get(0)).colorIndex || provinceHasNeighbourWithColor(provinceList, i)) {
                i++;
            } else {
                Iterator it = provinceList.iterator();
                while (it.hasNext()) {
                    ((Hex) it.next()).colorIndex = i;
                }
                return true;
            }
        }
        return false;
    }

    protected void tagProvince(ArrayList<Hex> provinceList) {
        Iterator it = provinceList.iterator();
        while (it.hasNext()) {
            ((Hex) it.next()).genFlag = true;
        }
    }

    protected boolean giveProvinceToSomeone(int giverIndex) {
        clearGenFlags();
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.sameColor(giverIndex) && !activeHex.genFlag) {
                ArrayList<Hex> provinceList = this.detectorProvince.detectProvince(activeHex);
                if (provinceList.size() > 1) {
                    tagProvince(provinceList);
                    if (tryToGiveAwayProvince(provinceList)) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    protected void achieveFairNumberOfProvincesForEveryPlayer() {
        int[] numbers = new int[GameRules.colorNumber];
        countProvinces(numbers);
        int loopLimit = 50;
        while (maxDifferenceInNumbers(numbers) > 1 && loopLimit > 0 && giveProvinceToSomeone(indexOfMax(numbers))) {
            countProvinces(numbers);
            loopLimit--;
        }
    }

    protected void centerLand() {
        Hex centerHex = getCenterHex();
        int utterLeft = centerHex.index1;
        int utterRight = centerHex.index1;
        int utterUp = centerHex.index2;
        int utterDown = centerHex.index2;
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (activeHex.index1 < utterLeft) {
                utterLeft = activeHex.index1;
            }
            if (activeHex.index1 > utterRight) {
                utterRight = activeHex.index1;
            }
            if (activeHex.index2 < utterDown) {
                utterDown = activeHex.index2;
            }
            if (activeHex.index2 > utterUp) {
                utterUp = activeHex.index2;
            }
        }
        relocateMap(centerHex.index1 - ((utterLeft + utterRight) / 2), centerHex.index2 - ((utterDown + utterUp) / 2));
    }

    protected void relocateMap(int deltaX, int deltaY) {
        int i;
        int j;
        clearGenFlags();
        for (i = 0; i < this.fWidth; i++) {
            for (j = 0; j < this.fHeight; j++) {
                this.field[i][j].genFlag = this.field[i][j].active;
                this.field[i][j].lastColorIndex = this.field[i][j].colorIndex;
            }
        }
        this.gameController.fieldController.clearActiveHexesList();
        clearHexes();
        for (i = 0; i < this.fWidth; i++) {
            for (j = 0; j < this.fHeight; j++) {
                int destX = i + deltaX;
                int destY = j + deltaY;
                if (destX < 0 || destY < 0 || destX >= this.fWidth || deltaY >= this.fHeight) {
                    deactivateHex(this.field[i][j]);
                } else if (this.field[i][j].genFlag) {
                    activateHex(this.field[destX][destY], this.field[i][j].lastColorIndex);
                }
            }
        }
    }

    protected boolean isGood() {
        return isLinked() && ((double) this.gameController.fieldController.activeHexes.size()) > 0.3d * ((double) numberOfAvailableHexes());
    }

    protected int numberOfAvailableHexes() {
        int c = 0;
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                if (isHexInsideBounds(this.field[i][j])) {
                    c++;
                }
            }
        }
        return c;
    }

    protected boolean isLinked() {
        clearGenFlags();
        Hex activeHex = findActiveHex();
        if (activeHex == null) {
            return false;
        }
        int i;
        ArrayList<Hex> tempList = new ArrayList();
        tempList.add(activeHex);
        while (tempList.size() > 0) {
            Hex hex = (Hex) tempList.get(0);
            tempList.remove(0);
            hex.genFlag = true;
            for (i = 0; i < 6; i++) {
                Hex adjHex = hex.getAdjacentHex(i);
                if (!(!adjHex.active || adjHex.genFlag || tempList.contains(adjHex))) {
                    tempList.add(adjHex);
                }
            }
        }
        i = 0;
        while (i < this.fWidth) {
            int j = 0;
            while (j < this.fHeight) {
                if (this.field[i][j].active && !this.field[i][j].genFlag) {
                    return false;
                }
                j++;
            }
            i++;
        }
        return true;
    }

    protected Hex findActiveHex() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                if (this.field[i][j].active) {
                    return this.field[i][j];
                }
            }
        }
        return null;
    }

    protected void maybeDeactivateIfPossible(Hex hex) {
        if (hex.active && this.random.nextDouble() <= 0.8d && hex.numberOfActiveHexesNearby() == 4) {
            deactivateHex(hex);
        }
    }

    protected void cutOffHexesOutsideOfBounds() {
        int i = 0;
        while (i < this.fWidth) {
            int j = 0;
            while (j < this.fHeight) {
                if (this.field[i][j].active && !isHexInsideBounds(this.field[i][j])) {
                    deactivateHex(this.field[i][j]);
                    for (int k = 0; k < 6; k++) {
                        maybeDeactivateIfPossible(this.field[i][j].getAdjacentHex(k));
                    }
                }
                j++;
            }
            i++;
        }
    }

    protected void clearHexes() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                if (this.field[i][j].active) {
                    deactivateHex(this.field[i][j]);
                }
            }
        }
    }

    protected void clearGenFlags() {
        for (int i = 0; i < this.fWidth; i++) {
            for (int j = 0; j < this.fHeight; j++) {
                this.field[i][j].genFlag = false;
            }
        }
    }

    protected Hex getCenterHex() {
        return this.gameController.fieldController.getHexByPos((double) (this.boundWidth / 2.0f), (double) (this.boundHeight / 2.0f));
    }

    protected double distanceFromCenterToCorners() {
        return Yio.distance(0.0d, 0.0d, (double) (this.boundWidth / 2.0f), (double) (this.boundHeight / 2.0f));
    }

    protected Hex getRandomHexNearCenter() {
        while (true) {
            double a = getRandomAngle();
            double r = this.random.nextDouble();
            r = (r * r) * distanceFromCenterToCorners();
            Hex hex = this.gameController.fieldController.getHexByPos(((double) (this.boundWidth / 2.0f)) + (Math.cos(a) * r), ((double) (this.boundHeight / 2.0f)) + (Math.sin(a) * r));
            if (hex != null && isHexInsideBounds(hex)) {
                return hex;
            }
        }
    }

    protected double getRandomAngle() {
        return (this.random.nextDouble() * 2.0d) * 3.141592653589793d;
    }

    protected Hex getRandomHex() {
        Hex hex;
        do {
            hex = this.field[this.random.nextInt(this.fWidth)][this.random.nextInt(this.fHeight)];
        } while (!isHexInsideBounds(hex));
        return hex;
    }

    protected int getRandomColor() {
        return this.random.nextInt(GameRules.colorNumber);
    }

    protected boolean activateHex(Hex hex, int color) {
        if (hex.active) {
            return false;
        }
        hex.active = true;
        hex.setColorIndex(color);
        this.gameController.fieldController.activeHexes.listIterator().add(hex);
        return true;
    }

    protected void deactivateHex(Hex hex) {
        hex.active = false;
        this.gameController.fieldController.activeHexes.remove(hex);
    }

    protected void spawnIsland(Hex startHex, int size) {
        clearGenFlags();
        startHex.genPotential = size;
        ArrayList<Hex> propagationList = new ArrayList();
        propagationList.add(startHex);
        while (propagationList.size() > 0) {
            Hex hex = (Hex) propagationList.get(0);
            propagationList.remove(0);
            hex.genFlag = true;
            if (this.random.nextInt(size) <= hex.genPotential) {
                boolean activated = activateHex(hex, getRandomColor());
                if (hex.genPotential != 0 && activated) {
                    for (int i = 0; i < 6; i++) {
                        Hex adjHex = hex.getAdjacentHex(i);
                        if (!(adjHex.genFlag || adjHex.isEmptyHex() || propagationList.contains(adjHex))) {
                            adjHex.genPotential = hex.genPotential - 1;
                            propagationList.add(adjHex);
                        }
                    }
                }
            }
        }
    }

    protected void uniteIslandsWithRoads() {
        for (int i = 0; i < this.islandCenters.size(); i++) {
            createRoadBetweenIslands(i, getClosestIslandIndex(i));
        }
    }

    protected void createRoadBetweenIslands(int islandOne, int islandTwo) {
        if (islandTwo != -1) {
            PointYio startPoint = (PointYio) this.islandCenters.get(islandOne);
            PointYio endPoint = (PointYio) this.islandCenters.get(islandTwo);
            this.links.add(new Link(startPoint, endPoint));
            double distance = startPoint.distanceTo(endPoint);
            double delta = (double) (this.gameController.fieldController.hexSize / 2.0f);
            double a = startPoint.angleTo(endPoint);
            int n = (int) (distance / delta);
            for (int i = 0; i < n; i++) {
                spawnIsland(this.gameController.fieldController.getHexByPos(((double) startPoint.f144x) + ((((double) i) * delta) * Math.cos(a)), ((double) startPoint.f145y) + ((((double) i) * delta) * Math.sin(a))), 2);
            }
        }
    }

    protected boolean areIslandsAlreadyUnited(PointYio p1, PointYio p2) {
        for (int i = 0; i < this.links.size(); i++) {
            if (((Link) this.links.get(i)).equals(p1, p2)) {
                return true;
            }
        }
        return false;
    }

    protected int getClosestIslandIndex(int searcherIslandIndex) {
        PointYio startPoint = (PointYio) this.islandCenters.get(searcherIslandIndex);
        int closestIslandIndex = -1;
        double minDistance = (double) (this.fWidth * this.fHeight);
        int i = 1;
        while (i < this.islandCenters.size()) {
            if (!(i == searcherIslandIndex || areIslandsAlreadyUnited(startPoint, (PointYio) this.islandCenters.get(i)))) {
                double currentDistance = startPoint.distanceTo((PointYio) this.islandCenters.get(i));
                if (currentDistance < minDistance) {
                    minDistance = currentDistance;
                    closestIslandIndex = i;
                }
            }
            i++;
        }
        return closestIslandIndex;
    }

    protected void addTrees() {
        Iterator it = this.gameController.fieldController.activeHexes.iterator();
        while (it.hasNext()) {
            Hex activeHex = (Hex) it.next();
            if (this.random.nextDouble() < 0.1d && !activeHex.containsObject()) {
                this.gameController.fieldController.spawnTree(activeHex);
            }
        }
    }

    protected void removeSingleHoles() {
        int i = 0;
        while (i < this.fWidth) {
            int j = 0;
            while (j < this.fHeight) {
                if (!this.field[i][j].active && isHexInsideBounds(this.field[i][j]) && this.field[i][j].numberOfActiveHexesNearby() == 6) {
                    activateHex(this.field[i][j], getRandomColor());
                }
                j++;
            }
            i++;
        }
    }

    protected boolean isHexInsideBounds(Hex hex) {
        PointYio pos = hex.getPos();
        return ((double) pos.f144x) > ((double) this.f98w) * 0.1d && ((double) pos.f144x) < ((double) this.boundWidth) - (((double) this.f98w) * 0.1d) && ((double) pos.f145y) > 0.15d * ((double) this.f97h) && ((double) pos.f145y) < ((double) this.boundHeight) - (((double) this.f97h) * 0.1d);
    }

    protected int numberOfIslandsByLevelSize() {
        switch (this.gameController.fieldController.levelSize) {
            case 2:
                return 4;
            case 4:
                return 7;
            default:
                return 2;
        }
    }

    protected void createLand() {
        while (!isGood()) {
            clearHexes();
            int N = numberOfIslandsByLevelSize();
            for (int i = 0; i < N; i++) {
                Hex hex = getRandomHex();
                this.islandCenters.add(hex.getPos());
                spawnIsland(hex, 7);
            }
            uniteIslandsWithRoads();
            centerLand();
            cutOffHexesOutsideOfBounds();
        }
    }

    protected void endGeneration() {
        this.gameController.fieldController.emptyHex.active = false;
    }

    protected void beginGeneration() {
        this.gameController.fieldController.createFieldMatrix();
        this.islandCenters = new ArrayList();
        this.links = new ArrayList();
    }
}
