#pragma once

class AgentMsg {
	//*考虑一下这个是不是多余了*/

	//交互的AgentMsg，含所有完整的字段
	// 一个标准的agent消息
	//{"cmd":"SEND","id":"pump1","tp":"pump","rq":1,"rp":1,"ct":10,"dt":{"temp":20}}
	// 一个标准的agent消息
	//{"cmd":"GET","id":"pump1","rq":1,"rp":1,"ct":10,"dt":{"cv":true,"lm":0.006}}
public:
	String boardId;
	String boardType;
	String cmdType;
	long compTime;
	long reqId;
	long respId;
	String data;
};