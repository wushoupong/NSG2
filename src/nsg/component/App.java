package nsg.component;

public class App extends NSGComponent{	
	
	public int id;
	public Agent agent;
	
	public int appType;
	public int packetSize;
	public float rate;
	public String random;
	
	public float startTime;
	public float stopTime;
	
	 public App(int id, int x, int y, Agent agent){
	    	super(NSGComponent.APP);
	    	this.id = id;
	    	this.x = x;
			this.y = y;
			this.agent = agent;
	    }
}
