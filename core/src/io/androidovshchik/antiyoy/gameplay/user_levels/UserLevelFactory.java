package io.androidovshchik.antiyoy.gameplay.user_levels;

import java.util.ArrayList;
import java.util.Iterator;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevAfrica1936;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevAlmostFair;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevAtlantidaWar;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevBlackForest;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevGreatColonization;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevIslandOfDeath;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevKingOfTheIsland;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevLeBlanc;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevMinasTirith;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevPurpleWiggles;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevTheWeb;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevTheophileDugue;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevTwoForOne;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevWarOnIsland;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevWoman;
import yio.tro.antiyoy.gameplay.user_levels.pack_four.UlevZeraXenonArena;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevCoastalCrescent;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevConquestOfItalicPeninsula;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevExample1;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevExample2;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevExample3;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevHansJurgen;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevHumeniuk;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevLattice;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevMirage212;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevOlegDonskih1;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevOlegDonskih2;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevPuhtaytoe;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevSixFlags;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevSteveUpsideDown;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevVladSender1;
import yio.tro.antiyoy.gameplay.user_levels.pack_one.UlevVladSender2;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevAbandonedValley;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevBackdoor;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevConquestOfBritain;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevInsideOut;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevManInTheMiddle;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevScaryWWII;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevSixKingdoms;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevSouthAmerica;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevSpiderTrap;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevStroke;
import yio.tro.antiyoy.gameplay.user_levels.pack_three.UlevTarget;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevAntsNest;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevAssymetry;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevBridgeFort;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevButterfly;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevFennia;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevHalma;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevJapanConquest;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevKWar;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevMeetInTheMiddle;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevOneOnOne;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevReikEpsilon;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevSevenEmpires;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevSorvixChallengeArena;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevSpiral;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevTwoLands;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevWarOfStrategy;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevWarOnTheRiver;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevWarToEndAllWars;
import yio.tro.antiyoy.gameplay.user_levels.pack_two.UlevWesternBalkan;

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
