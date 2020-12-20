#pragma once
// #include <uClibc++.h>
#include<Arduino.h>
#include <string.h>
#include "AgentProtocol.h"
#include "BaseBuffer.h"

String BaseBuffer::getBoardId() { return boardId; }
int BaseBuffer::getReqId() { return rqId; }