import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class Helper {

    /**
     * Deserialize json to FileStatisticForEachLine object
     *
     * @param json json notation of FileStatisticForEachLine object
     * @return FileStatisticForEachLine object from json
     */
    public static FileStatisticForEachLine getFileStatisticLikeObj(String json) {
        return new Gson().fromJson(json, FileStatisticForEachLine.class);
    }

    /**
     * Adds all txt-files from the directory and all its subdirectories to the selected list
     *
     * @param file by user request
     * @param txtFilesList target ArrayList
     */
    public static void getAllTxtFilesFromFolder(File file, ArrayList<File> txtFilesList) {
        try {
            if (file.isFile()) {
                //if file is .txt
                if (file.getPath().endsWith(".txt")) {
                    txtFilesList.add(file);
                }
            } else {
                for (File f : file.listFiles()) {
                    getAllTxtFilesFromFolder(f, txtFilesList);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Folder is empty or File is not exist");
        }
    }
}
