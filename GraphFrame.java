/**
 * Builds the Frame of the GUI. 
 * creates tools Panel and Menu bar and their components for the frame
 * Handles initial sorting of data from file chosen by the user
 * contains main method
 * @author Daisy Rendell
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;

public class GraphFrame extends JFrame {
	private JComboBox<String> mycomboBox;
	DefaultComboBoxModel<String> srcModel, destModel;
	private JRadioButton radioButtonsrc, radioButtondest;
	private Font font1 = new Font("Sans-serif", Font.PLAIN, 20);
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem fileMenuOpen;
	private JMenuItem fileMenuQuit;
	private ArrayList<Packet> packets;
	private JPanel panel = new GraphPanel();
	
    /**
     * Main Method
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					new GraphFrame();
				} catch (IOException e) {
					System.out.print("The system cannot find the file specified");
				}
            }
        });
    }
    
    /**
     * Class Constructor
     * @throws IOException if Menu creation throws
     */
    public GraphFrame() throws IOException {
		super("Ass2");
		setupRadioButtons();
		getContentPane().add(setUpToolsPanel(), BorderLayout.NORTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar = new JMenuBar();
		Menu(menuBar);
		setJMenuBar(menuBar);
		setSize(800, 600);
		setVisible(true);
	    panel.setVisible
	    (true);
	    add(panel);
    }
    
    /**
     * Set up the Menu bar
     * @param menuBar JmenuBar 
     * @throws IOException if method setUpListeners() throws
     */
    private void Menu(JMenuBar menuBar) throws IOException {
    	setUpFileMenu();
		setUpListeners();
		menuBar.add(fileMenu);
	}
    
    /**
     * Set up the Radio Buttons - Source hosts and Destination hosts
     * 'Source hosts' is Selected by default. 
     * Selection of button results in filling the comboBox with selectable hosts
     */
	private void setupRadioButtons() {
		radioButtonsrc = new JRadioButton("Source hosts");
		radioButtondest = new JRadioButton("Destination hosts");
		radioButtonsrc.setFont(font1);
		radioButtondest.setFont(font1);
		radioButtonsrc.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(radioButtonsrc);
		group.add(radioButtondest);
		
		radioButtonsrc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mycomboBox.isVisible() == true) {
					mycomboBox.setModel(srcModel);
				}
			}
		});
		radioButtondest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mycomboBox.isVisible() == true) {
					mycomboBox.setModel(destModel);
				}
			}
		});
	}
	
	/**
	 * Set up tools Panel with radio buttons and comboBox
	 * Selection of ComboBox item results in repaint of JPanel with appropriate graph data
	 * corresponding to the chosen IP Address 
	 * @return toolsPanel containing the two radio buttons and a comboBox
	 */
	public JPanel setUpToolsPanel() {
		mycomboBox = new JComboBox<String>();
		mycomboBox.setVisible(false);
		mycomboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String IPAddress = (String)mycomboBox.getSelectedItem();
					GraphPanel.getGraphData(packets, IPAddress, radioButtonsrc.isSelected(),2);
					panel.repaint();
				}
			}
		});
		
		JPanel toolsPanel = new JPanel();
		toolsPanel.setBorder(new EmptyBorder(10, 20, 10, 150));
		JPanel buttonsPanel = new JPanel();
		toolsPanel.setLayout(new BorderLayout());
		buttonsPanel.setLayout(new GridLayout(2,1));
		buttonsPanel.add(radioButtonsrc);
		buttonsPanel.add(radioButtondest);
		toolsPanel.add(buttonsPanel,BorderLayout.LINE_START);
		mycomboBox.setPreferredSize(new Dimension(300,20));
		toolsPanel.add(mycomboBox, BorderLayout.LINE_END);
		return toolsPanel;
    }
	
	/**
	 * Listeners for the JMenuItems
	 * Handles file opening and applicable file data
	 * Calls repaint to fill Jpanel with data from the first IP Address in the ComboBox once 
	 * the trace file is loaded to indicate that the file has loaded. 
	 * ComboBox is set to "Source Hosts" be default
	 * @throws IOException if the specified file can't be found
	 */
	private void setUpListeners() throws IOException {
		fileMenuQuit.addActionListener(new ActionListener() {
			 public void actionPerformed (ActionEvent e) {
			  System.exit(0);
			 }
		});
		
		fileMenuOpen.addActionListener(new ActionListener(){
			 public void actionPerformed (ActionEvent e) {
				 JFileChooser chooser = new JFileChooser();
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
				 chooser.setFileFilter(filter);
				 int returnVal = chooser.showOpenDialog(menuBar.getParent());
				 if(returnVal == JFileChooser.APPROVE_OPTION) {
					 mycomboBox.setVisible(true);
					 try {
						packets = createValidPacketList(chooser.getSelectedFile());
					} catch (IOException e1) {
						 System.out.print("The system cannot find the file specified");
					}
					 String labels1[] = getSourceHosts(); //get sourceIP
					 srcModel = new DefaultComboBoxModel<String>(labels1);
					 String labels2[] = getDestHosts(); ; //get destIPs
					 destModel = new DefaultComboBoxModel<String>(labels2);
					 mycomboBox.setModel(srcModel);
					 String IPAddress = (String)mycomboBox.getSelectedItem();
					 GraphPanel.getGraphData(packets, IPAddress, radioButtonsrc.isSelected(), 2);
					 panel.repaint();
				 } else if(returnVal == JFileChooser.ERROR_OPTION) {
					 System.out.print("The system cannot find the file specified");
				 }
			 }
			});	
	}
	
	/**
	 * creates JMenu to add to JMenuBar
	 * JMenu 'File' contains two items 'Quit' and 'Open file'
	 * @return fileMenu 
	 */
	private JMenu setUpFileMenu() {
		fileMenu = new JMenu ("File");
		fileMenu.setFont(font1);
		fileMenuOpen = new JMenuItem ("Open trace file"); //Opens a JFileChooser
		fileMenuQuit = new JMenuItem ("Quit");
		fileMenuOpen.setFont(font1);
		fileMenuQuit.setFont(font1);
		fileMenu.add(fileMenuOpen);
		fileMenu.add(fileMenuQuit);
		return fileMenu;
	}
	
	/**
	 * collects Packets from the file with valid IP addresses
	 * adds suitable Packets to an ArrayList
	 * @param File f
	 * @return ArrayList of packets 
	 * @throws IOException if system can't find the requested file
	 */
	public static ArrayList<Packet> createValidPacketList(File f) throws IOException {
	    ArrayList<Packet> packets = new ArrayList<Packet>();
	    Packet packet = null;
	    try{
	        BufferedReader br = new BufferedReader(new FileReader(f)); 
	        String st; 
	        while ((st = br.readLine()) != null) {
	            packet = new Packet(st);
	            if(packet.getSourceHost().matches("^192\\.168\\.0.((0|\\d{1,2}|(1\\d\\d)|(2[0-4]\\d)|25[0-5])$)$")){
	                packets.add(packet);
	                System.out.print("added");
	            }
	        } 
	        br.close();
	    }  catch(IOException e1){
	        System.out.print(f.getName() +" (The system cannot find the file specified)");
	    }
	    return packets;
	}
	
	/**
	 * Gets Unique sorted source Host objects from the Array of Packets
	 * puts Hosts into an array
	 * @param packets
	 * @return array of unique sorted Hosts
	 */
	private static Object[] getUniqueSortedSourceHosts(ArrayList<Packet> packets) {
	    Set<String> set = new HashSet<>();
	    ArrayList<Object> list = new ArrayList<Object>();
	    for(Packet packet: packets){
	        set.add(packet.getSourceHost());
	    }
	    for(String src : set){
	        Host host = new Host(src);
	        list.add(host);
	    }
	    Object[] array = list.toArray(); 
	    Arrays.sort(array);
	    return array;
	}
	
	/**
	 * Get Unique sorted Destination Host objects from the Array of packets
	 * puts Hosts into an array
	 * @param packets
	 * @return array of unique sorted Hosts 
	 */
	private static Object[] getUniqueSortedDestHosts(ArrayList<Packet> packets) {
	    Set<String> set = new HashSet<>();
	    ArrayList<Object> list = new ArrayList<Object>();
	    for(Packet packet: packets){
	        set.add(packet.getDestinationHost());
	    }
	    for(String src : set){
	        Host host = new Host(src);
	        list.add(host);
	    }
	    Object[] array = list.toArray();
	    Arrays.sort(array);
	    return array;
	}
	
	/**
	 * Turns array of destination hosts into array of strings
	 * @return String array of Hosts 
	 */
	public String[] getDestHosts() {
		Object[] hosts = getUniqueSortedDestHosts(packets);
		String[] arr = new String[hosts.length];
		for(int i = 0; i < hosts.length; i++) {
			arr[i] = (hosts[i].toString());
		}
		return arr;
		
	}
	
	/**
	 * Turns array of Source hosts into array of strings
	 * @return String array of Hosts 
	 */
	public String[] getSourceHosts() {
		Object[] hosts = getUniqueSortedSourceHosts(packets);
		String[] arr = new String[hosts.length];
		for(int i = 0; i < hosts.length; i++) {
			arr[i] = (hosts[i].toString());
		}
		return arr;	
	}
}

