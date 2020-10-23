package com.watabou.pixeldungeon.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.pixeldungeon.Dungeon;
import com.watabou.pixeldungeon.actors.hero.Hero;
import com.watabou.pixeldungeon.actors.mobs.npcs.Crewmate;
import com.watabou.pixeldungeon.items.Item;
import com.watabou.pixeldungeon.items.keys.GoldenKey;
import com.watabou.pixeldungeon.items.keys.Key;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.sprites.ItemSprite;
import com.watabou.pixeldungeon.ui.Window;
import com.watabou.pixeldungeon.utils.GLog;
import com.watabou.pixeldungeon.utils.Utils;

public class WndCrewmate extends Window {

    private static final String TXT_MESSAGE =
        "I thought you were going to leave me here... Thank you so much. Here, take this access card. " +
        "It will let you go through the greenhouse. I'm going to see if there are any escape pods left. " +
        "I can try to wait for you... but no promises.";

    private static final String TXT_FAREWELL = "Good luck, friend!";

    private static final int WIDTH = 120;
    private static final float GAP = 2;

    public WndCrewmate(final Crewmate crewmate, final Item item) {

        super();

        IconTitle titlebar = new IconTitle();
        titlebar.icon(new ItemSprite(item.image(), null));
        titlebar.label(Utils.capitalize(item.name()));
        titlebar.setRect(0, 0, WIDTH, 0);
        add(titlebar);

        BitmapTextMultiline message = PixelScene.createMultiline(TXT_MESSAGE, 6);
        message.maxWidth = WIDTH;
        message.measure();
        message.y = titlebar.bottom() + GAP;
        add(message);

        // Add reward buttons


        resize(WIDTH, (int)(message.height));

        giveReward(crewmate, item, Crewmate.Quest.key);
    }

    private void giveReward(Crewmate crewmate, Item item, GoldenKey reward) {

        // FIXME Causes nullptr here but not in WndWandmaker
        // hide();

        item.detach(Dungeon.hero.belongings.backpack);

        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.name());
        } else {
            Dungeon.level.drop(reward, crewmate.pos).sprite.drop();
        }

        crewmate.yell(Utils.format(TXT_FAREWELL, Dungeon.hero.className()));
        crewmate.destroy();
        crewmate.sprite.die();
        Crewmate.Quest.complete();
    }


}
