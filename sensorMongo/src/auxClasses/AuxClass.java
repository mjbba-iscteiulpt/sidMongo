package auxClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.SparkContext;

public class AuxClass {
	private List<Double> lastTemp = new ArrayList<Double>();
	private List<Double> lastHum = new ArrayList<Double>();

	public AuxClass() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * public List<Double> getOutliers(List<Double> input) {
	 * Collections.sort(input); List<Double> output = new ArrayList<Double>();
	 * List<Double> data1 = new ArrayList<Double>(); List<Double> data2 = new
	 * ArrayList<Double>(); if (input.size() % 2 == 0) { data1 = input.subList(0,
	 * input.size() / 2); data2 = input.subList(input.size() / 2, input.size()); }
	 * else { data1 = input.subList(0, input.size() / 2); data2 =
	 * input.subList(input.size() / 2 + 1, input.size()); } double q1 =
	 * getMedian(data1); System.out.println("Q1: "+q1); double q3 =
	 * getMedian(data2); System.out.println("Q2: "+q3); double iqr = q3 - q1;
	 * System.out.println("IQR: "+iqr); double lowerFence = q1 - 1.5 * iqr; double
	 * upperFence = q3 + 1.5 * iqr; System.out.println("Lower Fence: "+lowerFence);
	 * System.out.println("Upper Fence: "+upperFence); for (int i = 0; i <
	 * input.size(); i++) { System.out.println("Input= "+input.get(i)+" | Lower: "
	 * +lowerFence+" | Upper: "+upperFence); if (input.get(i) < lowerFence ||
	 * input.get(i) > upperFence) output.add(input.get(i)); } return output; }
	 * 
	 * private double getMedian(List<Double> data) { if (data.size() % 2 == 0)
	 * return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2; else
	 * return data.get(data.size() / 2); }
	 */

	public List<Double> getOutliers(List<Double> input) {
		Collections.sort(input);
		double median = input.get(Math.round(input.size() / 2));
		double LQ = input.get(Math.round(input.size() / 4));
		double UQ = input.get(Math.round(3 * input.size() / 4));
		double IQR = UQ - LQ;
		List<Double> data4 = new ArrayList<Double>();
		double sum = 0;
		for (double v : input)
			sum += v;
		double mean = sum / input.size();
		for (Double v : input) {
			System.out.println("median"+median+" | irq"+IQR);
			System.out.println(
					"v: " + v + " | median-2*IRQ: " + (median - 2 * IQR) + " | mean + 2 * IQR: " + mean + 2 * IQR);
			if (v > (median - 2 * IQR) && v > (mean + 2 * IQR)) {
				data4.add(v);
				System.out.println("V adicionado: " + v);
			}
		}
		return data4;
	}

	public void addTemp(double temp) {
		if (lastTemp.size() == 10)
			lastTemp.remove(0);
		lastTemp.add(temp);
	}

	public void addHum(double hum) {
		if (lastHum.size() == 10)
			lastHum.remove(0);
		lastHum.add(hum);
	}

	public List<Double> getLastTemp() {
		return lastTemp;
	}

	public List<Double> getLastHum() {
		return lastHum;
	}

}
