package game; // Declares that this class belongs to the "game" package

// Import necessary player classes from the "players" package
import players.ConservativeNPCPlayer;
import players.KongPlayer;
import players.RandomPlayer;
import players.SimpleNPCPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * The AddPlayers class is responsible for initializing and managing
 * a list of players participating in the game. It creates a predefined
 * set of AI-controlled players with different strategies.
 */
public class AddPlayers {

    // A list to store Player objects representing participants in the game.
    private List<Player> playersInGame;

    /**
     * Constructor for the AddPlayers class.
     * Initializes the playersInGame list and populates it with instances
     * of different types of AI-controlled players.
     */
    public AddPlayers() {
        // Initialize the list that will store Player objects
        playersInGame = new ArrayList<>();

        // Add instances of SimpleNPCPlayer with unique identifiers
        playersInGame.add(new SimpleNPCPlayer("Simple_01"));
        playersInGame.add(new SimpleNPCPlayer("Simple_02"));

        // Add instances of RandomPlayer with unique identifiers
        playersInGame.add(new RandomPlayer("Random_01"));
        playersInGame.add(new RandomPlayer("Random_02"));

        // Add instances of ConservativeNPCPlayer with unique identifiers
        playersInGame.add(new ConservativeNPCPlayer("Conservative_01"));
        playersInGame.add(new ConservativeNPCPlayer("Conservative_02"));

        playersInGame.add(new KongPlayer("Kong_01"));

    }

    /**
     * Retrieves the list of players currently in the game.
     *
     * @return A List of Player objects representing the players participating in the game.
     */
    public List<Player> getPlayersInGame() {
        return playersInGame;
    }
}
