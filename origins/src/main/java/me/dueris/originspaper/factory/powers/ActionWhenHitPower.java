package me.dueris.originspaper.factory.powers;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.data.ApoliDataTypes;
import me.dueris.originspaper.data.types.HudRender;
import me.dueris.originspaper.factory.action.ActionFactory;
import me.dueris.originspaper.factory.condition.ConditionFactory;
import me.dueris.originspaper.registry.registries.PowerType;
import me.dueris.originspaper.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActionWhenHitPower extends PowerType implements CooldownInterface {
	private final ActionFactory<Tuple<Entity, Entity>> bientityAction;
	private final ConditionFactory<Tuple<DamageSource, Float>> damageCondition;
	private final int cooldown;
	private final HudRender hudRender;
	private final ConditionFactory<Tuple<Entity, Entity>> bientityCondition;

	public ActionWhenHitPower(@NotNull ResourceLocation key, @NotNull ResourceLocation type, Component name, Component description, boolean hidden, ConditionFactory<Entity> condition, int loadingPriority,
							  ActionFactory<Tuple<Entity, Entity>> bientityAction, ConditionFactory<Tuple<DamageSource, Float>> damageCondition, int cooldown, HudRender hudRender, ConditionFactory<Tuple<Entity, Entity>> bientityCondition) {
		super(key, type, name, description, hidden, condition, loadingPriority);
		this.bientityAction = bientityAction;
		this.damageCondition = damageCondition;
		this.cooldown = cooldown;
		this.hudRender = hudRender;
		this.bientityCondition = bientityCondition;
	}

	public static InstanceDefiner buildDefiner() {
		return PowerType.buildDefiner().typedRegistry(OriginsPaper.apoliIdentifier("action_when_hit"))
			.add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
			.add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
			.add("cooldown", SerializableDataTypes.INT, 1)
			.add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
			.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null);
	}

	public boolean doesApply(@Nullable Entity attacker, Entity entity, DamageSource source, float amount) {
		return attacker != null
			&& !CooldownPower.isInCooldown(entity.getBukkitEntity(), this)
			&& (bientityCondition == null || bientityCondition.test(new Tuple<>(attacker, entity)))
			&& (damageCondition == null || damageCondition.test(new Tuple<>(source, amount)));
	}

	public void whenHit(Entity attacker, Entity entity) {
		this.bientityAction.accept(new Tuple<>(attacker, entity));
		CooldownPower.addCooldown(entity.getBukkitEntity(), cooldown, this);
	}

	@EventHandler
	public void whenHit(@NotNull EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player p) {
			net.minecraft.world.entity.player.Player player = ((CraftPlayer) p).getHandle();
			if (!getPlayers().contains(player)) return;
			Entity damager = ((CraftEntity) e.getDamager()).getHandle();
			DamageSource damageSource = Util.damageSourceFromBukkit(e.getDamageSource());
			if (doesApply(damager, player, damageSource, Double.valueOf(e.getDamage()).floatValue()) && isActive(player)) {
				whenHit(damager, player);
			}
		}
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public HudRender getHudRender() {
		return hudRender;
	}
}