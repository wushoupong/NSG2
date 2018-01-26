package nsg.panels;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import nsg.NSGParameters;

public class HandModePanel extends JPanel{
	static final long serialVersionUID = 0;

	public HandModePanel(){
		this.setLayout(new FlowLayout(	FlowLayout.LEFT, 0,0));
		((FlowLayout)(this.getLayout())).setAlignment(FlowLayout.LEFT);
		this.setBackground(NSGParameters.PANEL_COLOR);
		//this.add(new JLabel("Hand mode"));

	}
}
