package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.game.dots.Sedlak.core.Color;
import sk.tuke.gamestudio.game.dots.Sedlak.core.Field;

import static org.junit.Assert.assertEquals;

public class ColorTest {
    private final Field field;

    public ColorTest(Field field) {
        this.field = field;
    }

    @Test
    public void isSameColor() throws Exception {
        int row = 0, column = 0;
        int farbaBodky;
        Color[][] color = field.getBodka(row, column);
        switch (color[row][column].getColor()) {
            case RED:
                farbaBodky = 1;
                break;
            case BLUE:
                farbaBodky = 2;
                break;
            case GREEN:
                farbaBodky = 3;
                break;
            case PURPLE:
                farbaBodky = 4;
                break;
            case YELLOW:
                farbaBodky = 5;
                break;
            default:
                farbaBodky = 0;
                break;
        }
        assertEquals(1|2|3|4|5,farbaBodky);

    }
}