package com.example.monopoly2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Monopolymenu extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Game.class.getResource("monopolymenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1315, 810);
        mainScene = scene;
        System.out.println(mainScene);
        stage.setTitle("Monopoly");
        stage.setScene(scene);
        stage.show();
    }

    String[] PlayerNames = {"Player 1", "Player 2", "Player 3"};
    boolean[] PlayerBots = {false,true,true};
    int numberOfPlayers = 3;
    Game game;
    Scene mainScene;

    @FXML
    private TextField startMoneyField;
    @FXML
    private Button saveStartMoney;
    @FXML
    private TextField moneyToWinField;
    @FXML
    private Button saveMoneyToWin;
    @FXML
    private TextField numberOfPlayersField;
    @FXML
    private Button saveNumberOfPlayers;
    @FXML
    private CheckBox player1Bot;
    @FXML
    private TextField player1Name;
    @FXML
    private CheckBox player2Bot;
    @FXML
    private TextField player2Name;
    @FXML
    private CheckBox player3Bot;
    @FXML
    private TextField player3Name;

    public void submitStartMoney(ActionEvent event){
        try {
            Game.startMoney = Integer.parseInt(startMoneyField.getText());
            System.out.println(Game.startMoney);
        }
        catch (NumberFormatException e) {
            System.out.println("NaN");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void submitMoneyToWin(ActionEvent event){
        try {
        Game.moneyToWin = Integer.parseInt(moneyToWinField.getText());
        System.out.println(Game.moneyToWin);
        }
        catch (NumberFormatException e) {
            System.out.println("NaN");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void submitNumberOfPlayers(ActionEvent event){
        try {
            numberOfPlayers = Integer.parseInt(numberOfPlayersField.getText());
            if (numberOfPlayers > 3)
                numberOfPlayers = 3;
            if (numberOfPlayers < 2)
                numberOfPlayers = 2;
            System.out.println(numberOfPlayers);
        }
        catch (NumberFormatException e) {
            System.out.println("NaN");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        game = new Game();
        FXMLLoader loader = new FXMLLoader(com.example.monopoly2.Game.class.getResource("monopolymain.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        game.gameController = loader.getController();
        game.gameController.setPrevScene(startMoneyField.getScene());
        stage.setScene(scene);
        stage.show();
        game.startGame(PlayerNames,PlayerBots,numberOfPlayers);
        game = null;
    }

    @FXML
    public void startEditor(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Game.class.getResource("editor.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void loadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null)
        {
            jsonLoader.currentFile = file;
        }
    }

    @FXML
    void exitGame(ActionEvent event) throws Exception {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    @FXML
    void submitPlayer1(ActionEvent event) {
        PlayerNames[0] = player1Name.getText();
        PlayerBots[0] = player1Bot.isSelected();
    }

    @FXML
    void submitPlayer2(ActionEvent event) {
        PlayerNames[1] = player2Name.getText();
        PlayerBots[1] = player2Bot.isSelected();
    }

    @FXML
    void submitPlayer3(ActionEvent event) {
        PlayerNames[2] = player3Name.getText();
        PlayerBots[2] = player3Bot.isSelected();
    }

    public static void main(String[] args) {

        launch();
    }
}
