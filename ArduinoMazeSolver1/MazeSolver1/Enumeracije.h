#pragma once
class Enumeracije
{
public:

	enum RadnjeMbota {
		PrednjiUZSenzor,
		DesniUZSenzor,
		LijeviUZSenzor,
		OcitajSenzorCrte
	};

	enum NaredbaAndroida {
		KreniNaprijed,
		ZaustaviSe,
		RotirajSe,
		Null
	};
	Enumeracije();
	NaredbaAndroida DohvatiRadnjuIzStringa(String tekst);


};

