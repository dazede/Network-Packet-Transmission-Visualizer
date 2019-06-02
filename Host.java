/**
 * Host object
 * for sorting a list of IP Addresses
 * @author Daisy Rendell
 *
 */
class Host implements Comparable<Host>{
    private String ip;
    
    /**
     * Constructor Method
     * @param str IP Address
     */
    public Host(String str){
        this.ip = str;
    }
    
    /**
     * Returns the IP Address as a string
     */
    public String toString(){
        return this.ip;
    }
    
    /**
     * Compares two IP Address 
     */
    public int compareTo(Host other){
        String[] split = new String[4];
        String[] oth = new String[4];
        split = this.ip.split("\\.");
        oth = other.ip.split("\\.");
        
        for(int i = 0; i <= 3; i++){
            if(Integer.valueOf(split[i]) > Integer.valueOf(oth[i])){
                return 1;
            } else if (Integer.valueOf(split[i]) < Integer.valueOf(oth[i])){
                return -1;
            } 
        } 
        return 0;
    }
}