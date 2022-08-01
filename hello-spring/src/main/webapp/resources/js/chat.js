// chat.js

document.querySelector("#sendBtn").addEventListener('click', (e) => {
	const msg = document.querySelector("#msg").value;
	if(!msg) return;
	
	const payload = {
		chatroomId,
		memberId,
		msg,
		time : Date.now(),
		type : 'CHAT'
	};
	
	stompClient.send(`/app/chat/${chatroomId}`, {}, JSON.stringify(payload));
	document.querySelector("#msg").value = '';
});

const lastCheck = () => {
	console.log('lastCheck!');
	let payload = {
		chatroomId,
		memberId,
		lastCheck : Date.now(),
		type : "LAST_CHECK"
	};
	
	stompClient.send("/app/lastCheck", {}, JSON.stringify(payload));
};

window.addEventListener('focus', (e) => {
	lastCheck();
});

setTimeout(() => {
	const container = document.querySelector('#msg-container');
	
	lastCheck();
	
	stompClient.subscribe(`/app/chat/${chatroomId}`, (message) => {
		console.log(`/app/chat/${chatroomId} : `, message);
		
		const {'content-type' : contentType} = message.headers;
		console.log('contentType : ', contentType);
		if(!contentType) return;
		
		const {memberId, msg, time} = JSON.parse(message.body);
		const html = `<li class="list-group-item">${memberId} : ${msg}</li>`;
		container.insertAdjacentHTML('beforeend', html);
	});
	
}, 500);
