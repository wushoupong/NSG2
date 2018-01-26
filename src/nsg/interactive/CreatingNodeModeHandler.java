package nsg.interactive;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.SwingUtilities;

import nsg.DataMaintainer;
import nsg.NSGParameters;
import nsg.SceneManager;
import nsg.SceneVirtualizer;
import nsg.component.Node;
import nsg.component.WiredNode;
import nsg.menu.NodePopMenu;
import nsg.tool.Tool;

public class CreatingNodeModeHandler implements MouseListener, MouseMotionListener, NSGParameters{

	NodePopMenu menu;
	int nodeid = 0;
	SceneManager sm;
	SceneVirtualizer sv;
	DataMaintainer dm;
	
	Node target;
	public CreatingNodeModeHandler(SceneManager sm) {
		this.sm = sm;
		dm=sm.dm;
		sv=sm.sv;
		nodeid = 0;
		menu= new NodePopMenu(sm);
	}

	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) return;
		if (target == null) return;
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		target.x = X;
		target.y = Y;
		sm.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		//sm.status.setText("test");
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		if (sm.scroll(e.getX(),e.getY())){
			return;
		}
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		target = dm.findNode(X, Y);
		sm.repaint();	
	}

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (target != null){
				menu.setTarget(target);
				menu.show(sv, e.getX(), e.getY());
			}
				return;
		}
		if (SwingUtilities.isLeftMouseButton(e)) {
			//��ܥ��b�즲��
			if (target != null) return;
		
//			 ����Node
			int X =(int) (e.getX()/sv.scale - sv.shiftX);
			int Y =(int) (e.getY()/sv.scale - sv.shiftY);
			switch (sv.nodePanel.type.getSelectedIndex()) {
			case 0:// NORMAL
				addNode(X, Y);
				break;
			case 1:// H_CHAIN
				for (int i = 0; i < Integer.parseInt(sv.nodePanel.vNodes.getText()); i++) {
					addNode(X, Y);
					Y = Y + Integer.parseInt(sv.nodePanel.vDistance.getText());
				}
				break;
			case 2:// V_CHAIN
				for (int i = 0; i < Integer.parseInt(sv.nodePanel.hNodes.getText()); i++) {
					addNode(X, Y);
					X = X + Integer.parseInt(sv.nodePanel.hDistance.getText());
				}
				break;
			case 3:// GRID
				int tmp = X;
				for (int i = 0; i < Integer.parseInt(sv.nodePanel.vNodes.getText()); i++) {
					X = tmp;
					for (int j = 0; j < Integer.parseInt(sv.nodePanel.hNodes.getText()); j++) {
						addNode(X, Y);
						X = X + Integer.parseInt(sv.nodePanel.vDistance.getText());
					}
					Y = Y + Integer.parseInt(sv.nodePanel.hDistance.getText());
				}
				break;
			case 4:// RANDOM
				for (int i = 0; i < Integer.parseInt(sv.nodePanel.vNodes.getText()); i++) {
					int randX = (int) (Math.random() * Integer.parseInt(sv.nodePanel.xRange.getText()));
					int randY = (int) (Math.random() * Integer.parseInt(sv.nodePanel.yRange.getText()));
					addNode(X+randX, Y+randY);
				}
				break;
			}
			sv.repaint();
			return;
		}
	}
	public void draw(Graphics2D g) {
		if (target != null) {
			g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
			Ellipse2D shap;
			int R = 30;
			int Rdiv2 = R / 2;
			g.setColor(TARGET_COLOR);
			shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
			g.draw(shap);
			shap = new Ellipse2D.Double(target.x - Rdiv2-1, target.y - Rdiv2-1, R+2, R+2);
			g.draw(shap);
		}		
	}
	
	public void addNode(int x, int y) {
		dm.nodes.add(new WiredNode(nodeid, x, y));
		nodeid++;
	}
	

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}	
	
	public void mouseReleased(MouseEvent e) {}	
}
