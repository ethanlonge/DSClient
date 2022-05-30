package comp3100.dsclient;

public class Job {
    public int id;
    public JobType type;

    public int submitTime;
    public int estRuntime;


    public int cores;
    public int memory;
    public int disk;

    /**
     * Create a Job object from a string
     * @param str Job string (from DS-Server)
     * @return Created Job Object
     */
    public static Job fromString(String str)
    {
        var job = new Job();

        var parts = str.split(" ");

        switch (parts[0]) {
            case "JOBN" -> job.type = JobType.NORMAL;
            case "JOBP" -> job.type = JobType.FAILED;
        }

        job.submitTime = Integer.parseInt(parts[1]);
        job.id = Integer.parseInt(parts[2]);
        job.estRuntime = Integer.parseInt(parts[3]);
        job.cores = Integer.parseInt(parts[4]);
        job.memory = Integer.parseInt(parts[5]);
        job.disk = Integer.parseInt(parts[6]);

        return job;
    }
}
