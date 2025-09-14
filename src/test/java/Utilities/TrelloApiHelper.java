package Utilities;

import BaseTest.BaseApiTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TrelloApiHelper {

    // Board oluştur
    public static Response createBoard(String boardName) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .queryParam("name", boardName)
                .post("/boards");
    }

    // Board içindeki listeleri al
    public static Response getBoardLists(String boardId) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .get("/boards/" + boardId + "/lists");
    }

    // Kart oluştur
    public static Response createCard(String listId, String cardName) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .queryParam("idList", listId)
                .queryParam("name", cardName)
                .post("/cards");
    }

    // Kart güncelle
    public static Response updateCard(String cardId, String newName) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .queryParam("name", newName)
                .put("/cards/" + cardId);
    }

    // Kart sil
    public static Response deleteCard(String cardId) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .delete("/cards/" + cardId);
    }

    // Board sil
    public static Response deleteBoard(String boardId) {
        return RestAssured
                .given()
                .queryParam("key", BaseApiTest.KEY)
                .queryParam("token", BaseApiTest.TOKEN)
                .delete("/boards/" + boardId);
    }
}
