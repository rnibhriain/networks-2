public class RoutingKey {

	int hops;
	String nextDst;

	// Initialising the Routing key
	public RoutingKey(int hopCount, String nextDest) {

		this.hops = hopCount;
		this.nextDst = nextDest;

	}

	// setting the next destination 
	public void setNextHop(String nextHop) {

		this.nextDst = nextHop;

	}
}