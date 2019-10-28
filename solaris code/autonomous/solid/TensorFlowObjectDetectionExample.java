/*

  Original code by firstinspires; adapted for controlling solaris robot platform by ben caunt
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * This 2019-2020 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the Skystone game elements.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Concept: TensorFlow Object Detection Webcam", group = "Concept")

public class TensorFlowObjectDetectionExample extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AU2TlW//////AAABmep5d0YlekZIqsKMHpu4ldp6HkIZkAgJ1eSR8hIbp90QQkJ7FfU+AUqypCFf3VangLajH2T0m++ba/jRY5LjUY32+NfBlI6BmkFeXxCiFZAmiKYCOk6xR9jZLllS5C0ePCGJZGX89A4PKEq+nafcWfkRkt7GAnbCdRuMfW9Bk9EMF9pdgZXZpa8PXW2gIfMBMPXoDpjefIfkw1nmPFPVK7vUjiZBLJUEp7gObGimnhCxEPBuDmap0wB5pVDJn5eYK5/c5+gS3002nS1GT5NS6OBSb20uC1RwcjY7jpK3+B3D0VO146CQ7ECKOgZ7Ng5dQqnv38ReQI3MWs+ZqExHMH6kCTnBTSslc5w51uEn++7V ";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;
    @Override
    public void runOpMode() {
        /*
          ***  ROBOT INITALIZATION   ***
        */
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

        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        /*
          *** END OF INIT ***
        */
        waitForStart();
        /*
        ***  PLAY BUTTON HAS BEEN PRESSED  INIT IS OVER ***
        */
        // initalize RobotIsNotAlligned to true so that the loop can run
        boolean RobotIsNotAlligned = true;

        if (opModeIsActive()) {
            while (RobotIsNotAlligned) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                      telemetry.addData("# Object Detected", updatedRecognitions.size());
                      // step through the list of recognitions and display boundary info.
                      int i = 0;
                      for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());
                      }
                      telemetry.update();

                    }
                    // if the robot is not allgined with the skystone then correct for the mistake
                    if (isRobotAlignedWithSkyStone(recognition.getLeft()) == false)
                    {
                      // allign left and right with the skystone
                      System.out.println("as far as i know there is no 'pass' in java so i use this instead lolol");
                    }
                    // if the robot IS allgined with the skystone but more than 100mm away, go forward a 'little bit'
                    // isSafe function returns true if the measure of the front distance sensor is greater than the input
                    // in this case, if the front distance sensor is gives back a value greater than 100 go forward a little bit, then reallign if necessary then repeat until
                    else if (isSafe(100))
                    {
                      move(0.4, 100, 0);
                    }
                    // if the robot is allignd and is within 100mm then this loop will exit and object detection will stop
                    else
                    {
                      // the robot is now alligned
                      RobotIsNotAlligned = false;
                    }
                }
            }
        }



        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
       tfodParameters.minimumConfidence = 0.8;
       tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
       tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    // check if the robot is aligned with the skystone
    private boolean isRobotAlignedWithSkyStone(float leftValue) {
      // left minimum should be around 15 and left max should be around 155
      float leftValueMinimumThreshold = 15;
      float leftValueMaximumThreshold = 155;

      // tests if leftValue is within the minimum and maximum threshold and then returns true if this is the case
      if (leftValue => leftValueMinimumThreshold && leftValue <= leftValueMaximumThreshold)
      {
        return true;
      }
      else
      {
        // this was not the case and the robot is not alligned with the skystone therefore the function shall return false
        return false;
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

    }

}
