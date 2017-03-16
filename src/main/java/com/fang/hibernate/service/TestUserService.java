package com.fang.hibernate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.fang.hibernate.po.TestUser;

@Service
public class TestUserService {
	@Autowired
	private HibernateTemplate hibernateTemplate;

	public void test() {
		List<TestUser> list = (List<TestUser>) hibernateTemplate.find("from TestUser");
		if (list != null && list.size() > 0) {
			for (TestUser testUser : list) {
				System.out.println(testUser);
			}
		}
	}
}
