/**
 * Packet Object
 * Contains information about one line in the Trace File
 * Splits the information in the packet into useable variables. 
 * @author grace
 *
 */
class Packet {
    double timestamp = 0.00;
    String srcHost;
    String destHost; 
    int ipPacketSize;
    private String[] split = new String[10];
    
    /**
     * Constructor method
     * @param str line of text containing Packet information
     */
    public Packet(String str){
        split = str.split("\\t", -1);
        
        if(split[1] != null && split[1].length() > 0){
            this.timestamp = Double.valueOf(split[1]);
        }
        if(split[7] != null && split[7].length() > 0){
            this.ipPacketSize = Integer.valueOf(split[7]);
        }
        this.srcHost = split[2];
        this.destHost = split[4];
    }
    
    /**
     * gets the source IP Address
     * @return source IP Address
     */
    public String getSourceHost(){
        return this.srcHost;
    }
    
    /**
     * gets the destination IP Address
     * @return destination IP Address
     */
    public String getDestinationHost(){
        return this.destHost;
    }
    
    /**
     * gets the time stamp of the packet
     * @return time stamp
     */
    public double getTimeStamp(){
        return this.timestamp;
    }
    
    /**
     * gets the IP packet size
     * @return IP packet size
     */
    public int getIpPacketSize(){
        return this.ipPacketSize;
    }
    
    /**
     * turns the useful information from the packet into a string 
     */
    public String toString(){
        String ip = Integer.toString(this.ipPacketSize);
        String ts = String.format("%.2f", this.timestamp);
        return "src="+srcHost+", dest="+destHost+", time="+ts+", size="+ip;
    }
}
    
    