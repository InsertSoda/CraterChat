package com.insertsoda.craterchat.commands;

import com.insertsoda.craterchat.CraterChat;
import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.api.v1.CommandManager;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpCommand implements Command {

    int amountPerPage = 5;
    ArrayList<ArrayList<CommandContainer>> cachedCommands = new ArrayList<>();
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

    private void buildCommandsCache(){
        int currentPage = 1;

        //  I don't want to do page - 1 constantly to offset the index to follow stuff starting at 0 I'm doing this
        cachedCommands.add(0, new ArrayList<>());
        // also add an arraylist at index 1, so it doesn't cause an exception when getting index 1
        cachedCommands.add(1, new ArrayList<>());


        for(CommandContainer commandContainer : CraterChat.getRegisteredCommands().values()){
            if(cachedCommands.get(currentPage).size() >= amountPerPage){
                currentPage++;
                cachedCommands.add(currentPage, new ArrayList<>());
            }
            cachedCommands.get(currentPage).add(commandContainer);
        }

        totalAmountOfPages = currentPage;
    }

    private void handleCommand(int page){
        if(cachedCommands.isEmpty()){
            this.buildCommandsCache();
        }
        if(page > totalAmountOfPages){
            CraterChat.Chat.sendMessage("Entered page " + page + " is higher than the total amount of pages, which is " + totalAmountOfPages + "!");
            return;
        }

        StringBuilder helpMessage = new StringBuilder("==Help ").append(" ").append(page).append("/").append(totalAmountOfPages).append(" ==");

        for (CommandContainer commandContainer : cachedCommands.get(page)) {
            helpMessage.append("\n").append("/").append(commandContainer.getMetadata().getName()).append(" || ");
            if(commandContainer.getMetadata().getDescription() == null){
                helpMessage.append("No description provided");
            } else {
                helpMessage.append(commandContainer.getMetadata().getDescription());
            }

            helpMessage.append(" (Added by ").append(commandContainer.getMetadata().getSourceModContainer().metadata().name()).append(")");
        }

        CraterChat.Chat.sendMessage(helpMessage.toString());
    }

    @Override
    public String getDescription() {
        return "Returns a list of every command with a description";
    }

    @Override
    public @NotNull String getName() {
        return "help";
    }
}
