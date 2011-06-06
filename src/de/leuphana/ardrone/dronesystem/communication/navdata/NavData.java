package de.leuphana.ardrone.dronesystem.communication.navdata;

/*
 * orientiert an:
 * 
 * Copyright 2010 Cliff L. Biffle.  All Rights Reserved.
 * Use of this source code is governed by a BSD-style license that can be found
 * in the LICENSE file.

 */

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.leuphana.ardrone.dronesystem.domain.ArDroneState;
import de.leuphana.ardrone.dronesystem.domain.ArDroneState.Mask;
import de.leuphana.ardrone.dronesystem.domain.util.Util;
import de.leuphana.ardrone.dronesystem.network.CommandSender;
import de.leuphana.ardrone.dronesystem.network.NavDataReceiver;
import de.leuphana.ardrone.dronesystem.old.CommandCounter;
import de.leuphana.ardrone.dronesystem.old.domain.Commands;

public class NavData extends Thread {

	// private final InetAddress inetAddress;
	NavDataReceiver receiver;
	// ArDroneState state;
	int oldSeqNumber;
	int seqNumber;
	private int stateValues;

	public int getStateValues() {
		return stateValues;
	}

	public void setStateValues(int stateValues) {
		this.stateValues = stateValues;
	}

	NavDataParser parser;
	private DemoNavData demoNavData;

	public NavData() {

		// this.inetAddress = inetAddress;
		// TODO Auto-generated constructor stub
		receiver = new NavDataReceiver();
		// state = new ArDroneState();
		parser = new NavDataParser();
		demoNavData = new DemoNavData();
		// init();
	}

	private void init() {
		System.out.println("init started");
		try {
			receiver.sendTrash();
			byte[] data = receiver.receive();
			parser.parse(data);
			// DroneConnect.instance
			// .sendCommand(String.format(Commands.CONF_NAVDATA_TRUE.getCommandString(),
			// CommandCounter.getCounter()));
			if (ArDroneState.isOne(Mask.NAVDATA_BOOTSTRAP, stateValues)) {
				System.out.println("Bootstrap active.");
				System.out.println("Changing to demo...");
				// sende was anderes
				CommandSender.INSTANCE
						.sendCommand("AT*CONFIG=\"general:navdata_demo\",\"TRUE\"");
				CommandSender.INSTANCE.sendCommand(String.format(
						Commands.CONF_NAVDATA_TRUE.getCommandString(),
						CommandCounter.getCounter()));
				data = receiver.receive();
				parser.parse(data);
				// DroneConnect.instance.sendCommand("AT*CTRL=0");
				// DroneConnect.instance.sendCommand(String.format(Commands.CTRL.getCommandString(),CommandCounter.getCounter()));
				// DroneConnect.instance.sendCommand(String.format(Commands.COMMAND_ACK.getCommandString(),CommandCounter.getCounter()));
				if (ArDroneState.isZero(Mask.COMMAND_MASK, stateValues)) {
					System.out.println("Command activated");
					System.out.println("Running in demo mode");
					CommandSender.INSTANCE.sendCommand("AT*CTRL=0");
					// DroneConnect.instance.sendCommand(String.format(Commands.CTRL.getCommandString(),CommandCounter.getCounter()));
					// DroneConnect.instance.sendCommand(String.format(Commands.COMMAND_ACK.getCommandString(),CommandCounter.getCounter()));
					// Util.sleepMillis(1)
					// this.start();
					return;
				}
				// DroneConnect.instance.sendCommand("AT*CTRL=0");
				// DroneConnect.instance.sendCommand(String.format(Commands.CTRL.getCommandString(),CommandCounter.getCounter()));

			}
			System.err.println("Unable to init NAVDATA");
			// demoNavData.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		Util.sleepMillis(2090);
		while (true) {
			byte[] bytes;
			try {
				bytes = receiver.receive();
				parser.parse(bytes);
				System.out.println(demoNavData);
				Util.sleepMillis(50);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("timeOut");
				// e.printStackTrace();
			}
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
			stateValues = buffer.getInt();
			System.out.println("STATE:" + stateValues);

			// 8-11
			seqNumber = buffer.getInt();
			System.out.println("seqNumber:" + seqNumber);
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
				int gesamt = (buffer.getShort() & 0xFFFF);
				int optionsSize = gesamt - 4;
				// Stefan hat gemeckert deshalb >0
				if (optionsSize > 0) {
					byte[] dst = new byte[optionsSize];
					buffer.get(dst, 0, optionsSize);
					ByteBuffer option = ByteBuffer.wrap(dst);
					option.order(ByteOrder.LITTLE_ENDIAN);
					buffer.position(buffer.position() + optionsSize);
					handleOption(tag, option);
				}
			}
			// int checksum last 64 bit

			// state.update(stateValues);

		}

		private void handleOption(int tag, ByteBuffer option) {
			try {
				demoNavData.ctrlState = option.getInt();
				System.out.println(stateValues);
				System.out.println(demoNavData.ctrlState);
				demoNavData.setBatteryLevel(option.getInt());
				demoNavData.pitch = option.getFloat();
				// demoNavData.rotate = option.getFloat();
				// demoNavData.yaw = option.getFloat() ;
				demoNavData.yaw = option.getFloat();
				demoNavData.rotate = option.getFloat();
				demoNavData.altitude = option.getInt();
				demoNavData.vx = option.getFloat();
				demoNavData.vy = option.getFloat();
				demoNavData.vz = option.getFloat();
				demoNavData.frameIndex = option.getInt();
			} catch (BufferUnderflowException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("Buffer underflow");
			}
			// switch (tag) {
			// case 0: // demo_nav_data
			// demoNavData.ctrlState = option.getInt();
			// demoNavData.batteryLevel = option.getInt();
			// demoNavData.pitch = option.getFloat();
			// demoNavData.rotate = option.getFloat();
			// demoNavData.yaw = option.getFloat();
			// demoNavData.altitude = option.getInt();
			// demoNavData.vx = option.getFloat();
			// demoNavData.vy = option.getFloat();
			// demoNavData.vz = option.getFloat();
			// demoNavData.frameIndex = option.getInt();
			// System.out.println(demoNavData);
			// break;
			//
			// default:
			// throw new IllegalArgumentException();
			// }

		}

	}

	public DemoNavData getDemoNavData() {
		return demoNavData;
	}

	public void setDemoNavData(DemoNavData demoNavData) {
		this.demoNavData = demoNavData;
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
