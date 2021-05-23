public class AgentBrain {
	
	//Don't delete this variable
	private AgentAction nextMove;

	//We reload the brain each time, so this variable needs to be static
	private static int numGamesPlayed = 0;
	private static int loop = 0;
	private static boolean keyboardPlayOnly = false;

	@SuppressWarnings("unused")
	private int currentNumMoves;
	
	public AgentBrain() {
		nextMove = null;

		numGamesPlayed++;

		currentNumMoves = 0;
	}

	public void setNextMove(AgentAction m) {
		if(nextMove != null) {
			System.out.println("Trouble adding move, only allowed to add 1 at a time");
		}
		else {
			nextMove = m;
		}
	}
	//For wumpus world, we do one move at a time
	public AgentAction getNextMove(GameTile [][] visibleMap) {		
		//Possible things to add to your moves
//		nextMove = AgentAction.doNothing;
//		nextMove = AgentAction.moveDown;
//		nextMove = AgentAction.moveUp;
//		nextMove = AgentAction.moveUp;
//		nextMove = AgentAction.moveLeft;
//		nextMove = AgentAction.pickupSomething;
//		nextMove = AgentAction.declareVictory;
//
//		nextMove = AgentAction.shootArrowNorth; NORTH = UP
//		nextMove = AgentAction.shootArrowSouth; SOUTH = DOWN
//		nextMove = AgentAction.shootArrowEast; EAST = RIGHT
//		nextMove = AgentAction.shootArrowWest; WEST = LEFT
//		nextMove = AgentAction.quit;


		//Ideally you would remove all this code, but I left it in so the keylistener would work
		
		if(keyboardPlayOnly) {
			
			BlindNode test = new BlindNode(visibleMap, 4, 1, 1, "");
			
			System.out.println(test.toString());
			
			if(nextMove == null) {
				return AgentAction.doNothing;
			}
			else {
				AgentAction tmp = nextMove;
				nextMove = null;
				return tmp;
			}

		}
		
		// If keyboard play is false (it should be), it will run the code below
		
		if(numGamesPlayed == 1 && loop <=25) {
			loop++;
			return AgentAction.doNothing;
		}
		
		String action = WumpusWorld.blindSearch(visibleMap);
		
		System.out.println("ACTION: " + action);
		
		if(action.equals("pickup")) {
			return AgentAction.pickupSomething;
		}
		else if(action.equals("moveright")) {
			return AgentAction.moveRight;
		}
		else if(action.equals("movedown")) {
			return AgentAction.moveDown;
		}
		else if(action.equals("moveleft")) {
			return AgentAction.moveLeft;
		}
		else if(action.equals("moveup")) {
			return AgentAction.moveUp;
		}
		else if(action.equals("victory")) {
			return AgentAction.declareVictory;
		}
		else if(action.equals("shootup")) {
			return AgentAction.shootArrowNorth;
		}
		else if(action.equals("shootright")) {
			return AgentAction.shootArrowEast;
		}
		else if(action.equals("shootdown")) {
			return AgentAction.shootArrowSouth;
		}
		else if(action.equals("shootleft")) {
			return AgentAction.shootArrowWest;
		}
		
		if(numGamesPlayed >= 20) {
			printMaze(visibleMap);
			return AgentAction.quit;
		}
		else {
			System.out.println("GAME: " + numGamesPlayed);
			printMaze(visibleMap);
			return AgentAction.declareVictory;
		}
		
	}
	
	public void printMaze(GameTile[][] maze) {
		for(int i=0; i<maze.length; i++) {
			for(int j=0; j<maze[0].length; j++) {
				if(maze[i][j] == null) {
					System.out.print("null");
				}
				else {
					System.out.print(maze[i][j].toString());
				}
			}
			System.out.println();
		}
	}
}
