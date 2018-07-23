package com.spring.springwebsocket;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 이 클래스는 자바에서의 소켓프로그래밍에서 서버의 역할을 함.
// 이 클래스의 메소드가 다른 클라이언트들에게 메시지를 보내는 역할.
// 서버단의 SocketHandler 정의
// WebSocket에서 서버단의 프로세스를 정의할 수 있다.
public class SocketHandler extends TextWebSocketHandler{
	
	private final Logger logger = LogManager.getLogger(getClass());
	HttpServletRequest request;
	
	//접속하는 사용자에 대한 세션을 보관하기 위해 정의
	private Set<WebSocketSession> sessionSet = new HashSet<WebSocketSession>();
	
	public SocketHandler() {
		super();
		this.logger.info("create SocketHandler instance!");
	}
	
	//클라이언트에서 연결을 종료할 경우 발생하는 이벤트
	// 매개변수로 오는 session은 접속이 끊기는 클라이언트의 session임.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		
		sessionSet.remove("remove session!");
	}
	
	// 클라이언트에서 접속을 하여 성공할 경우 발생하는 이벤트
	// 성공하면 클라이언트의 session을 sessionSet에 저장한다.
	@Override 
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		
		sessionSet.add(session);
		this.logger.info("add session!");
	}
	
	//클라이언트에서 send를 이용해서 메시지 발송을 한 경우 이벤트 핸들링
	// 클라이언트에서 서버로 메시지를 보내면 이 메소드가 자동 호출
	// 해당 클라이언트의 session객체 정보가 session이라는 파라미터로 온다.
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		
		//session.getAttributes() : HandshakeInterceptor의 beforeHandshake()메서드에서 저장한 map을 가져온다.
		Map<String, Object> map = session.getAttributes();
		String userId = (String)map.get("userId");
		System.out.println("전송자 아이디: " + userId);
		
		for(WebSocketSession client_session : this.sessionSet) {
			if(client_session.isOpen()) {
				try {
					client_session.sendMessage(message);
				}catch(Exception ignored) {
					
					this.logger.error("fail to send message!", ignored);
				}
			}
		}
	}
	
	//전송 에러 발생할 때 호출
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("에러 발생");
		this.logger.error("web socket error!", exception);
	}
	
	//메시지가 긴 경우 분할해서 보낼 수 있는지 여부를 결정하는 메소드
	@Override
	public boolean supportsPartialMessages() {
		this.logger.info("call method!");
		
		return false;
	}
}
