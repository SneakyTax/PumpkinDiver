package com.afforess.pumpkindiver;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PumpkinDiver extends JavaPlugin {

	public final PumpkinDiverPlayerListener listener = new PumpkinDiverPlayerListener();
	public static Logger log;
	public static Server server;
	public static Plugin instance;
	public static PluginDescriptionFile description;
	
	

	public void onEnable(){
		log = Logger.getLogger("Minecraft");
		server = this.getServer();
		description = this.getDescription();
		instance = this;
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, listener, Priority.Monitor, this);
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
	}
	
	public void onDisable(){
		
	}
}
