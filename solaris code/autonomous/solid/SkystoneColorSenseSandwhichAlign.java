package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "SkystoneColorSenseSandwhichAlign", group = "")
public class SkystoneColorSenseSandwhichAlign extends LinearOpMode {

  private DcMotor FrontRight;
  private DcMotor BackRight;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  private DistanceSensor DistanceLeftBack;
  private DistanceSensor DistanceLeftFront;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    FrontRight = hardwareMap.dcMotor.get("FrontRight");
    BackRight = hardwareMap.dcMotor.get("BackRight");
    FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
    BackLeft = hardwareMap.dcMotor.get("BackLeft");
    DistanceLeftBack = hardwareMap.get(DistanceSensor.class, "DistanceLeftBack");
    DistanceLeftFront = hardwareMap.get(DistanceSensor.class, "DistanceLeftFront");

    // setup
    // reverse right motors
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        CheckAlignment();
      }
    }
  }

  /**
   * Turns left at relatively slow power
   *
   * Used to become re-alligned with stones if tilted to right
   */
  private void LeftAdjust(double power) {
    FrontLeft.setPower(-power);
    BackLeft.setPower(-power);
    FrontRight.setPower(power);
    BackRight.setPower(power);
  }

  /**
   * Turns left at relatively slow power
   *
   * Used to become re-alligned with stones if tilted to Left
   */
  private void RightAdjust(double power) {
    FrontLeft.setPower(power);
    BackLeft.setPower(power);
    FrontRight.setPower(-power);
    BackRight.setPower(-power);
  }

  /**
   * On function call reference and compare
   * distance sensor values then adjust accordingly
   */
  private void CheckAlignment() {
    double DistanceBack;
    double DistanceFront;
    double margin;

    DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.CM);
    DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.CM);
    margin = 1.065;
    // The front distance sensor is farther from the stones therefore turn Left until this is no longer true
    if (DistanceBack * margin < DistanceFront) {
      STOPMOTORS();
      while (DistanceBack * margin < DistanceFront && opModeIsActive()) {
        DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.MM);
        DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.MM);
        telemetry.addData("Turn ", "left");
        telemetry.addData("BackDist", DistanceBack);
        telemetry.addData("FrontDist", DistanceFront);
        telemetry.update();
        LeftAdjust(0.3);
      }
      STOPMOTORS();
    } else if (DistanceFront * margin < DistanceBack) {
      // The back distance sensor is closer to the stones therefore turn Right until this is no longer true
      STOPMOTORS();
      while (DistanceFront * margin < DistanceBack && opModeIsActive()) {
        DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.MM);
        DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.MM);
        telemetry.addData("Turn ", "Right");
        telemetry.addData("BackDist", DistanceBack);
        telemetry.addData("FrontDist", DistanceFront);
        telemetry.update();
        RightAdjust(0.3);
      }
      STOPMOTORS();
    } else {
      telemetry.addData("Turn ", "No turn needed");
      telemetry.addData("BackDist", DistanceBack);
      telemetry.addData("FrontDist", DistanceFront);
      telemetry.update();
    }
  }

  /**
   * STOPS MOTORs
   */
  private void STOPMOTORS() {
    FrontLeft.setPower(0);
    BackLeft.setPower(0);
    FrontRight.setPower(0);
    BackRight.setPower(0);
  }

  // use mechanum wheels to strafe left at desired speed
  private void strafeLeft(double power) {
    FrontLeft.setPower(-power);
    BackLeft.setPower(power);
    FrontRight.setPower(power);
    BackRight.setPower(-power);
  }

  // method shall be used to straf left towards skystone on start of match
  // the method takes advantage of the usage of mechanum wheels and needs to be modified if used in other drivetrains including swerve drive
  // method also takes advantage of two distance sensors and average the distsance between the two however one can be used if modified
  private void StrafeToSkystones() {
    // call strafeLeft method to cause the robot to strafe left at 30% power until it 

  }

}
