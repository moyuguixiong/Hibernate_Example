package hibernatetest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ctc.wstx.evt.WEntityDeclaration;
import com.fang.hibernate.po.Course;
import com.fang.hibernate.po.Customer;
import com.fang.hibernate.po.Orders;
import com.fang.hibernate.po.Student;
import com.fang.hibernate.po.SysUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-root.xml")
public class HibernateTest {

  @Autowired
  private SessionFactory sessionFactory;

  @Test
  public void test() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    /*
     * SysUser sysUser = new SysUser();
     * sysUser.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
     * sysUser.setUserName("张三");
     * sysUser.setAge(30);
     * sysUser.setBirthday(new Date());
     * sysUser.setGender(true);
     * session.save(sysUser);
     */
    session.setFlushMode(FlushMode.AUTO);// 默认的刷险方式FlushMode.AUTO
    SysUser sysUser = session.load(SysUser.class, "517ed3bbb9a84357896565bd027d9105");
    // System.out.println(sysUser.getUserName());
    System.out.println(Hibernate.isInitialized(sysUser));
    if (!Hibernate.isInitialized(sysUser)) {
      Hibernate.initialize(sysUser);
    }

    tx.commit();
    session.close();
  }

  @Test
  // 级联 保存(插入)测试
  public void oneToMany() {
    Customer customer = new Customer();
    customer.setCname("赵六");

    Orders orders1 = new Orders();
    orders1.setAddress("北京");
    orders1.setCustomer(customer);

    customer.getOrders().add(orders1);
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    session.save(customer);
    tx.commit();
    session.close();
  }

  @Test
  // 级联删除(OneToMany)
  public void deleteTest() {

    // 数据库默认行为：订单有该用户数据，则只删除用户，会报错。
    // 对于hibernate，如果没有配置级联删除，而删除用户，默认情况下，会先更新订单的外键为null，然后再删除用户。
    // 如果设置了级联删除，会先删除该用户的订单数据(即外键数据)，然后再删除用户。
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    Customer customer = session.get(Customer.class, 9);
    if (customer != null) {
      session.delete(customer);
    }
    // session.save(orders1);
    // session.save(orders2);
    tx.commit();
    session.close();
  }

  @Test
  public void manyToManyTest() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();
    Student student1 = new Student();
    student1.setSname("张三");

    Student student2 = new Student();
    student2.setSname("李四");

    Course course1 = new Course();
    course1.setCname("Java开发");

    Course course2 = new Course();
    course2.setCname(".net开发");

    student1.getCourses().add(course1);
    student1.getCourses().add(course2);
    course1.getStudents().add(student1);
    course1.getStudents().add(student2);

    student2.getCourses().add(course1);
    course1.getStudents().add(student2);

    session.save(student1);
    session.save(student2);
    session.save(course1);
    session.save(course2);

    tx.commit();
    session.close();
  }

  @Test
  // 非级联(删除多对多表的对应关系)
  public void manyToManyDelete() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Student student = session.get(Student.class, 1);
    Course course = session.get(Course.class, 1);
    student.getCourses().remove(course);

    tx.commit();
    session.close();
  }

  @Test
  // 非级联(删除学生)，默认就会：先删除多对多表中该学生的所有数据，然后删除学生表中该学生
  public void manyToManyDelete2() {
    Session session = sessionFactory.openSession();
    Transaction tx = session.beginTransaction();

    Student student = session.get(Student.class, 1);
    session.delete(student);

    tx.commit();
    session.close();
  }

}
