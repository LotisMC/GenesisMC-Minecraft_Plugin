package me.dueris.genesismc.factory.powers.apoli.provider.origins;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.PowerUpdateEvent;
import me.dueris.genesismc.factory.CraftApoli;
import me.dueris.genesismc.factory.powers.apoli.provider.PowerProvider;
import me.dueris.genesismc.util.entity.PowerHolderComponent;
import me.dueris.genesismc.util.entity.PowerUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SlimelingSizeChangers implements Listener {

	@EventHandler
	public void onRejoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();
				if (!PowerHolderComponent.hasPower(p, "origins:slime_skin")) return;
				double curSize = p.getAttribute(Attribute.GENERIC_SCALE).getBaseValue();
				double healthScale = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

				if (curSize >= 1.33) healthScale = 26;
				else if (curSize >= 1.2) healthScale = 22;
				else if (curSize >= 1.0) healthScale = 20;
				else if (curSize >= 0.8) healthScale = 16;
				else if (curSize >= 0.7) healthScale = 14;
				else healthScale = 14;

				p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(curSize);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(healthScale);
			}
		}.runTaskLater(GenesisMC.getPlugin(), 20);
	}

	@EventHandler
	public void respawn(PlayerPostRespawnEvent e) {
		Player p = e.getPlayer();
		if (!PowerHolderComponent.hasPower(p, "origins:slime_skin")) return;
		p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(1);
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
	}

	public static class AddSize implements Listener, PowerProvider {
		protected static NamespacedKey powerReference = GenesisMC.originIdentifier("slimeling_addsize");

		@EventHandler(priority = EventPriority.HIGHEST)
		public void powerGrant(PowerUpdateEvent e) {
			if (!e.isRemoved() && (e.getPower().getTag().equalsIgnoreCase(powerReference.asString()) || e.getPower().getTag().equalsIgnoreCase("origins:slime_skin"))) {
				Player p = e.getPlayer();
				double curSize = p.getAttribute(Attribute.GENERIC_SCALE).getBaseValue();
				double healthScale = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				if (e.getPower().getTag().equalsIgnoreCase(powerReference.asString())) {
					if (curSize < 1.33) {
						curSize = Math.min(1.33, curSize + 0.1);
					}

					new BukkitRunnable() {
						@Override
						public void run() {
							try {
								PowerUtils.removePower(Bukkit.getConsoleSender(), e.getPower(), p, CraftApoli.getLayerFromTag("origins:origin"), false);
							} catch (InstantiationException | IllegalAccessException ex) {
								throw new RuntimeException(ex);
							}
						}
					}.runTaskLater(GenesisMC.getPlugin(), 1);
				}

				if (curSize >= 1.33) healthScale = 26;
				else if (curSize >= 1.2) healthScale = 22;
				else if (curSize >= 1.0) healthScale = 20;
				else if (curSize >= 0.8) healthScale = 16;
				else if (curSize >= 0.7) healthScale = 14;
				else healthScale = 14;

				p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(curSize);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(healthScale);
			}
		}
	}

	public static class RemoveSize implements Listener, PowerProvider {
		protected static NamespacedKey powerReference = GenesisMC.originIdentifier("slimeling_removesize");

		@EventHandler(priority = EventPriority.HIGHEST)
		public void powerGrant(PowerUpdateEvent e) {
			if (!e.isRemoved() && (e.getPower().getTag().equalsIgnoreCase(powerReference.asString()) || e.getPower().getTag().equalsIgnoreCase("origins:slime_skin"))) {
				Player p = e.getPlayer();
				double curSize = p.getAttribute(Attribute.GENERIC_SCALE).getBaseValue();
				double healthScale = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
				if (e.getPower().getTag().equalsIgnoreCase(powerReference.asString())) {
					if (curSize > 0.66) {
						curSize = Math.max(0.66, curSize - 0.1);
					}

					new BukkitRunnable() {
						@Override
						public void run() {
							try {
								PowerUtils.removePower(Bukkit.getConsoleSender(), e.getPower(), p, CraftApoli.getLayerFromTag("origins:origin"), false);
							} catch (InstantiationException | IllegalAccessException ex) {
								throw new RuntimeException(ex);
							}
						}
					}.runTaskLater(GenesisMC.getPlugin(), 1);
				}

				if (curSize >= 1.33) healthScale = 26;
				else if (curSize >= 1.2) healthScale = 22;
				else if (curSize >= 1.0) healthScale = 20;
				else if (curSize >= 0.8) healthScale = 16;
				else if (curSize >= 0.7) healthScale = 14;
				else healthScale = 14;

				p.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(curSize);
				p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(healthScale);
			}
		}
	}
}