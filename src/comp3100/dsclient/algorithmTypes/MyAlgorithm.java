package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.DSClient;
import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MyAlgorithm implements Algorithm {

    Server[] servers;

    Map<Job, Server> jobServerMap;

    @Override
    public void setup(Server[] serverList) {
        servers = serverList;

        jobServerMap = new HashMap<>();
    }

    @Override
    public void run(DSClient client, Job job) throws IOException {
        run(client, job, false);
    }

    public void run(DSClient client, Job job, boolean reschedule) throws IOException {
        for (var server : Arrays.stream(servers)
            .sorted(Comparator.comparingInt(Server::getAvailableCores)).toList()) {
            if (server.canFitJobCurrently(job)) {
                if (server.scheduleJob(client, job)) {
                    jobServerMap.put(job, server);
                    return;
                }
            }
        }

        for (var server : Arrays.stream(servers)
            .sorted(Comparator.comparingInt(s -> s.currentJobsRunning.stream().map(s2 -> s2.estRuntime).reduce(Integer::sum).orElse(0))).toList()) {
            if (server.canFitJob(job)) {
                if (server.scheduleJob(client, job)) {
                    jobServerMap.put(job, server);
                    return;
                }
            }
        }
    }

    @Override
    public void jobCompletion(int jobId) {
        var job = jobServerMap.keySet().stream().filter(j -> j.id == jobId).findFirst().orElse(null);

        if (job == null) {
            return;
        }

        var server = jobServerMap.get(job);
        var test = server.currentJobsRunning.removeIf(p -> p.id == job.id);

        jobServerMap.remove(job);
    }
}
