import java.util.HashMap;

public class WumpusNode {
	
	private static int numNodes = 0;
	
	private GameTile[][] maze;
	private int posX;
	private int posY;
	
	private int wumpusX = -1;
	private int wumpusY = -1;
	
	private int goldX = -1;
	private int goldY = -1;
	
	private WumpusNode next;
	private int numActions = 0;
	private String action = "";
	
	private String mazeStr = "7";
	private static HashMap<String, Boolean> explored = new HashMap<String, Boolean>();
	private static boolean victory = false;
	
	// Getter's
	public int getNumNodes() {return numNodes;}
	
	public GameTile[][] getMaze() {return maze;}
	
	public int getX() {return posX;}
	
	public int getY() {return posY;}
	
	public WumpusNode getNext() {return next;}
	
	public int getNumActions() {return numActions;}
	
	public String getAction() {return action;}
	
	public static boolean getVict() {return victory;}
	
	// Setter's
	public void setX(int x) {posX = x;}
	
	public void setY(int y) {posY = y;}
	
	public void setNext(WumpusNode next) {this.next = next;}
	
	public void setAction(String action) {this.action += action;}
	
	public void setMazeString(String mazeStr) {this.mazeStr = mazeStr;}
	
	public static void setVict(boolean bool) {victory = bool;}
	
	static public void addToHash(String hash) {explored.put(hash, true);}
	
	static public boolean inHash(String hash) {return explored.containsKey(hash);}
	
	public WumpusNode(GameTile[][] maze, int x, int y, int actions, String action) {
		numNodes++;
		this.maze = new GameTile[maze.length][maze[0].length];
		
		for(int i=0; i<maze.length; i++) {
			for(int j=0; j<maze[0].length; j++) {
				if(maze[i][j] == null) {
					this.maze[i][j] = null;
				}
				else {
					this.maze[i][j] = new GameTile(maze[i][j]);
				}
			}
		}
		
		posX = x;
		posY = y;
		numActions = actions+1;
		this.action = action;
	}

	@Override
	public String toString() {
		if(mazeStr.equals("7")) {
			mazeStr = posX + " " + posY + " ";
			for(int i=0; i<maze.length; i++) {
				for(int j=0; j<maze[0].length; j++) {
					if(maze[i][j] == null) {
						mazeStr = mazeStr + "null";
					}
					else {
						mazeStr = mazeStr + maze[i][j].toString();
					}
				}
			}
			
			return mazeStr;
		}
		else {
			return mazeStr;
		}
	}
	
	public String goldString() {
		mazeStr = posX + " " + posY + " ";
		for(int i=0; i<maze.length; i++) {
			for(int j=0; j<maze[0].length; j++) {
				mazeStr = mazeStr + maze[i][j].toString();
			}
		}
		
		mazeStr+= " GOLD";
		
		return mazeStr;
	}
	
	public WumpusNode[] genChildren() {
		WumpusNode[] w = new WumpusNode[] {null, null, null, null, null};
		// NODES: pickup, down, left, up, right
		// DO NOT generate if tile has wumpus or pit
		
		if(maze[posX][posY].hasGlitter() && checkNode(maze[posX][posY])) {
			w[0] = new WumpusNode(maze, posX, posY, numActions, action);
			w[0].setAction("pickup ");
			w[0].setMazeString(w[0].goldString());
			w[0].maze[posX][posY].setGlitter(false);
			
			return w;
		}
		
		// Move Down node: x + 1
		if(posX + 1 <= maze.length) {
			if(checkNode(maze[posX+1][posY])) {
				//System.out.println("MOVING DOWN TO: " + posX + " " + (posY+1));
				w[1] = new WumpusNode(maze, posX+1, posY, numActions, action);
				w[1].setAction("movedown ");
			}
		}
		// Move Left node: y - 1
		if(posY - 1 >= 0) {
			if(checkNode(maze[posX][posY-1])) {
				//System.out.println("MOVING LEFT TO: " + (posX-1) + " " + posY);
				w[2] = new WumpusNode(maze, posX, posY-1, numActions, action);
				w[2].setAction("moveleft ");
			}
		}
		
		// Move Right node: y + 1
		if(posY + 1 <= maze[0].length) {
			if(checkNode(maze[posX][posY+1])) {
				//System.out.println("MOVING RIGHT TO: " + (posX+1) + " " + posY);
				w[3] = new WumpusNode(maze, posX, posY+1, numActions, action);
				w[3].setAction("moveright ");
			}
		}
		
		// Move Up node: x - 1
		if(posX - 1 >= 0) {
			if(checkNode(maze[posX-1][posY])) {
				//System.out.println("MOVING UP TO: " + posX + " " + (posY-1));
				w[4] = new WumpusNode(maze, posX-1, posY, numActions, action);
				w[4].setAction("moveup ");
			}
		}
		
		return w;
	}
	
	public WumpusNode[] genShots() {
		WumpusNode[] w = new WumpusNode[] {null, null, null, null, null};
		// NODES: pickup, down, left, up, right
		// DO NOT generate if tile has wumpus or pit
		
		if(maze[posX][posY].hasGlitter() && checkNode(maze[posX][posY])) {
			w[0] = new WumpusNode(maze, posX, posY, numActions, action);
			w[0].setAction("pickup ");
			w[0].setMazeString(w[0].goldString());
			w[0].maze[posX][posY].setGlitter(false);
			
			return w;
		}
		
		findWumpus();
		
		// Shoot Down node:
		if(shootDown(maze, posX, posY)) {
			w[1] = new WumpusNode(maze, posX, posY, numActions, action);
			w[1].setAction("shootdown ");
			w[1].maze[wumpusX][wumpusY].setWumpus(false);
		}
		
		// Shoot Left node:
		if(shootLeft(maze, posX, posY)) {
			w[2] = new WumpusNode(maze, posX, posY, numActions, action);
			w[2].setAction("shootleft ");
			w[2].maze[wumpusX][wumpusY].setWumpus(false);
		}
		
		// Shoot Up node:
		if(shootUp(maze, posX, posY)) {
			w[3] = new WumpusNode(maze, posX, posY, numActions, action);
			w[3].setAction("shootup ");
			w[3].maze[wumpusX][wumpusY].setWumpus(false);
		}
		
		// Shoot Right node:
		if(shootRight(maze, posX, posY)) {
			w[4] = new WumpusNode(maze, posX, posY, numActions, action);
			w[4].setAction("shootright ");
			w[4].maze[wumpusX][wumpusY].setWumpus(false);
		}
		
		return w;
	}
	
	boolean checkNode(GameTile tile) {
		if(tile == null) {
			return false;
		}
		
		if(!tile.isWall() && !tile.hasPit() && !tile.hasWumpus()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	boolean shootDown(GameTile[][] maze, int x, int y) {
		boolean wumpus = false;
		
		if(maze[x][y].hasPit() || maze[x][y].hasWumpus()) {
			return false;
		}
		
		for(int i=x; i<maze.length-1; i++) {
			if(maze[i][y].isWall()) {
				return false;
			}
			else if(maze[i][y].hasWumpus()) {
				wumpus = true;
			}
		}
		
		return wumpus;
	}
	
	boolean shootLeft(GameTile[][] maze, int x, int y) {
		boolean wumpus = false;
		
		if(maze[x][y].hasPit() || maze[x][y].hasWumpus()) {
			return false;
		}
		
		for(int i=y; i>0; i--) {
			if(maze[x][i].isWall()) {
				return false;
			}
			else if(maze[x][i].hasWumpus()) {
				wumpus = true;
			}
		}
		
		return wumpus;
	}

	boolean shootUp(GameTile[][] maze, int x, int y) {
		boolean wumpus = false;
		
		if(maze[x][y].hasPit() || maze[x][y].hasWumpus()) {
			return false;
		}
		
		for(int i=x; i>0; i--) {
			if(maze[i][y].isWall()) {
				return false;
			}
			else if(maze[i][y].hasWumpus()) {
				wumpus = true;
			}
		}
		
		return wumpus;
	}

	boolean shootRight(GameTile[][] maze, int x, int y) {
		boolean wumpus = false;
		
		if(maze[x][y].hasPit() || maze[x][y].hasWumpus()) {
			return false;
		}
		
		for(int i=y; i<maze[0].length-1; i++) {
			if(maze[x][i].isWall()) {
				return false;
			}
			else if(maze[x][i].hasWumpus()) {
				wumpus = true;
			}
		}
		
		return wumpus;
	}
	
	boolean victoryCheck() {
		if(posX == 4 && posY == 1) {
			victory = true;
			return true;
		}
		else {return false;}
	}
	
	int[] findWumpus() {
		if(wumpusX == -1 && wumpusY == -1) {
			for(int i=0; i<maze.length; i++) {
				for(int j=0; j<maze[0].length; j++) {
					if(maze[i][j].hasWumpus()) {
						wumpusX = i;
						wumpusY = j;
						return new int[] {i, j};
					}
				}
			}
			
			return null;
		}
		else {
			return new int[] {wumpusX, wumpusY};
		}
	}
	
	boolean wumpusAliveCheck() {
		int[] pos = findWumpus();
		
		if(pos != null) {
			if(maze[pos[0]][pos[1]].hasWumpus()) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	int[] findGold() {
		if(goldX == -1 && goldY == -1) {
			for(int i=0; i<maze.length; i++) {
				for(int j=0; j<maze[0].length; j++) {
					if(maze[i][j].hasGlitter()) {
						goldX = i;
						goldY = j;
						return new int[] {i, j};
					}
				}
			}
			
			return null;
		}
		else {
			return new int[] {goldX, goldY};
		}
	}
	
	boolean goldCheck() {
		int[] pos = findGold();
		
		if(pos != null) {
			if(maze[pos[0]][pos[1]].hasGlitter()) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	public static int[] findPlayer(GameTile[][] maze) {
		int [] pos = new int [2];
		
		for(int i=0; i<maze.length; i++) {
			for(int j=0; j<maze[0].length; j++) {
				if(maze[i][j] != null && maze[i][j].hasPlayer()) {
					pos[0] = i;
					pos[1] = j;
					return pos;
				}
			}
		}
		
		System.out.println("Player Location Not Found: Default to 0,0");
		return new int[] {0,0};
	}
	
	public static void reset() {
		victory = false;
		explored.clear();
	}
}