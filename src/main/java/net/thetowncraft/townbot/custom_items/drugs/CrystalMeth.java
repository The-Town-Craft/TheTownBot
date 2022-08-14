package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.custom_items.CustomItem;
import net.thetowncraft.townbot.util.Rarity;
import net.thetowncraft.townbot.util.data.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;

public class CrystalMeth extends CustomItem {
    private static final HashMap<String, Integer> METH_STAGES = new HashMap<>();
    private static final HashMap<String, Long> WITHDRAWAL_TIMER = new HashMap<>();
    private static final HashMap<String, Boolean> AWAITING_SYMPTOMS = new HashMap<>();

    @Override
    public void onInteract(PlayerInteractEvent event, int amount){
        Player player = event.getPlayer();
        double randomTime = Math.random() * (180000.0 - 60000) + 60000;

        if(!METH_STAGES.containsKey(player.getUniqueId().toString())){
            doFirstTimeEffects(player);
        }

        if(METH_STAGES.containsKey(player.getUniqueId().toString())){
            doNonMethVirginEffects(player);
        }

        if(METH_STAGES.get(player.getUniqueId().toString()) > 1){
            if(!WITHDRAWAL_TIMER.containsKey(player.getUniqueId().toString())){
                WITHDRAWAL_TIMER.put(player.getUniqueId().toString(), System.currentTimeMillis());
                AWAITING_SYMPTOMS.put(player.getUniqueId().toString(), true);

                while(!AWAITING_SYMPTOMS.get(player.getUniqueId().toString())){
                    if(System.currentTimeMillis() - WITHDRAWAL_TIMER.get(player.getUniqueId().toString()) > randomTime){
                        AWAITING_SYMPTOMS.replace(player.getUniqueId().toString(), false);
                        doWithdrawalSymptoms(player);
                        AWAITING_SYMPTOMS.remove(player.getUniqueId().toString());
                        WITHDRAWAL_TIMER.remove(player.getUniqueId().toString());
                        break;
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
        METH_STAGES.put(player.getUniqueId().toString(), 1);
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(600, 1));
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(600, 20));
        player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(800, 2));
        player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1200, 3));
    }

    public void doNonMethVirginEffects(Player player){
        METH_STAGES.replace(player.getUniqueId().toString(), METH_STAGES.get(player.getUniqueId().toString()) + 1);
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(600, 1));
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(600, 20 - METH_STAGES.get(player.getUniqueId().toString()) * 2));
        player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(800, 2));
        player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1200, 5 - METH_STAGES.get(player.getUniqueId().toString()) / 2));
    }

    public void doWithdrawalSymptoms(Player player){
        player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(1200, 2));
        if(METH_STAGES.get(player.getUniqueId().toString()) > 2){
            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(750, 1));
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(3600, 1));
        }
        if(METH_STAGES.get(player.getUniqueId().toString()) > 3){
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(200 * METH_STAGES.get(player.getUniqueId().toString()), METH_STAGES.get(player.getUniqueId().toString())));
        }
        if(METH_STAGES.get(player.getUniqueId().toString()) > 5){
            player.addPotionEffect(PotionEffectType.WITHER.createEffect(200, 1));
        }
        player.sendMessage(ChatColor.RED + "You are suffering withdrawal symptoms...");
    }

    public static void save() {
        JSONObject methStages = new JSONObject();
        METH_STAGES.forEach(methStages::put);
        Data.writeToFile(getFile(), methStages.toString());
    }

    public static void load() {
        JSONObject methStages = Data.getJSONObjectFromFile(getFile());
        for(String key : methStages.keySet()) {
            try {
                METH_STAGES.put(key, (Integer) methStages.get(key));
            }
            catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static File getFile() {
        return Data.getFile("drugs/crystalmeth/methstages.json");
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
