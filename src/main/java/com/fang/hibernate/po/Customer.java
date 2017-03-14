package com.fang.hibernate.po;

import java.util.HashSet;
import java.util.Set;

public class Customer {

  private Integer cid;

  private String cname;

  private Set<Orders> orders = new HashSet<Orders>();

  public Customer() {

  }

  public Customer(Integer cid, String cname) {
    super();
    this.cid = cid;
    this.cname = cname;
  }

  public Customer(Integer cid, String cname, Set<Orders> orders) {
    super();
    this.cid = cid;
    this.cname = cname;
    this.orders = orders;
  }

  public Integer getCid() {
    return cid;
  }

  public void setCid(Integer cid) {
    this.cid = cid;
  }

  public String getCname() {
    return cname;
  }

  public void setCname(String cname) {
    this.cname = cname;
  }

  public Set<Orders> getOrders() {
    return orders;
  }

  public void setOrders(Set<Orders> orders) {
    this.orders = orders;
  }

  @Override
  public String toString() {
    return "Customer [cid=" + cid + ", cname=" + cname + "]";
  }

}
