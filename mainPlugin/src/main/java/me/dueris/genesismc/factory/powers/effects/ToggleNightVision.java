package me.dueris.genesismc.factory.powers.effects;

import me.dueris.genesismc.CooldownManager;
import me.dueris.genesismc.GenesisMC;
import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.events.KeybindTriggerEvent;
import me.dueris.genesismc.factory.conditions.ConditionExecutor;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.utils.KeybindUtils;
import me.dueris.genesismc.utils.OriginContainer;
import me.dueris.genesismc.utils.PowerContainer;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import static me.dueris.genesismc.utils.KeybindUtils.isKeyBeingPressed;

import java.util.ArrayList;

public class ToggleNightVision extends CraftPower implements Listener {

    @Override
    public void run(Player p) {

    }

    public void execute(Player p, PowerContainer power) {
        if (!getPowerArray().contains(p)) return;
        if (runCancel) return;
        String tag = power.getTag();
        String key = (String) power.getKey().getOrDefault("key", "key.origins.primary_active");
        KeybindUtils.runKeyChangeTrigger(KeybindUtils.getTriggerFromOriginKey(p, key));
        if (CooldownManager.isPlayerInCooldown(p, key)) return;
        if (powers_active.containsKey(power.getTag())) {
            setActive(power.getTag(), !powers_active.get(tag));
            if (true) {
                if (active) {
                    //active
                    KeybindUtils.runKeyChangeTriggerReturn(KeybindUtils.getTriggerFromOriginKey(p, key), p, key);
                    ItemMeta met = KeybindUtils.getKeybindItem(key, p.getInventory()).getItemMeta();
                    met.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "contin"), PersistentDataType.BOOLEAN, false);
                    KeybindUtils.getKeybindItem(key, p.getInventory()).setItemMeta(met);
                    setActive(tag, false);
                    in_continuous.remove(p);
                    active = false;
                    runCancel = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            runCancel = false;
                            this.cancel();
                        }
                    }.runTaskTimer(GenesisMC.getPlugin(), 0, 5);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            //run code for while its disabled
                            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        }
                    }.runTaskTimer(GenesisMC.getPlugin(), 0, 1);
                } else {
                    //nonactive
                    KeybindUtils.runKeyChangeTrigger(KeybindUtils.getKeybindItem(key, p.getInventory()));
                    ItemMeta met = KeybindUtils.getKeybindItem(key, p.getInventory()).getItemMeta();
                    met.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "contin"), PersistentDataType.BOOLEAN, true);
                    KeybindUtils.getKeybindItem(key, p.getInventory()).setItemMeta(met);
                    setActive(tag, true);
                    in_continuous.add(p);
                    active = true;
                    runCancel = true;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            runCancel = false;
                            this.cancel();
                        }
                    }.runTaskLater(GenesisMC.getPlugin(), 5);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            //run code for while its enabled
                            ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                            if (conditionExecutor.check("condition", "conditions", p, power, getPowerFile(), p, null, null, null, p.getItemInHand(), null)) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 400, false, false, false));
                            } else {
                                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                            }
                        }
                    }.runTaskTimer(GenesisMC.getPlugin(), 0, 1);
                }
//                ToggleTriggerEvent toggleTriggerEvent = new ToggleTriggerEvent(p, key, origin, !active);
//                Bukkit.getServer().getPluginManager().callEvent(toggleTriggerEvent);
            } else {
                KeybindUtils.runKeyChangeTrigger(KeybindUtils.getKeybindItem(key, p.getInventory()));
                setActive(tag, true);
                in_continuous.add(p);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        KeybindUtils.runKeyChangeTriggerReturn(KeybindUtils.getTriggerFromOriginKey(p, key), p, key);
                        setActive(tag, false);
                        ItemMeta met = KeybindUtils.getKeybindItem(key, p.getInventory()).getItemMeta();
                        met.getPersistentDataContainer().set(new NamespacedKey(GenesisMC.getPlugin(), "contin"), PersistentDataType.BOOLEAN, false);
                        KeybindUtils.getKeybindItem(key, p.getInventory()).setItemMeta(met);
                        in_continuous.remove(p);
                    }
                }.runTaskLater(GenesisMC.getPlugin(), 2);
            }
        } else {
            //set true
            KeybindUtils.runKeyChangeTrigger(KeybindUtils.getKeybindItem(key, p.getInventory()));
            setActive(tag, true);
            in_continuous.add(p);
            new BukkitRunnable() {
                @Override
                public void run() {
                    KeybindUtils.runKeyChangeTriggerReturn(KeybindUtils.getTriggerFromOriginKey(p, key), p, key);
                    setActive(tag, false);
                    in_continuous.remove(p);
                }
            }.runTaskLater(GenesisMC.getPlugin(), 2);
        }
    }

    @EventHandler
    public void keybindToggle(KeybindTriggerEvent e) {
        Player p = e.getPlayer();
        if (getPowerArray().contains(e.getPlayer())) {
            for (OriginContainer origin : OriginPlayer.getOrigin(e.getPlayer()).values()) {
                ConditionExecutor conditionExecutor = me.dueris.genesismc.GenesisMC.getConditionExecutor();
                for (PowerContainer power : origin.getMultiPowerFileFromType(getPowerFile())) {
                    if (conditionExecutor.check("condition", "conditions", p, power, getPowerFile(), p, null, null, null, p.getItemInHand(), null)) {
                        if (!CooldownManager.isPlayerInCooldown(p, power.getKey().getOrDefault("key", "key.origins.primary_active").toString())) {
                            if (isKeyBeingPressed(e.getPlayer(), power.getKey().getOrDefault("key", "key.origins.primary_active").toString(), true)) {
                                execute(p, power);
                            }
                        }
                    } else {
                        KeybindUtils.runKeyChangeTriggerReturn(KeybindUtils.getKeybindItem(power.getKey().getOrDefault("key", "key.origins.primary_active").toString(), p.getInventory()), p, power.getKey().getOrDefault("key", "key.origins.primary_active").toString());
                        setActive(power.getTag(), false);
                    }
                }
            }
        }
    }

    @Override
    public void setActive(String tag, Boolean bool) {
        if (powers_active.containsKey(tag)) {
            powers_active.replace(tag, bool);
        } else {
            powers_active.put(tag, bool);
        }
    }

    public static ArrayList<Player> in_continuous = new ArrayList<>();
    public boolean active = true;
    public boolean runCancel = false;

    @Override
    public String getPowerFile() {
        return "origins:toggle_night_vision";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return toggle_night_vision;
    }
}
