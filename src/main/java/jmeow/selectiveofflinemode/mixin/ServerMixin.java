package jmeow.selectiveofflinemode.mixin;

import jmeow.selectiveofflinemode.NameExpiry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


import java.util.Date;
import java.util.Map;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerMixin {
	@Shadow
	@Nullable
	String profileName;

	@Shadow @Final
    static Logger LOGGER;

	@Unique
	private static final Map<String, Date> nameExpiry;

	static {
		nameExpiry = Map.ofEntries();
	}

	@Redirect(method = "onHello", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;isOnlineMode()Z"))
	private boolean isOnlineMode(MinecraftServer instance) {
        if (profileName != null) {
			if(NameExpiry.names.containsKey(profileName) && NameExpiry.names.get(profileName).after(new Date())) {
				LOGGER.info("{} joined with permission", profileName);
				return false;
			}
        }
        return true;
	}
}