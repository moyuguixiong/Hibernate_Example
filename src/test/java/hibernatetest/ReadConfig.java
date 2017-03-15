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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.junit.Test;

import com.fang.hibernate.po.TestUser;

public class ReadConfig {
	@Test
	public void test() {
		System.out.println(ReadConfig.class.getClass().getResource("/"));
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		TestUser testUser = session.load(TestUser.class, 5L);
		System.out.println(testUser);
		
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
		int pageIndex = 6;
		int pageSize = 3;
		Criteria criteriaCount = session.createCriteria(TestUser.class);
		criteriaCount.setProjection(Projections.rowCount());
		double totalCount = Double.parseDouble(criteriaCount.uniqueResult().toString());
		System.out.println("总数量" + (int) totalCount);
		System.out.println("最大页码" + (int) Math.ceil(totalCount / pageSize));

		Criteria criteria = session.createCriteria(TestUser.class, "user");

		// 查询哪些字段
		criteria.setProjection(Projections.projectionList().add(Property.forName("user.name"), "name")
				.add(Property.forName("user.id"), "id"));
		// 设置where条件
		criteria.add(Restrictions.gt("id", 0L));
		// 设置排序
		criteria.addOrder(Order.asc("id"));
		// 将数组转换为对象
		criteria.setResultTransformer(Transformers.aliasToBean(TestUser.class));

		criteria.setFirstResult((pageIndex - 1) * pageSize);
		criteria.setMaxResults(pageSize);

		@SuppressWarnings("unchecked")
		List<TestUser> list = criteria.list();
		for (TestUser testUser : list) {
			System.out.println(testUser);
		}
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
