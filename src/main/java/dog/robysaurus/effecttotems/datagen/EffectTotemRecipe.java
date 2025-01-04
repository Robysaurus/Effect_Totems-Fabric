package dog.robysaurus.effecttotems.datagen;

import dog.robysaurus.effecttotems.item.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import static dog.robysaurus.effecttotems.EffectTotems.EFFECT_TOTEM;

public class EffectTotemRecipe extends SpecialCraftingRecipe {

    public EffectTotemRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getWidth() == 3 && input.getHeight() == 3 && input.getStackCount() == 7) {
            if(!input.getStackInSlot(0,1).isEmpty() || !input.getStackInSlot(2,1).isEmpty()) {
                return false;
            }
            if(!input.getStackInSlot(0,0).isOf(Items.NETHER_STAR) || !input.getStackInSlot(2,0).isOf(Items.NETHER_STAR) || !input.getStackInSlot(0,2).isOf(Items.NETHER_STAR) || !input.getStackInSlot(2,2).isOf(Items.NETHER_STAR)) {
                return false;
            }
            if(!input.getStackInSlot(1,0).isOf(Items.POTION) || !input.getStackInSlot(1,1).isOf(Items.TOTEM_OF_UNDYING) || !input.getStackInSlot(1,2).isOf(Items.NETHERITE_BLOCK)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        ItemStack stack = input.getStackInSlot(1, 0);
        if (!stack.isOf(Items.POTION) || stack.get(DataComponentTypes.POTION_CONTENTS) == null) {
            return ItemStack.EMPTY;
        }
        Potion potion = stack.get(DataComponentTypes.POTION_CONTENTS).potion().get().value();
        if (potion.hasInstantEffect()) {
            return ItemStack.EMPTY;
        }
        RegistryEntry<Potion> potionEntry = Registries.POTION.getEntry(potion);
        if(!potionEntry.matches(Potions.LONG_NIGHT_VISION) &&
                !potionEntry.matches(Potions.LONG_INVISIBILITY) &&
                !potionEntry.matches(Potions.LONG_LEAPING) &&
                !potionEntry.matches(Potions.LONG_FIRE_RESISTANCE) &&
                !potionEntry.matches(Potions.LONG_SWIFTNESS) &&
                !potionEntry.matches(Potions.LONG_SLOWNESS) &&
                !potionEntry.matches(Potions.LONG_TURTLE_MASTER) &&
                !potionEntry.matches(Potions.LONG_WATER_BREATHING) &&
                !potionEntry.matches(Potions.LONG_POISON) &&
                !potionEntry.matches(Potions.LONG_REGENERATION) &&
                !potionEntry.matches(Potions.LONG_STRENGTH) &&
                !potionEntry.matches(Potions.LONG_WEAKNESS) &&
                !potionEntry.matches(Potions.LONG_SLOW_FALLING)) {
            ItemStack result = new ItemStack(ModItems.TOTEM, 1);
            result.set(DataComponentTypes.POTION_CONTENTS, stack.get(DataComponentTypes.POTION_CONTENTS));
            return result;
        }else {
            return ItemStack.EMPTY;
        }
    }
    
    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return EFFECT_TOTEM;
    }
}