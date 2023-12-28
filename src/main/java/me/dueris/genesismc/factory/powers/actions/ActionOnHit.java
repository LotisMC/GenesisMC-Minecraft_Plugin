package me.dueris.genesismc.factory.powers.actions;

import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.entity.OriginPlayerUtils;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionOnHit extends CraftPower {

    @Override
    public void run(Player p) {

    }

    @EventHandler
    public void action(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player p) {
            Entity actor = e.getDamager();
            Entity target = p;
            for (OriginContainer origin : OriginPlayerUtils.getOrigin(p).values()) {
                if (getPowerArray().contains(p)) {
                    for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                        if(GenesisMC.getConditionExecutor().check("condition", "conditions", p, power, getPowerFile(), actor, target, actor.getLocation().getBlock(), null, p.getActiveItem(), e)){
                            if(GenesisMC.getConditionExecutor().check("damage_condition", "damage_conditions", p, power, getPowerFile(), actor, target, actor.getLocation().getBlock(), null, p.getActiveItem(), e)){
                                if(GenesisMC.getConditionExecutor().check("bientity_condition", "bientity_conditions", p, power, getPowerFile(), actor, target, actor.getLocation().getBlock(), null, p.getActiveItem(), e)){
                                    if (power == null) continue;
                                    setActive(p, power.getTag(), true);
                                    Actions.biEntityActionType(actor, target, power.getBiEntityAction());
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            setActive(p, power.getTag(), false);
                                        }
                                    }.runTaskLater(GenesisMC.getPlugin(), 2L);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getPowerFile() {
        return "origins:action_on_hit";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return action_on_hit;
    }

    @Override
    public void setActive(Player p, String tag, Boolean bool) {
        if(powers_active.containsKey(p)){
            if(powers_active.get(p).containsKey(tag)){
                powers_active.get(p).replace(tag, bool);
            }else{
                powers_active.get(p).put(tag, bool);
            }
        }else{
            powers_active.put(p, new HashMap());
            setActive(p, tag, bool);
        }
    }
}