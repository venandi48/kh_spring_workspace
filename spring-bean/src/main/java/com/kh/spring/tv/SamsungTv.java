package com.kh.spring.tv;

public class SamsungTv implements Tv {

	private RemoteControl remoteControl;

	public SamsungTv() {
		System.out.println("SamsungTv 객체 생성!");
	}
	
	public SamsungTv(RemoteControl remoteControl) {
		System.out.println("SamsungTv 객체 생성 & 의존 주입 : " + remoteControl);
		this.remoteControl = remoteControl;
	}

	@Override
	public void powerOn() {
		System.out.println("SamsungTv 전원을 켭니다.");
	}
	
	@Override
	public void channelTo(int no) {
		remoteControl.channelTo(no);
	}

}
