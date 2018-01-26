package nsg.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import nsg.NSGParameters;

public class NormalModePanel extends JPanel{
	static final long serialVersionUID = 0;
	
	public NormalModePanel(){
		((FlowLayout)(this.getLayout())).setAlignment(FlowLayout.LEFT);
		this.setBackground(NSGParameters.PANEL_COLOR);
	}
}
