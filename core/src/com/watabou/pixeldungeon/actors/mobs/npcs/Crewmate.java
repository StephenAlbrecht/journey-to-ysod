package com.watabou.pixeldungeon.actors.mobs.npcs;

import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.Journal;
import com.watabou.pixeldungeon.actors.Actor;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.actors.buffs.Buff;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.GoldenKey;
import com.watabou.pixeldungeon.items.potions.PotionOfHealing;
import com.watabou.pixeldungeon.items.wands.Wand;
import com.watabou.pixeldungeon.levels.CrewLevel;
import com.watabou.pixeldungeon.levels.PrisonLevel;
import com.watabou.pixeldungeon.levels.Room;
import com.watabou.pixeldungeon.levels.Terrain;
import com.watabou.pixeldungeon.scenes.GameScene;
import com.watabou.pixeldungeon.sprites.WandmakerSprite;
import com.watabou.pixeldungeon.utils.Utils;
import com.watabou.pixeldungeon.windows.WndCrewmate;
import com.watabou.pixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Crewmate extends NPC {

    {
        name = "crewmate";
        spriteClass = WandmakerSprite.class;
    }

    private static final String TXT_MED1 =
        "I thought I was the only one left... I'm hurt bad, man... Can you find me a _medkit_ to stop the " +
        "bleeding? I... think there's some medical supplies... somewhere in the bunks... I can show you a better way " +
        "out... Please don't let me die...";

    private static final String TXT_MED2 =
        "... There should be some medical supplies in the bunks... Please hurry...";

    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 1000;
    }

    @Override
    public String defenseVerb() {
        return "absorbed";
    }

    @Override
    public void damage(int dmg, Object src) {
    }

    @Override
    public void add(Buff buff) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    public void interact() {

        sprite.turnTo(pos, Dungeon.hero.pos);
        if (Quest.given) {

            Item item = Dungeon.hero.belongings.getItem(PotionOfHealing.class);

            if (item != null) {
                GameScene.show(new WndCrewmate(this, item));
            } else {
                tell(TXT_MED2, Dungeon.hero.className());
            }

        } else {
            tell(TXT_MED1);
            Quest.given = true;
            Quest.placeItem();
            Journal.add(Journal.Feature.CREWMATE);
        }

    }

    private void tell(String format, Object...args) {
        GameScene.show(new WndQuest(this, Utils.format(format, args)));
    }

    @Override
    public String description() {
        return "This man is slumped over and covered in blood. He trembles in a cold sweat." +
               "His eyes flutter in delirium.";
    }

    public static class Quest {

        private static boolean spawned;
        private static boolean alternative;
        private static boolean given;
        private static boolean processed;

        public static GoldenKey key;

        public static void reset() {
            spawned = false;
            key = null;
        }

        private static final String NODE        = "crewmate";
        private static final String SPAWNED     = "spawned";
        private static final String ALTERNATIVE    = "alternative";
        private static final String PROCESSED   = "processed";
        private static final String GIVEN       = "given";
        private static final String KEY         = "key";

        public static void storeInBundle(Bundle bundle) {

            Bundle node = new Bundle();

            node.put(SPAWNED, spawned);

            if (spawned) {
                node.put(ALTERNATIVE, alternative);
                node.put(PROCESSED, processed);
                node.put(GIVEN, given);
                node.put(KEY, key);
            }

            bundle.put(NODE, node);
        }

        public static void restoreFromBundle(Bundle bundle) {

            Bundle node = bundle.getBundle(NODE);

            if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

                alternative    = node.getBoolean(ALTERNATIVE);
                processed   = node.getBoolean(PROCESSED);
                given       = node.getBoolean(GIVEN);
                key         = (GoldenKey)node.get(KEY);

            } else {
                reset();
            }
        }

        public static void spawn(CrewLevel level, Room room) {
            if (!spawned) {

                Crewmate npc = new Crewmate();
                do {
                    npc.pos = room.random();
                } while (level.map[npc.pos] == Terrain.ENTRANCE || level.map[npc.pos] == Terrain.SIGN);
                level.mobs.add(npc);
                Actor.occupyCell(npc);

//                alternative = Random.Int(2) == 0;
                spawned     = true;
                alternative = false;
                given       = false;
                processed   = false;
                key         = new GoldenKey();
            }
        }

        public static void placeItem() {
            int medkitPos = Dungeon.level.randomRespawnCell();
            while (Dungeon.level.heaps.get(medkitPos) != null) {
                medkitPos = Dungeon.level.randomRespawnCell();
            }
            Dungeon.level.drop((new PotionOfHealing()).identify(), medkitPos);
        }

        public static void complete() {
            key = null;
            Journal.remove(Journal.Feature.CREWMATE);
        }
    }
}
