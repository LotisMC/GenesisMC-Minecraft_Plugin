package me.dueris.genesismc.factory.powers.player;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Swimming extends CraftPower {

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }

    Player p;

    public Swimming(){
        this.p = p;
    }

    @Override
    public void run(Player p) {
        for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
            if (swimming.contains(p)) {
                ConditionExecutor conditionExecutor = new ConditionExecutor();
                if (!conditionExecutor.check("condition", "conditions", p, origin, getPowerFile(), p, null, null, null, p.getItemInHand(), null)) {
                    if (origin.getPowerFileFromType(getPowerFile()) == null) {
                        getPowerArray().remove(p);
                        return;
                    }
                    if (!getPowerArray().contains(p)) return;
                    setActive(origin.getPowerFileFromType(getPowerFile()).getTag(), false);
                    return;
                } else {
                    p.setSwimming(true);
                    if (origin.getPowerFileFromType(getPowerFile()) == null) {
                        getPowerArray().remove(p);
                        return;
                    }
                    if (!getPowerArray().contains(p)) return;
                    setActive(origin.getPowerFileFromType(getPowerFile()).getTag(), true);
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "origins:swimming";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return swimming;
    }
}
