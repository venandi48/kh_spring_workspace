package com.kh.spring.data.corona19;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	private String createDt;	// 등록일시 2022-07-26 09:03:14.995
	private int deathCnt;	// 사망자수 16
	private int defCnt;		// 확진자수 11806
	private String gubun;		// 시도명 제주
	private String gubunCn;		// 시도명 济州
	private String gubunEn;		// 시도명 Jeju
	private int incDec;		// 전일대비증감 17
	private int localOccCnt;	// 지역발생수 1606
	private int overFlowCnt;	// 해외유입수 17
	private String qurRate;		// 10만명당발생률 38556
	private int seq;			// 고유번호 18344
	private String stdDay;		// 기준일시 2022년 07월 26일 00시
	private String updateDt;	// 수정일시분초 null 
}
