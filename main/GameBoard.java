import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.util.Random;

import javax.swing.Action;

public class GameBoard {
    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int startingTiles = 2;
    private Tiles[][] board;
    private boolean dead;
    private boolean won;
    private boolean hasStarted;
    private BufferedImage gameBoard;
    private BufferedImage finalBoard;
    private int x;
    private int y;

    private static int SPACING = 10;
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * 80;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * 80;

    public GameBoard(int x, int y) {
        this.x = x;
        this.y = y;
        board = new Tiles[ROWS][COLS];
        gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        createBoardImage();
        start();
    }

    private void createBoardImage() {
        Graphics2D g = (Graphics2D) gameBoard.getGraphics();
        g.setColor(new Color(0xBBADA0));
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(new Color(0xCDC1B4));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = SPACING + SPACING * col + 80 * col;
                int y = SPACING + SPACING * row + 80 * row;
                g.fillRect(x, y, 80, 80);
            }
        }
    }

    private void start() {
        for (int i = 0; i < startingTiles; i++) {
            spawnRandom();
        }
    }

    private void spawnRandom() {
        Random rand = new Random();
        boolean notValid = true;

        while (notValid) {
            int location = rand.nextInt(ROWS*COLS);
            int row = location / ROWS;
            int col = location % COLS;

            Tiles current = board[row][col];
            if (current == null) {
                int value = 2;
                if (rand.nextInt(10) < 2) {
                    value = 4;
                }
                Tiles tile = new Tiles(value, getTileX(col), getTileY(row));
                board[row][col] = tile;
                notValid = false;
            }
        } 
    }

    public int getTileX(int col) {
        return SPACING + col * 80 + col * SPACING;
    }

    public int getTileY(int row) {
        return SPACING + row * 80 + row * SPACING;
    }

    public void render(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
        g2d.drawImage(gameBoard, 0, 0, null);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tiles current = board[row][col];
                if (current == null) continue;
                current.render(g2d);
            }
        }

        g.drawImage(finalBoard, x, y, null);
        g2d.dispose();
    }

    public void update() {
        checkKeys();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tiles current = board[row][col];
                if (current == null) continue;
                current.update();
                resetPosition(current, row, col);
                if (current.getValue() == 2048) {
                    won = true;
                }
            }
        }
    }

    private void resetPosition(Tiles current, int row, int col) {
        if (current == null) return;

        int x = getTileX(col);
        int y = getTileY(row);

        int distX = current.getX() - x;
        int distY = current.getY() - y;

        if (Math.abs(distX) < Tiles.SLIDE_SPEED) {
            current.setX(current.getX() - distX);
        }
        
        if (Math.abs(distY) < Tiles.SLIDE_SPEED) {
            current.setY(current.getY() - distY);
        }

        if (distX < 0) {
            current.setX(current.getX() + Tiles.SLIDE_SPEED);
        }

        if (distY < 0) {
            current.setY(current.getY() + Tiles.SLIDE_SPEED);
        }

        if (distX > 0) {
            current.setX(current.getX() - Tiles.SLIDE_SPEED);
        }

        if (distY > 0) {
            current.setY(current.getY() - Tiles.SLIDE_SPEED);
        }
    }

    private boolean move(int row, int col, int horizontalAction, int verticalAction, Actions action) {
        boolean canMove = false;

        Tiles current = board[row][col];
        if (current == null) return false;
        boolean move = true;
        int newCol = col;
        int newRow = row;
        while (move) {
            newCol += horizontalAction;
            newRow += verticalAction;
            if (checkOutOfBounds(action, newRow, newCol)) break;
            if (board[newRow][newCol] == null) {
                board[newRow][newCol] = current;
                board[newRow - verticalAction][newCol - horizontalAction] = null;
                board[newRow][newCol].setSlideTo(new MyPoint(newRow, newCol));
                canMove = true;
            } else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canCombine()) {
                board[newRow][newCol].setCanCombine(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
                canMove = true;
                board[newRow - verticalAction][newCol - horizontalAction] = null;
                board[newRow][newCol].setSlideTo(new MyPoint(newRow, newCol));
                //board[newRow][newCol].setCombineAnimation(true);
            } else {
                move = false;
            }
        }
        return canMove;
    }

    private boolean checkOutOfBounds(Actions action, int row, int col) {
        if (action == Actions.LEFT) {
            return col < 0;
        } else if (action == Actions.RIGHT) {
            return col > COLS - 1;
        } else if (action == Actions.UP) {
            return row < 0;
        } else if (action == Actions.DOWN) {
            return row > ROWS - 1;
        } else {
            return false;
        }
    }

    private void moveTiles(Actions action) {
        boolean canMove = false;
        int horizontalAction = 0;
        int verticalAction = 0;

        if (action == Actions.LEFT) {
            horizontalAction = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalAction, verticalAction, action);
                    } else {
                        move(row, col, horizontalAction, verticalAction, action);
                    }
                }
            }
        } else if (action == Actions.RIGHT) {
            horizontalAction = 1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = COLS - 1; col >= 0; col--) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalAction, verticalAction, action);
                    } else {
                        move(row, col, horizontalAction, verticalAction, action);
                    }
                }
            }
        } else if (action == Actions.UP) {
            verticalAction = -1;
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalAction, verticalAction, action);
                    } else {
                        move(row, col, horizontalAction, verticalAction, action);
                    }
                }
            }
        } else if (action == Actions.DOWN) {
            verticalAction = 1;
            for (int row = ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontalAction, verticalAction, action);
                    } else {
                        move(row, col, horizontalAction, verticalAction, action);
                    }
                }
            }
        } else {
            System.out.println(action + " is not a valid action.");
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Tiles current = board[row][col];
                if (current == null) continue;
                current.setCanCombine(true);
            }
        }

        if (canMove) {
            spawnRandom();
            checkDead();
        }
    }

    private void checkDead() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == null) return;
                if (checkSurroundingTiles(row, col, board[row][col])) {
                    return;
                }
            }
        }
        this.dead = true;
    }

    private boolean checkSurroundingTiles(int row, int col, Tiles current) {
        if (row > 0) {
            Tiles check  = board[row - 1][col];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        } 
        if (row < ROWS - 1) {
            Tiles check  = board[row + 1][col];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        } 
        if (col > 0) {
            Tiles check  = board[row][col - 1];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        } 
        if (col < COLS - 1) {
            Tiles check  = board[row][col + 1];
            if (check == null) return true;
            if (current.getValue() == check.getValue()) return true;
        }
        return false;
    }

    private void checkKeys() {

        if (KeyboardAgent.typed(KeyEvent.VK_LEFT)) {
            moveTiles(Actions.LEFT);
            if (!hasStarted) hasStarted = true;
        }

        if (KeyboardAgent.typed(KeyEvent.VK_RIGHT)) {
            moveTiles(Actions.LEFT);
            if (!hasStarted) hasStarted = true;
        }

        if (KeyboardAgent.typed(KeyEvent.VK_UP)) {
            moveTiles(Actions.UP);
            if (!hasStarted) hasStarted = true;
        }

        if (KeyboardAgent.typed(KeyEvent.VK_DOWN)) {
            moveTiles(Actions.DOWN);
            if (!hasStarted) hasStarted = true;
        }
    }
}