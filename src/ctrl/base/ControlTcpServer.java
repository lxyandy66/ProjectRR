package ctrl.base;
import tool.network.ServerManager;
import tool.network.SocketProcessor;

public abstract class ControlTcpServer extends ServerManager{


	public ControlTcpServer() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ControlTcpServer(int port) {
		super();
		// TODO Auto-generated constructor stub
		this.setPort(port);
	}
	
	public ControlTcpServer(int port,SocketProcessor sp) {
		super(sp);
		// TODO Auto-generated constructor stub
		this.setPort(port);
	}
	
}
