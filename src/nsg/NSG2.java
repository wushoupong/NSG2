package nsg;

//import AnalysisFrame;
//import ItmTclFrame;
//import PeerLooker;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import nsg.analysis.PacketCounterFrame;
import nsg.p2p.ItmTclFrame;
import nsg.p2p.P2PAnalysisFrame;

public class NSG2 extends JFrame implements NSGParameters{
	static final long serialVersionUID = 0;
	
	
	public static final int WIRED_FRAME 				= 1; 
	public static final int WIRELESS_FRAME 				= 2; 
	public static final int PACKET_COUNTER_FRAME 		= 3; 	
	public static final int ITM_P2P_FRAME 				= 4; 
	public static final int P2P_ANALYSIS_FRAME 			= 5; 	
	
	private static JDesktopPane jdp = new JDesktopPane();
	public static JDesktopPane getMainDesktopPane(){
		return jdp;
	}
	JMenuBar menubar = new JMenuBar();
	
//	private static boolean drawGrid = true;
//	private static boolean drawNode = true;
//	private static boolean drawLink = true;
//	private static boolean drawLinkDetail = false;
//	private static boolean drawAgent = true;
//	private static boolean drawAgentDetail = false;
//	private static boolean drawApp = true;
//	
//	private static boolean drawIR = false;
//	private static boolean drawSR = true;
//	private static boolean drawNodeLocation = true;
//	private static boolean drawConnectivity = true;
	
	private static NSG2 mainFrame;
	
	public NSG2(){
		super("NSG2.1, http://sites.google.com/site/pengjungwu/nsg");
		mainFrame = this;
		jdp.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		jdp.setBackground(NSGParameters.DSEKTOP_BACKGROUND_COLOR);
		this.getContentPane().add(jdp, BorderLayout.CENTER);
		createMenuBar();
	}
	
	private void createMenuBar(){
		this.setJMenuBar(menubar);
		/*
		 * System menu
		 */
		JMenu menu = new JMenu("System");
		menu.setMnemonic('S');
		JMenuItem item = new JMenuItem("Exit");
		item.setMnemonic('X');
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		menu.add(item);
		menubar.add(menu);
		
		/*
		 * Scenario menu
		 */
		menu = new JMenu("Scenario");
		menu.setMnemonic('C');
		item = new JMenuItem("New wired scenario");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createInternalFrame(WIRED_FRAME);
			}
		});
		menu.add(item);
		item = new JMenuItem("New wireless scenario");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createInternalFrame(WIRELESS_FRAME);
			}
		});
		menu.add(item);
		menubar.add(menu);

		/*
		 * P2P menu
		 */
		menu = new JMenu("P2P");
		menu.setMnemonic('2');
		item = new JMenuItem("ITM-P2P generator");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createInternalFrame(ITM_P2P_FRAME);
			}
		});
		menu.add(item);
		item = new JMenuItem("P2P analysis");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				createInternalFrame(P2P_ANALYSIS_FRAME);
			}
		});
		menu.add(item);		
		menubar.add(menu);		
		
//		/*
//		 * Scenario menu
//		 */
//		menu = new JMenu("Analyze");
//		menu.setMnemonic('C');
//		item = new JMenuItem("Packet counter");
//		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				createInternalFrame(PACKET_COUNTER_FRAME);
//			}
//		});
//		menu.add(item);
//		menubar.add(menu);
		
		/*
		 * Window menu
		 */
		menu = new JMenu("Window");
		menu.setMnemonic('W');
		item = new JMenuItem("Iconify all");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JInternalFrame[] all = jdp.getAllFrames();
				for (int i =0 ; i <all.length ; i++){
					try {
						all[i].setIcon(true);
					} catch (java.beans.PropertyVetoException evt) {}
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Deiconify all");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JInternalFrame[] all = jdp.getAllFrames();
				for (int i =0 ; i <all.length ; i++){
					try {
						all[i].setIcon(false);
					} catch (java.beans.PropertyVetoException evt) {}
				}
			}
		});
		menu.add(item);

		item = new JMenuItem("Close all");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JInternalFrame[] all = jdp.getAllFrames();
				for (int i =0 ; i <all.length ; i++){
					try {
						all[i].setClosed(true);
						all[i].dispose();
					} catch (java.beans.PropertyVetoException evt) {}
				}
			}
		});
		menu.add(item);
		
		menubar.add(menu);
	
		/*
		 * Window menu
		 */
		menu = new JMenu("About");
		menu.setMnemonic('A');
		item = new JMenuItem("Version");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(mainFrame, "NSG2\nVersion: 2.1 (Nov. 18, 2008)\nAuthor: Peng-Jung Wu\nhttp://sites.google.com/site/pengjungwu/nsg","About NSG2", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(item);		
		menubar.add(menu);		
	}
	
	public void createInternalFrame(int type){
		JInternalFrame iframe = new JInternalFrame();
		switch (type){
		case WIRED_FRAME:
			iframe = new SceneManager(this,WIRED_MODE);
			iframe.setBounds(0,0,jdp.getWidth(),jdp.getHeight());
			break;
		case WIRELESS_FRAME:
			iframe = new SceneManager(this,WIRELESS_MODE);
			iframe.setBounds(0,0,jdp.getWidth(),jdp.getHeight());
			break;
		case PACKET_COUNTER_FRAME:
			iframe =  new PacketCounterFrame();
			iframe.setBounds(jdp.getWidth()/4,0,jdp.getWidth()/2,jdp.getHeight());
			break;
		case ITM_P2P_FRAME:
			iframe =  new ItmTclFrame();
			iframe.setBounds(0,0,jdp.getWidth()/2,jdp.getHeight());
			break;
		case P2P_ANALYSIS_FRAME:
			iframe =  new P2PAnalysisFrame();
			iframe.setBounds(jdp.getWidth()/2,0,jdp.getWidth()/2,jdp.getHeight());
			break;
		default:
			iframe = new JInternalFrame();
			iframe.setBounds(640,0,720,512);
		}
		iframe.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		iframe.setVisible(true);
		
		jdp.add(iframe);
		try {
			//iframe.setMaximum(true); 
			iframe.setSelected(true);
	    } catch (java.beans.PropertyVetoException e) {
	    	e.printStackTrace();
	    }		
	}
	
//	private void createWiredSceneManager(){
//		SceneManager f = new SceneManager(this,WIRED_MODE);
//		f.setBounds(jdp.getWidth()/4,0,jdp.getWidth()/2,jdp.getHeight());
//		f.setVisible(true);
//		jdp.add(f);
//		try {
//			f.setMaximum(true); 
//	        f.setSelected(true);
//	    } catch (java.beans.PropertyVetoException e) {}
//	}
//	
//	private void createPacketCounterFrame(){
//		PacketCounterFrame f = new PacketCounterFrame();
//		f.setBounds(jdp.getWidth()/4,0,jdp.getWidth()/2,jdp.getHeight());
//		f.setVisible(true);
//		jdp.add(f);
//		try {
//			f.setMaximum(true); 
//	        f.setSelected(true);
//	    } catch (java.beans.PropertyVetoException e) {}
//
//	}
//	private void createThroughputFrame(){
//		
//	}
//	
//	private void createWirelessSceneManager(){
//		SceneManager f = new SceneManager(this,WIRELESS_MODE);
//		f.setBounds(jdp.getWidth()/4,0,jdp.getWidth()/2,jdp.getHeight());
//		f.setVisible(true);
//		jdp.add(f);
//		try {
//			f.setMaximum(true); 
//	        f.setSelected(true);
//	    } catch (java.beans.PropertyVetoException e) {}
//	}
	
	public void createTCLManager(SceneManager sm){		
		TCLManager f = new TCLManager(sm);
		
		f.setBounds(0,0,jdp.getWidth()/2,jdp.getHeight());
		f.setVisible(true);
		jdp.add(f);
		try {
	        f.setSelected(true);
	    } catch (java.beans.PropertyVetoException e) {}		
	}
	
	public static void main(String[] args) {
		NSG2 f = new NSG2();
		//f.setBounds(100,0,1024,768);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);
	}
}
