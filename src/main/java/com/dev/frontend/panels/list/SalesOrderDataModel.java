package com.dev.frontend.panels.list;

import java.util.List;

import com.crossover.model.dto.SalesOrderDTO;
import com.dev.frontend.services.Services;

public class SalesOrderDataModel extends ListDataModel {
	private static final long serialVersionUID = 7526529951747614655L;

	public SalesOrderDataModel() {
		super(new String[] { "Order Number", "Customer", "Total Price" }, 0);
	}

	@Override
	public int getObjectType() {
		return Services.TYPE_SALESORDER;
	}

	@Override
	public String[][] convertRecordsListToTableModel(List<Object> list) {

		String[][] newArrayContent = new String[list.size()][4];

		for (int i = 0; i < newArrayContent.length; i++) {
			SalesOrderDTO salesOrderDTO = (SalesOrderDTO) list.get(i);
			newArrayContent[i][0] = salesOrderDTO.getOrderNumber();
			newArrayContent[i][1] = salesOrderDTO.getCustomer().getName();
			newArrayContent[i][2] = salesOrderDTO.getTotalPrice().toString();
		}

		return newArrayContent;
	}
}
