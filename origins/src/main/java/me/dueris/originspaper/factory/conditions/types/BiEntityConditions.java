package me.dueris.originspaper.factory.conditions.types;

import io.github.dueris.calio.util.holder.Pair;
import me.dueris.calio.data.factory.FactoryElement;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.conditions.ConditionFactory;
import me.dueris.originspaper.factory.conditions.meta.MetaConditions;
import me.dueris.originspaper.factory.data.types.Comparison;
import me.dueris.originspaper.factory.data.types.RotationType;
import me.dueris.originspaper.factory.powers.apoli.EntitySetPower;
import me.dueris.originspaper.factory.powers.apoli.PreventEntityRender;
import me.dueris.originspaper.registry.Registries;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BiEntityConditions implements Listener {
	public static void registerConditions() {
		MetaConditions.register(Registries.BIENTITY_CONDITION, BiEntityConditions::register);
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("both"), (data, pair) -> {
			AtomicBoolean a = new AtomicBoolean(true);
			AtomicBoolean t = new AtomicBoolean(true);
			a.set(ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.first()));
			t.set(ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.second()));
			return a.get() && t.get();
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("either"), (data, pair) -> {
			AtomicBoolean a = new AtomicBoolean(true);
			AtomicBoolean t = new AtomicBoolean(true);
			a.set(ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.first()));
			t.set(ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.second()));
			return a.get() || t.get();
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("invert"), (data, pair) -> ConditionExecutor.testBiEntity(data.getJsonObject("condition"), pair.second(), pair.first())));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("undirected"), (data, pair) -> {
			AtomicBoolean a = new AtomicBoolean(true);
			AtomicBoolean b = new AtomicBoolean(true);
			a.set(ConditionExecutor.testBiEntity(data.getJsonObject("condition"), pair.first(), pair.second()));
			b.set(ConditionExecutor.testBiEntity(data.getJsonObject("condition"), pair.second(), pair.first()));
			return a.get() || b.get();
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("actor_condition"), (data, pair) -> ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.first())));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("target_condition"), (data, pair) -> ConditionExecutor.testEntity(data.getJsonObject("condition"), pair.second())));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("equal"), (data, pair) -> pair.first() == pair.second()));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("in_entity_set"), (data, pair) -> EntitySetPower.isInEntitySet(pair.second(), data.getString("set"))));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("can_see"), (data, pair) -> PreventEntityRender.canSeeEntity(pair.first(), pair.second(), data)));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("distance"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null) {
				Comparison comparison = Comparison.fromString(data.getString("comparison"));
				double compareTo = data.getNumber("compare_to").getDouble();
				compareTo *= compareTo;
				return comparison.compare(actor.position().distanceToSqr(target.position()), compareTo);
			} else {
				return false;
			}
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("owner"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null) {
				return target instanceof OwnableEntity tamable && actor.equals(tamable.getOwner())
					|| target instanceof TraceableEntity ownable && actor.equals(ownable.getOwner());
			} else {
				return false;
			}
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("riding"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			return actor != null && target != null && actor.getVehicle() != null && actor.getVehicle().equals(target);
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("riding_root"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			return actor != null && target != null && actor.isPassenger() && actor.getRootVehicle().equals(target);
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("riding_recursive"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null && actor.isPassenger()) {
				net.minecraft.world.entity.Entity vehicle = actor.getVehicle();

				while (vehicle != null && !vehicle.equals(target)) {
					vehicle = vehicle.getVehicle();
				}

				return target.equals(vehicle);
			} else {
				return false;
			}
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("attack_target"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null) {
				return actor instanceof Mob mobActor && target.equals(mobActor.getTarget())
					|| actor instanceof NeutralMob angerableActor && target.equals(angerableActor.getTarget());
			} else {
				return false;
			}
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("attacker"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null) {
				return target instanceof LivingEntity livingTarget && actor.equals(livingTarget.getLastHurtByMob());
			} else {
				return false;
			}
		}));
		register(new ConditionFactory(OriginsPaper.apoliIdentifier("relative_rotation"), (data, pair) -> {
			net.minecraft.world.entity.Entity actor = pair.first().getHandle();
			net.minecraft.world.entity.Entity target = pair.second().getHandle();
			if (actor != null && target != null) {
				RotationType actorRotationType = data.getEnumValueOrDefault("actor_rotation", RotationType.class, RotationType.HEAD);
				RotationType targetRotationType = data.getEnumValueOrDefault("target_rotation", RotationType.class, RotationType.BODY);
				Vec3 actorRotation = actorRotationType.getRotation(actor);
				Vec3 targetRotation = targetRotationType.getRotation(target);
				List<Axis> axesList = new ArrayList<>();
				if (data.isPresent("axes")) {
					axesList.addAll(data.getJsonArray("axes").asList().stream().map(FactoryElement::getString).map(String::toUpperCase).map(Axis::valueOf).toList());
				}

				EnumSet<Axis> axes = axesList.isEmpty() ? EnumSet.copyOf(Arrays.stream(Axis.values()).toList()) : EnumSet.copyOf(axesList);
				actorRotation = RotationType.reduceAxes(actorRotation, axes);
				targetRotation = RotationType.reduceAxes(targetRotation, axes);
				Comparison comparison = Comparison.fromString(data.getString("comparison"));
				double compareTo = data.getNumber("compare_to").getDouble();
				double angle = RotationType.getAngleBetween(actorRotation, targetRotation);
				return comparison.compare(angle, compareTo);
			} else {
				return false;
			}
		}));
	}

	public static void register(@NotNull ConditionFactory<Pair<Entity, Entity>> factory) {
		OriginsPaper.getPlugin().registry.retrieve(Registries.BIENTITY_CONDITION).register(factory, factory.getSerializerId());
	}

}