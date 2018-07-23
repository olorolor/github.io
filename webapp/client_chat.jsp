<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>websocket</title>
		<script type="text/javascript">
			var log = function (s) {
				document.getElementById("output").textContent += (s + "\n");
			}
			
			w = new WebSocket("ws://192.168.0.113:8800/springwebsocket/broadcasting?id=guest");
			// 서버에서 handshaking이 성공적으로 끝나면 자동으로 호출되는 메서드
			w.onopen = function () {
				alert("WebSocket Connected!");
			}
			w.onmessage = function(e) {
				log(e.data.toString());
			}
			w.onclose = function(e) {
				log("WebSocket closed!!");
			}
			w.onerror = function(e) {
				log("WebSocket error!!");
			}
			
			window.onload = function() {
				document.getElementById("send_button").onclick = function() {
					if(document.getElementById("nicname").value == "") {
						alert("별명을 입력하세요 : ");
					}else {
						var nicname = document.getElementById("nicname").value;
						var input = document.getElementById("input").value;
						w.send(nicname + "> " + input);
					}
				}
			}
		</script>
	</head>
	<body>
		<input type="text" id="input" placeholder="input message...." size="55">
		<button id="send_button">Send</button>
		대화명<input type="text" id="nicname" placeholder="대화명 입력" size="10">
		<p/>
		<textarea id="output" readonly rows="30" cols="80"></textarea>
	</body>
</html>