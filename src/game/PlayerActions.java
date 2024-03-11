package game;

public enum PlayerActions {
    FOLD, // discard hand, lose any bets, out until next deal
    CHECK, // declines to make a bet, but remains in the game
    CALL, // match highest bet in the current round
    RAISE, // bet more than the highest bet made in the current round
    ALL_IN // bet all remaining chips
}
