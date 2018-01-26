package nsg.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nsg.NSGParameters;
import nsg.component.Link;

public class LinkMenu extends JDialog implements NSGParameters{
	static final long serialVersionUID = 0;
	
	JButton done = new JButton("Done");
	Link target;
	
	public JComboBox queueType = new JComboBox(new String[] {"DropTail", "RED", "FQ", "DRR", "SFQ", "CBQ"});
	public JComboBox linkType = new JComboBox(new String[] { "duplex-link", "simplex-link" });
	public JTextField capacity = new JTextField("100"); // Mb
	public JTextField propagationDelay = new JTextField("10"); // ms
	public JTextField queueSize = new JTextField("50"); // number of packet
	
	public LinkMenu(JFrame p){
		super(p,true);
		setTitle("Link parameters setup");
		
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w/2-250,h/2-300,300,250);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
			JPanel innerPanel = new JPanel();
			innerPanel.setLayout(new GridLayout(5,2));
			innerPanel.add(new JLabel("Queue type"));innerPanel.add(queueType);
			innerPanel.add(new JLabel("Link type"));innerPanel.add(linkType);
			innerPanel.add(new JLabel("Capacity"));innerPanel.add(capacity);
			innerPanel.add(new JLabel("Propagation delay"));innerPanel.add(propagationDelay);
			innerPanel.add(new JLabel("Queue size"));innerPanel.add(queueSize);
		panel.add(innerPanel);	
		getContentPane().add(panel, BorderLayout.CENTER);
		panel = new JPanel();
		panel.add(done);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		done.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				if ((capacity.getText().equals(""))||
//					(propagationDelay.getText().equals(""))){
//							JOptionPane.showMessageDialog(LinkMenu.this, "Capacity and propagation delay are necessary");
//							return; 
//				}
				target.capacity = Float.parseFloat(capacity.getText());
				target.linkType = linkType.getSelectedIndex();
				target.propagationDelay = Integer.parseInt(propagationDelay.getText());
				target.queueSize = Integer.parseInt(queueSize.getText());
				target.queueType = queueType.getSelectedIndex();
				target = null;
				setVisible(false);
			}
		});
	}
	public void setTarget(Link target){
		this.target=target;
		queueType.setSelectedIndex(target.queueType);
		linkType.setSelectedIndex(target.linkType);
		capacity.setText(String.valueOf(target.capacity));
		propagationDelay.setText(String.valueOf(target.propagationDelay));
		queueSize.setText(String.valueOf(target.queueSize));

	}
}
