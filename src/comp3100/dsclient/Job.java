package comp3100.dsclient;

public class Job {
    public int id;
    public JobType type;

    public int submitTime;
    public int estRuntime;

    public int cores;
    public int memory;
    public int disk;

    public static Job fromString(String str)
    {
        var job = new Job();

        var parts = str.split(" ");

        switch (parts[0]) {
            case "JOBN" -> job.type = JobType.NORMAL;
            case "JOBP" -> job.type = JobType.FAILED;
        }

        job.id = Integer.parseInt(parts[2]);
        job.estRuntime = Integer.parseInt(parts[1]);
        job.cores = Integer.parseInt(parts[3]);
        job.memory = Integer.parseInt(parts[4]);
        job.disk = Integer.parseInt(parts[5]);

        return job;
    }
}
