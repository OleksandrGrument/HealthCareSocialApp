/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.comeonbabys.android.app.db.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author PvTai Nov 7, 2014 10:44:40 AM
 */
public class ListReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7483468131868842875L;
	private List<ReportDTO> listReportDto;

	public List<ReportDTO> getListReportDto() {
		return listReportDto;
	}

	public void setListReportDto(List<ReportDTO> listReportDto) {
		this.listReportDto = listReportDto;
	}
}
