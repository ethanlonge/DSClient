package comp3100.dsclient;

import comp3100.dsclient.algorithmTypes.Algorithm;
import comp3100.dsclient.algorithmTypes.AtlAlgorithm;
import comp3100.dsclient.algorithmTypes.LrrAlgorithm;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class Main {

    public Main(Algorithm algorithm) {
        try {
            var s = new Socket("localhost", 50000);

            var client = new DSClient(s);

            if (!client.doHandshake()) {
                return;
            }

            var buf = client.din.readLine();

            var servers = client.getAllServers();

            algorithm.setup(servers);

            while (!buf.equals("NONE")) {
                if (buf.startsWith("JOB")) {
                    // Job Execution
                    var job = Job.fromString(buf);

                    if (job.type != JobType.NORMAL)
                        continue;

                    System.out.println("New Job (" + job.id + ")!");

                    algorithm.run(client, job);

                    client.sendRedy();
                }
                if (buf.startsWith("JCPL")) {
                    client.dout.write("REDY\n".getBytes());
                }

                buf = client.din.readLine();
            }

            System.out.println("Completed all jobs!");
            client.quit();

        } catch (ConnectException e) {
            System.out.println("Connection Refused from DS-Server (Is it running?)");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException occured on " + e.getStackTrace()[0].getFileName() + ":L" + e.getStackTrace()[0].getLineNumber());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            if (args[0].equals("-a")) {
                switch(args[1]) {
                    case "lrr":
                        new Main(new LrrAlgorithm());
                        System.exit(0);
                        break;
                    case "atl":
                        new Main(new AtlAlgorithm());
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Algorithm \"" + args[1] + "\" does not exist");
                        System.exit(1);
                }
            }
        } else {
            new Main(new LrrAlgorithm());
        }
    }
}
