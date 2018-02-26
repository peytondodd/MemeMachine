package me.kavin.gwhpaladins.listener;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kavin.gwhpaladins.Main;
import me.kavin.gwhpaladins.command.Command;
import me.kavin.gwhpaladins.command.CommandManager;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter{
	
	HashMap<String, String> lastMsg = new HashMap<String, String>();
	
	public static void init(){
	Main.api.addEventListener(new DiscordListener());
	}
	@Override
	public void onReady(ReadyEvent event) {
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | .help", "Hax.kill"));
		Main.channels = new ArrayList<TextChannel>(Main.api.getTextChannels());
		
		List<Guild> guilds = Main.api.getGuilds();
		
		new Thread(new Runnable() {

			@Override
			public void run() {


				while(true){
					for(Guild g : guilds){
						if(g.getName().startsWith("Project"))
						for(Role role : g.getRoles()){
							if(!role.getName().equalsIgnoreCase("Meme Machine") && !role.getName().equalsIgnoreCase("@everyone") && !role.getName().equalsIgnoreCase("JukeBot") && !role.getName().equalsIgnoreCase("the memer") && !role.getName().equalsIgnoreCase("himebot"))
							role.getManager().setColor(getRainbowColor(60000, 0)).queue();
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
						}
					}
				}
				
			}
		}).start();
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
	if(event.getAuthor() == Main.api.getSelfUser()){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
				}
				event.getMessage().delete().queue();
			}
		}).start();
	} else {
		if(!event.getAuthor().isBot() && lastMsg.containsKey(event.getAuthor().getName())) {
			if(lastMsg.get(event.getAuthor().getName()).startsWith(event.getMessage().getRawContent())) {
				event.getChannel().sendMessage("You may not spam!").queue();
				event.getMessage().delete().queue();
			}
		}
		lastMsg.put(event.getAuthor().getName(), event.getMessage().getRawContent());
	}
	if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser()){
		return;
	}
	for (Command cmd : CommandManager.commands)
		cmd.onCommand(event.getMessage().getRawContent() , event);
	}
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getRawContent().startsWith("."))
		event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").queue();
	}
	/**
     * @param speed is the time in milliseconds the color needs for a whole loop.
     * @param offset is the value the time gets displaced to create different loops.
     * @return a int color based on System.currentTimeMillis().
     */
    public static Color getRainbowColor(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 1f, 1f);
    }
}