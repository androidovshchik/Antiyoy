package io.androidovshchik.antiyoy.gameplay.user_levels;

import java.util.ArrayList;
import java.util.Iterator;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevAfrica1936;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevAlmostFair;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevAtlantidaWar;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevBlackForest;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevGreatColonization;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevIslandOfDeath;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevKingOfTheIsland;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevLeBlanc;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevMinasTirith;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevPurpleWiggles;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevTheWeb;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevTheophileDugue;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevTwoForOne;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevWarOnIsland;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevWoman;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_four.UlevZeraXenonArena;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevCoastalCrescent;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevConquestOfItalicPeninsula;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevExample1;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevExample2;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevExample3;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevHansJurgen;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevHumeniuk;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevLattice;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevMirage212;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevOlegDonskih1;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevOlegDonskih2;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevPuhtaytoe;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevSixFlags;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevSteveUpsideDown;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevVladSender1;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_one.UlevVladSender2;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevAbandonedValley;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevBackdoor;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevConquestOfBritain;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevInsideOut;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevManInTheMiddle;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevScaryWWII;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevSixKingdoms;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevSouthAmerica;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevSpiderTrap;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevStroke;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_three.UlevTarget;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevAntsNest;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevAssymetry;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevBridgeFort;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevButterfly;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevFennia;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevHalma;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevJapanConquest;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevKWar;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevMeetInTheMiddle;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevOneOnOne;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevReikEpsilon;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevSevenEmpires;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevSorvixChallengeArena;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevSpiral;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevTwoLands;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevWarOfStrategy;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevWarOnTheRiver;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevWarToEndAllWars;
import io.androidovshchik.antiyoy.gameplay.user_levels.pack_two.UlevWesternBalkan;

public class UserLevelFactory {
    private static UserLevelFactory instance;
    ArrayList<AbstractUserLevel> levels = new ArrayList();

    public static void initialize() {
        instance = null;
    }

    public static UserLevelFactory getInstance() {
        if (instance == null) {
            instance = new UserLevelFactory();
        }
        return instance;
    }

    public UserLevelFactory() {
        initLevels();
    }

    private void initLevels() {
        add(new UlevExample1());
        add(new UlevExample2());
        add(new UlevExample3());
        add(new UlevTheWeb());
        add(new UlevKingOfTheIsland());
        add(new UlevZeraXenonArena());
        add(new UlevBlackForest());
        add(new UlevLeBlanc());
        add(new UlevAlmostFair());
        add(new UlevPurpleWiggles());
        add(new UlevTwoForOne());
        add(new UlevTheophileDugue());
        add(new UlevMinasTirith());
        add(new UlevGreatColonization());
        add(new UlevAfrica1936());
        add(new UlevIslandOfDeath());
        add(new UlevWarOnIsland());
        add(new UlevWoman());
        add(new UlevAtlantidaWar());
        add(new UlevSpiderTrap());
        add(new UlevManInTheMiddle());
        add(new UlevSixKingdoms());
        add(new UlevInsideOut());
        add(new UlevTarget());
        add(new UlevSouthAmerica());
        add(new UlevBackdoor());
        add(new UlevStroke());
        add(new UlevConquestOfBritain());
        add(new UlevAbandonedValley());
        add(new UlevScaryWWII());
        add(new UlevSpiral());
        add(new UlevAssymetry());
        add(new UlevFennia());
        add(new UlevButterfly());
        add(new UlevSevenEmpires());
        add(new UlevSorvixChallengeArena());
        add(new UlevHalma());
        add(new UlevMeetInTheMiddle());
        add(new UlevAntsNest());
        add(new UlevTwoLands());
        add(new UlevWarOnTheRiver());
        add(new UlevWarOfStrategy());
        add(new UlevWarToEndAllWars());
        add(new UlevOneOnOne());
        add(new UlevBridgeFort());
        add(new UlevReikEpsilon());
        add(new UlevWesternBalkan());
        add(new UlevJapanConquest());
        add(new UlevKWar());
        add(new UlevPuhtaytoe());
        add(new UlevHumeniuk());
        add(new UlevHansJurgen());
        add(new UlevVladSender1());
        add(new UlevVladSender2());
        add(new UlevLattice());
        add(new UlevSixFlags());
        add(new UlevCoastalCrescent());
        add(new UlevSteveUpsideDown());
        add(new UlevOlegDonskih1());
        add(new UlevOlegDonskih2());
        add(new UlevMirage212());
        add(new UlevConquestOfItalicPeninsula());
    }

    private void add(AbstractUserLevel level) {
        this.levels.add(level);
    }

    public ArrayList<AbstractUserLevel> getLevels() {
        return this.levels;
    }

    public AbstractUserLevel getLevel(String key) {
        Iterator it = this.levels.iterator();
        while (it.hasNext()) {
            AbstractUserLevel level = (AbstractUserLevel) it.next();
            if (level.getKey().equals(key)) {
                return level;
            }
        }
        return null;
    }
}
