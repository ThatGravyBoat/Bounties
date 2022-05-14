package tech.thatgravyboat.bounties.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.thatgravyboat.bounties.EventManager;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onItemPickup", at = @At("HEAD"))
    public void onItemPickup(ItemEntity item, CallbackInfo ci) {
        //noinspection ConstantConditions
        if (((Object)this) instanceof Player player) {
            EventManager.onItemObtained(item.getItem(), player);
        }
    }
}
