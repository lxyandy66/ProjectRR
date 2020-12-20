#pragma once
// #include <uClibc++.h>
#include<Arduino.h>
#include <string.h>
#include <vector>
#include"CtrlComponent.h"
#include"DevBoardESP8266.h"
#include"AgentProtocol.h"


class Agent : public CtrlComponent {
private:
	DevBoardESP8266 wifiModule;
	Stream* debugSerial;
	long reqId;
	long respId;
	AgentBuffer ab;
public:
	Agent(String bdId);
	// void parseMsg(String msg);//处理交互的数据
	double compData();
	void setWifiModule(DevBoardESP8266 wifi);
	void sendMessage(String msg);
	void debugPrint(String str);
	String agentCaculate();//打包data的内容到json对象 //考虑这一部分也要继承一下
	double compTemp();//计算Agent的内容 这个需要核实一下，/*agent有没有分类的*/

	/*多线程部分*/
	int threadAgent(struct pt* pt); //Agent线程,主要线程
};