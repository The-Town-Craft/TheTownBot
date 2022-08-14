package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import static net.thetowncraft.townbot.util.data.Data.getFile;

public class CrystalMeth extends CustomItem {

    private static final HashMap<UUID, Integer> METH_STAGES = new HashMap<>();
    private static final HashMap<UUID, Long> WITHDRAWAL_TIMER = new HashMap<>();
    private static final HashMap<UUID, Boolean> AWAITING_SYMPTOMS = new HashMap<>();

    @Override
    public void onInteract(PlayerInteractEvent event, int amount){
        Player player = event.getPlayer();
        double randomTime = Math.random() * (180000.0 - 60000) + 60000;

        if(!METH_STAGES.containsKey(player.getUniqueId())){
            doFirstTimeEffects(player);
        }

        if(METH_STAGES.containsKey(player.getUniqueId())){
            doNonMethVirginEffects(player);
        }

        if(METH_STAGES.get(player.getUniqueId()) > 1){
            if(!WITHDRAWAL_TIMER.containsKey(player.getUniqueId())){
                WITHDRAWAL_TIMER.put(player.getUniqueId(), System.currentTimeMillis());
                AWAITING_SYMPTOMS.put(player.getUniqueId(), true);

                while(!AWAITING_SYMPTOMS.get(player.getUniqueId())){
                    if(System.currentTimeMillis() - WITHDRAWAL_TIMER.get(player.getUniqueId()) > randomTime){
                        AWAITING_SYMPTOMS.replace(player.getUniqueId(), false);

                    }
                }
            }
            else{
                doNonMethVirginEffects(player);
                player.damage(5);
            }
        }
    }

    public void doFirstTimeEffects(Player player){
        METH_STAGES.put(player.getUniqueId(), 1);
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(600, 1));
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(600, 20));
        player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(800, 2));
        player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1200, 3));
    }

    public void doNonMethVirginEffects(Player player){
        int dampener = METH_STAGES.get(player.getUniqueId());
        METH_STAGES.replace(player.getUniqueId(), METH_STAGES.get(player.getUniqueId()) + 1);
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(600, 1));
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(600, 20 - METH_STAGES.get(player.getUniqueId()) * 2));
        player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(800, 2));
        player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1200, 5 - METH_STAGES.get(player.getUniqueId()) / 2));
    }

    public void doWithdrawlSymptoms(Player player){
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(1200, 2));
        if(METH_STAGES.get(player.getUniqueId()) > 2){
            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(750, 1));
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(3600, 1));
        }
        if(METH_STAGES.get(player.getUniqueId()) > 3){
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(200 * METH_STAGES.get(player.getUniqueId()), METH_STAGES.get(player.getUniqueId())));
        }
        if(METH_STAGES.get(player.getUniqueId()) > 5){
            player.addPotionEffect(PotionEffectType.WITHER.createEffect(200, 1));
        }
        player.sendMessage(ChatColor.RED + "You are suffering withdrawal symptoms...");
    }

    public static void save() {
        JSONObject methStages = new JSONObject();
        METH_STAGES.forEach(methStages::put);
        Data.writeToFile(getFile(), methStages.toString());
    }

    private static File getFile() {
        return Data.getFile("drugs/crystalmeth/methstages.json");
        return Data.getFile("drugs/crystalmeth/withdrawaltimer.json");
        return Data.getFile("drugs/crystalmeth/withdrawal.json");
    }

    @Override
    public String getName(){
        return ChatColor.AQUA + "Crystal Meth";
    }

    @Override
    public String getDescription(){
        return ChatColor.AQUA + "\"Yeah! Science!\"";
    }

    @Override
    public int getCustomModelData(){
        return 1;
    }

    @Override
    public Material getBaseItem(){
        return Material.AMETHYST_SHARD;
    }

    @Override
    public Rarity getRarity(){
        return Rarity.EPIC;
    }

    @Override
    public boolean shines(){
        return true;
    }

}
