/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Accelerometer;
//import com.sun.squawk.util.MathUtils;
//import java.lang.Math;
//import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 *
 * @author JRees
 */
public class RobotIO {

    private static Victor leftDrive;
    private static Victor rightDrive;
    private static Victor armMotor;
    //private static Victor gripperMotor;
    private static Victor gripperRoller;

    private static Compressor compressor;
    private static Solenoid gearShift;
    private static Solenoid armBrake;
    private static Solenoid armTiltUp;
    private static Solenoid armTiltDown;
    private static Solenoid minibotDeployOut;
    private static Solenoid minibotDeployIn;
    private static Solenoid minibotAlignOut;
    private static Solenoid minibotAlignIn;

    private static DigitalInput bannerSensor;
    private static DigitalInput bottomLimit;
    private static DigitalInput minibotAlignedLimit;
    private static DigitalInput tubeSensor;
    private static DigitalInput leftLineSensor;
    private static DigitalInput rightLineSensor;
    private static DigitalInput centerLineSensor;
    //public static DigitalInput armTiltSensor;
    public static Encoder armBannerSensorEncoder;
    private static AnalogChannel ultraSonicFront;
    private static Encoder armEncoder;
    private static int LastEncValue = -1000000;
    private static Gyro gyro;
    public static Accelerometer accel;
    //private static Encoder leftEncoder;
    //private static Encoder rightEncoder;

    private static double leftDriveValue,rightDriveValue,armMotorValue,leftCurr,rightCurr,gripperMotorValue,gripperRollerValue,leftEncoderValue,rightEncoderValue,gyroValue;
    private static boolean armBrakeValue,compressorOn,bannerSensorValue,tubeSensorValue,gearShiftValue,bottomLimitValue,armTiltUpValue,armTiltDownValue,minibotAlignOutValue,minibotDeployOutValue,leftLineValue,rightLineValue,centerLineValue;//,armTiltSensorVal
    private static double ultraSonicValue,accelValue;


    //Motor Ports
    public static final int leftDrMtrPort      = 9; /** leftDr = {@value #leftDr} */
    public static final int rightDr            = 10; /** rightDr = {@value #rightDr} */
    public static final int armMotorPort       = 3; /** armMotorPort = {@value #armMotorPort} */
    public static final int gripperRollerPort  = 4; /** gripperRollerPort= {@value #gripperRollerPort}*/


    //Solenoid Ports
    public static final int gearShiftPort         = 1; /** gearShiftPort = {@value #gearShiftPort}*/
    public static final int armTiltUpPort         = 2;
    public static final int armTiltDownPort       = 3;
    public static final int minibotAlignOutPort   = 4; /** minibotAlignPort = (@value #minibotAlignPort)*/
    public static final int minibotAlignInPort    = 5;
    public static final int minibotDeployOutPort  = 6; /** minibotDeployPort = (@value #minibotDeployPort)*/
    public static final int minibotDeployInPort   = 7;
    public static final int armBrakePort          = 8;

    //Relay ports
    public static final int compressorRelay    = 1; /** compressorRelay = {@value #compressorRelay} */

    //Digital Inputs
    public static final int armEncBotPort      = 1;
    public static final int armEncTopPort      = 2;
    public static final int leftLinePort       = 3;
    public static final int centerLinePort     = 4;
    public static final int rightLinePort      = 5;
    public static final int minibotSwitchPort  = 6;
    public static final int tubeSensorPort     = 7;
    public static final int bottomLimitPort    = 8;
    public static final int armEncPort1        = 9;
    public static final int armEncPort2        = 10;
    public static final int pressureSwitch     = 14; /** pressureSwitch = {@value #pressureSwitch} */
    public static final int ultrasonicSensorPort= 3;
    public static final int gyroPort           = 1;
    public static final int accelPort          = 2;

    public static final double ARM_MOTOR_UP_VAL   = 0.5;
    public static final double ARM_MOTOR_DN_VAL   =-0.3;
    public static final double ARM_MOTOR_OFF_VAL  = 0.0;

    public static final int arraySize = 5;   // Change this as needed to get a good average value
    public static       int     myPtr = 0;   // Pointer to where to write the value in the array
    public static int           myItr;       // Iterator to retrieve the individual values in the array
    public static double        mySum;       // Temporary storage of the sum
    public static boolean       myAvg1st = true;  // Indicates the first runthrough of the average routene
    public static double[]      avgArray = new double[arraySize]; // default values to 0

    public static int ctr = 0;
    public static final int barArraySize = 8;
    public static double[] barArray      =  new double[barArraySize];
    // DigitalInput e = new DigitalInput();
    /**
     * Creates all RobotIO objects
     * this is a new comment
     */
    public RobotIO()
    {
     compressor      = new Compressor(pressureSwitch, compressorRelay);
     accel           = new Accelerometer(accelPort);
     leftDrive       = new Victor(leftDrMtrPort);
     rightDrive      = new Victor(rightDr);
     gearShift       = new Solenoid(gearShiftPort);
     armBrake        = new Solenoid(armBrakePort);
     gripperRoller   = new Victor(gripperRollerPort);
     armMotor        = new Victor(armMotorPort);
 //  bannerSensor    = new DigitalInput(bannerSensorPort);
     bottomLimit     = new DigitalInput(bottomLimitPort);
     minibotAlignedLimit = new DigitalInput(minibotSwitchPort);
     tubeSensor      = new DigitalInput(tubeSensorPort);
     leftLineSensor  = new DigitalInput(leftLinePort);
     centerLineSensor= new DigitalInput(centerLinePort);
     rightLineSensor = new DigitalInput(rightLinePort);
     ultraSonicFront = new AnalogChannel(ultrasonicSensorPort);
     armTiltUp         = new Solenoid(armTiltUpPort);
     armTiltDown       = new Solenoid(armTiltDownPort);
     minibotDeployOut = new Solenoid(minibotDeployOutPort);
     minibotDeployIn = new Solenoid(minibotDeployInPort);
     minibotAlignOut  = new Solenoid(minibotAlignOutPort);
     minibotAlignIn  = new Solenoid(minibotAlignInPort);
     gyro           =  new Gyro(gyroPort);
     accel.setZero(1.5);
     accel.setSensitivity(0.3);

    //armTiltSensor  = new DigitalInput(armTiltSensorPort);
     //armBannerSensorEncoder = new Encoder(armEncBotPort,armEncTopPort);
     armBannerSensorEncoder =  new Encoder(armEncBotPort,armEncTopPort,false,CounterBase.EncodingType.k2X); // 10 == Lower Limit, 9 == Upper Limit
     armEncoder =  new Encoder(armEncPort1,armEncPort2,false,CounterBase.EncodingType.k2X); // 10 == Lower Limit, 9 == Upper Limit
     armBannerSensorEncoder.start();
     armEncoder.start();
//     rightEncoder.start();
//     leftEncoder.start();
//     rightEncoder.setDistancePerPulse(.00428399);
//     leftEncoder.setDistancePerPulse(.00428399);
    }

    //  Gets Information and stores it
    public static void retrieveValues()
    {
        leftDriveValue        = leftDrive.get();
        rightDriveValue       = rightDrive.get();
        compressorOn          = compressor.getPressureSwitchValue();
        armMotorValue         = armMotor.get();
  //      gripperMotorValue = gripperMotor.get();
        gripperRollerValue    = gripperRoller.get();
        armBrakeValue         = armBrake.get();
        gearShiftValue        = gearShift.get();
        bottomLimitValue      = bottomLimit.get();
        tubeSensorValue       = tubeSensor.get();
        armTiltUpValue        = armTiltUp.get();
        armTiltDownValue      = armTiltDown.get();
        minibotAlignOutValue  = minibotAlignOut.get();
        minibotDeployOutValue = minibotDeployOut.get();
        leftLineValue         = leftLineSensor.get();
        centerLineValue       = centerLineSensor.get();
        rightLineValue        = rightLineSensor.get();
//        leftEncoderValue      = leftEncoder.getRate();
//        rightEncoderValue     = rightEncoder.getRate();
        leftCurr              = leftDrive.get();
        rightCurr             = rightDrive.get();
        gyroValue             = getAVG(gyro.getAngle());
        accelValue            = accel.getAcceleration();

/*        if (LastEncValue != armBannerSensorEncoder.getRaw()) {
            LastEncValue = armBannerSensorEncoder.getRaw();
            System.out.println(" Banner = " + LastEncValue);
            System.out.println("Encoder = " + armEncoder.getRaw());
        } */
        if (LastEncValue != armEncoder.getRaw()) {
            LastEncValue = armEncoder.getRaw();
            System.out.println("Encoder = " + armEncoder.getRaw());
        }

        ultraSonicValue   = ultraSonicFront.getValue();
        //armTiltSensorVal  = armTiltSensor.get();

    }
    public static double getAccelValue()
    {
        return accelValue;
    }
    //converting the ultrasonic sensor value to inches
    //10 millivolts = 1 inch
   public static double getGyroValue()
    {
       return gyroValue;
    }

    public static double getDistVal(){
       return (ultraSonicValue/2.0);
    }
    public static void setArmBrakeValue(boolean armBrakeValue) {
        RobotIO.armBrakeValue = armBrakeValue;
    }

    public static boolean getArmBrakeValue() {
        return armBrakeValue;
    }
//    public static void startCamera()
//    {
//        camera.getInstance();
//    }
    public static boolean getArmTiltUpValue()
    {
        return armTiltUpValue;
    }
//    public static double getRightSpeed()
//    {
//        return rightEncoderValue;
//    }
//    public static double getLeftSpeed()
//    {
//        return leftEncoderValue;
//    }
//    public static double getCurrentLeftSpeed()
//    {
//        return leftCurr;
//    }
//    public static double getCurrentRightSpeed()
//    {
//        return rightCurr;
//    }
    public static void setArmTilt(boolean armTiltValue)
    {
        RobotIO.armTiltUpValue = armTiltValue;
        RobotIO.armTiltDownValue = !armTiltValue;
    }

    public static int getArmEncoder()
    {        return armBannerSensorEncoder.getRaw();

    }

    public static int getArmEncoderNew()
    {
        return armEncoder.getRaw();
    }

    public static boolean getLeftLineSensor()
    {
        return leftLineValue;
    }

    /*public static boolean getArmTiltSensor()
    {
        return armTiltSensorVal;
    }*/

    public static boolean getCenterLineSensor()
    {
        return centerLineValue;
    }

    public static boolean getRightLineSensor()
    {
        return rightLineValue;
    }

//    public static void setBottomLimitValue(boolean bottomLimitValue)
//    {
//        RobotIO.bottomLimitValue = bottomLimitValue;
//    }

    public static boolean getBottomLimitValue()
    {
        return bottomLimitValue;
    }
    public static void setTubeSensorValue(boolean tubeSensorValue)
    {
            RobotIO.tubeSensorValue = tubeSensorValue;
    }
    public static boolean getTubeSensorValue()
    {//got rid of useless timer method-Adam
        return tubeSensorValue;
    }
    public static void startCompressor(){
        //System.out.println("is comp enabled?" + compressor.enabled() + " is switch on? " + compressor.getPressureSwitchValue());
        compressor.start();
    }
    public static boolean getPressureSwitchValue()
    {
        return compressor.getPressureSwitchValue();
    }

    public static void changeGears(boolean t)
    {
        gearShiftValue = t;
    }

    public static double getArmMotorValue() {
        return armMotorValue;
    }

    public static void setArmMotorValue(double armMotorValue) {
        RobotIO.armMotorValue = armMotorValue;
    }

    public static double getAVG(double value)
    {
      
       if(ctr < barArraySize-1)
       {
           ctr+=1;
       }
       else
       {
           ctr = 0;
       }
       barArray[ctr]=value;
       double total = 0;
       for (int i = 0; i < barArraySize;i++)
       {
           total = total+barArray[i];
       }
       return(total/barArraySize);

    }
    public static void setValues()
    {
        leftDrive.set(leftDriveValue);
        rightDrive.set(rightDriveValue);
        gearShift.set(gearShiftValue);
        armMotor.set(armMotorValue);

        armTiltUp.set(armTiltUpValue);
        armTiltDown.set(armTiltDownValue);
        minibotDeployOut.set(minibotDeployOutValue);
        minibotDeployIn.set(!minibotDeployOutValue);
        minibotAlignOut.set(minibotAlignOutValue);
        minibotAlignIn.set(!minibotAlignOutValue);
        gripperRoller.set(gripperRollerValue);
        armBrake.set(armBrakeValue);
        if(armMotorValue == 0)
        {
            armBrake.set(true);
        }
        else
        {
            armBrake.set(false);
        }

    }
    public static double getGripperRollerValue()
    {
        return gripperRollerValue;
    }
    public static void setGripperRollerValue(double gripperRollerValue)
    {
        RobotIO.gripperRollerValue = gripperRollerValue;
    }
    public static double getLeftDrive()
    {
        return leftDriveValue;
    }
    public static double getRightDrive()
    {
        return rightDriveValue;
    }

    public static boolean getCompressorState()
    {
        return compressorOn;
    }

    public static void setLeftDrive(double value)
    {
        leftDriveValue = value;
    }

    public static void setRightDrive(double value)
    {
        rightDriveValue = value;
    }


     public static boolean getMinibotAlignOut() {
        return minibotAlignOutValue;
    }

    public static void setMinibotAlignOut(boolean t) {

        minibotAlignOutValue = t;
    }

    public static boolean getMinibotDeployOut() {
        return minibotDeployOutValue;

    }

    public static void setMinibotDeployOut(boolean v,boolean u) {
        if(v && u)
            minibotDeployOutValue = true;
        else if(!v)
            minibotDeployOutValue = false;
    }

    public static boolean getMinibotAlignedSwitch()
    {
        return minibotAlignedLimit.get();
    }
    
    /*Returns the distance from the ultrasonic sensor in inch scale by inches.
     * 
     */
    public static double getFrontDistance()
    {
        return (ultraSonicValue / 2);
    }

    public static double getFrontDistanceAvg()
    {
       if(myAvg1st){
         for (myItr = 0; myItr < arraySize; myItr++) {
          // avgArray[myItr] = ultraSonicValue;
           avgArray[myItr] = 436;
          }
          myAvg1st = false;
       }
       else {
          avgArray[myPtr] = ultraSonicValue;
       }

       if (myPtr < (arraySize - 1)){
          myPtr++;
       }
       else {
          myPtr = 0;
       }

       // Sum up the values in the array
       mySum = 0;
       for (myItr = 0; myItr < arraySize; myItr++) {
          mySum  = mySum + avgArray[myItr];
       }

       return (mySum / arraySize / 2);
    }

    /*
     public static double getGripperValue()
    {
        return gripperMotorValue;
    }

    public static void setGripperValue(double gripperMotorValue)
    {
        RobotIO.gripperMotorValue = gripperMotorValue;
     }
     */

}


