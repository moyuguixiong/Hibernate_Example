package hibernatetest;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fang.hibernate.po.TestUser;
import com.fang.hibernate.service.TestUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-root.xml")
public class ReadConfig {
	@Autowired
	private TestUserService testUserService;

	@Test
	public void test() {
		System.out.println(ReadConfig.class.getClass().getResource("/"));
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		// hql的更新、删除、插入

		session.createQuery("from ");

		tx.commit();
		session.close();
		sessionFactory.close();
	}

	@Test
	public void hibernateTemplateTest() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for (int i = 2; i < 50; i++) {
			TestUser user = new TestUser();
			user.setName("编号" + (i + 1));
			user.setAge(i + 2);
			session.save(user);
		}
		tx.commit();
		session.close();
		sessionFactory.close();
	}

	public void hql(Session session) {
		Query query = session.createQuery("select new TestUser(id,name) from TestUser order by id desc");
		int pageIndex = 1;
		int pageSize = 5;
		query.setFirstResult((pageIndex - 1) * pageSize);
		query.setMaxResults(pageSize);
		// @SuppressWarnings("unchecked")
		// List<Object[]> list = query.list();
		// for (Object[] object : list) {
		// System.out.println(Arrays.toString(object));
		// }
		@SuppressWarnings("unchecked")
		List<TestUser> list = query.list();
		for (TestUser testUser : list) {
			System.out.println(testUser);
		}
	}

	public void qbc(Session session) {
		// qbc
		Criteria criteria = session.createCriteria(TestUser.class, "user");

		// 投影查询，即查询有限个字段
		criteria.setProjection(Projections.projectionList().add(Property.forName("user.id"), "id")
				.add(Property.forName("user.name"), "name").add(Property.forName("user.age"), "age"));

		// 条件查询
		criteria.add(Restrictions.and(Restrictions.gt("id", 1L), Restrictions.lt("age", 100)));

		// 排序
		// criteria.addOrder(Order.desc("age"));
		// criteria.addOrder(Order.desc("name"));
		criteria.addOrder(Order.desc("id"));

		// 分页
		int pageSize = 5;
		int pageIndex = 2;
		criteria.setFirstResult((pageIndex - 1) * pageSize);
		criteria.setMaxResults(pageSize);

		criteria.setResultTransformer(Transformers.aliasToBean(TestUser.class));

		@SuppressWarnings("unchecked")
		List<TestUser> list = criteria.list();
		for (TestUser testUser : list) {
			System.out.println(testUser);
		}

		// or查询
		Criteria criteria2 = session.createCriteria(TestUser.class);
		criteria.add(Restrictions.disjunction(Restrictions.eq("id", 51L), Restrictions.lt("age", 10)));
		TestUser usr = (TestUser) criteria2.list().get(0);
		System.out.println(usr);

	}

	public void sql(Session session) {
		SQLQuery sqlQuery = session.createSQLQuery("select * from testuser order by id desc");
		sqlQuery.addEntity(TestUser.class);
		int pageIndex = 1;
		int pageSize = 5;
		sqlQuery.setFirstResult((pageIndex - 1) * pageSize);
		sqlQuery.setMaxResults(5);
		@SuppressWarnings("unchecked")
		List<TestUser> list = sqlQuery.list();
		for (TestUser testUser : list) {
			System.out.println(testUser);
		}
	}
}
