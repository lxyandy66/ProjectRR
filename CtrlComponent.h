
#pragma once

#include<Arduino.h>
#include <string.h>
#include <vector>
#include<ArduinoJson.h>
#include <pt.h>
#include <Chrono.h>
#include <LightChrono.h>



class CtrlComponent {

protected:
	unsigned long pinLed;//LED不能少
	boolean needBlink;
	String boardId;
	StaticJsonDocument<AgentProtocol::MSG_SIZE> jsonOut;//发送的buffer 64还不够
	StaticJsonDocument<AgentProtocol::MSG_SIZE> jsonInputBuffer;//接收的buffer //继承的
	StaticJsonDocument<AgentProtocol::DATA_SIZE> jsonData;//采集数据的buffer

public:
	CtrlComponent(String bdId);
	// ~CtrlComponent();
	void setBoardId(String str);
	String getBoardId();
	virtual void sendMessage(String msg) = 0;
	virtual void debugPrint(String str) = 0;//用于开发中debug的显示

	/*LED相关*/
	int CtrlComponent::threadBlinker(struct pt* pt);//blinker线程,通过needBlink异步控制LED
	void setLedPin(unsigned long p);
	unsigned long getLedPin();
	void setNeedBlink(boolean b);
	boolean getNeedBlink();
	void changeLed(boolean b);
	void changeLed();
};