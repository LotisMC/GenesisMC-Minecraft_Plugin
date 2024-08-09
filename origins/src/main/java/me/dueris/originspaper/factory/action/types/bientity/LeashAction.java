package me.dueris.originspaper.factory.action.types.bientity;

import io.github.dueris.calio.parser.InstanceDefiner;
import io.github.dueris.calio.parser.reader.DeserializedFactoryJson;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.action.ActionFactory;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class LeashAction {

	public static void action(DeserializedFactoryJson data, @NotNull Tuple<Entity, Entity> actorAndTarget) {

		Entity actor = actorAndTarget.getA();
		Entity target = actorAndTarget.getB();

		if (actor == null || !(target instanceof Mob mobTarget)) {
			return;
		}

		if (!mobTarget.isLeashed()) {
			mobTarget.setLeashedTo(actor, true);
		}

	}

	public static @NotNull ActionFactory<Tuple<Entity, Entity>> getFactory() {
		return new ActionFactory<>(
			OriginsPaper.apoliIdentifier("leash"),
			InstanceDefiner.instanceDefiner(),
			LeashAction::action
		);
	}
}