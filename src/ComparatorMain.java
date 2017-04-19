import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Created by Farima on 4/5/2017.
 */
public class ComparatorMain {
    public static void main(String[] args) {
        try {
            PrintWriter printWriter=new PrintWriter("filesInBoth.txt");
            BufferedReader bufferedReaderFile1 = new BufferedReader(new FileReader("blocksclones_index_WITH_FILTER.txt"));
            BufferedReader bufferedReaderFile2 = null;
            String lineFile1="";
            String lineFile2="";
            while ((lineFile1=bufferedReaderFile1.readLine())!=null){
                bufferedReaderFile2 = new BufferedReader(new FileReader("CCOutput.txt"));
                String[] file1=lineFile1.split(",");
                while ((lineFile2=bufferedReaderFile2.readLine())!=null){
                    String[] file2=lineFile2.split(",");
                    if ((lineFile1.equals(lineFile2))
                            ||(file1[0].equals(file2[2])&&file1[1].equals(file2[3])&&
                            file1[2].equals(file2[0])&&file1[3].equals(file2[1]))){
                        printWriter.append(lineFile1+System.lineSeparator());
                    }
                    //else System.out.println(lineFile1+" "+lineFile2);
                }
                bufferedReaderFile2.close();
            }
            printWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
