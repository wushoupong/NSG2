package nsg.p2p;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ItmTclFrame extends JInternalFrame{
		 String path; 
		 int routers 			= 2050;
		 int transit 			= 50;
		 int fecN 				= 6;
		 int fecK 				= 4;
		 int bufferSize 		= 800;
		 int serviceTime 		= 180;	//1800, for simulation speedup
		 int recoveryTime 		= 6;	//60  , for simulation speedup
		 int depth 				= 10;
		 int width 				= 200;		
		 int numParent 			= 6;		
		 int simTime 			= 300;
		 int numSim 			= 10;		
		 
		JTextField pathField 		= new JTextField("~/csvt-sim/");		
		JTextField routerField 		= new JTextField("990");		//Number of routers
		JTextField transitField 	= new JTextField("30");			//number of transit routers
		JTextField fecNField 		= new JTextField("6");			//FEC N
		JTextField fecKField 		= new JTextField("4");			//FEC K
		JTextField bufferField 		= new JTextField("800");		//Buffer size of peers
		JTextField serviceField 	= new JTextField("180");		//Service time
		JTextField recoveryField 	= new JTextField("6");			//Recovery time
		JTextField depthField 		= new JTextField("20");			//Depth of topology
		JTextField widthField 		= new JTextField("50");			//Width of topology		
		JTextField numParentField 	= new JTextField("6");			//Number of parents
		JTextField simTimeField 	= new JTextField("180");		//Simulation time
		JTextField numSimField 		= new JTextField("5");			//Number of simulations
		JButton batchButton 		= new JButton("Create batch"); 		//Create batch files
		JButton tclButton 			= new JButton("Create TCL"); 		//Create batch files
		
		public ItmTclFrame(){
			super("ItmTclFrame",true,true,true,true);
			uiInit();
			
			batchButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					parameterInit();
					File f = new File(path);
					if (!f.exists()) f.mkdirs();
						
					try{
						createBatch();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});
			
			tclButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					parameterInit();						
					try{
						for (int i=0 ; i<numSim ; i++) createTCL(i);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			});			
		}
		
		public void createBatch() throws Exception{
				String filename;
				PrintStream out;
	
				//generate NS2 batch file
				filename = path+"sim-go";
				out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
				for (int i=0 ; i<numSim ; i++) out.println("ns sim"+i+".tcl > res"+i+".txt;echo \"done sim"+i+"\";");
				out.flush();
				out.close();	
				new File(path+"sim-go").setExecutable(true);
				
				//generate GT-ITM batch file
				filename = path+"gt-go";
				out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
				out.println("itm ts"+routers+";");
				for (int i=0 ; i<numSim ; i++) out.println("sgb2ns ts"+routers+"-"+i+".gb ts"+routers+"-"+i+".tcl;rm ts990-"+i+".gb;");
				out.flush();
				out.close();
				new File(path+"gt-go").setExecutable(true);
				
				//generate GT-ITM setup file
				filename = path+"ts990";
				out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
				out.println("## <#method keyword> <#number of graphs> [<#initial seed>]");
				out.println("## <#stubs/xit> <#t-s edges> <#s-s edges>");
				out.println("## <#n> <#scale> <#edgemethod> <#alpha> [<#beta>] [<#gamma>]");
				out.println("## number of nodes = 1*8* (1 + 4*6) = 200");
				out.println("ts " + numSim + " 66");
				out.println("8 0 0");
				out.println("6 20 3 1.0");
				out.println("5 20 3 0.5");
				out.println("4 10 3 0.5");
				out.flush();
				out.close();	
		}
		
		public void createTCL(int index) throws Exception{
			String filename = path+"sim"+index+".tcl";
			
			if (width<numParent){ System.out.println("width<numParent"); return; }
			PrintStream out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
			
			out.println("\n#===================================");
			out.println("#        Initialization               ");
			out.println("#===================================  ");
			out.println("set ns [new Simulator]");

			out.println("\n#===================================");
			out.println("#        Routers Definition           ");
			out.println("#===================================  ");
			for (int a = 0; a < routers; a++) {
				out.println("set rt" + a + " [$ns node]");
			}		

			out.println("\n#===================================");
			out.println("#        Put GT-ITM script here       ");
			out.println("#===================================  ");		
			InputStream in = new BufferedInputStream(new FileInputStream(path+"ts"+routers+"-"+index+".tcl"));
			int ch=in.read();
			while(ch != -1){
				out.write(ch);
				ch=in.read();
			}
			in.close();
		
			out.println("\n#===================================");
			out.println("#        Nodes Definition             ");
			out.println("#===================================  ");		
			out.println("# setup VIDEO SERVER");
			out.println("set n0 [$ns node]");
			int routerID = (int) (Math.random() * (routers - transit));
			routerID = routerID + transit;
			out.println("$ns duplex-link $n0 $rt" + routerID + " 100.0Mb 1ms DropTail");
			
			out.println("# setup PEERS");
			for (int a = 1; a <= depth * width; a++) {
				out.println("set n" + a + " [$ns node]");
				routerID = (int) (Math.random() * (routers - transit));
				routerID = routerID + transit;
				out.println("$ns duplex-link $n" + a + " $rt" + routerID + " 100.0Mb 1ms DropTail");				
			}

			out.println("\n#===================================");
			out.println("#        Apps Definition              ");
			out.println("#===================================  ");
			for (int appID = 0; appID <= depth * width; appID++) {
				out.println("set PeerApp" + appID + " [new Application/PeerApp]");
				out.println("$PeerApp"    + appID + " set_id " + appID);
				out.println("$PeerApp"    + appID + " set_fecN " + fecN);
				out.println("$PeerApp"    + appID + " set_fecK " + fecK);
				out.println("$PeerApp"    + appID + " set_degree " + numParent);//degree
				out.println("$PeerApp"    + appID + " set_buffer " + bufferSize);
				out.println("$PeerApp"    + appID + " set_serviceTime " + serviceTime);
				out.println("$PeerApp"    + appID + " set_recoveryTime " + recoveryTime);
			}

			out.println("\n#===================================");
			out.println("#Peers Definition (depth=0)           ");
			out.println("#===================================  ");
			int parentID = 0;
			for (int childID = 1; childID <= width; childID++) {
				out.println("set sAgent" + childID + " [new Agent/UDP]");
				out.println("set rAgent" + childID + " [new Agent/UDP]");
				out.println("$sAgent" + childID + " set packetSize_ 1500");
				out.println("$rAgent" + childID + " set packetSize_ 1500");
				out.println("$ns attach-agent $n" + parentID + " $sAgent" + childID + "");
				out.println("$ns attach-agent $n" + childID + " $rAgent" + childID + "");
				out.println("$ns connect $sAgent" + childID + " $rAgent" + childID + "");
				out.println("$PeerApp" + parentID + " attach-agent $sAgent" + childID + "");
				out.println("$PeerApp" + childID + " attach-agent $rAgent" + childID + "");
				out.println("$PeerApp" + parentID + " add_child $sAgent" + childID + " -1");
				out.println();
			}
			out.println("\n#===================================");
			out.println("#Peers Definition (depth>0)           ");
			out.println("#===================================  ");
			int[] parents;
			for (int dep = 2; dep <= depth; dep++) {
				for (int wid = 1; wid <= width; wid++) {
					int childID = (dep - 1) * width + wid;
					parents = selectParent(dep);
//					if (numParent == 1){
//						parentID = parents[0];
//						out.println("set sAgent" + childID + " [new Agent/UDP]");
//						out.println("set rAgent" + childID + " [new Agent/UDP]");
//						out.println("$sAgent" + childID + " set packetSize_ 1500");
//						out.println("$rAgent" + childID + " set packetSize_ 1500");
//						out.println("$ns attach-agent $n" + parentID + " $sAgent" + childID + "");
//						out.println("$ns attach-agent $n" + childID + " $rAgent" + childID	+ "");
//						out.println("$ns connect $sAgent" + childID + " $rAgent" + childID + "");
//						out.println("$PeerApp" + parentID + " attach-agent $sAgent" + childID + "");
//						out.println("$PeerApp" + childID + " attach-agent $rAgent" + childID	+ "");
//						out.println("$PeerApp" + parentID + " add_child $sAgent" + childID + " -1");					
//						out.println();
//					}else{
						for (int channel = 0; channel < numParent; channel++) {
							parentID = parents[channel];
							out.println("set s" + channel + "Agent" + childID + " [new Agent/UDP]");
							out.println("set r" + channel + "Agent" + childID + " [new Agent/UDP]");
							out.println("$s" + channel + "Agent" + childID + " set packetSize_ 1500");
							out.println("$r" + channel + "Agent" + childID + " set packetSize_ 1500");
							out.println("$ns attach-agent $n" + parentID + " $s" + channel + "Agent" + childID + "");
							out.println("$ns attach-agent $n" + childID + " $r" + channel + "Agent" + childID	+ "");
							out.println("$ns connect $s" + channel + "Agent" + childID + " $r" + channel + "Agent" + childID + "");
							out.println("$PeerApp" + parentID + " attach-agent $s" + channel + "Agent" + childID + "");
							out.println("$PeerApp" + childID + " attach-agent $r" + channel + "Agent" + childID	+ "");
							out.println("$PeerApp" + parentID + " add_child $s" + channel + "Agent" + childID + " " + channel);					
							out.println();
						}
//					}
				}
			}			
			out.println("\n#===================================");
			out.println("#        Cross Traffic (Exponential Traffic over UDP connection)               ");
			out.println("#===================================  ");
			for (int peerID = 1; peerID <= depth * width; peerID++) {
				int randomID;
				do {
					randomID = (int) (Math.random() * (depth * width));
				} while (randomID == peerID || randomID == 0);
				
				out.println("set sCrossAgent"+peerID+" [new Agent/UDP]");
				out.println("set rCrossAgent"+peerID+" [new Agent/Null]");
				out.println("$sCrossAgent"+peerID+" set packetSize_ 1500");
				out.println("$ns attach-agent $n"+peerID+" $sCrossAgent"+peerID+"");
				out.println("$ns attach-agent $n"+randomID+" $rCrossAgent"+peerID+"");
				out.println("$ns connect $sCrossAgent"+peerID+" $rCrossAgent"+peerID+"");

				out.println("set crossTf"+peerID+" [new Application/Traffic/Exponential]");
				out.println("$crossTf"   +peerID+" set packetSize_ 1024");
				out.println("$crossTf"   +peerID+" set burst_time_ 500ms");
				out.println("$crossTf"   +peerID+" set idle_time_ 500ms");
				out.println("$crossTf"   +peerID+" set rate_ 200k");
				out.println("$crossTf"   +peerID+" attach-agent $sCrossAgent"+peerID+"");
				out.println("$ns at 1.0 \"$crossTf"+peerID+" start\"");
				out.println("$ns at "+simTime+".0 \"$crossTf"+peerID+" stop\"\n");				
			}
			
			out.println("\n#=====Start video server=====");
			out.println("$ns at 1.0 \"$PeerApp0 start\"");
			out.println("$ns at "+simTime+".0 \"$PeerApp0 stop\"");
			
			for (int a = 0; a <= depth * width; a++) {
				out.println("$ns at "+simTime+".0 \"$PeerApp" + a + " end_sim\"");
			}			
			
			out.println("$ns at "+simTime+".0 \"$ns halt\"");
			out.println("$ns run");
			out.flush();
			out.close();
		}

		public int[] selectParent(int depth) {
			int pid;
			int[] result = new int[numParent];
			int[] tmp = new int[width];
			for (int a = 0; a < numParent; a++) {
				do {
					pid = (int) (Math.random() * width);
				} while (tmp[pid] != 0);
				tmp[pid] = 1;
				result[a] = (depth - 2) * width + pid + 1;
			}
			return result;
		}
		public void parameterInit(){
			path 			= pathField.getText();
			routers 		= Integer.parseInt(routerField.getText());
			transit 		= Integer.parseInt(transitField.getText());
			fecN 			= Integer.parseInt(fecNField.getText());
			fecK 			= Integer.parseInt(fecKField.getText());
			bufferSize 		= Integer.parseInt(bufferField.getText());
			serviceTime 	= Integer.parseInt(serviceField.getText());
			recoveryTime 	= Integer.parseInt(recoveryField.getText());					
			depth 			= Integer.parseInt(depthField.getText());
			width 			= Integer.parseInt(widthField.getText());
			numParent 		= Integer.parseInt(numParentField.getText());
			simTime 		= Integer.parseInt(simTimeField.getText());
			numSim 			= Integer.parseInt(numSimField.getText());
		}
		public void uiInit(){
			pathField.setText(System.getProperty("user.home")+"/csvt-sim/");
			
			Container c=getContentPane();
			
			JPanel up = new JPanel();
			up.setLayout(new FlowLayout());
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(0,2));
			
			p.add(new JLabel("Built date  ",JLabel.RIGHT));
			p.add(new JLabel("Dec. 11, 2007  ",JLabel.LEFT));
			p.add(new JLabel("Path  ",JLabel.RIGHT));
			p.add(pathField);
			p.add(new JLabel("# of routers  ",JLabel.RIGHT));
			p.add(routerField);
			p.add(new JLabel("# of transits  ",JLabel.RIGHT));
			p.add(transitField);
			p.add(new JLabel("fecN  ",JLabel.RIGHT));
			p.add(fecNField);
			p.add(new JLabel("fecK  ",JLabel.RIGHT));
			p.add(fecKField);
			p.add(new JLabel("Buffer size",JLabel.RIGHT));
			p.add(bufferField);
			p.add(new JLabel("Service time",JLabel.RIGHT));
			p.add(serviceField);
			p.add(new JLabel("Recovery time",JLabel.RIGHT));
			p.add(recoveryField);
			p.add(new JLabel("Depth  ",JLabel.RIGHT));
			p.add(depthField);
			p.add(new JLabel("Width  ",JLabel.RIGHT));
			p.add(widthField);
			p.add(new JLabel("NumParent  ",JLabel.RIGHT));
			p.add(numParentField);		
			p.add(new JLabel("SimTime  ",JLabel.RIGHT));
			p.add(simTimeField);
			p.add(new JLabel("NumSim  ",JLabel.RIGHT));
			p.add(numSimField);			
			
			p.add(new JPanel().add(batchButton));
			p.add(new JPanel().add(tclButton));
			up.add(p);
			c.add(up,BorderLayout.NORTH);			
		}
}

//public void batch() throws Exception{
//String filename;
//PrintStream out;
//
////generate NS2 batch files
//filename = filenameField.getText()+"sim-go";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//for (int i=0 ; i<10 ; i++){
//	out.print("ns sim"+i+".tcl > res"+i+".txt;");
//	out.print("echo \"done sim"+i+"\";");
//}
//out.flush();
//out.close();				
//
////generate GT-ITM batch files
//filename = filenameField.getText()+"gt-go";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("itm ts"+routers+";");
//for (int i=0 ; i<10 ; i++){
//	out.print("sgb2ns ts"+routers+"-"+i+".gb ts"+routers+"-"+i+".tcl;");
//}
//for (int i=0 ; i<10 ; i++){
//	out.print("rm ts990-"+i+".gb;");
//}
//out.flush();
//out.close();
//
////generate GT-ITM batch files
//filename = filenameField.getText()+"ts990";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("## Comments :\n");
//out.print("## <#method keyword> <#number of graphs> [<#initial seed>]\n");
//out.print("## <#stubs/xit> <#t-s edges> <#s-s edges>\n");
//out.print("## <#n> <#scale> <#edgemethod> <#alpha> [<#beta>] [<#gamma>]\n");
//out.print("## number of nodes = 1*8* (1 + 4*6) = 200\n");
//out.print("ts 10 66\n");
//out.print("8 0 0\n");
//out.print("6 20 3 1.0\n");
//out.print("5 20 3 0.5\n");
//out.print("4 10 3 0.5\n");
//out.flush();
//out.close();			
//
////generate GT-ITM batch files
//filename = path+"/go01";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("cd 1-Single-F64-B5;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim0.tcl > res0.txt;echo \"done sim0\";cd ..;");
//
//out.print("cd 1-Single-F64-B5;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim1.tcl > res1.txt;echo \"done sim1\";cd ..;");
//out.flush();
//out.close();			
//
////generate GT-ITM batch files
//filename = path+"/go23";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("cd 1-Single-F64-B5;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim2.tcl > res2.txt;echo \"done sim2\";cd ..;");
//
//out.print("cd 1-Single-F64-B5;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim3.tcl > res3.txt;echo \"done sim3\";cd ..;");		
//out.flush();
//out.close();				
//
////generate GT-ITM batch files
//filename = path+"/go45";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("cd 1-Single-F64-B5;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim4.tcl > res4.txt;echo \"done sim4\";cd ..;");
//
//out.print("cd 1-Single-F64-B5;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim5.tcl > res5.txt;echo \"done sim5\";cd ..;");			
//out.flush();
//out.close();		
//
//filename = path+"/go67";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("cd 1-Single-F64-B5;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim6.tcl > res6.txt;echo \"done sim6\";cd ..;");
//
//out.print("cd 1-Single-F64-B5;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim7.tcl > res7.txt;echo \"done sim7\";cd ..;");			
//out.flush();
//out.close();	
//
//filename = path+"/go89";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));	
//out.print("cd 1-Single-F64-B5;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim8.tcl > res8.txt;echo \"done sim8\";cd ..;");
//
//out.print("cd 1-Single-F64-B5;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 2-Single-F64-B10;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 3-Single-F64-B20;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 4-Multi-F64-B5;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 5-Multi-F64-B10;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 6-Multi-F64-B20;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 11-Single-NF-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 12-Single-F64-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 13-Single-N54-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 14-Multi-NF-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 15-Multi-F64-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");
//out.print("cd 16-Multi-F54-B40;ns sim9.tcl > res9.txt;echo \"done sim9\";cd ..;");			
//out.flush();
//out.close();
//
//filename = path+"/allgt";
//out = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
//out.print("cd 1-Single-F64-B5;./gt-go;cd ..;");
//out.print("cd 2-Single-F64-B10;./gt-go;cd ..;");
//out.print("cd 3-Single-F64-B20;./gt-go;cd ..;");
//out.print("cd 4-Multi-F64-B5;./gt-go;cd ..;");
//out.print("cd 5-Multi-F64-B10;./gt-go;cd ..;");
//out.print("cd 6-Multi-F64-B20;./gt-go;cd ..;");
//out.print("cd 11-Single-NF-B40;./gt-go;cd ..;");
//out.print("cd 12-Single-F64-B40;./gt-go;cd ..;");
//out.print("cd 13-Single-N54-B40;./gt-go;cd ..;");
//out.print("cd 14-Multi-NF-B40;./gt-go;cd ..;");
//out.print("cd 15-Multi-F64-B40;./gt-go;cd ..;");
//out.print("cd 16-Multi-F54-B40;./gt-go;cd ..;");		
//out.flush();
//out.close();
//
//}

//batchButton.addActionListener(new ActionListener(){
//public void actionPerformed(ActionEvent e){
//	parameterInit();
//	
//	(new File(path+"/1-Single-F64-B5/")).mkdirs();
//	filenameField.setText(path+"/1-Single-F64-B5/");
//	single=true;
//	fecN=6;
//	fecK=4;
//	bufferSize=200;
//	batchBatch();
//	
//	(new File(path+"/2-Single-F64-B10/")).mkdirs();
//	filenameField.setText(path+"/2-Single-F64-B10/");
//	single=true;
//	fecN=6;
//	fecK=4;
//	bufferSize=400;
//	batchBatch();
//
//	(new File(path+"/3-Single-F64-B20/")).mkdirs();
//	filenameField.setText(path+"/3-Single-F64-B20/");
//	single=true;
//	fecN=6;
//	fecK=4;
//	bufferSize=800;
//	batchBatch();
//
////	(new File(path+"/8-Single-NF-B20(x)/")).mkdirs();
////	filenameField.setText(path+"/8-Single-NF-B20(x)/");
////	single=true;
////	fecN=4;
////	fecK=4;
////	bufferSize=800;
////	batchBatch();
//
////	(new File(path+"/10-Single-F54-B20(x)/")).mkdirs();
////	filenameField.setText(path+"/10-Single-F54-B20(x)/");
////	single=true;
////	fecN=5;
////	fecK=4;
////	bufferSize=800;
////	batchBatch();
//
//	(new File(path+"/11-Single-NF-B40/")).mkdirs();
//	filenameField.setText(path+"/11-Single-NF-B40/");
//	single=true;
//	fecN=4;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//
//	(new File(path+"/12-Single-F64-B40/")).mkdirs();
//	filenameField.setText(path+"/12-Single-F64-B40/");
//	single=true;
//	fecN=6;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//
//	(new File(path+"/13-Single-N54-B40/")).mkdirs();
//	filenameField.setText(path+"/13-Single-N54-B40/");
//	single=true;
//	fecN=5;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//
////	(new File(path+"/17-Single-F32-B40(x)/")).mkdirs();
////	filenameField.setText(path+"/17-Single-F32-B40(x)/");
////	single=true;
////	fecN=3;
////	fecK=2;
////	bufferSize=1600;
////	batchBatch();
//
////	(new File(path+"/18-Single-F96-B40(x)/")).mkdirs();
////	filenameField.setText(path+"/18-Single-F96-B40(x)/");
////	single=true;
////	fecN=9;
////	fecK=6;
////	bufferSize=1600;
////	batchBatch();
//	
//	(new File(path+"/4-Multi-F64-B5/")).mkdirs();
//	filenameField.setText(path+"/4-Multi-F64-B5/");
//	single=false;
//	fecN=6;
//	fecK=4;
//	bufferSize=200;
//	batchBatch();
//	
//	(new File(path+"/5-Multi-F64-B10/")).mkdirs();
//	filenameField.setText(path+"/5-Multi-F64-B10/");
//	single=false;
//	fecN=6;
//	fecK=4;
//	bufferSize=400;
//	batchBatch();
//	
//	(new File(path+"/6-Multi-F64-B20/")).mkdirs();
//	filenameField.setText(path+"/6-Multi-F64-B20/");				
//	single=false;
//	fecN=6;
//	fecK=4;
//	bufferSize=800;
//	batchBatch();
//	
////	(new File(path+"/7-Multi-NF-B20(x)/")).mkdirs();
////	filenameField.setText(path+"/7-Multi-NF-B20(x)/");
////	single=false;
////	fecN=4;
////	fecK=4;
////	bufferSize=800;
////	batchBatch();
//	
////	(new File(path+"/9-Multi-F54-B20(x)/")).mkdirs();
////	filenameField.setText(path+"/9-Multi-F54-B20(x)/");
////	single=false;
////	fecN=5;
////	fecK=4;
////	bufferSize=800;
////	batchBatch();
//	
//	(new File(path+"/14-Multi-NF-B40/")).mkdirs();
//	filenameField.setText(path+"/14-Multi-NF-B40/");
//	single=false;
//	fecN=4;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//	
//	(new File(path+"/15-Multi-F64-B40/")).mkdirs();
//	filenameField.setText(path+"/15-Multi-F64-B40/");
//	single=false;
//	fecN=6;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//	
//	(new File(path+"/16-Multi-F54-B40/")).mkdirs();
//	filenameField.setText(path+"/16-Multi-F54-B40/");
//	single=false;
//	fecN=5;
//	fecK=4;
//	bufferSize=1600;
//	batchBatch();
//	
////	(new File(path+"/19-Multi-F32-B40(x)/")).mkdirs();
////	filenameField.setText(path+"/19-Multi-F32-B40(x)/");
////	single=false;
////	fecN=3;
////	fecK=2;
////	bufferSize=1600;
////	batchBatch();
//	
////	(new File(path+"/20-Multi-F96-B40(x)/")).mkdirs();
////	filenameField.setText(path+"/20-Multi-F96-B40(x)/");
////	single=false;
////	fecN=9;
////	fecK=6;
////	bufferSize=1600;
////	batchBatch();
//}
//});

//tclButton.addActionListener(new ActionListener(){
//	public void actionPerformed(ActionEvent e){
//		parameterInit();
//		
//		
//		filenameField.setText(path+"/1-Single-F64-B5/");
//		single=true;
//		fecN=6;
//		fecK=4;
//		bufferSize=200;
//		goBatch();
//
//		filenameField.setText(path+"/2-Single-F64-B10/");
//		single=true;
//		fecN=6;
//		fecK=4;
//		bufferSize=400;
//		goBatch();
//
//		filenameField.setText(path+"/3-Single-F64-B20/");
//		single=true;
//		fecN=6;
//		fecK=4;
//		bufferSize=800;
//		goBatch();
//
////		filenameField.setText(path+"/8-Single-NF-B20(x)/");
////		single=true;
////		fecN=4;
////		fecK=4;
////		bufferSize=800;
////		goBatch();
//
////		filenameField.setText(path+"/10-Single-F54-B20(x)/");
////		single=true;
////		fecN=5;
////		fecK=4;
////		bufferSize=800;
////		goBatch();
//
//		filenameField.setText(path+"/11-Single-NF-B40/");
//		single=true;
//		fecN=4;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//
//		filenameField.setText(path+"/12-Single-F64-B40/");
//		single=true;
//		fecN=6;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//
//		filenameField.setText(path+"/13-Single-N54-B40/");
//		single=true;
//		fecN=5;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//
////		filenameField.setText(path+"/17-Single-F32-B40(x)/");
////		single=true;
////		fecN=3;
////		fecK=2;
////		bufferSize=1600;
////		goBatch();
//
////		filenameField.setText(path+"/18-Single-F96-B40(x)/");
////		single=true;
////		fecN=9;
////		fecK=6;
////		bufferSize=1600;
////		goBatch();
//		
//		filenameField.setText(path+"/4-Multi-F64-B5/");
//		single=false;
//		fecN=6;
//		fecK=4;
//		bufferSize=200;
//		goBatch();
//		
//		filenameField.setText(path+"/5-Multi-F64-B10/");
//		single=false;
//		fecN=6;
//		fecK=4;
//		bufferSize=400;
//		goBatch();
//		
//		filenameField.setText(path+"/6-Multi-F64-B20/");				
//		single=false;
//		fecN=6;
//		fecK=4;
//		bufferSize=800;
//		goBatch();
//		
////		filenameField.setText(path+"/7-Multi-NF-B20(x)/");
////		single=false;
////		fecN=4;
////		fecK=4;
////		bufferSize=800;
////		goBatch();
//		
////		filenameField.setText(path+"/9-Multi-F54-B20(x)/");
////		single=false;
////		fecN=5;
////		fecK=4;
////		bufferSize=800;
////		goBatch();
//		
//		filenameField.setText(path+"/14-Multi-NF-B40/");
//		single=false;
//		fecN=4;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//		
//		filenameField.setText(path+"/15-Multi-F64-B40/");
//		single=false;
//		fecN=6;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//		
//		filenameField.setText(path+"/16-Multi-F54-B40/");
//		single=false;
//		fecN=5;
//		fecK=4;
//		bufferSize=1600;
//		goBatch();
//		
////		filenameField.setText(path+"/19-Multi-F32-B40(x)/");
////		single=false;
////		fecN=3;
////		fecK=2;
////		bufferSize=1600;
////		goBatch();
//		
////		filenameField.setText(path+"/20-Multi-F96-B40(x)/");
////		single=false;
////		fecN=9;
////		fecK=6;
////		bufferSize=1600;
////		goBatch();				
//	}
//});