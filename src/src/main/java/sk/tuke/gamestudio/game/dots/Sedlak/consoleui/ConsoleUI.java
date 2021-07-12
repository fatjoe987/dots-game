package sk.tuke.gamestudio.game.dots.Sedlak.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.dots.Sedlak.core.Color;
import sk.tuke.gamestudio.game.dots.Sedlak.core.Field;
import sk.tuke.gamestudio.game.dots.Sedlak.core.GameState;
import sk.tuke.gamestudio.service.*;

import java.util.*;

public class ConsoleUI {

    private static final String GAME_NAME = "dots";
    private static String meno = new String();

    private final Field field;
    //private RatingService ratingService = new RatingServiceJDBC();
    //private CommentService commentService = new CommentServiceJDBC();
    //private ScoreService scoreService = new ScoreServiceJDBC();

    @Autowired
    private CommentService commentService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    private int skore=0;
    private int rating;
    private String comment;
    public int counter = 120;

    public ConsoleUI(Field field) {
        this.field = field;
    }



    public void play() {
        printScores();
        System.out.println("\n");
        printComments();
        //printAvgRating();

        System.out.print("Your name: ");
        meno = new Scanner(System.in).nextLine().trim();

        System.out.print("Rate the game (write 1-5): ");
        rating = new Scanner(System.in).nextInt();
        if (rating>5) rating = 5;
        if (rating<1) rating = 1;
        setRate();
        System.out.println("Comment: ");
        comment = new Scanner(System.in).nextLine().trim();
        setComment();
        System.out.println("\n\nConnect dots with same color, represented by " +
                "\nR-red\nG-green\nB-blue\nY-yellow\nP-purple");
        System.out.println("You can connect only neighbouring dots(left, right, up, down)");

        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                counter--;
                if (counter<0) {
                    timer.cancel();
                    field.setState(GameState.TIME_OVER);
                    System.out.println("\n\nTime is over!!!\n\n");

                }
            }
        },0,1000);
        System.out.print("\n");
        do {
            show();
            handleInput();
        } while (field.getState() == GameState.PLAYING);


        if (field.getState()==GameState.TIME_OVER) {
            System.out.println(" ");
            System.out.println("Your score: " + skore);
            scoreService.addScore(new Score(GAME_NAME,meno,skore, new Date()));

            System.exit(0);
        }

    }

    private void handleInput() {
        while (true) {
            System.out.println("Enter input (e.g. A1A2, B2C2,...)");
            System.out.println("Enter X for exit\n");
            String input = new Scanner(System.in).nextLine().trim().toUpperCase();

            if ("X".equals(input)) {
                System.out.println("You quit the game\n");
                System.exit(0);

            }
            if (input.length()<4) {
                System.out.println("You can't connect only one dot. Common sense...\n");
                return;
            }

            if (input.length() % 2 == 0) {
                try {
                    int[] column = new int[30];
                    int[] row = new int[30];
                    Color[][] novadot = new Color[6][6];
                    Color[][] farba = field.getBodka(row[5],column[5]);
                    int i = 0;
                    for (i = 0; i<input.length()/2;i++) {
                        column[i] = input.charAt(i * 2) - 'A';
                        row[i] = input.charAt((i * 2) + 1) - '0' - 1;
                        if (i>=1 && farba[row[i]][column[i]].getColor()!=farba[row[i-1]][column[i-1]].getColor()) {
                            System.out.println("You can't connect two different colors.\n");
                            return;
                        }
                        if (i>=1 && column[i]!=column[i-1] && row[i]!=row[i-1]) {
                            System.out.println("You can only connect neighboring dots\n");
                            return;
                        }
                    }
                    int scoreNow = 0;
                    for (int j = 0; j <input.length()/2;j++) {
                            int posun = row[j];

                            if (row[j] >= 0 && row[j] <= field.getRowCount() && column[j] >= 0 && column[j] <= field.getColCount()) {
                                field.newColor(novadot, row[j], column[j]);


                            } else {
                                System.out.println("Wrong input!!!\n");
                            }
                    }
                    scoreNow = input.length()/2 *input.length()/2;
                    skore = skore + scoreNow;
                    System.out.println("Score this move: " + scoreNow);
                    System.out.println("Your score: " + skore);
                } catch (NumberFormatException e) {
                    System.out.println("Error unknown\n");
                }
            }
            else  {
                System.out.println("Wrong input!!!");
            }
            show();
        }
    }

    private void show() {
        printFieldHeader();
        printFieldBody();
    }

    private void printFieldHeader() {
        System.out.println( "You have: " + counter + " seconds");
        System.out.print(" ");
        for (int column = 0; column < field.getColCount(); column++) {
            System.out.print(" ");
            System.out.print(column + 1);
        }
        System.out.println();

    }

    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print((char) ('A' + row));
            for (int column = 0; column < field.getColCount(); column++) {
                System.out.print(" ");
                printTile(column, row);
            }
            System.out.println();
        }

    }

    private void printTile(int row, int column) {
        Color[][] color = field.getBodka(row, column);
        switch (color[row][column].getColor()) {
            case RED:
                System.out.print("R");
                break;
            case BLUE:
                System.out.print("B");
                break;
            case GREEN:
                System.out.print("G");
                break;
            case PURPLE:
                System.out.print("P");
                break;
            case YELLOW:
                System.out.print("Y");
                break;
            default: 
                System.out.print("-");
            break;
        }
    }
    private void printComments() {
        List<Comment> comments = commentService.getComments(GAME_NAME);
        System.out.println("Comments: ");
        for (Comment c : comments) {
            System.out.println(c);
        }
    }

    private void printScores() {
            List<Score> scores = scoreService.getBestScores(GAME_NAME);

            Collections.sort(scores);
            if (!scores.isEmpty())
            System.out.println("Top scores: ");
            for (Score s : scores) {
                System.out.println(s);
            }
    }
    private void printAvgRating() {
        try {
           int avgRating = ratingService.getAverageRating(GAME_NAME);
                System.out.println("Average rating: " + avgRating);
        } catch (RatingException e) {
            e.printStackTrace();
        }
    }

    private void setRate() {
        try {
            ratingService.setRating(new Rating(meno,GAME_NAME,rating,new Date()));
        } catch (RatingException e) {
            e.printStackTrace();
        }
    }

    private void setComment() {
        try {
            commentService.addComment(new Comment(meno,GAME_NAME,comment,new Date()));
        } catch (CommentException e) {
            e.printStackTrace();
        }
    }

}
