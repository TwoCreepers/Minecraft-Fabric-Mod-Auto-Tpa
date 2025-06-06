package com.guanshangyu.auto_tpa.client;

import com.guanshangyu.auto_tpa.AutoTpa;
import com.guanshangyu.auto_tpa.client.config.ConfigManager;
import com.guanshangyu.auto_tpa.client.config.GuiRegistry.StringListGuiProvider;
import com.guanshangyu.auto_tpa.client.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoTpaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigManager.register();
        AutoConfig.getGuiRegistry(ModConfig.class).registerTypeProvider(new StringListGuiProvider(), List.class);
        ClientReceiveMessageEvents.GAME.register(this::onMessage);
        AutoTpa.LOGGER.info(AutoTpa.MOD_ID + " init");
    }
    public void onMessage(Text message, boolean overlay) {
        // 检查是否达到工作条件
        if (overlay) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        ChatHud hud = client.inGameHud.getChatHud();
        if (hud == null) return;
        ModConfig config = ConfigManager.getConfig();
        if (config.autoAcceptEnabled) return;
        String rawMessage = message.getString();
        Pattern tpaPattern = Pattern.compile(config.tpaPattern);
        Matcher matcher = tpaPattern.matcher(rawMessage);
        if (!matcher.find()) return;

        String senderName = matcher.group(1);

        // 构建命令（支持自定义命令）
        final String commandAccept = config.customAcceptCommand;
        final String commandReject = config.customRejectCommand;

        // 检查黑名单
        if (config.blacklist.contains(senderName)) {
            if (config.showNotification) {
                Objects.requireNonNull(client.getNetworkHandler()).sendChatCommand(commandReject);
                client.execute(()->hud.addMessage(Text.of("§c[自动TPA] 拒绝黑名单玩家: " + senderName)));
            }
            return;
        }
        // 检查好友模式和白名单
        if (config.onlyFromFriends && !config.whitelist.contains(senderName)) {
            if (config.showNotification) {
                Objects.requireNonNull(client.getNetworkHandler()).sendChatCommand(commandReject);
                client.execute(()->hud.addMessage(Text.of("§6[自动TPA] 拒绝非白名单玩家: " + senderName)));
            }
            return;
        }
        // 发送通知
        if (config.showNotification) {
            String delayMsg = "§a[自动TPA] 正在接受来自 " + senderName + " 的传送请求";
            client.execute(()->hud.addMessage(Text.of(delayMsg)));
        }
        Objects.requireNonNull(client.getNetworkHandler()).sendChatCommand(commandAccept);
    }
}
