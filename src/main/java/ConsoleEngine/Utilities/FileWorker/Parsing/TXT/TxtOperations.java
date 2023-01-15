package ConsoleEngine.Utilities.FileWorker.Parsing.TXT;

import ConsoleEngine.CalculationEngine.Engine.Calculator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtOperations {
   public String parse(File file) throws IOException {
       String result = "";
       StringBuilder text = new StringBuilder();
       try {
           FileReader reader = new FileReader(file.getName());
           int c;
           while ((c = reader.read()) != -1) {
               text.append((char) c);
           }
       } catch (IOException ex) {
           System.out.println(ex.getMessage());
       }
       result = text.toString();
       return result;
    }

    public void write(String text, String outFileName, String parentPath)  {
        Pattern regex = Pattern.compile("[()0-9]*( ){0,}([+-/*]( ){0,}[()0-9]{0,})*");
        Matcher m = regex.matcher(text);
        while (m.find()) {
            String expression = m.group();
            if(expression.equals("") || expression.equals(" ")) {
                continue;
            }
            try {
                //expression.replaceAll(" ", "");
                text = text.replace(expression, Calculator.calculateExpression(expression));
//                text = new StringBuilder(text).insert(0, " ").toString();
//                text = new StringBuilder(text).insert(text.length(), " ").toString();
            }
            catch(Exception e){
                //System.out.println(e.getMessage());
            }
        }

        try
        {
            FileWriter writer = new FileWriter(parentPath + "\\" + outFileName, false);
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
