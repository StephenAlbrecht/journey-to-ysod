package com.watabou.pixeldungeon.levels;

import com.watabou.noosa.Scene;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.actors.mobs.npcs.Crewmate;

public class CrewLevel extends RegularLevel {

    {
        color1 = 0x48763c;
        color2 = 0x59994a;
    }

    @Override
    public String tilesTex() { return Assets.TILES_SEWERS; }

    @Override
    public String waterTex() { return Assets.WATER_SEWERS; }

    protected boolean[] water() {
        return Patch.generate(feeling == Feeling.WATER ? 0.60f : 0.45f, 5);
    }

    protected boolean[] grass() {
        return Patch.generate(feeling == Feeling.GRASS ? 0.60f : 0.40f, 4);
    }

    @Override
    protected void decorate() {}

    @Override
    protected void createMobs() {
        super.createMobs();
        Crewmate.Quest.spawn(this, roomEntrance);
    }

    @Override
    protected void createItems() {}

    @Override
    public void addVisuals(Scene scene) {
        super.addVisuals(scene);
        addVisuals(this, scene);
    }

    public static void addVisuals(Level level, Scene scene) {}

    @Override
    public String tileName(int tile) {
        if (tile == Terrain.WATER) {
            return "Slick oil";
        }
        return super.tileName(tile);
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.EMPTY_DECO:
                return "Thick red liquid covers the floor. It looks like blood.";
            case Terrain.BOOKSHELF:
                return "The dense rack of machinery flashes and beeps.";
            default:
                return super.tileDesc(tile);
        }
    }
}
