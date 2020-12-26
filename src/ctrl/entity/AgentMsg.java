package ctrl.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.json.JSONStringer;

import java.math.BigInteger;


/**
 * The persistent class for the MAS_Command database table.
 * 
 */
@Entity
@Table(name="MAS_Command")
@NamedQuery(name="AgentMsg.findAll", query="SELECT a FROM AgentMsg a")
public class AgentMsg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int id;

	@Column(name="board_id")
	private String boardId;
	
	@Column(name="board_type")
	private String boardType;

	@Column(name="cmd_type")
	private String cmdType;

	@Column(name="comp_time")
	private BigInteger compTime;

	private String data;

	@Column(name="recv_time")
	private BigInteger recvTime;
	
	@Column(name="resnd_time")
	private BigInteger resndTime;

	@Column(name="req_id")
	private int reqId;

	@Column(name="resp_id")
	private int respId;

	public AgentMsg() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBoardId() {
		return this.boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}
	
	public String getBoardType() {
		return this.boardType;
	}

	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}

	public String getCmdType() {
		return this.cmdType;
	}

	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}

	public BigInteger getCompTime() {
		return this.compTime;
	}

	public void setCompTime(BigInteger compTime) {
		this.compTime = compTime;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public BigInteger getRecvTime() {
		return this.recvTime;
	}

	public void setRecvTime(BigInteger recvTime) {
		this.recvTime = recvTime;
	}

	/**
	 * @return the resndTime
	 */
	public BigInteger getResndTime() {
		return resndTime;
	}

	/**
	 * @param resndTime the resndTime to set
	 */
	public void setResndTime(BigInteger resndTime) {
		this.resndTime = resndTime;
	}

	public int getReqId() {
		return this.reqId;
	}

	public void setReqId(int reqId) {
		this.reqId = reqId;
	}

	public int getRespId() {
		return this.respId;
	}

	public void setRespId(int respId) {
		this.respId = respId;
	}

	
	public String toJsonString() {
		return JSONStringer.valueToString(this);
	}
}