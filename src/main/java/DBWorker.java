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

    //SQL-requests for insert and update records
    private static final String INSERT_TO_PATH_BY_USERS_QUERY_TABLE = "INSERT INTO path_by_users_query (path_query, json_list_of_txt_files) VALUES (?, ?)";
    private static final String UPDATE_PATH_BY_USERS_QUERY_TABLE = "UPDATE path_by_users_query SET json_list_of_txt_files = ? WHERE path_query = ?";
    private static final String INSERT_TO_TXT_FILE_LIST_TABLE = "INSERT INTO txt_files_list (file_path, json_of_file_statistic) VALUES (?, ?)";
    private static final String UPDATE_TXT_FILE_LIST_TABLE = "UPDATE txt_files_list SET json_of_file_statistic = ? WHERE file_path = ?";

    //param constants for path_by_users_query (PBUQ) table
    private static final int PARAM_INDEX_OF_PATH_QUERY_FOR_INSERT_TO_PBUQ_TABLE = 1;
    private static final int PARAM_INDEX_OF_JSON_LIST_FILES_FOR_INSERT_TO_PBUQ_TABLE = 2;
    private static final int PARAM_INDEX_OF_PATH_QUERY_FOR_UPDATE_TO_PBUQ_TABLE = 2;
    private static final int PARAM_INDEX_OF_JSON_LIST_FILES_FOR_UPDATE_TO_PBUQ_TABLE = 1;

    //param constants for txt_files_list (TFL) table
    private static final int PARAM_INDEX_OF_FILE_PATH_FOR_INSERT_TO_TFL_TABLE = 1;
    private static final int PARAM_INDEX_OF_JSON_OF_FILE_STATISTIC_FOR_INSERT_TO_TFL_TABLE = 2;
    private static final int PARAM_INDEX_OF_FILE_PATH_FOR_UPDATE_TO_TFL_TABLE = 2;
    private static final int PARAM_INDEX_OF_JSON_OF_FILE_STATISTIC_FOR_UPDATE_TO_TFL_TABLE = 1;

    /**
     * Makes a request to create or update a record in the path_by_users_query (PBUQ) table
     *
     * @param connection                use to connect with db
     * @param fileName                  file name received from user
     * @param filesList                 list of files in the directory and subdirectories received by the user request
     * @param sql                       request to create or update path_by_users_query (PBUQ) table
     * @param paramIndexOfPathQuery     the corresponding index of the PathQuery variable for the SQL query
     * @param paramIndexOfJsonListFiles the corresponding index of the PathQuery variable for the SQL query
     * @throws SQLException
     */
    public void addOrUpdateRecordToPathByUsersQueryTable(Connection connection, String fileName, List<File> filesList, String sql, int paramIndexOfPathQuery, int paramIndexOfJsonListFiles) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(paramIndexOfPathQuery, fileName);

            String json = new Gson().toJson(filesList);
            preparedStatement.setString(paramIndexOfJsonListFiles, json);
            preparedStatement.execute();
        }
    }

    /**
     * @param connection use to connect with db
     * @param fileName   file name received from user
     * @param filesList  list of files in the directory and subdirectories received by the user request
     * @throws SQLException
     */
    public void addRecordToPathByUsersQueryTable(Connection connection, String fileName, List<File> filesList) throws SQLException {
        addOrUpdateRecordToPathByUsersQueryTable(connection, fileName, filesList, INSERT_TO_PATH_BY_USERS_QUERY_TABLE, PARAM_INDEX_OF_PATH_QUERY_FOR_INSERT_TO_PBUQ_TABLE, PARAM_INDEX_OF_JSON_OF_FILE_STATISTIC_FOR_INSERT_TO_TFL_TABLE);
    }

    /**
     * @param connection use to connect with db
     * @param fileName   file name received from user
     * @param filesList  list of files in the directory and subdirectories received by the user request
     * @throws SQLException
     */
    public void updateRecordToPathByUsersQueryTable(Connection connection, String fileName, List<File> filesList) throws SQLException {
        addOrUpdateRecordToPathByUsersQueryTable(connection, fileName, filesList, UPDATE_PATH_BY_USERS_QUERY_TABLE, PARAM_INDEX_OF_PATH_QUERY_FOR_UPDATE_TO_PBUQ_TABLE, PARAM_INDEX_OF_JSON_LIST_FILES_FOR_UPDATE_TO_PBUQ_TABLE);
    }

    /**
     * Makes a request to create or update a record in the txt_files_list (TFL) table.
     * Used to make exceptions handling  possible that occur when working with a txt file
     *
     * @param connection                           use to connect with db
     * @param entry                                of filesStatisticMap
     * @param sql                                  request to create or update path_by_users_query (PBUQ) table
     * @param paramIndexOfPathToFolderOrFileByUser the corresponding index of the file_path variable for the SQL query
     * @param paramIndexOfJson                     the corresponding index of the json_of_file_statistic variable for the SQL query
     * @throws SQLException
     */
    public void addOrUpdateRecordToTxtFileListTable(Connection connection, Map.Entry<File, FileStatisticForEachLine> entry, String sql, int paramIndexOfPathToFolderOrFileByUser, int paramIndexOfJson) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            String pathToFolderOrFileByUser = entry.getKey().getPath();
            ArrayList<LineStatisticItem> statisticItems = entry.getValue().getLineStatisticItems();

            preparedStatement.setString(paramIndexOfPathToFolderOrFileByUser, pathToFolderOrFileByUser);
            String json = new Gson().toJson(statisticItems);
            preparedStatement.setString(paramIndexOfJson, json);

            preparedStatement.execute();
        }
    }

    /**
     * Makes a request to create a record in the txt_files_list (TFL) table.
     *
     * @throws SQLException
     */
    public void addRecordToTxtFileListTable(Connection connection, Map.Entry<File, FileStatisticForEachLine> entry) throws SQLException {
        addOrUpdateRecordToTxtFileListTable(connection, entry, INSERT_TO_TXT_FILE_LIST_TABLE, PARAM_INDEX_OF_FILE_PATH_FOR_INSERT_TO_TFL_TABLE, PARAM_INDEX_OF_JSON_LIST_FILES_FOR_INSERT_TO_PBUQ_TABLE);
    }

    /**
     * Makes a request to update a record in the txt_files_list (TFL) table.
     *
     * @throws SQLException
     */
    public void updateRecordToTxtFileListTable(Connection connection, Map.Entry<File, FileStatisticForEachLine> entry) throws SQLException {
        addOrUpdateRecordToTxtFileListTable(connection, entry, UPDATE_TXT_FILE_LIST_TABLE, PARAM_INDEX_OF_FILE_PATH_FOR_UPDATE_TO_TFL_TABLE, PARAM_INDEX_OF_JSON_OF_FILE_STATISTIC_FOR_UPDATE_TO_TFL_TABLE);
    }

    /**
     * @param connection        use to connect with db
     * @param filesStatisticMap store statistic per line of txt-file as value and this file as key
     * @throws SQLException
     */
    public void addRecordToTxtFileListTable(Connection connection, HashMap<File, FileStatisticForEachLine> filesStatisticMap) throws SQLException {

        for (Map.Entry<File, FileStatisticForEachLine> entry : filesStatisticMap.entrySet()) {
            addRecordToTxtFileListTable(connection, entry);
        }
    }

    /**
     * Makes a request to update a record in the txt_files_list (TFL) table.
     *
     * @param connection        use to connect with db
     * @param filesStatisticMap store statistic per line of txt-file as value and this file as key
     * @throws SQLException
     */
    public void updateRecordToTxtFileListTable(Connection connection, HashMap<File, FileStatisticForEachLine> filesStatisticMap) throws SQLException {

        for (Map.Entry<File, FileStatisticForEachLine> entry : filesStatisticMap.entrySet()) {
            updateRecordToTxtFileListTable(connection, entry);
        }
    }
}
