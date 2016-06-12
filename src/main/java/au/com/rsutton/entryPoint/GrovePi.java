package au.com.rsutton.entryPoint;

import au.com.rsutton.entryPoint.GrovePiProvider.DHTType;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;

public interface GrovePi
{

	void setMode(Pin gpioD7, PinMode digitalOutput);

	void setState(Pin gpioD8, PinState high);

	float readDHT(int sensorPort, DHTType moduleType);

}