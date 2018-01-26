package nsg.interactive;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.SwingUtilities;

import nsg.DataMaintainer;
import nsg.NSGParameters;
import nsg.SceneManager;
import nsg.SceneVirtualizer;
import nsg.component.Agent;
import nsg.component.NSGComponent;
import nsg.component.Node;
import nsg.menu.AgentPopMenu;
import nsg.tool.Tool;

public class CreatingAgentModeHandler implements MouseListener, MouseMotionListener, NSGParameters{
	public NSGComponent target;
	public Node src;
	public Agent agentSrc;
	int agentid;
	int mouseX;
	int mouseY;

	SceneManager sm;
//	ParameterDialog pm;
	SceneVirtualizer sv;
	DataMaintainer dm;
	AgentPopMenu menu;

	public CreatingAgentModeHandler(SceneManager sm) {
		this.sm = sm;
		sv = sm.sv;
		//pm = sm.parameters;
		dm = sm.dm;
		menu= new AgentPopMenu(sm);
	}

	public void mouseMoved(MouseEvent e) {
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		if (sm.scroll(e.getX(),e.getY())){
			return;
		}
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		mouseX = X;
		mouseY = Y;
	
		target = dm.findNode(X, Y);
		if (target != null){
			sv.repaint();
			return;
		}
		target = dm.findAgent(X, Y);

		sm.repaint();
	}

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if ((target != null)&&(target.type == NSGComponent.AGENT)){
				menu.setTarget((Agent)target);
				menu.show(sv, e.getX(), e.getY());
			}
				return;
		}		
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (target == null) {
				if (src != null) {
					addAgent(src, X, Y);
					src = null;
					sv.repaint();
				}
				if (agentSrc != null){
					agentSrc = null;
					sv.repaint();
				}
			} else {
				if (target.type == NSGComponent.NODE){
					src = (Node)target;
					agentSrc = null;
				}else{
					if(agentSrc == null){
						src=null;
						agentSrc = (Agent)target;
					}else{
						if ((agentSrc.agentType + ((Agent)target).agentType) % 4 == 1) {
							if (agentSrc.remoteAgent != null){
								agentSrc.remoteAgent.remoteAgent = null;
							}
							if (((Agent)target).remoteAgent != null){
								((Agent)target).remoteAgent.remoteAgent = null;
							}
							((Agent)target).remoteAgent = agentSrc;
							agentSrc.remoteAgent=(Agent)target;
							agentSrc = null;
						}else{
							src=null;
							agentSrc = (Agent)target;
						}
					}
				}
				sv.repaint();
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		
		int R = 8;
		int Rdiv2 = R / 2;
		Ellipse2D shap;

		if (target != null) {
			g.setColor(TARGET_COLOR);
			if (target.type == NSGComponent.NODE){
				R = 30;
				Rdiv2 = R / 2;
				shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
				g.draw(shap);
				shap = new Ellipse2D.Double(target.x - Rdiv2-1, target.y - Rdiv2-1, R+2, R+2);
				g.draw(shap);
			}else{
				R = 8;
				Rdiv2 = R / 2;
				shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
				g.fill(shap);
				shap = new Ellipse2D.Double(target.x - Rdiv2 -3, target.y - Rdiv2 -3, R+5, R+5);
				g.draw(shap);
			}
		}
		if (src != null) {
			g.setColor(SRC_COLOR);
			R = 30;
			Rdiv2 = R / 2;
			shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
			g.draw(shap);
			shap = new Ellipse2D.Double(src.x - Rdiv2-1, src.y - Rdiv2-1, R+2, R+2);
			g.draw(shap);
			Line2D.Double line;
			int X1 = src.x;
			int Y1 = src.y;
			int X2 = mouseX;
			int Y2 = mouseY;
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
		}
		if (agentSrc != null) {
			R = 8;
			Rdiv2 = R / 2;
			g.setColor(SRC_COLOR);	
			shap = new Ellipse2D.Double(agentSrc.x - Rdiv2, agentSrc.y - Rdiv2, R, R);
			g.fill(shap);
			shap = new Ellipse2D.Double(agentSrc.x - Rdiv2 -3, agentSrc.y - Rdiv2 -3, R+5, R+5);
			g.draw(shap);
		}
	}
	
	public void addAgent(Node node, int x, int y) {
		Agent agent = new Agent(agentid, x, y);	
		switch(sv.agentPanel.agentType.getSelectedIndex()){
		case 0://AGENT_TCP = 1
			agent.agentType=AGENT_TCP;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 1://AGENT_TCP_TAHOE = 3;
			agent.agentType=AGENT_TCP_TAHOE;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 2://AGENT_TCP_RENO = 5;	
			agent.agentType=AGENT_TCP_RENO;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 3://AGENT_TCP_NEWRENO = 7;
			agent.agentType=AGENT_TCP_NEWRENO;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 4://AGENT_TCP_VEGAS = 9;
			agent.agentType=AGENT_TCP_VEGAS;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 5://AGENT_TCP_SINK = 2;
			agent.agentType=AGENT_TCP_SINK;
			break;
		case 6://AGENT_UDP = 13;	
			agent.agentType=AGENT_UDP;
			if (sv.agentPanel.packetSize.getText().equals("")){
				agent.packetSize = -1;
			}else{
				agent.packetSize = Integer.parseInt(sv.agentPanel.packetSize.getText());
			}
			break;
		case 7://AGENT_NULL = 10;	
			agent.agentType=AGENT_NULL;
			break;	
		default:
			System.err.println("addAgent error");
		}

		agent.attachedNode = node;
		dm.agents.add(agent);
		agentid++;
	}

	public void mouseDragged(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}	
}
