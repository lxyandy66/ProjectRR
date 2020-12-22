#pragma once
// #include <uClibc++.h>
#include<Arduino.h>
#include <string.h>
#include "AgentProtocol.h"
#include "BaseBuffer.h"
//#include "BaseBuffer.cpp"



class CoordinatorBuffer :public BaseBuffer {
	//需要在Agent中缓存的数据
	//这个CoordinatorBuffer 意思是Agent处理从Coordinator来的buffer，不光是信息
private:
	boolean isConv;
	double lambda;//作为实际需要的数据

public:
	CoordinatorBuffer();
	CoordinatorBuffer(String id, int rq, boolean isConv, double lambda);
	static CoordinatorBuffer msgToCoordinatorBuffer(AgentMsg msg);
	static CoordinatorBuffer msgToCoordinatorBuffer(AgentMsg msg, JsonDocument jd);
};