package nsg;

import java.awt.Color;

public interface NSGParameters {
	static final long serialVersionUID = 0;
	
	//Systm colors
	public static final Color DSEKTOP_BACKGROUND_COLOR 	= new Color(37,111, 150);
	public static final Color INFO_COLOR 				= Color.BLUE;
	public static final Color STATUS_LABEL_COLOR 		= new Color(228,222, 109);
	public static final Color TOOLBAR_COLOR 			= Color.WHITE;
	public static final Color BUTTON_ENABLE_COLOR 		= Color.LIGHT_GRAY;
	public static final Color BUTTON_DISABLE_COLOR 		= new Color(238,238, 238);
	
	public static final Color BACKGROUND_COLOR       	= Color.WHITE;
	public static final Color PANEL_COLOR       		= new Color(209,207, 250);
	public static final Color NODE_COLOR             	= new Color(194,180, 237);
	public static final Color NODE_TEXT_COLOR          = Color.BLACK;
	public static final Color AGENT_COLOR             	= new Color(0,128, 192);
	public static final Color AGENT_LINK_COLOR       	= Color.PINK;
	public static final Color APP_COLOR             	= new Color(0,187, 0);
	public static final Color IR_COLOR               	= Color.PINK;
	public static final Color SR_COLOR               	= new Color(168,168, 168);
	public static final Color GRID_COLOR       			= new Color(200,200, 200);
	public static final Color CONNECTED_COLOR        	= Color.BLUE;
	public static final Color LINK_COLOR             	= new Color(0	,64, 128);
	public static final Color TARGET_COLOR             	= Color.RED;
	public static final Color SRC_COLOR              	= Color.GREEN;	
//	public static final Color FTP_COLOR              	= Color.RED;
//	public static final Color CBR_COLOR              	= Color.MAGENTA; 
//	public static final Color PING_COLOR             	= Color.DARK_GRAY;
	
	//TCL mode
	public static final int WIRED_MODE = 1;
	public static final int WIRELESS_MODE = 2;
	
	//Scene virtualizer mode
	public static final int NORMAL_MODE = 0;
	public static final int HAND_MODE = 1;
	public static final int CREATING_NODE_MODE = 2;
	public static final int CREATING_AGENT_MODE = 3;
	public static final int CREATING_APP_MODE = 4;
	public static final int CREATING_LINK_MODE = 5;
	
	//Agent-type
	public static final int AGENT_TCP = 0;
	public static final int AGENT_TCP_TAHOE = 4;
	public static final int AGENT_TCP_RENO = 8;
	public static final int AGENT_TCP_NEWRENO = 12;
	public static final int AGENT_TCP_VEGAS = 16;
//	public static final int AGENT_TCP_FACK = 20;
//	public static final int AGENT_TCP_FULLTCP = 24;	
	
	public static final int AGENT_TCP_SINK = 1;	
//	public static final int AGENT_TCP_SINK_SACK1 = 5;	
//	public static final int AGENT_TCP_SINK_SACK1_DELACK = 9;	
//	public static final int AGENT_TCP_SINK_DELACK = 13;	
	
	public static final int AGENT_UDP = 2;	
	
	public static final int AGENT_NULL = 3;
	
	//Application-type
	public static final int APP_FTP = 0;
	public static final int APP_CBR = 1;
	public static final int APP_PING = 2;
	public static final int APP_EXPONENTIAL = 3;
	public static final int APP_PARETO = 4;	
	
	//Queue-type
	public static final int QUEUE_DROP_TAIL = 0;
	public static final int QUEUE_RED = 1;
	public static final int QUEUE_FQ = 2;
	public static final int QUEUE_DRR = 3;
	public static final int QUEUE_SFQ = 4;
	public static final int QUEUE_CBQ = 5;
	
	//Link-type
	public static final int DUPLEX_LINK = 0;
	public static final int SIMPLEX_LINK = 1;	
	
}
