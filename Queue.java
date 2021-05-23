public class Queue {
	// QUEUE: We want to ADD nodes to the BACK, and REMOVE from the FRONT
	
	static int exploredN;
	WumpusNode front;
	
	public Queue() {
		front = null;
	}
	
	public Queue(WumpusNode node) {
		front = node;
		front.setNext(null);
	}
	
	public void enq(WumpusNode node) {
		if(front != null) {
			WumpusNode frontTemp = front;
			while(frontTemp.getNext() != null) {
				frontTemp = frontTemp.getNext();
			}
			frontTemp.setNext(node);
		}
		else {
			front = node;
		}
	}
	
	public WumpusNode deq() {
		if(front != null) {
			WumpusNode frontTemp = front;
			front = front.getNext();
			exploredN++;
			return frontTemp;
		}
		else {
			return front;
		}
	}
}