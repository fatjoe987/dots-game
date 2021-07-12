package sk.tuke.gamestudio;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScoreServiceJDBCTest {
    private ScoreService service = new ScoreServiceJDBC();
    @Test
    public void addScore() {
        Score score = new Score("dots", "Aladin", 200, new Date());
        service.addScore(score);
        List<Score> scores = service.getBestScores("dots");

        assertEquals(1, scores.size());
        assertEquals("Zuzka", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getPoints());
        assertEquals("dots", scores.get(0).getGame());

    }
}
