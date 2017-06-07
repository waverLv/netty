package com.netty.http_xml;

import com.netty.http_xml.pojo.Address;
import com.netty.http_xml.pojo.Customer;
import com.netty.http_xml.pojo.Order;
import com.netty.http_xml.pojo.Shipping;

public class OrderFactory {
	 public static Order create(long orderId){
		 Order order = new Order();
		 order.setOrderNumber(orderId);
		 order.setTotal(9999.99f);
		 Address address = new Address();
		 address.setCity("����");
		 address.setCountry("�й�");
		 address.setPostCode("471900");
		 address.setState("����ʡ");
		 address.setStreet1("���Ǵ��1");
		 address.setStreet2("���ݴ��");
		 order.setBillTo(address);
		 Customer customer = new Customer();
		 customer.setCustomerNumber(12);
		 customer.setFirstName("Kobe");
		 customer.setLastName("Briente");
		 order.setCustomer(customer);
		 order.setShipping(Shipping.INTERNATIONAL_EXPRESS);
		 order.setShipTo(address);
		 return order;
	 }

}
