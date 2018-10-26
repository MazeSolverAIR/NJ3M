#ifndef _MOTORS_h
#define _MOTORS_h

#if defined(ARDUINO) && ARDUINO >= 100
	#include "arduino.h"
	#include "MeMCore.h"
#else
	#include "WProgram.h"
#endif


class Motors
{
public:
	Motors(/*uint8_t pinRight, uint8_t pinLeft*/);
	virtual ~Motors();
	void KreniNaprijed(uint16_t);
	void Zaustavi();
	void Skreni(char, uint16_t, uint16_t);

private:
	MeDCMotor rightMotor;
	MeDCMotor leftMotor;
	float IzracunajVrijemeRotacije(uint16_t, uint16_t);
};

#endif

