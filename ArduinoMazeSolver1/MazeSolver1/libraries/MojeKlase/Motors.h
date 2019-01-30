#pragma once
#include "Arduino.h"
#include "MeMCore.h"

class Motors
{
public:
	Motors();
	void KreniNaprijed(uint16_t);
	void Zaustavi();
	void Skreni(char, uint16_t, uint16_t);

private:
	float IzracunajVrijemeRotacije(uint16_t, uint16_t);
	MeDCMotor rightMotor;
	MeDCMotor leftMotor;
};
