package com.dev.frontend.panels.list;

import java.util.List;

import com.crossover.model.dto.ProductDTO;
import com.dev.frontend.services.Services;


public class ProductDataModel extends ListDataModel
{
	private static final long serialVersionUID = 7526529951747614655L;

	public ProductDataModel() 
	{
		super(new String[]{"Code","Description","Price","Quantity"}, 0);
	}

	@Override
	public int getObjectType() {
		return Services.TYPE_PRODUCT;
	}

	@Override
	public String[][] convertRecordsListToTableModel(List<Object> list) 
	{

		String[][] newArrayContent = new String[list.size()][4];
		
		for (int i = 0; i < newArrayContent.length; i++) {
			ProductDTO productDTO = (ProductDTO) list.get(i);
			newArrayContent[i][0] = productDTO.getCode().toString();
			newArrayContent[i][1] = productDTO.getDescription();
			newArrayContent[i][2] = productDTO.getPrice().toString();
			newArrayContent[i][3] = productDTO.getQuantity().toString();
		}
		
		return newArrayContent;
	}
}
