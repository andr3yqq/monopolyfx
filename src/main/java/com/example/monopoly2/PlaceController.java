package com.example.monopoly2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlaceController {

    @FXML
    private Label placeColl;

    @FXML
    private Label placeName;

    @FXML
    private Label placePrice;

    private Board place;

    public void setInfo(Board place)
    {
        this.place = place;
        placeName.setText(place.name);
        placePrice.setText("$" + place.price);
        if (place.collection == 0)
            placeColl.setText("");
        else
            placeColl.setText(String.valueOf(place.collection));
    }

    public char getDisplayPos()
    {
        return place.displayPos;
    }

}
