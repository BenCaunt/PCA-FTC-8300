package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import android.graphics.Color;
import java.util.Locale;
import android.view.View;
import android.app.Activity;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "SkystoneColorSenseSandwhichAlign", group = "")
public class SkystoneColorSenseSandwhichAlign extends LinearOpMode {

  // wheel motors
  private DcMotor FrontRight;
  private DcMotor BackRight;
  private DcMotor FrontLeft;
  private DcMotor BackLeft;
  // distance sensors for color sensor sandwhich
  private DistanceSensor DistanceLeftBack;
  private DistanceSensor DistanceLeftFront;

  // color sensor
  ColorSensor sensorColor;
  // distance sensor built into color sensor
  DistanceSensor sensorDistance;
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
    sensorColor = hardwareMap.get(ColorSensor.class, "colorSensorLeft");
    // colorSensorLeft = hardwareMap.colorSensor.get("colorSensorLeft");
    // get a reference to the color sensor.
    sensorColor = hardwareMap.get(ColorSensor.class, "colorSensorLeft");

    // sensor distance is the distance sensor built into the color sensor
    // get a reference to the distance sensor that shares the same name.
    sensorDistance = hardwareMap.get(DistanceSensor.class, "colorSensorLeft");

    /*
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * quick explination of sensors
    * on the side of the robot is an array of three sensors, {2m distance sensor,  color sensor, addition 2m distance sensor}
    * the two distance sensors are used to make sure the robot is parallel to the row of stones
    * the color sensor is used to make sure the robot is the optimal distance away from the skystones as well as actually classify the stones
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    */
    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
    final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

    // wait for the start button to be pressed.

    // reverse right motors
    FrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    BackRight.setDirection(DcMotorSimple.Direction.REVERSE);

    waitForStart();
    // drive has pressed start
    if (opModeIsActive()) {
      // strafe from wall to row of skystones and properly align
      //StrafeToSkystones();
      checkForSkystone();

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
   * margin should be 1.02 for strafing to skystones
   * margin should be between 1.05 and 1.1 for actually checking skystones
   */
  private void CheckAlignment(double margin) {
    double DistanceBack;
    double DistanceFront;

    DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.CM);
    DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.CM);
    // margin = 1.02;
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

  // use mecanum wheels to strafe left at desired speed
  private void strafeLeft(double power) {
    FrontLeft.setPower(-power);
    BackLeft.setPower(power);
    FrontRight.setPower(power);
    BackRight.setPower(-power);
  }
  // use mecanum wheels to strafe Right at a desired speed
  private void strafeRight(double power) {
    FrontLeft.setPower(power);
    BackLeft.setPower(-power);
    FrontRight.setPower(-power);
    BackRight.setPower(power);

  }

  // method shall be used to straf left towards skystone on start of match
  // the method takes advantage of the usage of mecanum wheels and needs to be modified if used in other drivetrains including swerve drive
  // method also takes advantage of two distance sensors and average the distsance between the two however one can be used if modified
  private void StrafeToSkystones() {
    double DistanceBack;
    double DistanceFront;
    double DistanceAverage;
    double SkystoneSafeDist;
    // get data from distance sensor
    DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.CM);
    DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.CM);
    // average distance sensors
    DistanceAverage = (DistanceBack + DistanceFront) / 2;
    // the safe distacne to the skystones
    SkystoneSafeDist = 25;

    // start strafing left
    strafeLeft(0.2);
    // maintain motor speed while the average of the distance sensors is greater than the SkystoneSafeDist keep going and then stop once it reaches the limmit
    do {
      // get data from distance sensor
      DistanceBack = DistanceLeftBack.getDistance(DistanceUnit.CM);
      DistanceFront = DistanceLeftFront.getDistance(DistanceUnit.CM);
      // average distance sensors
      DistanceAverage = (DistanceBack + DistanceFront) / 2;

      // halt execution for n milliseconds
      sleep(50);


    } while((DistanceAverage > SkystoneSafeDist) && opModeIsActive());

    // after loop ends stop motors
    STOPMOTORS();

    // make sure robot is parallel with stones
    CheckAlignment(1.02);

  }

  // on method call, return true if the skystone is detected and false if it is a normal stone
  private boolean isSkystone()
  {
    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;

    // boolean that holds the condition of the skystone being detected or not
    boolean stoneClassification;
    // convert the RGB values to HSV values.
    // multiply by the SCALE_FACTOR.
    // then cast it back to int (SCALE_FACTOR is a double)
    Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
            (int) (sensorColor.green() * SCALE_FACTOR),
            (int) (sensorColor.blue() * SCALE_FACTOR),
            hsvValues);

    // send the info back to driver station using telemetry function.
    telemetry.addData("Distance (cm)",
    String.format(Locale.US, "%.02f", sensorDistance.getDistance(DistanceUnit.CM)));
    // skystone detected
    if (hsvValues[0] > 108)
    {
        telemetry.addData("classified as","skystone");
        stoneClassification = true;
    }
    // normal stone detected
    else
    {
        telemetry.addData("classified as","stone");
        stoneClassification = false;
    }
    telemetry.update();
    return stoneClassification;
  }

  // extension to the isSkystone method above,  this method simply conducts the entire action of testing if a stone is a skystone, including proper movement
  // returns boolean true if skystone has been detected and boolean false if stone detected
  private boolean checkForSkystone()
  {
    // get starting distance to stone
    double staringPosition = sensorDistance.getDistance(DistanceUnit.CM);
    // distance to skystone that will be updated until it is the correct distance away
    double distanceToStone;
    // if the distance from the color sensor to the stone is over 5 cm then get closer until it is just under
    if (staringPosition > 7)
    {
      do {
        distanceToStone = sensorDistance.getDistance(DistanceUnit.CM);
        // strafe left to stone
        strafeLeft(0.25);
        telemetry.addData("stone distance is: ","Too Far");
        telemetry.update();
      } while (distanceToStone > 5);
      STOPMOTORS();

    }

    // if the distance from the color sensor to the stone is under 4 cm then retreat from the stone until it is around 5cm
    else if (staringPosition < 7)
    {
      do {

        distanceToStone = sensorDistance.getDistance(DistanceUnit.CM);
        // strafe right away from stone
        strafeRight(0.15);
        telemetry.addData("stone distance is: ","Too Close");
        telemetry.update();
      } while(distanceToStone < 3);
      STOPMOTORS();
    }

    // the distance to the skystone is just fine, do not adjust distance
    else
    {
      telemetry.addData("stone distance is: ","...just fine...");
      telemetry.update();
    }
    boolean skystoneDetected = isSkystone();
    // if a skystone was detected
    if (skystoneDetected == true)
    {
      telemetry.addData("skystoneDetected == ", "true");
      telemetry.update();
      // skystone collection routine
    }
    // a skystone was not detected so prepare to search another stone
    else
    {
      double DistanceFromStones;

      // retreat from line of stones
      do {
        DistanceFromStones = sensorDistance.getDistance(DistanceUnit.CM);
        telemetry.addData("retreating from wall of stones...","");
        telemetry.addData("Distance From stone: ", DistanceFromStones);
        telemetry.update();
        strafeRight(0.23);


      } while (DistanceFromStones < 14);
      STOPMOTORS();
      // call alignment function to make sure the robot is still parallel and fix it if it is not
      CheckAlignment(1.02);
    }

    // return the value of the stone that was detected
    return skystoneDetected;

  }

}
