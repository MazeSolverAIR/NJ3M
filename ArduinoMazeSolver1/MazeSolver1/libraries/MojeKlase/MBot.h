#pragma once
#include "Motors.h"
#include "MeMCore.h"

class MBot
{
  public:
    MBot();

    void KreirajMotore(uint8_t leftMotorPin, uint8_t rightMotorPin);
    Motors motors;
};
