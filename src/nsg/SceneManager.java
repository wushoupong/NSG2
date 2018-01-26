package nsg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nsg.menu.AgentMenu;
import nsg.menu.AppMenu;
import nsg.menu.LinkMenu;
import nsg.menu.WayPointMenu;

public class SceneManager extends JInternalFrame implements NSGParameters {
	static final long serialVersionUID = 0;

	public  boolean drawGrid = true;
	public  boolean drawNode = true;
	public  boolean drawLink = true;
	public  boolean drawLinkDetail = false;
	public  boolean drawAgent = true;
	public  boolean drawAgentDetail = false;
	public  boolean drawApp = true;
	
	public  boolean drawIR = false;
	public  boolean drawSR = true;
	public  boolean drawNodeLocation = true;
	public  boolean drawConnectivity = true;
	
	public JButton b1 = new JButton("Hand");
	public JButton b2 = new JButton("Node");
	public JButton b3 = new JButton("Link");
	public JButton b4 = new JButton("Agent");
	public JButton b5 = new JButton("Application");
	public JButton b6 = new JButton("Parameters");
	public JButton b7 = new JButton("TCL");
	
	JMenuBar menubar = new JMenuBar();
	public float centerX= -1;
	public float centerY= -1;
	public int sceneMode;

	JToolBar toolbar = new JToolBar();
	NSG2 nsg;
	JPanel center = new JPanel(new BorderLayout());
	public JLabel status = new JLabel();
	JSlider slider = new JSlider(8, 500, 100);

	ParameterDialog parameters;
	public DataMaintainer dm;
	public SceneVirtualizer sv;

	public LinkMenu linkmenu = new LinkMenu(nsg);
	public AgentMenu agentmenu = new AgentMenu(nsg);
	public AppMenu appmenu = new AppMenu(nsg);
	public WayPointMenu waypointMenu = new WayPointMenu(nsg);

	public int goEast = 0;
	public int goWest = 0;
	public int goNorth = 0;
	public int goSouth = 0;
	
	JTextField trBox = new JTextField("250",5);//For transmission range
	JTextField irBox = new JTextField("550",5);//For transmission range
	JButton setRange = new JButton("Set");
	
	public Thread thread;
		
	public class MoveThread extends Thread{
		public void run(){
			try{
				Thread.sleep(800);
			}catch(Exception e){
				e.printStackTrace();
			}	
			while(goEast!=0 || goWest!=0 || goNorth!=0 || goSouth!=0){
				if (goEast != 0){
					sv.shiftX = sv.shiftX - (int)(goEast/sv.scale);
				}
				if (goWest != 0){
					sv.shiftX = sv.shiftX + (int)(goWest/sv.scale);
				}
				if (goSouth != 0){
					sv.shiftY = sv.shiftY - (int)(goSouth/sv.scale);
				}
				if (goNorth != 0){
					sv.shiftY = sv.shiftY + (int)(goNorth/sv.scale);
				}				
				centerX =sv.getWidth()/2/sv.scale - sv.shiftX;
				centerY =sv.getHeight()/2/sv.scale - sv.shiftY;		
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						repaint();						
					}
				});
				try{
					Thread.sleep(100);
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
			System.out.println("Thread stop");
			thread = null;
		}		
	}
	
	public boolean scroll(int x, int y){
		return false; //Disable mouse auto scroll
		
//		if ((sv.getWidth()-x < 60)&&(sv.getWidth()-x > 10)){
//			goEast = 60-(sv.getWidth()-x);			
//		}else{
//			goEast = 0;
//		}
//		if ((x < 60)&&(x > 10)){
//			goWest = 60-x;			
//		}else{
//			goWest = 0;
//		}
//		if ((sv.getHeight()-y < 60)&&(sv.getHeight()-y > 10)){
//			goSouth = 60-(sv.getHeight()-y);			
//		}else{
//			goSouth = 0;
//		}
//		if ((y < 60)&&(y > 10)){
//			goNorth = 60-y;
//			
//		}else{
//			goNorth = 0;
//		}
//		if (goEast!=0 || goWest!=0 || goNorth!=0 || goSouth!=0){
//			if (thread==null){
//				thread = new MoveThread();
//				thread.start();
//				System.out.println("Thread start");			
//			}
//			return true;
//		}else{
//			return false;
//		}
	}
	
	public SceneManager(NSG2 f, int mode) {
		super("", true, true, true, true);
		this.nsg = f;
		this.sceneMode = mode;
		dm = new DataMaintainer();
		sv = new SceneVirtualizer(this);
		
		
		
		if (mode == WIRED_MODE) {
			this.setTitle("Wired scenario");
			parameters = new ParameterDialog(nsg, WIRED_MODE);
		} else {
			this.setTitle("Wireless scenario");
			parameters = new ParameterDialog(nsg, WIRELESS_MODE);
		}

		createMenuBar();
		createToolBar();


		// JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		Box p = Box.createHorizontalBox();

		status.setPreferredSize(new Dimension(200, 20));
		status.setMaximumSize(new Dimension(200, 20));
		status.setMinimumSize(new Dimension(200, 20));

		slider.setPreferredSize(new Dimension(200, 20));
		slider.setMaximumSize(new Dimension(200, 20));
		slider.setMinimumSize(new Dimension(200, 20));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {	
				if (centerX == -1 ){
					centerX =sv.getWidth()/2/sv.scale - sv.shiftX;
					centerY =sv.getHeight()/2/sv.scale - sv.shiftY;
				}
				sv.scale = slider.getValue() / 100.0f;
				sv.shiftX = (int) (sv.getWidth()/2/sv.scale - centerX);
				sv.shiftY = (int) (sv.getHeight()/2/sv.scale - centerY);
				sv.repaint();
			}
		});
		if (mode == WIRELESS_MODE){
			p.add(status);
			p.add(new JLabel("Transmission range"));
			p.add(trBox);
			p.add(new JLabel("Interference range"));
			p.add(irBox);
			p.add(setRange);
			setRange.addActionListener(new ActionListener() {
				boolean showMessage = true;
				public void actionPerformed(ActionEvent e) {
					if (showMessage) {
						showMessage = false;
						JOptionPane.showInternalMessageDialog(sv, "Note that the transmission/interference range setting here only affects the display,\nnot the actual simulation parameters.\nThe actual transmission/interference ranges in NS2 simulator\nare affected by wireless channel parameters\nthat can be setup in parameter mode.\n(press \"parameters\" button and select channel tab for further information.)","Information", JOptionPane.INFORMATION_MESSAGE);
					}
					sv.transmissionRange = Integer.valueOf(trBox.getText());
					sv.interferenceRange = Integer.valueOf(irBox.getText());
					sv.repaint();
				}
			});
			
		}
		p.setBackground(STATUS_LABEL_COLOR);
		p.setOpaque(true);
		slider.setOpaque(false);
		p.add(Box.createHorizontalGlue());
		p.add(new JLabel("Zoom"));
		p.add(slider);
		center.add(p, BorderLayout.SOUTH);
		center.add(sv, BorderLayout.CENTER);
		this.getContentPane().add(center, BorderLayout.CENTER);		
	}
	public void createToolBar(){
		toolbar.setBackground(TOOLBAR_COLOR);
		toolbar.setOpaque(true);
		toolbar.setFloatable(false);
		this.getContentPane().add(toolbar, BorderLayout.NORTH);
		
//		JButton b = new JButton("Hand");
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sv.switchMode(SceneVirtualizer.HAND_MODE);
			}
		});
		toolbar.add(b1);

		// JButton b = new JButton(new ImageIcon("node.gif"));
//		b2 = new JButton("Node");
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sv.switchMode(CREATING_NODE_MODE);
			}
		});
		toolbar.add(b2);

		if (sceneMode == SceneManager.WIRED_MODE) {
			// b = new JButton(new ImageIcon("link.gif"));
//			b = new JButton("Link");
			b3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sv.switchMode(CREATING_LINK_MODE);
				}
			});
			toolbar.add(b3);
		}

		// b = new JButton(new ImageIcon("agent.gif"));
//		b = new JButton("Agent");
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sv.switchMode(SceneVirtualizer.CREATING_AGENT_MODE);
			}
		});
		toolbar.add(b4);

		// b = new JButton(new ImageIcon("app.gif"));
//		b = new JButton("Application");
		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sv.switchMode(SceneVirtualizer.CREATING_APP_MODE);
			}
		});
		toolbar.add(b5);

//		b = new JButton("Parameter");
		b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parameters.setVisible(true);
			}
		});
		toolbar.add(b6);

		// b = new JButton(new ImageIcon("tcl.gif"));
//		b = new JButton("TCL");
		b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nsg.createTCLManager(SceneManager.this);
			}
		});
		toolbar.add(b7);		
	}
	public void createMenuBar(){
		this.setJMenuBar(menubar);
		/*
		 * File menu
		 */
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
//		JMenuItem item = new JMenuItem("Save");
//		item.setMnemonic('S');
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				//TODO
//			}
//		});
//		menu.add(item);

//		item = new JMenuItem("Save as");
//		item.setMnemonic('A');
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
////				TODO
//			}
//		});
//		menu.add(item);

//		item = new JMenuItem("Print");
//		item.setMnemonic('P');
//		item.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
////				TODO
//			}
//		});
//		menu.add(item);
//		
//		menu.addSeparator();
		
		JMenuItem item = new JMenuItem("Close");
		item.setMnemonic('C');
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SceneManager.this.dispose();
			}
		});
		menu.add(item);
		
		menubar.add(menu);
		
		/*
		 * View menu
		 */
		menu = new JMenu("Draw");
		menu.setMnemonic('D');
		JCheckBoxMenuItem checkItem = new JCheckBoxMenuItem("Draw grid");
		checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		checkItem.setSelected(true);
		checkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					drawGrid = true;
				} else {
					drawGrid = false;
				}
				repaint();
			}
		});
		menu.add(checkItem);
		
		checkItem = new JCheckBoxMenuItem("Draw node");
		checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		checkItem.setSelected(true);
		checkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					drawNode = true;
				} else {
					drawNode = false;
				}
				repaint();
			}
		});
		menu.add(checkItem);
		
		if (sceneMode == WIRED_MODE){
			checkItem = new JCheckBoxMenuItem("Draw link");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
			checkItem.setSelected(true);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawLink = true;
					} else {
						drawLink = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
				
			checkItem = new JCheckBoxMenuItem("Draw link detail");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
			checkItem.setSelected(false);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawLinkDetail = true;
					} else {
						drawLinkDetail = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
		}
		
		checkItem = new JCheckBoxMenuItem("Draw agent");
		checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		checkItem.setSelected(true);
		checkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					drawAgent = true;
				} else {
					drawAgent = false;
				}
				repaint();
			}
		});
		menu.add(checkItem);
		
		checkItem = new JCheckBoxMenuItem("Draw agent detail");
		checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		checkItem.setSelected(false);
		checkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					drawAgentDetail = true;
				} else {
					drawAgentDetail = false;
				}
				repaint();
			}
		});
		menu.add(checkItem);
		
		checkItem = new JCheckBoxMenuItem("Draw application");
		checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		checkItem.setSelected(true);
		checkItem.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					drawApp = true;
				} else {
					drawApp = false;
				}
				repaint();
			}
		});
		menu.add(checkItem);
		
		if (sceneMode == WIRELESS_MODE){		
			menu.addSeparator();		
			
			checkItem = new JCheckBoxMenuItem("Draw node location");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
			checkItem.setSelected(true);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawNodeLocation = true;
					} else {
						drawNodeLocation = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
			
			checkItem = new JCheckBoxMenuItem("Draw transmission range");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
			checkItem.setSelected(true);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawSR = true;
					} else {
						drawSR = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
			
			checkItem = new JCheckBoxMenuItem("Draw interference range");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
			checkItem.setSelected(false);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawIR = true;
					} else {
						drawIR = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
			
			checkItem = new JCheckBoxMenuItem("Draw connectivity");
			checkItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
			checkItem.setSelected(true);
			checkItem.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						drawConnectivity = true;
					} else {
						drawConnectivity = false;
					}
					repaint();
				}
			});
			menu.add(checkItem);
		}
		menubar.add(menu);		
		
		/*
		 * Mode menu
		 */
		menu = new JMenu("Mode");
		menu.setMnemonic('M');
		item = new JMenuItem("Hand mode");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sv.switchMode(SceneVirtualizer.HAND_MODE);
			}
		});
		menu.add(item);

		item = new JMenuItem("Node mode");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sv.switchMode(SceneVirtualizer.CREATING_NODE_MODE);
			}
		});
		menu.add(item);

		if (sceneMode == WIRED_MODE){	
			item = new JMenuItem("Link mode");
			item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.CTRL_MASK));
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					sv.switchMode(SceneVirtualizer.CREATING_LINK_MODE);
				}
			});
			menu.add(item);
		}

		item = new JMenuItem("Agent mode");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sv.switchMode(SceneVirtualizer.CREATING_AGENT_MODE);
			}
		});
		menu.add(item);

		item = new JMenuItem("Application mode");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sv.switchMode(SceneVirtualizer.CREATING_APP_MODE);
			}
		});
		menu.add(item);
		
		item = new JMenuItem("Parameter");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				parameters.setVisible(true);
			}
		});
		menu.add(item);

		item = new JMenuItem("Generate TCL");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.CTRL_MASK));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				nsg.createTCLManager(SceneManager.this);
			}
		});
		menu.add(item);
		
		menubar.add(menu);	
	}	
}
