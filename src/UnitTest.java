
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.json.JSONObject;
import ctrl.entity.AgentMsg;
import ctrl.msg.AgentMsgProcessor;
import tool.data.processor.DatabaseImporter;
import tool.mcu.ThreadSerial;
import tool.network.SocketProcessor;

public class UnitTest {

	static SocketProcessor testSp = new SocketProcessor() {

		@Override
		public void processIncomeSocket(Socket s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void processIncomeMsg(String str) {
			// TODO Auto-generated method stub

		}

		@Override
		public void printConsole(String str) {
			// TODO Auto-generated method stub
			System.out.println(str);
		}

		@Override
		public void initalProcess(Object ts) {
			// TODO Auto-generated method stub
			try {
				((ThreadSerial) ts).sendMessage("SET+LED");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.out.println("LED has changed!");
			}

		}
	};

//	Runnable rb=new Runnable() {
//		int i=0;
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			try {
//				while(i<100) {
//					System.out.println(i++);
//					Thread.sleep(1000);
//				}
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	};
//	new Thread(rb).start();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		String jsonTest="{ \"name\":\"runoob\", \"alexa\":10000, \"site\":null }";

		Scanner in = new Scanner(System.in);
//		JSONObject jo=new JSONObject(in.nextLine());
//		System.out.println("success jsonlized: "+jo.toString());
//		System.out.println("init: "+MsgProtocal.getInitJsonStr(123));
		if(false){//持久化测试部分
			DatabaseImporter importer = new DatabaseImporter() {

				@Override
				public void persistTool(EntityManager em) {
					// TODO Auto-generated method stub

				}
			};
			ArrayList<AgentMsg> msgArray = new ArrayList<>();
			AgentMsg msg = AgentMsgProcessor.msgToObject(
					"{\"cmd\":\"TEST\",\"id\":\"pump1\",\"rq\":1,\"rp\":1,\"ct\":10,\"dt\":{\"temp\":20}}",
					BigInteger.valueOf(System.currentTimeMillis()));
			msg.setResndTime(BigInteger.valueOf(System.currentTimeMillis()));
			msgArray.add(msg);

			try {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Control");
				importer.initDataBase(emf);

				importer.persistEntity(msgArray);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		


//		SerialMessenger sm=new SerialMessenger();
//		String cmd=null;
//		try {
//			sm.initSerialPort("/dev/cu.usbmodem1413203", 9600);
//			System.out.println("init success");
//			ThreadSerial ts=new ThreadSerial(sm, testSp);
//			System.out.println("Object construct success");
//			new Thread(ts).run();
//			System.out.println("Threadrun success");
//			//while(in.hasNext()) {
//				cmd="SET+LED";//in.nextLine();
//				System.out.println(cmd+ " has been received");
//				ts.sendMessage(cmd);
//				System.out.println(cmd+ " has been sent");
//			//}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
