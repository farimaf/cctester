import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Paths;

/**
 * Created by Farima on 5/10/2017.
 */
public class ComparatorExhaustive {
    public static void main(String[] args) {
        try {
            PrintWriter printWriterBoth=new PrintWriter("./output/filesInBoth_exhust.txt");
            PrintWriter printWriterMissed=new PrintWriter("./output/missedFiles_exhust.txt");
            String firstFileName = new File(Paths.get("./input/first_file_exhust/").toString()).listFiles()[0].getName();
            BufferedReader bufferedReaderFile1 = new BufferedReader(new FileReader(Paths.get("./input/first_file_exhust").toString() + "/" + firstFileName));
            String secondFileName = new File(Paths.get("./input/second_file_exhust/").toString()).listFiles()[0].getName();
            BufferedReader bufferedReaderFile2;
            String lineFile1="",lineFile2="";
            while ((lineFile1=bufferedReaderFile1.readLine())!=null) {
                System.out.println(lineFile1);
                boolean lineFound = false;
                String[] file1 = lineFile1.split(",");
                bufferedReaderFile2 = new BufferedReader(new FileReader(Paths.get("./input/second_file_exhust").toString() + "/" + secondFileName));
                while ((lineFile2 = bufferedReaderFile2.readLine()) != null) {
                        String[] file2 = lineFile2.split(",");
                        if ((lineFile2.equals(lineFile2))
                                || (file1[0].equals(file2[2]) && file1[1].equals(file2[3]) &&
                                file1[2].equals(file2[0]) && file1[3].equals(file2[1]))) {
                        printWriterBoth.append(lineFile1 + " " + System.lineSeparator());//analysisOutput+System.lineSeparator());
                        lineFound = true;
                        break;
                    }
                }
                if (!lineFound) {
                    printWriterMissed.append(lineFile1 + System.lineSeparator());
                }
            }
            printWriterBoth.close();
            printWriterMissed.close();
            }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
