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
import nsg.component.Node;

public class NodePanel extends JPanel{
	static final long serialVersionUID = 0;
	
	public JComboBox type = new JComboBox(new String[] {"Single", "Vertical chain", "Horizontal chain", "Grid", "Random"});
	JLabel l1 = new JLabel("   Number of nodes");
	public JTextField vNodes = new JTextField("5",4); 
	JLabel l2 = new JLabel(" x ");
	public JTextField hNodes = new JTextField("5",4); 
	JLabel l3 = new JLabel("   Vertical distance");
	public JTextField vDistance = new JTextField("200",4); //meter
	JLabel l4 = new JLabel("   Horizontal distance");
	public JTextField hDistance = new JTextField("200",4); //meter.
	JLabel l5 = new JLabel("   Range");
	public JTextField xRange = new JTextField("1000",4); //meter.
	JLabel l6 = new JLabel(" x ");
	public JTextField yRange = new JTextField("1000",4); //meter.
	
	Node node;
	public void clearTarget(){
		node=null;
	}
	
	public void setTarget(Node node){
		this.node = node;
	}

	SceneVirtualizer sv;

	public NodePanel(SceneVirtualizer sv){
		this.setLayout(new FlowLayout(	FlowLayout.LEFT, 0,0));
		this.sv = sv;		
		((FlowLayout)(this.getLayout())).setAlignment(FlowLayout.LEFT);
		this.setBackground(NSGParameters.PANEL_COLOR);
		add(type);
		add(l1);
		add(vNodes);
		add(l2);
		add(hNodes);
		add(l3);
		add(vDistance);
		add(l4);
		add(hDistance);
		add(l5);
		add(xRange);
		add(l6);
		add(yRange);
		l1.setVisible(false);
		vNodes.setVisible(false);
		l2.setVisible(false);
		hNodes.setVisible(false);
		l3.setVisible(false);
		vDistance.setVisible(false);
		l4.setVisible(false);
		hDistance.setVisible(false);
		l5.setVisible(false);
		xRange.setVisible(false);
		l6.setVisible(false);
		yRange.setVisible(false);
		type.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				l1.setVisible(false);
				vNodes.setVisible(false);
				l2.setVisible(false);
				hNodes.setVisible(false);
				l3.setVisible(false);
				vDistance.setVisible(false);
				l4.setVisible(false);
				hDistance.setVisible(false);
				l5.setVisible(false);
				xRange.setVisible(false);
				l6.setVisible(false);
				yRange.setVisible(false);
				switch(type.getSelectedIndex()){
				case 0: //Single
//					l1.setVisible(true);
//					vNodes.setVisible(true);
//					l2.setVisible(true);
//					hNodes.setVisible(true);
//					l3.setVisible(true);
//					vDistance.setVisible(true);
//					l4.setVisible(true);
//					hDistance.setVisible(true);
					break;
				case 1: //Vertical chain
					l1.setVisible(true);
					vNodes.setVisible(true);
//					l2.setVisible(true);
//					hNodes.setVisible(true);
					l3.setVisible(true);
					vDistance.setVisible(true);
//					l4.setVisible(true);
//					hDistance.setVisible(true);
					break;
				case 2: //Horizontal chain
					l1.setVisible(true);
//					vNodes.setVisible(true);
//					l2.setVisible(true);
					hNodes.setVisible(true);
//					l3.setVisible(true);
//					vDistance.setVisible(true);
					l4.setVisible(true);
					hDistance.setVisible(true);
					break;
				case 3: //Grid
					l1.setVisible(true);
					vNodes.setVisible(true);
					l2.setVisible(true);
					hNodes.setVisible(true);
					l3.setVisible(true);
					vDistance.setVisible(true);
					l4.setVisible(true);
					hDistance.setVisible(true);
					break;
				case 4: //Random
					l1.setVisible(true);
					vNodes.setVisible(true);
//					l2.setVisible(true);
//					hNodes.setVisible(true);
//					l3.setVisible(true);
//					vDistance.setVisible(true);
//					l4.setVisible(true);
//					hDistance.setVisible(true);
					l5.setVisible(true);
					xRange.setVisible(true);
					l6.setVisible(true);
					yRange.setVisible(true);
					break;
				default:
					System.out.println("NodePanel switch error");
					return;
				}	
			}
		});
		
	}
}
