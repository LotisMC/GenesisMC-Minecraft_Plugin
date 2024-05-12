package me.dueris.genesismc.factory.powers.apoli.provider.origins;

import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.factory.powers.apoli.provider.PowerProvider;
import me.dueris.genesismc.util.entity.PowerHolderComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PiglinNoAttack implements Listener, PowerProvider {
	protected static NamespacedKey powerReference = GenesisMC.originIdentifier("piglin_brothers");
	static ArrayList<EntityType> piglinValid = new ArrayList<>();
	static {
		piglinValid.add(EntityType.PIGLIN);
		piglinValid.add(EntityType.PIGLIN_BRUTE);
		piglinValid.add(EntityType.ZOMBIFIED_PIGLIN);
	}
	private final HashMap<Player, HashMap<Entity, Integer>> cooldowns = new HashMap<>();

	public void tick(Player p) {
		if (cooldowns.containsKey(p)) {
			for (Entity en : cooldowns.get(p).keySet()) {
				if (cooldowns.get(p).get(en) <= 1) {
					cooldowns.get(p).remove(en);
				} else {
					HashMap<Entity, Integer> map = new HashMap<>();
					map.put(en, cooldowns.get(p).get(en) - 1);
					cooldowns.put(p, map);
				}
			}
		}
	}

	@EventHandler
	public void target(EntityTargetEvent e) {
		if (piglinValid.contains(e.getEntity().getType())) {
			if (PowerHolderComponent.hasPower(e.getTarget(), powerReference.asString())) {
				if (!cooldowns.containsKey(e.getTarget())) {
					cooldowns.put((Player) e.getTarget(), new HashMap<>());
				}
				if (!cooldowns.get((Player) e.getTarget()).containsKey(e.getEntity())) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void damageEntity(EntityDamageByEntityEvent e) {
		if (piglinValid.contains(e.getEntity().getType())) {
			if (PowerHolderComponent.hasPower(e.getDamager(), powerReference.asString())) {
				Player p = (Player) e.getDamager();
				HashMap<Entity, Integer> map = new HashMap<>();
				map.put(e.getEntity(), 600);
				cooldowns.put(p, map);
			}
		}
	}

}
