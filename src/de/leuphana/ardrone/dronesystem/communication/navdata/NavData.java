package de.leuphana.ardrone.dronesystem.communication.navdata;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import de.leuphana.ardrone.dronesystem.old.network.DataReceiver;

public class NavData extends Thread{

	
	private InetAddress inetAddress;
	DataReceiver receiver;

	public NavData(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
		// TODO Auto-generated constructor stub
		receiver = new DataReceiver(inetAddress);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			receiver.receive();
		}
//		super.run();
	}
	
	class NavDataParser
	{
		/* navigation bytes listings from
		 * http://code.google.com/p/ardroneme/source/checkout
		 * (cause missing in the ardrone documentation)
		 */
	    static final int NAV_STATE_OFFSET	=  4;
	    static final int NAV_BATTERY_OFFSET	= 24;
	    static final int NAV_PITCH_OFFSET	= 28;
	    static final int NAV_ROLL_OFFSET	= 32;
	    static final int NAV_YAW_OFFSET	= 36;
	    static final int NAV_ALTITUDE_OFFSET= 40;

	    static final int MYKONOS_TRIM_COMMAND_MASK   = 1 <<  7; /*!< Trim command ACK : (0) None, (1) one received */
	    static final int MYKONOS_TRIM_RUNNING_MASK   = 1 <<  8; /*!< Trim running : (0) none, (1) running */
	    static final int MYKONOS_TRIM_RESULT_MASK    = 1 <<  9; /*!< Trim result : (0) failed, (1) succeeded */
	    static final int MYKONOS_ANGLES_OUT_OF_RANGE = 1 << 19; /*!< Angles : (0) Ok, (1) out of range */
	    static final int MYKONOS_WIND_MASK           = 1 << 20; /*!< Wind : (0) Ok, (1) too much to fly */
	    static final int MYKONOS_ULTRASOUND_MASK     = 1 << 21; /*!< Ultrasonic sensor : (0) Ok, (1) deaf */
	    static final int MYKONOS_CUTOUT_MASK         = 1 << 22; /*!< Cutout system detection : (0) Not detected, (1) detected */
	    static final int MYKONOS_COM_WATCHDOG_MASK   = 1 << 30; /*!< Communication Watchdog : (1) com problem, (0) Com is ok */
	    static final int MYKONOS_EMERGENCY_MASK      = 1 << 31; /*!< Emergency landing : (0) no emergency, (1) emergency */

	    
	    
	    public void parse(byte[] bytes)
	    {
	    	ByteBuffer buffer = ByteBuffer.wrap(bytes);
	    	int header = buffer.getInt();
	    	if(header != 0x55667788)
	    	{
	    		return;
	    	}
	    	int state = buffer.getInt();
	    	int seqNumber = buffer.getInt();
	    	int visionFlag = buffer.getInt();
	    	
	    }
	    
	}
}
