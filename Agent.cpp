#pragma once
#include"AgentProtocol.h"
#include"Agent.h"


Agent::Agent(String bdId) :CtrlComponent(bdId) {}


// 一个标准的agent消息
//{"cmd":"SEND","id":"pump1","rq":1,"rp":1,"ct":10,"dt":{"temp":20}}
// 一个标准的agent消息
//{"cmd":"GET","id":"pump1","rq":1,"rp":1,"ct":10,"dt":{"cv":true,"lm":0.006}}
//对应解析的动作
void Agent::parseMsg(String msg) {
	//其实coordinator的动作更简单，每一秒更新一下就行了
	this->jsonInputBuffer.clear();
	deserializeJson(this->jsonInputBuffer, msg);
	// 从debug输出是否收敛
	this->debugPrint(jsonInputBuffer[AgentProtocol::DATA_FROM_JSON][AgentProtocol::DATA_ISCONV_FROM_JSON]);//先输出一下

	//this->debugPrint(typeid(jsonInputBuffer[AgentProtocol::DATA_FROM_JSON][AgentProtocol::DATA_ISCONV_FROM_JSON]).name())//这个方法用不了，不能动态编译
	if (jsonInputBuffer[AgentProtocol::CMD_TYPE_FROM_JSON] == AgentProtocol::CMD_SEND) {
		// 如果接收到的信息类型是SEND

		if (jsonInputBuffer[AgentProtocol::DATA_FROM_JSON][AgentProtocol::DATA_ISCONV_FROM_JSON] == false)
	}
}

void Agent::setWifiModule(DevBoardESP8266 wifi) {
	this->wifiModule = wifi;
}
void Agent::sendMessage(String msg) {
	this->wifiModule.sendContent(msg);
}

void Agent::debugPrint(String str) {
	this->debugSerial->println(str);//仅用于debug
}

String Agent::agentCaculate() {
	// 计算coordinator的数据
	this->jsonData.clear();
	strBuffer = "";
	jsonData[AgentProtocol::DATA_TEMP_FROM_JSON] = compTemp();
	serializeJson(jsonData, strBuffer);
	return strBuffer;
}

String Agent::packCoordinatorData() {
	//发送agent的信息
	jsonOut.clear();
	jsonOut[AgentProtocol::DEV_ID_FROM_JSON] = this->boardId;
	jsonOut[AgentProtocol::CMD_TYPE_FROM_JSON] = "SEND";//目前统统都是send
	jsonOut[AgentProtocol::REQ_ID_FROM_JSON] = ++this->reqId;
	jsonOut[AgentProtocol::RESP_ID_FROM_JSON] = ++this->respId;//AgentProtocol::RESP_ID_FROM_JSON
	int startTime = micros();
	jsonOut[AgentProtocol::DATA_FROM_JSON] = agentCaculate();
	jsonOut[AgentProtocol::COMPUTE_TIME_FROM_JSON] = micros() - startTime;//Arduino Uno上，精度为4微秒
	strBuffer = "";//serialize对字符串只能追加
	serializeJson(jsonOut, strBuffer);
	return strBuffer;
}

double Agent::compTemp() {
	return 26.0;
}