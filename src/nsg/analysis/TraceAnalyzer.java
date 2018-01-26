package nsg.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFileChooser;


class TraceItem{
	String event;
	String layer;
	String type;
	String node;
	long count;
	long totalSize;	
}

public class TraceAnalyzer {
	ArrayList data = new ArrayList();
	
	public static void main(String[] args ){
		File tclFile;
		JFileChooser tcljfc = new JFileChooser();
		try {
			tcljfc.setDialogTitle("Please select file");
			int m = tcljfc.showSaveDialog(null);
			if (m == JFileChooser.APPROVE_OPTION) {
				System.out.println("start analysis");
				tclFile = tcljfc.getSelectedFile();
				TraceAnalyzer a = new TraceAnalyzer();
				a.analysisFile(tclFile);
			} else {
				System.out.println("Exit");
				return;
			}
		} catch (Exception evt) {
			System.out.println(evt.getMessage());
			return;
		}	
		
		
	}
	
    public void analysisFile(File f) {
        try {
            //File f = new File(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f)));
            String line;
            line = in.readLine();
            while (line != null) {
                analysisLine(line.getBytes());
                line = in.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        dump();
    }

    private void analysisLine(byte[] line) {
        //將byte[]變成Vector
        Vector v = new Vector();
        String tmp = "";
        for (int i = 0; i < line.length; i++) {
            if (line[i] == ' ') {
                if (tmp.equals("")) {
                    continue;
                }
                v.add(tmp);
                tmp = "";
                continue;
            }
            tmp = tmp + String.valueOf((char) line[i]);
        }

        //將Vector變成String[]
        String[] token = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            token[i] = (String) v.get(i);
        }

        Iterator it = data.iterator();
        TraceItem p;
   		while (it.hasNext()) {
   			p = (TraceItem) it.next();   			
   			if ((p.event.equals(token[0]))&&(p.node.equals(token[2]))&&(p.layer.equals(token[3]))&&(p.type.equals(token[6]))) {
   				p.count ++;
   				p.totalSize = p.totalSize + Integer.parseInt(token[7]);
   				return;
   			}
   		}
   		
   		TraceItem item = new TraceItem();
   		item.event = token[0];
   		item.node = token[2];
   		item.layer = token[3];
   		item.type = token[6];
   		item.count = 1;
   		item.totalSize = Integer.parseInt(token[7]);
   		data.add(item);
   		return;
    }
    
    private void dump(){
//   		ArrayList data2 = new ArrayList();
//    	
//    	Iterator it = data.iterator();
//        TraceItem p;
//   		while (it.hasNext()) {
//   			p = (TraceItem) it.next();   			
//   			System.out.println(p.event +" "+p.node+" "+p.layer+" "+p.type +" "+ p.count + " " + p.totalSize);
//   		}
//   		
//   		
//   		
//   		ArrayList data2 = new ArrayList();
//   		it = data2.iterator();
//   		while (it.hasNext()) {
//   			p = (TraceItem) it.next();   	
//   			if ((p.event.equals(token[0]))&&(p.node.equals(token[2]))&&(p.layer.equals(token[3]))&&(p.type.equals(token[6]))) {
//
//   			System.out.println(p.event +" "+p.node+" "+p.layer+" "+p.type +" "+ p.count + " " + p.totalSize);
//   		}
    }
	
}
