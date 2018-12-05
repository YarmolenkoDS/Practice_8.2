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
     * Gets http response.
     *
     * @param uri the uri
     * @return the http response
     * @throws IOException the io exception
     */
    public CloseableHttpResponse getHttpResponse(String uri) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        return httpclient.execute(httpget);
    }

    /**
     * Gets ta header.
     *
     * @param uri the uri
     * @return the ta header
     * @throws IOException the io exception
     */
    public String getTAHeader(String uri) throws IOException {
        CloseableHttpResponse response = getHttpResponse(uri);
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
        CloseableHttpResponse response = getHttpResponse(uri);
        try {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return EntityUtils.toString(entity);
                } else {
                    throw new ClientProtocolException("Response without entity");
                }
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } finally {
            response.close();
        }
    }

    /**
     * Json parse object.
     *
     * @param json the json
     * @return the object
     * @throws IllegalArgumentException the illegal argument exception
     */
    public Object jsonParse(String json) throws IllegalArgumentException {
        if (json == null || json.equals("")) {
            throw new IllegalArgumentException("Empty or null argument");
        }
        return Configuration.defaultConfiguration().jsonProvider().parse(json);
    }

    /**
     * Json list less than 700 calories foods.
     *
     * @param document the document
     * @throws NullPointerException the null pointer exception
     */
    public void jsonListLessThan700CaloriesFoods(Object document) throws NullPointerException {
        if (document == null) {
            throw new NullPointerException();
        }
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
     * @throws NullPointerException the null pointer exception
     */
    public void jsonListFoods(Object document) throws NullPointerException {
        if (document == null) {
            throw new NullPointerException();
        }
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
     * @throws NullPointerException the null pointer exception
     */
    public void jsonMaxNumber(Object document) throws NullPointerException {
        if (document == null) {
            throw new NullPointerException();
        }
        System.out.println("\nMax number (JSON): " + JsonPath.read(document, "$..numbers.max()"));
    }

    /**
     * Xml list foods.
     *
     * @param menu the menu
     * @throws NullPointerException the null pointer exception
     */
    public void xmlListFoods(List<Food> menu) throws NullPointerException {
        if (menu == null) {
            throw new NullPointerException();
        }
        System.out.println("\nFood list (XML):");
        for (Food food : menu) {
            System.out.println(food.getName());
        }
    }

    /**
     * Xml list less than 700 calories foods.
     *
     * @param menu the menu
     * @throws NullPointerException the null pointer exception
     */
    public void xmlListLessThan700CaloriesFoods(List<Food> menu) throws NullPointerException  {
        if (menu == null) {
            throw new NullPointerException();
        }
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
     * @throws NullPointerException the null pointer exception
     */
    public void xmlMaxCaloriesFood(List<Food> menu) throws NullPointerException {
        if (menu == null) {
            throw new NullPointerException();
        }
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
     * @throws IllegalArgumentException     the illegal argument exception
     */
    public List<Food> xmlParse(String uri) throws ParserConfigurationException, IOException, SAXException, IllegalArgumentException {
        if (uri == null || uri.equals("")) {
            throw new IllegalArgumentException("Empty or null argument");
        }
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
