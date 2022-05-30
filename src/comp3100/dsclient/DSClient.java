package comp3100.dsclient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class DSClient {
    Socket s;
    BufferedReader din;
    DataOutputStream dout;

    public DSClient(Socket s) throws IOException {
        this.s = s;
        din = new BufferedReader(new InputStreamReader(s.getInputStream()));
        dout = new DataOutputStream(s.getOutputStream());
    }

    /**
     * Gets all servers from DS-Server
     * @return List of valid servers
     * @throws IOException Communication error with server
     */
    public Server[] getAllServers() throws IOException {
        System.out.println("Getting all servers");

        dout.write(("GETS All\n").getBytes());

        var metadata = din.readLine().split(" ");

        dout.write("OK\n".getBytes());

        if (metadata[0].equals(".")) { // Check if no servers
            dout.write("OK\n".getBytes());
            return new Server[]{};
        }

        var maxServerLen = Integer.parseInt(metadata[1]);

        var servers = new Server[maxServerLen];

        for (var i = 0; i < maxServerLen; i++) {
            servers[i] = Server.fromString(din.readLine());
        }

        dout.write("OK\n".getBytes());
        din.readLine(); // Clear '.' after getting servers

        return servers;
    }

    /**
     * Not implemented yet...
     * TODO: Implement DSClient.getCapableServers method
     *
     * @param core Number of cores
     * @param memory Memory Size (in MiB)
     * @param disk Disk Size (in MiB)
     * @return List of capable servers calculated by DS-Server
     * @throws IOException Communication error with server
     */
    public Server[] getCapableServers(int core, int memory, int disk) throws IOException
    {
        System.out.println("Getting capable servers");

        dout.write(("GETS Capable " + core + " " + memory + " " + disk + "\n").getBytes());

        var metadata = din.readLine().split(" ");

        dout.write("OK\n".getBytes());

        if (metadata[0].equals(".")) { // Check if no available servers
            return new Server[]{};
        }

        var maxServerLen = Integer.parseInt(metadata[1]);

        var servers = new Server[maxServerLen];

        for (var i = 0; i < maxServerLen; i++) {
            servers[i] = Server.fromString(din.readLine());
        }

        dout.write("OK\n".getBytes());
        din.readLine(); // Clear '.' after getting servers

        return servers;
    }
//
//    public Server[] getAvailableServers(int core, int memory, int disk) throws IOException
//    {
//        System.out.println("Getting available servers");
//
//        dout.write(("GETS Avail " + core + " " + memory + " " + disk + "\n").getBytes());
//
//        var metadata = din.readLine().split(" ");
//
//        dout.write("OK\n".getBytes());
//
//        if (metadata[0].equals(".")) { // Check if no servers
//            din.readLine(); // Clear '.' after getting servers
//            return new Server[]{};
//        }
//
//        var maxServerLen = Integer.parseInt(metadata[1]);
//
//        var servers = new Server[maxServerLen];
//
//        for (var i = 0; i < maxServerLen; i++) {
//            servers[i] = Server.fromString(din.readLine());
//        }
//
//        dout.write("OK\n".getBytes());
//        din.readLine(); // Clear '.' after getting servers
//
//        return servers;
//    }

    /**
     * Schedules a Job on a Server using DS-Server
     * @param job Job to schedule
     * @param server Server to schedule Job on
     * @return Scheduling Successful
     * @throws IOException Communication error with server
     */
    public boolean scheduleJob(Job job, Server server) throws IOException {
        System.out.println("Scheduling job (" + job.id + ") on server (" + server.type + " - " + server.id + ")");

        dout.write(("SCHD " + job.id + " " + server.type + " " + server.id + "\n").getBytes());

        if (din.readLine().startsWith("OK")) { // Clear out OK
            System.out.println("Scheduled job (" + job.id + ") successfully!");

            return true;
        }

        return false;
    }

    public boolean rescheduleJob(Job job, Server oldServer, Server newServer) throws IOException {
        System.out.println("Rescheduling job (" + job.id + ") on server (" + newServer.type + " - " + newServer.id + ")");

        dout.write(("MIGJ " + job.id + " " + oldServer.type + " " + oldServer.id + " " + newServer.type + "" + newServer.id + "\n").getBytes());

        if (din.readLine().startsWith("OK")) { // Clear out OK
            System.out.println("Rescheduled job (" + job.id + ") successfully!");

            return true;
        }

        return false;
    }

    public boolean terminateServer(Server server) throws IOException {
        System.out.println("Terminating server (" + server.type + " - " + server.id + ")");

        dout.write(("TERM " + server.type + " " + server.id + "\n").getBytes());

        if (din.readLine().startsWith("OK")) { // Clear out OK
            System.out.println("Terminated server (" + server.type + "-" + server.id + ") successfully!");

            return true;
        }

        return false;
    }

    public boolean killJob(Job job) throws IOException {
        System.out.println("Killing job (" + job.id + ")");

        dout.write(("KILLJ " + job.id + "\n").getBytes());

        if (din.readLine().startsWith("OK")) { // Clear out OK
            System.out.println("Killed job (" + job.id + ") successfully!");

            return true;
        }

        return false;
    }

    /**
     * Does the handshake process with the server so that the server can start processing requests
     * @return Success handshaking
     * @throws IOException Communication error with server
     */
    public boolean doHandshake() throws IOException {
        var username = System.getProperty("user.name");

        System.out.println("C: HELO");
        dout.write("HELO\n".getBytes());

        System.out.println("Waiting for S Response");
        //noinspection StatementWithEmptyBody
        while (!din.readLine().equals("OK")) { } // Could be redone for elegance, but oh well it works, TODO: make handshake elegant
        System.out.println("S: OK");

        System.out.println("C: AUTH " + username);
        dout.write(("AUTH " + username + "\n").getBytes());

        var sres = "";
        while(!(sres = din.readLine()).equals("OK")) {
            System.out.println("S: " + sres);
        }
        System.out.println("S: OK");

        System.out.println("C: REDY");
        dout.write("REDY\n".getBytes());

        return true;
    }

    /**
     * Sends a REDY command to server
     * @throws IOException Communication error with server
     */
    public void sendRedy() throws IOException {
        dout.write("REDY\n".getBytes());
    }

    /**
     * Reads line from Input
     * @return Line of input
     * @throws IOException Communication error with server
     */
    public String readLine() throws IOException {
        return din.readLine();
    }

    /**
     * Sends a QUIT command to server
     * @throws IOException Communication error with server
     */
    public void quit() throws IOException {
        dout.write("QUIT\n".getBytes());
    }
}
