package nsg.component;

import nsg.NSGParameters;


public class Agent extends NSGComponent implements NSGParameters{
	public int id;
	
//	public JComboBox agentType = new JComboBox(new String[] { "TCP", "TCP/Tahoe", "TCP/Reno", "TCP/Newreno", "TCP/Vegas", "TCPSink", "UDP", "NULL" });
//	public JTextField packetSize = new JTextField("1500"); // Bytes
//	public JTextField window_ = new JTextField("");
//	public JTextField maxcwnd_ = new JTextField("");
//	public JTextField windowInit_ = new JTextField("");
//	public JTextField tcpTick_ = new JTextField("");
//	public JTextField maxburst_ = new JTextField(""); 	
	public int agentType;
	public Node attachedNode;
	public Agent remoteAgent;	
	public int packetSize = -1; 
	public int window = -1;
	public int maxcwnd = -1; 
	public int windowInit = -1; 
	public int tcpTick = -1; 
	public int maxburst = -1; 
	

	public Agent(int id, int x, int y){
		super(NSGComponent.AGENT);
		this.id = id;
		this.x=x;
		this.y=y;
	}

	public static String convertType(int type){
		switch (type){
		case AGENT_TCP:
			return "TCP";
		case AGENT_TCP_SINK:
			return "TCPSink";
		case AGENT_UDP:
			return "UDP";
		case AGENT_NULL:
			return "Null";
		case AGENT_TCP_TAHOE:///
			return "TCP/FullTcp/Tahoe";
		case AGENT_TCP_RENO:
			return "TCP/Reno";
		case AGENT_TCP_NEWRENO:
			return "TCP/Newreno";
		case AGENT_TCP_VEGAS:
			return "TCP/Vegas";
		default:
			System.out.println("Agent convertType error");
			return "unknow";
		}
	}
}
