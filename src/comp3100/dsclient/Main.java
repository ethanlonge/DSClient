package comp3100.dsclient;

import comp3100.dsclient.algorithmTypes.Algorithm;
import comp3100.dsclient.algorithmTypes.AtlAlgorithm;
import comp3100.dsclient.algorithmTypes.LrrAlgorithm;
import comp3100.dsclient.algorithmTypes.FFAlgorithm;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Main {

    /**
     * Runs the algorithm through the server event loop
     * @param algorithm Algorithm to schedule servers with
     */
    public static void run(Algorithm algorithm) {
        try {
            // Initiate connection with DS-Server over default port and create a DSClient instance
            var s = new Socket("localhost", 50000);
            var client = new DSClient(s);

            if (!client.doHandshake()) { // Do handshake on DS-Server to continue code
                return;
            }

            // Server sends Job first before you can query it at all >:(
            var buf = client.readLine();

            // Get list of available servers and pass it to the algorithm's Setup method
            var servers = client.getAllServers();
            algorithm.setup(servers);

            while (!buf.equals("NONE")) { // TODO: Create a proper switching of server commands (potentially use enums and switch?)
                if (buf.startsWith("JOB")) { // Get any Job Type and schedule it using the algorithm
                    // Job Execution
                    var job = Job.fromString(buf);

                    if (job.type != JobType.NORMAL)
                        continue;

                    System.out.println("New Job (" + job.id + ")!");

                    algorithm.run(client, job);

                    client.sendRedy();
                }
                if (buf.startsWith("JCPL")) { // Ignore Job statuses for Stage 1
                    client.sendRedy();
                }

                buf = client.readLine(); // Read next server event
            }

            System.out.println("Completed all jobs!");
            client.quit();

        } catch (ConnectException e) { // 9/10 times this catches because the server isn't running
            System.out.println("Connection Refused from DS-Server (Is it running?)");
            System.exit(1);
        } catch (IOException e) { // Rare exception but can run into it
            System.out.println("IOException occured on " + e.getStackTrace()[0].getFileName() + ":L" + e.getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
    }

    /**
     * CLI Entrypoint of the DSClient project. Does basic switching for algorithms
     * @param args Arguments from CLI
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            // Switch algorithm from '-a' flag if it exists
            if (args[0].equals("-a")) {
                switch (args[1]) {
                    case "lrr" -> {
                        Main.run(new LrrAlgorithm());
                        System.exit(0);
                    }
                    case "atl" -> {
                        Main.run(new AtlAlgorithm());
                        System.exit(0);
                    }
                    case "ff" -> {
                        Main.run(new FFAlgorithm());
                        System.exit(0);
                    }
                    default -> { // Algorithm invalid
                        System.out.println("Algorithm \"" + args[1] + "\" does not exist");
                        System.exit(1);
                    }
                }
            }
        } else { // Default algorithm = LRR
            Main.run(new LrrAlgorithm());
        }
    }
}
