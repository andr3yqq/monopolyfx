package com.example.monopoly2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class GameController {

    String newline = "\n";
    String bluecolor = "#1e90ff";
    String greencolor = "#32c738";
    String redcolor = "#ff2d1f";

    private Object TURN_PAUSE = new Object();
    private Object PROP_PAUSE = new Object();
    Scene prevScene;

    Player player;
    int dice1, dice2;
    boolean bought;


    @FXML
    private BorderPane entireBoard;

    VBox[] panes;
    HBox tempbox;

    @FXML
    private HBox boardBottom;

    @FXML
    private VBox boardLeft;

    @FXML
    private VBox boardRight;

    @FXML
    private HBox boardTop;

    @FXML
    private TextFlow textFlow1;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label player1Money;

    @FXML
    private Label player1Name;

    @FXML
    private Label player2Money;

    @FXML
    private Label player2Name;

    @FXML
    private Pane player3pane;

    @FXML
    private Label player3Money;

    @FXML
    private Label player3Name;

    public void loadPlaces(int playerCount) {
        scrollPane.vvalueProperty().bind(textFlow1.heightProperty());
        panes = new VBox[Game.board.size()];
        for (int i = 0; i < Game.board.size(); i++)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("place.fxml"));
            try {
                panes[i] = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            PlaceController placeController = loader.getController();
            placeController.setInfo(Game.board.get(i));
            panes[i].setId("place"+Game.board.get(i).id);
            switch (placeController.getDisplayPos()){
                case 't':
                {
                    boardTop.getChildren().add(panes[i]);
                    HBox.setHgrow(panes[i], Priority.ALWAYS);
                    break;
                }
                case 'r':
                {
                    boardRight.getChildren().add(panes[i]);
                    VBox.setVgrow(panes[i], Priority.ALWAYS);
                    break;
                }
                case 'b':
                {
                    boardBottom.getChildren().addFirst(panes[i]);
                    HBox.setHgrow(panes[i], Priority.ALWAYS);
                    break;
                }
                case 'l':
                {
                    boardLeft.getChildren().addFirst(panes[i]);
                    VBox.setVgrow(panes[i], Priority.ALWAYS);
                    break;
                }
                default:
                    break;

            }
        }
        tempbox = new HBox();
        tempbox.setSpacing(3);
        tempbox.setAlignment(Pos.TOP_CENTER);
        Circle circle = new Circle(8);
        circle.setFill(Paint.valueOf(bluecolor));
        circle.setStroke(Color.BLACK);
        circle.setId("player" + 1);
        tempbox.getChildren().add(circle);
        circle = new Circle(8);
        circle.setFill(Paint.valueOf(greencolor));
        circle.setStroke(Color.BLACK);
        circle.setId("player" + 2);
        tempbox.getChildren().add(circle);
        if (playerCount == 3)
        {
            circle = new Circle(8);
            circle.setFill(Paint.valueOf(redcolor));
            circle.setStroke(Color.BLACK);
            circle.setId("player" + 3);
            tempbox.getChildren().add(circle);
            player3pane.setVisible(true);
        }
        panes[0].getChildren().add(tempbox);

    }

    public void updatePlace(int placeId, int playerId)
    {
        String color = "#ffffff";
        switch (playerId)
        {
            case 1:
            {
                color = bluecolor;
                break;
            }
            case 2:
            {
                color = greencolor;
                break;
            }
            case 3:
            {
                color = redcolor;
                break;
            }
            default:
                break;
        }
        String style = String.format("-fx-border-color: black; -fx-background-color: %s;", color);

        panes[placeId].setStyle(style);
    }

    public void UpdatePos(int placeId, int newPos, int playerId)
    {
        String color = "#ffffff";
        switch (playerId)
        {
            case 1:
            {
                color = bluecolor;
                break;
            }
            case 2:
            {
                color = greencolor;
                break;
            }
            case 3:
            {
                color = redcolor;
                break;
            }
        }
        Circle circle = new Circle(8);
        circle.setFill(Paint.valueOf(color));
        circle.setStroke(Color.BLACK);
        circle.setId("player" + playerId);
        tempbox = (HBox)panes[placeId].getChildren().getLast();
        panes[placeId].getChildren().removeLast();
        tempbox.getChildren().removeIf(c -> (Objects.equals(c.getId(), circle.getId())));
        panes[placeId].getChildren().add(tempbox);
        Node node = panes[newPos].getChildren().getLast();
        if (node instanceof HBox)
        {
            tempbox = (HBox)panes[newPos].getChildren().getLast();
            panes[newPos].getChildren().removeLast();
        }
        else
        {
            tempbox = new HBox();
            tempbox.setSpacing(3);
            tempbox.setAlignment(Pos.TOP_CENTER);
        }
        tempbox.getChildren().add(circle);
        panes[newPos].getChildren().add(tempbox);
    }

    public void DeletePos(int placeId, int playerId)
    {
        Circle circle = new Circle(8);
        circle.setId("player" + playerId);
        tempbox = (HBox)panes[placeId].getChildren().getLast();
        panes[placeId].getChildren().removeLast();
        tempbox.getChildren().removeIf(c -> (Objects.equals(c.getId(), circle.getId())));
        panes[placeId].getChildren().add(tempbox);
    }

    @FXML
    void throwDice(ActionEvent event) {
        dice1 = player.RollDice();
        dice2 = player.RollDice();
        Platform.exitNestedEventLoop(TURN_PAUSE,null);
    }

    @FXML
    void buyItem(ActionEvent event)
    {
        bought = true;
        Platform.exitNestedEventLoop(PROP_PAUSE,null);
    }

    @FXML
    void dontBuyItem(ActionEvent event)
    {
        bought = false;
        Platform.exitNestedEventLoop(PROP_PAUSE,null);
    }

    public void SetPlayerNames(Player[] players, int playerCount)
    {

        player1Name.setText(players[0].name);
        player2Name.setText(players[1].name);
        if (playerCount == 3)
            player3Name.setText(players[2].name);
    }

    public void PlayerThrowDice(Player currentPlayer, int[] dice)
    {
        player = currentPlayer;
        Platform.enterNestedEventLoop(TURN_PAUSE);
        dice[0] = dice1;
        dice[1] = dice2;
    }

    public boolean PlayerProperty(Player currentPlayer)
    {
        Platform.enterNestedEventLoop(PROP_PAUSE);
        return bought;
    }

    public void DisplayText(String gameText)
    {
        Text t1 = new Text(newline+gameText);
        t1.setFont(new Font(20));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),e -> {}));
        timeline.playFromStart();
        textFlow1.getChildren().add(t1);

    }

    public void UpdatePlayerStats(Player[] players, int playerCount)
    {
        if (players[0].money < 0)
            player1Money.setText("lost");
        else
            player1Money.setText("$" + players[0].money);
        if (players[1].money < 0)
            player2Money.setText("lost");
        else
            player2Money.setText("$" + players[1].money);
        if (playerCount == 3) {
            if (players[2].money < 0)
                player3Money.setText("lost");
            else
                player3Money.setText("$" + players[2].money);
        }
    }

    void setPrevScene(Scene scene)
    {
        prevScene = scene;
    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(prevScene);
        stage.show();
    }

    @FXML
    void surrender(ActionEvent event) {
        player.money = -1;
        Platform.exitNestedEventLoop(TURN_PAUSE,null);
    }
}
