package comp3100.dsclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    public String type;
    public int id;
    public ServerStatus status;

    public int startupTime;
    public int cores;
    public int memory;
    public int storage;

    public ArrayList<Job> currentJobsRunning = new ArrayList<>();

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

    public int getAvailableCores() {
        return cores - currentJobsRunning.stream().map(job -> job.cores).reduce(Integer::sum).orElse(0);
    }

    public int getAvailableMemory() {
        return memory - currentJobsRunning.stream().map(job -> job.memory).reduce(Integer::sum).orElse(0);
    }

    public int getAvailableStorage() {
        return storage - currentJobsRunning.stream().map(job -> job.disk).reduce(Integer::sum).orElse(0);
    }

    public boolean canFitJobCurrently(Job job) {
        return (
            job.cores <= getAvailableCores() &&
            job.memory <= getAvailableMemory() &&
            job.disk <= getAvailableStorage()
        );
    }

    public boolean canFitJob(Job job) {
        return (
            job.cores <= cores &&
            job.memory <= memory &&
            job.disk <= storage
        );
    }

    public boolean scheduleJob(DSClient client, Job job) throws IOException {
        if (client.scheduleJob(job, this)) {
            this.currentJobsRunning.add(job);
            return true;
        }

        return false;
    }
}
