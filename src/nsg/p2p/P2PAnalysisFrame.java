package nsg.p2p;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class P2PAnalysisFrame extends JInternalFrame {
	File file;
	Container c;

	JTextField filenameField = new JTextField(30);
	JTextField widthField = new JTextField(5);
	JTextField heightField = new JTextField(5);
	JButton start = new JButton("Start");
	JTextArea output1 = new JTextArea();
	JTextArea output2 = new JTextArea();
	JTextArea output3 = new JTextArea();
	JTextArea output4 = new JTextArea();
	JTextArea msgBoard = new JTextArea(6,10);
	
	double[] packetMap = new double[1000000];
	int width;
	int height;
	int peers;
	String message;
	long[] receive;
	long[] drop;
	double[] delay;
	long[] count;
	
	public P2PAnalysisFrame() {
		super("LossAnalysisFrame", true, true, true, true);
		uiInit();
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						start.setEnabled(false);
						init();
						analysis();
						start.setEnabled(true);
					}
				}.start();
			}
		});
	}
	
	public void init(){
		// 參數設定
		width = Integer.parseInt(widthField.getText());
		height = Integer.parseInt(heightField.getText());
		peers = width * height;
		
		delay = new double[peers];
		count = new long[peers];		
		receive = new long[peers];
		drop = new long[peers];
	}

	public void analysis() {
		try {
			// 取得要分析的檔案
			if (filenameField.getText().equals("")) {
				JFileChooser tcljfc = new JFileChooser();
				tcljfc.setDialogTitle("Please select file");
				int m = tcljfc.showSaveDialog(null);
				if (m == JFileChooser.APPROVE_OPTION) {
					file = tcljfc.getSelectedFile();
					filenameField.setText(file.getAbsolutePath());
				} else {
					return;
				}
			} else {
				file = new File(filenameField.getText());
			}
			output1.setText("");
			output2.setText("");
			output3.setText("");
			output4.setText("");
			msgBoard.setText("");
			
			// 開始分析檔案
			analysisFile(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void analysisFile(File f) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String line;
			line = in.readLine();
			while (line != null) {
				if (!analysisLine(line.getBytes())){
					msgBoard.append(line+"\n");
				}
				line = in.readLine();
			}
			// 輸出結果
			dump();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean analysisLine(byte[] line) {
		// 將Line分解成Token
		Vector<String> v = new Vector<String>();
		String tmp = "";
		for (int i = 0; i < line.length; i++) {
			if (line[i] == ' ') {
				if (tmp.equals("")) continue;
				v.add(tmp);
				tmp = "";
				continue;
			}
			tmp = tmp + String.valueOf((char) line[i]);
		}
		if (tmp.length() != 0) v.add(tmp);

		// 將Token變成Array
		String[] token = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			token[i] = v.get(i);
		}

		// 統計資料
		int type = Integer.parseInt(token[0]);	
		int id = Integer.parseInt(token[1]);
		int pid, rcv, drp;
		double time;
		
		switch(type){
		case 1: //for delay
			//Format
			//printf("1 0 snd %d %3.6f \n", sendPackets, (Scheduler::instance().clock()));
			//printf("1 %d otn %d %3.6f \n", id, bufferHead+start+i, (Scheduler::instance().clock()));
			//printf("1 %d rcv %d %3.6f \n", id,tmp->id_, (Scheduler::instance().clock()));
			pid = Integer.parseInt(token[3]);
			time = Double.parseDouble(token[4]);
			if (id == 0) {
				packetMap[pid] = time;
			} else {
				delay[id - 1] = delay[id - 1] + (time - packetMap[pid]);
				count[id - 1] = count[id - 1] + 1;
			}			
			return true;
		case 2://for packet loss
			//Format
			//printf("2 %d %d %d %d %d %d %d\n", id, receivePackets,lossPackets, sendPackets, oldPackets, forcePush, processedPacketNo);
			rcv = Integer.parseInt(token[2]);
			drp = Integer.parseInt(token[3]);

			if (id==0) return false;
			
			receive[id-1] = rcv;
			drop[id-1] = drp;			
			return true;
		}
		return false;
	}
	
	private void dump() {
		//Delay
		for (int i = 0; i < peers; i++) {		
			if (count[i] == 0){
				output1.append("-1\n");				
			}else{
				output1.append(String.valueOf(delay[i] / count[i])+"\n");
			}
		}

		//Avg. delay
		double totalDelay = 0;
		int counter = 0;
		for (int i = 0; i < peers; i++) {
			if (count[i] != 0){
				totalDelay = totalDelay + delay[i] / count[i];
				counter++;
			}
			if ((i+1) % width == 0) {
				output2.append(String.valueOf(totalDelay / counter)+"\n");
				totalDelay = 0;
				counter = 0;
			}
		}		
		
		//Packet loss rate	
		for (int i = 0; i < peers; i++) {
			if (receive[i] == 0){
				output3.append("1\n");				
			}else{
				output3.append(String.valueOf(drop[i] / (double)(drop[i]+receive[i]))+"\n");
			}			
		}

		//Avg. packet loss rate
		double totalRate = 0;
		for (int i = 0; i < peers; i++) {
			if (receive[i] == 0){
				totalRate = totalRate + 1;			
			}else{
				totalRate = totalRate + (drop[i] / (double)(drop[i]+receive[i]));			
			}			

			if ((i+1) % width == 0) {
				output4.append(String.valueOf(totalRate / width)+"\n");
				totalRate = 0;
			}
		}
	}
	
	public void uiInit(){
		c = getContentPane();

		filenameField.setText(System.getProperty("user.home")+"/csvt-sim/");
		widthField.setText("50");
		heightField.setText("20");

		JPanel up = new JPanel();
		up.setLayout(new GridLayout(2,1));
		JPanel up1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		up1.add(new JLabel("Filename"));
		up1.add(filenameField);
		up.add(up1);
		JPanel up2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		up2.add(new JLabel("Width"));
		up2.add(widthField);
		up2.add(new JLabel("Height"));
		up2.add(heightField);
		up2.add(start);
		up.add(up2);
		c.add(up, BorderLayout.NORTH);
		
		JPanel center = new JPanel(new BorderLayout());
		JPanel label = new JPanel(new GridLayout(1,4));
		label.add(new JLabel("Delay"));
		label.add(new JLabel("Avg. delay"));
		label.add(new JLabel("Loss rate"));
		label.add(new JLabel("Avg. loss rate"));
		center.add(label,BorderLayout.NORTH);
		
		JPanel output = new JPanel(new GridLayout(1,4));
		output.add(new JScrollPane(output1));
		output.add(new JScrollPane(output2));
		output.add(new JScrollPane(output3));
		output.add(new JScrollPane(output4));
		center.add(output,BorderLayout.CENTER);
		c.add(center, BorderLayout.CENTER);
				
		JPanel down = new JPanel(new BorderLayout());
		down.add(new JLabel("Message Board"), BorderLayout.NORTH);
		down.add(new JScrollPane(msgBoard), BorderLayout.CENTER);
		c.add(down, BorderLayout.SOUTH);		
	}	
}
