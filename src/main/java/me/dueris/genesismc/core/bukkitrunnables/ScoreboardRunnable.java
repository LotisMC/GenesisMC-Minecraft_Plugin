package me.dueris.genesismc.core.bukkitrunnables;

import me.dueris.genesismc.core.GenesisMC;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class ScoreboardRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            PersistentDataContainer data = p.getPersistentDataContainer();
            int originid = data.get(new NamespacedKey(GenesisMC.getPlugin(), "originid"), PersistentDataType.INTEGER);
            int phantomid = data.get(new NamespacedKey(GenesisMC.getPlugin(), "in-phantomform"), PersistentDataType.INTEGER);
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard scoreboard = manager.getNewScoreboard();
            Team team = scoreboard.registerNewTeam("origin-players");
            team.addEntities(p);
            team.setCanSeeFriendlyInvisibles(true);
            team.setDisplayName("Origin Player");
            Objective objective = scoreboard.registerNewObjective("originid", "id");
            Score score = objective.getScore(p);
            score.setScore(originid);
            if(p.hasPotionEffect(PotionEffectType.INVISIBILITY) && phantomid == 2){
                p.setScoreboard(manager.getNewScoreboard());
            }else{
                p.setScoreboard(team.getScoreboard());
            }

        }
    }
}
