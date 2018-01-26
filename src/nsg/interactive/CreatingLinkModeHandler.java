package nsg.interactive;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import nsg.DataMaintainer;
import nsg.NSGParameters;
import nsg.SceneManager;
import nsg.SceneVirtualizer;
import nsg.component.Link;
import nsg.component.Node;
import nsg.menu.LinkPopMenu;
import nsg.tool.Tool;

public class CreatingLinkModeHandler implements MouseListener, MouseMotionListener, NSGParameters{
	public Node target;
	//Link tempTargetLink;
	Link targetLink;
	public Node src;
	public Node dst;
	int linkid = 0;
	
	LinkPopMenu menu;
	
	SceneManager sm;
	SceneVirtualizer sv;
	DataMaintainer dm;

	public CreatingLinkModeHandler(SceneManager sm) {
		this.sm = sm;
		dm=sm.dm;
		sv=sm.sv;
		linkid = 0;
		menu= new LinkPopMenu(sm);
	}

	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			if (targetLink != null){
				menu.setTarget(targetLink);
				menu.show(sv, e.getX(), e.getY());
			}
				return;
		}		
		if (SwingUtilities.isLeftMouseButton(e)) {
//			if (tempTargetLink!= null){
//				targetLink=tempTargetLink;
//				sv.linkPanel.setTarget(targetLink);
//				return;
//			}else{
//				targetLink = null;
//				sv.linkPanel.clearTarget();
//			}
			if (target == null) {
				if (src != null) {
					src = null;
				}
			} else {
				if (src == null) {
					src = target;
				} else {
					if (src == target) {
						src = null;
					} else {
						addLink(src, target);
						src = null;
					}
				}
			}
			sv.repaint();
			return;
		}
	}

	public void mouseMoved(MouseEvent e) {
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		if (sm.scroll(e.getX(),e.getY())){
			return;
		}
		int X =(int) (e.getX()/sv.scale - sv.shiftX);
		int Y =(int) (e.getY()/sv.scale - sv.shiftY);
		
		target = dm.findNode(X, Y);
//		if (target != null){
//			tempTargetLink = null;
//			targetLink = null;
//			//sv.linkPanel.clearTarget();
//			sv.repaint();
//			return;
//		}
		targetLink = dm.findLink(X, Y);
		sm.repaint();
	}

	public void addLink(Node src, Node dst) {
		Link link = new Link(linkid, src, dst);
		if ((sv.linkPanel.capacity.getText().equals(""))||
		   (sv.linkPanel.propagationDelay.getText().equals(""))){
			JOptionPane.showMessageDialog(sv, "Capacity and propagation delay are necessary");
			return; 
		}
		link.capacity = Float.parseFloat(sv.linkPanel.capacity.getText());
		link.linkType = sv.linkPanel.linkType.getSelectedIndex();
		link.propagationDelay = Integer.parseInt(sv.linkPanel.propagationDelay.getText());
		if (sv.linkPanel.queueSize.getText().equals("")){
			link.queueSize = -1;
		}else{
			link.queueSize = Integer.parseInt(sv.linkPanel.queueSize.getText());
		}
		link.queueType = sv.linkPanel.queueType.getSelectedIndex();
		dm.links.add(link);
		linkid++;
	}


	
	public void draw(Graphics2D g) {
		g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
		
		int R = 30;
		int Rdiv2 = R / 2;
		Ellipse2D shap;
		if (target != null) {
			g.setColor(TARGET_COLOR);
			shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
			g.draw(shap);
			shap = new Ellipse2D.Double(target.x - Rdiv2 - 1, target.y - Rdiv2 - 1, R + 2, R + 2);
			g.draw(shap);
		}
		if (src != null) {
			g.setColor(SRC_COLOR);
			shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
			g.draw(shap);
			shap = new Ellipse2D.Double(src.x - Rdiv2 - 1, src.y - Rdiv2 - 1, R + 2, R + 2);
			g.draw(shap);
		}
//		if (tempTargetLink != null) {
//			g.setColor(TARGET_COLOR);
//			Line2D.Double line;
//			int X1 = tempTargetLink.src.x;
//			int Y1 = tempTargetLink.src.y;
//			int X2 = tempTargetLink.dst.x;
//			int Y2 = tempTargetLink.dst.y;
//			line = new Line2D.Double(X1, Y1, X2, Y2);
//			g.draw(line);
//			line = new Line2D.Double(X1, Y1 - 1, X2, Y2 - 1);
//			g.draw(line);
//			line = new Line2D.Double(X1, Y1 + 1, X2, Y2 + 1);
//			g.draw(line);
//		}
		if (targetLink != null) {
			g.setColor(SRC_COLOR);
			Line2D.Double line;
			int X1 = targetLink.src.x;
			int Y1 = targetLink.src.y;
			int X2 = targetLink.dst.x;
			int Y2 = targetLink.dst.y;
			line = new Line2D.Double(X1, Y1, X2, Y2);
			g.draw(line);
			line = new Line2D.Double(X1, Y1 - 1, X2, Y2 - 1);
			g.draw(line);
			line = new Line2D.Double(X1, Y1 + 1, X2, Y2 + 1);
			g.draw(line);
		}
	}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}
}
