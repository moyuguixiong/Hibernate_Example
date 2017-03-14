package hibernatetest;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fang.hibernate.po.Customer;
import com.fang.hibernate.po.Orders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-root.xml")
public class HibernateTestForTableJoin {

  /**
   *
   * 1、涉及投影查询、条件查询、分页、排序等，适合用QBC：
   * (1)查询结果转对象比较容易
   * (2)动态条件查询比较灵活，调用方法动态设置参数即可，HQL还得拼接字符串。
   *
   * 2、涉及表连接查询，适合用HQL，支持的形式比较多。
   * (1)交叉连接(笛卡尔积)
   * (2)内连接(查询交集)
   * (3)迫切内连接
   * (4)隐式内连接
   * (5)左外连接
   * (6)迫切左外连接
   * (7)右外连接
   *
   */

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  public void innerJoinTest() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Query query = session
        .createQuery("select distinct c from Customer as c inner join fetch c.orders");

    // @SuppressWarnings("unchecked")
    // List<Object[]> list = query.list();
    // if (list != null && list.size() > 0) {
    // for (Object[] objects : list) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    @SuppressWarnings("unchecked")
    List<Customer> list = query.list();
    if (list != null && list.size() > 0) {
      for (Customer customer : list) {
        System.out.println(customer);
        Set<Orders> orders = customer.getOrders();
        if (orders != null && orders.size() > 0) {
          for (Orders order : orders) {
            System.out.print(order.getOid() + "," + order.getAddress() + "   ");
          }
        }
        System.out.println();
      }
    }

    tx.commit();
    session.close();
  }

}
