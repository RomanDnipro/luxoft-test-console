import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static final String USER_NAME = "root";
    static final String PASSWORD = "root";
    static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false";

    public static void main(String[] args) throws IOException {

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            System.out.println("Введите путь к директории, или имя .txt-файла, например: D:\\test2.txt, для статистики:");
            HashMap<File, FileStatisticForEachLine> filesStatisticMap = new HashMap();
            File file;
            String fileName = new BufferedReader(new InputStreamReader(System.in)).readLine();
            file = new File(fileName);
            ArrayList<File> filesList = new ArrayList<>();
            Helper.getAllTxtFilesFromFolder(file, filesList);
            DBWorker dbWorker = new DBWorker();

            for (File f : filesList) {
                filesStatisticMap.put(f, new FileStatisticForEachLine(f));
            }

            try {
                //ДОБАВЛЯЕМ В ТАБЛИЦУ path_by_users_query ЗАПРОС ПОЛЬЗОВАТЕЛЯ И
                //JSON ПРЕДСТАВЛЕНИЕ СПИСКА TXT-ФАЙЛОВ ПО ЭТОМУ ЗАПРОСУ
                dbWorker.addRecordToPathByUsersQueryTable(connection, fileName, filesList);

                //ДОБАВЛЯЕМ В ТАБЛИЦУ txt_files_list ПУТИ К TXT-ФАЙЛАМ И
                //JSON ПРЕДСТАВЛЕНИЯ ОБЪЕКТОВ FileStatisticForEachLine
                // ДЛЯ КАЖДОГО ФАЙЛА
                dbWorker.addRecordToTxtFileListTable(connection, filesStatisticMap);

            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println("------все или некоторые записи уже существует в базе данных------");
                } else {
                    e.printStackTrace();
                }
            }

            for (FileStatisticForEachLine value : filesStatisticMap.values()) {
                System.out.println(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
