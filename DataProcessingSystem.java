import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DataProcessingSystem {

    // ---------------- TASK ----------------

    static class Task {
        private final int id;
        private final String data;

        public Task(int id, String data) {
            this.id = id;
            this.data = data;
        }

        public int getId() {
            return id;
        }

        public String getData() {
            return data;
        }
    }

    // ---------------- LOGGER ----------------

    static class Logger {

        public static synchronized void log(String message) {
            System.out.println(
                    "[" + LocalDateTime.now() + "] "
                            + message);
        }
    }

    // ---------------- SHARED QUEUE ----------------

    static class TaskQueue {

        private final Queue<Task> queue =
                new LinkedList<>();

        public synchronized void addTask(Task task) {
            queue.add(task);
        }

        public synchronized Task getTask() {

            if (queue.isEmpty()) {
                return null;
            }

            return queue.poll();
        }
    }

    // ---------------- SHARED RESULTS ----------------

    static class ResultStore {

        private final List<String> results =
                Collections.synchronizedList(
                        new ArrayList<>());

        public void addResult(String result) {
            results.add(result);
        }

        public List<String> getResults() {
            return results;
        }
    }

    // ---------------- WORKER ----------------

    static class Worker implements Runnable {

        private final String workerName;
        private final TaskQueue queue;
        private final ResultStore store;

        public Worker(
                String workerName,
                TaskQueue queue,
                ResultStore store) {

            this.workerName = workerName;
            this.queue = queue;
            this.store = store;
        }

        @Override
        public void run() {

            Logger.log(workerName + " started.");

            while (true) {

                Task task = queue.getTask();

                if (task == null) {
                    break;
                }

                try {

                    Logger.log(
                            workerName +
                                    " processing Task "
                                    + task.getId());

                    Thread.sleep(
                            (long) (Math.random() * 2000));

                    // Simulated error

                    if (task.getId() == 10) {
                        throw new RuntimeException(
                                "Simulated processing error");
                    }

                    String result =
                            "Task "
                                    + task.getId()
                                    + " processed by "
                                    + workerName;

                    store.addResult(result);

                    Logger.log(result);

                } catch (InterruptedException e) {

                    Logger.log(
                            workerName +
                                    " interrupted: "
                                    + e.getMessage());

                    Thread.currentThread().interrupt();

                } catch (Exception e) {

                    Logger.log(
                            workerName +
                                    " ERROR while processing Task "
                                    + task.getId()
                                    + ": "
                                    + e.getMessage());
                }
            }

            Logger.log(workerName + " completed.");
        }
    }

    // ---------------- FILE WRITER ----------------

    public static void saveResults(
            List<String> results,
            String fileName) {

        try (FileWriter writer =
                     new FileWriter(fileName)) {

            for (String result : results) {
                writer.write(result + "\n");
            }

            Logger.log(
                    "Results saved to "
                            + fileName);

        } catch (IOException e) {

            Logger.log(
                    "File Error: "
                            + e.getMessage());
        }
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        TaskQueue queue = new TaskQueue();
        ResultStore store = new ResultStore();

        for (int i = 1; i <= 20; i++) {

            queue.addTask(
                    new Task(
                            i,
                            "Data-" + i));
        }

        ExecutorService executor =
                Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 4; i++) {

            executor.execute(
                    new Worker(
                            "Worker-" + i,
                            queue,
                            store));
        }

        executor.shutdown();

        try {

            executor.awaitTermination(
                    5,
                    TimeUnit.MINUTES);

        } catch (InterruptedException e) {

            Logger.log(
                    "Executor interrupted.");
        }

        saveResults(
                store.getResults(),
                "results.txt");

        Logger.log(
                "Successful Results = "
                        + store.getResults().size());

        Logger.log(
                "Program Completed.");
    }
}