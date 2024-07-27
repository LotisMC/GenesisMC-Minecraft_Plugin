package me.dueris.originspaper.factory.powers.apoli;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.dueris.calio.data.FactoryData;
import me.dueris.calio.data.factory.FactoryElement;
import me.dueris.calio.data.factory.FactoryJsonArray;
import me.dueris.calio.data.factory.FactoryJsonObject;
import me.dueris.calio.data.types.OptionalInstance;
import me.dueris.originspaper.OriginsPaper;
import me.dueris.originspaper.factory.actions.Actions;
import me.dueris.originspaper.factory.powers.holder.PowerType;
import net.minecraft.world.InteractionHand;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActionOnEntityUse extends PowerType {
	private final FactoryJsonObject bientityAction;
	private final FactoryJsonObject heldItemAction;
	private final FactoryJsonObject resultItemAction;
	private final FactoryJsonObject bientityCondition;
	private final FactoryJsonObject itemCondition;
	private final FactoryJsonArray hands;
	private final ItemStack resultStack;

	public ActionOnEntityUse(String name, String description, boolean hidden, FactoryJsonObject condition, int loading_priority, FactoryJsonObject bientityAction, FactoryJsonObject heldItemAction, FactoryJsonObject resultItemAction, FactoryJsonObject bientityCondition, FactoryJsonObject itemCondition, FactoryJsonArray hands, ItemStack resultStack) {
		super(name, description, hidden, condition, loading_priority);
		this.bientityAction = bientityAction;
		this.heldItemAction = heldItemAction;
		this.resultItemAction = resultItemAction;
		this.bientityCondition = bientityCondition;
		this.itemCondition = itemCondition;
		this.hands = hands;
		this.resultStack = resultStack;
	}

	public static FactoryData registerComponents(FactoryData data) {
		return PowerType.registerComponents(data).ofNamespace(OriginsPaper.apoliIdentifier("action_on_entity_use"))
			.add("bientity_action", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("held_item_action", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("result_item_action", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("bientity_condition", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("item_condition", FactoryJsonObject.class, new FactoryJsonObject(new JsonObject()))
			.add("hands", FactoryJsonArray.class, new FactoryJsonArray(new Gson().fromJson("[\"off_hand\", \"main_hand\"]", JsonArray.class)))
			.add("result_stack", ItemStack.class, new OptionalInstance());
	}

	@EventHandler
	public void entityRightClickEntity(@NotNull PlayerInteractEntityEvent e) {
		Player actor = e.getPlayer();
		Entity target = e.getRightClicked();

		if (!getPlayers().contains(actor)) return;

		if (!isActive(actor)) return;
		if (!ConditionExecutor.testItem(itemCondition, actor.getInventory().getItem(e.getHand()))) return;
		if (!ConditionExecutor.testBiEntity(bientityCondition, (CraftEntity) actor, (CraftEntity) target)) return;
		boolean pass = false;
		if (e.getHand().isHand()) {
			InteractionHand hand = CraftEquipmentSlot.getHand(e.getHand());
			pass = hands.asList().stream().map(FactoryElement::getString).map(String::toUpperCase).map(InteractionHand::valueOf).toList().contains(hand);
		}
		if (!pass) return;
		Actions.executeBiEntity(actor, target, bientityAction);
		Actions.executeItem(actor.getActiveItem(), e.getPlayer().getWorld(), heldItemAction);
		if (resultStack != null) {
			actor.getInventory().addItem(resultStack);
			Actions.executeItem(actor.getActiveItem(), e.getPlayer().getWorld(), resultItemAction);
		}
	}

	public FactoryJsonObject getBientityAction() {
		return bientityAction;
	}

	public ItemStack getResultStack() {
		return resultStack;
	}

	public FactoryJsonObject getHeldItemAction() {
		return heldItemAction;
	}

	public FactoryJsonObject getBientityCondition() {
		return bientityCondition;
	}

	public FactoryJsonObject getResultItemAction() {
		return resultItemAction;
	}

	public FactoryJsonObject getItemCondition() {
		return itemCondition;
	}

	public List<InteractionHand> getHands() {
		return hands.asList().stream().map(FactoryElement::getString).map(String::toUpperCase).map(InteractionHand::valueOf).toList();
	}

}