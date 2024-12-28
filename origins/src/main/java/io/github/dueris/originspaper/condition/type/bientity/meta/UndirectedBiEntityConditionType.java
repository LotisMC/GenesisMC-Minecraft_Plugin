package io.github.dueris.originspaper.condition.type.bientity.meta;

import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.condition.BiEntityCondition;
import io.github.dueris.originspaper.condition.ConditionConfiguration;
import io.github.dueris.originspaper.condition.type.BiEntityConditionType;
import io.github.dueris.originspaper.condition.type.BiEntityConditionTypes;
import io.github.dueris.originspaper.data.TypedDataObjectFactory;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class UndirectedBiEntityConditionType extends BiEntityConditionType {

	public static final TypedDataObjectFactory<UndirectedBiEntityConditionType> DATA_FACTORY = TypedDataObjectFactory.simple(
		new SerializableData()
			.add("condition", BiEntityCondition.DATA_TYPE),
		data -> new UndirectedBiEntityConditionType(
			data.get("condition")
		),
		(conditionType, serializableData) -> serializableData.instance()
			.set("condition", conditionType.biEntityCondition)
	);

	private final BiEntityCondition biEntityCondition;

	public UndirectedBiEntityConditionType(BiEntityCondition biEntityCondition) {
		this.biEntityCondition = biEntityCondition;
	}

	@Override
	public @NotNull ConditionConfiguration<?> getConfig() {
		return BiEntityConditionTypes.UNDIRECTED;
	}

	@Override
	public boolean test(Entity actor, Entity target) {
		return biEntityCondition.test(actor, target)
			|| biEntityCondition.test(target, actor);
	}

}