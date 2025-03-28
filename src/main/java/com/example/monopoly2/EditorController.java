package com.example.monopoly2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class EditorController {

    VBox[] panes;
    LinkedList<Board> board;

    @FXML
    private HBox boardBottom;

    @FXML
    private VBox boardLeft;

    @FXML
    private VBox boardRight;

    @FXML
    private HBox boardTop;
    @FXML
    private ChoiceBox<String> editorDisplayPos;

    @FXML
    private TextField editorId;

    @FXML
    private TextField removeId;

    @FXML
    private TextField editorName;

    @FXML
    private TextField removeName;

    @FXML
    private TextField editorPrice;

    @FXML
    private TextField editorStreet;

    @FXML
    private TextField editorTax;

    @FXML
    private ChoiceBox<String> editorType;

    @FXML
    private BorderPane entireBoard;

    @FXML
    private VBox newPlaceVBox;

    @FXML
    private VBox removePlaceVBox;

    @FXML
    private Label selectedFile;

    @FXML
    void initialize() {
        editorDisplayPos.getItems().addAll("Top", "Bottom", "Left", "Right");
        editorType.getItems().addAll("Building", "Random", "Lottery", "Free parking", "Prison");
        selectedFile.setText("Selected board filename: " + jsonLoader.currentFile.getName());
        board = jsonLoader.LoadJson();
        loadPlaces();
    }
    void loadPlaces() {
        boardBottom.getChildren().clear();
        boardTop.getChildren().clear();
        boardLeft.getChildren().clear();
        boardRight.getChildren().clear();
        panes = new VBox[board.size()];
        for (int i = 0; i < board.size(); i++)
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("place.fxml"));
            try {
                panes[i] = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            PlaceController placeController = loader.getController();
            placeController.setInfo(board.get(i));
            panes[i].setId("place"+board.get(i).id);
            Label label = new Label("id " + board.get(i).id);
            panes[i].getChildren().add(label);
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
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Game.class.getResource("monopolymenu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        jsonLoader.SaveJson(board);
    }

    @FXML
    void showAdd(ActionEvent event) {
        newPlaceVBox.setVisible(true);
        removePlaceVBox.setVisible(false);
    }
    @FXML
    void showRemove(ActionEvent event) {
        newPlaceVBox.setVisible(false);
        removePlaceVBox.setVisible(true);
    }

    @FXML
    void submitPlace(ActionEvent event) {
        int selection = editorType.getSelectionModel().getSelectedIndex();
        int dPosSelection = editorDisplayPos.getSelectionModel().getSelectedIndex();
        char dPos;
        Board place = null;
        switch (dPosSelection) {
            case 1: {
                dPos = 'b';
                break;
            }
            case 2: {
                dPos = 'l';
                break;
            }
            case 3: {
                dPos = 'r';
                break;
            }
            default: {
                dPos = 't';
                break;
            }
        }
        switch (selection) {
            case 0:
            {
                place = new Board(editorName.getText(),'H',Integer.parseInt(editorStreet.getText()),Integer.parseInt(editorPrice.getText()),Integer.parseInt(editorTax.getText()),Integer.parseInt(editorId.getText()),dPos);
                break;
            }
            case 1:
            {
                place = new Board(editorName.getText(),'R',0,0,0,Integer.parseInt(editorId.getText()),dPos);
                break;
            }
            case 2:
            {
                place = new Board(editorName.getText(),'L',0,0,0,Integer.parseInt(editorId.getText()),dPos);
                break;
            }
            case 3:
            {
                place = new Board(editorName.getText(),'F',0,0,0,Integer.parseInt(editorId.getText()),dPos);
                break;
            }
            case 4:
            {
                place = new Board(editorName.getText(),'P',0,0,0,Integer.parseInt(editorId.getText()),dPos);
                break;
            }
        }
        board.add(Integer.parseInt(editorId.getText()),place);
        for (int i = Integer.parseInt(editorId.getText()); i < board.size(); i++)
        {
            if(board.get(i).id != i)
                board.get(i).id = i;
        }
        loadPlaces();
    }

    @FXML
    void removePlace(ActionEvent event) {
        if (!removeId.getText().isEmpty())
        {
            board.remove(Integer.parseInt(removeId.getText()));
        }
        else if (!removeName.getText().isEmpty())
        {
            board.removeIf(c -> (Objects.equals(c.name, removeName.getText())));
        }
        for (int i = 0; i < board.size(); i++)
        {
            if(board.get(i).id != i)
                board.get(i).id = i;
        }
        loadPlaces();
    }
}
