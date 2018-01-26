package nsg.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import nsg.NSG2;
import nsg.NSGParameters;
import nsg.component.Node;
import nsg.component.Waypoint;

public class WayPointMenu extends JDialog implements NSGParameters{
	static final long serialVersionUID = 0;
	
	JButton done = new JButton("Done");
	JButton add = new JButton("Add");
	Node target;
	
	public JTextField startTime = new JTextField("",4); // Mb
	public JTextField destX = new JTextField("",4); // ms
	public JTextField destY= new JTextField("",4); // number of packet
	public JTextField speed= new JTextField("",4); // number of packet

	public DefaultTableModel model;
	public JTable table = new JTable();
	
	TablePopMenu tablePopMenu = new TablePopMenu(); 
	public WayPointMenu(JFrame p){
		super(p,true);		
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds(w/2-250,h/2-300,550,250);		
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
			panel.add(new JLabel("Start time:"));panel.add(startTime);
			panel.add(new JLabel("  Destination:("));panel.add(destX);
			panel.add(new JLabel(", "));panel.add(destY);
			panel.add(new JLabel(")  Speed:"));panel.add(speed);
			panel.add(add);
		getContentPane().add(panel, BorderLayout.NORTH);
		getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
			
		panel = new JPanel();
			panel.add(done);
		getContentPane().add(panel, BorderLayout.SOUTH);
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String[] data = new String[4];
				data[0]=startTime.getText();
				data[1]=destX.getText();
				data[2]=destY.getText();
				data[3]=speed.getText();
				model.addRow(data);
			}
		});
		
		done.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (model.getRowCount()!=0){
					target.waypoints = new ArrayList();
					Waypoint point;
					for(int a = 0; a <model.getRowCount() ; a++){
						point = new Waypoint();
						point.startTime = (String)model.getValueAt(a, 0);
						point.destX = (String)model.getValueAt(a, 1);
						point.destY = (String)model.getValueAt(a, 2);
						point.speed = (String)model.getValueAt(a, 3);
						target.waypoints.add(point);
					}
				}else{
					target.waypoints=null;
				}
				target = null;
				setVisible(false);
			}
		});
		table.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				if (SwingUtilities.isRightMouseButton(e)) {
					tablePopMenu.show(WayPointMenu.this.table, e.getX(), e.getY());
				}
			}
		});
	}
	
	public void setTarget(Node target){
		startTime.setText("");
		destX.setText("");
		destY.setText("");
		speed.setText("");
		this.target=target;
		setTitle("n"+target.id + "'s waypoint setup");
		model = new DefaultTableModel();
		model.addColumn("Start time");
		model.addColumn("X");
		model.addColumn("Y");
		model.addColumn("Speed");
		table.setModel(model);
		if (target.waypoints != null){
			Object[] points = target.waypoints.toArray();
			String[] data = new String[4];
			for (int a = 0 ; a <points.length ; a++){				
				data[0] = ((Waypoint)points[a]).startTime;
				data[1] = ((Waypoint)points[a]).destX;
				data[2] = ((Waypoint)points[a]).destY;
				data[3] = ((Waypoint)points[a]).speed;
				model.addRow(data);
			}
		}
	}
	
	public class TablePopMenu extends JPopupMenu{
		public TablePopMenu(){
			JMenuItem item;
			item = new JMenuItem("Delete");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (table.getSelectedRow() >=0 ){
						model.removeRow(table.getSelectedRow());
					}
				}
			});
			add(item);
		}
	}
}
