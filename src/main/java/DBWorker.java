import com.google.gson.Gson;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBWorker {

    static final String INSERT_TO_PATH_BY_USERS_QUERY_TABLE = "INSERT INTO path_by_users_query (path_quary, json_list_of_txt_files) VALUES (?, ?)";
    static final String INSERT_TO_TXT_FILE_LIST_TABLE = "INSERT INTO txt_files_list (file_path, json_of_file_statistic) VALUES (?, ?)";

    public void addRecordToPathByUsersQueryTable(Connection connection, String fileName, List<File> filesList) throws SQLException {

        //ДОБАВЛЯЕМ В ТАБЛИЦУ path_by_users_query ЗАПРОС ПОЛЬЗОВАТЕЛЯ И
        //JSON ПРЕДСТАВЛЕНИЕ СПИСКА TXT-ФАЙЛОВ ПО ЭТОМУ ЗАПРОСУ

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TO_PATH_BY_USERS_QUERY_TABLE)) {
            preparedStatement.setString(1, fileName);

            String json = new Gson().toJson(filesList);
            preparedStatement.setString(2, json);
            preparedStatement.execute();
        }
    }

    public void addRecordToTxtFileListTable(Connection connection, HashMap<File, FileStatisticForEachLine> filesStatisticMap) throws SQLException {

        //ДОБАВЛЯЕМ В ТАБЛИЦУ txt_files_list ПУТИ К TXT-ФАЙЛАМ И
        //JSON ПРЕДСТАВЛЕНИЯ ОБЪЕКТОВ FileStatisticForEachLine
        // ДЛЯ КАЖДОГО ФАЙЛА

        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TO_TXT_FILE_LIST_TABLE)) {
            for (Map.Entry<File, FileStatisticForEachLine> entry : filesStatisticMap.entrySet()) {
                String pathToFolderOrFileByUser = entry.getKey().getPath();
                ArrayList<LineStatisticItem> statisticItems = entry.getValue().getLineStatisticItems();

                preparedStatement.setString(1, pathToFolderOrFileByUser);
                String json = new Gson().toJson(statisticItems);
                preparedStatement.setString(2, json);

                preparedStatement.execute();
            }
        }
    }

    public void getRecordFromPathByUsersQueryTable(){

    }


}
