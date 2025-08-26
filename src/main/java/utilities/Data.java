package utilities;

import java.nio.file.Path;
import java.util.stream.Stream;

import components.Todo;
import components.task.DeadlineTask;
import components.task.EventTask;
import components.task.Task;
import components.task.TaskType;
import exceptions.TaskNotFoundException;

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

        try {
            Stream<String> contentStream = Files.lines(filepath);
            contentStream.forEach(entry -> res.addTask(decodeData(entry)));
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error encountered when reading data from file: " + filepath.toString());
        }

        return res;
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
}
