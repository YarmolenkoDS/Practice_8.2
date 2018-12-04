package ua.edu.sumdu.ta.yarmolenko.pr8_2;

/**
 * The type Food.
 */
public class Food {
    private String name;
    private String price;
    private String description;
    private int calories;

    /**
     * Instantiates a new Food.
     *
     * @param name        the name
     * @param price       the price
     * @param description the description
     * @param calories    the calories
     */
    public Food(String name, String price, String description, int calories) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.calories = calories;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets calories.
     *
     * @return the calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Sets calories.
     *
     * @param calories the calories
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
