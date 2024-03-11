package players;

import game.Player;

import java.util.Scanner;

public class ManualPlayer extends Player {
    Scanner kb;

    public ManualPlayer(String name) {
        super(name);
        kb = new Scanner(System.in);
    }

    @Override
    public void takePlayerTurn() {
        //printExampleStateInformation();
        String response = "";
        do {
            System.out.println("fold, check, call, raise, or all in? [fold/check/call/raise/all]");
            response = kb.nextLine().trim().toLowerCase();
        } while(!response.equals("fold") && !response.equals("check") &&!response.equals("call") && !response.equals("raise") && !response.equals("all"));
        switch(response) {
            case "fold":
                fold();
                break;
            case "check":
                check();
                break;
            case "call":
                call();
                break;
            case "raise":
                System.out.println("amount?");
                raise(kb.nextInt());
                kb.nextLine();
                break;
            case "all":
                allIn();
                break;
        }
    }

    @Override
    public boolean shouldFold() {
        return false;
    }

    @Override
    public boolean shouldCheck() {
        return false;
    }

    @Override
    public boolean shouldCall() {
        return false;
    }

    @Override
    public boolean shouldRaise() {
        return false;
    }

    @Override
    public boolean shouldAllIn() {
        return false;
    }
}
