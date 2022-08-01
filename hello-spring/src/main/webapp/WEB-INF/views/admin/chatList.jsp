<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="채팅관리" name="title"/>
</jsp:include>

<table class="table text-center" id="tbl-chat-list">
	<thead>
		<tr>
			<th scope="col">회원아이디</th>
			<th scope="col">메세지</th>
			<th scope="col">안읽은 메세지수</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="chatLog">
			<tr data-chatroom-id="${chatLog.chatroomId}" data-member-id="${chatLog.memberId}">
				<td>${chatLog.memberId}</td>
				<td class="msg">${chatLog.msg}</td>
				<td class="unread">
					<span class="badge badge-danger unread-count ${chatLog.unreadCount eq 0 ? 'd-none' : '' }">${chatLog.unreadCount}</span>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script>
setTimeout(() => {
	stompClient.subscribe("/app/admin/chatList", (message) => {
		console.log('/app/admin/chatList : ', message);
		const {chatroomId, memberId, msg, type} = JSON.parse(message.body);
		console.log(chatroomId, msg);
		const tbody = document.querySelector("#tbl-chat-list tbody");
		
		let tr = document.querySelector(`tr[data-chatroom-id=\${chatroomId}]`);
		if(tr) {
			const span = tr.querySelector("span.unread-count");
			switch(type) {
			case "LAST_CHECK" :
				span.innerText = 0;
				span.classList.add('d-none');
				break;
			case "CHAT" : 
				// 기존 채팅방 메세지
				tr.querySelector(".msg").innerHTML = msg;
				if(memberId != 'admin') {
					let unreadCount = Number(span.innerText);
					span.innerText = unreadCount + 1;
					span.classList.remove('d-none');
				}
				// 끌어올리기
				tbody.insertAdjacentElement('afterbegin', tr);
				break;
			}
		} else {
			// 신규 채팅방 메세지
			tr = document.createElement('tr');
			tr.dataset.chatroomId = chatroomId;
			tr.dataset.memberId = memberId;
			const td1 = document.createElement('td');
			td1.append(memberId);
			const td2 = document.createElement('td');
			td2.classList.add('msg');
			td2.append(msg);
			const td3 = document.createElement('td');
			td3.classList.add('unread');
			const span = document.createElement('span');
			span.classList.add('badge', 'badge-danger', 'unread-count');
			span.append(1);
			td3.append(span);
			tr.append(td1, td2, td3);
			
			// 끌어올리기
			tbody.insertAdjacentElement('afterbegin', tr);
		}
	});	
}, 500);

document.querySelectorAll("tr[data-chatroom-id]").forEach((tr) => {
	tr.addEventListener('click', (e) => {
		const that = e.target.parentElement;
		const {chatroomId, memberId} = that.dataset;
		console.log(chatroomId, memberId);
		
		const url = `${pageContext.request.contextPath}/admin/\${chatroomId}/\${memberId}/chat.do`;
		const title = chatroomId; // 팝업윈도우 name
		const spec = "width=500px, height=500px";
		open(url, title,spec)
	});
});
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
