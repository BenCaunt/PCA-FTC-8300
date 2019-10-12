package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous
public class SolarisDistanceDrive extends LinearOpMode {
  // motor objects
  private DcMotor FrontRight;
  private DcMotor BackRight;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  // Distance Sensor object
  private DistanceSensor distance;

  // Move Function
  // any negative value for left turn
  // any positive value for right turn
  // 0 for straight 'turn'
  public void move(double power, int time, float direction)
  {
    // if direction == 0, then go straight
    if (direction == 0)
    {
      FrontLeft.setPower(power);
      FrontRight.setPower(power);
      BackLeft.setPower(power);
      BackRight.setPower(power);
    }
    // if direction is less than 0, turn left
    else if (direction < 0)
    {
      // basically the current drive train sort of sucks at turning so we add this
      // this changes the power to 20% of the requested power and makes it go for 5 times as long
      power = power / 5;
      time = time * 5;
      FrontLeft.setPower(-power);
      FrontRight.setPower(power);
      BackLeft.setPower(-power);
      BackRight.setPower(power);
    }
    // if direction is greater than 0, turn Right
    else if (direction > 0)
    {
      // basically the current drive train sort of sucks at turning so we add this
      // this changes the power to 20% of the requested power and makes it go for 5 times as long
      power = power / 5;
      time = time * 5;
      FrontLeft.setPower(power);
      FrontRight.setPower(-power);
      BackLeft.setPower(power);
      BackRight.setPower(-power);
    }
    else
    {
      System.out.println("Stuff went wrong\n");
    }
    // power motors for 'time' amount of time
    sleep(time);

    // stop motors after time runs out

    FrontLeft.setPower(0);
    FrontRight.setPower(0);
    BackLeft.setPower(0);
    BackRight.setPower(0);

  }

  // Move function until the distance sensor detects a specific distance in MM
  // power variable is motor power
  // distance variable is the distance threshold that the distance sensor needs to be at or just under
  public void MoveSensor(double power, int DistanceThresh)
  {
    // set motor power
    FrontLeft.setPower(power);
    FrontRight.setPower(power);
    BackLeft.setPower(power);
    BackRight.setPower(power);

    // while loop for comparing distance sensor reading to DistanceThresh variable
    double SensorReading = distance.getDistance(DistanceUnit.MM);
    while (SensorReading > DistanceThresh)
    {
      // update every 50 milliseconds
      sleep(50);
      SensorReading = distance.getDistance(DistanceUnit.MM);
    }
    // once the loop exits, set all motor power to 0
    FrontLeft.setPower(0);
    FrontRight.setPower(0);
    BackLeft.setPower(0);
    BackRight.setPower(0);
  }

  // test if the current reading of the distance sensor is greater than the safe distance 
  public boolean isSafe(int safeDist)
  {
    if (distance.getDistance(DistanceUnit.MM) > safeDist)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    FrontRight = hardwareMap.dcMotor.get("FrontRight");
    BackRight = hardwareMap.dcMotor.get("BackRight");
    FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
    BackLeft = hardwareMap.dcMotor.get("BackLeft");
    distance = hardwareMap.get(DistanceSensor.class, "distance");
    // Reverse right motors
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      MoveSensor(0.3, 400);


    }
  }
}
