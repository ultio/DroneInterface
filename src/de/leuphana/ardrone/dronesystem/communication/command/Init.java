package de.leuphana.ardrone.dronesystem.communication.command;

import static de.leuphana.ardrone.dronesystem.domain.CmdValue.COMMAND_ACK;

import java.util.concurrent.ConcurrentLinkedQueue;

import de.leuphana.ardrone.dronesystem.domain.Command;
import de.leuphana.ardrone.dronesystem.domain.util.Counter;
import de.leuphana.ardrone.dronesystem.network.CommandSender;
import de.leuphana.ardrone.dronesystem.old.Util;

/**
 * Given a list of initialization commands, sends all commands to the drone and
 * then initializes the Keep-Alive and Commander routines. TODO: current
 * implementation does not check whether the commands are received/accepted.
 * Implement a check whether navdata received reflects init commands sent.
 * 
 * @see {@link KeepAlive}, {@link Commander}
 * @author Florian, Moritz
 * 
 */
public class Init {

	private Init() {
	}

	// public static void exec(final CommandSender sender,
	// final ConcurrentLinkedQueue<Command> initCommands) {
	// Runnable r = new Runnable() {
	//
	// @Override
	// public void run() {
	// while (!initCommands.isEmpty()) {
	// sender.sendCommand(initCommands.poll()
	// .getMessageWithCounter());
	// Util.sleepMillis(20);
	// //check navdata here
	// sender.sendCommand(COMMAND_ACK.with(Counter.get()));
	// Util.sleepMillis(20);
	// }
	//
	// }
	// };
	// Thread t = new Thread(r);
	// t.start();
	// }
	public static void asyncExec(final CommandSender sender,
			final ConcurrentLinkedQueue<Command> initCommands) {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				while (!initCommands.isEmpty()) {
					sender.sendCommand(initCommands.poll()
							.getMessageWithCounter());
					Util.sleepMillis(20);
					sender.sendCommand(COMMAND_ACK.with(Counter.get()));
					Util.sleepMillis(20);
				}
				KeepAlive.start();
				Commander.start();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
}
