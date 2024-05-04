package me.dueris.genesismc.factory.powers.apoli;

import me.dueris.calio.builder.inst.factory.FactoryElement;
import me.dueris.calio.builder.inst.factory.FactoryJsonObject;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.OriginChangeEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.registry.registries.Layer;
import me.dueris.genesismc.registry.registries.Power;
import me.dueris.genesismc.util.entity.OriginPlayerAccessor;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class StackingStatusEffect extends CraftPower implements Listener {
    public static PotionEffectType getPotionEffectType(String effectString) {
	if (effectString == null) {
	    return null;
	}
	return PotionEffectType.getByKey(NamespacedKey.fromString(effectString));
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		runExecution(e.getPlayer());
	    }
	}.runTaskTimer(GenesisMC.getPlugin(), 0, 40);
    }

    @EventHandler
    public void lol(OriginChangeEvent e) {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		runExecution(e.getPlayer());
	    }
	}.runTaskTimer(GenesisMC.getPlugin(), 0, 40);
    }

    public void runExecution(Player p) {
	if (getPlayersWithPower().contains(p)) {
	    for (Layer layer : CraftApoli.getLayersFromRegistry()) {
		for (Power power : OriginPlayerAccessor.getMultiPowerFileFromType(p, getType(), layer)) {
		    if (ConditionExecutor.testEntity(power.getJsonObject("condition"), (CraftEntity) p) && ConditionExecutor.testEntity(power.getJsonObject("entity_condition"), (CraftEntity) p)) {
			setActive(p, power.getTag(), true);
			applyStackingEffect(p, power);
		    } else {
			setActive(p, power.getTag(), false);
		    }
		}
	    }
	}
    }

    private void applyStackingEffect(Player player, Power power) {
	for (FactoryJsonObject effect : power.getList$SingularPlural("effect", "effects").stream().map(FactoryElement::toJsonObject).toList()) {
	    PotionEffectType potionEffectType = getPotionEffectType(effect.getString("effect"));
	    if (potionEffectType != null) {
		try {
		    player.addPotionEffect(new PotionEffect(potionEffectType, 50, 1, false, false, true));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    } else {
		Bukkit.getLogger().warning("Unknown effect ID: " + effect.getString("effect"));
	    }
	}
	player.sendHealthUpdate();
    }


    @Override
    public String getType() {
	return "apoli:stacking_status_effect";
    }

    @Override
    public ArrayList<Player> getPlayersWithPower() {
	return stacking_status_effect;
    }
}
