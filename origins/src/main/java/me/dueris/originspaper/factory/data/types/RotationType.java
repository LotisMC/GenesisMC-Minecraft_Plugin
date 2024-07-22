package me.dueris.originspaper.factory.data.types;

import me.dueris.calio.data.factory.FactoryJsonObject;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Function;

public enum RotationType {
	HEAD(e -> e.getViewVector(1.0F)),
	BODY(RotationType::getBodyRotationVector);

	private final Function<Entity, Vec3> function;

	RotationType(Function<Entity, Vec3> function) {
		this.function = function;
	}

	public static double getAngleBetween(Vec3 a, Vec3 b) {
		double dot = a.dot(b);
		return dot / (a.length() * b.length());
	}

	public static Vec3 reduceAxes(Vec3 vector, EnumSet<Axis> axesToKeep) {
		return new Vec3(axesToKeep.contains(Axis.X) ? vector.x : 0.0, axesToKeep.contains(Axis.Y) ? vector.y : 0.0, axesToKeep.contains(Axis.Z) ? vector.z : 0.0);
	}

	private static Vec3 getBodyRotationVector(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			float f = livingEntity.getXRot() * (float) (Math.PI / 180.0);
			float g = -livingEntity.getYRot() * (float) (Math.PI / 180.0);
			float h = Mth.cos(g);
			float i = Mth.sin(g);
			float j = Mth.cos(f);
			float k = Mth.sin(f);
			return new Vec3(i * j, -k, h * j);
		} else {
			return entity.getViewVector(1.0F);
		}
	}

	public static Vec3 parseDirection(FactoryJsonObject jsonObject) {
		return jsonObject != null && !jsonObject.isEmpty() ? VectorGetter.getNMSVector(jsonObject) : null;
	}

	public Vec3 getRotation(Entity entity) {
		return this.function.apply(entity);
	}
}
