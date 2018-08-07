package topbloc;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;


import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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

	public static ArrayList<Double> multiplyArr(ArrayList<Double> arr1, ArrayList<Double> arr2) {
		ArrayList<Double> multArr = new ArrayList<Double>();
		for (int i = 0; i < arr1.size(); i++) {
			Double newNum = arr1.get(i) * arr2.get(i);
			multArr.add(newNum);
		}

		return multArr;
	}

	public static ArrayList<Double> divideArr(ArrayList<Double> arr1, ArrayList<Double> arr2) {
		ArrayList<Double> divArr = new ArrayList<Double>();
		for (int i = 0; i < arr1.size(); i++) {
			Double newNum = arr2.get(i) / arr1.get(i);
			divArr.add(newNum);
		}

		return divArr;
	}

	public static ArrayList<String> concatArr(ArrayList<String> arr1, ArrayList<String> arr2) {
		ArrayList<String> catArr = new ArrayList<String>();
		for (int i = 0; i < arr1.size(); i++) {
			String concatString = arr1.get(i) + " " + arr2.get(i);
			catArr.add(concatString);
		}

		return catArr;
	}

	// Helper methods to allow Array members to be transferred into strings
	public static String doubleToStr(ArrayList<Double> array) {
		StringBuilder setString = new StringBuilder();
		for (Double s : array) {
			setString.append(s.toString());
			setString.append("\t");
		}
		return setString.toString();
	}

	public static String arrToStr(ArrayList<String> array) {
		StringBuilder setString = new StringBuilder();
		for (String s : array) {
			setString.append(s.toString());
			setString.append("\t");
		}
		return setString.toString();

	}

	public static void useJSON(ArrayList<Double> setOneArr, ArrayList<Double> setTwoArr, ArrayList<String> combinedArr)
			throws Exception {

		String urlStr = "http://34.239.125.159:5000/challenge";

	
		String setOneString = doubleToStr(setOneArr);
		String setTwoString = doubleToStr(setTwoArr);
		String wordString = arrToStr(combinedArr);

		StringEntity postObj = createEntity(setOneString, setTwoString, wordString);

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(urlStr);
		request.setEntity(postObj);
		HttpResponse response = client.execute(request);
		System.out.println(response.getStatusLine().getStatusCode());
	}

	public static StringEntity createEntity(String numSet1, String numSet2, String wordSet1) {
		String postData = "data={" + "\"id\": \"sean.groebe25@gmail.com\", " + "\"numberSetOne\": \"" + numSet1 + "\", "
				+ "\"numberSetTwo\": \"" + numSet2 + "\", " + "\"wordSetOne\": \"" + wordSet1 + "\"" + "}";
		StringEntity entity = new StringEntity(postData, ContentType.APPLICATION_FORM_URLENCODED);
		return entity;
	}


	public static void main(String[] args) throws Exception {
		String filePath1 = "/Users/Groebe_1/Documents/workspaceSG/topbloc/src/main/resources/Data1.xlsx";
		String filePath2 = "/Users/Groebe_1/Documents/workspaceSG/topbloc/src/main/resources/Data2.xlsx";
		Data dataSet1 = createDataObject(filePath1);
		Data dataSet2 = createDataObject(filePath2);

		ArrayList<Double> setOneArr = multiplyArr(dataSet1.getNumSet1(), dataSet2.getNumSet1());
		ArrayList<Double> setTwoArr = divideArr(dataSet1.getNumSet2(), dataSet2.getNumSet2());
		ArrayList<String> combinedArr = concatArr(dataSet1.getWordSet(), dataSet2.getWordSet());
		useJSON(setOneArr, setTwoArr, combinedArr);
		

	}
}
