package com.automation;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;
import static com.automation.ExpectedData.*;

public class TutorialsAPITest {
    private static int uniqueId;

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = TutorialConstants.BASE_URL;
    }

    @BeforeMethod
    public void setUpID() {
        Response response = HttpClient.createTutorial(TITLE, DESCRIPTION, PUBLISHED);
        assertEquals(response.getStatusCode(), 201);
        uniqueId = response.jsonPath().getInt("id");
    }

    @Test
    public void testCreateTutorial() {
        Response response = HttpClient.createTutorial(TITLE, DESCRIPTION, PUBLISHED);
        assertEquals(response.statusCode(), 201);
        assertEquals(response.jsonPath().getString("title"), TITLE);
        assertEquals(response.jsonPath().getString("description"), DESCRIPTION);
        assertEquals(response.jsonPath().getBoolean("published"), PUBLISHED);
    }

    @Test
    public void testCreatePublishedTutorial() {
        Response response = HttpClient.createTutorial(TITLE, DESCRIPTION, PUBLISHED);
        assertEquals(response.statusCode(), 201);
        assertEquals(response.jsonPath().getString("title"), TITLE);
        assertEquals(response.jsonPath().getString("description"), DESCRIPTION);
    }

    @Test
    public void testGetAllTutorials() {
        Response response = RestAssured.get();
        assertEquals(response.getStatusCode(), 200);
        assertFalse(response.getBody().jsonPath().getList("$").isEmpty());
    }

    @Test
    public void testReadTutorial() {
        Response response = RestAssured.get(TutorialConstants.BASE_URL + "/" + uniqueId);
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.jsonPath().getInt("id"), uniqueId);
        assertEquals(response.jsonPath().getString("title"), TITLE);
        assertEquals(response.jsonPath().getString("description"), DESCRIPTION);
        assertFalse(response.jsonPath().getBoolean("published"));
    }

    @Test
    public void testReadAllTutorialsByKeyword() {
        Response response = RestAssured.get(TutorialConstants.BASE_URL + "?title=" + TITLE);
        assertEquals(response.getStatusCode(), 200);
        assertFalse(response.getBody().jsonPath().getList("$").isEmpty());

        List<String> tutorialTitles = response.jsonPath().getList("title");
        for (String title: tutorialTitles) {
            assertTrue(title.contains(TITLE));
        }
    }

    @Test
    public void testReadAllTutorialsPublished() {
        Response response = RestAssured.get(TutorialConstants.BASE_URL + "/published");
        assertEquals(response.statusCode(), 200);
        assertFalse(response.getBody().jsonPath().getList("$").isEmpty());

        List<Boolean> tutorialsPublished = response.jsonPath().getList("published");
        for (Boolean published: tutorialsPublished) {
            assertTrue(published);
        }
    }

    @Test
    public void testUpdateTutorial() {
        Response response = HttpClient.updateTutorial(uniqueId, NEW_TITLE, NEW_DESCRIPTION, NEW_PUBLISHED);
        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getInt("id"), uniqueId);
        assertEquals(response.jsonPath().getString("title"), NEW_TITLE);
        assertEquals(response.jsonPath().getString("description"), NEW_DESCRIPTION);
        assertEquals(response.jsonPath().getBoolean("published"), NEW_PUBLISHED);
    }

    @Test
    public void testDeleteTutorial() {
        Response response = HttpClient.deleteTutorial(uniqueId);
        assertEquals(response.statusCode(), 204);

        response = RestAssured.get(TutorialConstants.BASE_URL + "/" + uniqueId);
        assertEquals(response.statusCode(), 404);
    }

    @Test
    public void testDeleteALlTutorials() {
        Response response = RestAssured.delete();
        assertEquals(response.statusCode(), 204);

        response = RestAssured.get();
        assertTrue(response.getBody().jsonPath().getList("$").isEmpty());
    }

    @AfterMethod
    public void tearDown() {
        HttpClient.deleteTutorial(uniqueId);
    }
}