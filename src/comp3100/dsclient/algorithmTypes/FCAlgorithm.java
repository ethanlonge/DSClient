package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.DSClient;
import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import java.io.IOException;

public class FCAlgorithm implements Algorithm {

    @Override
    public void setup(Server[] serverList) {

    }

    @Override
    public void run(DSClient client, Job job) throws IOException {
        var servers = client.getCapableServers(job.cores, job.memory, job.disk);

        client.scheduleJob(job, servers[0]);
    }
}
