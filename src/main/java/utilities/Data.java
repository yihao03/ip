package utilities;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import components.Todo;
import components.task.DeadlineTask;
import components.task.EventTask;
import components.task.Task;
import components.task.TaskType;

import java.io.IOException;
import java.nio.file.Files;

public class Data {
    private static final Path DATA_DIRECTORY = Path.of("data");
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("todo_list.txt");
    public static final String DELIMITER = "%20";

    private static Path createDataFileIfMissing() {
        if (!DATA_DIRECTORY.toFile().exists()) {
            DATA_DIRECTORY.toFile().mkdir();
        }

        if (!DATA_FILE.toFile().exists()) {
            try {
                DATA_FILE.toFile().createNewFile();
            } catch (Exception e) {
                System.out.println("Error creating data file: " + e.getMessage());
            }
        }

        return DATA_FILE;
    }

    public static Todo readListFromFile() {
        Path filepath = Data.createDataFileIfMissing();
        Todo res = new Todo();
        Todo dueSoonList = new Todo();

        try {
            Stream<String> contentStream = Files.lines(filepath);
            contentStream.forEach(entry -> {
                Task task = Data.decodeData(entry);
                res.loadTask(task);
                if (task.isDueSoon()) {
                    dueSoonList.loadTask(task);
                }
            });
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error encountered when reading data from file: " + filepath.toString());
            throw new RuntimeException(e);
        }

        if (dueSoonList.getTasks().size() > 0) {
            System.out.println("You have tasks that are due soon!");
            System.out.println(dueSoonList.listTasks());
        }

        return res;
    }

    public static void saveListToFile(Todo list) {
        Path filepath = Data.createDataFileIfMissing();
        String data = Data.encodeData(list);

        try {
            Files.writeString(filepath, data);
        } catch (IOException e) {
            System.out.println("Error encountered when writing data to file: " + filepath.toString());
            throw new RuntimeException(e);
        }
    }

    public static Task decodeData(String entry) {
        String[] info = entry.split(Data.DELIMITER);

        try {
            TaskType entryType = TaskType.valueOf(info[0]);
            switch (entryType) {
            case TODO:
                return Task.decodeData(info);
            case EVENT:
                return EventTask.decodeData(info);
            case DEADLINE:
                return DeadlineTask.decodeData(info);
            default:
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            return Task.createCorruptTask();
        }
    }

    public static String encodeData(Todo list) {
        List<Task> tasks = list.getTasks();
        StringBuilder sb = new StringBuilder();

        tasks.forEach(task -> {
            sb.append(task.encodeData());
            sb.append(System.lineSeparator());
        });

        return sb.toString();
    }
}
