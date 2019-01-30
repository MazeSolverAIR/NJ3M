#include "MBot.h"


MBot::MBot()
{
}



void MBot::KreirajMotore(uint8_t leftMotorPin, uint8_t rightMotorPin)
{
  motors = Motors(leftMotorPin, rightMotorPin);
}
