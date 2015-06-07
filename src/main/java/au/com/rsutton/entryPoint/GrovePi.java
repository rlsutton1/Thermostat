package au.com.rsutton.entryPoint;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

public interface GrovePi
{

	float readDHT(int address, int i);

	void setMode(Pin gpioD7, PinMode digitalOutput);

	void setState(Pin gpioD8, PinState high);

}