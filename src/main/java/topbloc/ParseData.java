package topbloc;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

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
		//HttpClient client = HttpClientBuilder.create().build();
		HttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(urlStr);


		List<NameValuePair> postObj = new ArrayList<NameValuePair>();

		String setOneString = doubleToStr(setOneArr);
		String setTwoString = doubleToStr(setTwoArr);
		String wordString = arrToStr(combinedArr);
		
		postObj.add(new BasicNameValuePair("id", "sean.groebe25@gmail.com"));
		postObj.add(new BasicNameValuePair("numberSetOne", setOneString));
		postObj.add(new BasicNameValuePair("numberSetTwo", setTwoString));
		postObj.add(new BasicNameValuePair("wordSetOne", wordString));
		httpPost.setEntity(new UrlEncodedFormEntity(postObj));

		HttpResponse response = client.execute(httpPost);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
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
//		try {
//			DefaultHttpClient httpClient = new DefaultHttpClient();
//			HttpPost postRequest = new HttpPost("http://34.239.125.159:5000/challenge");
//			StringEntity input = new StringEntity("{\id\"")
//		}
//		System.out.println(setOneArr);
//		System.out.println(setTwoArr);
//		System.out.println(combinedArr);

	}
}
