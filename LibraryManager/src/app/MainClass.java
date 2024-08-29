package app;

import View.AuthorView;
import View.MainMenuView;

import javax.swing.*;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainClass {
    public static JFrame mainFrame = new JFrame("Main Application");

    public static void main(String[] args) {

        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create an instance of your MainMenuView
        MainMenuView mainMenuView = new MainMenuView();
        mainFrame.setContentPane(mainMenuView.contentPane);
        mainFrame.pack();
        mainFrame.setVisible(true);

    }



}