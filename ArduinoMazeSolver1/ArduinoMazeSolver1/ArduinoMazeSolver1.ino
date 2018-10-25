/**
 * \par Copyright (C), 2012-2016, MakeBlock
 * @file    DCMotorDriverTest.ino
 * @author  MakeBlock
 * @version V1.0.0
 * @date    2015/09/09
 * @brief   Description: this file is sample code for Me DC motor device.
 *
 * Function List:
 *    1. void MeDCMotor::run(int16_t speed)
 *    2. void MeDCMotor::stop(void)
 *
 * \par History:
 * <pre>
 * <Author>     <Time>        <Version>      <Descr>
 * Mark Yan     2015/09/09    1.0.0          rebuild the old lib
 * </pre>
 */
#include "MeOrion.h"

MeDCMotor motor1(M1);

MeDCMotor motor2(M2);

uint8_t motorSpeed = 127;

//TODO: Varijabla koja pamti omjer između vremena potrebnog za okretanje kod brzine = 127 za 90° i trenutne brzine i koristi se za delay

void setup()
{
}

void loop()
{
  Skreni('l',motorSpeed); 

  delay(450);

  PokreniMotore(0);

  delay(1000);
}

void PokreniMotore(uint8_t motorSpeed)
{
  motor1.run(-motorSpeed); /* value: between -255 and 255. */
  motor2.run(motorSpeed);
}

void Skreni(char smijer, uint8_t motorSpeed)
{
  if(smijer == 'l')
  {
    motor1.run(motorSpeed); /* value: between -255 and 255. */
    motor2.run(motorSpeed);
  }
  else
  {
    motor2.run(-motorSpeed); /* value: between -255 and 255. */
    motor1.run(-motorSpeed);
  }
}
