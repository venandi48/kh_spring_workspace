package com.kh.spring.data.corona19;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Body {
	private List<Item> items;
	private int numOfRows;
	private int pageNo;
	private int totalCount;
}
