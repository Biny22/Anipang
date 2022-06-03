package Anipang;

import java.util.ArrayList;
import java.util.HashSet;

class Controller {

    private GUI gui;

    Controller(GUI gui)
    {
        this.gui = gui;
    }

    void setOutImg(HashSet<ArrayList<Integer>> set)
    {
        gui.setOutImage(set);
    }

    void setInImg()
    {
        gui.setInImage();
    }

    void setScore()
    {
        gui.setScore();
    }

    void setTime(long time)
    {
        gui.setTime(time);
    }

    void gameSet()
    {
        gui.gameSet();
    }
}
