package net.thetowncraft.townbot.custom_items;

import net.thetowncraft.townbot.Plugin;
import net.thetowncraft.townbot.custom_items.CustomItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

public class CustomCraftingManager {

    public static ShapedRecipe NETHER_CANDY_RECIPE;
    public static ShapedRecipe SLEEPER_RECIPE;
    public static ShapedRecipe SPICE_RECIPE;
    public static ShapedRecipe SWEET_DUST_RECIPE;
    public static ShapedRecipe POWER_MIX_RECIPE;
    public static FurnaceRecipe COOKED_POWER_MIX_RECIPE;
    public static ShapedRecipe HYPER_SUGAR_RECIPE;
    public static ShapedRecipe POWDERED_METH_RECIPE;
    public static FurnaceRecipe CRYSTAL_METH_RECIPE;
    public static BlastingRecipe REFINED_METH_RECIPE;

    public static void init() {

        //Basic Drug Ingredient Recipes

        NETHER_CANDY_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "nether_candy_recipe"), CustomItems.NETHER_CANDY.createItemStack(1));
        NETHER_CANDY_RECIPE.shape("SNS","SNS","SNS");
        NETHER_CANDY_RECIPE.setIngredient('S', Material.SUGAR);
        NETHER_CANDY_RECIPE.setIngredient('N', Material.NETHER_WART);

        SLEEPER_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "sleeper_recipe"),
                CustomItems.SLEEPER.createItemStack(1));
        SLEEPER_RECIPE.shape("   "," FN","   ");
        SLEEPER_RECIPE.setIngredient('F', Material.FERMENTED_SPIDER_EYE);
        SLEEPER_RECIPE.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        SPICE_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "spice_recipe"),
                CustomItems.SPICE.createItemStack(1));
        SPICE_RECIPE.shape("NBN","BNB","NBN");

        SPICE_RECIPE.setIngredient('B', Material.BLAZE_POWDER);
        SPICE_RECIPE.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        SWEET_DUST_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "sweet_dust_recipe"),
                CustomItems.SWEET_DUST.createItemStack(1));
        SWEET_DUST_RECIPE.shape("SSS","NNN","SSS");
        SWEET_DUST_RECIPE.setIngredient('S', Material.SUGAR);
        SWEET_DUST_RECIPE.setIngredient('N', new RecipeChoice.ExactChoice(CustomItems.NETHER_CANDY.createItemStack(1)));

        //Meth Ingredients Recipes

        POWER_MIX_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "power_mix_recipe"),
                CustomItems.POWER_MIX.createItemStack(1));
        POWER_MIX_RECIPE.shape(" S ","BWB"," S ");
        POWER_MIX_RECIPE.setIngredient('S', Material.SUGAR);
        POWER_MIX_RECIPE.setIngredient('B', new RecipeChoice.ExactChoice(CustomItems.SPICE.createItemStack(1)));
        POWER_MIX_RECIPE.setIngredient('W', new RecipeChoice.ExactChoice(CustomItems.SLEEPER.createItemStack(1)));

        COOKED_POWER_MIX_RECIPE = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "cooked_power_mix_recipe"),
                CustomItems.COOKED_POWER_MIX.createItemStack(1),
                new RecipeChoice.ExactChoice(CustomItems.POWER_MIX.createItemStack(1)), 1f, 400);

        HYPER_SUGAR_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "hypersugar_recipe"),
                CustomItems.HYPERSUGAR.createItemStack(1));
        HYPER_SUGAR_RECIPE.shape(" S ","SBS"," S ");
        HYPER_SUGAR_RECIPE.setIngredient('S', new RecipeChoice.ExactChoice(CustomItems.SWEET_DUST.createItemStack(1)));
        HYPER_SUGAR_RECIPE.setIngredient('B', Material.BLAZE_POWDER);

        //Powdered Meth, Crystal Meth and Refined Meth Recipes

        POWDERED_METH_RECIPE = new ShapedRecipe(new NamespacedKey(Plugin.get(), "powdered_meth_recipe"),
                CustomItems.POWDERED_METH.createItemStack(1));
        POWDERED_METH_RECIPE.shape("   "," CH","  ");
        POWDERED_METH_RECIPE.setIngredient('C', new RecipeChoice.ExactChoice(CustomItems.COOKED_POWER_MIX.createItemStack(1)));
        POWDERED_METH_RECIPE.setIngredient('H', new RecipeChoice.ExactChoice(CustomItems.HYPERSUGAR.createItemStack(1)));

        CRYSTAL_METH_RECIPE = new FurnaceRecipe(new NamespacedKey(Plugin.get(), "crystal_meth_recipe"),
                CustomItems.CRYSTAL_METH.createItemStack(1), new RecipeChoice.ExactChoice(CustomItems.POWDERED_METH.createItemStack(1)), 1.5f, 800);

        REFINED_METH_RECIPE = new BlastingRecipe(new NamespacedKey(Plugin.get(), "refined_meth_recipe"),
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
