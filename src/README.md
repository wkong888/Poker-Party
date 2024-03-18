# BMHS AP Computer Science Poker Party Project

This project is designed for high school AP Computer Science students. It provides a foundational poker game framework, allowing students to extend the `Player` class to create their own NPC (Non-Player Character) poker players. The goal is to apply object-oriented programming concepts to develop strategies for NPC behavior in a poker game.

## Getting Started

To get started with this project, you'll need to clone the repository and ensure you have a Java development environment set up.

### Prerequisites

- Java Development Kit (JDK) 11 or newer
- An Integrated Development Environment (IDE) like IntelliJ IDEA, Eclipse, or Visual Studio Code

### Installation

1. Clone the repository to your local machine:
2. Open the project in your IDE of choice and ensure it's set to use JDK 11 or newer.

## Project Structure

- `src/game`: Contains the core game logic and classes.
    - `Player.java`: An abstract class representing a player in the poker game. This is the class you will extend.
    - `Evaluator.java`: Handles the evaluation of poker hands.
    - Other game management classes.

## Extending the Player Class

To create your own NPC poker player, extend the `Player` class found in `src/game/Player.java`. Implement the abstract methods to define your NPC's behavior in different game scenarios.

## Example:
<pre>
<code>
public class MyNPCPlayer extends Player {
    public MyNPCPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        // Implement how your NPC takes its turn
    }

    @Override
    protected boolean shouldFold() {
        // Implement your folding logic
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        // Implement your checking logic
        return true;
    }

    @Override
    protected boolean shouldCall() {
        // Implement your calling logic
        return false;
    }

    @Override
    protected boolean shouldRaise() {
        // Implement your raising logic
        return false;
    }

    @Override
    protected boolean shouldAllIn() {
        // Implement your all-in logic
        return false;
    }
}
</code>
</pre>

## Assignment

1. Extend the `Player` class to create your NPC.
2. Implement the logic for deciding when to fold, call, raise, or check.
3. Document your strategy in comments within your class.

## Contributing

This project is an educational tool and contributions are welcome. If you have suggestions to improve this project, please fork the repository and create a pull request.

