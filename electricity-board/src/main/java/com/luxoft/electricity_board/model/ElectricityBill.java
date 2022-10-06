package com.luxoft.electricity_board.model;

import java.util.ArrayList;
import java.util.List;

public class ElectricityBill {

	private String consumerNumber;
	private String consumerName;
	private String consumerAddress;
	private Integer unitsConsumed;
	private  Double billAmount;

	public ElectricityBill() {
		// TODO Auto-generated constructor stub
	}

	public ElectricityBill(String consumerNumber, String consumerName, String consumerAddress, Integer unitsConsumed,
			Double billAmount) {
		super();
		this.consumerNumber = consumerNumber;
		this.consumerName = consumerName;
		this.consumerAddress = consumerAddress;
		this.unitsConsumed = unitsConsumed;
		this.billAmount = billAmount;
	}

	public String getConsumerNumber() {
		return consumerNumber;
	}

	public void setConsumerNumber(String consumerNumber) {
		this.consumerNumber = consumerNumber;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getConsumerAddress() {
		return consumerAddress;
	}

	public void setConsumerAddress(String consumerAddress) {
		this.consumerAddress = consumerAddress;
	}

	public Integer getUnitsConsumed() {
		return unitsConsumed;
	}

	public void setUnitsConsumed(Integer unitsConsumed) {
		this.unitsConsumed = unitsConsumed;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	
	
	
	@Override
	public String toString() {
		return "ElectricityBill [consumerNumber=" + consumerNumber + ", consumerName=" + consumerName
				+ ", consumerAddress=" + consumerAddress + ", unitsConsumed=" + unitsConsumed + ", billAmount="
				+ billAmount + "]";
	}

	
	
	

}
