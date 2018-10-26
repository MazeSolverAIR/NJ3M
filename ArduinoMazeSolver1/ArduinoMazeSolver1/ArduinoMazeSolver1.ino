#include "MeMCore.h"

MeDCMotor motor1(M1);

MeDCMotor motor2(M2);

uint8_t brzinaMotora = 127;

const uint8_t brzinaKodOmjera = 127;

float omjerConst = 0;

void setup()
{
  const float stupnjeviZaOmjer = 90;
  const float delayZaOmjer = 472;
  omjerConst = stupnjeviZaOmjer / delayZaOmjer;
}

void loop()
{
  Skreni('l', 360, brzinaMotora); 

  ZaustaviMotore();

  delay(2000);
}

void PokreniMotore(uint8_t motorSpeed)
{
  motor1.run(-motorSpeed);
  motor2.run(motorSpeed);
}

void ZaustaviMotore()
{
  motor1.stop(); 
  motor2.stop();
}

void Skreni(char smijer, int stupnjevi, uint8_t motorSpeed)
{
  float vrijemeTrajanjaRotacije = IzracunajVrijemeRotacije(stupnjevi, motorSpeed);
  if(smijer == 'l')
  {
    motor1.run(motorSpeed);
    motor2.run(motorSpeed);
  }
  else
  {
    motor2.run(-motorSpeed);
    motor1.run(-motorSpeed);
  }

  delay((int)vrijemeTrajanjaRotacije);
}

float IzracunajVrijemeRotacije(int stupnjevi, uint8_t motorSpeed)
{
  float vrijeme = 0;
  
  return vrijeme = stupnjevi/((motorSpeed/brzinaKodOmjera)*omjerConst);
}
