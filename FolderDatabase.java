import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FolderDatabase {
    private final String rootPath;

    public FolderDatabase(String rootPath) {
        this.rootPath = rootPath;
        createRootFolder();
    }

    private void createRootFolder() {
        try {
            Files.createDirectories(Paths.get(rootPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(String entity, String id, String data) {
        Path entityPath = Paths.get(rootPath, entity);
        try {
            Files.createDirectories(entityPath);
            Path filePath = entityPath.resolve(id + ".txt");
            Files.writeString(filePath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadData(String entity, String id) {
        Path filePath = Paths.get(rootPath, entity, id + ".txt");
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            return null;
        }
    }

    public List<String> listEntities() {
        List<String> entities = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(rootPath))) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    entities.add(path.getFileName().toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public List<String> listData(String entity) {
        List<String> dataIds = new ArrayList<>();
        Path entityPath = Paths.get(rootPath, entity);
        if (Files.exists(entityPath)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(entityPath, "*.txt")) {
                for (Path path : stream) {
                    String fileName = path.getFileName().toString();
                    dataIds.add(fileName.substring(0, fileName.lastIndexOf('.')));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataIds;
    }

    public void deleteData(String entity, String id) {
        Path filePath = Paths.get(rootPath, entity, id + ".txt");
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
