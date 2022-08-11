package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

public class DrugCraftingManager {

    public void onEnable(){

        //Basic Ingredient Recipes, I dunno if the way I'm implementing the CustomItems.ITEM.createItemStack(1) is the correct way so tell me if it's not
        ShapedRecipe netherCandyRecipe = new ShapedRecipe(new NamespacedKey(Plugin.get(), "netherCandyRecipe"), CustomItems.NETHER_CANDY.createItemStack(1));
        netherCandyRecipe.shape("SNS","SNS","SNS");
        netherCandyRecipe.setIngredient('S', Material.SUGAR);
        netherCandyRecipe.setIngredient('N', Material.NETHER_WART);

        SmithingRecipe sleeperRecipe = new SmithingRecipe(new NamespacedKey(Plugin.get(), "sleeperRecipe"), CustomItems.SLEEPER.createItemStack(1), new RecipeChoice.MaterialChoice(Material.FERMENTED_SPIDER_EYE), new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        ShapedRecipe spiceRecipe = new ShapedRecipe(new NamespacedKey(Plugin.get(), "spiceRecipe"), CustomItems.SPICE.createItemStack(1));
        spiceRecipe.shape("NBN","BNB","NBN");
        spiceRecipe.setIngredient('B', Material.BLAZE_POWDER);
        spiceRecipe.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        ShapedRecipe sweetDustRecipe = new ShapedRecipe(new NamespacedKey(Plugin.get(), "sweetDustRecipe"), CustomItems.SWEET_DUST.createItemStack(1));
        sweetDustRecipe.shape("SSS","NNN","SSS");
        sweetDustRecipe.setIngredient('S', Material.SUGAR);
        sweetDustRecipe.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.SWEET_DUST.createItemStack(1)));

        //Meth Ingredients Recipes

    }
}
