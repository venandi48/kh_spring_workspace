package com.kh.spring.data.model.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kh.spring.data.corona19.Body;
import com.kh.spring.data.corona19.Item;
import com.kh.spring.data.corona19.Response;
import com.kh.spring.data.course.Course;
import com.kh.spring.data.course.Student;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public Resource passXML(String url) {
		return resourceLoader.getResource(url);
	}

	/**
	 * Document객체 이용한 parsing
	 * 
	 * 1. DocumentBuilderFactory 2. DocumentBuilder 3. Document
	 * 
	 * Document#getElementByTagName(tagName)
	 * NodeList#item(index)
	 * Node#getTextContent()
	 */
	@Override
	public Course parseXml(String url) {
		Course course = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url); // org.w3c.dom.Document

			int id = Integer.parseInt(getTextContent(doc, "id"));
			String title = getTextContent(doc, "title");
			int price = Integer.parseInt(getTextContent(doc, "price"));
			LocalDate created = LocalDate.parse(getTextContent(doc, "created"));

			NodeList nodeList = doc.getElementsByTagName("student");
			List<Student> students = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i); // org.w3c.dom.Node
				Element elem = (Element) node; // org.w3c.dom.Element

				int studentId = Integer.parseInt(getTextContent(elem, "id"));
				String name = getTextContent(elem, "name");
				String tel = getTextContent(elem, "tel");

				Student student = new Student(studentId, name, tel);
				students.add(student);
			}

			course = new Course(id, title, price, created, students);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return course;
	}

	private String getTextContent(Document doc, String tagName) {
		return doc.getElementsByTagName(tagName).item(0).getTextContent();
	}

	private String getTextContent(Element elem, String tagName) {
		return elem.getElementsByTagName(tagName).item(0).getTextContent();
	}
	
	/**
	 * XmlMapper
	 *  - jackson-dataformat-xml 의존 필요
	 *  - jackson-datatype-jsr310 의존 (jdk1.8 time패키지 이용하는 경우)
	 */
	@Override
	public Course parseXml2(String url) {
		// Resource -> String -> XmlMapper 파싱
		Resource resource = resourceLoader.getResource(url);
		String xml = convertToXml(resource);
		Course course = convertToJava(xml, Course.class);
		return course;
	}
	
	@Override
	public <T> T parseXml2(String url, Class<T> clazz) {
		ObjectMapper xmlMapper = new XmlMapper().registerModule(new JavaTimeModule()); // jdk1.8 time패키지 이용하는 경우
		T obj = null;
		try {
			obj = xmlMapper.readValue(new URL(url), clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 제네릭메소드
	 *  - 메소드 독립적인 타입변수
	 */
	private <T> T convertToJava(String xml, Class<T> clazz) {
		log.debug("xml = {}", xml);
		
//		ObjectMapper xmlMapper = new XmlMapper(); // jackson-dataformat-xml 의존만 사용할 경우
		ObjectMapper xmlMapper = new XmlMapper().registerModule(new JavaTimeModule()); // jdk1.8 time패키지 이용하는 경우
		T obj = null;
		try {
			obj = xmlMapper.readValue(xml, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}

	private String convertToXml(Resource resource) {
		String xml = null;
		try (Reader reader = new InputStreamReader(resource.getInputStream(), "utf-8")) {
			xml = FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml;
	}

	@Override
	public Response getCorona19(String url) {
		Response response = new Response();
		Body body = new Body();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url);

			NodeList nodeList = doc.getElementsByTagName("item");
			List<Item> items = new ArrayList<>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element elem = (Element) nodeList.item(i);
				String createDt = getTextContent(elem, "createDt");
				int deathCnt = Integer.parseInt(getTextContent(elem, "deathCnt"));
				int defCnt = Integer.parseInt(getTextContent(elem, "defCnt"));
				String gubun = getTextContent(elem, "gubun");
				String gubunCn = getTextContent(elem, "gubunCn");
				String gubunEn = getTextContent(elem, "gubunEn");
				int incDec = Integer.parseInt(getTextContent(elem, "incDec"));
				int localOccCnt = Integer.parseInt(getTextContent(elem, "localOccCnt"));
				int overFlowCnt = Integer.parseInt(getTextContent(elem, "overFlowCnt"));
				String qurRate = getTextContent(elem, "qurRate");
				int seq = Integer.parseInt(getTextContent(elem, "seq"));
				String stdDay  = getTextContent(elem, "stdDay");
				String updateDt  = getTextContent(elem, "updateDt");
				Item item = new Item(createDt, deathCnt, defCnt, gubun, gubunCn, gubunEn, incDec, localOccCnt, overFlowCnt, qurRate, seq, stdDay, updateDt);
				items.add(item);
			}
			body.setItems(items);
			response.setBody(body);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public Response getCorona19XmlMapper(String url) {
		Resource resource = resourceLoader.getResource(url);
		String xml = convertToXml(resource);
		Response response = convertToJava(xml, Response.class);
		return response;
	}
	
	@Override
	public Object passJson(String url) {
		return resourceLoader.getResource(url);
	}
	
	@Override
	public Course parseJson(String url) {
		// jdk1.8 time패키지 이용하는 경우 registerModule 필요
		ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
		Course course = null;
		try {
			course = objectMapper.readValue(new URL(url), Course.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return course;
	}
	
}
