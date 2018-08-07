package topbloc;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

public class ParseData {

	private static Data createDataObject(String filePath) throws IOException {

		Data newDataSet = null; // Create data object to be returned
		ArrayList<Double> num1 = new ArrayList<Double>();
		ArrayList<Double> num2 = new ArrayList<Double>();
		ArrayList<String> words = new ArrayList<String>();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
			Workbook workbook = new XSSFWorkbook(inputStream);

			int numSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numSheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				Iterator<Row> rowIterator = sheet.rowIterator();

				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0) {
						continue;
					}
					Iterator<Cell> cells = row.cellIterator();

					while (cells.hasNext()) {

						Cell cell = cells.next();

						System.out.println(cell.getRowIndex());
						if (cell.getColumnIndex() == 0) {
							System.out.println("Cell Index: " + cell.getRowIndex());
							Double cellNum = cell.getNumericCellValue();
							num1.add(cellNum);

						}
						if (cell.getColumnIndex() == 1) {

							Double cellNum = cell.getNumericCellValue();
							num2.add(cellNum);
							System.out.println("Cell Index: " + cell.getRowIndex());
						}
						if (cell.getColumnIndex() == 2) {
							System.out.println("Cell Index: " + cell.getRowIndex());

							words.add(cell.getStringCellValue());

						}
						// }
					}
				}
				newDataSet = new Data(num1, num2, words);
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newDataSet;
	}

	public static Double[] multiplyArr(ArrayList<Double> arr1, ArrayList<Double> arr2) {
		Double[] multArr = new Double[arr1.size()];
		for (int i = 0; i < arr1.size(); i++) {
			Double newNum = arr1.get(i) * arr2.get(i);
			multArr[i] = newNum;
		}

		return multArr;
	}

	public static Double[] divideArr(ArrayList<Double> arr1, ArrayList<Double> arr2) {
		Double[] divArr = new Double[arr1.size()];
		for (int i = 0; i < arr1.size(); i++) {
			Double newNum = arr2.get(i) / arr1.get(i);
			divArr[i] = newNum;
		}

		return divArr;
	}

	public static String[] concatArr(ArrayList<String> arr1, ArrayList<String> arr2) {
		String[] catArr = new String[arr1.size()];
		for (int i = 0; i < arr1.size(); i++) {
			String concatString = arr1.get(i) + " " + arr2.get(i);
			catArr[i] = concatString;
		}

		return catArr;
	}

	public static void useJSON(JSONObject data) {

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost("http://34.239.125.159:5000/challenge");
			StringEntity jsonEntity = new StringEntity(data.toString());
			post.setEntity(jsonEntity);
			post.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(post);
			System.out.println(response.toString());

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
	}

	public static void main(String[] args) throws Exception {
		String filePath1 = "/Users/Groebe_1/Documents/workspaceSG/topbloc/src/main/resources/Data1.xlsx";
		String filePath2 = "/Users/Groebe_1/Documents/workspaceSG/topbloc/src/main/resources/Data2.xlsx";
		Data dataSet1 = createDataObject(filePath1);
		Data dataSet2 = createDataObject(filePath2);

		Double[] setOneArr = multiplyArr(dataSet1.getNumSet1(), dataSet2.getNumSet1());
		Double[] setTwoArr = divideArr(dataSet1.getNumSet2(), dataSet2.getNumSet2());
		String[] combinedArr = concatArr(dataSet1.getWordSet(), dataSet2.getWordSet());

		JSONObject jObj = new JSONObject();
		jObj.put("id", "sean.groebe25@gmail.com");
		JSONArray numSet1 = new JSONArray();
		JSONArray numSet2 = new JSONArray();
		JSONArray wordSet1 = new JSONArray();
		for (int i = 0; i < setOneArr.length; i++) {
			numSet1.add(setOneArr[i]);
			numSet2.add(setTwoArr[i]);
			wordSet1.add(combinedArr[i]);
		}
		jObj.put("numberSetOne", numSet1);
		jObj.put("numberSetTwo", numSet2);
		jObj.put("wordSetOne", wordSet1);

		useJSON(jObj);

	}
}
