package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "watsonred1 (Blocks to Java)", group = "")
public class watsonred1 extends LinearOpMode {
  /**
   * mechanum wheel drive function 
   * the variable 'direction' has 1 of 4 possible values as shown here 
   * 
   *          1 - forward 
   *   2 - left         3 - right 
   *          4 - reverse 
   *    
   * 
   *   the distance variable is a double that contains the number of centimeters 
   *   that the robot needs to travel 
   * 
   */
  private void mechanum_drive(int direction, long distance) {
    System.out.println("Robot is travelingL " + distance + "centimeter(s)\n");
    System.out.println("Robot is traveling in direction: " + direction + "\n");
    // go forward 
    if (direction == 1)
    {
      back_left.setPower(1);
      back_right.setPower(1);

      front_left.setPower(1);
      front_right.setPower(1);
    }
    // go backward 
    else if (direction == 4)
    {
      back_left.setPower(-1);
      back_right.setPower(-1);

      front_left.setPower(-1);
      front_right.setPower(-1);
    }
    // go left 
    else if (direction == 2)
    {
      // code for robot going left 
    }
    // go right 
    else if (direction == 3)
    {
      // code for robot going right 
    }
    
    else 
    {
      System.out.println("ISSUE, BIG ISSUE,  int direction is != 1,2,3, or 4 direction value is: " + distance + "\n");

    }
    // amount of time the robot motors travel for 
    // *** TODO ***
    // basically remove this sleep function and change it to a while loop that ends 
    // once the motor turns a certain number of times
    // this should be calculated out of the distance variable 
    sleep(distance);
    
  
  }
  
  //private mechanum_drive(int direction, double distance);
  private DcMotor back_right;
  private DcMotor back_left;
  private DcMotor front_right;
  private DcMotor front_left;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    back_right = hardwareMap.dcMotor.get("back_right");
    back_left = hardwareMap.dcMotor.get("back_left");
    back_right = hardwareMap.dcMotor.get("front_right");
    back_left = hardwareMap.dcMotor.get("front_left");
    // Reverse one of the drive motors.
    // You will have to determine which motor to reverse for your robot.
    // In this example, the right motor was reversed so that positive
    // applied power makes it move the robot in the forward direction.
    back_right.setDirection(DcMotorSimple.Direction.REVERSE);
    front_right.setDirection(DcMotorSimple.Direction.REVERSE);
    waitForStart();
    if (opModeIsActive()) {
      // robot code here!!!!!!!!!!!!!!!!!!!!!!!!!111 
      // test mechanum drive 
      mechanum_drive(1, 5);
    }
  }


  
}
