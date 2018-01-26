package nsg.component;

import nsg.NSGParameters;

public class Link extends NSGComponent implements NSGParameters{

	
	public int id;
	public Node src;
	public Node dst;
	
	public int linkType;
	public int queueType;
	public float capacity; //Mb
	public int propagationDelay; //ms
	public int queueSize; //number of packet


    public Link(int id, Node src, Node dst){
    	super(NSGComponent.LINK);
    	this.id = id;
    	this.src = src;
    	this.dst = dst;
    	this.linkType = DUPLEX_LINK;
    	this.queueType = QUEUE_DROP_TAIL;
    	this.propagationDelay = 20;
    	this.queueSize=50;
    	x = (int)((src.x + dst.x)/2.0f);
    	y = (int)((src.y + dst.y)/2.0f);
    }
}
