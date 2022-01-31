import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class HistoryHandler {
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    private List<String> list = new ArrayList<>(100);

    public void history(String fileName) {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter("history/" + fileName, true));
            bufferedReader = new BufferedReader(new FileReader("history/" + fileName));

            while (bufferedReader.ready()) {
                list.add(bufferedReader.readLine());
                if (list.size() > 100) {
                    list.remove(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeHistory(String history) {
        try{
            bufferedWriter.write(history + "\n");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void closeStreams() {
        try {
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getHistoryList() {
        return list;
    }
}
