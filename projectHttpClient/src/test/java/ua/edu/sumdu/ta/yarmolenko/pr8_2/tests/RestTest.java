package ua.edu.sumdu.ta.yarmolenko.pr8_2.tests;

import org.junit.Test;
import ua.edu.sumdu.ta.yarmolenko.pr8_2.Food;
import ua.edu.sumdu.ta.yarmolenko.pr8_2.Rest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestTest {

    private static final String expectedTAHeader = "X-Ta-Course-Example-Header: TA-Fall-2018";

    /**
     *
     */
    @Test
    public static void main(String[] args) throws Exception {
        try {
            Rest rest = new Rest();

            String xmlTAHeader = rest.getTAHeader(Rest.jsonPath);
            String jsonTAHeader = rest.getTAHeader(Rest.xmlPath);

            assertEquals(expectedTAHeader, xmlTAHeader);
            assertEquals(expectedTAHeader, jsonTAHeader);

            System.out.println("XML Header: " + xmlTAHeader);
            System.out.println("JSON Header: " + jsonTAHeader);

            String xmlEntityContent = rest.getData(Rest.xmlPath);
            String jsonEntityContent = rest.getData(Rest.jsonPath);

            List<Food> menu = rest.xmlParse(Rest.xmlPath);

            rest.xmlListFoods(menu);
            rest.xmlListLessThan700CaloriesFoods(menu);
            rest.xmlMaxCaloriesFood(menu);

            Object jsonParsedContent = rest.jsonParse(jsonEntityContent);
            rest.jsonListFoods(jsonParsedContent);
            rest.jsonListLessThan700CaloriesFoods(jsonParsedContent);
            rest.jsonMaxNumber(jsonParsedContent);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
