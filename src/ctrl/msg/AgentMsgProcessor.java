package ctrl.msg;

import java.math.BigInteger;
import org.json.JSONObject;

import ctrl.base.MsgProtocal;
import ctrl.entity.AgentMsg;

/**
 * 该类用于持久化Agent发出信息用，从串口或socket处理获取的数据
 * 
 * @author Mr_Li
 *
 */
public class AgentMsgProcessor {

	public static String MSG;

	public static AgentMsg msgToObject(String msg, BigInteger recvTime) {

		// 先检查信息合法化
		if (!AgentMsgProcessor.isLegalMsg(msg))
			throw new IllegalArgumentException("指令不符合格式");

		AgentMsg temp = new AgentMsg();
		
		// 确定CmdType合法
		JSONObject jo = AgentMsgProcessor.getNewestJsonObject(
				AgentMsgProcessor.toJsonObjectArray(msg));

		// 从解析的json中获取数据
		temp.setCmdType(jo.getString(MsgProtocal.CMD_TYPE_FROM_JSON.getValue()));
		if(!MsgProtocal.isValidCmd(temp.getCmdType()))
			throw new IllegalArgumentException("cmd 指令不合法");
		temp.setBoardId(jo.getString(MsgProtocal.DEV_ID_FROM_JSON.getValue()));
		
		temp.setReqId(jo.getInt(MsgProtocal.REQ_ID_FROM_JSON.getValue()));
		temp.setRespId(jo.getInt(MsgProtocal.RESP_ID_FROM_JSON.getValue()));
		temp.setCompTime(jo.getBigInteger(MsgProtocal.COMPUTE_TIME_FROM_JSON.getValue()));
		temp.setData(jo.getString(MsgProtocal.DATA_FROM_JSON.getValue()));
		temp.setRecvTime(recvTime);

		return temp;
	}

	/**
	 * 用于判断是否收到的信息是否符合格式，仅考虑形式，不考虑内容及取值 例如多个JSON? { @code "CMD_TYPE+{JSON content}"
	 * }
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean isLegalMsg(String msg) {
		return true;
	}

	/**
	 * 将剪切后的数据内容转换成Json格式处理 需要考虑多个JSON的情况
	 * 
	 * @param content 收到后去掉"cmdType+"后的内容
	 * @return
	 */
	public static JSONObject[] toJsonObjectArray(String content) {
		int k = getContentJsonNumber(content);
		JSONObject[] joArray = new JSONObject[1];
		joArray[0] = new JSONObject(content);
		return joArray;
	}

	/**
	 * @param joArray
	 * @return
	 */
	public static JSONObject getNewestJsonObject(JSONObject[] joArray) {
		return joArray[0];
	}

	/**
	 * 判断传入的content中有多少JSON字符串 避免同时收到两组数据，即两个JSON字符串的情况
	 * 
	 * @param content
	 * @return
	 */
	public static int getContentJsonNumber(String content) {
		// TODO Auto-generated method stub
		return 1;
	}

	/**
	 * 用于持久化
	 * 
	 * @param id
	 * @param reqId
	 * @param respId
	 * @param temp
	 */
//	public AgentMsg(int id, long reqId, long respId, String data) {
//		this.devId = id;
//		this.reqId = reqId;
//		this.respId = respId;
//		this.data = data;
//	}
//
//	public AgentMsg(JSONObject jo) {
//		this.devId = jo.getInt(MsgProtocal.DEV_ID_FROM_JSON);
//		this.reqId = jo.getLong(MsgProtocal.REQ_ID_FROM_JSON);
//		this.respId = jo.getLong(MsgProtocal.RESP_ID_FROM_JSON);
//		this.data = jo.getString(MsgProtocal.DATA_FROM_JSON);
//	}
//
//	public static AgentMsg toObjectFromJsonStr(String str) {
//		return new AgentMsg(new JSONObject(str));
//
//	}

}
