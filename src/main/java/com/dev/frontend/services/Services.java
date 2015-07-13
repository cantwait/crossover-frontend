package com.dev.frontend.services;

import java.util.ArrayList;
import java.util.List;

import com.crossover.model.dto.CustomerDTO;
import com.crossover.model.dto.CustomerListDTO;
import com.crossover.model.dto.ProductDTO;
import com.crossover.model.dto.ProductListDTO;
import com.crossover.model.dto.SalesOrderDTO;
import com.crossover.model.dto.SalesOrderListDTO;
import com.dev.frontend.panels.ComboBoxItem;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Services 
{
	
	private static final String BASE_URL = Utils.getProperty("base.url");
	
	public static final int TYPE_PRODUCT = 1;
	public static final int TYPE_CUSTOMER = 2;
	public static final int TYPE_SALESORDER = 3;
	

	
	
	public static Object save(Object object,int objectType) throws Exception
	{
		Object persistedObject = null;
		if (objectType == TYPE_CUSTOMER) {
			persistedObject = saveCustomer((CustomerDTO) object);
		}else if(objectType == TYPE_PRODUCT){
			persistedObject = saveProduct((ProductDTO) object);
		}else{
			String response = validateSalesOrder((SalesOrderDTO) object);
			if(response.equalsIgnoreCase("1")){
				throw new Exception("Not enough items to sell");
			}else if(response.equalsIgnoreCase("2")){
				throw new Exception("The price Exceed your credit");
			}else{
				
			}
			persistedObject = saveSalesOrder((SalesOrderDTO)object);
		}
		return persistedObject;
	}
	public static Object readRecordByCode(String code,int objectType)
	{ 
		Object object = null;
		if(objectType == TYPE_CUSTOMER){
			object = fetchCustomerByCode(code);
		}else
		if(objectType == TYPE_PRODUCT){
			object = fetchProductByCode(code);
		}else{
			object = fetchSalesOrderByCode(code);
		}
		
		
		return object;
	}
	public static boolean deleteRecordByCode(String code,int objectType)
	{
		if(objectType == TYPE_CUSTOMER){
			deleteCustomer(code);
		}else if(objectType == TYPE_PRODUCT){
			deleteProduct(code);
		}else{
			deleteSalesOrder(code);
		}
		return true;
	}
	
	public static List<Object> listCurrentRecords(int objectType)
	{
		List<Object> objects = new ArrayList<Object>();

		if(objectType == TYPE_CUSTOMER){
			objects.addAll(fetchCustomer());
		}else
		if(objectType == TYPE_PRODUCT){
			objects.addAll(fetchProducts());
		}else{
				objects.addAll(fetchSalesOrder());
		}
		return objects;
	}
	public static List<ComboBoxItem> listCurrentRecordRefernces(int objectType) 
	{	

		List<ComboBoxItem> cbi = new ArrayList<ComboBoxItem>();
		
		
		
		if(objectType == TYPE_CUSTOMER){
			List<CustomerDTO> customers = fetchCustomer();
			for (CustomerDTO customerDTO : customers) {
				cbi.add(new ComboBoxItem(customerDTO.getCode().toString(), customerDTO.getName()));
			}
		}else if(objectType == TYPE_PRODUCT){
			List<ProductDTO> products = fetchProducts();
			for (ProductDTO productDTO : products) {
				cbi.add(new ComboBoxItem(productDTO.getCode().toString(), productDTO.getDescription()));
			}
		}
		
		return cbi;
	}
	public static double getProductPrice(String productCode) {

		
		ProductDTO productDTO = fetchProductByCode(productCode);
		
		return productDTO.getPrice();
	}
	
	private static boolean deleteSalesOrder(String code){
		boolean deleted = false;
		try {
			HttpResponse<String> deleteSalesOrderResponse = Unirest.get(BASE_URL + "/sales/{orderNumber}/delete")
						.routeParam("orderNumber", code).asString();
			int status = deleteSalesOrderResponse.getStatus();
			if(status == 200)
				deleted = true;
		} catch (UnirestException e) {
			e.printStackTrace();
		}		
		return deleted;
	}
	
	private static boolean deleteCustomer(String code){
		boolean deleted = false;
		try {
			HttpResponse<String> deleteCustomerResponse = Unirest.get(BASE_URL + "/customer/{customerCode}/delete")
						.routeParam("customerCode", code).asString();
			int status = deleteCustomerResponse.getStatus();
			if(status == 200)
				deleted = true;
		} catch (UnirestException e) {
			e.printStackTrace();
		}		
		return deleted;
	}
	
	private static boolean deleteProduct(String code){
		boolean deleted = false;
		try {
			HttpResponse<String> deleteProductResponse = Unirest.get(BASE_URL + "/product/{productCode}/delete")
						.routeParam("productCode", code).asString();
			
			int status = deleteProductResponse.getStatus();
			if(status == 200)
				deleted = true;
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		return deleted;
	}
	
	private static ProductDTO saveProduct(ProductDTO productDTO){
		HttpResponse<String> productResponse;
		ProductDTO persistedProductDTO = null;
		try {
			productResponse = Unirest.post(BASE_URL + "/product/new")
					.header("Accept", "application/json")
					.header("Content-Type", "application/json")
					.body(getGson().toJson(productDTO))
					.asString();
			persistedProductDTO = getGson().fromJson(productResponse.getBody(), ProductDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return persistedProductDTO;
	}
	
private static String validateSalesOrder(SalesOrderDTO object) {
		HttpResponse<String> salesOrderResponse;
		String stringResponse = null;
		try {
			salesOrderResponse = Unirest.post(BASE_URL + "/sales/validate")
					.header("Accept", "text/plain")
					.header("Content-Type", "application/json")
					.body(getGson().toJson(object))
					.asString();
			stringResponse = getGson().fromJson(salesOrderResponse.getBody(), String.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		
		return stringResponse;
	}
	
	private static SalesOrderDTO saveSalesOrder(SalesOrderDTO object) {
		
		
		HttpResponse<String> salesOrderResponse;
		SalesOrderDTO persistedSalesOrderDTO = null;
		try {
			salesOrderResponse = Unirest.post(BASE_URL + "/sales/new")
					.header("Accept", "application/json")
					.header("Content-Type", "application/json")
					.body(getGson().toJson(object))
					.asString();
			persistedSalesOrderDTO = getGson().fromJson(salesOrderResponse.getBody(), SalesOrderDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return persistedSalesOrderDTO;
	}
	
	private static CustomerDTO saveCustomer(CustomerDTO customerDTO){

		HttpResponse<String> customerResponse;
		CustomerDTO persistedCustomerDTO = null;
		try {
			customerResponse = Unirest.post(BASE_URL + "/customer/new")
					.header("Accept", "application/json")
					.header("Content-Type", "application/json")
					.body(getGson().toJson(customerDTO))
					.asString();
			persistedCustomerDTO = getGson().fromJson(customerResponse.getBody(), CustomerDTO.class); 
		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return persistedCustomerDTO;
	}
	
	
	private static SalesOrderDTO fetchSalesOrderByCode(String code) {
		HttpResponse<String> salesOrderResponse;
		SalesOrderDTO salesOrderDTO = null;
		try {
			salesOrderResponse = Unirest.get(BASE_URL + "/sales/{orderNumber}")
					.header("Accept", "application/json")
					.routeParam("orderNumber", code)
					.asString();
			salesOrderDTO = getGson().fromJson(salesOrderResponse.getBody(), SalesOrderDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return salesOrderDTO;
	}
	
	private static ProductDTO fetchProductByCode(String code) {
		HttpResponse<String> productResponse;
		ProductDTO productDTO = null;
		try {
			productResponse = Unirest.get(BASE_URL + "/product/{orderNumber}")
					.header("Accept", "application/json")
					.routeParam("orderNumber", code)
					.asString();
			productDTO = getGson().fromJson(productResponse.getBody(), ProductDTO.class);

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return productDTO;
	}
	
	private static CustomerDTO fetchCustomerByCode(String code) {
		HttpResponse<String> customerResponse;
		CustomerDTO customerDTO = null;
		try {
			customerResponse = Unirest.get(BASE_URL + "/customer/{customerCode}")
					.header("Accept", "application/json")
					.routeParam("customerCode", code)
					.asString();
			customerDTO = getGson().fromJson(customerResponse.getBody(), CustomerDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return customerDTO;
	}
	
	private static List<CustomerDTO> fetchCustomer() {
		
		HttpResponse<String> customerResponse;
		CustomerListDTO customerDTO = null;
		try {
			customerResponse = Unirest.get(BASE_URL + "/customer")
					.header("Accept", "application/json")
					.asString();
			customerDTO = getGson().fromJson(customerResponse.getBody(), CustomerListDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return customerDTO.getCustomers();
	}
	
	private static List<ProductDTO> fetchProducts(){
		HttpResponse<String> productResponse;
		ProductListDTO productDTO = null;
		try {
			productResponse = Unirest.get(BASE_URL + "/product")
					.header("Accept", "application/json")
					.asString();
			productDTO = getGson().fromJson(productResponse.getBody(), ProductListDTO.class); 

		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return productDTO.getProducts();
	}
	
	private static List<SalesOrderDTO> fetchSalesOrder() {
		HttpResponse<String> productResponse;
		SalesOrderListDTO  salesOrderListDTO = null;
		try {
			productResponse = Unirest.get(BASE_URL + "/sales")
					.header("Accept", "application/json")
					.asString();
			salesOrderListDTO = getGson().fromJson(productResponse.getBody(), SalesOrderListDTO.class); 
			
		} catch (UnirestException e) {
			e.printStackTrace();
		}	
		return salesOrderListDTO.getSalesOrder();
	}
	
	private static Gson getGson(){
		return new Gson();
	}
	
}
