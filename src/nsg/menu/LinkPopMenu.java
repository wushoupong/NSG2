package nsg.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nsg.SceneManager;
import nsg.component.Link;

public class LinkPopMenu extends JPopupMenu{
	static final long serialVersionUID = 0;
	
	Link target;
	SceneManager sm;
	public void setTarget(Link target){
		this.target = target;
	}
	
	public LinkPopMenu(SceneManager sm){
		this.sm = sm;
		JMenuItem item;
		item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LinkPopMenu.this.sm.dm.removeLink(target);
			}
		});
		add(item);
		item = new JMenuItem("Setup");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LinkPopMenu.this.sm.linkmenu.setTarget(target);
				LinkPopMenu.this.sm.linkmenu.setVisible(true);
			}
		});
		add(item);

	}
}