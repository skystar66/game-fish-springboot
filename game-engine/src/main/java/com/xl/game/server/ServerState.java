package com.xl.game.server;

/**
 * 服务器状态
 * 
 * @author xuliang
 * @QQ 359135103 2019年12月20日 下午13:28:38
 */
public enum ServerState {
	/** 正常 */
	NORMAL(0),
	/** 维护 */
	MAINTAIN(1),

	;
	private final int state;

	ServerState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public static ServerState valueOf(int state) {
		for (ServerState serverState : ServerState.values()) {
			if (state == serverState.getState()) {
				return serverState;
			}
		}
		throw new RuntimeException(String.format("服务器状态%d未知", state));
	}

}
