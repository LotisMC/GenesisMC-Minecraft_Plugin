package io.github.dueris.originspaper.condition.type.entity;

import io.github.dueris.calio.SerializableDataTypes;
import io.github.dueris.calio.data.SerializableData;
import io.github.dueris.originspaper.OriginsPaper;
import io.github.dueris.originspaper.condition.factory.ConditionTypeFactory;
import io.github.dueris.originspaper.data.ApoliDataTypes;
import io.github.dueris.originspaper.data.types.Comparison;
import io.github.dueris.originspaper.data.types.InventoryType;
import io.github.dueris.originspaper.util.Util;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public class InventoryConditionType {

	public static boolean condition(SerializableData.@NotNull Instance data, Entity entity) {

		Set<InventoryType> inventoryTypes = data.get("inventory_types");
		Util.ProcessMode processMode = data.get("process_mode");
		Comparison comparison = data.get("comparison");

		int compareTo = data.get("compare_to");
		int matches = 0;

		if (inventoryTypes.contains(InventoryType.INVENTORY)) {
			matches += Util.checkInventory(data, entity, processMode.getProcessor());
		}

		return comparison.compare(matches, compareTo);

	}

	public static @NotNull ConditionTypeFactory<Entity> getFactory() {
		return new ConditionTypeFactory<>(
			OriginsPaper.apoliIdentifier("inventory"),
			new SerializableData()
				.add("inventory_types", SerializableDataTypes.enumSet(InventoryType.class, ApoliDataTypes.INVENTORY_TYPE), EnumSet.of(InventoryType.INVENTORY))
				.add("process_mode", ApoliDataTypes.PROCESS_MODE, Util.ProcessMode.ITEMS)
				.add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
				.add("slots", SerializableDataTypes.list(ApoliDataTypes.ITEM_SLOT), null)
				.add("slot", ApoliDataTypes.ITEM_SLOT, null)
				.add("power", ApoliDataTypes.POWER_REFERENCE, null)
				.add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
				.add("compare_to", SerializableDataTypes.INT, 0),
			InventoryConditionType::condition
		);
	}

}
