package com.pk.Shayari2.Firebase_Database.Quotes_Home_Category;

public class Model_quotes_category {

    String image, name; // these name must match with the name in firebase

    public Model_quotes_category() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
