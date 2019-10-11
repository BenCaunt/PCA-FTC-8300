package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "SolarisAutonoMech2 (Blocks to Java)", group = "")
public class SolarisAutonoMech2 extends LinearOpMode {

  private DcMotor FrontRight;
  private DcMotor BackRight;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  // drive forward / backward function
  public void BasicDrive(double power, int time)
  {
    FrontLeft.setPower(power);
    FrontRight.setPower(power);
    BackLeft.setPower(power);
    BackRight.setPower(power);
    sleep(time);
    FrontLeft.setPower(0);
    FrontRight.setPower(0);
    BackLeft.setPower(0);
    BackRight.setPower(0);
  }

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
      time = time * 5l;
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


  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    FrontRight = hardwareMap.dcMotor.get("FrontRight");
    BackRight = hardwareMap.dcMotor.get("BackRight");
    FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
    BackLeft = hardwareMap.dcMotor.get("BackLeft");

    // Reverse right motors
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // sleep(5000);
      move(1, 1000, 0);
      // turn left
      move(1, 1000, -1);
      move(-1, 1000, 0);

    }
  }
}
