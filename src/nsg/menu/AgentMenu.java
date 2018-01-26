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
import nsg.component.Agent;

public class AgentMenu extends JDialog implements NSGParameters {
	static final long serialVersionUID = 0;

	JButton done = new JButton("Done");
	Agent target;

	JLabel l1 = new JLabel("Agent type");
	public JComboBox agentType = new JComboBox(new String[] { "TCP", "TCP/Tahoe", "TCP/Reno", "TCP/Newreno", "TCP/Vegas", "TCPSink", "UDP", "NULL" });
	JLabel l2 = new JLabel("Packet size");
	public JTextField packetSize = new JTextField("1500"); // Bytes
	JLabel l3 = new JLabel("Advertised window");
	public JTextField window = new JTextField("");
	JLabel l4 = new JLabel("Congestion window maximum value");
	public JTextField maxcwnd = new JTextField("");
	JLabel l5 = new JLabel("Congestion window init. value");
	public JTextField windowInit = new JTextField("");
	JLabel l6 = new JLabel("Timeout");
	public JTextField tcpTick = new JTextField("");
	JLabel l7 = new JLabel("Maximum burst");
	public JTextField maxburst = new JTextField(""); 

	public AgentMenu(JFrame p) {
		super(p, true);
		setTitle("Agent parameters setup");

		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w / 2 - 250, h / 2 - 300, 500, 400);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(7, 2));
			innerPanel.add(l1);	innerPanel.add(agentType);
			innerPanel.add(l2);	innerPanel.add(packetSize);
			innerPanel.add(l3);	innerPanel.add(window);
			innerPanel.add(l4);	innerPanel.add(maxcwnd);
			innerPanel.add(l5);	innerPanel.add(windowInit);
			innerPanel.add(l6);	innerPanel.add(tcpTick);
			innerPanel.add(l7);	innerPanel.add(maxburst);
		panel.add(innerPanel);
		getContentPane().add(panel, BorderLayout.CENTER);
		panel = new JPanel();
		panel.add(done);
		getContentPane().add(panel, BorderLayout.SOUTH);

		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (agentType.getSelectedIndex()) {
				case 0:
					target.agentType = AGENT_TCP;
					setupTCP();
					break;
				case 1:
					target.agentType = AGENT_TCP_TAHOE;
					setupTCP();
					break;
				case 2:
					target.agentType = AGENT_TCP_RENO;
					setupTCP();
					break;
				case 3:
					target.agentType = AGENT_TCP_NEWRENO;
					setupTCP();
					break;
				case 4:
					target.agentType = AGENT_TCP_VEGAS;
					setupTCP();
					break;
				case 5:
					target.agentType = AGENT_TCP_SINK;
					setupSink();
					break;
				case 6:
					target.agentType = AGENT_UDP;
					setupUDP();					
					break;
				case 7:
					target.agentType = AGENT_NULL;
					setupNull();					
					break;
				default:
					System.err.println("Agent error");
				}
				target = null;
				setVisible(false);
			}
		});
		
		agentType.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				switch(agentType.getSelectedIndex()){
				case 0: 
				case 1: 
				case 2: 
				case 3: 
				case 4: 
					switchToTCP();
					break;
				case 5: 
					switchToSink();
					break;
				case 6: 
					switchToUDP();
					break;
				case 7: 
					switchToNull();
					break;
				default:
					System.out.println("app type error");
					return;
				}
			}
		});
	}
	
	public void setTarget(Agent target) {
		this.target = target;
		packetSize.setEditable(true);
		switch (target.agentType) {
		case AGENT_TCP:
			agentType.setSelectedIndex(0);
			break;
		case AGENT_TCP_TAHOE:
			agentType.setSelectedIndex(1);
			break;
		case AGENT_TCP_RENO:
			agentType.setSelectedIndex(2);
			break;
		case AGENT_TCP_NEWRENO:
			agentType.setSelectedIndex(3);
			break;
		case AGENT_TCP_VEGAS:
			agentType.setSelectedIndex(4);
			break;
		case AGENT_TCP_SINK:
			agentType.setSelectedIndex(5);
			break;
		case AGENT_UDP:
			agentType.setSelectedIndex(6);
			break;
		case AGENT_NULL:
			agentType.setSelectedIndex(7);
			break;
		default:
			System.err.println("addAgent error");
		}
		packetSize.setText(String.valueOf(target.packetSize));
		window.setText(String.valueOf(target.window));
		maxcwnd.setText(String.valueOf(target.maxcwnd));
		windowInit.setText(String.valueOf(target.windowInit));
		tcpTick.setText(String.valueOf(target.tcpTick));
		maxburst.setText(String.valueOf(target.maxburst));
	}


	public void setupTCP(){
		target.packetSize=Integer.parseInt(packetSize.getText());
		target.window=Integer.parseInt(window.getText());
		target.maxcwnd=Integer.parseInt(maxcwnd.getText());
		target.windowInit=Integer.parseInt(windowInit.getText());
		target.tcpTick=Integer.parseInt(tcpTick.getText());
		target.maxburst=Integer.parseInt(maxburst.getText());
	}
	public void setupSink(){
		target.packetSize=-1;
		target.window=-1;
		target.maxcwnd=-1;
		target.windowInit=-1;
		target.tcpTick=-1;
		target.maxburst=-1;		
	}
	public void setupUDP(){
		target.packetSize=Integer.parseInt(packetSize.getText());
		target.window=-1;
		target.maxcwnd=-1;
		target.windowInit=-1;
		target.tcpTick=-1;
		target.maxburst=-1;		
	}
	public void setupNull(){
		target.packetSize=-1;
		target.window=-1;
		target.maxcwnd=-1;
		target.windowInit=-1;
		target.tcpTick=-1;
		target.maxburst=-1;				
	}
	
	public void switchToTCP(){
		l1.setVisible(true);
		agentType.setVisible(true);
		l2.setVisible(true);
		packetSize.setVisible(true);
		l3.setVisible(true);
		window.setVisible(true);
		l4.setVisible(true);
		maxcwnd.setVisible(true);
		l5.setVisible(true);
		windowInit.setVisible(true);
		l6.setVisible(true);
		tcpTick.setVisible(true);
		l7.setVisible(true);
		maxburst.setVisible(true);		
	}

	public void switchToUDP(){
		l1.setVisible(true);
		agentType.setVisible(true);
		l2.setVisible(true);
		packetSize.setVisible(true);
		l3.setVisible(false);
		window.setVisible(false);
		l4.setVisible(false);
		maxcwnd.setVisible(false);
		l5.setVisible(false);
		windowInit.setVisible(false);
		l6.setVisible(false);
		tcpTick.setVisible(false);
		l7.setVisible(false);
		maxburst.setVisible(false);			
	}

	public void switchToNull(){
		l1.setVisible(true);
		agentType.setVisible(true);
		l2.setVisible(false);
		packetSize.setVisible(false);
		l3.setVisible(false);
		window.setVisible(false);
		l4.setVisible(false);
		maxcwnd.setVisible(false);
		l5.setVisible(false);
		windowInit.setVisible(false);
		l6.setVisible(false);
		tcpTick.setVisible(false);
		l7.setVisible(false);
		maxburst.setVisible(false);		
	}

	public void switchToSink(){
		l1.setVisible(true);
		agentType.setVisible(true);
		l2.setVisible(false);
		packetSize.setVisible(false);
		l3.setVisible(false);
		window.setVisible(false);
		l4.setVisible(false);
		maxcwnd.setVisible(false);
		l5.setVisible(false);
		windowInit.setVisible(false);
		l6.setVisible(false);
		tcpTick.setVisible(false);
		l7.setVisible(false);
		maxburst.setVisible(false);	
	}
}
