package me.kavin.mememachine.command.commands;

import java.util.ArrayList;
import java.util.Random;

import me.kavin.mememachine.command.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Dab extends Command{
	public Dab(){
	super(">dab", "`Shows a person dabbing`");
		memeurls.add("https://ibb.co/kjGceb");
		memeurls.add("https://ibb.co/cPbokG");
		memeurls.add("https://ibb.co/gX8qzb");
		memeurls.add("https://ibb.co/cRzKsw");
		memeurls.add("https://ibb.co/ejvEQG");
		memeurls.add("https://ibb.co/n6E8kG");
		memeurls.add("https://ibb.co/dJgAzb");
		memeurls.add("https://ibb.co/gSV3Kb");
		memeurls.add("https://ibb.co/cN8qzb");
		memeurls.add("https://ibb.co/ndSTkG");
	}
	ArrayList<String> memeurls = new ArrayList<String>();
	@Override
	public void onCommand(String message , MessageReceivedEvent event) {
	if (message.equalsIgnoreCase(">dab")){
		event.getChannel().sendMessage(getMeme()).queue();
	}
	}
	private String getMeme() {
		return memeurls.get(new Random().nextInt(memeurls.size()));
	}
}
