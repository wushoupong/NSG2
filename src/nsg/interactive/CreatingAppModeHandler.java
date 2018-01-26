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
import nsg.component.App;
import nsg.menu.AppPopMenu;
import nsg.tool.Tool;

public class CreatingAppModeHandler  implements MouseListener, MouseMotionListener, NSGParameters {
	public Agent target;
	public App appTarget;
	public Agent src;
	int mouseX;
	int mouseY;
	int appid = 0;
	
	AppPopMenu menu;
	SceneManager sm;
	SceneVirtualizer sv;
	DataMaintainer dm;

	
	public CreatingAppModeHandler(SceneManager sm) {
		this.sm = sm;
		sv = sm.sv;
		dm = sm.dm;
		 appid = 0;
		 menu= new AppPopMenu(sm);
	}
	public void mouseDragged(MouseEvent arg0) {
	}
	public void mouseClicked(MouseEvent arg0) {
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}	
	public void mouseMoved(MouseEvent e) {
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		if (sm.scroll(e.getX(),e.getY())){
			return;
		}
		// System.out.println("mouseMoved");
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		mouseX = X;
		mouseY = Y;
		
		target = dm.findAgent(X, Y);
		if ((target != null) && (target.agentType % 2 == 1)){
			target = null;
		}
		if (target != null){
			Object[] apps = dm.getApps();
			App app;
			for (int j=0 ; j<apps.length ; j++){
				app = (App)apps[j];
				if (app.agent == target){
					target = null;
					break;
				}
			}
		}
		if (target == null){
			appTarget = dm.findApp(X, Y);
		}
		sm.repaint();	
	}
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (appTarget != null){
				menu.setTarget(appTarget);
				menu.show(sv, e.getX(), e.getY());
			}
				return;
		}
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		if (SwingUtilities.isLeftMouseButton(e)) {
			if (target != null){
				src=target;
			}else if (src != null){
				addApp(X, Y, src);
				src=null;
			}
			sv.repaint();
		}
	}

	public void addApp(int x, int y, Agent agent) {
		App app = new App(appid, x, y, agent);
		switch(sv.appPanel.appType.getSelectedIndex()){
		case APP_FTP: 
			app.appType=APP_FTP;
			app.startTime = Float.parseFloat(sv.appPanel.startTime.getText());
			app.stopTime = Float.parseFloat(sv.appPanel.stopTime.getText());
			app.packetSize = Integer.parseInt(sv.appPanel.packetSize.getText());
			//none
			break;
		case APP_CBR: 
			app.appType=APP_CBR;
			app.startTime = Float.parseFloat(sv.appPanel.startTime.getText());
			app.stopTime = Float.parseFloat(sv.appPanel.stopTime.getText());
			app.packetSize = Integer.parseInt(sv.appPanel.packetSize.getText());
			app.rate = Float.parseFloat(sv.appPanel.rate.getText());
//			app.random = (String)sv.appPanel.random.getSelectedItem();
			break;
		case APP_PING: 
			app.appType=APP_PING;
			app.startTime = Float.parseFloat(sv.appPanel.startTime.getText());
			break;
		case APP_EXPONENTIAL: 
			app.appType=APP_EXPONENTIAL;			
			break;
		case APP_PARETO: 
			app.appType=APP_PARETO;			
			break;
		default:
			System.out.println("addApp error");
			return;
		}	
		dm.apps.add(app);
		appid++;
	}
	
	
	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		int R = 8;
		int Rdiv2 = R / 2;
		Ellipse2D shap;
		g.setColor(TARGET_COLOR);
		if (target != null) {
			shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
			g.fill(shap);
			shap = new Ellipse2D.Double(target.x - Rdiv2 -3, target.y - Rdiv2 -3, R+5, R+5);
			g.draw(shap);
		}
		if (appTarget != null) {
			//g.setColor(APP_COLOR);
			shap = new Ellipse2D.Double(appTarget.x - Rdiv2, appTarget.y - Rdiv2, R, R);
			g.fill(shap);
		}
		g.setColor(SRC_COLOR);
		if (src != null) {
			shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
			g.fill(shap);
			shap = new Ellipse2D.Double(src.x - Rdiv2 -3, src.y - Rdiv2 -3, R+5, R+5);
			g.draw(shap);
			Line2D.Double line;
			int X1 = src.x;
			int Y1 = src.y;
			int X2 = mouseX;
			int Y2 = mouseY;
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
		}
		
		
		
	}
}
