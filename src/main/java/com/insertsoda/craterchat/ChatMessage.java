package com.insertsoda.craterchat;

import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.UI;

public class ChatMessage {

    private String messageContent;

    long startTime;

    public ChatMessage(String messageContent){
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setStartTime(long startTime){
        this.startTime = startTime;
    }

    public void render(Viewport uiViewport, float height){
        if(TimeUtils.timeSinceMillis(this.startTime) < 7500 || CraterChat.Chat.isOpen()) {
            FontRenderer.drawText(UI.batch, uiViewport, this.messageContent, -uiViewport.getWorldWidth() / 2.0F + 25, height);
        }
    }
}
