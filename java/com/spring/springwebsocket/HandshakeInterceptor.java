package com.spring.springwebsocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/* �����Ͽ��� HttpSession ��ü�� ����ϵ����ϱ� ���� Ŭ���� ���� */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler, Map<String, Object> map) throws Exception {
		
		// ���� �ĸ����� ��, attributes�� ���� �����ϸ� ������ �ڵ鷯 Ŭ������ WebSocketSession�� ���޵ȴ�.
		System.out.println("Before Handshake");
		// http�Ͽ����� ��û�� ó���ϱ����ؼ� ��������� ���� ����̾����� ������ http�� �ƴ� websocket�������
		// ����ϱ� ������ http�Ͽ��� ��� ��ü���� ����� �� ��� �װ͵��� ���� ���ؼ� ������ �۾��� �ʿ��ϴ�.
		ServletServerHttpRequest ssreq = (ServletServerHttpRequest)request;
		System.out.println("URI : " + request.getURI());
		HttpServletRequest req = ssreq.getServletRequest();
		//Ŭ���̾�Ʈ���� ���� id �޴� ù��° ��� : String id = (String)req.getParameter("id");
		//Ŭ���̾�Ʈ���� ���� id �޴� �ι�° ��� : HttpSession�� ����� �̿����� ���̵� �����ϴ� ���
		//String id = (String)req.getSession().getAttributes("id");
		String id = "admin";
		map.put("userId", id);
		System.out.println("HttpSession�� ����� id: " + id);
		
		return super.beforeHandshake(request, response, wsHandler, map);
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler, Exception ex) {
		System.out.println("After Handshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
