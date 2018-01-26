package nsg.panels;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nsg.NSGParameters;
import nsg.SceneVirtualizer;

public class AgentPanel extends JPanel implements NSGParameters{
	static final long serialVersionUID = 0;
		
	public JComboBox agentType = new JComboBox(new String[] {"TCP","TCP/Tahoe","TCP/Reno","TCP/Newreno","TCP/Vegas","TCPSink","UDP","NULL"});
	public JTextField packetSize = new JTextField("1500", 4); //Bytes
	JLabel l1 = new JLabel("Agent type");
	JLabel l2 = new JLabel("Packet size");
	JLabel l3 = new JLabel("bytes  ");
	
	SceneVirtualizer f;
	public AgentPanel(SceneVirtualizer f){
		this.setLayout(new FlowLayout(	FlowLayout.LEFT, 0,0));
		this.f = f;
		((FlowLayout)(this.getLayout())).setAlignment(FlowLayout.LEFT);
		this.setBackground(NSGParameters.PANEL_COLOR);
		this.add(l1);
		this.add(agentType);
		this.add(l2);
		this.add(packetSize);
		this.add(l3);
	}	
}
