package ConsoleEngine.Utilities.FileWorker.Parsing.JSON;

import ConsoleEngine.CalculationEngine.Engine.Calculator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class JSONOperations {

    public ArrayList<String> parse(File file) throws IOException {
        ArrayList<String> exampleList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try( FileReader reader = new FileReader(file.getAbsolutePath())) {
            JSONObject rootJSONObject = (JSONObject) parser.parse(reader);
            JSONArray exampleJSONArray = (JSONArray) rootJSONObject.get("Math");
            int i = 0;
            for (Object it : exampleJSONArray) {
                JSONObject exampleJSONObject = (JSONObject) it;
                exampleList.add((String) exampleJSONObject.get("example" + i++));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return exampleList;
    }
    public void write(ArrayList<String> list, String outFileName, String parentPath)  {
        int i = 0;
        for (String text:list) {
            list.set(i, Calculator.calculateExpression(text));
            i++;
        }

        JSONArray bookList = new JSONArray();

        for (int j = 0; j < i; j++) {
            JSONObject obj = new JSONObject();
            obj.put("example" + j, list.get(j));
            bookList.add(obj);
            try (FileWriter file = new FileWriter(parentPath + "\\" + outFileName, false)) {
                file.write(bookList.toJSONString());
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
