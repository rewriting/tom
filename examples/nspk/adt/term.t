%typeterm String{
  implement { String}
  get_fun_sym(t) {t}
  cmp_fun_sym(s1,s2) { s1.equals(s2)}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%typeterm Integer{
  implement { Integer}
  get_fun_sym(t) {t}
  cmp_fun_sym(s1,s2) { s1.equals(s2)}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%typeterm Double{
  implement { Double}
  get_fun_sym(t) {t}
  cmp_fun_sym(s1,s2) { s1.equals(s2)}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%typeterm AgentId{
  implement { AgentId}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op AgentId a {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isA()}
  make() { getTermFactory().makeAgentId_A()}
}

%op AgentId b {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isB()}
  make() { getTermFactory().makeAgentId_B()}
}

%op AgentId c {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isC()}
  make() { getTermFactory().makeAgentId_C()}
}

%op AgentId d {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isD()}
  make() { getTermFactory().makeAgentId_D()}
}

%op AgentId e {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isE()}
  make() { getTermFactory().makeAgentId_E()}
}

%op AgentId alice {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isAlice()}
  make() { getTermFactory().makeAgentId_Alice()}
}

%op AgentId bob {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isBob()}
  make() { getTermFactory().makeAgentId_Bob()}
}

%op AgentId devil {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isDevil()}
  make() { getTermFactory().makeAgentId_Devil()}
}

%typeterm SWC{
  implement { SWC}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op SWC SLEEP {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isSLEEP()}
  make() { getTermFactory().makeSWC_SLEEP()}
}

%op SWC WAIT {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isWAIT()}
  make() { getTermFactory().makeSWC_WAIT()}
}

%op SWC COMMIT {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isCOMMIT()}
  make() { getTermFactory().makeSWC_COMMIT()}
}

%typeterm Key{
  implement { Key}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Key K(id:AgentId) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isK()}
  get_slot(id,t) { t.getId()}
  make(t0) { getTermFactory().makeKey_K(t0)}
}

%typeterm Nonce{
  implement { Nonce}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Nonce N(id1:AgentId, id2:AgentId) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isN()}
  get_slot(id1,t) { t.getId1()}
  get_slot(id2,t) { t.getId2()}
  make(t0, t1) { getTermFactory().makeNonce_N(t0, t1)}
}

%op Nonce DN {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isDN()}
  make() { getTermFactory().makeNonce_DN()}
}

%typeterm Address{
  implement { Address}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Address A(id:AgentId) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isA()}
  get_slot(id,t) { t.getId()}
  make(t0) { getTermFactory().makeAddress_A(t0)}
}

%op Address DA {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isDA()}
  make() { getTermFactory().makeAddress_DA()}
}

%typeterm Agent{
  implement { Agent}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Agent agent(id:AgentId, state:SWC, nonce:Nonce) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isAgent()}
  get_slot(id,t) { t.getId()}
  get_slot(state,t) { t.getState()}
  get_slot(nonce,t) { t.getNonce()}
  make(t0, t1, t2) { getTermFactory().makeAgent_Agent(t0, t1, t2)}
}

%typeterm Message{
  implement { Message}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Message msg(src:AgentId, dst:AgentId, key:Key, nonce1:Nonce, nonce2:Nonce, adr:Address) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isMsg()}
  get_slot(src,t) { t.getSrc()}
  get_slot(dst,t) { t.getDst()}
  get_slot(key,t) { t.getKey()}
  get_slot(nonce1,t) { t.getNonce1()}
  get_slot(nonce2,t) { t.getNonce2()}
  get_slot(adr,t) { t.getAdr()}
  make(t0, t1, t2, t3, t4, t5) { getTermFactory().makeMessage_Msg(t0, t1, t2, t3, t4, t5)}
}

%typeterm Intruder{
  implement { Intruder}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op Intruder intruder(id:AgentId, listNonce:ListNonce, listMessage:ListMessage) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isIntruder()}
  get_slot(id,t) { t.getId()}
  get_slot(listNonce,t) { t.getListNonce()}
  get_slot(listMessage,t) { t.getListMessage()}
  make(t0, t1, t2) { getTermFactory().makeIntruder_Intruder(t0, t1, t2)}
}

%typeterm State{
  implement { State}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op State state(senders:ATerm, receivers:ATerm, intruder:Intruder, network:ListMessage) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isState()}
  get_slot(senders,t) { (ATermList)t.getSenders()}
  get_slot(receivers,t) { (ATermList)t.getReceivers()}
  get_slot(intruder,t) { t.getIntruder()}
  get_slot(network,t) { t.getNetwork()}
  make(t0, t1, t2, t3) { getTermFactory().makeState_State(t0, t1, t2, t3)}
}

%op State ATTACK {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isATTACK()}
  make() { getTermFactory().makeState_ATTACK()}
}

%op State ERROR {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isERROR()}
  make() { getTermFactory().makeState_ERROR()}
}

%typeterm ListMessage{
  implement { ListMessage}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op ListMessage nilListMessage {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isNilListMessage()}
  make() { getTermFactory().makeListMessage_NilListMessage()}
}

%op ListMessage consListMessage(headListMessage:Message, tailListMessage:ListMessage) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isConsListMessage()}
  get_slot(headListMessage,t) { t.getHeadListMessage()}
  get_slot(tailListMessage,t) { t.getTailListMessage()}
  make(t0, t1) { getTermFactory().makeListMessage_ConsListMessage(t0, t1)}
}

%typeterm ListNonce{
  implement { ListNonce}
  get_fun_sym(t) {null}
  cmp_fun_sym(s1,s2) { false}
  get_subterm(t,n) {null}
  equals(t1,t2) {t1.equals(t2)}
}

%op ListNonce nilListNonce {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isNilListNonce()}
  make() { getTermFactory().makeListNonce_NilListNonce()}
}

%op ListNonce consListNonce(headListNonce:Nonce, tailListNonce:ListNonce) {
  fsym {}
  is_fsym(t) { (t!= null) &&t.isConsListNonce()}
  get_slot(headListNonce,t) { t.getHeadListNonce()}
  get_slot(tailListNonce,t) { t.getTailListNonce()}
  make(t0, t1) { getTermFactory().makeListNonce_ConsListNonce(t0, t1)}
}

