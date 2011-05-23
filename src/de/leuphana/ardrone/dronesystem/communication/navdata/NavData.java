package de.leuphana.ardrone.dronesystem.communication.navdata;

/*
 * orientiert an:
 * 
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved.
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.

 */

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.leuphana.ardrone.dronesystem.domain.util.Util;
import de.leuphana.ardrone.dronesystem.old.network.DataReceiver;

public class NavData extends Thread {

	private final InetAddress inetAddress;
	DataReceiver receiver;
	// ArDroneState state;
	int oldSeqNumber;
	int seqNumber;
	NavDataParser parser;
	DemoNavData demoNavData;

	public NavData(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
		// TODO Auto-generated constructor stub
		receiver = new DataReceiver(inetAddress);
		// state = new ArDroneState();
		parser = new NavDataParser();
		demoNavData = new DemoNavData();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			byte[] bytes = receiver.receive();
			parser.parse(bytes);
			Util.sleepMillis(300);
		}
		// super.run();
	}

	class NavDataParser {
		/*
		 * navigation bytes listings from
		 * http://code.google.com/p/ardroneme/source/checkout (cause missing in
		 * the ardrone documentation)
		 */
		// static final int NAV_STATE_OFFSET = 4;
		// static final int NAV_BATTERY_OFFSET = 24;
		// static final int NAV_PITCH_OFFSET = 28;
		// static final int NAV_ROLL_OFFSET = 32;
		// static final int NAV_YAW_OFFSET = 36;
		// static final int NAV_ALTITUDE_OFFSET = 40;

		public void parse(byte[] bytes) {
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			// 0-3
			int header = buffer.getInt();
			if (header != 0x55667788) {
				return;
			}
			// 4-7
			int stateValues = buffer.getInt();
			// 8-11
			seqNumber = buffer.getInt();
			// 12-15
			int visionFlag = buffer.getInt();
			while (buffer.position() < buffer.limit() - 64) {
				// get the tag. length 16 bit. bitmask to ensure correct
				// typeconversion from short to int
				int tag = buffer.getShort() & 0xFFFF;
				// get the blocksize (complete size including tag and size).
				// length: 16 bit)
				// size is number of bytes. optionSize is therefore 4 bytes
				// smaller
				int optionsSize = (buffer.getShort() & 0xFFFF) - 4;
				byte[] dst = new byte[optionsSize];
				buffer.get(dst, 0, optionsSize);
				ByteBuffer option = ByteBuffer.wrap(dst);
				option.order(ByteOrder.LITTLE_ENDIAN);
				buffer.position(buffer.position() + optionsSize);
				handleOption(tag, option);
			}
			// int checksum last 64 bit

			// int battery = buffer.getInt(NAV_BATTERY_OFFSET);
			// int pitchOffset = buffer.getInt(NAV_PITCH_OFFSET);
			// int rollOffset = buffer.getInt(NAV_ROLL_OFFSET);
			// int yawOffset = buffer.getInt(NAV_YAW_OFFSET);
			// int altitude = buffer.getInt(NAV_ALTITUDE_OFFSET);
			// // getOptions
			// state.update(stateValues);

		}

		private void handleOption(int tag, ByteBuffer option) {
			switch (tag) {
			case 0: // demo_nav_data
				demoNavData.ctrlState = option.getInt();
				demoNavData.batteryLevel = option.getInt();
				demoNavData.pitch = option.getFloat();
				demoNavData.rotate = option.getFloat();
				demoNavData.yaw = option.getFloat();
				demoNavData.altitude = option.getInt();
				demoNavData.vx = option.getFloat();
				demoNavData.vy = option.getFloat();
				demoNavData.vz = option.getFloat();
				demoNavData.frameIndex = option.getInt();
				System.out.println(demoNavData);
				break;

			default:
				throw new IllegalArgumentException();
			}

		}

	}

	static final int MYKONOS_TRIM_COMMAND_MASK = 1 << 7; /*
														 * !< Trim command ACK :
														 * (0) None, (1) one
														 * received
														 */
	static final int MYKONOS_TRIM_RUNNING_MASK = 1 << 8; /*
														 * !< Trim running : (0)
														 * none, (1) running
														 */
	static final int MYKONOS_TRIM_RESULT_MASK = 1 << 9; /*
														 * !< Trim result : (0)
														 * failed, (1) succeeded
														 */
	static final int MYKONOS_ANGLES_OUT_OF_RANGE = 1 << 19; /*
															 * !< Angles : (0)
															 * Ok, (1) out of
															 * range
															 */
	static final int MYKONOS_WIND_MASK = 1 << 20; /*
												 * !< Wind : (0) Ok, (1) too
												 * much to fly
												 */
	static final int MYKONOS_ULTRASOUND_MASK = 1 << 21; /*
														 * !< Ultrasonic sensor
														 * : (0) Ok, (1) deaf
														 */
	static final int MYKONOS_CUTOUT_MASK = 1 << 22; /*
													 * !< Cutout system
													 * detection : (0) Not
													 * detected, (1) detected
													 */
	static final int MYKONOS_COM_WATCHDOG_MASK = 1 << 30; /*
														 * !< Communication
														 * Watchdog : (1) com
														 * problem, (0) Com is
														 * ok
														 */
	static final int MYKONOS_EMERGENCY_MASK = 1 << 31; /*
														 * !< Emergency landing
														 * : (0) no emergency,
														 * (1) emergency
														 */
}
