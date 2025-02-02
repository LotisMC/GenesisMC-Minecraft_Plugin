package io.github.dueris.originspaper.action.type.bientity.meta;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.action.ActionConfiguration;
import io.github.dueris.originspaper.action.EntityAction;
import io.github.dueris.originspaper.action.type.BiEntityActionType;
import io.github.dueris.originspaper.action.type.BiEntityActionTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ActorActionBiEntityActionType extends BiEntityActionType {

	public static final TypedDataObjectFactory<ActorActionBiEntityActionType> DATA_FACTORY = TypedDataObjectFactory.simple(
		new SerializableData()
			.add("action", EntityAction.DATA_TYPE),
		data -> new ActorActionBiEntityActionType(
			data.get("action")
		),
		(actionType, serializableData) -> serializableData.instance()
			.set("action", actionType.action)
	);

	private final EntityAction action;

	public ActorActionBiEntityActionType(EntityAction action) {
		this.action = action;
	}

	@Override
	protected void execute(Entity actor, Entity target) {

		if (actor != null) {
			action.execute(actor);
		}

	}

	@Override
	public @NotNull ActionConfiguration<?> getConfig() {
		return BiEntityActionTypes.ACTOR_ACTION;
	}

}
