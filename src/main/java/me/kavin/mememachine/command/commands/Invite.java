package me.kavin.mememachine.command.commands;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Invite extends Command{
	public Invite(){
	super(">invite", "`Gives you a link to invite me`");
	}
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
		if (message.equalsIgnoreCase(getPrefix())){
			event.getAuthor().openPrivateChannel().complete().sendMessage("`You can invite me here:`\nhttps://discordapp.com/oauth2/authorize?client_id=" + Main.api.getSelfUser().getId() + "&permissions=8&scope=bot").complete();
		}
	}
}
