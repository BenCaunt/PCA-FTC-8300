package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "solarisTeleOP", group = "")
public class solarisTeleOP extends LinearOpMode {
  private Servo claw;
  private DcMotor back_right;
  private DcMotor back_left;
  private DcMotor front_left;
  private DcMotor front_right;
  private DcMotor armMotor;
  private Servo clawArm;
  private Servo autoGrab;
  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    back_right = hardwareMap.dcMotor.get("FrontRight");
    back_left = hardwareMap.dcMotor.get("FrontLeft");
    front_left = hardwareMap.dcMotor.get("BackLeft");
    front_right = hardwareMap.dcMotor.get("BackRight");
    armMotor = hardwareMap.dcMotor.get("armMotor");
    clawArm = hardwareMap.servo.get("clawArm");
    clawArm.setDirection(Servo.Direction.REVERSE);
    autoGrab = hardwareMap.servo.get("autoGrab");
    autoGrab.setDirection(Servo.Direction.REVERSE);
    claw = hardwareMap.servo.get("claw");



    // Reverse one of the drive motors.
    back_right.setDirection(DcMotorSimple.Direction.REVERSE);
    //back_left.setDirection(DcMotorSimple.Direction.REVERSE);
    //front_left.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // speed that dpad controls go at; joy stick speed devided by this 
        double dpadSpeed = 0.5;
          
          if (gamepad1.dpad_down == true || gamepad2.dpad_down == true) {
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            back_left.setPower(dpadSpeed);
            back_right.setPower(dpadSpeed);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            front_left.setPower(dpadSpeed);
            front_right.setPower(-dpadSpeed);
          }
          if (gamepad1.dpad_up == true || gamepad2.dpad_up == true) {
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            back_left.setPower(-dpadSpeed);
            back_right.setPower(-dpadSpeed);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            front_left.setPower(-dpadSpeed);
            front_right.setPower(dpadSpeed);
          }
          if (gamepad1.dpad_right == true || gamepad2.dpad_right == true) {
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            back_left.setPower(dpadSpeed);
            back_right.setPower(-dpadSpeed);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            front_left.setPower(-dpadSpeed);
            front_right.setPower(-dpadSpeed);
          }
          if (gamepad1.dpad_left == true || gamepad2.dpad_left == true) {
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            back_left.setPower(-dpadSpeed);
            back_right.setPower(dpadSpeed);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            front_left.setPower(dpadSpeed);
            front_right.setPower(dpadSpeed);
          }
          else {
          if (gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0) {
            // Right Joystick
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            double right_x_scaled = gamepad1.right_stick_x / dpadSpeed;
            back_left.setPower(right_x_scaled);
            front_left.setPower(right_x_scaled);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            back_right.setPower(-right_x_scaled);
            front_right.setPower(right_x_scaled);
          } else {
            // Left Joystick
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            double left_y_scaled = gamepad1.left_stick_y / dpadSpeed;
            back_left.setPower(-left_y_scaled);
            back_right.setPower(-left_y_scaled);
            // The Y axis of a joystick ranges from -1 in its topmost position
            // to +1 in its bottommost position. We negate this value so that
            // the topmost position corresponds to maximum forward power.
            front_left.setPower(-left_y_scaled);
            front_right.setPower(left_y_scaled);
          }
        }
        // control arm with left stick on gamepad 2 
        armMotor.setPower(-gamepad2.left_stick_y / 1.5);
        
        // flip claw out 
        if (gamepad2.x) {
          armOut();
        }
        // flip claw in 
        if (gamepad2.b) {
          armIn();
        }
        // grab stone 
        if (gamepad2.a) {
          closeClaw();
        }
        
        // release stone 
        if (gamepad2.y) {
          openClaw();
        }
        
        // platform grab down 
        if (gamepad2.left_stick_button) {
          autoClawDown();
        }
        if (gamepad2.right_stick_button){
          autoClawUp();
        }
        
        telemetry.addData("Left Pow", back_left.getPower());
        telemetry.addData("Left Pow", back_right.getPower());
        telemetry.addData("Left Pow", front_left.getPower());
        telemetry.addData("Right Pow", front_right.getPower());
        telemetry.update();
  }
}
}
public void armOut() {
  autoClawDown();
  sleep(150);
  clawArm.setPosition(0.73);
  sleep(750);
  autoClawUp();
}
  
  public void armIn() {
    autoClawDown();
    sleep(100);
    clawArm.setPosition(0.1);
    sleep(550);
    autoClawUp();
    sleep(200);
  }
  
  public void autoClawDown() {
    autoGrab.setPosition(1);
    
  }
  public void autoClawUp() {
    autoGrab.setPosition(0.5);
  }

  private void closeClaw() {
    claw.setPosition(1);
  }


  private void openClaw() {
    claw.setPosition(0);
  }
}
