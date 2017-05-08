import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by Farima on 4/5/2017.
 */
public class ComparatorMain {

    LinkedHashSet<String> mainFileList=new LinkedHashSet<>();
    public ComparatorMain(){
        try {
            String lineMainInput = "";
            String mainFileName=new File(Paths.get("./input/main_file/").toString()).listFiles()[0].getName();
            BufferedReader bufferedReaderMainInput = new BufferedReader(new FileReader(Paths.get("./input/main_file/").toString()+"/"+mainFileName));//to see skewness and kurtosis

            while ((lineMainInput = bufferedReaderMainInput.readLine()) != null) {
                mainFileList.add(lineMainInput);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ComparatorMain comparatorMain=new ComparatorMain();
        try {
            ArrayList<String> file1List=new ArrayList<>();
            ArrayList<String> file2List=new ArrayList<>();
            PrintWriter printWriter=new PrintWriter("./output/filesInBoth.txt");
            String firstFileName=new File(Paths.get("./input/first_file/").toString()).listFiles()[0].getName();
            BufferedReader bufferedReaderFile1 = new BufferedReader(new FileReader(Paths.get("./input/first_file").toString()+"/"+firstFileName));
            String secondFileName=new File(Paths.get("./input/second_file/").toString()).listFiles()[0].getName();
            BufferedReader bufferedReaderFile2 = new BufferedReader(new FileReader(Paths.get("./input/second_file").toString()+"/"+secondFileName));
            String line="";
            while ((line=bufferedReaderFile1.readLine())!=null) {
                file1List.add(line);
            }
            line="";
            while ((line=bufferedReaderFile2.readLine())!=null) {
                file2List.add(line);
            }
            bufferedReaderFile1.close();
            bufferedReaderFile2.close();

            double minDiffSkewnessClone=1000;
            double maxDiffSkewnessClone=-1000;
            double minDiffKurtosisClone=1000;
            double maxDiffKurtosisClone=-1000;

            double minDiffSkewnessNotClone=1000;
            double maxDiffSkewnessNotClone=-1000;
            double minDiffKurtosisNotClone=1000;
            double maxDiffKurtosisNotClone=-1000;

            for(String lineFile1:file1List){
                double querySkewness=0;
                double queryKurtosis=0;
                double candidateSkewness=0;
                double candidateKurtosis=0;

                boolean lineFound=false;
                String[] file1=lineFile1.split(",");
                for(String lineFile2:file2List){
                    String[] file2=lineFile2.split(",");
                    if ((lineFile1.equals(lineFile2))
                            ||(file1[0].equals(file2[2])&&file1[1].equals(file2[3])&&
                            file1[2].equals(file2[0])&&file1[3].equals(file2[1]))){
                        PairInfo pairInfo=comparatorMain.getSkewnessKurtosis(lineFile1);
                        querySkewness=pairInfo.querySkewness;
                        queryKurtosis=pairInfo.queryKurtosis;
                        candidateKurtosis=pairInfo.candidateKurtosis;
                        candidateSkewness=pairInfo.candidateSkewness;
                        double skewnessDiff=Math.abs(querySkewness-candidateSkewness);
                        double kurtosisDiff=Math.abs(queryKurtosis-candidateKurtosis);
                        if (skewnessDiff>maxDiffSkewnessClone) maxDiffSkewnessClone=skewnessDiff;
                        if (skewnessDiff<minDiffSkewnessClone) minDiffSkewnessClone=skewnessDiff;
                        if (kurtosisDiff>maxDiffKurtosisClone) maxDiffKurtosisClone=kurtosisDiff;
                        if (kurtosisDiff<minDiffKurtosisClone) minDiffKurtosisClone=kurtosisDiff;
                        String analysisOutput=querySkewness+","+candidateSkewness+","+skewnessDiff+","
                                        +queryKurtosis+","+candidateKurtosis+","+kurtosisDiff;
                        printWriter.append(lineFile1+" "+analysisOutput+System.lineSeparator());
                        lineFound=true;
                        break;
                    }
                }

                if(!lineFound) {
                    //System.out.println(lineFile1);
                    PairInfo pairInfo=comparatorMain.getSkewnessKurtosis(lineFile1);
                    double skewnessDiffNonClone=Math.abs(pairInfo.querySkewness-pairInfo.candidateSkewness);
                    double kurtosisDiffNonClone=Math.abs(pairInfo.queryKurtosis-pairInfo.candidateKurtosis);
                    if (skewnessDiffNonClone>maxDiffSkewnessNotClone) maxDiffSkewnessNotClone=skewnessDiffNonClone;
                    if (skewnessDiffNonClone<minDiffSkewnessNotClone) minDiffSkewnessNotClone=skewnessDiffNonClone;
                    if (kurtosisDiffNonClone>maxDiffKurtosisNotClone) maxDiffKurtosisNotClone=kurtosisDiffNonClone;
                    if (kurtosisDiffNonClone<minDiffKurtosisNotClone) minDiffKurtosisNotClone=kurtosisDiffNonClone;
                }
            }
            System.out.println("For clones:");
            System.out.println("max diff kurtosis: "+maxDiffKurtosisClone);
            System.out.println("min diff kurtosis: "+minDiffKurtosisClone);
            System.out.println("max diff skewness: "+maxDiffSkewnessClone);
            System.out.println("min diff skewness: "+minDiffSkewnessClone);

            System.out.println("For non clones:");
            System.out.println("max diff kurtosis: "+maxDiffKurtosisNotClone);
            System.out.println("min diff kurtosis: "+minDiffKurtosisNotClone);
            System.out.println("max diff skewness: "+maxDiffSkewnessNotClone);
            System.out.println("min diff skewness: "+minDiffSkewnessNotClone);
            printWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    PairInfo getSkewnessKurtosis(String clonePair){
        PairInfo pairInfo = new PairInfo();
        for (String lineMainInput:mainFileList){
            String[] separatedLineFromMeta = lineMainInput.split("@#@");
            String projId = separatedLineFromMeta[0].split(",")[0];
            String fileId = separatedLineFromMeta[0].split(",")[1];
            double skewness = Double.parseDouble(separatedLineFromMeta[0].split(",")[11]);
            double kurtosis = Double.parseDouble(separatedLineFromMeta[0].split(",")[12]);
            if (clonePair.split(",")[0].equals(projId) && clonePair.split(",")[1].equals(fileId)) {
                pairInfo.querySkewness = skewness;
                pairInfo.queryKurtosis = kurtosis;
            } else if (clonePair.split(",")[2].equals(projId) && clonePair.split(",")[3].equals(fileId)) {
                pairInfo.candidateKurtosis = kurtosis;
                pairInfo.candidateSkewness = skewness;
            }
        }

        return pairInfo;
    }

    private class PairInfo{
        double querySkewness;
        double candidateSkewness;
        double queryKurtosis;
        double candidateKurtosis;

        public double getQuerySkewness() {
            return querySkewness;
        }

        public void setQuerySkewness(double querySkewness) {
            this.querySkewness = querySkewness;
        }

        public double getCandidateSkewness() {
            return candidateSkewness;
        }

        public void setCandidateSkewness(double candidateSkewness) {
            this.candidateSkewness = candidateSkewness;
        }

        public double getQueryKurtosis() {
            return queryKurtosis;
        }

        public void setQueryKurtosis(double queryKurtosis) {
            this.queryKurtosis = queryKurtosis;
        }

        public double getCandidateKurtosis() {
            return candidateKurtosis;
        }

        public void setCandidateKurtosis(double candidateKurtosis) {
            this.candidateKurtosis = candidateKurtosis;
        }
    }
}
