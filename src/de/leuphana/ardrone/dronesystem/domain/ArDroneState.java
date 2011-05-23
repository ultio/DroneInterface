package de.leuphana.ardrone.dronesystem.domain;

public class ArDroneState {

	public enum Mask {
		FLY(1 << 0), /*
					 * !< FLY MASK : (0) ardrone is landed, (1) ardrone is
					 * flying
					 */
		VIDEO(1 << 1), /*
						 * !< VIDEO MASK : (0) video disable, (1) video enable
						 */
		VISION_MASK(1 << 2), /*
							 * !< VISION MASK : (0) vision disable, (1) vision
							 * enable
							 */
		CONTROL_MASK(1 << 3), /*
							 * !< CONTROL ALGO : (0) euler angles control, (1)
							 * angular speed control
							 */
		ALTITUDE_MASK(1 << 4), /*
								 * !< ALTITUDE CONTROL ALGO : (0) altitude
								 * control inactive (1) altitude control active
								 */
		USER_FEEDBACK_START(1 << 5), /* !< USER feedback : Start button state */
		COMMAND_MASK(1 << 6), /*
							 * !< Control command ACK : (0) None, (1) one
							 * received
							 */
		FW_FILE_MASK(1 << 7), /* Firmware file is good (1) */
		FW_VER_MASK(1 << 8), /* Firmware update is newer (1) */
		// FW_UPD_MASK (1 << 9), /* Firmware update is ongoing (1) */
		NAVDATA_DEMO_MASK(1 << 10), /*
									 * !< Navdata demo : (0) All navdata, (1)
									 * only navdata demo
									 */
		NAVDATA_BOOTSTRAP(1 << 11), /*
									 * !< Navdata bootstrap : (0) options sent
									 * in all or demo mode, (1) no navdata
									 * options sent
									 */
		MOTORS_MASK(1 << 12), /* !< Motors status : (0) Ok, (1) Motors problem */
		COM_LOST_MASK(1 << 13), /*
								 * !< Communication Lost : (1) com problem, (0)
								 * Com is ok
								 */
		VBAT_LOW(1 << 15), /* !< VBat low : (1) too low, (0) Ok */
		USER_EL(1 << 16), /*
						 * !< User Emergency Landing : (1) User EL is ON, (0)
						 * User EL is OFF
						 */
		TIMER_ELAPSED(1 << 17), /*
								 * !< Timer elapsed : (1) elapsed, (0) not
								 * elapsed
								 */
		ANGLES_OUT_OF_RANGE(1 << 19), /* !< Angles : (0) Ok, (1) out of range */
		ULTRASOUND_MASK(1 << 21), /* !< Ultrasonic sensor : (0) Ok, (1) deaf */
		CUTOUT_MASK(1 << 22), /*
							 * !< Cutout system detection : (0) Not detected,
							 * (1) detected
							 */
		PIC_VERSION_MASK(1 << 23), /*
									 * !< PIC Version number OK : (0) a bad
									 * version number, (1) version number is OK
									 */
		ATCODEC_THREAD_ON(1 << 24), /*
									 * !< ATCodec thread ON : (0) thread OFF (1)
									 * thread ON
									 */
		NAVDATA_THREAD_ON(1 << 25), /*
									 * !< Navdata thread ON : (0) thread OFF (1)
									 * thread ON
									 */
		VIDEO_THREAD_ON(1 << 26), /*
								 * !< Video thread ON : (0) thread OFF (1)
								 * thread ON
								 */
		ACQ_THREAD_ON(1 << 27), /*
								 * !< Acquisition thread ON : (0) thread OFF (1)
								 * thread ON
								 */
		CTRL_WATCHDOG_MASK(1 << 28), /*
									 * !< CTRL watchdog : (1) delay in control
									 * execution (> 5ms), (0) control is well
									 * scheduled
									 */
		ADC_WATCHDOG_MASK(1 << 29), /*
									 * !< ADC Watchdog : (1) delay in uart2 dsr
									 * (> 5ms), (0) uart2 is good
									 */
		COM_WATCHDOG_MASK(1 << 30), /*
									 * !< Communication Watchdog : (1) com
									 * problem, (0) Com is ok
									 */
		EMERGENCY_MASK(1 << 31); /*
								 * !< Emergency landing : (0) no emergency, (1)
								 * emergency
								 */
		int bitmask;

		private Mask(int i) {
			bitmask = i;
		}

		public int get() {
			return bitmask;
		}

	}

	private int stateValue;

	public void update(int stateValue) {
		this.stateValue = stateValue;
	}

	public static boolean get(Mask m, int value) {
		return (value & m.get()) == 1;
	}

	public boolean get(Mask m) {
		return (stateValue & m.get()) == 1;
	}

}
