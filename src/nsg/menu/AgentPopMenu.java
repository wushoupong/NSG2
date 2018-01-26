package nsg.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nsg.SceneManager;
import nsg.component.Agent;

public class AgentPopMenu extends JPopupMenu{
	static final long serialVersionUID = 0;
	
	Agent target;
	SceneManager sm;
	public void setTarget(Agent target){
		this.target = target;
	}
	
	public AgentPopMenu(SceneManager sm){
		this.sm = sm;
		JMenuItem item;
		item = new JMenuItem("Delete");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AgentPopMenu.this.sm.dm.removeAgent(target);
			}
		});
		add(item);
		
		item = new JMenuItem("Setup");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AgentPopMenu.this.sm.agentmenu.setTarget(target);
				AgentPopMenu.this.sm.agentmenu.setVisible(true);
			}
		});
		add(item);
	}
}