
#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"

uint16_t brzinaKretanja = 127;

MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);

MeUltrasonicSensor ultraSonic(3);

Me4Button button = Me4Button();

MeBluetooth bluetooth = MeBluetooth();

uint8_t modRadnje = -1;
bool stisnutGumb = true;

void setup() 
{

	button.setpin(A7);
	bluetooth.begin(9600);
}

void loop() 
{
	IzvrsiPritisakTipke();

	switch (modRadnje)
	{
	case 0:
		IzvrsiRadnjuBT(CitajBluetooth());
		break;
	case 1:
		ZaustaviMotore();
	default:
		ZaustaviMotore();
		break;
	}

}

void IzvrsiPritisakTipke()
{
	if (button.pressed() && stisnutGumb)
	{
		stisnutGumb = false;
		modRadnje++;

		if (modRadnje>1)
			modRadnje = 0;
	}
	if (!button.pressed() && !stisnutGumb)
	{
		stisnutGumb = true;
	}
}

char CitajBluetooth()
{
	//byte sEventBuffer[1024];
	//String myString;
	char porukica;
	if (bluetooth.available())
	{
		//myString = bluetooth.readString();
		porukica = bluetooth.read();
		//bluetooth.readBytes((char *)sEventBuffer, 1024);
		
	}
	//String myString = String((char*)sEventBuffer);
	return porukica;
}

void IzvrsiRadnjuBT(char btPoruka)
{
	if (btPoruka == 'A')
	{
		Kreni(brzinaKretanja);
		/*switch (DohvatiRadnjuIzPoruke(btPoruka))
		{
			case KreniNaprijed:
				Kreni(brzinaKretanja);
				break;
			case ZaustaviSe:
				ZaustaviMotore();
				break;
			case RotirajSe:
				Skreni('l', 90, brzinaKretanja);
				break;

			default:
				Skreni('l', 90, brzinaKretanja);
				break;
		}
		if (btPoruka == "RunMotors")
			Kreni(brzinaKretanja);
		else
			Skreni('d', 90, brzinaKretanja);

		PosaljiBTPoruku();*/
	}
	else if (btPoruka == 'B')
		Skreni('d', 90, brzinaKretanja);
}

void PosaljiBTPoruku()
{
	//String prednjiSenzor = DohvatiRadnjuIzEnuma(PrednjiUZSenzor);

	//bluetooth.sendString(prednjiSenzor + ultraSonic.distanceCm);
}

void Kreni(uint16_t brzinaKretanja)
{
	leftMotor.run(-brzinaKretanja);
	rightMotor.run(brzinaKretanja);
}

void ZaustaviMotore()
{
	leftMotor.stop();
	rightMotor.stop();
}

void Skreni(char smijer, uint16_t stupnjevi, uint16_t brzina)
{
	if (smijer == 'l')
	{
		leftMotor.run(brzina);
		rightMotor.run(brzina);
	}
	else 
	{
		leftMotor.run(-brzina);
		rightMotor.run(-brzina);
	}
	delay(IzracunajVrijemeRotacije(stupnjevi, brzina));
	ZaustaviMotore();
}

int IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	int vrijemeOkretanje = (int)((stupnjevi * 635) / (brzina));

	return vrijemeOkretanje;
}

/*String DohvatiRadnjuIzEnuma(InfoZaAndroid info)
{
	if (info == PrednjiUZSenzor)
		return "FUsS:";

	else if (info == DesniUZSenzor)
		return "RUsS:";

	else if (info == LijeviUZSenzor)
		return "LUsS:";

	else if (info == ZadnjiInfo)
		return "Over:";

	return "Over:";
}

NaredbaAndroida DohvatiRadnjuIzPoruke(String tekst)
{
	String radniTekst = tekst.substring(0, tekst.lastIndexOf(':'));

	if (radniTekst == "RunMotors")
		return KreniNaprijed;

	else if (radniTekst == "StopMotors")
		return ZaustaviSe;

	else if (radniTekst == "RotateFull")
		return RotirajSe;

	else if (radniTekst == "SpeedUpLeft")
		return UbrzajLijeviMotor;

	else if (radniTekst == "SpeedUpRight")
		return UbrzajDesniMotor;

	else if (radniTekst == "Over")
		return ZadnjaNaredba;

	return Nulla;
}

enum InfoZaAndroid {
	PrednjiUZSenzor,
	DesniUZSenzor,
	LijeviUZSenzor,
	OcitajSenzorCrte,
	ZadnjiInfo,
	Null
};

enum NaredbaAndroida {
	KreniNaprijed,
	ZaustaviSe,
	RotirajSe,
	UbrzajLijeviMotor,
	UbrzajDesniMotor,
	ZadnjaNaredba,
	Nulla
};*/
