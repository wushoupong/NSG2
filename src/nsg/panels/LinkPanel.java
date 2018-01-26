package nsg.panels;

import java.awt.FlowLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nsg.NSGParameters;
import nsg.SceneVirtualizer;
import nsg.component.Link;

public class LinkPanel extends JPanel {
	static final long serialVersionUID = 0;

	public JComboBox queueType = new JComboBox(new String[] {"DropTail", "RED", "FQ", "DRR", "SFQ", "CBQ"});
	public JComboBox linkType = new JComboBox(new String[] { "duplex-link", "simplex-link" });
	public JTextField capacity = new JTextField("100"); // Mb
	public JTextField propagationDelay = new JTextField("10"); // ms
	public JTextField queueSize = new JTextField("50"); // number of packet

	Link link;
	SceneVirtualizer sv;

	public LinkPanel(SceneVirtualizer sv) {
		this.setLayout(new FlowLayout(	FlowLayout.LEFT, 0,0));
		this.sv = sv;
		capacity.setColumns(4);
		propagationDelay.setColumns(4);
		queueSize.setColumns(4);

		((FlowLayout) (getLayout())).setAlignment(FlowLayout.LEFT);
		setBackground(NSGParameters.PANEL_COLOR);
		add(new JLabel("Lnik type"));
		add(linkType);
		add(new JLabel("   Queue type"));
		add(queueType);
		add(new JLabel("   Capacity"));
		add(capacity);
		add(new JLabel("Mbps"));
		add(new JLabel("   Propagation Delay"));
		add(propagationDelay);
		add(new JLabel("ms"));
		add(new JLabel("   Queue Size"));
		add(queueSize);
	}
}
