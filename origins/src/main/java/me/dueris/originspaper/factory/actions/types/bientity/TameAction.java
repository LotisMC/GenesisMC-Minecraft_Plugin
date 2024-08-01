package me.dueris.originspaper.factory.actions.types.bientity;

import io.github.dueris.calio.parser.InstanceDefiner;
import io.github.dueris.calio.parser.reader.DeserializedFactoryJson;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.actions.ActionFactory;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;

public class TameAction {

	public static void action(DeserializedFactoryJson data, Tuple<Entity, Entity> actorAndTarget) {

		Entity actor = actorAndTarget.getA();
		Entity target = actorAndTarget.getB();

		if (!(actor instanceof Player actorPlayer)) {
			return;
		}

		if (target instanceof TamableAnimal tameableTarget && !tameableTarget.isTame()) {
			tameableTarget.tame(actorPlayer);
		}

		else if (target instanceof AbstractHorse targetHorseLike && !targetHorseLike.isTamed()) {
			targetHorseLike.tameWithName(actorPlayer);
		}

	}

	public static ActionFactory<Tuple<Entity, Entity>> getFactory() {
		return new ActionFactory<>(
			OriginsPaper.apoliIdentifier("tame"),
			InstanceDefiner.instanceDefiner(),
			TameAction::action
		);
	}
}
