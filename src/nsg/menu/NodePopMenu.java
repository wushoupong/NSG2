package nsg.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nsg.NSGParameters;
import nsg.SceneManager;
import nsg.component.Node;

public class NodePopMenu extends JPopupMenu implements NSGParameters{
	static final long serialVersionUID = 0;
	
	Node target;
	SceneManager sm;
	
	public void setTarget(Node target){
		this.target = target;
	}
	
	public NodePopMenu(SceneManager sm){
		this.sm = sm;
		JMenuItem item;
		item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NodePopMenu.this.sm.dm.removeNode(target);
			}
		});
		add(item);
		if (NodePopMenu.this.sm.sceneMode == WIRELESS_MODE) {
			item = new JMenuItem("Waypoint");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NodePopMenu.this.sm.waypointMenu.setTarget(target);
					NodePopMenu.this.sm.waypointMenu.setVisible(true);
				}
			});
			add(item);
		}

	}
}
