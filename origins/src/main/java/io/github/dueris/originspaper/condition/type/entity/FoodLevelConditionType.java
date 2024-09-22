package io.github.dueris.originspaper.condition.type.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.OriginsPaper;
import io.github.dueris.originspaper.condition.factory.ConditionTypeFactory;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.types.Comparison;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FoodLevelConditionType {

	public static boolean condition(Entity entity, Comparison comparison, int compareTo) {
		return entity instanceof Player player
			&& comparison.compare(player.getFoodData().getFoodLevel(), compareTo);
	}

	public static @NotNull ConditionTypeFactory<Entity> getFactory() {
		return new ConditionTypeFactory<>(
			OriginsPaper.apoliIdentifier("food_level"),
			new SerializableData()
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.INT),
			(data, entity) -> condition(entity,
				data.get("comparison"),
				data.get("compare_to")
			)
		);
	}

}
