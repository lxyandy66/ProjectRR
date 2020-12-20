
#pragma once
// #include <uClibc++.h>
#include<Arduino.h>
#include <string.h>
#include <vector>
#include <pt.h>
#include "AgentMsg.h"
#include "AgentProtocol.h"
#include "CtrlComponent.h"
#include <Chrono.h>
#include <LightChrono.h>
#include "AgentBuffer.h"



class Coordinator : public CtrlComponent {//
private:
	// String boardId; //继承的
	Chrono optChrono;//节拍器,coordinator用
	boolean needBlink;//加个flag
	AgentBufferList bufferList;
	String strBuffer;



	long reqId;
public:
	Coordinator(String bdId);
	// Coordinator();
	// ~Coordinator();
	boolean isConverge();//计算是否收敛
	double compLambda();//计算lamda值

	Chrono getChrono();


	String coordinateCalculate();//获取lambda和isConv的Json字符串
	String packCoordinatorData();//包括计算、更新jsonout、序列化

	int threadCoordinate(struct pt* pt); //Coordinate线程,主要线程
	void sendMessage(String msg);//讲道理这个直接保留成虚的到时候在使用的时候继承子类会不会好点
	void debugPrint(String str);//同上 //通过



	void addToBufferList(AgentBuffer ab);
	void debugListPrint();

};
