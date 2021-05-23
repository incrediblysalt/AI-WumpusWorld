public class WumpusWorld {
	
	static String[][] strMaze;
	static String nextAct = "";
	static int numActions = 0;
	static boolean hasGold = true;
	
	public static String BreadthFirst(GameTile[][] visibleMap) {
		
		GameTile[][] maze = new GameTile[visibleMap.length][visibleMap[0].length];
		
		for(int i=0; i<visibleMap.length; i++) {
			for(int j=0; j<visibleMap[0].length; j++) {
				maze[i][j] = visibleMap[i][j];
			}
		}
		
		// And now to get the player's location
		int pos[] = WumpusNode.findPlayer(maze);
		
		// And with the position found, let's create the initial node!
		WumpusNode init = new WumpusNode(maze, pos[0], pos[1], -1, "");
		
		Queue queue = new Queue(init);
		
		WumpusNode current = init;
		
		boolean unsolvable = false;
		
		while(WumpusNode.getVict() == false) {
			
			while(current.wumpusAliveCheck()) {
				current = queue.deq();
				
				if(current == null) {
					System.out.println("Impossible Condition.");
					unsolvable = true;
				}
				
				if(unsolvable) {
					WumpusNode.setVict(true);
					break;
				}
				
				if(!WumpusNode.inHash(current.toString())) {
					WumpusNode.addToHash(current.toString());
					
					WumpusNode[] childs = current.genShots();
					
					for(int i=0; i<childs.length; i++) {
						if(childs[i] != null) {
							queue.enq(childs[i]);
						}
					}
					
					childs = current.genChildren();
					
					for(int i=0; i<childs.length; i++) {
						if(childs[i] != null) {
							queue.enq(childs[i]);
						}
					}
				}
				
				if(!current.wumpusAliveCheck()) {
					queue = new Queue(current);
					WumpusNode.reset();
					String[] actions = current.getAction().split(" ");
					return actions[0];
				}
			}
			
			while(current.goldCheck()) {
				current = queue.deq();
				
				if(current == null) {
					System.out.println("Impossible Condition.");
					unsolvable = true;
				}
				
				if(unsolvable) {
					WumpusNode.setVict(true);
					break;
				}
				
				if(!WumpusNode.inHash(current.toString())) {
					WumpusNode.addToHash(current.toString());
					
					WumpusNode[] childs = current.genChildren();
					
					for(int i=0; i<childs.length; i++) {
						if(childs[i] != null) {
							queue.enq(childs[i]);
						}
					}
				}
				
				if(!current.goldCheck()) {
					queue = new Queue(current);
					WumpusNode.reset();
					String[] actions = current.getAction().split(" ");
					return actions[0];
				}
			}
			
			current = queue.deq();
			
			if(current == null) {
				System.out.println("Impossible Condition.");
				unsolvable = true;
			}
			
			if(unsolvable) {
				WumpusNode.setVict(true);
				break;
			}
			
			// If the current node is not null, we will continue the search
			if(!WumpusNode.inHash(current.toString())) {
				WumpusNode.addToHash(current.toString());
				
				WumpusNode[] childs = current.genChildren();
				
				for(int i=0; i<childs.length; i++) {
					if(childs[i] != null) {
						queue.enq(childs[i]);
					}
				}
			}
			
			if(current.victoryCheck()) {
				break;
			}
		}
		
		// Be sure to reset variables for when the search ends
		WumpusNode.reset();
		
		// And if an unsolvable maze is found, set action to victory
		if(unsolvable) {
			return "victory";
		}
		
		// Otherwise, find the first action from our current node
		String[] actions = current.getAction().split(" ");
		return actions[0];
	}
	
	public static String blindSearch(GameTile[][] visibleMap) {
		
		// If we make it here, gold is still true.
		// Let's start by making a copy of the map
		
		GameTile[][] maze = new GameTile[visibleMap.length][visibleMap[0].length];
		
		for(int i=0; i<visibleMap.length; i++) {
			for(int j=0; j<visibleMap[0].length; j++) {
				maze[i][j] = visibleMap[i][j];
			}
		}
		
		// Now if our player is at the start of a map: x == 4, y == 1, and
		// the square above and to the right are null, we need to reset
		// our static variables
		if(maze[4][1].hasPlayer() && maze[3][1] == null && maze[4][2] == null) {
			strMaze = new String[maze.length][maze[0].length];
			// Although it's been set to new, we want to give it a value still
			// Let's use "*" to represent unknown
			for(int i=0; i<maze.length; i++) {
				for(int j=0; j<maze[0].length; j++) {
					strMaze[i][j] = "*";
				}
			}
			
			// With our string maze cleared, let's be sure to reset the
			// numActions and hasGold
			hasGold = true;
			numActions = 0;
			nextAct = "";
			
			// Since we know the player is at the beginning, let's do
			// a check and see if they're standing on a stench or a breeze,
			// and just make them leave
			if(maze[4][1].hasBreeze() || maze[4][1].hasStench()) {
				return "victory";
			}
		}
		
		// If gold has been set false, we want to go to escape immediately.
		if(!hasGold) {
			return WumpusWorld.escape(visibleMap);
		}
		
		// Now we can actually work on moving our player
		// First up: let's check and see if player is on gold
		// If so, pick it up and set hasGold to false
		int posX = -1;
		int posY = -1;
		
		for(int i=0; i<maze.length; i++) {
			for(int j=0; j<maze[0].length; j++) {
				if(maze[i][j] != null && maze[i][j].hasPlayer()) {
					posX = i;
					posY = j;
					break;
				}
			}
		}
		
		if(posX == -1 && posY == -1) {
			System.out.println("Uhhhh this isn't right.");
			return "victory";
		}
		
		if(maze[posX][posY].hasGlitter()) {
			hasGold = false;
			numActions+=1;
			return "pickup";
		}
		
		// Now, we get here if we are NOT on gold.
		// First off, we want to see if we're on a stench or a breeze
		if(maze[posX][posY].hasStench() || maze[posX][posY].hasBreeze()) {
			// SO; you're on a breeze or a stench
			// Let's mark suspicious places on our strMaze so we don't go there.
			// However, if the square is not null, we've already been there
			// and it is safe
			
			// Check down: x + 1
			if(maze[posX+1][posY] == null) {
				strMaze[posX+1][posY] = "X";
			}
			// Check left: y - 1
			if(maze[posX][posY-1] == null) {
				strMaze[posX][posY-1] = "X";
			}
			// Check right: y + 1
			if(maze[posX][posY+1] == null) {
				strMaze[posX][posY+1] = "X";
			}
			// Check up: x - 1
			if(maze[posX-1][posY] == null) {
				strMaze[posX-1][posY] = "X";
			}
		}
		
		// Now that we've marked potentially unsafe spots, now the fun part:
		// moving!
		// We want to move if it is NOT explored and NOT unsafe "X"
		
		if(nextAct.equals("")) {
			// Move up: x - 1
			if(maze[posX-1][posY] == null && !strMaze[posX-1][posY].equals("X")) {
				nextAct = "moveup";
			}
			// Move right: y + 1
			else if(maze[posX][posY+1] == null && !strMaze[posX][posY+1].equals("X")) {
				nextAct = "moveright";
			}
			// Move left: y - 1
			else if(maze[posX][posY-1] == null && !strMaze[posX][posY-1].equals("X")) {
				nextAct = "moveleft";
			}
			// Move down: x + 1
			else if(maze[posX+1][posY] == null && !strMaze[posX+1][posY].equals("X")) {
				nextAct = "movedown";
			}
		}
		
		// But what if we don't set nextAct to anything, meaning that the
		// spots around us may not be null or may be suspicious?
		if(nextAct.equals("")) {
			// Move up: x - 1
			if(maze[posX-1][posY] != null && !maze[posX-1][posY].isWall()) {
				nextAct = "moveup";
			}
			// Move right: y + 1
			else if(maze[posX][posY+1] != null && !maze[posX][posY+1].isWall()) {
				nextAct = "moveright";
			}
			// Move left: y - 1
			else if(maze[posX][posY-1] != null && !maze[posX][posY-1].isWall()) {
				nextAct = "moveleft";
			}
			// Move down: x + 1
			else if(maze[posX+1][posY] != null && !maze[posX+1][posY].isWall()) {
				nextAct = "movedown";
			}
		}
		
		// However, we don't want to perform too many actions since we can
		// travel on squares we've already been to. Let's make a check
		if(numActions >= 20 || nextAct.equals("")) {
			hasGold = false;
		}
		
		// If we get here, we should return our next action and set the
		// static variable to empty
		String temp = nextAct;
		nextAct = "";
		
		numActions+=1;
		return temp;
	}
	
	public static String escape(GameTile[][] visibleMap) {
		GameTile[][] maze = new GameTile[visibleMap.length][visibleMap[0].length];
		
		for(int i=0; i<visibleMap.length; i++) {
			for(int j=0; j<visibleMap[0].length; j++) {
				if(visibleMap[i][j] == null) {
					maze[i][j] = null;
				}
				else {
					maze[i][j] = visibleMap[i][j];
				}
			}
		}
		
		// And now to get the player's location
		int pos[] = WumpusNode.findPlayer(maze);
		
		// And with the position found, let's create the initial node!
		WumpusNode init = new WumpusNode(maze, pos[0], pos[1], numActions, "");
		
		Queue queue = new Queue(init);
		
		WumpusNode current = init;
		
		boolean unsolvable = false;
		
		while(WumpusNode.getVict() == false) {
			
			current = queue.deq();
			
			if(current == null) {
				System.out.println("Impossible Condition.");
				unsolvable = true;
			}
			
			if(unsolvable) {
				WumpusNode.setVict(true);
				break;
			}
			
			// If the current node is not null, we will continue the search
			if(!WumpusNode.inHash(current.toString())) {
				WumpusNode.addToHash(current.toString());
				
				WumpusNode[] childs = current.genChildren();
				
				for(int i=0; i<childs.length; i++) {
					if(childs[i] != null) {
						queue.enq(childs[i]);
					}
				}
			}
			
			if(current.victoryCheck()) {
				break;
			}
		}
		
		// Be sure to reset variables for when the search ends
		WumpusNode.reset();
		
		// And if an unsolvable maze is found, set action to victory
		if(unsolvable) {
			return "victory";
		}
		
		// Otherwise, find the first action from our current node
		String[] actions = current.getAction().split(" ");
		return actions[0];
	}
}