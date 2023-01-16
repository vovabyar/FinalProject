package ConsoleTests;

import ConsoleEngine.Utilities.FileWorker.Parsing.JSON.JSONOperations;
import ConsoleEngine.Utilities.FileWorker.Parsing.TXT.TxtOperations;
import ConsoleEngine.Utilities.FileWorker.Parsing.XML.XMLOperations;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TxtOperationsTest {

    @Test
    void parse() throws IOException {
        TxtOperations txtOperations = new TxtOperations();
        String actual = txtOperations.parse(new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt"));
        String expected = "dsjf dsfkjsdf df  3 d (1+2) adfkmdf sdf k dsfpodskf pokds sdf odsdf\n" +
                "sd\n" +
                "f ksdf dsf ksdfok dsf\n" +
                "sdf sdo kfdp ksdf pdofk pskfpaksawr ofd [skod adfpokk fd[ kf kdfoijj odfj\n" +
                "sdfp jdf opsraoero sefpo ksdpo fresokr seor skdf[p s[dfko sdf\n" +
                "sdf oks dfkopaerbpokg rejsiog erjip apj epo we fo wefpowrgpij we[fk we[fpwke[ok ergko w\n" +
                "wef okwef kewof we ewf\n" +
                "(2*5)*(2+4+9*8)\n" +
                " fdslfkgs ksdfl kjdfs sdfkl gsdfkl     (2)*(3+4)";
        assertEquals(expected, actual);
    }

    @Test
    void write() {
        ArrayList<String> listExpected = new ArrayList<>();
        File file= new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\test.txt");
        String expected = "dsjf dsfkjsdf df  3 d (1+2) adfkmdf sdf k dsfpodskf pokds sdf odsdf\n" +
                "sd\n" +
                "f ksdf dsf ksdfok dsf\n" +
                "sdf sdo kfdp ksdf pdofk pskfpaksawr ofd [skod adfpokk fd[ kf kdfoijj odfj\n" +
                "sdfp jdf opsraoero sefpo ksdpo fresokr seor skdf[p s[dfko sdf\n" +
                "sdf oks dfkopaerbpokg rejsiog erjip apj epo we fo wefpowrgpij we[fk we[fpwke[ok ergko w\n" +
                "wef okwef kewof we ewf\n" +
                "(2*5)*(2+4+9*8)\n" +
                " fdslfkgs ksdfl kjdfs sdfkl gsdfkl     (2)*(3+4)";

        TxtOperations txtOperations = new TxtOperations();
        txtOperations.write(expected, "res_" + file.getName(), file.getParentFile().getAbsolutePath());
        File fileExp = new File("D:\\demo1\\src\\main\\java\\ConsoleTests\\resources\\res_test.txt");
        assertEquals(fileExp, new File(file.getParentFile().getAbsolutePath() + "res_" + file.getName()));
    }
}