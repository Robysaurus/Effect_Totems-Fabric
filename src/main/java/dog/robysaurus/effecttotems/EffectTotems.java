package dog.robysaurus.effecttotems;

import dog.robysaurus.effecttotems.datagen.EffectTotemRecipe;
import dog.robysaurus.effecttotems.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EffectTotems implements ModInitializer {
	public static final String MOD_ID = "effecttotems";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final RecipeSerializer<EffectTotemRecipe> EFFECT_TOTEM = RecipeSerializer.register("crafting_special_effecttotem", new SpecialCraftingRecipe.SpecialRecipeSerializer<>(EffectTotemRecipe::new));
	@Override
	public void onInitialize() {
		ModItems.registerModItems();
	}
}