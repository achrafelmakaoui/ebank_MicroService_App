package com.elmakaoui.ebankbot.discord;

import com.elmakaoui.ebankbot.agents.EbankAIAgent;
import com.zgamelogic.discord.annotations.DiscordController;
import com.zgamelogic.discord.annotations.DiscordMapping;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.ai.chat.prompt.Prompt;

@DiscordController
public class DiscordBot {
    private final EbankAIAgent ebankAiAgent;

    public DiscordBot(EbankAIAgent ebankAiAgent) {
        this.ebankAiAgent = ebankAiAgent;
    }
    @DiscordMapping
    public void perform(MessageReceivedEvent event){
        System.out.println(event);
        if(!event.getAuthor().isBot()){
            String query=event.getMessage().getContentRaw();
            String response=ebankAiAgent.chat(new Prompt(query),event.getAuthor().getId());
            event.getChannel().sendMessage(response).queue();
        }
    }
}