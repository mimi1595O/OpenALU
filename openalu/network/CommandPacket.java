package openalu.network;

/**
 *
 * @author ASUS
 */
public class CommandPacket { // JSON architecture over text packet
    public String command;
    public String data;
    
    CommandPacket(String command, String data){
        this.command=command;
        this.data=data;
    }
    String parse(){
        return "{\"command\": \""+command+"\","
                + "\n\"data\": \""+escape(data)+"\""
                + "\n}";
    }
    
    String escape(String s) {
        return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}
