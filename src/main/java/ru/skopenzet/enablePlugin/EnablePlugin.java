package ru.skopenzet.enablePlugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public final class EnablePlugin extends JavaPlugin {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        getLogger().info("Плагин запустился");
        saveDefaultConfig();
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                    Commands.literal("hello")
                            .executes(context -> {
                                String raw = getConfig().getString("messages.command-hello", "<red>Ошибка");
                                context.getSource().getSender().sendMessage(MM.deserialize(raw));
                                return Command.SINGLE_SUCCESS;
                            })
                            .build()
            );
            event.registrar().register(
                            Commands.literal("enableplugin")
                                    .then(Commands.literal("reload")
                                    .requires(source -> source.getSender().hasPermission("enableplugin.reload"))
                                    .executes(context -> {
                                        reloadConfig();
                                        String raw = getConfig().getString("messages.command-reload", "<red>Ошибка");
                                        context.getSource().getSender().sendMessage(MM.deserialize(raw));
                                        return Command.SINGLE_SUCCESS;
                                    })
                    )
                    .build()
                );
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин остановлен");
    }
}