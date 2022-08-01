// adminChat.js

const ws = new SockJS(`http://${location.host}/spring/stomp`);
const stompClient = Stomp.over(ws);

stompClient.connect({}, (frame) => {
	console.log('connect : ', frame);
	
	lastCheck();

	const container = document.querySelector('#msg-container');

	stompClient.subscribe(`/app/chat/${chatroomId}`, (message) => {
		console.log(`/app/chat/${chatroomId} : `, message);

		const {'content-type' : contentType} = message.headers;
		console.log('contentType : ', contentType);
		if(!contentType) return;

		const {memberId, msg, time} = JSON.parse(message.body);
		const html = `<li class="list-group-item">${memberId} : ${msg}</li>`;
		container.insertAdjacentHTML('beforeend', html);
	});

});

window.addEventListener('focus', (e) => {
	lastCheck();
});

const lastCheck = () => {
	console.log('lastCheck!');
	let payload = {
		chatroomId,
		memberId : 'admin',
		lastCheck : Date.now(),
		type : "LAST_CHECK"
	};
	
	stompClient.send("/app/admin/lastCheck", {}, JSON.stringify(payload));
}

document.querySelector("#sendBtn").addEventListener('click', (e) => {
	const msg = document.querySelector("#msg").value;
	if(!msg) return;
	
	const payload = {
		chatroomId,
		memberId : 'admin',
		msg,
		time : Date.now(),
		type : 'CHAT'
	};
	
	stompClient.send(`/app/chat/${chatroomId}`, {}, JSON.stringify(payload));
	document.querySelector("#msg").value = '';
});