package me.dueris.genesismc.factory.powers.apoli;

import com.google.gson.JsonObject;
import me.dueris.calio.data.FactoryData;
import me.dueris.calio.data.annotations.Register;
import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.calio.data.types.OptionalInstance;
import me.dueris.calio.data.types.RequiredInstance;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.event.KeybindTriggerEvent;
import me.dueris.genesismc.factory.data.types.HudRender;
import me.dueris.genesismc.factory.data.types.JsonKeybind;
import me.dueris.genesismc.factory.powers.holder.PowerType;
import me.dueris.genesismc.util.KeybindingUtils;
import net.minecraft.core.particles.ParticleTypes;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static me.dueris.genesismc.factory.powers.apoli.FireProjectile.in_continuous;

public class Launch extends PowerType implements Listener, CooldownPower, KeyedPower {
	private final int cooldown;
	private final float speed;
	private final HudRender hudRender;
	private final Sound sound;
	private final JsonKeybind keybind;

	@Register
	public Launch(String name, String description, boolean hidden, FactoryJsonObject condition, int loading_priority, int cooldown, float speed, FactoryJsonObject hudRender, Sound sound, FactoryJsonObject keybind) {
		super(name, description, hidden, condition, loading_priority);
		this.cooldown = cooldown;
		this.speed = speed;
		this.hudRender = HudRender.createHudRender(hudRender);
		this.sound = sound;
		this.keybind = JsonKeybind.createJsonKeybind(keybind);
	}

	public static FactoryData registerComponents(FactoryData data) {
		return PowerType.registerComponents(data).ofNamespace(GenesisMC.apoliIdentifier("launch"))
			.add("cooldown", int.class, 1)
			.add("speed", float.class, new RequiredInstance())
			.add("hud_render", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("sound", Sound.class, new OptionalInstance())
			.add("key", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()));
	}

	@EventHandler
	public void inContinuousFix(KeybindTriggerEvent e) {
		Player p = e.getPlayer();
		if (getPlayers().contains(p)) {
			if (KeybindingUtils.isKeyActive(getJsonKey().getKey(), p)) {
				in_continuous.putIfAbsent(p, new ArrayList<>());
			}
		}
	}

	@EventHandler
	public void keybindToggle(KeybindTriggerEvent e) {
		Player p = e.getPlayer();
		if (getPlayers().contains(p)) {
			if (isActive(p)) {
				if (!Cooldown.isInCooldown(p, this)) {
					if (KeybindingUtils.isKeyActive(getJsonKey().getKey(), p)) {
						String key = getJsonKey().getKey();
						final int[] times = {-1};
						new BukkitRunnable() {
							@Override
							public void run() {
								if (times[0] >= 0) {
									/* Launch power doesnt execute continuously */
									if (!in_continuous.get(p).contains(key)) {
										Cooldown.addCooldown(p, cooldown, getSelf());
										this.cancel();
										return;
									}
								}
								Cooldown.addCooldown(p, cooldown, getSelf());
								p.setVelocity(p.getVelocity().setY(p.getVelocity().getY() + speed));
								((CraftWorld) p.getWorld()).getHandle().sendParticles(ParticleTypes.CLOUD, p.getX(), p.getY(), p.getZ(), 8, ((CraftPlayer) p).getHandle().getRandom().nextGaussian(), 0.0D, ((CraftPlayer) p).getHandle().getRandom().nextGaussian(), 0.5);
								if (sound != null) {
									p.getWorld().playSound(p, sound, 0.5F, 0.4F / (((CraftPlayer) p).getHandle().getRandom().nextFloat() * 0.4F + 0.8F));
								}
								times[0]++;
							}
						}.runTaskTimer(GenesisMC.getPlugin(), 1L, 1L);
					}
				}
			}
		}
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public JsonKeybind getJsonKey() {
		return keybind;
	}

	@Override
	public HudRender getHudRender() {
		return hudRender;
	}

	public float getSpeed() {
		return speed;
	}

	public Sound getSound() {
		return sound;
	}

	private Launch getSelf() {
		return this;
	}
}
