package com.netty.http_xml.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import com.netty.http_xml.OrderFactory;
import com.netty.http_xml.pojo.Order;

public class TestOrder {
	private IBindingFactory factory = null;
	private StringWriter writer = null;
	private StringReader reader = null;
	private final static String CHARSET_NAME = "UTF-8";
	
	private String encode2Xml(Order order) throws JiBXException, IOException{
		factory = BindingDirectory.getFactory(Order.class);
		writer = new StringWriter();
		IMarshallingContext ctx = factory.createMarshallingContext();
		ctx.setIndent(2);
		ctx.marshalDocument(order, CHARSET_NAME, null, writer);
		String xmlStr = writer.toString();
		writer.close();
		System.out.println(xmlStr.toString());
		return xmlStr;
		
	}
	
	private Order decode2Order(String xmlBody) throws JiBXException{
		reader = new StringReader(xmlBody);
		IUnmarshallingContext ctx = factory.createUnmarshallingContext();
		Order order = (Order) ctx.unmarshalDocument(reader);
		return order;
	}

	public static void main(String[] args) throws JiBXException, IOException {
		TestOrder test = new TestOrder();
		Order order = OrderFactory.create(123);
		String xmlBody = test.encode2Xml(order);
		Order order2 = test.decode2Order(xmlBody);
		System.out.println(order2);
	}
}
