package ConsoleTests;

import ConsoleEngine.Utilities.FileWorker.Parsing.JSON.JSONOperations;
import ConsoleEngine.Utilities.FileWorker.Parsing.XML.XMLOperations;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JSONOperationsTest {

    @Test
    void parse() throws IOException {
        JSONOperations jsonOperations = new JSONOperations();
        ArrayList<String> listActual = jsonOperations.parse(new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.json"));
        ArrayList<String> listExpected = new ArrayList<>();
        listExpected.add("231 + 2 + 3 * 5 + (3 * 2) + 2");
        listExpected.add("8 + 3 + sjdsajsd");
        listExpected.add("3 + 2 + 5");
        listExpected.add("2 + 3*(2+1) ");

        assertEquals(listExpected, listActual);
    }

    @Test
    void write() {
        ArrayList<String> listExpected = new ArrayList<>();
        File file= new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.json");

        listExpected.add("231 + 2 + 3 * 5 + (3 * 2) + 2");
        listExpected.add("8 + 3 + sjdsajsd");
        listExpected.add("3 + 2 + 5");
        listExpected.add("2 + 3*(2+1) ");
        JSONOperations jsonOperations = new JSONOperations();
        jsonOperations.write(listExpected, "res_" + file.getName(), file.getParentFile().getAbsolutePath());
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\res_input.json");
        assertEquals(fileExp, new File(file.getParentFile().getAbsolutePath() + "res_" + file.getName()));
    }
}