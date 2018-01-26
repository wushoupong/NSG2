package nsg.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import nsg.NSGParameters;
import nsg.component.App;

public class AppMenu extends JDialog implements NSGParameters{
	static final long serialVersionUID = 0;
	
	JButton done = new JButton("Done");
	App target;
	
	JLabel l1 = new JLabel("Application type");
	public JComboBox appType = new JComboBox(new String[] {"FTP", "CBR"/*,"PING", "EXPONENTIAL","PARETO"*/});
	JLabel l2 = new JLabel("Start time");
	public JTextField startTime = new JTextField("1",4); //sec.
	JLabel l3 = new JLabel("Stop time");
	public JTextField stopTime = new JTextField("2",4); //sec.
	JLabel l4 = new JLabel("Packet size");
	public JTextField packetSize = new JTextField("1000",4); //bytes
	JLabel l5 = new JLabel("Rate");
	public JTextField rate = new JTextField("1",4); //Mb
	JLabel l6 = new JLabel("Interval");
	public JTextField interval = new JTextField("0.005",4); //sec.
	JLabel l7 = new JLabel("Random");
	public JTextField random = new JTextField("false",4); //sec.
	
	public AppMenu(JFrame p){
		super(p,true);
		setTitle("Application parameters setup");
		
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w/2-250,h/2-300,300,350);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
			JPanel innerPanel = new JPanel();
			innerPanel.setLayout(new GridLayout(7,2));
			innerPanel.add(l1);innerPanel.add(appType);
			innerPanel.add(l2);innerPanel.add(startTime);
			innerPanel.add(l3);innerPanel.add(stopTime);
			innerPanel.add(l4);innerPanel.add(packetSize);
			innerPanel.add(l5);innerPanel.add(rate);
			//innerPanel.add(l6);innerPanel.add(interval);
			innerPanel.add(l7);innerPanel.add(random);
		panel.add(innerPanel);	
		getContentPane().add(panel, BorderLayout.CENTER);
		panel = new JPanel();
		panel.add(done);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		done.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				switch(appType.getSelectedIndex()){
				case APP_FTP: 
					target.appType=APP_FTP;
					setupFTP();
					break;
				case APP_CBR: 
					target.appType=APP_CBR;
					setupCBR();
					break;
				case APP_PING: 
					target.appType=APP_PING;
					setupPing();
					break;
//				case APP_EXPONENTIAL: 
//					target.appType=APP_EXPONENTIAL;
//					
//					break;
//				case APP_PARETO: 
//					target.appType=APP_PARETO;
//					break;
				default:
					System.out.println("App error");
					return;
				}					

				target = null;
				setVisible(false);
			}
		});
		
		appType.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				switch(appType.getSelectedIndex()){
				case APP_FTP: 
					switchToFTP();
					break;
				case APP_CBR: 
					switchToCBR();
					break;
				case APP_PING: 
					switchToPing();
					break;
//				case APP_EXPONENTIAL: 
//					break;
//				case APP_PARETO: 
//					break;
				default:
					System.out.println("addApp error");
					return;
				}
			}
		});
	}
	
	public void setTarget(App target){
		this.target=target;		
		appType.setSelectedIndex(target.appType);

		switch(appType.getSelectedIndex()){
		case APP_FTP: 
			switchToFTP();
			break;
		case APP_CBR: 
			switchToCBR();
			break;
		case APP_PING: 
			break;
		case APP_EXPONENTIAL: 
			break;
		case APP_PARETO: 
			break;
		default:
			System.out.println("addApp error");
			return;
		}
		startTime.setText(String.valueOf(target.startTime));
		stopTime.setText(String.valueOf(target.stopTime));
		packetSize.setText(String.valueOf(target.packetSize));
		rate.setText(String.valueOf(target.rate));
		random.setText(target.random);
	}	
	
	public void setupFTP(){
		target.startTime = Float.parseFloat(startTime.getText());
		target.stopTime = Float.parseFloat(stopTime.getText());
		target.packetSize = -1;
		target.rate = -1;
		target.random = "false";			
	}
	public void setupCBR(){
		target.startTime = Float.parseFloat(startTime.getText());
		target.stopTime = Float.parseFloat(stopTime.getText());
		target.packetSize = Integer.parseInt(packetSize.getText());
		target.rate = Float.parseFloat(rate.getText());
		target.random = random.getText();		
	}
	public void setupPing(){
		target.startTime = Float.parseFloat(startTime.getText());
		target.stopTime = -1;
		target.packetSize = -1;
		target.rate = -1;
		target.random = "false";			
	}
	

	
	public void switchToCBR(){
		l1.setVisible(true);
		appType.setVisible(true);
		l2.setVisible(true);
		startTime.setVisible(true);
		l3.setVisible(true);
		stopTime.setVisible(true);
		l4.setVisible(true);
		packetSize.setVisible(true);
		l5.setVisible(true);
		rate.setVisible(true);
		//l6.setVisible(true);
		//interval.setVisible(true);
		l7.setVisible(true);
		random.setVisible(true);	
	}
	
	public void switchToFTP(){
		l1.setVisible(true);
		appType.setVisible(true);
		l2.setVisible(true);
		startTime.setVisible(true);
		l3.setVisible(true);
		stopTime.setVisible(true);
		l4.setVisible(false);
		packetSize.setVisible(false);
		l5.setVisible(false);
		rate.setVisible(false);
		//l6.setVisible(true);
		//interval.setVisible(true);
		l7.setVisible(false);
		random.setVisible(false);	
	}
	
	public void switchToPing(){
		l1.setVisible(true);
		appType.setVisible(true);
		l2.setVisible(false);
		startTime.setVisible(false);
		l3.setVisible(false);
		stopTime.setVisible(false);
		l4.setVisible(false);
		packetSize.setVisible(false);
		l5.setVisible(false);
		rate.setVisible(false);
		//l6.setVisible(true);
		//interval.setVisible(true);
		l7.setVisible(false);
		random.setVisible(false);	
	}	
}
