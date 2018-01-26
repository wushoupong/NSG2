package nsg.interactive;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import nsg.SceneManager;
import nsg.SceneVirtualizer;
import nsg.tool.Tool;

public class HandModeHandler implements MouseListener, MouseMotionListener {

	SceneVirtualizer sv;
	SceneManager sm;
	
	int referenceX;
	int referenceY;

	public HandModeHandler(SceneManager sm) {
		this.sm = sm;
		sv = sm.sv;
	}

	public void mouseDragged(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			// System.out.println("mousePressed+LeftMouseButton");
			sv.shiftX = sv.shiftX + (int)((e.getX() - referenceX)/sv.scale);
			sv.shiftY = sv.shiftY + (int)((e.getY() - referenceY)/sv.scale);
			referenceX = e.getX();
			referenceY = e.getY();
			sm.centerX =sv.getWidth()/2/sv.scale - sv.shiftX;
			sm.centerY =sv.getHeight()/2/sv.scale - sv.shiftY;
			sm.repaint();
			return;
		}
	}

	public void mouseMoved(MouseEvent e) {
		sm.status.setText("Location : ( " + Tool.translateX(sv.shiftX, e.getX() ,sv.scale) + ", " + Tool.translateY(sv.shiftY, e.getY(),sv.scale) + " )");
		if (sm.scroll(e.getX(),e.getY())){
			return;
		}
		sm.repaint();
	}
	public void mouseClicked(MouseEvent arg0) {
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) {
			// System.out.println("mousePressed+LeftMouseButton");
			referenceX = e.getX();
			referenceY = e.getY();
			return;
		}

	}

	public void mouseReleased(MouseEvent arg0) {
	}

}
