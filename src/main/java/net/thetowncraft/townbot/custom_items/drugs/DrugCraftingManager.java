package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import net.thetowncraft.townbot.custom_items.drugs.ingredients.methingredients.CookedPowerMix;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

public class DrugCraftingManager {

    public void onEnable(){

        //Basic Ingredient Recipes

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

        ShapedRecipe powerMixRecipe = new ShapedRecipe(new NamespacedKey(Plugin.get(), "powerMixRecipe"), CustomItems.POWER_MIX.createItemStack(1));
        powerMixRecipe.shape(" S ","BWB"," S ");
        powerMixRecipe.setIngredient('S', Material.SUGAR);
        powerMixRecipe.setIngredient('B', new RecipeChoice.ExactChoice(CustomItems.SPICE.createItemStack(1)));
        powerMixRecipe.setIngredient('W', new RecipeChoice.ExactChoice(CustomItems.SLEEPER.createItemStack(1)));

        FurnaceRecipe cookedPowerMixRecipe = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "cookedPowerMixRecipe"), CustomItems.COOKED_POWER_MIX.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.POWER_MIX.createItemStack(1)), 1f, 400);

        ShapedRecipe hypersugarRecipe = new ShapedRecipe(new NamespacedKey(Plugin.get(), "hypersugarRecipe"), CustomItems.HYPERSUGAR.createItemStack(1));
        hypersugarRecipe.shape(" S ","SBS"," S ");
        hypersugarRecipe.setIngredient('S', new RecipeChoice.ExactChoice(CustomItems.SWEET_DUST.createItemStack(1)));
        hypersugarRecipe.setIngredient('B', Material.BLAZE_POWDER);

        //Powdered Meth, Crystal Meth and Refined Meth Recipes

        SmithingRecipe powderedMethRecipe = new SmithingRecipe(new NamespacedKey(Plugin.get(), "powderedMethRecipe"), CustomItems.POWDERED_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.COOKED_POWER_MIX.createItemStack(1)), new RecipeChoice.ExactChoice(CustomItems.HYPERSUGAR.createItemStack(1)));

        FurnaceRecipe crystalMethRecipe = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "crystalMethRecipe"), CustomItems.CRYSTAL_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.POWDERED_METH.createItemStack(1)), 1.5f, 800);

        BlastingRecipe refinedMethRecipe = new BlastingRecipe(new NamespacedKey(Plugin.get(), "refinedMethRecipe"), CustomItems.REFINED_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.CRYSTAL_METH.createItemStack(1)), 1.5f, 1600);

    }
}
