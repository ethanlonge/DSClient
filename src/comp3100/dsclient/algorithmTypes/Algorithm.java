package comp3100.dsclient.algorithmTypes;

import comp3100.dsclient.DSClient;
import comp3100.dsclient.Job;
import comp3100.dsclient.Server;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public interface Algorithm {
    public void setup(Server[] serverList);

    public void run(DSClient client, Job job) throws IOException;

    public default void jobCompletion(int jobId) {

    }
}
