package qlearning;

public class Vector {
	private double x;
	private double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static Vector fromPolar(double theta, double magnitude) {
		double x = Math.cos(theta) * magnitude;
		double y = Math.sin(theta) * magnitude;
		return new Vector(x, y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector add(Vector location) {
		x += location.getX();
		y += location.getY();
		return this;
	}

	public Vector subtract(Vector location) {
		x -= location.getX();
		y -= location.getY();
		return this;
	}

	public Vector multiply(double scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}

	public Vector clone() {
		return new Vector(x, y);
	}

	public double getDistance(Vector vector) {
		return Math.sqrt(Math.pow(x - vector.getX(), 2) + Math.pow(y - vector.getY(), 2));
	}

	public double getDirectionTowards(Vector vector) {
		return Math.atan2(vector.getY() - y, vector.getX() - x);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
