package ua.edu.sumdu.ta.yarmolenko.pr8_2;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Rest.
 */
public class Rest {
    /**
     * The constant xmlPath.
     */
    public static final String xmlPath = "http://www.mocky.io/v2/5bebe91f3300008500fbc0e3";
    /**
     * The constant jsonPath.
     */
    public static final String jsonPath = "http://www.mocky.io/v2/5bed52fd3300004c00a2959d";
    private static final String taHeader = "X-Ta-Course-Example-Header";

    /**
     * Gets ta header.
     *
     * @param uri the uri
     * @return the ta header
     * @throws IOException the io exception
     */
    public String getTAHeader(String uri) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);

        try {
            return response.getFirstHeader(Rest.taHeader).toString();
        } finally {
            response.close();
        }
    }

    /**
     * Gets data.
     *
     * @param uri the uri
     * @return the data
     * @throws IOException             the io exception
     * @throws ClientProtocolException the client protocol exception
     */
    public String getData(String uri) throws IOException, ClientProtocolException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    long len = entity.getContentLength();
                    if (len != -1 && len < 2048) {
                        return EntityUtils.toString(entity);
                    } else {
                        // Stream content out
                    }
                }
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } finally {
            response.close();
        }
        return "";
    }

    /**
     * Json parse object.
     *
     * @param json the json
     * @return the object
     */
    public Object jsonParse(String json) {
        return Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    /**
     * Json list less than 700 calories foods.
     *
     * @param document the document
     */
    public void jsonListLessThan700CaloriesFoods(Object document) {
        List<String> foods = JsonPath.read(document, "$.breakfast_menu.food[?(@.calories < 700)].price");
        System.out.println("\nPrices of foods with less than 700 calories (JSON):");
        for (String food : foods) {
            System.out.println(food);
        }
    }

    /**
     * Json list foods.
     *
     * @param document the document
     */
    public void jsonListFoods(Object document) {
        List<String> foods = JsonPath.read(document, "$.breakfast_menu.food[*].name");
        System.out.println("\nFood list (JSON):");
        for (String food : foods) {
            System.out.println(food);
        }
    }

    /**
     * Json max number.
     *
     * @param document the document
     */
    public void jsonMaxNumber(Object document) {
        System.out.println("\nMax number (JSON): " + JsonPath.read(document, "$..numbers.max()"));
    }

    /**
     * Xml list foods.
     *
     * @param menu the menu
     */
    public void xmlListFoods(List<Food> menu) {
        System.out.println("\nFood list (XML):");
        for (Food food : menu) {
            System.out.println(food.getName());
        }
    }

    /**
     * Xml list less than 700 calories foods.
     *
     * @param menu the menu
     */
    public void xmlListLessThan700CaloriesFoods(List<Food> menu) {
        System.out.println("\nPrices of foods with less than 700 calories (XML):");
        for (Food food : menu) {
            if (food.getCalories() < 700) {
                System.out.println(food.getPrice());
            }
        }

    }

    /**
     * Xml list less than 700 calories foods.
     *
     * @param menu the menu
     */
    public void xmlMaxCaloriesFood(List<Food> menu) {
        int maxCaloriesFood = 0;
        String maxCaloriesFoodName = "";
        for (Food food : menu) {
            if (food.getCalories() > maxCaloriesFood) {
                maxCaloriesFood = food.getCalories();
                maxCaloriesFoodName = food.getName();
            }
        }
        if (maxCaloriesFood > 0) {
            System.out.println("\nMax calories food (XML): " + maxCaloriesFoodName);
        }
    }

    /**
     * Xml parse list.
     *
     * @param uri the uri
     * @return the list
     * @throws ParserConfigurationException the parser configuration exception
     * @throws IOException                  the io exception
     * @throws SAXException                 the sax exception
     */
    public List<Food> xmlParse(String uri) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(uri);

        List<Food> foods = new ArrayList<>();
        NodeList menu = document.getElementsByTagName("food");

        if (menu.getLength() == 0) {
            throw new ParseException("No menu items found");
        }

        for (int i = 0; i < menu.getLength(); i++) {
            NodeList menuItem = menu.item(i).getChildNodes();
            String name = null;
            String price = null;
            String description = null;
            int calories = -1;

            for (int j = 0; j < menuItem.getLength(); j++) {
                Node menuItemNode = menuItem.item(j);
                String foodProperty = menuItemNode.getNodeName();
                switch (foodProperty) {
                    case "name":
                        name = menuItemNode.getTextContent();
                        break;
                    case "price":
                        price = menuItemNode.getTextContent();
                        break;
                    case "description":
                        description = menuItemNode.getTextContent();
                        break;
                    case "calories":
                        calories = Integer.parseInt(menuItemNode.getTextContent());
                        break;
                }
            }

            if (name == null || price == null || description == null || calories == -1) {
                throw new ParseException("Unable to initialize menu item record");
            }

            Food food = new Food(name, price, description, calories);
            foods.add(food);
        }

        return foods;
    }
}
