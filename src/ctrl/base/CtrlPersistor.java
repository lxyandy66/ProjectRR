package ctrl.base;

import java.io.IOException;

import javax.persistence.EntityManager;
import ctrl.entity.AgentMsg;
import tool.data.processor.DatabaseImporter;

/**继承至DatabaseImporter，新增一个多线程的持久化的接口
 * @author Mr_Li
 *
 */
public class CtrlPersistor extends DatabaseImporter implements Runnable{
	
	
	private AgentMsg persistEntity;
	
	public void setEntityToPersist(AgentMsg e) {
		this.persistEntity=e;
	}
	
	public void closeImporter() {
		try {
			closeConnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(this.persistEntity==null||this==null||!isInit())
			return;
		try {
			persistEntity(this.persistEntity);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void persistTool(EntityManager em) {
		// TODO Auto-generated method stub
		
	}

}
