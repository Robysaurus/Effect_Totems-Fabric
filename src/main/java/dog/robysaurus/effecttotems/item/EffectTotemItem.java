package dog.robysaurus.effecttotems.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class EffectTotemItem extends Item {
    public EffectTotemItem(Settings settings) {
        super(settings);
        PotionContentsComponent potionContents = getComponents().get(DataComponentTypes.POTION_CONTENTS);
        if(potionContents != null) {
            potionContents.getEffects().forEach(effect -> effect.mapDuration(duration -> 1));
        }
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack itemStack = super.getDefaultStack();
        itemStack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Potions.WATER));
        return itemStack;
    }

    @Override
    public Text getName(ItemStack stack) {
        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
        return potionContents != null ? potionContents.getName(this.translationKey + ".effect.") : super.getName(stack);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient && entity instanceof LivingEntity livingEntity) {
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            if(potionContents != null) {
                potionContents.getEffects().forEach(livingEntity::addStatusEffect);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            potionContentsComponent.buildTooltip(tooltip::add, 1.0F, context.getUpdateTickRate());
        }
    }
}
