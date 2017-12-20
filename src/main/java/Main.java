import com.mysql.fabric.jdbc.FabricMySQLDriver;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int DUPLICATE_RECORD_TO_DB_CODE_ERROR = 1062;
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "root";
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/test?autoReconnect=true&useSSL=false";

    public static void main(String[] args) throws IOException, SQLException {

        System.out.println("Enter the path to the directory, or the name of the .txt-file, for example: D:\\test2.txt, " +
                "for recording statistics in the database and console:");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        reader.close();
        File file = new File(fileName);
        ArrayList<File> filesList = new ArrayList<>();
        Helper.getAllTxtFilesFromFolder(file, filesList);
        HashMap<File, FileStatisticForEachLine> filesStatisticMap = new HashMap();
        DBWorker dbWorker = new DBWorker();

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);

            FileExecutor.handleFilesList(filesList, filesStatisticMap);

            System.out.println("***start writing in DB***");

            //add new records to db
            dbWorker.addRecordToPathByUsersQueryTable(connection, fileName, filesList);
            dbWorker.addRecordToTxtFileListTable(connection, filesStatisticMap);

            //log statistic to console
            for (FileStatisticForEachLine value : filesStatisticMap.values()) {
                System.out.println(value);
            }
            System.out.println("***finish writing in DB***");
        } catch (MySQLIntegrityConstraintViolationException duplicateRecordInPathByUsersQueryTableEx) {
            System.out.println("***start updating DB records***");

            //check : if path from user's request, record is duplicate
            if (duplicateRecordInPathByUsersQueryTableEx.getErrorCode() == DUPLICATE_RECORD_TO_DB_CODE_ERROR) {
                try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
                     Statement statement = connection.createStatement()) {

                    //updating the record - path by user's query
                    dbWorker.updateRecordToPathByUsersQueryTable(connection, fileName, filesList);

                    //use foreach to handle duplicate record exception which can occur when working with each file.
                    for (Map.Entry<File, FileStatisticForEachLine> entry : filesStatisticMap.entrySet()) {
                        try {
                            dbWorker.addRecordToTxtFileListTable(connection, entry);
                        } catch (MySQLIntegrityConstraintViolationException duplicateRecordInTextFilesListTableEx) {

                            //check : if path of txt-file record is duplicate
                            if (duplicateRecordInTextFilesListTableEx.getErrorCode() == DUPLICATE_RECORD_TO_DB_CODE_ERROR) {
                                dbWorker.updateRecordToTxtFileListTable(connection, entry);
                                System.out.println("successful update txt-file: " + entry.getKey());
                            }
                        }
                    }
                }
            } else duplicateRecordInPathByUsersQueryTableEx.printStackTrace();
            System.out.println("***finish updating DB records***");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}