package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "_2mDistanceTest (Blocks to Java)", group = "")
public class _2mDistanceTest extends LinearOpMode {

  private DistanceSensor distance;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    distance = hardwareMap.get(DistanceSensor.class, "distance");

    // Put initialization blocks here.
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        telemetry.update();
        telemetry.addData("Distance", distance.getDistance(DistanceUnit.MM));
      }
    }
  }
}
