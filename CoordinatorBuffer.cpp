#pragma once

#include<Arduino.h>
#include <string.h>
#include "AgentProtocol.h"
#include "BaseBuffer.h"
#include "BaseBuffer.cpp"

double CoordinatorBuffer::getData() { return data; }

void CoordinatorBuffer::setData(double dt) { this->data = dt; }
CoordinatorBuffer::CoordinatorBuffer() {}
CoordinatorBuffer::CoordinatorBuffer(String id, int rq, double temp) :boardId(id), rqId(rq), data(temp) {}


CoordinatorBuffer CoordinatorBuffer::msgToAgentBuffer(AgentMsg msg) {
	//不推荐使用，用动态Json解析对象比较慢
	DynamicJsonDocument jsonTemp(AgentProtocol::DATA_SIZE);
	deserializeJson(jsonTemp, msg.data);
	return AgentBuffer(msg.boardId, msg.reqId, jsonTemp[AgentProtocol::DATA_TEMP_FROM_JSON].as<double>());
}

CoordinatorBuffer CoordinatorBuffer::msgToAgentBuffer(AgentMsg msg, StaticJsonDocument jd) {
	jd.clear();
	deserializeJson(jd, msg.data);
	return AgentBuffer(msg.boardId, msg.reqId, jd[AgentProtocol::DATA_TEMP_FROM_JSON].as<double>());
}