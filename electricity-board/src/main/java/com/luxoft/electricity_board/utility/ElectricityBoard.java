package com.luxoft.electricity_board.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luxoft.electricity_board.dao.DBHandler;
import com.luxoft.electricity_board.model.ElectricityBill;

public class ElectricityBoard {

	public List<ElectricityBill> generateBill(String filePath) throws Exception {
		File file = new File(filePath);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuffer stringBuffer = new StringBuffer();
		String line;
		List<ElectricityBill> details = new ArrayList<ElectricityBill>();
		while ((line = bufferedReader.readLine()) != null) {
			String userDataArr[] = line.split(",");
			String consumerNumber = userDataArr[0];
			if (validate(consumerNumber)) {
				ElectricityBill data = new ElectricityBill();
				data.setConsumerNumber(userDataArr[0]);
				data.setConsumerName(userDataArr[1]);
				data.setConsumerAddress(userDataArr[2]);
				data.setUnitsConsumed(Integer.valueOf(userDataArr[3]));
				double billAmount = parseData(data);
				data.setBillAmount(billAmount);
				details.add(data);
			} else {
				System.out
						.println("Consumer number should starts with '0' and should be of 10 digits " + consumerNumber);
			}
		}
		bufferedReader.close();
		return details;
	}

	public static boolean validate(String consumerNumber) {
		return consumerNumber.startsWith("0") && consumerNumber.length() == 10;
	}

	private static double parseData(ElectricityBill consumerElectricityData) {
		Double consumerBill = calculateBillAmount(consumerElectricityData);
		System.out.println("Bill Of the Consumer with consumer Number :" + consumerElectricityData.getConsumerNumber()
				+ " is " + consumerBill);
		return consumerBill;
	}

	public static double calculateBillAmount(ElectricityBill consumerData) {
		double billAmount = 0.0;
		Integer unitsConsumed = consumerData.getUnitsConsumed();
		if (unitsConsumed <= 100) {
			billAmount = Double.valueOf(0);
		} else if (unitsConsumed > 100 && unitsConsumed <= 300) {
			billAmount = (unitsConsumed - 100) * 1.5;
		} else if (unitsConsumed > 300 && unitsConsumed <= 600) {
			billAmount = 200 * 1.5 + (unitsConsumed - 300) * 3.5;
		} else if (unitsConsumed > 600 && unitsConsumed <= 1000) {
			billAmount = 200 * 1.5 + 300 * 3.5 + (unitsConsumed - 600) * 5.5;
		} else {
			billAmount = 200 * 1.5 + 300 * 3.5 + 600 * 5.5 + (unitsConsumed - 1000) * 7.5;
		}
		return billAmount;
	}

	public void addBill(List<ElectricityBill> billList) throws Exception {
		String filePath = "C:\\\\Users\\\\PPrakash\\\\Cloud\\\\Task\\\\electricity-board\\\\ElectricityBill.txt";
		List<ElectricityBill> listOfConsumers = generateBill(filePath);
		DBHandler handler = new DBHandler();
		Connection connection = handler.establishConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into electricity_bill values(?,?,?,?,?)");
		for (ElectricityBill consumerData : listOfConsumers) {
			preparedStatement.setString(1, consumerData.getConsumerNumber());
			preparedStatement.setString(2, consumerData.getConsumerName());
			preparedStatement.setString(3, consumerData.getConsumerAddress());
			preparedStatement.setInt(4, consumerData.getUnitsConsumed());
			preparedStatement.setDouble(5, consumerData.getBillAmount());
			preparedStatement.execute();
		}
		connection.commit();
		connection.close();
	}

}
