package utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import components.Todo;
import components.task.DeadlineTask;
import components.task.EventTask;
import components.task.Task;
import components.task.TaskType;

/**
 * Handles persistence of the Todo list. Responsibilities: - Ensures the data
 * directory and file exist - Serializes (encodes) tasks to a flat text file -
 * Deserializes (decodes) tasks from stored file entries - Provides a uniform
 * delimiter for field separation
 *
 * File format: Each line represents one task. The first token is the TaskType
 * (TODO, EVENT, DEADLINE). Subsequent tokens depend on the specific task type.
 *
 * Corrupt / malformed lines are converted into placeholder tasks via
 * Task.createCorruptTask().
 */
public class Data {
    /**
     * Delimiter used to separate encoded task fields (chosen to avoid common
     * natural text).
     */
    public static final String DELIMITER = "%20";
    /** Directory where application data is stored. */
    private static final Path DATA_DIRECTORY = Path.of("data");
    /** Path to the main todo list storage file. */
    private static final Path DATA_FILE = DATA_DIRECTORY.resolve("todo_list.txt");

    /**
     * Ensures the data directory and file exist, creating them if missing.
     *
     * @return Path to the data file
     */
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

    /**
     * Reads the task list from disk and reconstructs a Todo instance. Malformed
     * lines result in placeholder corrupt tasks.
     *
     * @return populated Todo list (possibly empty)
     * @throws RuntimeException wrapping any underlying IO issues
     */
    public static Todo readListFromFile() {
        Path filepath = Data.createDataFileIfMissing();
        Todo res = new Todo();

        try {
            Stream<String> contentStream = Files.lines(filepath);
            contentStream.forEach(entry -> res.loadTask(Data.decodeData(entry)));
            contentStream.close();
        } catch (IOException e) {
            System.out.println("Error encountered when reading data from file: " + filepath.toString());
            throw new RuntimeException(e);
        }

        return res;
    }

    /**
     * Persists the provided Todo list to disk, overwriting existing file
     * content.
     *
     * @param list Todo list to serialize
     * @throws RuntimeException wrapping any underlying IO issues
     */
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

    /**
     * Decodes a single encoded task line into a Task instance. Delegates to the
     * appropriate subclass based on the first token (TaskType).
     *
     * @param entry encoded line
     * @return decoded Task (or a corrupt placeholder if parsing fails)
     */
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

    /**
     * Encodes all tasks in the given Todo list into a single string with one
     * line per task, terminated by the system line separator.
     *
     * @param list Todo list to encode
     * @return serialized multi-line representation
     */
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
