package com.insertsoda.craterchat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.insertsoda.craterchat.api.v1.CommandContainer;
import com.insertsoda.craterchat.impl.CommandSourceImpl;
import com.insertsoda.craterchat.api.v1.CommandSource;
import com.insertsoda.craterchat.mixins.UITextInputAccessor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.settings.Keybind;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.ui.UITextInput;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Chat {

    CommandDispatcher<CommandSource> commandDispatcher;
    ParseResults<CommandSource> parse;
    CompletableFuture<Suggestions> commandSuggestions = null;
    String infoString = "";
    LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();
    UITextInput textInput = null;
    boolean isOpen = false;
    float lineHeight = 0.0F;
    public static Keybind chatKeybind = Keybind.fromDefaultKey("chat", Input.Keys.T);

    public boolean isOpen() {
        return isOpen;
    }

    public void toggle(){
        this.isOpen = !this.isOpen;

        if(this.textInput != null) {
            if (this.isOpen) {
                this.textInput.onClick();

                if(UI.itemCatalog.isShown()){
                    UI.itemCatalog.hide();
                }
            } else {
                this.textInput.deactivate();
            }
        }
    }

    public CommandSource buildCommandSource(){
        return new CommandSourceImpl(InGame.getLocalPlayer(), InGame.world);
    }

    public CommandDispatcher<CommandSource> getCommandDispatcher(){
        return this.commandDispatcher;
    }

    public Optional<Exception> executeCommand(String command){
        try {
            this.commandDispatcher.execute(command, buildCommandSource());
        } catch (Exception e) {
            return Optional.of(e);
        }

        return Optional.empty();
    }

    public void sendMessage(ChatMessage message){
        message.setStartTime(TimeUtils.millis());
        this.messages.addFirst(message);

        if(this.messages.size() > 15){
            this.messages.removeLast();
        }
    }

    public void sendMessage(String content){
        ChatMessage message = new ChatMessage(content);
        this.sendMessage(message);
    }

    public void clearChat() {
        this.messages = new LinkedList<>();
    }

    public void buildCommandDispatcher(){
        CraterChat.LOGGER.info("Building the command dispatcher...");
        this.commandDispatcher = new CommandDispatcher<>();
        for(Map.Entry<String, CommandContainer> entry : CraterChat.getRegisteredCommands().entrySet()){
            CommandContainer commandContainer = entry.getValue();
            LiteralArgumentBuilder<CommandSource> literalArgumentBuilder = LiteralArgumentBuilder.literal(entry.getKey());
            commandContainer.getCommand().register(literalArgumentBuilder);
            this.commandDispatcher.register(literalArgumentBuilder);
        }
        CraterChat.LOGGER.info("Built the command dispatcher");
    }

    public void render(Viewport uiViewport, OrthographicCamera uiCamera){
        if(this.lineHeight == 0.0F){
            this.lineHeight = FontRenderer.getTextDimensions(uiViewport, "Hello there", new Vector2()).y;
        }
        if(this.textInput == null){
            this.textInput = new UITextInput(0, 0, 0, 0) {
                @Override
                public void onCreate() {
                    super.onCreate();
                }

                @Override
                public boolean keyDown(int keycode) {
                    switch (keycode) {
                        case Keys.LEFT:
                            if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                                var input = ((UITextInputAccessor)this);

                                int pos = input.getDesiredCharIdx() - 1;
                                if (pos <= 0) return true;

                                // Move left until a non-alphanumeric character or the start of the input
                                do { --pos; }
                                while (pos > 0 && Character.isLetterOrDigit(textInput.inputText.charAt(pos)));

                                input.setDesiredCharIdx(pos);
                                input.setIsDefaultText(false);
                                return true;
                            }
                            break;
                        case Keys.RIGHT:
                            if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                                var input = ((UITextInputAccessor)this);

                                int pos = input.getDesiredCharIdx();
                                if (pos >= textInput.inputText.length()) return true;

                                // Move right until a non-alphanumeric character or the end of the input
                                do { ++pos; }
                                while (pos <= textInput.inputText.length() - 1 && Character.isLetterOrDigit(textInput.inputText.charAt(pos)));

                                input.setDesiredCharIdx(pos);
                                input.setIsDefaultText(false);
                                return true;
                            }
                            break;
                    }

                    return super.keyDown(keycode);
                }

                @Override
                public boolean keyTyped(char character) {
                    // Types in the character into the text field and stores its returning boolean for later
                    boolean keyTypedBoolean = super.keyTyped(character);

                    // Handle suggestions and command syntax errors while typing
                    if(this.inputText.startsWith("/")){
                        parse = commandDispatcher.parse(this.inputText.substring(1), buildCommandSource());

                        StringBuilder infoStringBuilder = new StringBuilder();

                        for(Map.Entry<CommandNode<CommandSource>, CommandSyntaxException> entry : parse.getExceptions().entrySet()){
                            infoStringBuilder.append(entry.getValue().getMessage()).append("\n");
                        }

                        commandSuggestions = commandDispatcher.getCompletionSuggestions(parse);

                        infoString = infoStringBuilder.toString();
                    }

                    // Handle sending messages/commands
                    if(Character.toString(character).equals("\n")){
                        if(this.inputText.startsWith("/")){
                            try {
                                commandDispatcher.execute(parse);
                            } catch (CommandSyntaxException e) {
                                CraterChat.Chat.sendMessage(new ChatMessage("[CommandSyntaxException]: " + e.getMessage()));
                            } catch (Exception e){
                                // This will only happen if a command itself causes an exception
                                CraterChat.Chat.sendMessage(new ChatMessage("[Unknown Exception]: " + e.getMessage()));
                            }
                        } else if(!this.inputText.isEmpty()) {
                            CraterChat.Chat.sendMessage(new ChatMessage("[Player]: " + this.inputText));
                        }

                        infoString = "";
                        commandSuggestions = null;
                        this.inputText = this.getDefaultInputText();
                        CraterChat.Chat.toggle();

                        return false;
                    }

                    if (character == '\t' && commandSuggestions != null) {
                        try {
                            inputText = "/" + commandSuggestions.get().getList().get(0).getText();
                            var input = ((UITextInputAccessor)this);
                            input.setDesiredCharIdx(inputText.length());
                            input.setIsDefaultText(false);
                            return true;
                        } catch (Exception e) {}
                    }

                    if (character == '\b' && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
                        var input = ((UITextInputAccessor)this);

                        int pos = input.getDesiredCharIdx() - 1;
                        if (pos <= 0) return true;

                        // Move left until a non-alphanumeric character or the start of the input
                        do { --pos; }
                        while (pos > 0 && Character.isLetterOrDigit(textInput.inputText.charAt(pos)));

                        input.setDesiredCharIdx(pos);
                        input.setIsDefaultText(false);
                        // The caret has already been moved, so now we can just take a substring from 0-caret to remove a whole word
                        inputText = inputText.substring(0, input.getDesiredCharIdx());
                        return true;
                    }

                    // Resume other operations I guess
                    return keyTypedBoolean;
                }
            };
        }

        Gdx.gl.glActiveTexture(33984);
        UI.batch.setProjectionMatrix(uiCamera.combined);
        UI.batch.begin();

        float height = uiViewport.getWorldHeight() / 4.0F;

        for (ChatMessage message : this.messages) {
            height -= FontRenderer.getTextDimensions(uiViewport, message.getMessageContent(), new Vector2()).y;
            message.render(uiViewport, height);
        }

        if(this.isOpen){
            this.textInput.updateText();
            this.textInput.drawElementBackground(uiViewport, UI.batch);

            int cursorIndex = MathUtils.clamp(((UITextInputAccessor)this.textInput).getDesiredCharIdx(), 0, this.textInput.inputText.length());
            String cursorCharacter = " ";

            long milliseconds = TimeUtils.millis();
            if(milliseconds % 1500 > 750){
                cursorCharacter = "|";
            }

            String text = this.textInput.inputText.substring(0, cursorIndex) + cursorCharacter + this.textInput.inputText.substring(cursorIndex);

            if(this.textInput.inputText.startsWith("/")){
                if(commandSuggestions != null && commandSuggestions.isDone()){
                    try {
                        for (Suggestion suggestion : commandSuggestions.get().getList()) {
                            text += "\n" + suggestion.getText();
                        }
                    } catch(Exception ignored) {
                        // No crashes while suggesting thx
                    }
                }

                text += "\n" + infoString;
            }

            FontRenderer.drawTextbox(UI.batch, uiViewport, "> " + text, -uiViewport.getWorldWidth() / 2.0F + 25, uiViewport.getWorldHeight() / 4.0F + lineHeight * 0.5F, uiViewport.getWorldWidth() / 3.0F);

        }

        UI.batch.end();
    }
}
