package nsg;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ParameterDialog extends JDialog implements NSGParameters {
	static final long serialVersionUID = 0;

	private void saveDefault() {
		String userHome = System.getProperty("user.home");
		File f = new File(userHome + "/nsg/");
		if (!f.exists())
			f.mkdirs();

		try {
			Writer out = new BufferedWriter(new FileWriter(userHome
					+ "/nsg/wirelessDefault.txt"));
			// Simulation Parameter
			out.write(simTimeItem.getText() + " ");
			out.write(traceFileItem.getText() + " ");
			out.write(namFileItem.getText() + " ");

			// Wireless parameters
			out.write(channelItem.getSelectedIndex() + " ");
			out.write(propagationItem.getSelectedIndex() + " ");
			out.write(phyItem.getSelectedIndex() + " ");
			out.write(macItem.getSelectedIndex() + " ");
			out.write(queueItem.getSelectedIndex() + " ");
			out.write(linkLayerItem.getSelectedIndex() + " ");
			out.write(antennaItem.getSelectedIndex() + " ");
			out.write(maxPacketInIfqItem.getText() + " ");
			out.write(routingItem.getSelectedIndex() + " ");
			out.write(agentTraceItem.getSelectedIndex() + " ");
			out.write(routerTraceItem.getSelectedIndex() + " ");
			out.write(channelItem.getSelectedIndex() + " ");
			out.write(macTraceItem.getSelectedIndex() + " ");
			out.write(movementTraceItem.getSelectedIndex() + " ");

			// Channel parameters
			out.write(Gt_Box.isSelected() + " ");
			out.write(Gr_Box.isSelected() + " ");
			out.write(L_Box.isSelected() + " ");
			out.write(freq_Box.isSelected() + " ");
			out.write(bandwidth_Box.isSelected() + " ");
			out.write(Pt_Box.isSelected() + " ");
			out.write(CPThresh_Box.isSelected() + " ");
			out.write(CSThresh_Box.isSelected() + " ");
			out.write(RXThresh_Box.isSelected() + " ");
			out.write(dataRate_Box.isSelected() + " ");
			out.write(basicRate_Box.isSelected() + " ");

			out.write(Gt_.getText() + " ");
			out.write(Gr_.getText() + " ");
			out.write(L_.getText() + " ");
			out.write(freq_.getText() + " ");
			out.write(bandwidth_.getText() + " ");
			out.write(Pt_.getText() + " ");
			out.write(CPThresh_.getText() + " ");
			out.write(CSThresh_.getText() + " ");
			out.write(RXThresh_.getText() + " ");
			out.write(dataRate_.getText() + " ");
			out.write(basicRate_.getText() + " ");

			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadDefault() {
		String userHome = System.getProperty("user.home");
		File f = new File(userHome + "/nsg/wirelessDefault.txt");
		if (!f.exists())
			return;

		try {
			BufferedReader in = new BufferedReader(new FileReader(userHome
					+ "/nsg/wirelessDefault.txt"));
			String[] result = in.readLine().split(" ");
			simTimeItem.setText(result[0]);
			traceFileItem.setText(result[1]);
			namFileItem.setText(result[2]);

			channelItem.setSelectedIndex(Integer.valueOf(result[3]));
				propagationItem.setSelectedIndex(Integer.valueOf(result[4]));
				phyItem.setSelectedIndex(Integer.valueOf(result[5]));
				macItem.setSelectedIndex(Integer.valueOf(result[6]));
				queueItem.setSelectedIndex(Integer.valueOf(result[7]));
				linkLayerItem.setSelectedIndex(Integer.valueOf(result[8]));
				antennaItem.setSelectedIndex(Integer.valueOf(result[9]));
				maxPacketInIfqItem.setText(result[10]);
				routingItem.setSelectedIndex(Integer.valueOf(result[11]));
				agentTraceItem.setSelectedIndex(Integer.valueOf(result[12]));
				routerTraceItem.setSelectedIndex(Integer.valueOf(result[13]));
				channelItem.setSelectedIndex(Integer.valueOf(result[14]));
				macTraceItem.setSelectedIndex(Integer.valueOf(result[15]));
				movementTraceItem.setSelectedIndex(Integer.valueOf(result[16]));
	
				// Channel parameters
				if (Boolean.valueOf(result[17]))
					Gt_Box.setSelected(true);
				if (Boolean.valueOf(result[18]))
					Gr_Box.setSelected(true);
				if (Boolean.valueOf(result[19]))
					L_Box.setSelected(true);
				if (Boolean.valueOf(result[20]))
					freq_Box.setSelected(true);
				if (Boolean.valueOf(result[21]))
					bandwidth_Box.setSelected(true);
				if (Boolean.valueOf(result[22]))
					Pt_Box.setSelected(true);
				if (Boolean.valueOf(result[23]))
					CPThresh_Box.setSelected(true);
				if (Boolean.valueOf(result[24]))
					CSThresh_Box.setSelected(true);
				if (Boolean.valueOf(result[25]))
					RXThresh_Box.setSelected(true);
				if (Boolean.valueOf(result[26]))
					dataRate_Box.setSelected(true);
				if (Boolean.valueOf(result[27]))
					basicRate_Box.setSelected(true);
	
				Gt_.setText(result[28]);
				Gr_.setText(result[29]);
				L_.setText(result[30]);
				freq_.setText(result[31]);
				bandwidth_.setText(result[32]);
				Pt_.setText(result[33]);
				CPThresh_.setText(result[34]);
				CSThresh_.setText(result[35]);
				RXThresh_.setText(result[36]);
				dataRate_.setText(result[37]);
				basicRate_.setText(result[38]);

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadConfig() {
		String userHome = System.getProperty("user.home");
		File f = new File(userHome + "/nsg/");
		if (!f.exists())
			f.mkdirs();

		f = new File(userHome + "/nsg/wirelessConfig.txt");
		if (!f.exists()) {
			try {
				Writer out = new BufferedWriter(new FileWriter(f));
				out.write("1 Channel/WirelessChannel\n");
				out.write("2 Propagation/TwoRayGround\n");
				out.write("3 Phy/WirelessPhy\n");
				out.write("4 Mac/802_11\n");
				out.write("5 Queue/DropTail/PriQueue\n");
				out.write("6 LL\n");
				out.write("7 Antenna/OmniAntenna\n");
				out.write("8 DSDV DSR AODV TORA\n");
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int type;
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			String line = in.readLine();
			while (line != null) {
				String[] result = line.split(" ");
				type = Integer.valueOf(result[0]);
				switch (type) {
				case 1:
					for (int i = 1; i < result.length; i++) {
						channelItem.addItem(result[i]);
					}
					break;
				case 2:
					for (int i = 1; i < result.length; i++) {
						propagationItem.addItem(result[i]);
					}
					break;
				case 3:
					for (int i = 1; i < result.length; i++) {
						phyItem.addItem(result[i]);
					}
					break;
				case 4:
					for (int i = 1; i < result.length; i++) {
						macItem.addItem(result[i]);
					}
					break;
				case 5:
					for (int i = 1; i < result.length; i++) {
						queueItem.addItem(result[i]);
					}
					break;
				case 6:
					for (int i = 1; i < result.length; i++) {
						linkLayerItem.addItem(result[i]);
					}
					break;
				case 7:
					for (int i = 1; i < result.length; i++) {
						antennaItem.addItem(result[i]);
					}
					break;
				case 8:
					for (int i = 1; i < result.length; i++) {
						routingItem.addItem(result[i]);
					}
					break;
				}
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Simulation parameters
	 */
	JTextField simTimeItem = new JTextField("10.0");
	JTextField traceFileItem = new JTextField("out.tr");
	JTextField namFileItem = new JTextField("out.nam");

	/*
	 * Wireless parameters
	 */
	JComboBox channelItem = new JComboBox();
	JComboBox propagationItem = new JComboBox();
	JComboBox phyItem = new JComboBox();
	JComboBox macItem = new JComboBox();
	JComboBox queueItem = new JComboBox();
	JComboBox linkLayerItem = new JComboBox();
	JComboBox antennaItem = new JComboBox();
	JTextField maxPacketInIfqItem = new JTextField("50");
	JComboBox routingItem = new JComboBox();
	JComboBox agentTraceItem = new JComboBox(new String[] { "ON", "OFF" });
	JComboBox routerTraceItem = new JComboBox(new String[] { "ON", "OFF" });
	JComboBox macTraceItem = new JComboBox(new String[] { "ON", "OFF" });
	JComboBox movementTraceItem = new JComboBox(new String[] { "ON", "OFF" });

	/*
	 * Channel parameters
	 */
	JCheckBox Gt_Box = new JCheckBox();
	JCheckBox Gr_Box = new JCheckBox();
	JCheckBox L_Box = new JCheckBox();
	JCheckBox freq_Box = new JCheckBox();
	JCheckBox bandwidth_Box = new JCheckBox();
	JCheckBox Pt_Box = new JCheckBox();
	JCheckBox CPThresh_Box = new JCheckBox();
	JCheckBox CSThresh_Box = new JCheckBox();
	JCheckBox RXThresh_Box = new JCheckBox();
	JCheckBox dataRate_Box = new JCheckBox();
	JCheckBox basicRate_Box = new JCheckBox();

	JTextField Gt_ = new JTextField("1", 10);
	JTextField Gr_ = new JTextField("1", 10);
	JTextField L_ = new JTextField("1.0", 10);
	JTextField freq_ = new JTextField("2.472e9", 10);
	JTextField bandwidth_ = new JTextField("11Mb", 10);
	JTextField Pt_ = new JTextField("0.031622777", 10);
	JTextField CPThresh_ = new JTextField("10.0", 10);
	JTextField CSThresh_ = new JTextField("5.011872e-12", 10);
	JTextField RXThresh_ = new JTextField("5.82587e-09", 10);
	JTextField dataRate_ = new JTextField("11Mb", 10);
	JTextField basicRate_ = new JTextField("1Mb", 10);

	JButton done = new JButton("Done");
	JButton save = new JButton("Save as default");
	JButton load = new JButton("Load");

	JTabbedPane tab = new JTabbedPane();

	public ParameterDialog(JFrame p, int mode) {
		super(p, true);
		loadConfig();
		loadDefault();
		
		setTitle("Simulation parameters setup");

		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w / 2 - 250, h / 2 - 300, 600, 600);

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(3, 2));
		innerPanel.add(new JLabel("Simulation time"));
		innerPanel.add(simTimeItem);
		innerPanel.add(new JLabel("Trace File"));
		innerPanel.add(traceFileItem);
		innerPanel.add(new JLabel("Nam File"));
		innerPanel.add(namFileItem);
		panel.add(innerPanel);
		tab.add("Simulation", panel);

		if (mode == WIRELESS_MODE) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout());
			innerPanel = new JPanel();
			innerPanel.setLayout(new GridLayout(14, 2));
			innerPanel.add(new JLabel("Channel type"));
			innerPanel.add(channelItem);
			innerPanel.add(new JLabel("Propagation model"));
			innerPanel.add(propagationItem);
			innerPanel.add(new JLabel("Phy type"));
			innerPanel.add(phyItem);
			innerPanel.add(new JLabel("Mac protocol type"));
			innerPanel.add(macItem);
			innerPanel.add(new JLabel("Queue type"));
			innerPanel.add(queueItem);
			innerPanel.add(new JLabel("Link layer type"));
			innerPanel.add(linkLayerItem);
			innerPanel.add(new JLabel("Antenna type"));
			innerPanel.add(antennaItem);
			innerPanel.add(new JLabel("Max packet in queue"));
			innerPanel.add(maxPacketInIfqItem);
			innerPanel.add(new JLabel("Routing protocol"));
			innerPanel.add(routingItem);
			innerPanel.add(new JLabel("Agent trace"));
			innerPanel.add(agentTraceItem);
			innerPanel.add(new JLabel("Router trace"));
			innerPanel.add(routerTraceItem);
			innerPanel.add(new JLabel("Mac trace"));
			innerPanel.add(macTraceItem);
			innerPanel.add(new JLabel("Movement trace"));
			innerPanel.add(movementTraceItem);
			panel.add(innerPanel);
			tab.add("Wireless", panel);

			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			innerPanel = new JPanel();
			// innerPanel.setLayout();
			// innerPanel.setPreferredSize(new Dimension(100,100));
			// innerPanel.setMaximumSize(new Dimension(300,350));
			JPanel innerPanel2 = new JPanel(new GridLayout(14, 2));
			innerPanel2.add(Gt_Box);
			innerPanel2.add(new JLabel("Gt_"));
			innerPanel2.add(Gt_);
			innerPanel2.add(Gr_Box);
			innerPanel2.add(new JLabel("Gr_"));
			innerPanel2.add(Gr_);
			innerPanel2.add(L_Box);
			innerPanel2.add(new JLabel("L_"));
			innerPanel2.add(L_);
			innerPanel2.add(freq_Box);
			innerPanel2.add(new JLabel("freq_"));
			innerPanel2.add(freq_);
			innerPanel2.add(bandwidth_Box);
			innerPanel2.add(new JLabel("bandwidth_"));
			innerPanel2.add(bandwidth_);
			innerPanel2.add(Pt_Box);
			innerPanel2.add(new JLabel("Pt_"));
			innerPanel2.add(Pt_);
			innerPanel2.add(CPThresh_Box);
			innerPanel2.add(new JLabel("CPThresh_"));
			innerPanel2.add(CPThresh_);
			innerPanel2.add(CSThresh_Box);
			innerPanel2.add(new JLabel("CSThresh_"));
			innerPanel2.add(CSThresh_);
			innerPanel2.add(RXThresh_Box);
			innerPanel2.add(new JLabel("RXThresh_"));
			innerPanel2.add(RXThresh_);
			innerPanel2.add(dataRate_Box);
			innerPanel2.add(new JLabel("dataRate_"));
			innerPanel2.add(dataRate_);
			innerPanel2.add(basicRate_Box);
			innerPanel2.add(new JLabel("basicRate_"));
			innerPanel2.add(basicRate_);
			innerPanel.add(innerPanel2);

			panel.add(innerPanel, BorderLayout.CENTER);

			panel
					.add(
							new JLabel(
									"<html><center><font color=#ff0000>The default values are based on Wu Xiuchao's technical report.<br>"
											+ "Those valuses can be used in simulating Orinoco 802.11b 11Mbps<br>"
											+ "PC card with 22.5m transmission range. However, Wu Xiuchao has pointed<br>"
											+ "out that some parameters should be adjusted based on simulation environment.<br>"
											+ "For more details please refer to </font><br>"
											+ "<font color=#0000ff>http://www.comp.nus.edu.sg/~wuxiucha/research/reactive/report/80211ChannelinNS2_new.pdf</font>",
									JLabel.CENTER), BorderLayout.SOUTH);
			tab.add("Channel", panel);
		}

		panel = new JPanel();
		panel.add(done);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel.add(save);
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveDefault();
			}
		});
		// panel.add(load);
		// done.addActionListener(new ActionListener(){
		// public void actionPerformed(ActionEvent e){
		// loadConfig();
		// }
		// });
		this.getContentPane().add(panel, BorderLayout.SOUTH);
		this.getContentPane().add(tab, BorderLayout.CENTER);
	}

}
