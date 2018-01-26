package nsg.component;


public class NSGComponent {
	public static final int NODE = 1;
	public static final int LINK = 2;
	public static final int AGENT = 3;
	public static final int APP = 4;

	public int x;
	public int y;
	public int type;
	
	public NSGComponent(int type) {
		this.type = type;
	}
}
