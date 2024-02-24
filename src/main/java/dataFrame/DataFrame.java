package dataFrame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class DataFrame {
	private String runsData;
	
	public DataFrame() {
		runsData = "resources/Memory/Runs.csv";
	}
	
	public void saveRun(List<String[]> runs) {
		//System.out.println(runs.toString());
		try {
			FileWriter fw = new FileWriter(runsData);
			CSVWriter cs = new CSVWriter(fw);
			
			String[] header = {"Animat","Action","Location x:","Location y:"};

			cs.writeNext(header);
			cs.writeAll(runs);
			cs.close();
			
		}catch(IOException e) {
			
		}
	}
	
	public void saveRun(String x, List<String[]> runs) {
		runsData = "resources/Memory/"+x+".csv";
		try {
			FileWriter fw = new FileWriter(runsData);
			CSVWriter cs = new CSVWriter(fw);
			
			String[] header = {"Animat","Day","Location x:","Location y:"};
			String[] footer = {runs.size()+""};

			cs.writeNext(header);
			cs.writeAll(runs);
			cs.writeNext(footer);
			cs.close();
			
		}catch(IOException e) {
			
		}
	}
	public void saveRun(String x, List<String[]> runs, String[] header) {
		runsData = "resources/Memory/"+x+".csv";
		try {
			FileWriter fw = new FileWriter(runsData);
			CSVWriter cs = new CSVWriter(fw);

			cs.writeNext(header);
			cs.writeAll(runs);
			cs.close();

		}catch(IOException e) {

		}
	}
	public void multiDaySave(String x,ArrayList<ArrayList<String[]>> runs) {
		runsData = "resources/Memory/"+x+".csv";
		try {
			FileWriter fw = new FileWriter(runsData);
			CSVWriter cs = new CSVWriter(fw);
			
			String[] header = {"Animat","Day","Location x:","Location y:","Teacher"};
			String[] footer = {runs.size()+""};

			cs.writeNext(header);
			for(ArrayList<String[]> i: runs) {
				cs.writeAll(i);
			}
			cs.writeNext(footer);
			cs.close();
			
		}catch(IOException e) {
			
		}
	}

	public ArrayList<String[]> multiDayConversion(ArrayList<ArrayList<String[]>> runs) {
		ArrayList<String[]> tmp = new ArrayList<>();
		for(ArrayList<String[]> i: runs) {
			tmp.addAll(i);
		}
		return tmp;
	}
	
	
	public ArrayList openMemory() {
		ArrayList<Object> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("resources/Memory/Memory.csv"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        records.add(Arrays.asList(values));
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}
	
	public ArrayList<String[]> openMemory(String a) {
		ArrayList<String[]> records = new ArrayList<>();
		boolean skip = true;
		try (BufferedReader br = new BufferedReader(new FileReader("resources/Memory/"+a+".csv"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	// removing header
		    	if(skip) {
		    		skip =false;
		    		continue;
		    	}
		        String[] values = line.split(",");
		        records.add(values);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return records;
	}

	/**
	 * Save inputs to a csv file
	 * @param x
	 * @param data
	 */
	public void saveInputs(String x, ArrayList<String[]> data) {
		try {
			FileWriter fw = new FileWriter("resources/Memory/"+x+".csv");
			CSVWriter cs = new CSVWriter(fw);

			String[] header = {"Animat","Nearest stone","Distance to water","Distance from start","Distance to end","Has stone"};
			String[] footer = {data.size()+""};

			cs.writeNext(header);
			cs.writeAll(data);
			cs.writeNext(footer);
			cs.close();

		}catch(IOException e) {

		}}

		/**
		 * Save data to a csv file
		 * @param x
		 * @param data
		 */
		public void saveData(String x, ArrayList<String[]> data, String[] header) {
			try {
				FileWriter fw = new FileWriter("resources/Memory/"+x+".csv");
				CSVWriter cs = new CSVWriter(fw);

				//String[] header = {"Animat","Day","Location x:","Location y:","Teacher"};
				String[] footer = {data.size()+""};

				cs.writeNext(header);
				cs.writeAll(data);
				cs.writeNext(footer);
				cs.close();

			}catch(IOException e) {

			}
		}

	
}
