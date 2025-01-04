package dog.robysaurus.effecttotems.item;

import dog.robysaurus.effecttotems.EffectTotems;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.*;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static final Item TOTEM = register(EffectTotemItem::new, new Item.Settings().maxCount(1).component(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT));
    
    private static Item register(Function<Item.Settings, Item> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(EffectTotems.MOD_ID, "totem"));
        Item item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }
    
    public static void registerModItems(){
        EffectTotems.LOGGER.info("Registering Mod Items for " + EffectTotems.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            ItemGroup.DisplayContext displayContext = entries.getContext();
            displayContext.lookup()
                    .getOptional(RegistryKeys.POTION)
                    .ifPresent(registryWrapper -> addPotions(entries, registryWrapper, displayContext.enabledFeatures()));
        });
    }

    private static void addPotions(ItemGroup.Entries entries, RegistryWrapper<Potion> registryWrapper, FeatureSet enabledFeatures) {
        registryWrapper.streamEntries()
                .filter(potionEntry -> potionEntry.value().isEnabled(enabledFeatures))
                .filter(potionEntry -> !potionEntry.value().hasInstantEffect())
                .filter(potionEntry -> 
                        !potionEntry.matches(Potions.LONG_NIGHT_VISION) &&
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
                        !potionEntry.matches(Potions.LONG_SLOW_FALLING))
                .map(entry -> PotionContentsComponent.createStack(ModItems.TOTEM, entry))
                .forEach(stack -> entries.add(stack, ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS));
    }
}
