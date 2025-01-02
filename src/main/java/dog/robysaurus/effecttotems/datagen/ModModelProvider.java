package dog.robysaurus.effecttotems.datagen;

import dog.robysaurus.effecttotems.EffectTotems;
import dog.robysaurus.effecttotems.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ModelIds;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        Identifier identifier = itemModelGenerator.uploadTwoLayers(ModItems.TOTEM, Identifier.of(EffectTotems.MOD_ID, "item/totem_overlay"), ModelIds.getItemModelId(ModItems.TOTEM));
        itemModelGenerator.registerPotionTinted(ModItems.TOTEM, identifier);
    }
}
