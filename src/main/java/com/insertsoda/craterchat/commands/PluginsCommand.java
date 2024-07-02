package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.loader.api.entrypoint.EntrypointContainer;

import java.util.ArrayList;

public class PluginsCommand implements Command {

    int amountPerPage = 5;
    ArrayList<ArrayList<ModContainer>> cachedPlugins = new ArrayList<>();
    int totalAmountOfPages = 0;
    @Override
    public void register(LiteralArgumentBuilder<CommandSource> literalArgumentBuilder) {
        literalArgumentBuilder
                .executes(context -> {
                    this.handleCommand(1);

                    return 1;
                })
                .then(CommandManager.argument("page", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            this.handleCommand(IntegerArgumentType.getInteger(context, "page"));

                            return 1;
                        })
                );
    }

    // This is basically identical to the help command
    private void buildPluginsCache(){
        int currentPage = 1;

        this.cachedPlugins.add(0, new ArrayList<>());
        this.cachedPlugins.add(1, new ArrayList<>());


        for(EntrypointContainer<CraterChatPlugin> plugin : QuiltLoader.getEntrypointContainers("craterchatplugin", CraterChatPlugin.class)){
            if(this.cachedPlugins.get(currentPage).size() >= this.amountPerPage){
                currentPage++;
                this.cachedPlugins.add(currentPage, new ArrayList<>());
            }
            this.cachedPlugins.get(currentPage).add(plugin.getProvider());
        }

        this.totalAmountOfPages = currentPage;
    }

    public void refreshPluginsCache(){
        this.cachedPlugins.clear();
        this.buildPluginsCache();
    }

    public void handleCommand(int page){
        if(this.cachedPlugins.isEmpty()){
            this.buildPluginsCache();
        }
        if(page > this.totalAmountOfPages){
            CraterChat.Chat.sendMessage("Entered page " + page + " is higher than the total amount of pages, which is " + this.totalAmountOfPages + "!");
            return;
        }

        StringBuilder pluginsMessage = new StringBuilder("==Loaded CraterChat Plugins ").append(" ").append(page).append("/").append(this.totalAmountOfPages).append(" ==");

        for (ModContainer modContainer : this.cachedPlugins.get(page)) {
            pluginsMessage.append("\n").append(modContainer.metadata().name());
        }

        CraterChat.Chat.sendMessage(pluginsMessage.toString());
    }

    @Override
    public @NotNull String getName() {
        return "plugins";
    }

    @Override
    public @Nullable String getDescription() {
        return "Returns a list of active CraterChat plugins";
    }

    @Override
    public String getPossibleArguments(){
        return "<page>";
    }
}
