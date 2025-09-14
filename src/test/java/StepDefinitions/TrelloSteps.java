package StepDefinitions;

import Utilities.TrelloApiHelper;
import com.thoughtworks.gauge.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrelloSteps {

    private static String boardId;
    private static String listId;
    private static List<String> cardIds = new ArrayList<>();

    @Step("Trello üzerinde <boardName> isminde bir board oluştur")
    public void createBoard(String boardName) {
        Response response = TrelloApiHelper.createBoard(boardName);
        response.then().statusCode(200);
        boardId = response.jsonPath().getString("id");

        // Board içindeki listeleri al
        Response listsResponse = TrelloApiHelper.getBoardLists(boardId);
        listsResponse.then().statusCode(200);

        // İlk listeyi al
        listId = listsResponse.jsonPath().getString("[0].id");
    }

    @Step("Board üzerine <cardName> isminde bir kart ekle")
    public void createCard(String cardName) {
        Response response = TrelloApiHelper.createCard(listId, cardName);
        response.then().statusCode(200);
        cardIds.add(response.jsonPath().getString("id"));
    }

    @Step("Oluşturulan kartlardan rastgele birini <newName> olarak güncelle")
    public void updateRandomCard(String newName) {
        if (cardIds.isEmpty()) return;
        String cardId = cardIds.get(new Random().nextInt(cardIds.size()));
        TrelloApiHelper.updateCard(cardId, newName).then().statusCode(200);
    }

    @Step("Oluşturulan tüm kartları sil")
    public void deleteCards() {
        for (String cardId : cardIds) {
            TrelloApiHelper.deleteCard(cardId).then().statusCode(200);
        }
        cardIds.clear();
    }

    @Step("Board'u sil")
    public void deleteBoard() {
        TrelloApiHelper.deleteBoard(boardId).then().statusCode(200);
    }
}
