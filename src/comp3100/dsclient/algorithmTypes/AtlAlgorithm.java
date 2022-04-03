package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import comp3100.dsclient.DSClient;
import java.io.IOException;

/**
 * This Algorithm finds the first largest server (determined by core count) and allocates all jobs
 * to it
 */
public class AtlAlgorithm implements Algorithm {
    Server atlServer;

    @Override
    public void setup(Server[] serverList) {
        // Find the largest server and allocate it to atlServer
        var largestCore = 0;
        Server largestServ = null;
        for (Server server : serverList) {
            if (server.cores > largestCore) {
                largestCore = server.cores;
                largestServ = server;
            }
        }

        atlServer = largestServ;
    }

    @Override
    public void run(DSClient client, Job job) throws IOException {
        // Schedule all jobs to found server
        client.scheduleJob(job, atlServer);
    }
}
