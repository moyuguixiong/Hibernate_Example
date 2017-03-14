package com.fang.hibernate.po;

public class Orders {

  private Integer oid;

  private String address;

  private Customer customer;

  public Orders() {

  }

  public Integer getOid() {
    return oid;
  }

  public void setOid(Integer oid) {
    this.oid = oid;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return "Orders [oid=" + oid + ", address=" + address + ", customer=" + customer + "]";
  }

}
