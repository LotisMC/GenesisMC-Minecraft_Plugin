package me.dueris.originspaper.factory.action.types.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import io.github.dueris.calio.parser.reader.DeserializedFactoryJson;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.data.ApoliDataTypes;
import me.dueris.originspaper.data.types.Shape;
import me.dueris.originspaper.factory.action.ActionFactory;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AreaOfEffectAction {

	public static void action(@NotNull DeserializedFactoryJson data, @NotNull Entity entity) {

		Consumer<Tuple<Entity, Entity>> biEntityAction = data.get("bientity_action");
		Predicate<Tuple<Entity, Entity>> biEntityCondition = data.get("bientity_condition");
		Shape shape = data.get("shape");

		boolean includeActor = data.get("include_actor");
		double radius = data.get("radius");

		for (Entity target : Shape.getEntities(shape, entity.level(), entity.getPosition(1.0f), radius)) {

			if (target == entity && !includeActor) {
				continue;
			}

			Tuple<Entity, Entity> actorAndTarget = new Tuple<>(entity, target);
			if (biEntityCondition == null || biEntityCondition.test(actorAndTarget)) {
				biEntityAction.accept(actorAndTarget);
			}

		}

	}

	public static @NotNull ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			OriginsPaper.apoliIdentifier("area_of_effect"),
			InstanceDefiner.instanceDefiner()
				.add("radius", SerializableDataTypes.DOUBLE, 16D)
				.add("shape", SerializableDataTypes.enumValue(Shape.class), Shape.CUBE)
				.add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
				.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
				.add("include_target", SerializableDataTypes.BOOLEAN, false)
				.add("include_actor", SerializableDataTypes.BOOLEAN, false),
			AreaOfEffectAction::action
		);
	}
}