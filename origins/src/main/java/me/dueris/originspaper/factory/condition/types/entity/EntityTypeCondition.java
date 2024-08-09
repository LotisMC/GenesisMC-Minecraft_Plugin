package me.dueris.originspaper.factory.condition.types.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.condition.ConditionFactory;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EntityTypeCondition {

	public static @NotNull ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			OriginsPaper.apoliIdentifier("entity_type"),
			InstanceDefiner.instanceDefiner()
				.add("entity_type", SerializableDataTypes.ENTITY_TYPE),
			(data, entity) -> {
				return entity.getType() == data.get("entity_type");
			}
		);
	}
}