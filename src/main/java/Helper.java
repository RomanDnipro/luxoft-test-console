import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class Helper {
    public static String getStatisticInfoByTxtFile(FileStatisticForEachLine file) {
        return "";
    }

    public static FileStatisticForEachLine getFileStatisticLikeObj(String json) {
        return new Gson().fromJson(json, FileStatisticForEachLine.class);
    }

    public static String getFileStatisticLikeString(String json) {
        return getFileStatisticLikeObj(json).toString();
    }

    public static String getJsonOfTxtFileList(String filePath) {
        DBWorker dbWorker = new DBWorker();

        return "";
    }

    public static void getAllTxtFilesFromFolder(File file, ArrayList<File> txtFilesList) {
        if (file.isFile()) {
            //если файл имеет расширение .txt
            if (file.getPath().endsWith(".txt")) {
                txtFilesList.add(file);
            }
        } else {
            for (File f : file.listFiles()) {
                getAllTxtFilesFromFolder(f, txtFilesList);
            }
        }
    }
}
