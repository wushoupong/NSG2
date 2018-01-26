package nsg;

import java.util.ArrayList;
import java.util.Iterator;

import nsg.component.Agent;
import nsg.component.App;
import nsg.component.Link;
import nsg.component.Node;

public class DataMaintainer {

	public ArrayList nodes;
	public ArrayList links;
	public ArrayList agents;
	public ArrayList apps;
	
	public static Iterator it;
	
	public DataMaintainer() {
		nodes = new ArrayList();
		links = new ArrayList();
		agents = new ArrayList();
		apps = new ArrayList();
	}
	
	public void removeApp(App app) {
		apps.remove(app);
	}
	
	public void removeAgent(Agent agent) {
		
		agents.remove(agent);
		
		checkAgents();
		checkApps();		
//		//Remove Apps before removing the agent
//		Object[] apps = getApps();
//		App app;
//		for (int j=0 ; j<apps.length ; j++){
//			app = (App)apps[j];
//			if (app.agent == agent){
//				removeApp(app);
//				break;
//			}
//		}	
//		
//		Object[] agents2 = getAgents();
//		Agent agent2;
//		for (int i=0;i<agents2.length ; i++){
//			agent2 = (Agent)agents2[i];
//			if (agent2.remoteAgent == agent){
//				agents.remove(agent2);
//			}
//		}
		
		
	}
	
	public void removeLink(Link link) {
		links.remove(link);
	}
	
	public void checkApps() {
		Object[] apps = getApps();
		App app;
		for (int i=0;i<apps.length ; i++){
			app = (App)apps[i];
			if (!agents.contains(app.agent)){
				this.apps.remove(app);
			}
		}
	}
	
	public void checkAgents() {
		Object[] agents = getAgents();
		Agent agent;
		for (int i=0;i<agents.length ; i++){
			agent = (Agent)agents[i];
			if (!nodes.contains(agent.attachedNode)){
				this.agents.remove(agent);
			}
		}
		for (int i=0;i<agents.length ; i++){
			agent = (Agent)agents[i];
			if (!this.agents.contains(agent.remoteAgent)){
				agent.remoteAgent = null;
			}
		}
	}
	
	public void checkLinks() {
		Object[] links = getLinks();
		Link link;
		for (int i=0;i<links.length ; i++){
			link = (Link)links[i];
			if ((!nodes.contains(link.src))||(!nodes.contains(link.dst))){
				this.links.remove(link);
			}
		}
	}

	public void removeNode(Node node) {
		nodes.remove(node);
		
		checkLinks();
		checkAgents();
		checkApps();
//		//Remove links
//		Object[] links = getLinks();
//		Link link;
//		for (int i=0;i<links.length ; i++){
//			link = (Link)links[i];
//			if ((link.dst == node)||(link.src == node)){
//				removeLink(link);
//			}
//		}
//		
//		//Remove agents
//		Object[] agents = getAgents();
//		Agent agent;
//		for (int i=0;i<agents.length ; i++){
//			agent = (Agent)agents[i];
//			if (agent.attachedNode == node){
//				removeAgent(agent);
//			}
//		}
	}	

	
	
	public Node findNode(int x, int y) {
		it = nodes.iterator();
		Node p;
		while (it.hasNext()) {
			p = (Node) it.next();
			if ((Math.abs(p.x - x) < 10) && (Math.abs(p.y - y) < 10)) {
				return p;
			}
		}
		return null;
	}
	
	public Link findLink(int x, int y) {
		it = links.iterator();
		boolean temp;
		Link linkP;
		int x1, x2, y1, y2;
		double d;
		Link tempP = null;
		double tempD = 0;
		while (it.hasNext()) {
			linkP = (Link) it.next();
			temp = linkP.src.x > x ^ linkP.dst.x > x;
			if (!temp) {
				continue;
			}
			temp = linkP.src.y > y ^ linkP.dst.y > y;
			if (!temp) {
				continue;
			}
			x1 = linkP.src.x;
			x2 = linkP.dst.x;
			y1 = linkP.src.y;
			y2 = linkP.dst.y;
			d = Math.abs((y2 - y1) * x + (x1 - x2) * y + (y1 * (x2 - x1) + x1 * (y1 - y2))) / Math.sqrt((y2 - y1) * (y2 - y1) + (x1 - x2) * (x1 - x2));
			if (d < 40) {
				if (tempP == null) {
					tempP = linkP;
					tempD = d;
				} else if (tempD > d) {
					tempP = linkP;
					tempD = d;
				}
			}
		}
		return tempP;
	}

	public Agent findAgent(int x, int y) {
		it = agents.iterator();
		Agent p;
		while (it.hasNext()) {
			p = (Agent) it.next();
			if ((Math.abs(p.x - x) < 10) && (Math.abs(p.y - y) < 10)) {
				return p;
			}
		}
		return null;
	}

	public App findApp(int x, int y) {
		it = apps.iterator();
		App p;
		while (it.hasNext()) {
			p = (App) it.next();
			if ((Math.abs(p.x - x) < 10) && (Math.abs(p.y - y) < 10)) {
				return p;
			}
		}
		return null;
	}	
	
	public Object[] getNodes(){
		return nodes.toArray();
	}
	
	public Object[] getLinks(){
		return links.toArray();
	}
	
	public Object[] getAgents(){
		return agents.toArray();
	}
	
	public Object[] getApps(){
		return apps.toArray();
	}	
}
