package nsg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import nsg.component.Agent;
import nsg.component.App;
import nsg.component.Link;
import nsg.component.Node;
import nsg.component.Waypoint;
import nsg.tool.Tool;

public class TCLManager extends JInternalFrame implements NSGParameters {
	static final long serialVersionUID = 0;

	JTextPane tclArea = new JTextPane();

	File tclFile;
	JFileChooser tcljfc = new JFileChooser();

	// AttributeSet
	SimpleAttributeSet titleAttr = new SimpleAttributeSet();
	SimpleAttributeSet tclAttr = new SimpleAttributeSet();
	SimpleAttributeSet noteAttr = new SimpleAttributeSet();
	Document doc;

	JToolBar toolbar = new JToolBar();
	SceneManager sm;
	ParameterDialog pm;
	DataMaintainer dm;

	public TCLManager(SceneManager sm) {
		super("WiredTcl", true, true, true, true);
		this.sm = sm;
		dm = sm.sv.dm;
		pm = sm.parameters;
		
		tcljfc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File file) {
				if (file.isDirectory()) {
					return true;
				} else if (file.getName().endsWith(".tcl")) {
					return true;
				}
				return false;
			}

			public String getDescription() {
				return "TCL files (*.tcl)";
			}
		});
		StyleConstants.setFontSize(titleAttr, 14);
		StyleConstants.setFontFamily(titleAttr, "Courier New");
		StyleConstants.setBold(titleAttr, true);
		StyleConstants.setForeground(titleAttr, Color.BLUE);

		StyleConstants.setFontSize(tclAttr, 14);
		StyleConstants.setFontFamily(tclAttr, "Courier New");
		StyleConstants.setBold(tclAttr, false);
		StyleConstants.setForeground(tclAttr, Color.BLACK);

		StyleConstants.setFontSize(noteAttr, 14);
		StyleConstants.setFontFamily(noteAttr, "Courier New");
		StyleConstants.setBold(noteAttr, true);
		StyleConstants.setForeground(noteAttr, new Color(32, 158, 29));
		doc = tclArea.getDocument();

		JButton b = new JButton(" Save ");
		b.setMnemonic('S');
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTclFile();
			}
		});
		toolbar.add(b);
		b = new JButton(" Save as ");
		b.setMnemonic('A');
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveTclFileAs();
			}
		});
		toolbar.add(b);
		toolbar.setFloatable(false);

		this.getContentPane().add(toolbar, BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(tclArea), BorderLayout.CENTER);
		generate();
	}

	private void generate() {
		appendTitle("# This script is created by NSG2 beta1\n");
		appendTitle("# <http://wushoupong.googlepages.com/nsg>\n");

		parametersSetup();
		initialization();
		if (sm.sceneMode == WIRELESS_MODE)
			mobileInit();
		nodesDefinition();
		if (sm.sceneMode == WIRELESS_MODE)
			generateWaypoint();
		if (sm.sceneMode == WIRED_MODE)
			linksDefinition();
		agentsDefinition();
		appDefinition();
		termination();
	}

	private void parametersSetup() {
		appendNote("\n#===================================\n");
		appendNote("#     Simulation parameters setup\n");
		appendNote("#===================================\n");

		if (sm.sceneMode == WIRELESS_MODE) {
			// The Antenna height of transmitter and receiver is 1.5m.
			// The propagation model is TwoRayGround model.
			if (pm.Gt_Box.isSelected())
				appendTCL("Antenna/OmniAntenna set Gt_ " + pm.Gt_.getText(), "              ;#Transmit antenna gain\n");
			if (pm.Gr_Box.isSelected())
				appendTCL("Antenna/OmniAntenna set Gr_ " + pm.Gr_.getText(), "              ;#Receive antenna gain\n");
			if (pm.L_Box.isSelected())
				appendTCL("Phy/WirelessPhy set L_ " + pm.L_.getText(), "                 ;#System Loss Factor\n");
			if (pm.freq_Box.isSelected())
				appendTCL("Phy/WirelessPhy set freq_ " + pm.freq_.getText(), "          ;#channel\n");
			if (pm.bandwidth_Box.isSelected())
				appendTCL("Phy/WirelessPhy set bandwidth_ " + pm.bandwidth_.getText(), "        ;#Data Rate\n");
			if (pm.Pt_Box.isSelected())
				appendTCL("Phy/WirelessPhy set Pt_ " + pm.Pt_.getText(), "        ;#Transmit Power\n");
			if (pm.CPThresh_Box.isSelected())
				appendTCL("Phy/WirelessPhy set CPThresh_ " + pm.CPThresh_.getText(), "         ;#Collision Threshold\n");
			if (pm.CSThresh_Box.isSelected())
				appendTCL("Phy/WirelessPhy set CSThresh_ " + pm.CSThresh_.getText(), " ;#Carrier Sense Power\n");
			if (pm.RXThresh_Box.isSelected())
				appendTCL("Phy/WirelessPhy set RXThresh_ " + pm.RXThresh_.getText(), "  ;#Receive Power Threshold\n");
			if (pm.dataRate_Box.isSelected())
				appendTCL("Mac/802_11 set dataRate_ " + pm.dataRate_.getText(), "              ;#Rate for Data Frames\n");
			if (pm.basicRate_Box.isSelected())
				appendTCL("Mac/802_11 set basicRate_ " + pm.basicRate_.getText(), "              ;#Rate for Control Frames\n\n");

			appendTCL("set val(chan)   " + pm.channelItem.getSelectedItem() + "    ", ";# channel type\n");
			appendTCL("set val(prop)   " + pm.propagationItem.getSelectedItem() + "   ", ";# radio-propagation model\n");
			appendTCL("set val(netif)  " + pm.phyItem.getSelectedItem() + "            ", ";# network interface type\n");
			appendTCL("set val(mac)    " + pm.macItem.getSelectedItem() + "                 ", ";# MAC type\n");
			appendTCL("set val(ifq)    " + pm.queueItem.getSelectedItem() + "    ", ";# interface queue type\n");
			appendTCL("set val(ll)     " + pm.linkLayerItem.getSelectedItem() + "                         ", ";# link layer type\n");
			appendTCL("set val(ant)    " + pm.antennaItem.getSelectedItem() + "        ", ";# antenna model\n");
			appendTCL("set val(ifqlen) " + pm.maxPacketInIfqItem.getText() + "                         ", ";# max packet in ifq\n");
			if (dm.nodes.size() < 10) {
				appendTCL("set val(nn)     " + dm.nodes.size() + "                          ", ";# number of mobilenodes\n");
			} else if (dm.nodes.size() < 100) {
				appendTCL("set val(nn)     " + dm.nodes.size() + "                         ", ";# number of mobilenodes\n");
			} else if (dm.nodes.size() < 1000) {
				appendTCL("set val(nn)     " + dm.nodes.size() + "                        ", ";# number of mobilenodes\n");
			} else if (dm.nodes.size() < 10000) {
				appendTCL("set val(nn)     " + dm.nodes.size() + "                       ", ";# number of mobilenodes\n");
			}
			appendTCL("set val(rp)     " + pm.routingItem.getSelectedItem() + "                       ", ";# routing protocol\n");

			int maxX = 0;
			int maxY = 0;
			Node node;
			Object[] nodes = dm.getNodes();
			for (int i = 0; i < nodes.length; i++) {
				node = (Node)nodes[i];
				if (Tool.translateX(0, node.x ,sm.sv.scale) > maxX)
					maxX = Tool.translateX(0, node.x ,sm.sv.scale);
				if (Tool.translateY(0, node.y,sm.sv.scale) > maxY)
					maxY = Tool.translateY(0, node.y,sm.sv.scale);
				if (node.waypoints != null){
					Object[] points = node.waypoints.toArray();
					for (int a = 0 ; a <points.length ; a++){
						if ( Integer.parseInt(((Waypoint)points[a]).destX) > maxX)
							maxX = Integer.parseInt(((Waypoint)points[a]).destX);
						if (Integer.parseInt(((Waypoint)points[a]).destY) > maxY)
							maxY = Integer.parseInt(((Waypoint)points[a]).destY);
					}	
				}
				
			}

			appendTCL("set val(x)      " + (maxX+100) + "                      ", ";# X dimension of topography\n");
			appendTCL("set val(y)      " + (maxY+100) + "                      ", ";# Y dimension of topography\n");
		}
		appendTCL("set val(stop)   " + pm.simTimeItem.getText() + "                         ", ";# time of simulation end\n");
	}

	private void initialization() {
		boolean enableTrace = !pm.traceFileItem.getText().equals("");
		boolean enableNAM = !pm.namFileItem.getText().equals("");

		appendNote("\n#===================================\n");
		appendNote("#        Initialization        \n");
		appendNote("#===================================\n");
		appendNote("#Create a ns simulator\n");
		appendTCL("set ns [new Simulator]\n\n");

		if (sm.sceneMode == WIRELESS_MODE) {
			appendNote("#Setup topography object\n");
			appendTCL("set topo       [new Topography]\n");
			appendTCL("$topo load_flatgrid $val(x) $val(y)\n");
			appendTCL("create-god $val(nn)\n\n");
		}

		if (enableTrace) {
			appendNote("#Open the NS trace file\n");
			appendTCL("set tracefile [open " + pm.traceFileItem.getText() + " w]\n");
			appendTCL("$ns trace-all $tracefile\n\n");
		}
		if (enableNAM) {
			appendNote("#Open the NAM trace file\n");
			appendTCL("set namfile [open " + pm.namFileItem.getText() + " w]\n");
			appendTCL("$ns namtrace-all $namfile\n");
			if (sm.sceneMode == WIRELESS_MODE)
				appendTCL("$ns namtrace-all-wireless $namfile $val(x) $val(y)\n");
		}

		if (sm.sceneMode == WIRELESS_MODE)
			appendTCL("set chan [new $val(chan)]", ";#Create wireless channel\n");
	}

	private void mobileInit() {
		appendNote("\n#===================================\n");
		appendNote("#     Mobile node parameter setup\n");
		appendNote("#===================================\n");
		appendTCL("$ns node-config -adhocRouting  $val(rp) \\\n");
		appendTCL("                -llType        $val(ll) \\\n");
		appendTCL("                -macType       $val(mac) \\\n");
		appendTCL("                -ifqType       $val(ifq) \\\n");
		appendTCL("                -ifqLen        $val(ifqlen) \\\n");
		appendTCL("                -antType       $val(ant) \\\n");
		appendTCL("                -propType      $val(prop) \\\n");
		appendTCL("                -phyType       $val(netif) \\\n");
		appendTCL("                -channel       $chan \\\n");
		appendTCL("                -topoInstance  $topo \\\n");
		appendTCL("                -agentTrace    " + pm.agentTraceItem.getSelectedItem() + " \\\n");
		appendTCL("                -routerTrace   " + pm.routerTraceItem.getSelectedItem() + " \\\n");
		appendTCL("                -macTrace      " + pm.macTraceItem.getSelectedItem() + " \\\n");
		appendTCL("                -movementTrace " + pm.movementTraceItem.getSelectedItem() + "\n");
	}

	private void nodesDefinition() {
		appendNote("\n#===================================\n");
		appendNote("#        Nodes Definition        \n");
		appendNote("#===================================\n");

		appendNote("#Create " + dm.nodes.size() + " nodes\n");
		Node node;
		Object[] nodes = dm.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			node = (Node)nodes[i];
			appendTCL("set n" + node.id + " [$ns node]\n");
			if (sm.sceneMode == WIRELESS_MODE) {
				appendTCL("$n" + node.id + " set X_ " + Tool.translateX(node.x) + "\n");
				appendTCL("$n" + node.id + " set Y_ " + Tool.translateY(node.y) + "\n");
				appendTCL("$n" + node.id + " set Z_ 0.0\n");
				appendTCL("$ns initial_node_pos $n" + node.id + " 20\n");
			}
		}
	}

	private void generateWaypoint(){
		Node node;
		Object[] nodes = dm.getNodes();
		boolean enable = false;
		
		
		for (int i = 0; i < nodes.length; i++) {
			node = (Node)nodes[i];
			if (node.waypoints != null){
				if (enable == false){
					appendNote("\n#===================================\n" );
					appendNote("#        Generate movement          \n" );
					appendNote("#===================================\n" );
					enable = true;
				}
				Object[] points = node.waypoints.toArray();
				for (int a = 0 ; a <points.length ; a++){
					appendTCL("$ns at "+ ((Waypoint)points[a]).startTime + " \" $n" + node.id + " setdest "+ ((Waypoint)points[a]).destX +" " + ((Waypoint)points[a]).destY + " " + ((Waypoint)points[a]).speed+" \" \n");
				}	
			}
		}
	}	
	
	private void linksDefinition() {
		appendNote("\n#===================================\n");
		appendNote("#        Links Definition        \n");
		appendNote("#===================================\n");
		
		appendNote("#Createlinks between nodes\n");
		String linkType = "";
		String queueType = "";
		Link link;
		Object[] links = dm.getLinks();
		for (int i = 0; i < links.length; i++) {
			link = (Link)links[i];
			switch (link.queueType) {
			case QUEUE_DROP_TAIL:
				queueType = "DropTail";
				break;
			case QUEUE_RED:
				queueType = "RED";
				break;
			case QUEUE_FQ:
				queueType = "FQ";
				break;
			case QUEUE_DRR:
				queueType = "DRR";
				break;
			case QUEUE_SFQ:
				queueType = "SFQ";
				break;
			case QUEUE_CBQ:
				queueType = "CBQ";
				break;
			}
			if (link.linkType == DUPLEX_LINK) {
				linkType = "duplex-link";
			} else {
				linkType = "simplex-link";
			}
			appendTCL("$ns " + linkType + " $n" + link.src.id + " $n" + link.dst.id + " " + link.capacity + "Mb " + link.propagationDelay + "ms " + queueType + "\n");
			if (link.queueSize != -1)
				appendTCL("$ns queue-limit $n" + link.src.id + " $n" + link.dst.id + " " + link.queueSize + "\n");
		}

		appendNote("\n#Give node position (for NAM)\n");

		for (int i = 0; i < links.length; i++) {
			link = (Link)links[i];
			if (link.linkType == DUPLEX_LINK) {
				linkType = "duplex-link";
			} else {
				linkType = "simplex-link";
			}
			if (link.src.x < link.dst.x) {
				if (link.dst.y - link.src.y > 20) {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient right-down\n");
				} else if (link.dst.y - link.src.y < -20) {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient right-up\n");
				} else {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient right\n");
				}
			} else {
				if (link.dst.y - link.src.y > 20) {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient left-down\n");
				} else if (link.dst.y - link.src.y < -20) {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient left-up\n");
				} else {
					appendTCL("$ns " + linkType + "-op $n" + link.src.id + " $n" + link.dst.id + " orient left\n\n");
				}
			}
		}

	}

	private void agentsDefinition() {
		appendNote("\n#===================================\n");
		appendNote("#        Agents Definition        \n");
		appendNote("#===================================\n");

		// appendNote("#Create "+v.agents.size()+" nodes\n");
		Agent agent;
		Object[] agents = dm.getAgents();
		for (int i = 0; i < agents.length; i++) {
			agent = (Agent)agents[i];
			if (agent.remoteAgent == null)
				continue;
			switch (agent.agentType) {
			case AGENT_TCP:
			case AGENT_TCP_TAHOE:
			case AGENT_TCP_RENO:
			case AGENT_TCP_NEWRENO:
			case AGENT_TCP_VEGAS:
				appendNote("#Setup a " + Agent.convertType(agent.agentType) + " connection\n");
				appendTCL("set tcp" + agent.id + " [new Agent/" + Agent.convertType(agent.agentType) + "]\n");
				appendTCL("$ns attach-agent $n" + agent.attachedNode.id + " $tcp" + agent.id + "\n");
				appendTCL("set sink" + agent.remoteAgent.id + " [new Agent/" + Agent.convertType(agent.remoteAgent.agentType) + "]\n");
				appendTCL("$ns attach-agent $n" + agent.remoteAgent.attachedNode.id + " $sink" + agent.remoteAgent.id + "\n");
				appendTCL("$ns connect $tcp" + agent.id + " $sink" + agent.remoteAgent.id + "\n");
				if (agent.packetSize != -1)	appendTCL("$tcp" + agent.id + " set packetSize_ " + agent.packetSize + "\n");
				if (agent.window != -1)	appendTCL("$tcp" + agent.id + " set window_ " + agent.window + "\n");
				if (agent.maxcwnd != -1)	appendTCL("$tcp" + agent.id + " set maxcwnd_ " + agent.maxcwnd + "\n");
				if (agent.windowInit != -1)	appendTCL("$tcp" + agent.id + " set windowInit_ " + agent.windowInit + "\n");
				if (agent.tcpTick != -1)	appendTCL("$tcp" + agent.id + " set tcpTick_ " + agent.tcpTick + "\n");
				if (agent.maxburst != -1)	appendTCL("$tcp" + agent.id + " set maxburst_ " + agent.maxburst + "\n");
				appendTCL("\n");
				break;
			case AGENT_UDP:
				appendNote("#Setup a " + Agent.convertType(agent.agentType) + " connection\n");
				appendTCL("set udp" + agent.id + " [new Agent/" + Agent.convertType(agent.agentType) + "]\n");
				appendTCL("$ns attach-agent $n" + agent.attachedNode.id + " $udp" + agent.id + "\n");
				appendTCL("set null" + agent.remoteAgent.id + " [new Agent/" + Agent.convertType(agent.remoteAgent.agentType) + "]\n");
				appendTCL("$ns attach-agent $n" + agent.remoteAgent.attachedNode.id + " $null" + agent.remoteAgent.id + "\n");
				appendTCL("$ns connect $udp" + agent.id + " $null" + agent.remoteAgent.id + "\n");
				if (agent.packetSize != -1)	appendTCL("$udp" + agent.id + " set packetSize_ " + agent.packetSize + "\n");
				appendTCL("\n");
				break;
			case AGENT_TCP_SINK:
				continue;
			case AGENT_NULL:
				continue;
			default:
				System.out.println("Agents definition error");
				continue;
			}
		}
	}

	private void appDefinition() {
		appendNote("\n#===================================\n");
		appendNote("#        Applications Definition        \n");
		appendNote("#===================================\n");

		App app;
		Object[] apps = dm.getApps();
		for (int i = 0; i < apps.length; i++) {
			app = (App)apps[i];
			switch (app.appType) {
			case APP_FTP:
				appendNote("#Setup a FTP Application over " + Agent.convertType(app.agent.agentType) + " connection\n");
				appendTCL("set ftp" + app.id + " [new Application/FTP]\n");
				if (app.agent.agentType % 4 == 0) {
					appendTCL("$ftp" + app.id + " attach-agent $tcp" + app.agent.id + "\n");
				} else {
					appendTCL("$ftp" + app.id + " attach-agent $udp" + app.agent.id + "\n");
				}
				appendTCL("$ns at " + app.startTime + " \"$ftp" + app.id + " start\"\n");
				appendTCL("$ns at " + app.stopTime + " \"$ftp" + app.id + " stop\"\n\n");
				break;
			case APP_CBR:
				appendNote("#Setup a CBR Application over " + Agent.convertType(app.agent.agentType) + " connection\n");
				appendTCL("set cbr" + app.id + " [new Application/Traffic/CBR]\n");
				if (app.agent.agentType % 4 == 0) {
					appendTCL("$cbr" + app.id + " attach-agent $tcp" + app.agent.id + "\n");
				} else {
					appendTCL("$cbr" + app.id + " attach-agent $udp" + app.agent.id + "\n");
				}
				appendTCL("$cbr" + app.id + " set packetSize_ " + app.packetSize + "\n");
				appendTCL("$cbr" + app.id + " set rate_ " + app.rate + "Mb\n");
				appendTCL("$cbr" + app.id + " set random_ " + app.random + "\n");
				appendTCL("$ns at " + app.startTime + " \"$cbr" + app.id + " start\"\n");
				appendTCL("$ns at " + app.stopTime + " \"$cbr" + app.id + " stop\"\n\n");
				break;
			case APP_PING:				
//				appendNote("#Setup a Ping application\n");				
//				appendTCL("set ping_a(" + i + ") [new Agent/Ping]\n");
//				appendTCL("$ns attach-agent $node_(" + app.src.id	+ ") $ping_a(" + i + ")\n");
//				appendTCL("set ping_b(" + i + ") [new Agent/Ping]\n");
//				appendTCL("$ns attach-agent $node_(" + app.dst.id	+ ") $ping_b(" + i + ")\n");
//				appendTCL("$ns connect $ping_a(" + i + ") $ping_b(" + i	+ ")	;");
//				appendNote("#Connect Ping Agents\n");
//				appendTCL("$ns at " + l.start + " \"$ping_a(" + i + ") send\"	;");
//				appendNote("#Time to ping\n");
//				appendTCL("\n");
				
				break;
			case APP_EXPONENTIAL:
				break;
			case APP_PARETO:
				break;
			default:
				System.out.println("Applications definition error");
				continue;
			}
		}
	}

	private void termination() {
		boolean enableTrace = !pm.traceFileItem.getText().equals("");
		boolean enableNAM = !pm.namFileItem.getText().equals("");

		appendNote("\n#===================================\n");
		appendNote("#        Termination        \n");
		appendNote("#===================================\n");
		appendNote("#Define a 'finish' procedure\n");

		appendTCL("proc finish {} {\n");
		appendTCL("    global ns");
		if (enableTrace)
			appendTCL(" tracefile");
		if (enableNAM) {
			appendTCL(" namfile\n");
		} else {
			appendTCL("\n");
		}
		if (enableTrace | enableNAM)
			appendTCL("    $ns flush-trace\n");
		if (enableTrace)
			appendTCL("    close $tracefile\n");
		if (enableNAM) {
			appendTCL("    close $namfile\n");
			appendTCL("    exec nam "+pm.namFileItem.getText()+" &\n");
		}
		appendTCL("    exit 0\n");
		appendTCL("}\n");

		if (sm.sceneMode == WIRELESS_MODE) {
			// 告訴MobileNode模擬已結束
			appendTCL("for {set i 0} {$i < $val(nn) } { incr i } {\n");
			appendTCL("    $ns at $val(stop) \"\\$n$i reset\"\n");
			appendTCL("}\n");
		}

		// 結束nam與ns模擬器
		appendTCL("$ns at $val(stop) \"$ns nam-end-wireless $val(stop)\"\n");
		appendTCL("$ns at $val(stop) \"finish\"\n");
		appendTCL("$ns at $val(stop) \"puts \\\"done\\\" ; $ns halt\"\n");

		appendTCL("$ns run\n");
	}

	public void appendTitle(String s) {
		try {
			doc.insertString(tclArea.getCaretPosition(), s, titleAttr);
		} catch (Exception evt) {
			evt.printStackTrace();
		}
	}

	public void appendNote(String note) {
		try {
			doc.insertString(tclArea.getCaretPosition(), note, noteAttr);
		} catch (Exception evt) {
			evt.printStackTrace();
		}
	}

	public void appendTCL(String tcl) {
		try {
			doc.insertString(tclArea.getCaretPosition(), tcl, tclAttr);
		} catch (Exception evt) {
			evt.printStackTrace();
		}
	}

	public void appendTCL(String tcl, String note) {
		try {
			doc.insertString(tclArea.getCaretPosition(), tcl, tclAttr);
			doc.insertString(tclArea.getCaretPosition(), note, noteAttr);
		} catch (Exception evt) {
			evt.printStackTrace();
		}
	}

	public boolean saveTclFileAs() {
		try {
			tcljfc.setDialogTitle("Please select file");
			int m = tcljfc.showSaveDialog(this);
			if (m == JFileChooser.APPROVE_OPTION) {
				tclFile = tcljfc.getSelectedFile();
				if (!tclFile.getAbsolutePath().endsWith("tcl")) {
					tclFile = new File(tclFile.getAbsolutePath() + ".tcl");
				}
				saveTclFile();
				return true;
			} else {
				return false;
			}
		} catch (Exception evt) {
			System.out.println(evt.getMessage());
			return false;
		}
	}

	public boolean saveTclFile() {
		try {
			if (tclFile == null) {
				if (!saveTclFileAs()) {
					return false;
				}
			}
			Writer out = new OutputStreamWriter(new FileOutputStream(tclFile));
			// nineten
			out.write(tclArea.getText());
			out.flush();
			out.close();
			setTitle(tclFile.getAbsolutePath());
			return true;
		} catch (Exception evt) {
			System.out.println(evt.getMessage());
			return false;
		}
	}

}
