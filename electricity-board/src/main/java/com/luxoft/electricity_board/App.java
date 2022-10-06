package com.luxoft.electricity_board;

import java.util.List;

import com.luxoft.electricity_board.model.ElectricityBill;
import com.luxoft.electricity_board.utility.ElectricityBoard;

public class App 
{
	public static void main(String[] args) {
		ElectricityBoard eboard = new ElectricityBoard();
		String filePath = "C:\\Users\\PPrakash\\Cloud\\Task\\electricity-board\\ElectricityBill.txt";
		try {
			List<ElectricityBill> list = eboard.generateBill(filePath);
			eboard.addBill(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
