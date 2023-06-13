package me.dueris.genesismc.core.factory.powers.effects;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;

import static me.dueris.genesismc.core.factory.powers.Powers.strong_arms_break_speed;
import static org.bukkit.Material.*;

public class BetterMineSpeedRunnable extends BukkitRunnable {
    public static EnumSet<Material> tools;

    static {
        tools = EnumSet.of(
                WOODEN_PICKAXE, STONE_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, DIAMOND_PICKAXE, NETHERITE_PICKAXE,
                WOODEN_AXE, STONE_AXE, GOLDEN_AXE, IRON_AXE, DIAMOND_AXE, NETHERITE_AXE,
                WOODEN_SHOVEL, STONE_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, DIAMOND_SHOVEL, NETHERITE_SHOVEL,
                SHEARS);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (strong_arms_break_speed.contains(p)) {
                if (p.getGameMode().equals(GameMode.SURVIVAL) && !tools.contains(p.getEquipment().getItemInMainHand().getType())) {
                    if (!p.getActivePotionEffects().contains(PotionEffectType.FAST_DIGGING)) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20, 3, true, false, false));
                    }
                }
                p.setCooldown(SHIELD, 100);
            }
        }
    }
}