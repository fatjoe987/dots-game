package sk.tuke.gamestudio.game.dots.Sedlak.core;


public class Field {
    private final int rowCount;
    private final int colCount;
    private Color[][] bodka;



    private GameState state = GameState.PLAYING;

    public Field(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        bodka = new Color[rowCount][colCount];
        generate();
    }

    private void generate() {
        generateTiles();

    }

    public void newColor(Color[][] bodka, int row, int col) {
        bodka[row][col] = new Color() {
            @Override
            public void setColor(DotColor color) {
                super.setColor(color);
            }
        };
        this.bodka[row][col] = bodka[row][col];
    }

    private void generateTiles() {
        for (int i = 0;i<colCount;i= i+1) {
            for (int j = 0;j<rowCount;j=j+1) {
                bodka[i][j] = new Color() {
                    @Override
                    public void setColor(DotColor color) {
                        super.setColor(color);
                    }
                };
            }
        }
    }

    public Color[][] getBodka(int row, int column) {
        return bodka;
    }



    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isTimeOver() {
        if (getState()==GameState.TIME_OVER) {
            return true;
        }
        return false;
    }

}
