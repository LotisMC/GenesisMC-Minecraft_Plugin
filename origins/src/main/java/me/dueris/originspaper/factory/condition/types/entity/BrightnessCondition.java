package me.dueris.originspaper.factory.condition.types.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import io.github.dueris.calio.parser.reader.DeserializedFactoryJson;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.data.ApoliDataTypes;
import me.dueris.originspaper.data.types.Comparison;
import me.dueris.originspaper.factory.condition.ConditionFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BrightnessCondition {

	public static boolean condition(@NotNull DeserializedFactoryJson data, @NotNull Entity entity) {
		Level world = entity.level();

		Comparison comparison = data.get("comparison");
		float compareTo = data.get("compare_to");

		BlockPos blockPos = BlockPos.containing(entity.getX(), entity.getY() + entity.getEyeHeight(entity.getPose()), entity.getZ());
		float brightness = world.getLightLevelDependentMagicValue(blockPos);

		return comparison.compare(brightness, compareTo);

	}

	public static @NotNull ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			OriginsPaper.apoliIdentifier("brightness"),
			InstanceDefiner.instanceDefiner()
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.FLOAT),
			BrightnessCondition::condition
		);
	}
}