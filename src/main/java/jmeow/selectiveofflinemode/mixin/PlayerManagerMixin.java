package jmeow.selectiveofflinemode.mixin;

import jmeow.selectiveofflinemode.NameExpiry;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;
import java.util.Date;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
    @Inject(method = "checkCanJoin", at = @At(value = "RETURN"), cancellable = true)
    private void checkCanJoin(SocketAddress address, PlayerConfigEntry configEntry, CallbackInfoReturnable<Text> cir) {
        if (NameExpiry.names.containsKey(configEntry.name()) && NameExpiry.names.get(configEntry.name()).after(new Date())) {
            cir.setReturnValue(null);
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}