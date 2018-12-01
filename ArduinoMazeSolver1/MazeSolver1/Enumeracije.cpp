#include "Enumeracije.h"



Enumeracije::Enumeracije()
{

}

Enumeracije::NaredbaAndroida Enumeracije::DohvatiRadnjuIzPoruke(String tekst)
{
	String radniTekst = tekst.substring(0, tekst.lastIndexOf(':'));

	if (radniTekst == "RunMotors")
		return Enumeracije::KreniNaprijed;

	else if (radniTekst == "StopMotors")
		return Enumeracije::ZaustaviSe;

	else if (radniTekst == "RotateFull")
		return Enumeracije::RotirajSe;

	else if (radniTekst == "SpeedUpLeft")
		return Enumeracije::UbrzajLijeviMotor;

	else if (radniTekst == "SpeedUpRight")
		return Enumeracije::UbrzajDesniMotor;

	else if (radniTekst == "Over")
		return Enumeracije::ZadnjaPoruka;

	return Enumeracije::Null;
}

String Enumeracije::DohvatiRadnjuIzEnuma(InfoZaAndroid info)
{
	if (info == PrednjiUZSenzor)
		return "FUsS:";

	else if (info == DesniUZSenzor)
		return "RUsS:";

	else if (info == LijeviUZSenzor)
		return "LUsS:";

	else if (info == ZadnjaPoruka)
		return "Over:";

	return "Over:";
}


