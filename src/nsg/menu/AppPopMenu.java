package nsg.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nsg.SceneManager;
import nsg.component.App;

public class AppPopMenu extends JPopupMenu{
	static final long serialVersionUID = 0;
	
	App target;
	SceneManager sm;
	public void setTarget(App target){
		this.target = target;
	}
	
	public AppPopMenu(SceneManager sm){
		this.sm = sm;
		JMenuItem item;
		item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppPopMenu.this.sm.dm.removeApp(target);
			}
		});
		add(item);
		item = new JMenuItem("Setup");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AppPopMenu.this.sm.appmenu.setTarget(target);
				AppPopMenu.this.sm.appmenu.setVisible(true);
			}
		});
		add(item);
	}
}
