package me.dueris.genesismc.factory.powers.OriginsMod.player.attributes;

import me.dueris.genesismc.entity.OriginPlayer;
import me.dueris.genesismc.events.OriginChangeEvent;
import me.dueris.genesismc.factory.powers.CraftPower;
import me.dueris.genesismc.factory.powers.OriginsMod.value_modifying.ValueModifyingSuperClass;
import me.dueris.genesismc.utils.OriginContainer;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class AttributeModifyTransfer extends CraftPower implements Listener {
    @Override
    public void run() {

    }

    @EventHandler
    public void runChange(OriginChangeEvent e){
        if(getPowerArray().contains(e.getPlayer())){
            for(OriginContainer origin : OriginPlayer.getOrigin(e.getPlayer()).values()){
                ValueModifyingSuperClass valueModifyingSuperClass = new ValueModifyingSuperClass();
                applyAttribute(e.getPlayer(), valueModifyingSuperClass.getDefaultValue(origin.getPowerFileFromType(getPowerFile()).get("class")), Float.parseFloat(origin.getPowerFileFromType(getPowerFile()).get("multiplier", "1.0").toString()), origin.getPowerFileFromType(getPowerFile()).get("attribute").toString().toUpperCase().split(":")[1].replace("\\.", "_"));
            }
        }
    }

    public void applyAttribute(Player p, float value, float multiplier, String attribute){
        p.getAttribute(Attribute.valueOf(attribute)).setBaseValue(value * multiplier);
    }

    @Override
    public String getPowerFile() {
        return "origins:attribute_modify_transfer";
    }

    @Override
    public ArrayList<Player> getPowerArray() {
        return attribute_modify_transfer;
    }
}
