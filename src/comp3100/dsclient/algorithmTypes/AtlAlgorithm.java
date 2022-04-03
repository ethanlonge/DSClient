package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import comp3100.dsclient.DSClient;
import java.io.IOException;

public class AtlAlgorithm implements Algorithm {
    Server atlServer;

    @Override
    public void setup(Server[] serverList) {
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
        client.scheduleJob(job, atlServer);
    }
}
