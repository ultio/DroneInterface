package de.leuphana.ardrone.dronesystem.communication.navdata;

public class DemoNavData {
	public int ctrlState;
	/**
	 * vbat_flying_percentage
	 */
	public int batteryLevel;
	/**
	 * theta; forward-backward in milli degrees
	 */
	public float pitch;
	public float rotate;// phi; //(roll) in milli degrees
	public float yaw;// psi; //tilt Left Right in milli degrees
	/**
	 * altitude in centimeters
	 */
	public int altitude;
	// VORSICHT Richtung beachten
	/**
	 * estimated linear velocity in x direction (forward/backward)
	 */
	public float vx;
	/**
	 * estimated linear velocity in y direction (up/Down)
	 */
	public float vy;
	/**
	 * estimated linear velocity in z direction (tilt-Left/Right)
	 */
	public float vz;
	// .........
	// wievielter Frame
	public int frameIndex;

	@Override
	public String toString() {
		return "DemoNavData [ctrlState=" + ctrlState + ", batteryLevel="
				+ batteryLevel + ", pitch=" + pitch + ", rotate=" + rotate
				+ ", yaw=" + yaw + ", altitude=" + altitude + ", vx=" + vx
				+ ", vy=" + vy + ", vz=" + vz + ", frameIndex=" + frameIndex
				+ "]";
	}

	// deprecated stuff

	// komische matrizen für videoanalyse...

}
