package xyz.iwolfking.scalingbingoseals.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import iskallia.vault.config.VaultCrystalConfig;
import iskallia.vault.config.entry.LevelEntryList;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.layout.ClassicInfiniteCrystalLayout;
import iskallia.vault.item.crystal.objective.CrystalObjective;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import xyz.iwolfking.scalingbingoseals.config.ScalingBingoSealsConfig;
import xyz.iwolfking.scalingbingoseals.objective.ScalingBingoCrystalObjective;

import java.util.Map;

@Mixin(value = VaultCrystalConfig.class, remap = false)
public class MixinVaultCrystalConfig {
    @Shadow private Map<ResourceLocation, LevelEntryList<VaultCrystalConfig.SealEntry>> SEALS;

    @WrapOperation(method = "lambda$applySeal$18", at = @At(value = "INVOKE", target = "Liskallia/vault/item/crystal/CrystalData;setObjective(Liskallia/vault/item/crystal/objective/CrystalObjective;)V"))
    private static void modifyScalingBingoObjective(CrystalData instance, CrystalObjective objective, Operation<Void> original) {
        if(objective instanceof ScalingBingoCrystalObjective) {
            if(instance.getObjective() instanceof ScalingBingoCrystalObjective scalingBingoCrystalObjective) {
                ScalingBingoCrystalObjective newObjective = new ScalingBingoCrystalObjective(scalingBingoCrystalObjective.getObjectiveProbability(), scalingBingoCrystalObjective.getSealCount() + ScalingBingoSealsConfig.COMMON.sealAddCount.get());
                instance.setObjective(newObjective);
                if(ScalingBingoSealsConfig.COMMON.sizeToSetInfiniteLayout.get() != 0 && newObjective.getHeight() >= ScalingBingoSealsConfig.COMMON.sizeToSetInfiniteLayout.get()) {
                    instance.setLayout(new ClassicInfiniteCrystalLayout(1));
                }
                return;
            }
        }

        original.call(instance, objective);
    }
}
