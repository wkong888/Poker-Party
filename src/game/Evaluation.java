package game;

public class Evaluation {

    private Player player;

    private HandRanks handRank;

    public Evaluation(Player player, HandRanks handRank) {
        this.player = player;
        this.handRank = handRank;
    }

    public Player getPlayer() {
        return player;
    }

    public HandRanks getHandRank() {
        return handRank;
    }
}
