
public class Node {
	private String name;	// node name
	private double lat;		// latitude coordinate (-90,90)
	private double lon;		// longitude coordinate (-180,180)

	//constructors
	public Node(){
		this.name = null;
		this.lat = 0.00;
		this.lon = 0.00;
	}
	public Node(String name, double lat, double lon){
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}
	
	// setters
	public void setName(String name) {
		this.name = name;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	// getters
	public String getName() {
		return this.name;
	}
	public double getLat() {
		return this.lat;
	}
	public double getLon() {
		return this.lon;
	}
	
	//Node methods
	public void userEdit() {
		// get user info and edit node
		System.out.printf("   Name: ");
		this.setName(BasicFunctions.getString());
		this.setLat(BasicFunctions.getDouble("   latitude: ", -90.0, 90.0));
		this.setLon(BasicFunctions.getDouble("   longitude: ", -180.0, 180.0));
		
	}
	public void print() {
		// print node info as a table row
		
		String coordinates = "(" + Double.toString(this.getLat()) + "," + Double.toString(this.getLon()) + ")";
		System.out.printf("%19s%19s\n" , this.getName(), coordinates);
	}
	public static double distance(Node i, Node j) {
		// calculate distance btw two nodes
		double a;
		double b;
		final double R = 6371;
		double xi = i.getLat();
		double xj = j.getLat();
		double yi = i.getLon();
		double yj = j.getLon();
		
		double dx = xj - xi;
		double dy = yj - yi;
		
		xi = Math.toRadians(xi);
		xj = Math.toRadians(xj);
		yi = Math.toRadians(yi);
		yj = Math.toRadians(yj);
		dx = Math.toRadians(dx);
		dy = Math.toRadians(dy);
		
		double distance = 0;
		
		a = Math.pow((Math.sin(dx/2.0)), 2) + Math.cos(xi) * Math.cos(xj) * Math.pow((Math.sin(dy/2.0)), 2);
		
		b = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		
		distance = R * b;
		
		return distance;
	}

}
