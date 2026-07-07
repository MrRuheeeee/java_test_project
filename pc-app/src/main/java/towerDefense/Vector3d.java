package towerDefense;

import java.io.Serializable;

public class Vector3d implements Serializable{
	
	private static final long serialVersionUID = -1712403548469412573L;
	
	public double x;
	public double y;
	public double z;

	public final static Vector3d X = new Vector3d(1, 0, 0);
	public final static Vector3d Y = new Vector3d(0, 1, 0);
	public final static Vector3d Z = new Vector3d(0, 0, 1);
	public final static Vector3d Zero = new Vector3d(0, 0, 0);

	public Vector3d () {
	}

	public Vector3d (double x, double y, double z) {
		this.set(x, y, z);
	}

	public Vector3d (final Vector3d vector) {
		this.set(vector);
	}

	public Vector3d (final double[] values) {
		this.set(values[0], values[1], values[2]);
	}
	
	public Vector3d set (double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3d set (final Vector3d vector) {
		return this.set(vector.x, vector.y, vector.z);
	}

	public Vector3d cpy () {
		return new Vector3d(this);
	}

	public Vector3d add (final Vector3d vector) {
		return this.add(vector.x, vector.y, vector.z);
	}
	
	public Vector3d add (double x, double y, double z) {
		return this.set(this.x + x, this.y + y, this.z + z);
	}

	public Vector3d add (double values) {
		return this.set(this.x + values, this.y + values, this.z + values);
	}

	public Vector3d sub (final Vector3d a_vec) {
		return this.sub(a_vec.x, a_vec.y, a_vec.z);
	}

	public Vector3d sub (double x, double y, double z) {
		return this.set(this.x - x, this.y - y, this.z - z);
	}

	public Vector3d sub (double value) {
		return this.set(this.x - value, this.y - value, this.z - value);
	}

	public Vector3d scl (double scalar) {
		return this.set(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	public Vector3d scl (final Vector3d other) {
		return this.set(x * other.x, y * other.y, z * other.z);
	}

	public Vector3d scl (double vx, double vy, double vz) {
		return this.set(this.x * vx, this.y * vy, this.z * vz);
	}

	public Vector3d mulAdd (Vector3d vec, double scalar) {
		this.x += vec.x * scalar;
		this.y += vec.y * scalar;
		this.z += vec.z * scalar;
		return this;
	}

	public Vector3d mulAdd (Vector3d vec, Vector3d mulVec) {
		this.x += vec.x * mulVec.x;
		this.y += vec.y * mulVec.y;
		this.z += vec.z * mulVec.z;
		return this;
	}

	public static double len (final double x, final double y, final double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double len () {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public static double len2 (final double x, final double y, final double z) {
		return x * x + y * y + z * z;
	}

	public double len2 () {
		return x * x + y * y + z * z;
	}

	public boolean idt (final Vector3d vector) {
		return x == vector.x && y == vector.y && z == vector.z;
	}

	public static double dst (final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
		final double a = x2 - x1;
		final double b = y2 - y1;
		final double c = z2 - z1;
		return (double)Math.sqrt(a * a + b * b + c * c);
	}

	public double dst (Vector3d vector) {
		final double a = vector.x - x;
		final double b = vector.y - y;
		final double c = vector.z - z;
		return (double)Math.sqrt(a * a + b * b + c * c);
	}

	public double dst (double x, double y, double z) {
		final double a = x - this.x;
		final double b = y - this.y;
		final double c = z - this.z;
		return (double)Math.sqrt(a * a + b * b + c * c);
	}

	public static double dst2 (final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
		final double a = x2 - x1;
		final double b = y2 - y1;
		final double c = z2 - z1;
		return a * a + b * b + c * c;
	}

	public double dst2 (Vector3d point) {
		final double a = point.x - x;
		final double b = point.y - y;
		final double c = point.z - z;
		return a * a + b * b + c * c;
	}

	public double dst2 (double x, double y, double z) {
		final double a = x - this.x;
		final double b = y - this.y;
		final double c = z - this.z;
		return a * a + b * b + c * c;
	}

	public Vector3d nor () {
		final double len2 = this.len2();
		if (len2 == 0f || len2 == 1f) return this;
		return this.scl(1f / (double)Math.sqrt(len2));
	}

	public static double dot (double x1, double y1, double z1, double x2, double y2, double z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}

	public double dot (final Vector3d vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public double dot (double x, double y, double z) {
		return this.x * x + this.y * y + this.z * z;
	}

	public Vector3d crs (final Vector3d vector) {
		return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}

	public Vector3d crs (double x, double y, double z) {
		return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
	}
	
	public String toString () {
		return "(" + x + "," + y + "," + z + ")";
	}

	public Vector3d limit (double limit) {
		return limit2(limit * limit);
	}

	public Vector3d limit2 (double limit2) {
		double len2 = len2();
		if (len2 > limit2) {
			scl((double)Math.sqrt(limit2 / len2));
		}
		return this;
	}

	public Vector3d setLength (double len) {
		return setLength2(len * len);
	}

	public Vector3d setLength2 (double len2) {
		double oldLen2 = len2();
		return (oldLen2 == 0 || oldLen2 == len2) ? this : scl((double)Math.sqrt(len2 / oldLen2));
	}

	public Vector3d setZero () {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		return this;
	}
}