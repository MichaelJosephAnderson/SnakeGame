import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	
	
	// fields
	// snake is stored as an arraylist of positions of body segments
	private ArrayList<Point> bodySegments;
	private Point apple;
	private static final int ROWS = 30;
	private static final int COLS = 40;
	private int direction;
	private Timer timer;
	private int mode = 0;
	
	//constructor
	public GamePanel() {
		restartGame();
		//create and randomly locate snake and apple
		timer = new Timer(65, this);
		timer.start();
		
	}
	
	// method returns random empty location
	
	private void restartGame() {
		bodySegments = new ArrayList<Point>();
		bodySegments.add(randomEmptyLocation());
		apple = randomEmptyLocation();
		direction = 0; // no movement
		mode = 1;
	}
	
	private Point randomEmptyLocation() {
		while (true) {
			int randRow = (int) ((Math.random() * (ROWS -1)));
			int randCol = (int) ((Math.random() * (COLS -1)));
			boolean flag = false;
			for(Point p : bodySegments) {
				if (p.x == randRow && p.y == randCol) {
					flag = true;
				}
			}
			if (flag == false) {
				return new Point(randRow, randCol);
			}
		}
		// TO DO: add logic to verify spot is empty
	}

	public void paintComponent(Graphics g) {
		if (mode == 2) {
			Font f = new Font("Lucida Console", Font.PLAIN, 48);
			g.setFont(f);
			String s1 = "Game Over";
			String s2 = "Press space to start new game";
			FontMetrics fm = g.getFontMetrics();
			int size1 = fm.stringWidth(s1);
			int size2 = fm.stringWidth(s2);
			g.drawString(s1, this.getWidth() / 2 - size1 / 2, this.getHeight() / 3);
			g.drawString(s2, this.getWidth() / 2 - size2 / 2, this.getHeight() / 2);
			
			return;
		}
		super.paintComponent(g);
		// draw snake
		drawSnake(g);
		
		// draw apple
		drawApple(g);
		
		// draw border
		//drawBorder(g);
		
	}

	private void drawBorder(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0,0,this.getWidth(),this.getWidth()/COLS);
		g.fillRect(0, 0, this.getWidth()/COLS, this.getHeight());
		g.fillRect(0, this.getHeight() - (this.getWidth()/COLS), this.getWidth(), this.getWidth()/COLS);
		g.fillRect(this.getWidth() - (this.getWidth()/COLS), 0, this.getWidth()/COLS, this.getHeight());
	}

	private void drawApple(Graphics g) {
		drawBlock(g, apple, Color.RED);
		
	}

	private void drawSnake(Graphics g) {
		for (int i = 0; i < bodySegments.size(); i++) {
			Color bodyColor = new Color(28, 160, 200 - 200 * (bodySegments.size() - i) / (bodySegments.size()));
			drawBlock(g, bodySegments.get(i), bodyColor);
		}
		// draw head different
		Point head = bodySegments.get(0);
		Color headColor = new Color(28, 160, 31);
		drawBlock(g, head, headColor);
		
		// drawing tounge
		int toungeX = 0, toungeY = 0;
		int toungeLength = 10;
		int toungeWidth = 4;
		int blockWidth = this.getWidth() / COLS;
		int blockHeight = this.getHeight() / ROWS;
		int blockX = (int) (blockWidth * head.getY());
		int blockY = (int) (blockHeight * head.getX());
		
		if (direction == KeyEvent.VK_UP) {
			toungeX = blockX + (blockWidth - toungeWidth) / 2;
			toungeY = blockY - toungeLength;
			g.setColor(Color.RED);
			g.fillRect(toungeX, toungeY, toungeWidth, toungeLength);
		}else if (direction == KeyEvent.VK_DOWN) {
			toungeX = blockX + (blockWidth - toungeWidth) / 2;
			toungeY = blockY + blockHeight;
			g.setColor(Color.RED);
			g.fillRect(toungeX, toungeY, toungeWidth, toungeLength);
		}else if (direction == KeyEvent.VK_LEFT) {
			toungeX = (blockX - toungeLength);
			toungeY = blockY + (blockHeight - toungeWidth) / 2;
			g.setColor(Color.RED);
			g.fillRect(toungeX, toungeY, toungeLength, toungeWidth);
		}else if (direction == KeyEvent.VK_RIGHT) {
			toungeX = (blockX + blockWidth);
			toungeY = blockY + (blockHeight - toungeWidth) / 2;
			g.setColor(Color.RED);
			g.fillRect(toungeX, toungeY, toungeLength, toungeWidth);
		}
		
	}
	
	private void drawBlock(Graphics g, Point p, Color c) {
		int blockWidth = this.getWidth() / COLS;
		int blockHeight = this.getHeight() / ROWS;
		int blockX = (int) (blockWidth * p.getY());
		int blockY = (int) (blockHeight * p.getX());
		g.setColor(c);
		g.fillRect(blockX, blockY, blockWidth - 1, blockHeight - 1);
	}


	public void actionPerformed(ActionEvent arg0) {
		// changer state variables based on direction
		Point head = bodySegments.get(0);
		Point newHead = new Point(head.x, head.y);
		if (direction != 0) {
			int changeX = 0, changeY = 0;
			if (direction == KeyEvent.VK_UP) {
				changeY --;
			}else if (direction == KeyEvent.VK_DOWN) {
				changeY++;
			}else if (direction == KeyEvent.VK_LEFT) {
				changeX--;
			}else if (direction == KeyEvent.VK_RIGHT) {
				changeX++;
			}
			
			// move snake in that direction
			newHead.x = head.x + changeY;
			newHead.y = head.y + changeX;
			
		}
		
		// check if snake is on the apple, then eat apple and place a new one
		if (newHead.x == apple.x && newHead.y == apple.y) {
			bodySegments.add(0, newHead);		// insert a new head on the apple location
			apple = this.randomEmptyLocation();
		}else {
			bodySegments.add(0, newHead);
			bodySegments.remove(bodySegments.size() - 1);
		}
		
		// check if snake has gone off the edge
		if (newHead.x == -1 || newHead.y == -1 || newHead.x == ROWS + 1 || newHead.y == COLS) {
			mode = 2;
			return;
		}
		
		// check if snake is on itself
		for (int i = 1; i < bodySegments.size(); i++) {
			if (newHead.x == bodySegments.get(i).x && newHead.y == bodySegments.get(i).y) {
				mode = 2;
				return;
			}
		}
		// redraw state
		repaint();
		
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_SPACE && mode == 2) {
			restartGame();
		}
		int newDirection = arg0.getKeyCode();
		if (bodySegments.size() > 1 && (newDirection == KeyEvent.VK_UP && direction == KeyEvent.VK_DOWN ||
				newDirection == KeyEvent.VK_DOWN && direction ==KeyEvent.VK_UP ||
				newDirection == KeyEvent.VK_LEFT && direction == KeyEvent.VK_RIGHT||
				newDirection == KeyEvent.VK_RIGHT && direction == KeyEvent.VK_LEFT)) {
			return;
		}else {
			direction = newDirection;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
	
	