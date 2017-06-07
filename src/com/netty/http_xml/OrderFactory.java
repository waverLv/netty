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
		 address.setCity("洛阳");
		 address.setCountry("中国");
		 address.setPostCode("471900");
		 address.setState("河南省");
		 address.setStreet1("王城大道1");
		 address.setStreet2("中州大道");
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
