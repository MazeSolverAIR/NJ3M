#include "Enumeracije.h"

enum RadnjeMbota {
	PrednjiUZSenzor,
	DesnoUZSenzor,
	LijeviUZSenzor,
	OcitajSenzorCrte
};

enum NaredbaAndroida {
	KreniNaprijed,
	ZaustaviSe,
	RotirajSe,
	Null
};



NaredbaAndroida Enumeracije::DohvatiRadnjuIzStringa(String tekst)
{
	if (tekst == "Kreni naprijed")
		return KreniNaprijed;
	else if (tekst == "Zaustavi se")
		return ZaustaviSe;
	else if (tekst == "Skreni")
		return RotirajSe;
	
	return Null;
}
