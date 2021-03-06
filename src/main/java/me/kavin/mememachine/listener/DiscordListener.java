package me.kavin.mememachine.listener;

import java.util.ArrayList;

import me.kavin.mememachine.Main;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.command.CommandManager;
import me.kavin.mememachine.lists.Api;
import me.kavin.mememachine.utils.ColorUtils;
import me.kavin.mememachine.utils.Multithreading;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordListener extends ListenerAdapter {
	
	int bots = 0;
	int users = 0;
	
	public static void init(){
	Main.api.addEventListener(new DiscordListener());
	}
	@Override
	public void onReady(ReadyEvent event) {
		Api.loop();
		Main.api.getPresence().setStatus(OnlineStatus.IDLE);
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!", "Hax.kill"));
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Guild> toRemove = new ArrayList<Guild>();
				for(Guild g : Main.api.getGuilds()) {
					g.getMembers().forEach( member -> {
						if(member.getUser().isBot())
							bots++;
						else
							users++;
					}); 
					if((double)bots / (double)users > 2) {
						g.getOwner().getUser().openPrivateChannel().complete().sendMessage("Unfortunately your discord server has a high user/bot ratio. \nTry inviting more people on your server or delete some bots.\n feel free to invite me back from\n https://discordapp.com/oauth2/authorize?client_id=" + Main.api.getSelfUser().getId() + "&permissions=8&scope=bot").complete();
						toRemove.add(g);
					}
				}
				for(Guild g : toRemove)
					g.leave().complete();
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					for(Guild g : Main.api.getGuilds()){
						for(Role role : g.getRoles()){
							if(role.getName().equalsIgnoreCase("rainbow")) {
								role.getManager().setColor(ColorUtils.getRainbowColor(120000)).complete();
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) { }
							}
						}
					}
				}
				
			}
		}).start();
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		if (!event.getGuild().getMember(Main.api.getSelfUser()).hasPermission(Permission.ADMINISTRATOR)) {
			event.getGuild().getDefaultChannel().sendMessage("Please ask a server admistrator to invite me!").complete();
		}
		Guild g = event.getGuild();
		g.getMembers().forEach( member -> {
			if(member.getUser().isBot())
				bots++;
			else
				users++;
		}); 
		if((double)bots / (double)users > 2) {
			g.getOwner().getUser().openPrivateChannel().complete().sendMessage("Unfortunately your discord server has a high user/bot ratio. \nTry inviting more people on your server or delete some bots.\n feel free to invite me back from\n https://discordapp.com/oauth2/authorize?client_id=" + Main.api.getSelfUser().getId() + "&permissions=8&scope=bot").complete();
			g.leave().complete();
		}
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!", "Hax.kill"));
	}
	
	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event) {
		event.getGuild().getOwner().getUser().openPrivateChannel().complete().sendMessage("Congrats on becoming the new owner of " + event.getGuild().getName()).complete();
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		Main.api.getPresence().setGame(Game.of(GameType.DEFAULT, "Meminq | >help | " + Main.api.getGuilds().size() + " Servers!", "Hax.kill"));
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.PRIVATE) || event.getAuthor() == Main.api.getSelfUser() || event.getAuthor().isBot())
			return;
		for (Command cmd : CommandManager.commands)
			Multithreading.runAsync(new Runnable() {
				@Override
				public void run() {
					cmd.onCommand(event.getMessage().getContentRaw() , event);
				}
			});
	}
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		if(event.getAuthor() != Main.api.getSelfUser() && event.getMessage().getContentRaw().startsWith(">"))
		event.getMessage().getChannel().sendMessage("Error: I don't reply to PM's!").complete();
	}
}