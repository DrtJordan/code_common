package org.code.es.comsumer.vo;

import org.code.core.constant.Constants;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = Constants.ESProp.INDEX_NAME, type = Constants.ESProp.TYPE_PERSON_INFO)
public class Person {
	private int id;
	private int age;
	private String name;
	
	public Person(int id, int age, String name) {
		super();
		this.id = id;
		this.age = age;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
