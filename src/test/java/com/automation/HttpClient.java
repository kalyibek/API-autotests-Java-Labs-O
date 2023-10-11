package com.automation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;

public class HttpClient {

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = TutorialConstants.BASE_URL;
    }

    public static Response createTutorial(String title, String description, boolean published) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.setTitle(title);
        jsonObject.setDescription(description);
        jsonObject.setPublished(published);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonObject.json())
                .post();
    }

    public static Response updateTutorial(int id, String newTitle, String newDescription, boolean newPublished) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.setTitle(newTitle);
        jsonObject.setDescription(newDescription);
        jsonObject.setPublished(newPublished);

        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(jsonObject.json())
                .put(TutorialConstants.BASE_URL + "/" + id);
    }

    public static Response deleteTutorial(int id) {
        return RestAssured.delete(TutorialConstants.BASE_URL + "/" + id);
    }
}
