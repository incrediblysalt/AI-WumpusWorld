Welcome to Wumpus World, a project exploring AI in a dungeon crawler inspired minigame. Over the course of the AI project, it was up to us to add in the functionality the Agent would need in order to complete the game.
All the art and functionality of the screen were provided by the professor, while I worked on the Agent.

First up is the AgentBrain file, which is where each move calculated by the agent can be then reflected onto the screen.

WumpusWorld is where the agent attempts to calculate its next moves on the map, which is especially important when traversing the blind map where each tile is dark until explored.

The WumpusNode and BlindNode files, along with Queue and BQueue, go hand in hand; both are used in determining the next tile to explore, however the BlindNode and BQueue files were built for the final challenge of exploring in the dark.

For a more in depth look at each iteration of the project, check out the Presentations folder to find a video exploring each step of the process!
