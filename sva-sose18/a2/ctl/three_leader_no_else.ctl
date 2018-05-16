// Knoten mit ID 3 wird Leader, danach kann aber kein weiterer Knoten Leader werden
EF(Three_Becomes_Leader) & ~(EX(One_Becomes_Leader) & EX(Two_Becomes_Leader));