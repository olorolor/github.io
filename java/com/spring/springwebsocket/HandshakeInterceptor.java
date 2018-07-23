package com.spring.springwebsocket;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/* 웹소켓에서 HttpSession 객체를 사용하도록하기 위한 클래스 정의 */
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler, Map<String, Object> map) throws Exception {
		
		// 위의 파리미터 중, attributes에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다.
		System.out.println("Before Handshake");
		// http하에서의 요청을 처리하기위해서 만들었던게 예전 방식이었지만 지금은 http가 아닌 websocket방식으로
		// 통신하기 때문에 http하에서 썼던 객체들을 사용할 수 없어서 그것들을 쓰기 위해서 별도의 작업이 필요하다.
		ServletServerHttpRequest ssreq = (ServletServerHttpRequest)request;
		System.out.println("URI : " + request.getURI());
		HttpServletRequest req = ssreq.getServletRequest();
		//클라이언트에서 보낸 id 받는 첫번째 방법 : String id = (String)req.getParameter("id");
		//클라이언트에서 보낸 id 받는 두번째 방법 : HttpSession에 저장된 이용자의 아이디를 추출하는 경우
		//String id = (String)req.getSession().getAttributes("id");
		String id = "admin";
		map.put("userId", id);
		System.out.println("HttpSession에 저장된 id: " + id);
		
		return super.beforeHandshake(request, response, wsHandler, map);
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler, Exception ex) {
		System.out.println("After Handshake");
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
