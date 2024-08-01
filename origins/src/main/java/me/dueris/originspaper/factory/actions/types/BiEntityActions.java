package me.dueris.originspaper.factory.actions.types;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.parser.InstanceDefiner;
import io.github.dueris.calio.util.holder.Pair;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.event.AddToSetEvent;
import me.dueris.originspaper.event.RemoveFromSetEvent;
import me.dueris.originspaper.factory.actions.ActionFactory;
import me.dueris.originspaper.factory.actions.meta.*;
import me.dueris.originspaper.factory.data.ApoliDataTypes;
import me.dueris.originspaper.registry.Registries;
import net.minecraft.world.entity.Entity;

// Left is the actor, right is the target.
public class BiEntityActions {

	public static void register(ActionFactory<Pair<Entity, Entity>> factory) {
		OriginsPaper.getPlugin().registry.retrieve(Registries.BIENTITY_ACTION).register(factory, factory.getSerializerId());
	}

	public static void registerAll() {
		register(AndAction.getFactory(SerializableDataTypes.list(ApoliDataTypes.BIENTITY_ACTION)));
		register(ChanceAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
		register(IfElseAction.getFactory(ApoliDataTypes.BIENTITY_ACTION, ApoliDataTypes.BIENTITY_CONDITION));
		register(ChoiceAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
		register(IfElseListAction.getFactory(ApoliDataTypes.BIENTITY_ACTION, ApoliDataTypes.BIENTITY_CONDITION));
		register(DelayAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
		register(NothingAction.getFactory());
		register(SideAction.getFactory(ApoliDataTypes.BIENTITY_ACTION, entities -> !entities.getLeft().level().isClientSide));
	}

}
