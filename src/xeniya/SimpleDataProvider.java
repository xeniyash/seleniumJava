package xeniya;


import org.apache.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleDataProvider {

    private static Logger log = Logger.getLogger(SimpleDataProvider.class);

    public static final int CASE_NAME = 1;
    public static final int YEAR = 2;
    public static final int ITEM_NAME = 3;
    public static final int PRICE = 4;


    @DataProvider(name = "simpleDataProvider", parallel = false)
    public static Iterator<String[]> testCaseProvider() {
        List<String[]> lines = new ArrayList<>();

        Pattern pattern = Pattern.compile("^(.*),(\\d*),(.*),(.*)$");
        try {
            BufferedReader in = new BufferedReader(new FileReader("testCases\\TestCaseInput.txt"));
            String str = null;
            while ((str = in.readLine()) != null) {
                Matcher matcher = pattern.matcher(str);
                while (matcher.find()) {
                    lines.add(new String[]{matcher.group(CASE_NAME), matcher.group(YEAR),
                            matcher.group(ITEM_NAME), matcher.group(PRICE)});
                    log.debug("FOUND IN TEST CASES : " + Arrays.toString(lines.get(lines.size() - 1)));
                }
            }
            in.close();
        } catch (IOException e) {
            log.error(e);
            System.exit(1);
        }

        return lines.iterator();
    }
}
