package com.dev.frontend.panels.list;

import java.util.List;

import com.crossover.model.dto.CustomerDTO;
import com.dev.frontend.services.Services;

public class CustomerDataModel extends ListDataModel {
	private static final long serialVersionUID = 7526529951747613655L;

	public CustomerDataModel() {
		super(new String[] { "Code", "Name", "Phone", "Current Credit" }, 0);
	}

	@Override
	public int getObjectType() {
		return Services.TYPE_CUSTOMER;
	}

	@Override
	public String[][] convertRecordsListToTableModel(List<Object> list) {

		String[][] newArrayContent = new String[list.size()][4];
		
		for (int i = 0; i < newArrayContent.length; i++) {
			CustomerDTO customerDTO = (CustomerDTO) list.get(i);
			newArrayContent[i][0] = customerDTO.getCode().toString();
			newArrayContent[i][1] = customerDTO.getName();
			newArrayContent[i][2] = customerDTO.getPhone1();
			newArrayContent[i][3] = customerDTO.getCurrentCredit().toString();
		}
		

		return newArrayContent;
	}
}
