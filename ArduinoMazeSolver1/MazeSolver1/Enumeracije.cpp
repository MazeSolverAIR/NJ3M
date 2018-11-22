#include "Enumeracije.h"



Enumeracije::Enumeracije()
{

}

Enumeracije::NaredbaAndroida Enumeracije::DohvatiRadnjuIzStringa(String tekst)
{
	if (tekst == "Kreni naprijed")
		return Enumeracije::KreniNaprijed;
	else if (tekst == "Zaustavi se")
		return Enumeracije::ZaustaviSe;
	else if (tekst == "Skreni")
		return Enumeracije::RotirajSe;

	return Enumeracije::Null;
}


