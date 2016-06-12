package au.com.rsutton.entryPoint;

import java.util.Random;

import au.com.rsutton.entryPoint.GrovePiProvider.DHTType;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

public class GrovePiSimulator implements GrovePi

{

	@Override
	public float readDHT(int address, DHTType i)
	{
		return new Random().nextInt(14) + 14;
	}

	@Override
	public void setMode(Pin gpioD7, PinMode digitalOutput)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setState(Pin gpioD8, PinState high)
	{
		System.out.println("Set " + gpioD8 + " " + high);

	}

}
