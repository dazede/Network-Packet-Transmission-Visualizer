/**
 * Contains draw methods for drawing the JPanel
 * Draws a graph onto the JPanel containing data from the trace file 
 * about number of bytes sent over time intervals of 2 seconds
 * @author Daisy Rendell
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GraphPanel extends JPanel {
	static double[] arr1;
	static double[] arr2; 
	static int[] yMarks; 
	static int maxTime = 650;
	static int ticks;
	static int inc;
	
	/**
	 * Constructor method
	 */
    public GraphPanel() {
    	setOpaque(true);
    	setBackground(Color.white);
		setSize(800, 500);
		setVisible(true);
	}
    
    /**
     * Paints the JPanel
     */
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.setColor(Color.black);
    	g.drawString("Volume[bytes]", 15, 15);
    	g.drawString("Time [s]", 350, 395);
    	drawBarGraph(g);
    }
    
    /**
     * calls methods to draw the bar Graph
     * @param g Graphics Panel
     */
    private void drawBarGraph(Graphics g) {
    	getXticks();
    	drawAxes(g);
    	if(arr1 != null) {
    		drawY(g);
    		drawBars(g);
    	}
    }
    
    /**
     * Draws the x and y axis on the graph
     * For initial graph seen on loading. 
     * updates x-axis when a new IP Address is selected
     * @param g Graphics panel
     */
    private void drawAxes(Graphics g) {
    	int xLen = 0;
    	int j = 0;
    	ticks = (int) Math.ceil(700/ticks);
    	g.drawLine(50, 350, 50, 30); //y-axis
    	
    	for(int i = 50; i <= 780; i+=ticks) {
    		g.drawLine(i,350,i,355);
    		String str = Integer.toString(j);
    		g.drawString(str, i-7, 370);
    		j += inc;
    		xLen = i;
    	}
    	g.drawLine(45, 350, xLen, 350); //x-axis
    }
    
    /**
     * draws the data points onto the graph as bars
     * arr1[] = x-axis coordinates
     * yMarks[] = y-axis coordinates
     * @param g Graphics panel
     */
    private void drawBars(Graphics g) {
    	for(int i = 0; i < arr1.length; i++) {
    		g.setColor(Color.blue);
    		int x = (int) (arr1[i] + 50);
    		g.drawLine(x, 350, x, yMarks[i]);
    	}
    }
    
    /**
     * draws the Y axis ticks
     * updates y-axis when a new IP Address is selected
     * creates an array of y-coordinates from the data to draw the bars on the graph
     * @param g Graphics Panel
     */
    private void drawY(Graphics g) {
    	double yMax = Arrays.stream(arr2).max().getAsDouble();
    	int increment = getYIncrement((int)yMax);
    	int ticks = (int) Math.ceil((double)yMax/(double)(increment*1000));
    	ticks = (int) Math.ceil(320/ticks);
    	int j = 0;
    	double jj;
    	
    	//draws ticks on y-axis
    	for(int i = 350; i >= 30; i-=ticks) {
    		g.drawLine(45,i,50,i);
    		String str;
    		if(j < 1000) {
    			str = Integer.toString(j)+"K";
    		} else {
    			jj = (double) j;
    			jj = jj/(double)1000;
    			str = Double.toString(jj)+"M";
    		}
    		g.drawString(str, 10, i+3);
    		j += increment;
    	}
    	
    	//array of y-axis coordinates 
    	yMarks = new int[arr2.length];
    	double tickSpace = ((double)ticks/(double)10);
    	for(int i = 0; i < arr2.length; i++) {
    		if(increment == 100) {
    			yMarks[i] = 350 - (int)(tickSpace * (arr2[i]/(double)10000));
    		} else if(increment == 100) {
    			yMarks[i] = 350 - (int)(tickSpace * (arr2[i]/(double)20000));
    		} else {
    			yMarks[i] = 350 - (int)(tickSpace * (arr2[i]/(double)40000));
    		}
    	}
    }
    
    /**
     * Provides x-axis with increment for ticks and number of ticks
     */
    public static void getXticks() {
    	if(maxTime/100 >= 10) {
    		ticks = (int) Math.ceil((double)maxTime/(double)100);
    		inc = 100;
    	} else if(maxTime/50 >= 9) {
    		ticks = (int) Math.ceil((double)maxTime/(double)50);
    		inc = 50;
    	} else if(maxTime/20 >= 9) {
    		ticks = (int) Math.ceil((double)maxTime/(double)20);
    		inc = 20;
    	} else if(maxTime/10 >= 9) {
    		ticks = (int) Math.ceil((double)maxTime/(double)10);
    		inc = 10;
    	} else if(maxTime/5 >= 9) {
    		ticks = (int) Math.ceil((double)maxTime/(double)5);
    		inc = 5;
    	} else if(maxTime/2 >= 9) {
    		ticks = (int) Math.ceil((double)maxTime/(double)2);
    		inc = 2;
    	} else {
    		ticks = maxTime;
    		inc = 1;
    	}
    }
    
    /**
     * Provides y-axis with increment for ticks
     * @param yMax largest IP Packet size
     * @return increment of y axis ticks
     */
    private int getYIncrement(int yMax) {
    	if(yMax < 1000000) {
    		return 100;
    	} else if(yMax < 2000000) {
    		return 200;
    	} else {
    		return 400;
    	}
    }
        
    /**
     * Creates two arrays. One for the x-axis one for the y-axis
     * x-axis array contains time intervals
     * y-axis array contains bytes of data corresponding to the time interval
     * @param packets ArrayLsit of packets
     * @param ipFilter the desired IP Address chosen from the comboBox
     * @param isSrcHost true if the radio Button "Source" is selected, false if "Destination" is selected
     * @param interval the time interval (2 seconds) 
     */
    public static void getGraphData(ArrayList<Packet> packets, String ipFilter, boolean isSrcHost, int interval) {
    	int endTime = getEndTime(packets, isSrcHost, ipFilter);
        int slots = (int) Math.ceil((double)endTime / interval);
        arr1 = new double[slots];
        arr2 = new double[slots];
        
      //x-coordinate array by ticks 
        int interv = interval; 
        for(int i = 0; i < slots; i++){
            arr1[i] = interval;
            interval += interv;
        }
        
        //y-coordinate array 
        for(Packet packet : packets){
            if((isSrcHost && packet.getSourceHost().matches(ipFilter))||
            (!isSrcHost && packet.getDestinationHost().matches(ipFilter))){
                for(int i = 0; i < slots; i++){
                    if(!(packet.getTimeStamp() > arr1[i])){
                        arr2[i] += packet.getIpPacketSize();
                        break;
                    }
                }
            }
        }
        maxTime = (int) endTime;
    }
    
    /**
     * returns maximum time in packets list
     * @param packets arrayList of packets
     * @param isSrcHost true if source, false if destination
     * @param ipFilter IP Address we have selected 
     * @return max time
     */
    public static int getEndTime(ArrayList<Packet> packets, boolean isSrcHost, String ipFilter) {
    	int time = 0;
    	for(Packet packet : packets){
            if((isSrcHost && packet.getSourceHost().matches(ipFilter))||
            (!isSrcHost && packet.getDestinationHost().matches(ipFilter))){
            	if(packet.getTimeStamp() > time) {
            		time = (int) Math.ceil(packet.getTimeStamp());
            	}
            }
    	}
		return time;
    }
}