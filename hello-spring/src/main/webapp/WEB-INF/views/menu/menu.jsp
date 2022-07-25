<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:requestEncoding value="utf-8"/>

<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Menu REST API" name="title"></jsp:param>
</jsp:include>

	<style>
	.menu-test, .result {
		width : 75%;
		margin : 0 auto; 
	}
	</style>
	<div id="menu-container" class="text-center">
		<!-- 1.GET /menu-->
        <div class="menu-test">
            <h4>전체메뉴조회(GET)</h4>
            <input type="button" class="btn btn-block btn-outline-success btn-send" id="btn-menus" value="전송" />
        </div>
        <div class="result" id="menus-result"></div>
        <script>
        const MENU_URL = 'http://localhost:10000/springboot'
        
        /**
         * origin : protocol + host + port
         * 	- protocol : http:// https://
         * 	- host : domain, ip
         * 	- port : 9090
         *
         * SOP(Same Origin Policy)
         * 	- 동일근원정책
         *  - 웹브라우저에서 비동기요청(ajax)을 보낼 때 반드시 같은 origin으로만 요청을 보낼 수 있다.
         * 	- http://localhost:9090 -> http://localhost:1000 요청 보낼 수 없음.
         *
         *
         * CORS Policy(Cross Origin Resource Sharing Policy)
         *  - sop를 넘어서는 ajax요청을 위한 정책
         * 	- Access-Control-Allow-Origin 헤더에 해당 origin이 명시되어있어야 한다.
         *
         * CORS처리
         * 1. Access-Control-Allow-Origin : 헤더에 origin 명시
         * 2. jsonp : 보안취약점 발견으로 사용하지 않는걸 추천
         * 3. proxy server
         *
         */
        
        document.querySelector("#btn-menus").addEventListener('click', (e) => {
        	$.ajax({
        		url : `\${MENU_URL}/menu`,
        		method : "GET",
        		success(data) {
        			console.log(data);
        			renderResultTable(data, "menus-result");
        		},
        		error : console.log
        	});
        });
        
        const renderResultTable = (data, target) => {
        	const container = document.querySelector("#" + target);
        	const table = document.createElement("table");
        	table.classList.add('table');
        	const header = document.createElement("tr");
        	const td1 = document.createElement("td");
        	td1.innerHTML = "번호";
        	const td2 = document.createElement("td");
        	td2.innerHTML = "음식점";
        	const td3 = document.createElement("td");
        	td3.innerHTML = "메뉴";
        	const td4 = document.createElement("td");
        	td4.classList.add('text-right');
        	td4.innerHTML = "가격";
        	const td5 = document.createElement("td");
        	td5.innerHTML = "타입";
        	const td6 = document.createElement("td");
        	td6.innerHTML = "맛";
        	header.append(td1, td2, td3, td4, td5, td6);
        	table.append(header);
        	
        	if(data.length) {
        		data.forEach((menu) => {
        			const {id, restaurant, name, price, type, taste} = menu;
        			console.log(id, restaurant, name, price, type, taste);
	        		const tr = document.createElement("tr");
	        		const td1 = document.createElement("td");
	            	td1.innerHTML = id;
	            	const td2 = document.createElement("td");
	            	td2.innerHTML = restaurant;
	            	const td3 = document.createElement("td");
	            	td3.innerHTML = name;
	            	const td4 = document.createElement("td");
	            	td4.classList.add('text-right');
	            	td4.innerHTML = "￦" + price.toLocaleString();
	            	const td5 = document.createElement("td");
	            	const span5 = document.createElement("span");
	            	span5.classList.add("badge");
	            	switch(type) {
	            	case 'kr' : span5.classList.add("badge-primary"); break;
	            	case 'ch' : span5.classList.add("badge-secondary"); break;
	            	case 'jp' : span5.classList.add("badge-info"); break;
	            	}
	            	span5.innerHTML = type;
	            	td5.append(span5);
	            	const td6 = document.createElement("td");
	            	const span6 = document.createElement("span");
	            	span6.classList.add("badge");
	            	switch(taste) {
	            	case 'hot' : span6.classList.add("badge-danger"); break;
	            	case 'mild' : span6.classList.add("badge-warning"); break;
	            	}
	            	span6.innerHTML = taste;
	            	td6.append(span6);
	            	tr.append(td1, td2, td3, td4, td5, td6);
	            	table.append(tr);
        		});
        	} else {
        		const tr = document.createElement("tr");
        		const td = document.createElement("td");
        		td.colSpan = 6;
        		td.innerHTML = "검색된 결과가 없습니다.";
        		tr.append(td);
        		table.append(tr);
        	}
        	container.innerHTML = ""; // 초기화
        	container.append(table);
        };
        </script>
        
		<!-- 2.GET /menu/type/kr /menu/type/ch /menu/type/jp 타입별 조회 -->
		<div class="menu-test">
			<h4>메뉴 타입별 조회(GET)</h4>
			<select class="form-control" id="typeSelector">
				<option value="" disabled selected>음식타입선택</option>
				<option value="kr">한식</option>
				<option value="ch">중식</option>
				<option value="jp">일식</option>
			</select>
		</div>
		<div class="result" id="menuType-result"></div>
		<script>
		document.querySelector("#typeSelector").addEventListener('change', (e) => {
        	const selected = e.target.value;
        	console.log(selected);
        	
        	$.ajax({
        		url : `${pageContext.request.contextPath}/menu/selectMenuByType.do`,
        		data : { type : selected },
        		method : "GET",
        		success(data) {
        			console.log(data);
        			renderResultTable(data, "menuType-result");
        		},
        		error : console.log
        	});
        });
		</script>
        
		<!-- 3.GET menu/taste/mild menu/taste/hot -->
		<div class="menu-test">
			<h4>메뉴 맛별 조회(GET)</h4>
			<div class="form-check form-check-inline">
				<input type="radio" class="form-check-input" name="taste" id="get-hot" value="hot">
				<label for="get-hot" class="form-check-label">매운맛</label>&nbsp;
				<input type="radio" class="form-check-input" name="taste" id="get-mild" value="mild">
				<label for="get-mild" class="form-check-label">순한맛</label>
			</div>
		</div>
		<div class="result" id="menuTaste-result"></div>
		<script>
		document.querySelectorAll("[name=taste]").forEach((radio) => {
			radio.addEventListener('change', (e) => {
				const checked = e.target.value; 

				$.ajax({
					url : `${pageContext.request.contextPath}/menu/selectMenuByTaste.do`,
					data : { taste : checked },
					method : "GET",
					success(data) {
						console.log(data);
						renderResultTable(data, "menuTaste-result");
					},
					error : console.log
				});
			});
		});
		</script>
        
		<!-- POST /menu -->
		<div class="menu-test">
			<h4>메뉴 등록하기(POST)</h4>
			<form name="menuEnrollFrm">
				<input type="text" name="restaurant" placeholder="음식점" class="form-control" required/>
				<br />
				<input type="text" name="name" placeholder="메뉴" class="form-control" required/>
				<br />
				<input type="number" name="price" placeholder="가격" class="form-control" required/>
				<br />
				<div class="form-check form-check-inline">
					<input type="radio" class="form-check-input" name="type" id="post-kr" value="kr" checked>
					<label for="post-kr" class="form-check-label">한식</label>&nbsp;
					<input type="radio" class="form-check-input" name="type" id="post-ch" value="ch">
					<label for="post-ch" class="form-check-label">중식</label>&nbsp;
					<input type="radio" class="form-check-input" name="type" id="post-jp" value="jp">
					<label for="post-jp" class="form-check-label">일식</label>&nbsp;
				</div>
				<br />
				<div class="form-check form-check-inline">
					<input type="radio" class="form-check-input" name="taste" id="post-hot" value="hot" checked>
					<label for="post-hot" class="form-check-label">매운맛</label>&nbsp;
					<input type="radio" class="form-check-input" name="taste" id="post-mild" value="mild">
					<label for="post-mild" class="form-check-label">순한맛</label>
				</div>
				<br />
				<input type="submit" class="btn btn-block btn-outline-success btn-send" value="등록" >
			</form>
		</div>
		<script>
		document.menuEnrollFrm.addEventListener('submit', (e) => {
			e.preventDefault();

			const frm = e.target;
			/*
			const menu = {
				restaurant : frm.restaurant.value,
				name : frm.name.value,
				price : frm.price.value,
				type : frm.type.value,
				taste : frm.taste.value
			}
			*/
			const menu = {};
			const frmData = new FormData(frm);
			frmData.forEach((v, key) => { menu[key] = v});

			console.log(menu);
			console.log(JSON.stringify(menu));
			
			const headers = {
				'${_csrf.headerName}' : '${_csrf.token}'
			}

			// POST /menu 등록 insert
			$.ajax({
				url : `${pageContext.request.contextPath}/menu/insertMenu.do`,
				method : "POST",
				headers,
				data : JSON.stringify(menu),
				contentType : 'application/json; charset=utf-8',
				success(response) {
					console.log(response);
					const {msg} = response;
					alert(msg);
					frm.reset();
				},
				error : console.log
			});
		});
		</script>
		
		<!-- #3.PUT -->
		<div class="menu-test">
			<h4>메뉴 수정하기(PUT)</h4>
			<p>메뉴번호를 사용해 해당메뉴정보를 수정함.</p>
			<form name="menuSearchFrm">
				<input type="text" name="id" placeholder="메뉴번호" class="form-control" /><br />
				<input type="submit" class="btn btn-block btn-outline-primary btn-send" value="검색" >
			</form>
		
			<hr />
			<form name="menuUpdateFrm">
				<!-- where조건절에 사용할 id를 담아둠 -->
				<input type="hidden" name="id" />
				<input type="text" name="restaurant" placeholder="음식점" class="form-control" />
				<br />
				<input type="text" name="name" placeholder="메뉴" class="form-control" />
				<br />
				<input type="number" name="price" placeholder="가격" step="1000" class="form-control" />
				<br />
				<div class="form-check form-check-inline">
					<input type="radio" class="form-check-input" name="type" id="put-kr" value="kr" checked>
					<label for="put-kr" class="form-check-label">한식</label>&nbsp;
					<input type="radio" class="form-check-input" name="type" id="put-ch" value="ch">
					<label for="put-ch" class="form-check-label">중식</label>&nbsp;
					<input type="radio" class="form-check-input" name="type" id="put-jp" value="jp">
					<label for="put-jp" class="form-check-label">일식</label>&nbsp;
				</div>
				<br />
				<div class="form-check form-check-inline">
					<input type="radio" class="form-check-input" name="taste" id="put-hot" value="hot" checked>
					<label for="put-hot" class="form-check-label">매운맛</label>&nbsp;
					<input type="radio" class="form-check-input" name="taste" id="put-mild" value="mild">
					<label for="put-mild" class="form-check-label">순한맛</label>
				</div>
				<br />
				<input type="submit" class="btn btn-block btn-outline-success btn-send" value="수정" >
			</form>
		</div>
		<script>
		document.menuUpdateFrm.addEventListener('submit', (e) => {
			e.preventDefault();
			const frmData = new FormData(e.target);
			const menu = {};
			frmData.forEach((v, k) => {
				menu[k] = v;
			});
			console.log(menu);
			
			// PUT방식 : 수정
			$.ajax({
				url : `\${MENU_URL}/menu`,
				method : "PUT",
				data : JSON.stringify(menu),
				contentType : 'application/json; charset=utf-8',
				success(response){
					console.log(response);
					alert(response.msg);
				},
				error: console.log,
				complete() {
					e.target.reset();
					document.menuSearchFrm.reset();
				}
			});
		});
		
		document.menuSearchFrm.addEventListener('submit', (e) => {
			e.preventDefault();
			const frm = e.target;
			const menuId = frm.id.value;
			if(!menuId) return;
			
			$.ajax({
				url : `\${MENU_URL}/menu/\${menuId}`,
        		method : "GET",
        		success(data) {
        			console.log(data);
        			
        			const frm = document.menuUpdateFrm;
        			const {id, restaurant, name, price, taste, type} = data;
        			
        			frm.id.value = id;
        			frm.restaurant.value = restaurant;
        			frm.name.value = name;
        			frm.price.value = price;
        			frm.type.value = type;
        			frm.taste.value = taste;
        			
        		},
        		error(jqxhr, statusText, err) {
        			console.log(jqxhr, statusText, err);
        			alert(menuId + "번 메뉴가 존재하지 않습니다.");
        			frm.reset();
        		}
			});
		});
		</script>
		
		<div class="menu-test">
			<h4>메뉴 삭제하기(DELETE)</h4>
			<p>메뉴번호를 사용해 해당메뉴정보를 삭제함.</p>
			<form id="menuDeleteFrm" name="menuDeleteFrm">
				<input type="text" name="id" placeholder="메뉴번호" class="form-control" /><br />
				<input type="submit" class="btn btn-block btn-outline-danger btn-send" value="삭제" >
			</form>
		</div>
		<script>
		document.menuDeleteFrm.addEventListener('submit', (e) => {
			e.preventDefault();
			const id = e.target.id.value;
			if(!id) return;
			
			$.ajax({
				url : `\${MENU_URL}/menu/\${id}`,
				method : "DELETE",
				success(response) {
					console.log(response);
					alert(response.msg);
				},
				error : console.log,
				complete() {
					e.target.reset();
				}
			});
		});
		</script>
		
	</div>

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>