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

// �� Ŭ������ �ڹٿ����� �������α׷��ֿ��� ������ ������ ��.
// �� Ŭ������ �޼ҵ尡 �ٸ� Ŭ���̾�Ʈ�鿡�� �޽����� ������ ����.
// �������� SocketHandler ����
// WebSocket���� �������� ���μ����� ������ �� �ִ�.
public class SocketHandler extends TextWebSocketHandler{
	
	private final Logger logger = LogManager.getLogger(getClass());
	HttpServletRequest request;
	
	//�����ϴ� ����ڿ� ���� ������ �����ϱ� ���� ����
	private Set<WebSocketSession> sessionSet = new HashSet<WebSocketSession>();
	
	public SocketHandler() {
		super();
		this.logger.info("create SocketHandler instance!");
	}
	
	//Ŭ���̾�Ʈ���� ������ ������ ��� �߻��ϴ� �̺�Ʈ
	// �Ű������� ���� session�� ������ ����� Ŭ���̾�Ʈ�� session��.
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		
		sessionSet.remove("remove session!");
	}
	
	// Ŭ���̾�Ʈ���� ������ �Ͽ� ������ ��� �߻��ϴ� �̺�Ʈ
	// �����ϸ� Ŭ���̾�Ʈ�� session�� sessionSet�� �����Ѵ�.
	@Override 
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		
		sessionSet.add(session);
		this.logger.info("add session!");
	}
	
	//Ŭ���̾�Ʈ���� send�� �̿��ؼ� �޽��� �߼��� �� ��� �̺�Ʈ �ڵ鸵
	// Ŭ���̾�Ʈ���� ������ �޽����� ������ �� �޼ҵ尡 �ڵ� ȣ��
	// �ش� Ŭ���̾�Ʈ�� session��ü ������ session�̶�� �Ķ���ͷ� �´�.
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		
		//session.getAttributes() : HandshakeInterceptor�� beforeHandshake()�޼��忡�� ������ map�� �����´�.
		Map<String, Object> map = session.getAttributes();
		String userId = (String)map.get("userId");
		System.out.println("������ ���̵�: " + userId);
		
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
	
	//���� ���� �߻��� �� ȣ��
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("���� �߻�");
		this.logger.error("web socket error!", exception);
	}
	
	//�޽����� �� ��� �����ؼ� ���� �� �ִ��� ���θ� �����ϴ� �޼ҵ�
	@Override
	public boolean supportsPartialMessages() {
		this.logger.info("call method!");
		
		return false;
	}
}
