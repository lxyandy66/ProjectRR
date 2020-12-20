#include "AgentBuffer.h"


double AgentBuffer::getData() { return data; }
String AgentBuffer::getBoardId() { return boardId; }
int AgentBuffer::getReqId() { return rqId; }
void AgentBuffer::setData(double dt) { this->data = dt; }
AgentBuffer::AgentBuffer() {}
AgentBuffer::AgentBuffer(String id, int rq, double temp) :boardId(id), rqId(rq), data(temp) {}


AgentBuffer AgentBuffer::msgToAgentBuffer(AgentMsg msg) {
	//不推荐使用，用动态Json解析对象比较慢
	DynamicJsonDocument jsonTemp(AgentProtocol::DATA_SIZE);
	deserializeJson(jsonTemp, msg.data);
	return AgentBuffer(msg.boardId, msg.reqId, jsonTemp[AgentProtocol::DATA_TEMP_FROM_JSON].as<double>());
}

AgentBuffer AgentBuffer::msgToAgentBuffer(AgentMsg msg, StaticJsonDocument jd) {
	jd.clear();
	deserializeJson(jd, msg.data);
	return AgentBuffer(msg.boardId, msg.reqId, jd[AgentProtocol::DATA_TEMP_FROM_JSON].as<double>());
}




/*AgentBufferList类相关*/
AgentBufferList::AgentBufferList() { this->list = std::vector<AgentBuffer>(); }
int AgentBufferList::findAgentById(String id) {
	for (int i = 0;i < this->list.size();i++) {
		if (id == this->list[i].getBoardId()) {//C++里的String比较简单的让人发慌...
			return i;
		}
	}
	return -1;//老样子找不到返回-1
}
AgentBuffer* AgentBufferList::getAgentBuffer(String id) {
	int i = findAgentById(id);
	if (i != -1)
		return &(this->list[i]);
	else
		return nullptr;
	// return i != -1 ? this->list[i] : std::nullptr_t;
}

AgentBuffer* AgentBufferList::getAgentBuffer(int index) {
	if (isLegalIndex(index))
		return &(this->list[index]);
	else
		return nullptr;
}
int AgentBufferList::listSize() { return this->list.size(); }

void AgentBufferList::add(AgentBuffer ab) {
	this->list.push_back(ab);
}

void AgentBufferList::updateAgentBuffer(AgentBuffer ab, int index) {
	//更新对应的boardId的buffer
	//这个接口不应该放出来

	list[index] = ab;
}
void AgentBufferList::updateAgentBuffer(AgentBuffer ab) {
	int i = findAgentById(ab.getBoardId());
	if (i == -1)
		return add(ab);
	else {
		if (isNewerThanExist(ab, i))
			return updateAgentBuffer(ab, i);//如果要新则更新
		else
			return;//如果旧则直接放弃
	}
}

boolean AgentBufferList::isNewerThanExist(AgentBuffer ab, int index) {
	//*这个逻辑要考虑*//
	if (list[index].getReqId() < ab.getReqId())
		return true;
	else
		return false;
}

boolean AgentBufferList::isLegalIndex(int i) {
	return i < list.size();
}