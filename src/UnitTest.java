
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.json.JSONObject;
import ctrl.entity.AgentMsg;
import ctrl.msg.AgentMsgProcessor;
import tool.data.processor.DatabaseImporter;
import tool.mcu.ThreadSerial;
import tool.network.SocketProcessor;

public class UnitTest {

	public static void print(AgentMsg m) {
		System.out.println("in function type: " + m.getBoardType());
	}
	
	class ThreadTest implements Runnable{
		

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	static class Container{
		AgentMsg msg;
		public Container(AgentMsg msg) {
				this.msg = msg;
		}
		public void print() {System.out.println("in container: "+msg.getBoardType());}
		
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		AgentMsg msg = AgentMsgProcessor.msgToObject(
				"{\"id\":\"test\",\"tp\":\"co\",\"cmd\":\"SEND\",\"rq\":3,\"rp\":-1,\"dt\":\"{\\\"cv\\\":true,\\\"lm\\\":0.01}\",\"ct\":61}",
				BigInteger.TWO);
		Container ct=new Container(msg.clone());
		ct.print();
		msg.setBoardType("hahah");//很有意思，如果clone了这么算来都不影响
		ct.print();
		msg=null;//null不会影响，即使不clone
		ct.print();
//		System.out.println("msg type: "+msg.getBoardType());
//		AgentMsg msgCopy=msg.clone();
		
//
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Control");
//		
//		
//		System.out.println("dududu");//似乎生成EL info消息是在获取em时
//		EntityManager em= emf.createEntityManager();
//		EntityTransaction tr=em.getTransaction();
//		em.persist(msg);
//		em.flush();//commit之后isActive()就是false
//		msg.setBoardType("hahah");
//		em.persist(msg);
//		em.flush();
//		em.getTransaction().commit();
//		System.out.println(em.getTransaction().isActive());
		

	}

}
