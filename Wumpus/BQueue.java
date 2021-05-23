public class BQueue {
	// QUEUE: We want to ADD nodes to the BACK, and REMOVE from the FRONT
	
	static int exploredN;
	BlindNode front;
	
	public BQueue() {
		front = null;
	}
	
	public BQueue(BlindNode node) {
		front = node;
		front.setNext(null);
	}
	
	public void enq(BlindNode node) {
		if(front != null) {
			BlindNode frontTemp = front;
			while(frontTemp.getNext() != null) {
				frontTemp = frontTemp.getNext();
			}
			frontTemp.setNext(node);
		}
		else {
			front = node;
		}
	}
	
	public BlindNode deq() {
		if(front != null) {
			BlindNode frontTemp = front;
			front = front.getNext();
			exploredN++;
			return frontTemp;
		}
		else {
			return front;
		}
	}
}