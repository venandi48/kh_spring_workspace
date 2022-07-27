package com.kh.spring.data.corona19;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header {
	private String resultCode;
	private String resultMsg;
}
