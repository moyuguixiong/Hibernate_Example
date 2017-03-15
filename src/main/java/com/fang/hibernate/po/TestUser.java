package com.fang.hibernate.po;

public class TestUser {
	private Long id;

	private String name;

	private Integer age;

	public TestUser() {
	}

	public TestUser(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "TestUser [id=" + id + ", name=" + name + ", age=" + age + "]";
	}

}
