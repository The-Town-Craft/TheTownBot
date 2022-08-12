package net.thetowncraft.townbot.custom_items.drugs;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

public class DrugCraftingManager {

    public static ShapedRecipe NETHER_CANDY_RECIPE;
    public static SmithingRecipe SLEEPER_RECIPE;
    public static ShapedRecipe SPICE_RECIPE;
    public static ShapedRecipe SWEET_DUST_RECIPE;
    public static ShapedRecipe POWER_MIX_RECIPE;
    public static FurnaceRecipe COOKED_POWER_MIX_RECIPE;
    public static ShapedRecipe HYPER_SUGAR_RECIPE;
    public static SmithingRecipe POWDERED_METH_RECIPE;
    public static FurnaceRecipe CRYSTAL_METH_RECIPE;
    public static BlastingRecipe REFINED_METH_RECIPE;

    public static void init() {

        //Basic Ingredient Recipes

        NETHER_CANDY_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "netherCandyRecipe"), CustomItems.NETHER_CANDY.createItemStack(1));
        NETHER_CANDY_RECIPE.shape("SNS","SNS","SNS");
        NETHER_CANDY_RECIPE.setIngredient('S', Material.SUGAR);
        NETHER_CANDY_RECIPE.setIngredient('N', Material.NETHER_WART);

        SLEEPER_RECIPE = new SmithingRecipe(new NamespacedKey(Plugin.get(), "sleeperRecipe"),
                CustomItems.SLEEPER.createItemStack(1), new RecipeChoice.MaterialChoice(Material.FERMENTED_SPIDER_EYE),
                new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        SPICE_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "spiceRecipe"),
                CustomItems.SPICE.createItemStack(1));
        SPICE_RECIPE.shape("NBN","BNB","NBN");

        SPICE_RECIPE.setIngredient('B', Material.BLAZE_POWDER);
        SPICE_RECIPE.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        SWEET_DUST_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "sweetDustRecipe"),
                CustomItems.SWEET_DUST.createItemStack(1));
        SWEET_DUST_RECIPE.shape("SSS","NNN","SSS");
        SWEET_DUST_RECIPE.setIngredient('S', Material.SUGAR);
        SWEET_DUST_RECIPE.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.SWEET_DUST.createItemStack(1)));

        //Meth Ingredients Recipes

        POWER_MIX_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "powerMixRecipe"),
                CustomItems.POWER_MIX.createItemStack(1));
        POWER_MIX_RECIPE.shape(" S ","BWB"," S ");
        POWER_MIX_RECIPE.setIngredient('S', Material.SUGAR);
        POWER_MIX_RECIPE.setIngredient('B', new RecipeChoice.ExactChoice(CustomItems.SPICE.createItemStack(1)));
        POWER_MIX_RECIPE.setIngredient('W', new RecipeChoice.ExactChoice(CustomItems.SLEEPER.createItemStack(1)));

        COOKED_POWER_MIX_RECIPE = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "cookedPowerMixRecipe"),
                CustomItems.COOKED_POWER_MIX.createItemStack(1),
                new RecipeChoice.ExactChoice(CustomItems.POWER_MIX.createItemStack(1)), 1f, 400);

        HYPER_SUGAR_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "hypersugarRecipe"),
                CustomItems.HYPERSUGAR.createItemStack(1));
        HYPER_SUGAR_RECIPE.shape(" S ","SBS"," S ");
        HYPER_SUGAR_RECIPE.setIngredient('S', new RecipeChoice.ExactChoice(CustomItems.SWEET_DUST.createItemStack(1)));
        HYPER_SUGAR_RECIPE.setIngredient('B', Material.BLAZE_POWDER);

        //Powdered Meth, Crystal Meth and Refined Meth Recipes

        POWDERED_METH_RECIPE = new SmithingRecipe(new NamespacedKey(Plugin.get(), "powderedMethRecipe"),
                CustomItems.POWDERED_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.COOKED_POWER_MIX.createItemStack(1)),
                new RecipeChoice.ExactChoice(CustomItems.HYPERSUGAR.createItemStack(1)));

        CRYSTAL_METH_RECIPE = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "crystalMethRecipe"),
                CustomItems.CRYSTAL_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.POWDERED_METH.createItemStack(1)), 1.5f, 800);

        REFINED_METH_RECIPE = new BlastingRecipe(new NamespacedKey(Plugin.get(), "refinedMethRecipe"),
                CustomItems.REFINED_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.CRYSTAL_METH.createItemStack(1)), 1.5f, 1600);


        Bukkit.addRecipe(HYPER_SUGAR_RECIPE);
        Bukkit.addRecipe(SLEEPER_RECIPE);
        Bukkit.addRecipe(SPICE_RECIPE);
        Bukkit.addRecipe(REFINED_METH_RECIPE);
        Bukkit.addRecipe(NETHER_CANDY_RECIPE);
        Bukkit.addRecipe(CRYSTAL_METH_RECIPE);
        Bukkit.addRecipe(POWDERED_METH_RECIPE);
        Bukkit.addRecipe(COOKED_POWER_MIX_RECIPE);
        Bukkit.addRecipe(POWER_MIX_RECIPE);
        Bukkit.addRecipe(SWEET_DUST_RECIPE);
    }
}
