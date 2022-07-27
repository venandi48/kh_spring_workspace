package com.kh.spring.data.model.service;

import org.springframework.core.io.Resource;

import com.kh.spring.data.corona19.Response;
import com.kh.spring.data.course.Course;

public interface DataService {

	Resource passXML(String url);

	Course parseXml(String url);

	Course parseXml2(String url);

	Response getCorona19(String url);

	Response getCorona19XmlMapper(String url);

	Object passJson(String url);

	Course parseJson(String url);

	<T> T parseXml2(String url, Class<T> clazz);

}
