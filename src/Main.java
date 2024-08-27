import java.io.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize the program's window and set to max resolution
        var gameWindow = new JFrame();
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setVisible(true);
    }
}