package me.dueris.genesismc.core.utils;

import me.dueris.genesismc.core.GenesisMC;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class ParticleHandler extends BukkitRunnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PersistentDataContainer data = p.getPersistentDataContainer();
            @Nullable String origintag = data.get(new NamespacedKey(GenesisMC.getPlugin(), "origintag"), PersistentDataType.STRING);
            if (p.getGameMode() != GameMode.SPECTATOR && !p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                //origintag.equalsIgnoreCase("genesis:origin-")
                if (origintag == "genesis:origin-enderian") {
                    p.getWorld().spawnParticle(Particle.PORTAL, p.getLocation(), 4, 0.6F, -0.5, 0.6F);
                }
                //origintag.equalsIgnoreCase("genesis:origin-")
                if (origintag == "genesis:origin-starborne") {
                    p.getWorld().spawnParticle(Particle.GLOW, p.getLocation(), 1, 0.15F, -0.5, 0.15F);
                    Particle.DustTransition dustColorTransition = new Particle.DustTransition(Color.fromRGB(151, 0, 227), Color.fromRGB(53, 0, 212), 1F);
                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation(), 2, 0.15F, -0.5, 0.15F, dustColorTransition);
                    p.getWorld().spawnParticle(Particle.GLOW, p.getEyeLocation(), 1, 0.15F, -0.5, 0.15F);
                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getEyeLocation(), 2, 0.15F, -0.5, 0.15F, dustColorTransition);

                }
                //origintag.equalsIgnoreCase("genesis:origin-")
                if (origintag == "genesis:origin-allay") {
                    Particle.DustTransition dustColorTransition = new Particle.DustTransition(Color.fromRGB(97, 255, 250), Color.fromRGB(163, 255, 244), 0.275F);
                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getLocation(), 3, 0.4F, -0.5, 0.4F, dustColorTransition);
                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, p.getEyeLocation(), 3, 0.4F, -0.5, 0.4F, dustColorTransition);



                }

            }
        }
    }
}
