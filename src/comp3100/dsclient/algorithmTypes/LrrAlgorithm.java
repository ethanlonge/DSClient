package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.DSClient;
import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LrrAlgorithm implements Algorithm {
    List<Server> lrrServers;

    int lrrIdx = 0;
    int lrrMax = 0;

    private String getLargestServerType(Server[] serverList) {
        var largestCore = 0;
        Server largestServ = null;
        for (Server server : serverList) {
            if (server.cores > largestCore) {
                largestCore = server.cores;
                largestServ = server;
            }
        }

        assert largestServ != null;

        return largestServ.type;
    }

    @Override
    public void setup(Server[] serverList) {
        var largestServerType = getLargestServerType(serverList);
        lrrServers = new ArrayList<>();

        for (Server server : serverList) {
            if (server.type.equals(largestServerType)) {
                lrrServers.add(server);
            }
        }

        lrrMax = lrrServers.size();
    }

    @Override
    public void run(DSClient client, Job job) throws IOException {
        if (lrrIdx == lrrMax)
            lrrIdx = 0;

        client.scheduleJob(job, lrrServers.get(lrrIdx++));
    }
}
