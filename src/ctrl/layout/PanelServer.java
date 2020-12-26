package ctrl.layout;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;

import ctrl.base.ControlTcpServer;
import ctrl.base.CtrlPersistor;
import ctrl.base.MsgProtocal;
import ctrl.entity.AgentMsg;
import ctrl.msg.AgentMsgProcessor;
import tool.layout.AbstractGridBagPanel;
import tool.mcu.ThreadSerial;
import tool.network.SocketProcessor;
import tool.network.ThreadServer;

public class PanelServer extends AbstractGridBagPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean isBroadcast = true;
	/**
	 * 单点发送时设置的socket选择器
	 * 
	 */
	private int socketSelector = -1;

	private static int agentIdCounter = 0;
	
	
	private CtrlPersistor persistor=new CtrlPersistor();//用于持久化的工具
	private AgentMsg msgBuffer;
	private AgentMsg socketBuffer;

	private JTextField tfCmd = new JTextField();// 命令输入框
	private JButton btSend = new JButton("Send");// 发送命令按钮
	private JButton btShutdown = new JButton("Shutdown");// 关闭按钮，关闭服务器并结束所有连接
	private JButton btStart = new JButton("Start");// 启动服务器按钮
	private JButton btInfo = new JButton("States");// 获取按钮

	private JTextArea taConsole = new JTextArea();// 交互信息显示框
	private JScrollPane panelConsole = new JScrollPane(taConsole);// 交互信息显示的滚动容器

	private JCheckBox cbBroadcast = new JCheckBox("Broadcast", isBroadcast);
	private JLabel textHint = new JLabel(" ");

	private SocketProcessor socketproc = new SocketProcessor() {

		@Override
		public void processIncomeSocket(Socket s) {
			// TODO Auto-generated method stub

		}

		/**
		 * socket直接将收到的信息转发
		 */
		@Override
		public void processIncomeMsg(String str) {
			// TODO Auto-generated method stub
			//先直接转发，如果串口关闭则忽略这条消息
			if(str.trim().equals("")||str==null)
				return;
			if (tsSerial == null) {
				printConsole("Msg: " + str + " failed resend! " + "串口未启动");
				//return;
			}
			try {
				socketBuffer= AgentMsgProcessor.msgToObject(str, BigInteger.valueOf(System.nanoTime()));
				
				tsSerial.sendMessage(str);
				printConsole("已转发至串口: " + str);
				socketBuffer.setResndTime(BigInteger.valueOf(System.nanoTime()));
				persistor.setEntityToPersist(socketBuffer);
				new Thread(persistor).start();
				printConsole("已持久化socket消息: " + JSONObject.wrap(socketBuffer).toString());
				socketBuffer=null;
				// 例如单片机中为"rq"，在实体化后该字段为"reqId"
				// 这里应该在控制台输出实体化过的JSON对象，注意部分成员变量的命名：

			} catch(IllegalArgumentException e) {
				printConsole("Msg: " + str + " 指令不合法! " + e.getMessage());
			}catch (IOException e) {
				// TODO Auto-generated catch block
				printConsole("Msg: " + str + " failed resend! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void printConsole(String str) {
			// TODO Auto-generated method stub
			printText(str);
		}

		@Override
		public void initalProcess(Object t) {
			// TODO Auto-generated method stub
			ThreadServer ts = (ThreadServer) t;
			ts.setId(agentIdCounter++);
			String str = MsgProtocal.CMD_INIT + "+" + MsgProtocal.getInitJsonStr(ts.getId());
			try {
				ts.sendMessage(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				printConsole(str + " send failed!");
				e.printStackTrace();
			}

		}

	};

	private SocketProcessor serialProc = new SocketProcessor() {

		@Override
		public void processIncomeSocket(Socket s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void processIncomeMsg(String str) {
			// TODO Auto-generated method stub
			if(str.trim().equals("")||str==null)
				return;
			
			if (server == null) {
				printConsole("Msg: " + str + " failed resend! " + "服务器未启动");
				//return;
			}
			try {
				msgBuffer= AgentMsgProcessor.msgToObject(str, BigInteger.valueOf(System.currentTimeMillis()));
				server.sendBroardcast(str);
				printConsole("已转发至串口: " + str);
				msgBuffer.setResndTime(BigInteger.valueOf(System.currentTimeMillis()));
				persistor.setEntityToPersist(msgBuffer);
				new Thread(persistor).start();
				printConsole("已持久化: " + JSONObject.wrap(msgBuffer).toString());
				msgBuffer=null;
				// 例如单片机中为"rq"，在实体化后该字段为"reqId"
				// 这里应该在控制台输出实体化过的JSON对象，注意部分成员变量的命名：

			} catch(IllegalArgumentException e) {
				printConsole("Msg: " + str + " 指令不合法! " + e.getMessage());
			}catch (Exception e) {
				// TODO Auto-generated catch block
				printConsole("Msg: " + str + " failed resend! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void printConsole(String str) {
			// TODO Auto-generated method stub
			printText(str);
		}

		@Override
		public void initalProcess(Object ts) {
			// TODO Auto-generated method stub

		}
	};

	private ControlTcpServer server = new ControlTcpServer(8266, socketproc) {
	};
	private Thread trServer = new Thread(server);

	private Thread trSerial;

	private ThreadSerial tsSerial;

	private JComboBox<String> combTarget = new JComboBox<String>(
			server.getClientListName().stream().toArray(String[]::new));



	/**
	 * 目标下拉框的监听器
	 */
	private ItemListener combListen = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if (e.getStateChange() == ItemEvent.SELECTED) {
				socketSelector = combTarget.getSelectedIndex();
				textHint.setText("Set target as" + server.getClientList().get(socketSelector).getFullDescr());
			}
		}
	};

	// private void sendMsgToTarget(String str) {
	//
	// }

	/**
	 * 广播单选框的监听器 设置是否广播并对下拉框的可编辑性进行联动
	 */
	private ItemListener cbListen = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			isBroadcast = cbBroadcast.isSelected();
			textHint.setText("Set as " + (isBroadcast ? "Broadcast" : "Singal") + " mode");
			combTarget.setEnabled(!isBroadcast);
		}
	};

	/**
	 * 直接与TextArea交互
	 * 
	 * @param str
	 */
	private void printText(String str) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				taConsole.append("\n" + str);
			}
		});
	}

	/**
	 * 设置hint的消息内容
	 * 
	 * @param str
	 */
	private void printHint(String str) {
		textHint.setText(str);
	}

	public PanelServer() {
		super();
		constraints.fill = GridBagConstraints.BOTH;// GridBagConstraints.BOTH;//
		constraints.anchor = GridBagConstraints.WEST;
		addComponent(tfCmd, 0, 0, 4, 1);
//		constraints.fill = GridBagConstraints.BOTH;
		addComponent(btSend, 0, 4, 1, 2);
		addComponent(new JLabel("Target:"), 1, 0, 1, 1);
		addComponent(combTarget, 1, 1, 2, 1);
		constraints.anchor = GridBagConstraints.CENTER;
		addComponent(cbBroadcast, 1, 3, 1, 1);
		addComponent(btShutdown, 6, 1, 1, 1);
		addComponent(btInfo, 6, 2, 1, 1);
		addComponent(btStart, 6, 3, 1, 1);
		addComponent(textHint, 7, 0, 4, 1);
		constraints.ipady = 150;
		addComponent(panelConsole, 2, 0, 5, 4);

		// 注册监听器
		btInfo.addActionListener(this);
		btSend.addActionListener(this);
		btShutdown.addActionListener(this);
		btStart.addActionListener(this);
		combTarget.addActionListener(this);
		combTarget.addItemListener(combListen);
		cbBroadcast.addItemListener(cbListen);

	}

//	private void sendMsgToTarget(String str) {
//
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(btShutdown)) {
			try {
				if (trServer == null || !trServer.isAlive()) {
					printHint("服务器已关闭");
					return;
				} else {
					printHint("来自layout: 服务器开始关闭");
					server.shutdown();// 注意这里的shutdown对服务器进行操作
				}

				synchronized (tsSerial) {
					tsSerial.disconnect();
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(btStart)) {
			if (trServer != null && trServer.isAlive()) {
				printHint("服务器已经启动了");
				return;
			}
			trServer.start();// 这里的start对线程操作，线程调用之后自动调用run方法
			printText("服务器已启动!");
			try {
				/* 这里串口描述符号还是不能写死 */
				tsSerial = new ThreadSerial("/dev/cu.usbmodem1413203", 115200, serialProc);// 直接赋值并初始化串口
				trSerial = new Thread(tsSerial);
				trSerial.start();// 执行串口线程
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				serialProc.printConsole(e1.getMessage());
				tsSerial = null;
				e1.printStackTrace();
			}
			printText("串口已启动!");
			EntityManagerFactory emf=Persistence.createEntityManagerFactory("PU_Control");
			persistor.initDataBase(emf);
			printText("持久化程序已启动!");
		} else if (e.getSource().equals(btSend)) {
			try {
				if (isBroadcast) {
					server.sendBroardcast(tfCmd.getText().trim());
					tsSerial.sendMessage(tfCmd.getText().trim());
				} else {
					if (socketSelector == -1) {
						printText("没有选中的目标");
						return;
					} else {
						server.getClientList().get(socketSelector).sendMessage(tfCmd.getText().trim());
					}
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		} else if (e.getSource().equals(btInfo)) {
			printText("Current size is " + server.getClientList().size());
			server.getClientList().forEach(i -> printText(i.getFullDescr()));
		}
//		else if (e.getSource().equals(combTarget)) {
//			// 触发后清除现有的list
//			combTarget.removeAllItems();
//			// lambada表达式对list进行更新
//			combTarget.setModel(new DefaultComboBoxModel<String>(server.getClientListName()));
//			server.getClientList().stream().forEach(i -> {
//				combTarget.addEleme(i.getFullDescr());
//			});
//		}
	}

}
