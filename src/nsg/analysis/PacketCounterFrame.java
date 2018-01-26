package nsg.analysis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class PacketCounterFrame extends JInternalFrame {
	Container c;

	JButton b = new JButton("Analyze trace file");

	JTextPane message = new JTextPane();
//	 AttributeSet
	SimpleAttributeSet outputAttr = new SimpleAttributeSet();
//	SimpleAttributeSet tclAttr = new SimpleAttributeSet();
//	SimpleAttributeSet noteAttr = new SimpleAttributeSet();
	Document doc=message.getDocument();

	File traceFile;

	JFileChooser tracejfc = new JFileChooser();

	ArrayList data;

	public PacketCounterFrame() {
		super("PacketCounterFrame", true, true, true, true);
		StyleConstants.setFontSize(outputAttr, 14);
		StyleConstants.setFontFamily(outputAttr, "Courier New");
		StyleConstants.setBold(outputAttr, true);
		StyleConstants.setForeground(outputAttr, Color.BLACK);
		
		
		c = getContentPane();
		JPanel up = new JPanel();
		c.add(up, BorderLayout.NORTH);
		c.add(new JScrollPane(message), BorderLayout.CENTER);

		up.add(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tracejfc.setDialogTitle("Please select file");
					int m = tracejfc.showOpenDialog(null);
					if (m == JFileChooser.APPROVE_OPTION) {
						appendNote("start analysis\n");
						traceFile = tracejfc.getSelectedFile();
						analysisFile(traceFile);
					} else {
						//appendNote("Exit");
						return;
					}
				} catch (Exception evt) {
					System.out.println(evt.getMessage());
					return;
				}
			}
		});
	}

	public void analysisFile(File f) {
		data = new ArrayList();
		try {
			// File f = new File(path);
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
		dump(data);
	}

	private void analysisLine(byte[] line) {

		// 將byte[]變成Vector
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

		// 將Vector變成String[]
		String[] token = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			token[i] = (String) v.get(i);
		}

		Iterator it = data.iterator();
		TraceItem p;
		while (it.hasNext()) {
			p = (TraceItem) it.next();
			if ((p.event.equals(token[0])) && (p.node.equals(token[2]))
					&& (p.layer.equals(token[3])) && (p.type.equals(token[6]))) {
				p.count++;
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

	private void dump(ArrayList data) {

		Iterator it = data.iterator();
		TraceItem p;
		while (it.hasNext()) {
			p = (TraceItem) it.next();
			appendNote(p.event + " " + p.node + " " + p.layer + " "	+ p.type + " " + p.count + " " + p.totalSize+"\n");
		}

//		ArrayList data2 = new ArrayList();
//		it = data2.iterator();
//		while (it.hasNext()) {
//			p = (TraceItem) it.next();
//			if ((p.event.equals(token[0])) && (p.node.equals(token[2]))
//					&& (p.layer.equals(token[3])) && (p.type.equals(token[6]))) {
//
//				System.out.println(p.event + " " + p.node + " " + p.layer + " "
//						+ p.type + " " + p.count + " " + p.totalSize);
//			}
//		}

	}
	
	public void appendNote(String note) {
		try {
			doc.insertString(message.getCaretPosition(), note, outputAttr);
		} catch (Exception evt) {
			evt.printStackTrace();
		}
	}
}
