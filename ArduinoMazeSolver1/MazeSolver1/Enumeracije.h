#pragma once
class Enumeracije
{
public:

	enum InfoZaAndroid {
		PrednjiUZSenzor,
		DesniUZSenzor,
		LijeviUZSenzor,
		OcitajSenzorCrte,
		ZadnjaPoruka,
		Null
	};

	enum NaredbaAndroida {
		KreniNaprijed,
		ZaustaviSe,
		RotirajSe,
		UbrzajLijeviMotor,
		UbrzajDesniMotor,
		ZadnjaPoruka,
		Null
	};
	Enumeracije();
	NaredbaAndroida DohvatiRadnjuIzPoruke(String tekst);

	String DohvatiRadnjuIzEnuma(InfoZaAndroid);


};

