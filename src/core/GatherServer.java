package core;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**represents a gather KAG server, keeps track of the connection between the bot and the server, the properties of the server, and the status of the server
 * @author cameron
 */
public class GatherServer
{
	static final Logger LOGGER = LoggerFactory.getLogger(GatherServer.class);
	private String serverName;
	private String serverID;
	private String ip;
	private int port;
	private String rconPassword;				//sv_rconpassword
	private String serverPassword;				//sv_password
	private String serverLink;				//kag://127.0.0.1:50301/password
	private KagServerChecker serverCheckObject;
	private boolean isInUse;
	
	private Thread rconThread;
	
	GatherServer(String ip, int port, String rconPassword, String serverPassword, String serverLink)
	{
		this.ip=ip;
		this.port=port;
		this.rconPassword=rconPassword;
		this.serverPassword=serverPassword;
		this.serverLink=serverLink;
		this.isInUse=false;
	}
	
	/**Establish the TCPR connection with this KAG server. 
	 */
	public boolean connect()
	{
		try
		{
			//check for and stop any existing connections
			if(rconThread != null) rconThread.interrupt();
			//now create the new connection
			serverCheckObject = new KagServerChecker(ip, port, rconPassword);
			//add an rcon listener to process any received messages
			serverCheckObject.addListener(new RconListener());
			//create a thread for running the checker
			rconThread = new Thread(serverCheckObject);
			rconThread.start();
			return true;
		} catch (IOException e)
		{
			LOGGER.error("An error occured connecting to the gather KAG server("+e.getMessage()+"): "+ip+":"+port);
			rconThread = null;
			serverCheckObject = null;
			return false;
		}
	}
	
	/**Check if this connection thinks it is still connected to the server. 
	 * @return false if the connection variables indicate the server is not connected, true otherwise
	 * @see KagServerChecker#isConnected()
	 */
	public boolean isConnected()
	{
		if(rconThread == null || !serverCheckObject.isConnected()) return false;
		return true;
	}
	
	/**Check if this connection thinks it is still trying to reestablish a previous connection
	 * @return false if the reconnecting variables indicate the server is not connected, true otherwise
	 * @see KagServerChecker#isReconnecting()
	 */
	public boolean isReconnecting()
	{
		if(rconThread == null || serverCheckObject.isReconnecting()) return false;
		else return true;
	}
	
	/**Interrupts the connection listener, triggering a disconnect from the KAG server. 
	 */
	public void disconnect()
	{
		if(rconThread != null)
		{
			rconThread.interrupt();
		}
	}
	
	/**Send a message to the KAG server. 
	 * @param msg the message to send
	 * @see #KagServerChecker
	 */
	public void sendMessage(String msg)
	{
		if(serverCheckObject == null)
		{
			LOGGER.error("Could not send message to kag server, serverConnection is null");
			return;
		}
		serverCheckObject.sendMessage(msg);
	}
	
	/**Say a message to the users of the gather server. 
	 * @param msg the message to say
	 */
	public void say(String msg)
	{
		this.sendMessage("getNet().server_SendMsg(\""+msg+"\");");
	}
	
	/**Sends the clear game command to the gather server. 
	 */
	public void clearGame()
	{
		this.sendMessage("getRules().set_bool('clearGame', true);");
	}
	
	/**Check if two GatherServer objects are equal by comapring the ip and port of the two objects. 
	 * @param obj the object to compare with
	 * @return true if the ip address and port are both equal, otherwise false
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return ((GatherServer)obj).ip.equals(this.ip) && ((GatherServer)obj).port == this.port;
	}

	/**Getter for the server name string, only used by the bot for allowing players to differentiate servers. The value of this has no effect on the bot behavior. 
	 * @return the server name string
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**Setter for the server name string, only used by the bot for allowing players to differentiate servers. The value of this has no effect on the bot behavior. 
	 * @param serverName the new server name to use
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}

	/**Getter for the server id string, a reference id for passing to commands that need a server specified.
	 * @return the server id string
	 */
	public String getServerID()
	{
		return serverID;
	}

	/**Setter for the server id string, a reference id for passing to commands that need a server specified.
	 * @param serverName the new server id to use
	 */
	public void setServerID(String serverID)
	{
		this.serverID = serverID;
	}

	/**Getter for the server ip address. 
	 * @return the server ip address
	 */
	public String getIp()
	{
		return ip;
	}

	/**Setter for the server ip address. 
	 * @param ip the new server ip address
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**Getter for the server port. 
	 * @return the server port
	 */
	public int getPort()
	{
		return port;
	}

	/**Setter for the server port. 
	 * @param port the new server port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**Getter for the server password. 
	 * @return the current server password
	 */
	public String getServerPassword()
	{
		return serverPassword;
	}

	/**Setter for the server password. 
	 * @param serverPassword the new server password
	 */
	public void setServerPassword(String serverPassword)
	{
		this.serverPassword = serverPassword;
	}

	/**Getter for the http:// or kag:// link players can use to connect with this server. 
	 * @return the server link
	 */
	public String getServerLink()
	{
		if(this.serverLink != null) return this.serverLink;
		else return "<kag://"+this.getIp()+":"+this.getPort()+"/"+this.getServerPassword()+">";
	}

	/**Setter for the http:// or kag:// link players can use to connect with this server. 
	 * @param serverLink the new server link
	 */
	public void setServerLink(String serverLink)
	{
		this.serverLink = serverLink;
	}

	/**Getter for checking if the server is currently in use for a gather game. 
	 * @return true if the server is in use
	 */
	public boolean isInUse()
	{
		return isInUse;
	}

	/**Setter for whether the server is in use. 
	 * @param isInUse the new value for isInUse
	 */
	public void setInUse(boolean isInUse)
	{
		this.isInUse = isInUse;
	}
}
