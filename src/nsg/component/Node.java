package nsg.component;

import java.util.ArrayList;

public class Node  extends NSGComponent{
	static final long serialVersionUID = 0;

	public int id;
	public ArrayList waypoints;
	
	public Node(int id, int x, int y) {
		super(NSGComponent.NODE);
		this.id = id;
		this.x = x;
		this.y = y;
	}
}
