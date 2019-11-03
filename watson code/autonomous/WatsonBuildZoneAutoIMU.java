package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "WatsonBuildZoneAutoIMU", group = "")
public class WatsonBuildZoneAutoIMU extends LinearOpMode {

  private DcMotor back_right;
  private DcMotor back_left;
  private DcMotor front_right;
  private DcMotor front_left;
  private BNO055IMU imu;
  BNO055IMU.Parameters IMU_Parameters;

  double Left_Power;
  double Right_Power;
  float Yaw_Angle;
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {


    back_right = hardwareMap.dcMotor.get("back_right");
    back_left = hardwareMap.dcMotor.get("back_left");
    front_right = hardwareMap.dcMotor.get("front_right");
    front_left = hardwareMap.dcMotor.get("front_left");
    imu = hardwareMap.get(BNO055IMU.class, "imu");


    back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    // Reverse direction of one motor so robot moves
    // forward rather than spinning in place.
    front_right.setDirection(DcMotorSimple.Direction.REVERSE);
    back_right.setDirection(DcMotorSimple.Direction.REVERSE);
    // Create an IMU parameters object.
    IMU_Parameters = new BNO055IMU.Parameters();
    // Set the IMU sensor mode to IMU. This mode uses
    // the IMU gyroscope and accelerometer to
    // calculate the relative orientation of hub and
    // therefore the robot.
    IMU_Parameters.mode = BNO055IMU.SensorMode.IMU;
    // Intialize the IMU using parameters object.
    imu.initialize(IMU_Parameters);
    // Report the initialization to the Driver Station.
    telemetry.addData("Status", "IMU initialized, calibration started.");
    telemetry.update();
    // Wait one second to ensure the IMU is ready.
    sleep(1000);
    // Loop until IMU has been calibrated.
    while (!IMU_Calibrated()) {
      telemetry.addData("If calibration ", "doesn't complete after 3 seconds, move through 90 degree pitch, roll and yaw motions until calibration complete ");
      telemetry.update();
      // Wait one second before checking calibration
      // status again.
      sleep(1000);
    }
    // Report calibration complete to Driver Station.
    telemetry.addData("Status", "Calibration Complete");
    telemetry.addData("Action needed:", "Please press the start triangle");
    telemetry.update();
    // Wait for Start to be pressed on Driver Station.
    waitForStart();

    DriveWithImu(0.3, 5000);
    sleep(500);
    TurnWithIMU(0.3, -90);

  }
  /**
   * Function that becomes true when gyro is calibrated and
   * reports calibration status to Driver Station in the meantime.
   */
  private boolean IMU_Calibrated() {
    telemetry.addData("IMU Calibration Status", imu.getCalibrationStatus());
    telemetry.addData("Gyro Calibrated", imu.isGyroCalibrated() ? "True" : "False");
    telemetry.addData("System Status", imu.getSystemStatus().toString());
    return imu.isGyroCalibrated();
  }


  // power argument is motor power
  // drive time argument is the amount of time the motors run for
  private void DriveWithImu(double power, int DriveTime) {
    // current unix time
    long MethodStartTime = System.currentTimeMillis();
    // Initialize motor power variables
    Left_Power = power;
    Right_Power = power;
    // Set motor powers to the variable values.
    front_left.setPower(Left_Power);
    back_left.setPower(Left_Power);
    back_right.setPower(Right_Power);
    front_right.setPower(Right_Power);
    // Move robot forward for 2 seconds or until stop
    // is pressed on Driver Station.
    // keep driving and adjusting while the current unix time is less than the time of method start + DriveTime
    while (!(DriveTime+MethodStartTime < System.currentTimeMillis()  || isStopRequested())) {
      // Save gyro's yaw angle
      Yaw_Angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
      // Report yaw orientation to Driver Station.
      telemetry.addData("Yaw angle", Yaw_Angle);
      // If the robot is moving straight ahead the
      // yaw value will be close to zero. If it's not, we
      // need to adjust the motor powers to adjust heading.
      // If robot yaws right or left by 5 or more,
      // adjust motor power variables to compensation.
      if (Yaw_Angle < -5) {
        // Turn left
        Left_Power = 0.25;
        Right_Power = 0.35;
      } else if (Yaw_Angle > 5) {
        // Turn right.
        Left_Power = 0.35;
        Right_Power = 0.25;
      } else {
        // Continue straight
        Left_Power = 0.3;
        Right_Power = 0.3;
      }
      // Report the new power levels to the Driver Station.
      telemetry.addData("Left Motor Power", Left_Power);
      telemetry.addData("Right Motor Power", Right_Power);
      // Update the motors to the new power levels.
      front_left.setPower(Left_Power);
      back_left.setPower(Left_Power);
      back_right.setPower(Right_Power);
      front_right.setPower(Right_Power);
      telemetry.update();
      // Wait 1/5 second before checking again.
      sleep(200);
    }

  }
  // takes in power variable and turns until it reaches the TurnAngle variable
  // turns right if power is positive, left if negative
  public void TurnWithIMU(double power, int TurnAngle) {
          // Now let's execute a right turn using power
          // levels that will cause a turn in place.
          back_right.setPower(power);
          front_right.setPower(power);
          front_left.setPower(-power);
          back_left.setPower(-power);
          // Continue until robot yaws right by 90 degrees
          // or stop is pressed on Driver Station.
          while (!(Yaw_Angle <= TurnAngle || isStopRequested())) {
            // Update Yaw-Angle variable with current yaw.
            Yaw_Angle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            // Report yaw orientation to Driver Station.
            telemetry.addData("Yaw value", Yaw_Angle);
            telemetry.update();
          }
          // We're done. Turn off motors
          back_right.setPower(0);
          back_left.setPower(0);
          front_right.setPower(0);
          front_left.setPower(0);
          // Pause so final telemetry is displayed.
          sleep(1000);
        }
}
