package me.dueris.genesismc.factory.powers.OriginsMod.value_modifying;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.ErrorSystem;
import me.dueris.genesismc.utils.OriginContainer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;

import static me.dueris.genesismc.factory.powers.OriginsMod.value_modifying.ValueModifyingSuperClass.modify_crafting;

public class ModifyCraftingPower extends CraftPower implements Listener {
    @EventHandler
    public void run(PrepareItemCraftEvent e){
        Player p = (Player) e.getInventory().getHolder();
        if(modify_crafting.contains(p)) {
            if(e.getRecipe() == null) return;
            if(e.getInventory().getResult() == null) return;
            for (OriginContainer origin : OriginPlayer.getOrigin(p).values()) {
                ValueModifyingSuperClass valueModifyingSuperClass = new ValueModifyingSuperClass();
                try {
                    ConditionExecutor conditionExecutor = new ConditionExecutor();
                    if (conditionExecutor.check("condition", "condition", p, origin, "origins:modify_crafting", null, p)) {
                        if (conditionExecutor.check("item_condition", "item_condition", p, origin, "origins:modify_crafting", null, p)) {
                                if (e.getInventory().getResult().getType() == Material.valueOf(origin.getPowerFileFromType("origins:modify_crafting").get("recipe", null).toString().split(":")[1].toUpperCase())) {
                                    e.getInventory().setResult(new ItemStack(Material.valueOf(origin.getPowerFileFromType("origins:modify_crafting").getJsonHashMap("result").get("item").toString().toUpperCase().split(":")[1])));
                                }
                        }
                    }
                } catch (Exception ev) {
                    ErrorSystem errorSystem = new ErrorSystem();
                    errorSystem.throwError("unable to get recipe or result", "origins:modify_crafting", p, origin, OriginPlayer.getLayer(p, origin));
                    ev.printStackTrace();
                }
            }
        }
    }

    public Recipe getRecipeForMaterial(Material material) {
        // Iterate through all recipes to find the one with the specified material
        for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(material))) {
            if (recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) {
                return recipe;
            }
        }
        return null; // No matching recipe found
    }

    @Override
    public void run() {

    }

    @Override
    public String getPowerFile() {
        return "origins:modify_crafting_power";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return modify_crafting;
    }
}