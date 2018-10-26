#pragma once

#include "MeMCore.h"


class Motori
{
public:
	//Motori();
	Motori(uint8_t pinRight, uint8_t pinLeft);
	virtual ~Motori();
	void KreniNaprijed(uint16_t brzinaKretanja);
	void Zaustavi();
	void Skreni(char smijer, int stupnjevi, uint16_t motorSpeed);

private:
	MeDCMotor rightMotor;
	MeDCMotor leftMotor;
	float IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t motorSpeed);
};

