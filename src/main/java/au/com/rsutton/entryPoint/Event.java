package au.com.rsutton.entryPoint;

class Event implements Comparable<Event> {
	long time;
	int temp;

	@Override
	public int compareTo(Event o) {
		return (int) (time / 1000 - o.time / 1000);
	}
}
