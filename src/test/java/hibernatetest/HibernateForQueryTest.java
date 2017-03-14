package hibernatetest;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fang.hibernate.po.Customer;
import com.fang.hibernate.po.Orders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-root.xml")
public class HibernateForQueryTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  public void insertData() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Customer customer1 = new Customer();
    customer1.setCname("阿大");
    for (int i = 0; i < 50; i++) {
      Orders orders = new Orders();
      orders.setAddress("幸福大街" + (i + 1));
      orders.setCustomer(customer1);
      customer1.getOrders().add(orders);
    }

    Customer customer2 = new Customer();
    customer2.setCname("阿二");
    for (int i = 0; i < 30; i++) {
      Orders orders = new Orders();
      orders.setAddress("前进路" + (i + 1));
      orders.setCustomer(customer2);
      customer2.getOrders().add(orders);
    }

    session.save(customer1);
    session.save(customer2);

    tx.commit();
    session.close();
  }

  @Test
  public void hql() {

    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    // HQL方式查询:分页、排序、总记录数查询、参数绑定、投影查询
    Query query = session
        .createQuery(
            "select new Customer(cid,cname) from Customer where cname=:name order by cid asc")
        .setFirstResult(0).setMaxResults(1);

    query.setString("name", "阿二");
    Query queryCount = session.createQuery("select count(*) from Customer");
    System.out.println("总记录数：" + ((Long) queryCount.iterate().next()).intValue());

    // @SuppressWarnings("unchecked")
    // List<Object[]> list = query.list();// 进行投影查询后，查询出的就不是特定对象的数据了。
    // if (list != null && list.size() > 0) {
    // for (Object[] objects : list) {
    // System.out.println(Arrays.toString(objects));
    // }
    // }

    @SuppressWarnings("unchecked")
    List<Customer> list = query.list();
    for (Customer customer : list) {
      System.out.println(customer.getCname());
      Set<Orders> orders = customer.getOrders();
      if (orders != null && orders.size() > 0) {
        for (Orders orders2 : orders) {
          System.out.print(orders2.getOid() + "," + orders2.getAddress());
        }
      } else {
        System.out.println("没有查询出订单");
      }
      System.out.println("");
    }

    tx.commit();
    session.close();
  }

  @Test
  public void qbcForOrders() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Criteria criteria = session.createCriteria(Orders.class);
    @SuppressWarnings("unchecked")
    List<Orders> list = criteria.list();

    for (Orders order : list) {
      // System.out.println(order.getCustomer().getCname());
    }

    tx.commit();
    session.close();
  }

  @Test
  public void qbcForProjection() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    // QBC方式查询:分页、排序、总记录数查询、投影查询
    Criteria criteria = session.createCriteria(Orders.class, "orders")
        .add(Restrictions.gt("oid", 0)).addOrder(Order.asc("oid")).setFirstResult(60 * (1 - 1))
        .setMaxResults(60);
    criteria.setProjection(Projections.projectionList().add(Property.forName("orders.oid"), "oid")
        .add(Property.forName("orders.address"), "address")
        .add(Property.forName("orders.customer"), "customer"));

    criteria.setResultTransformer(new AliasToBeanResultTransformer(Orders.class));
    // criteria.setResultTransformer(Transformers.aliasToBean(Orders.class));

    // Criteria criteriaCount = session.createCriteria(Orders.class).setProjection(
    // Projections.rowCount());
    // System.out.println("总行数：" + criteriaCount.uniqueResult().toString());

    @SuppressWarnings("unchecked")
    List<Orders> list = criteria.list();
    System.out.println("总数：" + list.size());
    for (Orders orders : list) {
      System.out.println(orders.getOid() + "," + orders.getAddress());
      if (orders.getCustomer() != null) {
        System.out.println("订单所属客户：" + orders.getCustomer().getCname());
      }
    }

    // @SuppressWarnings("unchecked")
    // List<Object[]> list = criteria.list();
    // for (Object[] objects : list) {
    // System.out.println(Arrays.toString(objects));
    // }

    tx.commit();
    session.close();
  }

  @Test
  public void qbcForCustomer() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Criteria criteria = session.createCriteria(Customer.class, "customer");

    // qbc投影查询
    criteria.setProjection(Projections.projectionList()
        .add(Property.forName("customer.cid"), "cid")
        .add(Property.forName("customer.cname"), "cname"));

    // .add(Property.forName("customer.orders"), "orders")
    // 有主外键关系的表，集合类型参数(一对多种的多)不能添加到参数中，但是对象类型可以。集合类型参数要想查询，必须使用表连接，或单独查询。
    // 即：对于投影查询，不能通过对象获取关联表的数据？

    criteria.setResultTransformer(Transformers.aliasToBean(Customer.class));

    // qbc条件查询
    criteria.add(Restrictions.ge("cid", 0));

    // qbc排序
    criteria.addOrder(Order.asc("cid"));

    // qbc分页
    int pageSize = 1;
    int pageIndex = 2;
    criteria.setFirstResult((pageIndex - 1) * pageSize);
    criteria.setMaxResults(pageSize);

    @SuppressWarnings("unchecked")
    List<Customer> list = criteria.list();
    if (list != null && list.size() > 0) {
      for (Customer customer : list) {
        System.out.println(customer.getCid() + "," + customer.getCname());
        Set<Orders> set = customer.getOrders();
        if (set != null && set.size() > 0) {
          for (Orders orders : set) {
            System.out.print(orders.getAddress() + " ");
          }
        } else {
          System.out.println("未查询出订单");
        }
      }
    }

    tx.commit();
    session.close();
  }

  @Test
  public void qbcForQueryAllAndSet() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Criteria criteria = session.createCriteria(Customer.class);
    // criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

    @SuppressWarnings("unchecked")
    List<Customer> list = criteria.list();
    if (list != null && list.size() > 0) {
      for (Customer customer : list) {
        System.out.println("用户：" + customer.getCname());
        // 设置set上的batch-size属性，就会进行批量查询，使用in
        Set<Orders> orders = customer.getOrders();
        if (orders != null && orders.size() > 0) {
          for (Orders order : orders) {
            System.out.print(order.getAddress() + "      ");
          }
        }
        System.out.println();
      }
    }

    tx.commit();
    session.close();
  }

  @Test
  public void sql() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    // 原生SQL方式查询
    SQLQuery sqlQuery = session.createSQLQuery("select cname from customer");

    // 这种转换方法，可以只查询Customer类的部分字段
    sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(Customer.class));
    @SuppressWarnings("unchecked")
    List<Customer> list = sqlQuery.list();

    // 这种转换方法，必须查询Customer类中对应的所有字段。
    // @SuppressWarnings("unchecked")
    // List<Customer> list = sqlQuery.addEntity(Customer.class).list();

    if (list != null && list.size() > 0) {
      for (Customer customer : list) {
        System.out.println(customer.getCname());
      }
    }

    tx.commit();
    session.close();
  }

}
