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


class AgentBuffer {
	//需要在coordinator中缓存的数据
	//这个AgentBuffer 意思是Coordinator处理从agent来的buffer，不光是信息
private:
	String boardId;
	// String cmdType;//cmdType不缓存，只考虑解析时处理
	int rqId;//保存是针对第几次的coordinator信号的处理
	double data;//作为实际需要的数据

public:
	AgentBuffer();
	AgentBuffer(String id, int rq, double temp);
	double getData();
	String getBoardId();
	int getReqId();
	void setData(double dt);
	static AgentBuffer msgToAgentBuffer(AgentMsg msg);
	static AgentBuffer msgToAgentBuffer(AgentMsg msg, StaticJsonDocument jd);
};


class AgentBufferList {
private:
	std::vector<AgentBuffer> list;//作为实际缓存的容器
	boolean isLegalIndex(int i);
public:
	AgentBufferList();
	double getDataById(String id);
	int findAgentById(String id);
	void addAgent(String id);
	AgentBuffer* getAgentBuffer(String id);
	AgentBuffer* getAgentBuffer(int index);
	int listSize();
	void add(AgentBuffer ab);
	void updateAgentBuffer(AgentBuffer ab, int index);//更新对应的boardId的buffer
	void updateAgentBuffer(AgentBuffer ab);
	boolean isNewerThanExist(AgentBuffer ab, int index);

};
