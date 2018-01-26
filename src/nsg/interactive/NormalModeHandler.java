package nsg.interactive;


public class NormalModeHandler/* implements MouseListener, MouseMotionListener, NSGParameters*/{
//	public static final int NULL_MODE = 2;
//	public static final int NODE_MODE = 10;
//	public static final int LINK_MODE = 0;
//	public static final int AGENT_MODE = 9;
//	public static final int APP_MODE = 1;
//
//	public NSGComponent target;
//	public NSGComponent src;
//	SceneVirtualizer f;
//
//	public NormalModeHandler(SceneManager sm) {
//		this.f = f;
//	}
//
//	public void mouseClicked(MouseEvent arg0) {
//	}
//
//	public void mouseEntered(MouseEvent arg0) {
//	}
//
//	public void mouseExited(MouseEvent arg0) {
//	}
//
//	public void mousePressed(MouseEvent e) {
//		if (SwingUtilities.isLeftMouseButton(e)) {
//			if (target == null) {
//				switchMode(NormalModeHandler.NULL_MODE);
//				return;
//			} else {
//				if (target.type == NSGComponent.NODE) {
//					switchMode(NormalModeHandler.NODE_MODE);
//				} else if (target.type == NSGComponent.LINK) {
//					switchMode(NormalModeHandler.LINK_MODE);
//				} else if (target.type == NSGComponent.AGENT) {
//					switchMode(NormalModeHandler.AGENT_MODE);
//				} else if (target.type == NSGComponent.APP) {
//					switchMode(NormalModeHandler.APP_MODE);
//				}
//			}
//		}
//	}
//
//	private void switchMode(int mode) {
//			f.f.getContentPane().remove(f.modePanel);
//			
//		f.nodePanel.clearTarget();
//		f.linkPanel.clearTarget();
//		f.agentPanel.clearTarget();
//		f.appPanel.clearTarget();
//		
//		switch (mode) {
//		case NULL_MODE:
//			src = null;
//			break;
//		case NODE_MODE:
//			src = target;
//			f.nodePanel.setTarget((Node) src);
//			f.modePanel = f.nodePanel;
//			f.f.getContentPane().add(f.modePanel, BorderLayout.NORTH);
//			break;
//		case LINK_MODE:
//			src = target;
//			f.linkPanel.setTarget((Link) src);
//			f.modePanel = f.linkPanel;
//			f.f.getContentPane().add(f.modePanel, BorderLayout.NORTH);
//			break;
//		case AGENT_MODE:
//			src = target;
//			f.agentPanel.setTarget((Agent) src);
//			f.modePanel = f.agentPanel;
//			f.f.getContentPane().add(f.modePanel, BorderLayout.NORTH);
//			break;
//		case APP_MODE:
//			src = target;
//			f.appPanel.setTarget((App) src);
//			f.modePanel = f.appPanel;
//			f.f.getContentPane().add(f.modePanel, BorderLayout.NORTH);
//			break;
//		}
//		f.f.repaint();
//		f.f.validate();
//	}
//
//	public void mouseReleased(MouseEvent arg0) {
//	}
//
//	public void mouseDragged(MouseEvent arg0) {
//	}
//
//	public void mouseMoved(MouseEvent e) {
//		f.f.status.setText("Location : ( " + Tool.translateX(f.shiftX, e.getX()) + ", " + Tool.translateY(f.shiftY, e.getY()) + " )");
//		int X = e.getX() - f.shiftX;
//		int Y = e.getY() - f.shiftY;
//
//		NSGComponent p2;
//		Iterator it = f.nodes.iterator();
//		while (it.hasNext()) {
//			p2 = (NSGComponent) it.next();
//			if ((Math.abs(p2.x - X) < 10) && (Math.abs(p2.y - Y) < 10)) {
//				target = p2;
//				f.repaint();
//				return;
//			}
//		}
//
//		it = f.agents.iterator();
//		while (it.hasNext()) {
//			p2 = (NSGComponent) it.next();
//			if ((Math.abs(p2.x - X) < 10) && (Math.abs(p2.y - Y) < 10)) {
//				target = p2;
//				f.repaint();
//				return;
//			}
//		}
//
//		it = f.apps.iterator();
//		while (it.hasNext()) {
//			p2 = (NSGComponent) it.next();
//			if ((Math.abs(p2.x - X) < 10) && (Math.abs(p2.y - Y) < 10)) {
//				target = p2;
//				f.repaint();
//				return;
//			}
//		}
//
//		it = f.links.iterator();
//		boolean temp;
//		Link p;
//		int x1, x2, y1, y2;
//		double d;
//		Link tempP=null;
//		double tempD=0;
//		while (it.hasNext()) {
//			p = (Link) it.next();
//			temp = p.src.x > X ^ p.dst.x > X;
//			if (!temp) {
//				continue;
//			}
//			temp = p.src.y > Y ^ p.dst.y > Y;
//			if (!temp) {
//				continue;
//			}
//			 x1 = p.src.x;
//			 x2 = p.dst.x;
//			 y1 = p.src.y;
//			 y2 = p.dst.y;
//			 //int tempA=y2-y1;
//			 //int tempB=x1-x2;
//			 d = Math.abs((y2-y1)*X+(x1-x2)*Y+(y1*(x2-x1)+x1*(y1-y2)))/Math.sqrt((y2-y1)*(y2-y1)+(x1-x2)*(x1-x2));
//			 //d = Math.abs((1-x1)*tempA+(Y-y1)*tempB)/Math.sqrt(tempB*tempB+tempA*tempA);
//			 if (d<10){
//				 if (tempP==null){
//					 tempP=p;
//					 tempD=d;
//				 }else if (tempD>d){
//					 tempP=p;
//					 tempD=d;
//				 }
//			}
//		}
//		if (tempP!=null){
//			target = tempP;
//			f.repaint();
//			return;
//		}
//		if (target != null) {
//			target = null;
//			f.repaint();
//		}
//	}
//
//	public void draw(Graphics2D g) {
//		int R;
//		int Rdiv2;
//		Ellipse2D shap;
//		if (target != null) {
//			g.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.CAP_SQUARE));
//			g.setColor(TARGET_COLOR);
//			switch (target.type) {
//			case NSGComponent.NODE:
//				R = 30;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
//				g.draw(shap);
//				shap = new Ellipse2D.Double(target.x - Rdiv2 - 1, target.y - Rdiv2 - 1, R + 2, R + 2);
//				g.draw(shap);
//
//				break;
//			case NSGComponent.LINK:
//				Line2D.Double line;
//				int X1 = ((Link) target).src.x;
//				int Y1 = ((Link) target).src.y;
//				int X2 = ((Link) target).dst.x;
//				int Y2 = ((Link) target).dst.y;
//				line = new Line2D.Double(X1, Y1, X2, Y2);
//				g.draw(line);
//				line = new Line2D.Double(X1, Y1 - 1, X2, Y2 - 1);
//				g.draw(line);
//				line = new Line2D.Double(X1, Y1 + 1, X2, Y2 + 1);
//				g.draw(line);
//				break;
//			case NSGComponent.AGENT:
//				R = 8;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
//				g.fill(shap);
//				shap = new Ellipse2D.Double(target.x - Rdiv2 - 3, target.y - Rdiv2 - 3, R + 5, R + 5);
//				g.draw(shap);
//				break;
//			case NSGComponent.APP:
//				R = 8;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(target.x - Rdiv2, target.y - Rdiv2, R, R);
//				g.fill(shap);
//				break;
//			}
//		}
//
//		if (src != null) {
//			g.setColor(SRC_COLOR);
//			switch (src.type) {
//			case NSGComponent.NODE:
//				R = 30;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
//				g.draw(shap);
//				shap = new Ellipse2D.Double(src.x - Rdiv2 - 1, src.y - Rdiv2 - 1, R + 2, R + 2);
//				g.draw(shap);
//				break;
//			case NSGComponent.LINK:
//				Line2D.Double line;
//				int X1 = ((Link) src).src.x;
//				int Y1 = ((Link) src).src.y;
//				int X2 = ((Link) src).dst.x;
//				int Y2 = ((Link) src).dst.y;
//				line = new Line2D.Double(X1, Y1, X2, Y2);
//				g.draw(line);
//				line = new Line2D.Double(X1, Y1 - 1, X2, Y2 - 1);
//				g.draw(line);
//				line = new Line2D.Double(X1, Y1 + 1, X2, Y2 + 1);
//				g.draw(line);
//				break;
//			case NSGComponent.AGENT:
//				R = 8;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
//				g.fill(shap);
//				shap = new Ellipse2D.Double(src.x - Rdiv2 - 3, src.y - Rdiv2 - 3, R + 5, R + 5);
//				g.draw(shap);
//				break;
//			case NSGComponent.APP:
//				R = 8;
//				Rdiv2 = R / 2;
//				shap = new Ellipse2D.Double(src.x - Rdiv2, src.y - Rdiv2, R, R);
//				g.fill(shap);
//				break;
//			}
//		}
//	}

}
