package me.dueris.genesismc.factory.powers.OriginsMod.prevent;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.OriginContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;

import static me.dueris.genesismc.factory.powers.OriginsMod.prevent.PreventSuperClass.prevent_death;

public class PreventDeath extends CraftPower implements Listener {
    @EventHandler
    public void run(PlayerDeathEvent e){
        if(prevent_death.contains(e.getPlayer())){
            for(OriginContainer origin : OriginPlayer.getOrigin(e.getPlayer()).values()){
                ConditionExecutor conditionExecutor = new ConditionExecutor();
                if(conditionExecutor.check("damage_condition", "damage_conditions", e.getPlayer(), origin, "origins:prevent_death", e.getPlayer().getLastDamageCause(), e.getPlayer())){
                    e.setCancelled(true);
                }
            }
        }
    }

    @Override
    public void run() {

    }

    @Override
    public String getPowerFile() {
        return "origins:prevent_death";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return prevent_death;
    }
}