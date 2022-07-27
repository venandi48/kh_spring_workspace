<%@page import="org.springframework.security.core.authority.SimpleGrantedAuthority"%>
<%@page import="java.util.List"%>
<%@page import="com.kh.spring.member.model.dto.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="회원관리" name="title"/>
</jsp:include>
<div class="w-75 mx-auto">
	<button
		type="button"
		class="btn btn-outline-primary"
		data-toggle="modal" data-target="#adminNoticeModal">공지</button>
</div>
<!-- 관리자용 공지 modal -->
<div class="modal fade" id="adminNoticeModal" tabindex="-1" role="dialog" aria-labelledby="#adminNoticeModalLabel" aria-hidden="true">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="adminNoticeModalLabel">Notice</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<form name="adminNoticeFrm">
					<div class="form-group">
						<label for="send-to-name" class="col-form-label">받는이 :</label>
						<input type="text" class="form-control" id="send-to-name" placeholder="생략시 전체 접속회원에게 공지합니다.">
					</div>
					<div class="form-group">
						<label for="message-text" class="col-form-label">메세지 :</label>
						<textarea class="form-control" id="message-text"></textarea>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
				<button type="button" class="btn btn-primary" id="adminNoticeSendBtn">전송</button>
			</div>
		</div>
	</div>
</div>
<script>
document.querySelector("#adminNoticeSendBtn").addEventListener('click', (e) => {
	const to = document.querySelector("#send-to-name").value;
	const msg = document.querySelector("#message-text").value;
	
	if(!msg) return;
	
	const payload = {
		from : '<sec:authentication property="principal.username"/>',	
		to,
		msg,
		time : Date.now(),
		type : 'NOTICE'
	};
	console.log(payload);
	
	const url = to ? `/app/admin/notice/\${to}` : '/app/admin/notice';
	
	stompClient.send(url, null, JSON.stringify(payload));
	
	$(adminNoticeModal).modal('hide'); // 모달 숨기기
	document.adminNoticeFrm.reset(); // 폼초기화
});
</script>
<table class="table w-75 mx-auto">
	<thead>
		<tr>
		  <th scope="col">번호</th>
		  <th scope="col">아이디</th>
		  <th scope="col">이름</th>
		  <th scope="col">권한</th>
		  <th scope="col">수정</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="member" varStatus="vs">
			<tr data-member-id="${member.memberId}">
				<td>${vs.count}</td>
				<td>${member.memberId}</td>
				<td>${member.name}</td>
				<td>
					<% 
						//Member member = (Member) pageContext.getAttribute("member");
						//List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) member.getAuthorities();
					%>
					<input type="checkbox" name="authority" id="role-user-${vs.count}" value="ROLE_USER" <%= hasRole(pageContext, "ROLE_USER") ? "checked" : "" %>/>
					<label for="role-user-${vs.count}">일반</label>
					&nbsp;
					<input type="checkbox" name="authority" id="role-admin-${vs.count}" value="ROLE_ADMIN" <%= hasRole(pageContext, "ROLE_ADMIN") ? "checked" : "" %>/>
					<label for="role-admin-${vs.count}">관리자</label>
				</td>
				<td><button type="button" class="btn btn-outline-primary btn-update-authority" value="${member.memberId}">수정</button></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<script>
document.querySelectorAll(".btn-update-authority").forEach((btn) => {
	btn.addEventListener('click', (e) => {
		const memberId = e.target.value;
		console.log(memberId);
		
		const tr = document.querySelector(`[data-member-id=\${memberId}]`);
		const authorities = [...tr.querySelectorAll("[name=authority]:checked")].map((checkbox) => checkbox.value);
		console.log(authorities);
		
		const headers = {
				"${_csrf.headerName}" : "${_csrf.token}"
		};
		
		const param = {
			memberId,
			authorities
		};
		
		$.ajax({
			url : "${pageContext.request.contextPath}/admin/memberRoleUpdate.do",
			method : "POST",
			headers,
			contentType : 'application/json; charset=utf-8',
			data : JSON.stringify(param),
			success(response) {
				console.log(response);
				const {msg} = response;
				alert(msg);
				location.reload();
			},
			error : console.log
		});
	});
});
</script>

<%!
	/**
	 * 메소드 선언 -> servlet변환시에 메소드 등록
	 */
	 
	private boolean hasRole(PageContext pageContext, String role) {
		Member member = (Member) pageContext.getAttribute("member");
		List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) member.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
	 }

%>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
