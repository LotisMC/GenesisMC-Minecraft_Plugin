package me.dueris.originspaper.factory.action.types.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.data.ApoliDataTypes;
import me.dueris.originspaper.data.types.Space;
import me.dueris.originspaper.factory.action.ActionFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class AddVelocityAction {

	public static @NotNull ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(OriginsPaper.apoliIdentifier("add_velocity"),
			InstanceDefiner.instanceDefiner()
				.add("x", SerializableDataTypes.FLOAT, 0F)
				.add("y", SerializableDataTypes.FLOAT, 0F)
				.add("z", SerializableDataTypes.FLOAT, 0F)
				.add("space", ApoliDataTypes.SPACE, Space.WORLD)
				.add("client", SerializableDataTypes.BOOLEAN, true)
				.add("server", SerializableDataTypes.BOOLEAN, true)
				.add("set", SerializableDataTypes.BOOLEAN, false),
			(data, entity) -> {
				if (entity instanceof Player
					&& (entity.level().isClientSide ?
					!data.getBoolean("client") : !data.getBoolean("server")))
					return;
				Space space = data.get("space");
				Vector3f vec = new Vector3f(data.getFloat("x"), data.getFloat("y"), data.getFloat("z"));
				TriConsumer<Float, Float, Float> method = entity::push;
				if (data.getBoolean("set")) {
					method = entity::setDeltaMovement;
				}
				space.toGlobal(vec, entity);
				method.accept(vec.x, vec.y, vec.z);
				entity.hurtMarked = true;
			}
		);
	}
}