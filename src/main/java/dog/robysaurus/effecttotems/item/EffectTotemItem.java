package dog.robysaurus.effecttotems.item;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EffectTotemItem extends Item {
    public EffectTotemItem(Settings settings) {
        super(settings);
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
                ArrayList<StatusEffectInstance> effects = new ArrayList<>();
                potionContents.getEffects().forEach(effect -> effects.add(new StatusEffectInstance(effect.getEffectType(), 1, effect.getAmplifier(), true, true, true)));
                for(StatusEffectInstance effect : effects) {
                    livingEntity.addStatusEffect(effect);
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent != null) {
            buildTooltip(potionContentsComponent.getEffects(), tooltip::add);
        }
    }

    public static void buildTooltip(Iterable<StatusEffectInstance> effects, Consumer<Text> textConsumer) {
        List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list = Lists.newArrayList();
        boolean bl = true;

        for (StatusEffectInstance statusEffectInstance : effects) {
            bl = false;
            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
            registryEntry.value().forEachAttributeModifier(statusEffectInstance.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }

            textConsumer.accept(mutableText.formatted(registryEntry.value().getCategory().getFormatting()));
        }

        if (bl) {
            textConsumer.accept(Text.translatable("effect.none").formatted(Formatting.GRAY));
        }

        if (!list.isEmpty()) {
            textConsumer.accept(ScreenTexts.EMPTY);
            textConsumer.accept(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));

            for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
                EntityAttributeModifier entityAttributeModifier = pair.getSecond();
                double d = entityAttributeModifier.value();
                double e;
                if (entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.value();
                } else {
                    e = entityAttributeModifier.value() * 100.0;
                }

                if (d > 0.0) {
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(pair.getFirst().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    textConsumer.accept(
                            Text.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(pair.getFirst().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.RED)
                    );
                }
            }
        }
    }
}
