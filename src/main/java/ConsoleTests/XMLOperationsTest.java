package ConsoleTests;

import ConsoleEngine.Utilities.FileWorker.Parsing.XML.XMLOperations;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class XMLOperationsTest {

    @Test
    void parse() throws IOException, ParserConfigurationException, URISyntaxException, SAXException {
        XMLOperations xmlOperations = new XMLOperations();
        ArrayList<String> listActual = xmlOperations.parse(new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.xml"));
        ArrayList<String> listExpected = new ArrayList<>();
        listExpected.add("32 + 4 - 3 + 5 * 14");
        listExpected.add("84 + 2 * 324");
        listExpected.add("jnsfjnasfiojfs2345aokafsopkfa");
        assertEquals(listExpected, listActual);

    }

    @Test
    void write() throws XMLStreamException, IOException {
        ArrayList<String> listExpected = new ArrayList<>();
        File file= new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\input.xml");

        listExpected.add("32 + 4 - 3 + 5 * 14");
        listExpected.add("84 + 2 * 324");
        listExpected.add("jnsfjnasfiojfs2345aokafsopkfa");
        XMLOperations xmlOperations = new XMLOperations();
        xmlOperations.write(listExpected, "res_" + file.getName(), file.getParentFile().getAbsolutePath());
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\res_input.xml");
        assertEquals(fileExp, new File(file.getParentFile().getAbsolutePath() + "res_" + file.getName()));
    }
}