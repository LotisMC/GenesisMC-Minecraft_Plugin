package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.actions.Actions;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;

public class ActionOnBlockPlace extends CraftPower implements Listener {

    @EventHandler
    public void blockBreak(BlockPlaceEvent e) {
	if (action_on_block_place.contains(e.getPlayer())) {
	    for (Layer layer : CraftApoli.getLayersFromRegistry()) {
		for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(e.getPlayer(), getType(), layer)) {
		    if (!(ConditionExecutor.testEntity(power.getJsonObject("condition"), (CraftEntity) e.getPlayer()) && ConditionExecutor.testItem(power.getJsonObject("item_condition"), e.getItemInHand()) && ConditionExecutor.testBlock(power.getJsonObject("place_to_condition"), (CraftBlock) e.getBlockPlaced()) && ConditionExecutor.testBlock(power.getJsonObject("place_on_condition"), (CraftBlock) e.getBlockAgainst())))
			return;
		    e.setCancelled(true);
		    setActive(e.getPlayer(), power.getTag(), true);
		    Actions.executeEntity(e.getPlayer(), power.getJsonObject("entity_action"));
		    Actions.executeItem(e.getItemInHand(), power.getJsonObject("held_item_action"));
		    Actions.executeBlock(e.getBlockAgainst().getLocation(), power.getJsonObject("place_on_action"));
		    Actions.executeBlock(e.getBlockPlaced().getLocation(), power.getJsonObject("place_to_action"));
		    if (power.isPresent("result_stack")) {
			EdibleItem.runResultStack(power, true, e.getPlayer());
		    }
		}
	    }
	}
    }

    @Override
    public String getType() {
	return "apoli:action_on_block_place";
    }

    @Override
    public ArrayList<Player> getPlayersWithPower() {
	return action_on_block_place;
    }

}
