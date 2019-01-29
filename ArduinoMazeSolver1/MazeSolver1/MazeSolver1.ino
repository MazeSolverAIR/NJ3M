#include "MeMCore.h"
#include <SoftwareSerial.h>
#include "Arduino.h"


uint8_t modRadnje = -1;
bool stisnutGumb = true;
MeDCMotor leftMotor(M1);
MeDCMotor rightMotor(M2);
MeUltrasonicSensor ultraSonic(1);
MeUltrasonicSensor ultraSonicRight(4);
MeUltrasonicSensor ultraSonicLeft(3);
MeLineFollower lineFollower(2);
uint16_t brzinaKretanja = 80;
Me4Button button = Me4Button();
MeBluetooth bluetooth = MeBluetooth();

//Postavljanje inicijalnih postavki za rad sa robotom
void setup()
{
	button.setpin(A7);

	Serial.begin(115200);
	Serial.setTimeout(5);
}

//Loop u smislu while(1) koji cijelo vrijeme izvršava algoritam robota za izlazak iz labirinta
void loop()
{
	rjesenjeProblemaAutonomno();
}

String saljiPodatkeMobitelu() {
	String porukaMobitelu = "";
	porukaMobitelu += ultraSonic.distanceCm();
	porukaMobitelu += "b";
	return porukaMobitelu;
}


//Metoda koja služi za izvršavanje naredbe za kretanje robota, ovisno o poruci koju primi preko bluetootha
void IzvrsiNaredbu(String poruka) {
	if (poruka.equals("RM")) {
		Kreni(brzinaKretanja);
	}
	else if (poruka.equals("SM")) {
		ZaustaviMotore();
	}
	else if (poruka.equals("SL")) 
	{
		ZaustaviMotore();
		Skreni('l', 66, brzinaKretanja);
		Serial.println("OK");
		delay(1500);
	}
	else if (poruka.equals("SD")) {
		ZaustaviMotore();
		Skreni('d', 66, brzinaKretanja);
		delay(1500);
	}
	else if (poruka.equals("FR")) {
		ZaustaviMotore();
		Skreni('l', 150, brzinaKretanja);
		Skreni('l', 150, brzinaKretanja);
		delay(1500);
	}
}


bool stop = true;


//Algoritam za izlazak iz labirinta koji jednim dijelom prati liniju, a kada su oba čitača na liniji, izvršava algoritam
void rjesenjeProblemaAutonomno() {
	String poruka = "";
	poruka = Serial.readString();

	if (poruka.equals("STP"))
	{
		stop = true;
		return;
	}
	else if (poruka.equals("RUN"))
		stop = false;

	if (!stop)
	{
		Serial.println(saljiPodatkeMobitelu());
		delay(10);
		unsigned long sensorStateCenter = lineFollower.readSensors();
		switch (sensorStateCenter)
		{
		case S1_IN_S2_IN:
			IzvrsiNaredbu(poruka);
			break;
		case S1_IN_S2_OUT:
			ZaustaviMotore();
			Skreni('l', 5, 40);
			break;
		case S1_OUT_S2_IN:
			ZaustaviMotore();
			Skreni('d', 5, 40);
			break;
		case S1_OUT_S2_OUT:
			Skreni('l', 5, 40);
			break;
		}
	}
}

//Radnja koja se izvršava pritiskom na tipku
void IzvrsiPritisakTipke()
{
	if (button.pressed() && stisnutGumb)
	{
		stisnutGumb = false;
		modRadnje++;

		if (modRadnje > 2)
			modRadnje = 0;
	}
	if (!button.pressed() && !stisnutGumb)
	{
		stisnutGumb = true;
	}
}

//Metoda za kretanje robota u smjeru naprijed
void Kreni(uint16_t brzinaKretanja)
{
	leftMotor.run(-brzinaKretanja);
	rightMotor.run(brzinaKretanja);
}

//Metoda za zaustavljanje robota
void ZaustaviMotore()
{
	leftMotor.stop();
	rightMotor.stop();
}

//Metoda za skretanje robota ovisno o vrijednosti prvog parametra, l kao lijevi ili d kao desno
void Skreni(char smijer, uint16_t stupnjevi, uint16_t brzina)
{
	if (smijer == 'l')
	{
		leftMotor.run(150);
		rightMotor.run(150);
	}
	else
	{
		leftMotor.run(-150);
		rightMotor.run(-150);
	}
	delay(IzracunajVrijemeRotacije(stupnjevi,brzina));
	ZaustaviMotore();
}

//Metoda za izračun vremenu duljine okretanja u određenome smjeru kako bi se robot što točnije ka zadanoj vrijednosti
unsigned long IzracunajVrijemeRotacije(uint16_t stupnjevi, uint16_t brzina)
{
	unsigned long vrijemeOkretanje = (stupnjevi * 650) / (brzina);

	return vrijemeOkretanje;
}

//Algoritam za izlazak robota iz labirinta sa pravilom skretanja u desno (Bez potrebe android uređaja)
void rjesenjeProblemaDesno() {
	unsigned long sensorStateCenter = lineFollower.readSensors();
	unsigned long udaljenostDoZida = ultraSonic.distanceCm();
	//provjeriti slanje očitanja senzora mobilnoj aplikaciji
	//Serial.println(sensorStateCenter);
	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		if (udaljenostDoZida <= 22) {
			Skreni('d', 66, brzinaKretanja);
			delay(1300);
			unsigned long novaDistanca = ultraSonic.distanceCm();
			if (novaDistanca <= 22) {
				Skreni('d', 66, brzinaKretanja);
				delay(1300);
				//ponovno skreni da se ne vracas unatrag
				if (ultraSonic.distanceCm() > 22) {
					Skreni('d', 66, brzinaKretanja);
					delay(1300);
					if (ultraSonic.distanceCm() <= 22) {
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
						Skreni('d', 66, brzinaKretanja);
						delay(1300);
					}
					else {

					}
				}
				else {

				}
			}
			else {

			}
		}
		else {
			Kreni(brzinaKretanja);
		}

		break;
	case S1_IN_S2_OUT:
		ZaustaviMotore();
		Skreni('l', 5, 40);
		break;
	case S1_OUT_S2_IN:
		ZaustaviMotore();
		Skreni('d', 5, 40);
		break;
	case S1_OUT_S2_OUT:
		Skreni('d', 5, 40);
		break;
	}
}

//Algoritam za izlazak robota iz labirinta sa pravilom skretanja u lijevo (Bez potrebe android uređaja)
void rjesenjeProblema() {
	unsigned long sensorStateCenter = lineFollower.readSensors();
	unsigned long udaljenostDoZida = ultraSonic.distanceCm();
	switch (sensorStateCenter)
	{
	case S1_IN_S2_IN:
		if (udaljenostDoZida <= 22) {
			Skreni('l', 75, brzinaKretanja);
			delay(1300);
			if (ultraSonic.distanceCm() <= 22) {
				Skreni('l', 75, brzinaKretanja);
			}
			else {

			}
		}
		else {
			Kreni(brzinaKretanja);
		}
		
		break;
	case S1_IN_S2_OUT:
		//senzor 2 je van linije (desni senzor)
		ZaustaviMotore();
		Skreni('l', 5, 40);
		break;
	case S1_OUT_S2_IN:
		//senzor 1 je van linije (lijevi senzor)
		ZaustaviMotore();
		Skreni('d', 5, 40);
		break;
	case S1_OUT_S2_OUT:
		Skreni('l', 5, 40);
		break;
	}
}





