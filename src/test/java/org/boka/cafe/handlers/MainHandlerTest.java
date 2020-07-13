package org.boka.cafe.handlers;

import org.boka.cafe.Main;
import org.boka.cafe.Misc.Misc;
import org.boka.cafe.db.DatabaseManipulation;
import org.boka.cafe.pojo.Coordinates;
import org.junit.Test;

public class MainHandlerTest {

    @Test
    public void onUpdateReceived() {
        Coordinates c1 = new Coordinates(48.469936f, 35.00826f);
        Coordinates c2 = new Coordinates(48.4770335f, 35.01431519999999f);
        Misc.isSimilarCoordinates(c1,c2);
        DatabaseManipulation.fillCacheLangAndDistance();
//        DatabaseManipulation.getTextFromDB();
    }
}