#include "Coordinator.h"
#include<ArduinoJson.h>
#include <string.h>
#include"AgentBuffer.h"
#include"AgentBuffer.cpp"
#include<pt.h>
#include"AgentProtocol.h"
#include"AgentProtocol.cpp"





//Coordinator类


Coordinator::Coordinator(String bdId) :CtrlComponent(bdId) {}
// Coordinator::Coordinator() {}
// Coordinator::~Coordinator() {}

Chrono Coordinator::getChrono() { return this->optChrono; }


boolean Coordinator::isConverge() {
	//计算是否收敛
	return true;
}
double Coordinator::compLambda() {
	return 0.01;//计算lamda值
}

String Coordinator::coordinateCalculate() {
	// 计算coordinator的数据
	this->jsonData.clear();
	strBuffer = "";
	jsonData[AgentProtocol::DATA_ISCONV_FROM_JSON] = isConverge();
	jsonData[AgentProtocol::DATA_LAMBDA_FROM_JSON] = compLambda();
	serializeJson(jsonData, strBuffer);
	return strBuffer;
}

String Coordinator::packCoordinatorData() {
	//发送coordinator的信息
	jsonOut.clear();
	jsonOut[AgentProtocol::DEV_ID_FROM_JSON] = this->boardId;
	jsonOut[AgentProtocol::CMD_TYPE_FROM_JSON] = "SEND";//目前统统都是send
	jsonOut[AgentProtocol::REQ_ID_FROM_JSON] = ++this->reqId;
	jsonOut[AgentProtocol::RESP_ID_FROM_JSON] = -1;//AgentProtocol::RESP_ID_FROM_JSON
	int startTime = micros();
	jsonOut[AgentProtocol::DATA_FROM_JSON] = coordinateCalculate();
	jsonOut[AgentProtocol::COMPUTE_TIME_FROM_JSON] = micros() - startTime;//Arduino Uno上，精度为4微秒
	strBuffer = "";//serialize对字符串只能追加
	serializeJson(jsonOut, strBuffer);//用scoop库会报错
	return strBuffer;
}


int Coordinator::threadCoordinate(struct pt* pt) {
	//coordinator线程
	PT_BEGIN(pt);
	while (true) {
		PT_WAIT_UNTIL(pt, this->optChrono.hasPassed(1000));
		this->needBlink = true;
		optChrono.restart();//重置计时器
		sendMessage(packCoordinatorData());//发送数据
		needBlink = false;
	}
	PT_END(pt);//这个宏定义的真是牛皮，宏里面带个大括号
}


void Coordinator::sendMessage(String msg) {
	Serial.println(msg);//通过串口发送消息
}

void Coordinator::debugPrint(String str) {
	Serial.println("In debug: " + str);//通过串口发送消息
}


void Coordinator::addToBufferList(AgentBuffer ab) {
	//暂不进行其他处理
	//留个空出来，如果需要对比如接收到agent类型进行判断则在此处操作
	this->bufferList.updateAgentBuffer(ab);//这个操作直接封装在AgentBufferList中，不在Coordinator进行判断
}

void Coordinator::debugListPrint() {
	Serial.println("list size is " + this->bufferList.listSize());
	for (int i = 0;i < bufferList.listSize();i++) {
		Serial.println(i + " buffer: boardId: " + this->bufferList.getAgentBuffer(i)->getBoardId() +
			" reqId: " + this->bufferList.getAgentBuffer(i)->getReqId());
	}
}