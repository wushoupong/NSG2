package nsg.panels;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nsg.NSGParameters;
import nsg.SceneVirtualizer;

public class AppPanel extends JPanel implements NSGParameters {
	static final long serialVersionUID = 0;
	
	//	TODO
	JLabel l1 = new JLabel("Aplication type");
	public JComboBox appType = new JComboBox(new String[] {"FTP", "CBR"/*,"PING", "EXPONENTIAL","PARETO"*/});
	JLabel l2 = new JLabel("   Start time");
	public JTextField startTime = new JTextField("1",4); //sec.
	JLabel l3 = new JLabel("   Stop time");
	public JTextField stopTime = new JTextField("2",4); //sec.
	
	//CBR
	JLabel l4 = new JLabel("   Packet size");
	public JTextField packetSize = new JTextField("1000",4); //bytes
	JLabel l5 = new JLabel("   Rate");
	public JTextField rate = new JTextField("1",4); //Mb
	JLabel l6 = new JLabel("   Interval");
	public JTextField interval = new JTextField("0.005",4); //sec.
//	JLabel l7 = new JLabel("   Noise");
//	public JComboBox random = new JComboBox(new String[] {"true", "false"}); //sec.
	
	//EXPONENTIAL
	JLabel l8 = new JLabel("   Burst time");
	public JTextField burstTime = new JTextField("200",4); //ms
	JLabel l9 = new JLabel("   Idle time");
	public JTextField idleTime = new JTextField("400",4); //ms
	JLabel l10 = new JLabel("   Shape");
	public JTextField shape = new JTextField("1.5",4); //sec.
	
	SceneVirtualizer sv;

	public AppPanel(SceneVirtualizer f){
		this.setLayout(new FlowLayout(	FlowLayout.LEFT, 0,0));
		this.sv = f;
		((FlowLayout)(this.getLayout())).setAlignment(FlowLayout.LEFT);
		this.setBackground(NSGParameters.PANEL_COLOR);
		this.add(l1);
		this.add(appType);
		this.add(l2);
		this.add(startTime);
		this.add(l3);
		this.add(stopTime);
		this.add(l4);
		this.add(packetSize);
		this.add(l5);
		this.add(rate);
		this.add(l6);
		this.add(interval);
//		this.add(l7);
//		this.add(random);
		this.add(l8);
		this.add(burstTime);
		this.add(l9);
		this.add(idleTime);
		this.add(l10);
		this.add(shape);
		l4.setVisible(false);
		packetSize.setVisible(false);
		l5.setVisible(false);
		rate.setVisible(false);
		l6.setVisible(false);
		interval.setVisible(false);
//		l7.setVisible(false);
//		random.setVisible(false);
		l8.setVisible(false);
		burstTime.setVisible(false);
		l9.setVisible(false);
		idleTime.setVisible(false);
		l10.setVisible(false);
		shape.setVisible(false);		
		appType.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				l3.setVisible(false);
				stopTime.setVisible(false);
				l4.setVisible(false);
				packetSize.setVisible(false);
				l5.setVisible(false);
				rate.setVisible(false);
				l6.setVisible(false);
				interval.setVisible(false);
//				l7.setVisible(false);
//				random.setVisible(false);
				l8.setVisible(false);
				burstTime.setVisible(false);
				l9.setVisible(false);
				idleTime.setVisible(false);
				l10.setVisible(false);
				shape.setVisible(false);
				switch(appType.getSelectedIndex()){
				case APP_FTP: 
					l3.setVisible(true);
					stopTime.setVisible(true);
					//none
					break;
				case APP_CBR: 
					l3.setVisible(true);
					stopTime.setVisible(true);
					l4.setVisible(true);
					packetSize.setVisible(true);
					l5.setVisible(true);
					rate.setVisible(true);
					l6.setVisible(true);
					interval.setVisible(true);
//					l7.setVisible(true);
//					random.setVisible(true);
					break;
				case APP_PING: 
					
					break;
				case APP_EXPONENTIAL: 
					l3.setVisible(true);
					stopTime.setVisible(true);
					l8.setVisible(true);
					burstTime.setVisible(true);
					l9.setVisible(true);
					idleTime.setVisible(true);
					l10.setVisible(true);
					shape.setVisible(true);
					break;
				case APP_PARETO: 
					l3.setVisible(true);
					stopTime.setVisible(true);
					break;
				default:
					System.out.println("AppPanel switch error");
					return;
				}					
			}
		});
	}
}
