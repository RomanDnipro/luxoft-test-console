import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileExecutor {
    private static final int NUMBER_OF_THREADS = 10;

    /**
     * Concurrently handle a files list, put each file at map as key
     * and statistic object as value of it
     *
     * @param list files list
     * @param map  target map
     */
    public static void handleFilesList(List<File> list, HashMap<File, FileStatisticForEachLine> map) {

        ExecutorService service = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (File file : list) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        map.put(file, new FileStatisticForEachLine(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        service.shutdown();
    }
}
