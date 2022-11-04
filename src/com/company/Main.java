package com.company;

import Game.GameManager;

public class Main {

    // to start the game
    public static void main(String[] args) {
        GameManager gameManager = new GameManager(s -> System.out.println(s));
        gameManager.start(args[0]);
    }
}
