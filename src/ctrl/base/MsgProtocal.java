package ctrl.base;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONStringer;

/**
 * 命令的指令类型 获取，设置，分享
 * 
 * @author Mr_Li
 *
 */
public enum MsgProtocal {
	CMD_GET("GET"), CMD_INIT("INIT"),

	CMD_IDNT("IDNT"),
	CMD_SEND("SEND"),


	// 以下字符串均从串口获得的JSON字符串中取出
	DEV_ID_FROM_JSON("id"), 
	REQ_ID_FROM_JSON("rq"), 
	RESP_ID_FROM_JSON("rp"), 
	CMD_TYPE_FROM_JSON("cmd"),
	COMPUTE_TIME_FROM_JSON("ct"),

	DATA_FROM_JSON("dt");

	private String cmdStr;

	private MsgProtocal(String str) {
		this.cmdStr = str;
	}

	public String getValue() {
		return this.cmdStr;
	}

	/**
	 * 处理最初建立连接时Agent的身份设置
	 * 
	 * @param id
	 * @return
	 */
	public static String getInitJsonStr(int id) {
		Map<String, String> initSet = new HashMap<String, String>();
		initSet.put(MsgProtocal.DEV_ID_FROM_JSON.getValue(), String.valueOf(id));
		// 如日后需要可以直接加入相应参数至initSet中
		return JSONStringer.valueToString(initSet);
	}

	public static boolean isValidCmd(String str) {
		for (MsgProtocal msg : MsgProtocal.values()) {
			if (str.equals(msg.getValue()))
				return true;
		}
		return false;
	}
}
