package org.example.DelFile;

import java.io.File;

public class DeleteFile {
    /**
     * Метод для удаления файла по указанному пути.
     *
     * @param filePath Путь к удаляемому файлу
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean isDeleted = file.delete();
            if (isDeleted) {
                System.out.println("Файл успешно удалён: " + filePath);
            } else {
                System.out.println("Не удалось удалить файл: " + filePath);
            }
        } else {
            System.out.println("Файл не существует: " + filePath);
        }
    }
}
