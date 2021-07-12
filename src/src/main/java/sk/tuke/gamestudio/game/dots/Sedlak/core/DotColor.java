package sk.tuke.gamestudio.game.dots.Sedlak.core;


public enum DotColor {
    RED, BLUE, GREEN, YELLOW, PURPLE;

    public static DotColor getRandom() {
        return values()[(int) (Math.random()* values().length)];
    }
}
