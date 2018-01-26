package nsg;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import nsg.component.Agent;
import nsg.component.App;
import nsg.component.Link;
import nsg.component.Node;
import nsg.interactive.CreatingAgentModeHandler;
import nsg.interactive.CreatingAppModeHandler;
import nsg.interactive.CreatingLinkModeHandler;
import nsg.interactive.CreatingNodeModeHandler;
import nsg.interactive.HandModeHandler;
import nsg.interactive.NormalModeHandler;
import nsg.panels.AgentPanel;
import nsg.panels.AppPanel;
import nsg.panels.HandModePanel;
import nsg.panels.LinkPanel;
import nsg.panels.NodePanel;
import nsg.panels.NormalModePanel;
import nsg.tool.Tool;

public class SceneVirtualizer extends JComponent implements NSGParameters {
	static final long serialVersionUID = 0;

	public int mainMode;
	
	public int shiftX;
	public int shiftY;

	// public NSGComponent target;
	// public Node linkSrc;
	// public Node linkDst;

	//Mode handlers
	public HandModeHandler handModeHandler;
	public CreatingNodeModeHandler creatingNodeModeHandler;
	public NormalModeHandler normalModeHandler;
	public CreatingLinkModeHandler creatingLinkModeHandler;
	public CreatingAgentModeHandler creatingAgentModeHandler;
	public CreatingAppModeHandler creatingAppModeHandler;
	
	//Mode panels
	public JPanel modePanel;
	public LinkPanel linkPanel= new LinkPanel(this);
	public NodePanel nodePanel= new NodePanel(this);
	public AgentPanel agentPanel= new AgentPanel(this);
	public AppPanel appPanel= new AppPanel(this);
	public NormalModePanel normalModePanel= new NormalModePanel();
	public HandModePanel handModePanel= new HandModePanel();
	
	public int transmissionRange= 250;
	public int interferenceRange= 550;
	
	public float scale = 1.0f;
	public SceneManager sm;
	DataMaintainer dm;
	public SceneVirtualizer(SceneManager sm) {
		this.sm = sm;
		sm.sv = this;
		this.dm = sm.dm;
				
		handModeHandler = new HandModeHandler(sm);
		creatingNodeModeHandler = new CreatingNodeModeHandler(sm);
//		normalModeHandler = new NormalModeHandler(sm);
		creatingLinkModeHandler = new CreatingLinkModeHandler(sm);
		creatingAgentModeHandler = new CreatingAgentModeHandler(sm);
		creatingAppModeHandler = new CreatingAppModeHandler(sm);		
		
		if (sm.sceneMode == WIRED_MODE){
			shiftX = -5000;
			shiftY = -5000;
		}else{
			shiftY = -10137+NSG2.getMainDesktopPane().getHeight();
		}
		this.switchMode(CREATING_NODE_MODE);
		
		this.addMouseWheelListener(new MouseWheelListener(){
			public void mouseWheelMoved(MouseWheelEvent e){
				SceneVirtualizer.this.sm.slider.setValue(SceneVirtualizer.this.sm.slider.getValue()+e.getWheelRotation()*10);
			}
		});
	}

	public void switchMode(int mode) {
		mainMode = mode;
		if (this.getMouseListeners().length != 0){
			this.removeMouseListener(this.getMouseListeners()[0]);
			this.removeMouseMotionListener(this.getMouseMotionListeners()[0]);
		}
		if (modePanel != null){
			sm.center.remove(modePanel);
			sm.validate();
		}
		sm.b1.setBackground(BUTTON_DISABLE_COLOR);
		sm.b2.setBackground(BUTTON_DISABLE_COLOR);
		sm.b3.setBackground(BUTTON_DISABLE_COLOR);
		sm.b4.setBackground(BUTTON_DISABLE_COLOR);
		sm.b5.setBackground(BUTTON_DISABLE_COLOR);
		switch (mode) {
		case NORMAL_MODE:
//			modePanel= normalModePanel;
//			this.addMouseMotionListener(normalModeHandler);
//			this.addMouseListener(normalModeHandler);
			break;
		case HAND_MODE:
			sm.b1.setBackground(BUTTON_ENABLE_COLOR);
			modePanel= handModePanel;
			this.addMouseMotionListener(handModeHandler);
			this.addMouseListener(handModeHandler);
			break;
		case CREATING_NODE_MODE:
			sm.b2.setBackground(BUTTON_ENABLE_COLOR);
			modePanel= nodePanel;
			this.addMouseMotionListener(creatingNodeModeHandler);
			this.addMouseListener(creatingNodeModeHandler);
			break;
		case CREATING_AGENT_MODE:
			sm.b4.setBackground(BUTTON_ENABLE_COLOR);
			
			modePanel= agentPanel;
			//agentPanel.clearTarget();
			this.addMouseMotionListener(creatingAgentModeHandler);
			this.addMouseListener(creatingAgentModeHandler);
			break;
		case CREATING_APP_MODE:
			sm.b5.setBackground(BUTTON_ENABLE_COLOR);
			
			modePanel= appPanel;
			this.addMouseMotionListener(creatingAppModeHandler);
			this.addMouseListener(creatingAppModeHandler);
			break;
		case CREATING_LINK_MODE:
			sm.b3.setBackground(BUTTON_ENABLE_COLOR);
			System.out.println(sm.b5.getBackground());
			modePanel= linkPanel;
			this.addMouseMotionListener(creatingLinkModeHandler);
			this.addMouseListener(creatingLinkModeHandler);
			break;
		default:
			modePanel= normalModePanel;
//			this.addMouseMotionListener(normalModeHandler);
//			this.addMouseListener(normalModeHandler);
			System.err.println("Mode switching error!!!");
		}
		sm.center.add(modePanel,BorderLayout.NORTH);
		sm.validate();
	}

	public void paintComponent(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(BACKGROUND_COLOR);
		g.scale(scale, scale);
		g.fillRect(0, 0, 10000, 10000);
		
		g.translate(shiftX, shiftY);
		
		
		if (sm.drawGrid) drawGrid(g);
		if (sm.drawLink) drawLinks(g);
		if (sm.drawApp) drawApp(g);
		if (sm.drawAgent) drawAgents(g);
		if ((sm.sceneMode == WIRELESS_MODE)&&(sm.drawConnectivity)) drawConnected(g);
		if (sm.drawNode) drawNodes(g);
		switch(mainMode){
		case NORMAL_MODE:
			//normalModeHandler.draw(g);
			break;
		case HAND_MODE:
			//handModeHandler.draw(g);
			break;
		case CREATING_NODE_MODE:
			creatingNodeModeHandler.draw(g);
			break;
		case CREATING_AGENT_MODE:
			creatingAgentModeHandler.draw(g);
			break;
		case CREATING_APP_MODE:
			creatingAppModeHandler.draw(g);
			break;
		case CREATING_LINK_MODE:
			creatingLinkModeHandler.draw(g);
			break;
		}
		// drawInfo(g);
	}
	
	private void drawInfo(Graphics2D g) {
		g.translate(-shiftX, -shiftY);
		g.setColor(INFO_COLOR);
		g.drawString("Number of nodes: "+ dm.getNodes().length , 20,20 );
	}
	private void drawGrid(Graphics2D g) {
		g.setColor(GRID_COLOR);
		g.drawRect(0, 0, 10000, 10000);
		for (int i = 100; i < 10000; i = i + 100) {
			g.drawLine(i, 0, i, 10000);
		}
		for (int i = 100; i < 10000; i = i + 100) {
			g.drawLine(0, i, 10000, i);
		}
	}

	private void drawNodes(Graphics2D g) {
		Node p;
		int R = 30;

		int Rdiv2 = R / 2;
		Ellipse2D shap;
		Object[] nodes = dm.getNodes();
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		g.setColor(NODE_COLOR);
		for(int i = 0 ; i <nodes.length ; i++){
			p = (Node)nodes[i];
			shap = new Ellipse2D.Double(p.x - Rdiv2, p.y - Rdiv2, R, R);
			
			g.setColor(NODE_COLOR);
			g.fill(shap);
			g.setColor(NODE_TEXT_COLOR);
			if (p.id <10){
				g.drawString("n"+String.valueOf(p.id), p.x-6 , p.y+4 );
			}else if(p.id <100){
				g.drawString("n"+String.valueOf(p.id), p.x-10 , p.y+4 );
			}else{
				g.drawString("n"+String.valueOf(p.id), p.x-14 , p.y+4 );
			}
			if (sm.sceneMode == WIRELESS_MODE){
				if (sm.drawSR) {
					g.setColor(SR_COLOR);
					//transmission range = 250
					g.drawOval(p.x -transmissionRange, p.y - transmissionRange, transmissionRange*2, transmissionRange*2);
				}
				if (sm.drawIR) {
					g.setColor(IR_COLOR);
					//transmission range = 550
					g.drawOval(p.x -interferenceRange, p.y - interferenceRange, interferenceRange*2, interferenceRange*2);
				}
				if (sm.drawNodeLocation){
					g.setColor(NODE_TEXT_COLOR);
					g.drawString("("+Tool.translateX(p.x)+", "+Tool.translateY(p.y)+")", p.x-25, p.y+25);
				}
			}
		}
	}

	private void drawConnected(Graphics2D g) {
		//Line2D.Double line;
		g.setColor(CONNECTED_COLOR);
		Object[] nodes = dm.getNodes();
		Node s;
		Node d;
		for (int i=0 ; i<nodes.length ; i++){
			s = (Node)nodes[i];
			for (int j=i+1 ; j<nodes.length ; j++){
				d = (Node)nodes[j];
//				transmission range = 250
				if ((Math.abs(s.x-d.x) > transmissionRange)||(Math.abs(s.y-d.y) > transmissionRange)) continue;
				if (Tool.distance(s, d) < transmissionRange){
					g.drawLine(s.x, s.y, d.x, d.y);
				}				
			}			
		}
	}	
	
	private void drawLinks(Graphics2D g) {
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		g.setColor(LINK_COLOR);
		
		Link link;
		Line2D.Double line;
		int X1, Y1, X2, Y2;
		String type="";
		
		Object[] links = dm.getLinks();
		for(int i = 0 ; i <links.length ; i++){
			link = (Link)links[i];
			X1 = link.src.x;
			Y1 = link.src.y;
			X2 = link.dst.x;
			Y2 = link.dst.y;
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
			if (link.linkType == DUPLEX_LINK){
				type="DuplexLink";
			}else{
				type="SimplexLink ["+link.src.id +"->"+ link.dst.id+"]";
			}			
			g.drawString(type, (X1 + X2) / 2, (Y1 + Y2) / 2 + 13);
			if (sm.drawLinkDetail) {
				if (link.queueSize != -1){
					g.drawString("capacity:" + link.capacity + " propagationDelay:" + link.propagationDelay + " queueSize:" + link.queueSize + " queueType:" + link.queueType, (X1 + X2) / 2, (Y1 + Y2) / 2 + -11);
				}else{
					g.drawString("capacity:" + link.capacity + " propagationDelay:" + link.propagationDelay + " queueType:" + link.queueType, (X1 + X2) / 2, (Y1 + Y2) / 2 + -11);
				}
			}
		}
	}		
	
	private void drawAgents(Graphics2D g) {
		Agent a;
		int R = 8;

		int Rdiv2 = R / 2;
		Ellipse2D shap;
		Line2D.Double line;
		
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		int X1, Y1, X2, Y2;
		Object[] agents = dm.getAgents();
		for(int i = 0 ; i <agents.length ; i++){
			a = (Agent)agents[i];
			g.setColor(AGENT_COLOR);
			shap = new Ellipse2D.Double(a.x - Rdiv2, a.y - Rdiv2, R, R);
			g.fill(shap);
			shap = new Ellipse2D.Double(a.x - Rdiv2 -3, a.y - Rdiv2 -3, R+5, R+5);
			g.draw(shap);
			switch(a.agentType){
			case AGENT_TCP:
			case AGENT_TCP_TAHOE:
			case AGENT_TCP_RENO:
			case AGENT_TCP_NEWRENO:
			case AGENT_TCP_VEGAS:
				if (a.remoteAgent!=null){
					X1 = a.x;
					Y1 = a.y;
					X2 = a.remoteAgent.x;
					Y2 = a.remoteAgent.y;
					g.setColor(AGENT_LINK_COLOR);
					line = new Line2D.Double(X1, Y1, X2, Y2);
					g.draw(line);
				}
				g.setColor(AGENT_COLOR);
				g.drawString("tcp" + String.valueOf(a.id), a.x - R + 2, a.y - R);
				if (sm.drawAgentDetail){
					if (a.packetSize != -1){
						g.drawString(Agent.convertType(a.agentType) + " size:" + a.packetSize, a.x - R + 2, a.y - 3*R);
					}else{
						g.drawString(Agent.convertType(a.agentType) + " size:" + a.packetSize, a.x - R + 2, a.y - 3*R);						
					}
				}
				break;
			case AGENT_TCP_SINK:
				g.setColor(AGENT_COLOR);
				g.drawString("sink" + String.valueOf(a.id), a.x - R + 2, a.y - R);
				if (sm.drawAgentDetail) g.drawString(Agent.convertType(a.agentType), a.x - R + 2, a.y - 3*R);
				break;
			case AGENT_UDP:
				if (a.remoteAgent!=null){
					X1 = a.x;
					Y1 = a.y;
					X2 = a.remoteAgent.x;
					Y2 = a.remoteAgent.y;
					g.setColor(AGENT_LINK_COLOR);
					line = new Line2D.Double(X1, Y1, X2, Y2);
					g.draw(line);
				}
				g.setColor(AGENT_COLOR);
				g.drawString("udp" + String.valueOf(a.id), a.x - R + 2, a.y - R);
				if (sm.drawAgentDetail) g.drawString(Agent.convertType(a.agentType), a.x - R + 2, a.y - 3*R);
				break;
			case AGENT_NULL:
				g.setColor(AGENT_COLOR);
				g.drawString("null" + String.valueOf(a.id), a.x - R + 2, a.y - R);
				if (sm.drawAgentDetail) g.drawString(Agent.convertType(a.agentType), a.x - R + 2, a.y - 3*R);
				break;
			}
			X1 = a.x;
			Y1 = a.y;
			X2 = a.attachedNode.x;
			Y2 = a.attachedNode.y;
			g.setColor(AGENT_COLOR);
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
		}
	}	
	
	private void drawApp(Graphics2D g) {
		App a;
		int R = 8;

		int Rdiv2 = R / 2;
		Ellipse2D shap;
		Line2D.Double line;
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		//g.setColor(AGENT_COLOR);
		int X1, Y1, X2, Y2;
		String type="";
		Object[] apps = dm.getApps();
		for(int i = 0 ; i <apps.length ; i++){
			a = (App)apps[i];
			g.setColor(APP_COLOR);
			shap = new Ellipse2D.Double(a.x - Rdiv2, a.y - Rdiv2, R, R);
			g.fill(shap);
			switch(a.appType){
			case APP_FTP:
				type="ftp";
				break;
			case APP_CBR:
				type="cbr";
				break;
			case APP_PING:
				type="ping";
				break;
			}
			g.drawString(type + String.valueOf(a.id), a.x - R + 2, a.y - R);
			X1 = a.x;
			Y1 = a.y;
			X2 = a.agent.x;
			Y2 = a.agent.y;
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
		}
	}		
}
