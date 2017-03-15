package hibernatetest;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import com.fang.hibernate.po.Customer;
import com.fang.hibernate.po.TestUser;

public class ReadConfig {
	@Test
	public void test() {
		System.out.println(ReadConfig.class.getClass().getResource("/"));
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		TestUser testUser = new TestUser();
		testUser.setAge(20);
		testUser.setName("张三");
		session.save(testUser);
		// criteria.add(Restrictions.ge("", 0));
		tx.commit();
		session.close();
		sessionFactory.close();
	}
}
