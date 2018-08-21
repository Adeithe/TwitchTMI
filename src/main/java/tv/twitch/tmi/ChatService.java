package tv.twitch.tmi;

import lombok.Getter;
import tv.twitch.handle.impl.events.ConnectEvent;
import tv.twitch.handle.impl.obj.tmi.RawData;
import tv.twitch.utils.Parser;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

public class ChatService extends Thread {
	public static final String IP = "irc.chat.twitch.tv";
	public static final int PORT = 6697;
	
	private SocketFactory SocketFactory = SSLSocketFactory.getDefault();
	private Socket Socket;
	private BufferedWriter Writer;
	private BufferedReader Reader;
	
	private TwitchTMI TMI;
	
	@Getter private boolean connected;
	
	ChatService(TwitchTMI TMI) {
		this.TMI = TMI;
	}
	
	@Override
	public void run() {
		try {
			if(this.connect()) {
				this.sendRawData(
					"CAP REQ :twitch.tv/membership",
					"CAP REQ :twitch.tv/tags",
					"CAP REQ :twitch.tv/commands",
					
					"PASS " + this.TMI.getClient().getOAuth(),
					"NICK " + this.TMI.getClient().getUsername()
				);
				
				String line = null;
				while(this.isConnected() && ((line = this.Reader.readLine()) != null)) {
					if(this.TMI.getClient().getSettings().getVerbose().getLevel() > 0)
						System.out.println("> "+ line);
					RawData rawData = Parser.msg(line);
				}
				
				this.Socket.close();
				this.Writer.close();
				this.Reader.close();
			}
		} catch(IOException e) {
			System.out.println("Failed to read line!");
		}
	}
	
	void sendRawData(String... data) throws IOException {
		if(this.connected) {
			for(int i = 0; i < data.length; i++) {
				String line = new String(data[i].getBytes(), "UTF-8");
				if(!line.endsWith("\r\n"))
					line += "\r\n";
				if(this.TMI.getClient().getSettings().getVerbose().getLevel() > 1)
					System.out.println("< "+ line.substring(0, line.length() - 2));
				this.Writer.write(line);
			}
			this.Writer.flush();
		}
	}
	
	boolean connect() {
		try {
			this.Socket = SocketFactory.createSocket(IP, PORT);
			this.Writer = new BufferedWriter(new OutputStreamWriter(this.Socket.getOutputStream()));
			this.Reader = new BufferedReader(new InputStreamReader(this.Socket.getInputStream()));
			this.connected = true;
			this.TMI.getClient().getEventDispatcher().dispatch(new ConnectEvent(IP, PORT));
		} catch(IOException e) {
			this.connected = false;
		}
		return this.connected;
	}
	
	void disconnect() { this.connected = false; }
}
