package comp3100.dsclient;

public class Server {
    public String type;
    public int id;
    public ServerStatus status;

    public int startupTime;

    public int cores;
    public int memory;
    public int storage;

    /**
     * Create a Server object from a string
     * @param str Server string (from DS-Server)
     * @return Created Server Object
     */
    public static Server fromString(String str) {
        var vals = str.split(" ");

        var serv = new Server();
        serv.type = vals[0];
        serv.id = Integer.parseInt(vals[1]);
        serv.status = ServerStatus.valueOf(vals[2].toUpperCase());
        serv.startupTime = Integer.parseInt(vals[3]);
        serv.cores = Integer.parseInt(vals[4]);
        serv.memory = Integer.parseInt(vals[5]);
        serv.storage = Integer.parseInt(vals[6]);

        return serv;
    }
}
