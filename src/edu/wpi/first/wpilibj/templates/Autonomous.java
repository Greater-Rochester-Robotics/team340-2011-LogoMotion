/*
e * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author tyler
 */
public class Autonomous {
    RobotIO rIO;
    HumanIO hIO;
    Gripper myGripper;
    BaseDrive myBD;
    RobotArm myArm;
    Timer time=new Timer();
    private int stage;
    // state constants
    private static final int Idle             = 0;
    private static final int FullFwd          = 1;
    private static final int TiltArm          = 2;
    private static final int HalfFwd          = 3;
    private static final int HalfFwd2          = 4;
    private static final int LeftDriveAuto    = 5;
    private static final int RightDriveAuto   = 6;
    private static final int MoveElvUp        = 7;
    private static final int FindLineValue    = 8;
    private static final int MoveFwdHalfFinal = 9;
    private static final int MoveElvDwn       = 10;
    private static final int RlsGrpr          = 11;
    private static final int MoveBack         = 12;
    private static final int Stop             = 13;
    private static final int getUltraSonicVal = 14;
    private static final int MoveToGround     = 16;

   // base drive constants
    private static final double SpeedFull =  -1.0;
    private static final double SpeedHalf =  -0.25;
    private static final double SpeedThird=- 0.3;
    private static final double Forward   =  1.0;
    private static final double Reverse   = -1.0;
    private static final double Left      =  1.0;
    private static final double Right     =  1.0;

    // state times
    private static final double FFtime      = 0.5;
    private static final double FHtime      = 2.0;
    private static final double FHtime2     = 1.0;
    private static final double FHtimeleft  = 1.0;
    private static final double FHtimeright = 1.0;
    private static final double RHtime      = 2.0;


    public static int autoState;

    /**
     *
     * @param lclrIO
     * @param lclhIO
     */

    public Autonomous(RobotIO lclrIO,BaseDrive lclmyBD,RobotArm lclmyArm, Gripper lclgripper){
    
     stage=1;
     autoState  = Idle;
     this.myBD  = lclmyBD;
     this.rIO   = lclrIO;
     this.myArm = lclmyArm;
     this.myGripper = lclgripper;

    }
    
    public void Autonomous2(boolean t){
        rIO.retrieveValues();
        //System.out.println("auto called");
       if (t) {
            switch (autoState) {
                case Idle:
                    System.out.println("In Idle");
                    autoState = HalfFwd;
                    rIO.changeGears(false);
                    break;
                case HalfFwd:
                    System.out.println("Half Forward");
                    // 2011-03-04 10:15 Changed dist from 80" to 65" 2011-3-17 ghanged back to timed go

                    //TODO: Autonomous drive correction here
                    if (myBD.TimedGo(-0.25, 0, 5.2)) {//2011-3-18 8:57 changed the the time from 5.0 seconds to 5.2 for turnning isuse
                        System.out.println("Going to tilt arm");
                        autoState = TiltArm;
                    }
                    break;
                case TiltArm:
                    System.out.println("Tilting arm");
                    rIO.setArmTilt(false);
                    System.out.println("going to HalfFwd");
                    autoState = HalfFwd2;
                    break;
                case HalfFwd2:
                    System.out.println("Forward half");
                    // 2011-03-04 10:15
                    // Changed speed from 0.17 to 0.15
                    // Changed dist from 42" to 45"
                    if (myBD.TimedDistGo(-0.25, 0, 10.0, 53)) {
                        //2011-3-18 changed speed bact to -0.17
                        autoState = MoveToGround;
                        System.out.println("go to MoveToGround");
                    }
                    break;
                 case MoveToGround:
                    myArm.goDown();
                    System.out.println("going to ground");
                    if(rIO.getBottomLimitValue() == false){
                        rIO.armBannerSensorEncoder.reset();
                        autoState = MoveElvUp;
                        System.out.println("Reached ground");
                    }
                    break;
                case MoveElvUp:
                    System.out.println("move elevator up");
                    myArm.goToPos(hIO.TOP_VAL);
                    
                    myGripper.spinTubeUpSlow();
                    System.out.println("Encoder Value = " + rIO.getArmEncoder());
                    if (myArm.TimedGoUp(3.0)){//rIO.getArmEncoder() == hIO.TOP_VAL) {
                        autoState = RlsGrpr;
                        //myGripper.spinTubeOutAuto();
                        System.out.println("RlsGrpr");
                    }
                    break;
                case RlsGrpr:
                    System.out.println("Release The Kracken!");
                    if (myGripper.spinTubeOutAuto()) {
                        autoState = MoveBack;
                        System.out.println("Going to move arm down");
                    }
                    break;
                case MoveBack:
                    System.out.println("Backing up");
                    if (myBD.TimedGo(0.25, 0, 1)) {
                        autoState = MoveElvDwn;
                        System.out.println("Prepare to stop");
                    }
                    break;
                case MoveElvDwn:
                    //System.out.println("Moving Arm Down");
                    //myArm.goToPos(hIO.HUMAN_VAL);
                    
                    if (myArm.goToGround()){//rIO.getArmEncoder() == hIO.HUMAN_VAL) {
                        autoState = Stop;
                        System.out.println("Going to Stop");
                    }
                    break;

                case Stop:
                    System.out.println("STOP!");
                    break;
                default:
                    autoState = Stop;
                    break;
            }
        }

          rIO.setValues();
        
    }
}
/*   public void SmartAuto(){
        rIO.retrieveValues();
        switch ( autoState ){
            case Idle:
                System.out.println("In Idle");
                autoState = FullFwd;
                break;
            case FullFwd:
                System.out.println("Full Forward");
                if(myBD.TimedGo(SpeedFull , Forward , FFtime) ){
                System.out.println("Going to tilt arm");
                    autoState = TiltArm;
                }
                break;
            case TiltArm:
                 System.out.println("Tilting arm");
                rIO.setArmTilt(true);
                autoState = HalfFwd;
                 System.out.println("going to Half Forward");
                break;
            case HalfFwd:
                 System.out.println("Forward half");
                if( myBD.TimedGo(SpeedHalf, Forward, FHtime)){
                    autoState = FindLineValue;
                     System.out.println("Is It Found?");
                rIO.getCenterLineSensor();
                if(true)//continue to move arm up
                    autoState = MoveElvUp;
                    System.out.println("Line Found Go To Move Arm Up");
                }
                 break;
            case MoveElvUp:
                 System.out.println("move elevator up");
                if(myArm.goToPos(hIO.TOP_VAL)){
                    autoState = MoveFwdHalfFinal;
                    System.out.println("Go to MoveFwdHalf");
                }
                 break;
            case MoveFwdHalfFinal:
                 System.out.println(" Go Forward half");
                if( myBD.TimedGo(SpeedHalf, Forward, FHtime)){
                   autoState = getUltraSonicVal;
                   System.out.println("going to get UltraSonicVal");
                }
                 break;
            case getUltraSonicVal:
                System.out.println("searching for Distance");
                if(rIO.getFrontDistance() > 30){
                    autoState = RlsGrpr; 
                    System.out.println("Going to RlsGrpr");
                }

                break;
            case RlsGrpr:
                 System.out.println("Release The Kracken!");
                  if(myGripper.spinTubeOutAuto){
                    autoState = MoveBack;
                     System.out.println("Getting ready to move back");
                }
 *                break;
            case MoveElvDwn:
                System.out.println("Moving Arm Down");
                myArm.goToPos(hIO.HUMAN_VAL);
                if(rIO.getArmEncoder()== hIO.HUMAN_VAL){
                    autoState = Stop;
                    System.out.println("Going to Stop");
 *              }
                break;
            case MoveBack:
                 System.out.println("Backing up");
               if( myBD.TimedGo(SpeedHalf, Reverse, RHtime)){
                    autoState = Stop;
                     System.out.println("Prepare to stop");
                }
                break;
            case Stop:
                 System.out.println("STOP!");
                break;
            default:
                autoState = Stop;
                break;
       }
    }
        public void SmartAuto2(){
            rIO.retrieveValues();
        switch ( autoState ){
            case Idle:
                System.out.println("In Idle");
                autoState = FullFwd;
                break;
            case FullFwd:
                System.out.println("Full Forward");
                if(myBD.TimedGo(SpeedFull , Forward , FFtime) ){
                System.out.println("Going to tilt arm");
                    autoState = TiltArm;
                    }
                break;
            case TiltArm:
                 System.out.println("Tilting arm");
                rIO.setArmTilt(true);
                autoState = HalfFwd;
                 System.out.println("going to Half Forward");
                break;
            case HalfFwd:
                 System.out.println("Forward half");
                if( myBD.TimedGo(SpeedHalf, Forward, FHtime)){
                    autoState = LeftDriveAuto;
                    System.out.println("Going to Forward Quarter");
                }
                 break;
            case LeftDriveAuto:
                System.out.println("Turning left");
                if(myBD.TimedGo(SpeedThird , Left , FHtimeleft) ){
                    autoState = HalfFwd;
                    System.out.println(" Going to Half Forward Final ");
                }
                break;
            case MoveFwdHalfFinal:
                System.out.println("Foward half final");
                if(myBD.TimedGo(SpeedHalf , Forward , FHtime2) ){
                    autoState = RightDriveAuto;
                }
                break;
            case RightDriveAuto:
                System.out.println("Turning right");
                if(myBD.TimedGo(SpeedThird, Right, FHtimeright)){
                    autoState = RlsGrpr;
                }
                 break;
            case getUltraSonicVal:
                System.out.println("searching for Distance");
                if(rIO.getFrontDistance() > 30){
                    autoState = RlsGrpr;
                    System.out.println("Going to RlsGrpr");
                }
                break;
            case RlsGrpr:
                System.out.println("Release The Kracken!");
                  if(myGripper.spinTubeOutAuto){
                   autoState = MoveBack;
                     System.out.println("Getting ready to move back");
                }
                break;
            case MoveBack:
                 System.out.println("Backing up");
               if( myBD.TimedGo(SpeedHalf, Reverse, RHtime)){
                    autoState = Stop;
                     System.out.println("Prepare to stop");
                }
                break;
            case Stop:
                 System.out.println("STOP!");
                break;
            default:
                autoState = Stop;
                break;
               }

    }public void Autonomous2(boolean t){
        rIO.retrieveValues();
        //System.out.println("auto called");
       if (t) {
            switch (autoState) {
                case Idle:
                    System.out.println("In Idle");
                    autoState = HalfFwd;
                    rIO.changeGears(false);
                    break;
                case HalfFwd:
                    System.out.println("Half Forward");
                    // 2011-03-04 10:15 Changed dist from 80" to 65"
                    if (myBD.TimedDistGo(-0.25, 0, 5.0, 65)) {
                        System.out.println("Going to tilt arm");
                        autoState = TiltArm;
                    }
                    break;
                case TiltArm:
                    System.out.println("Tilting arm");
                    rIO.setArmTilt(false);
                    System.out.println("going to HalfFwd");
                    autoState = HalfFwd2;
                    break;
                case HalfFwd2:
                    System.out.println("Forward half");
                    // 2011-03-04 10:15
                    // Changed speed from 0.17 to 0.15
                    // Changed dist from 42" to 45"
                    if (myBD.TimedDistGo(-0.15, 0, 10.0, 45)) {
                        autoState = MoveElvUp;
                        System.out.println("go to Elevator Up");
                    }
                    break;
                case MoveElvUp:
                    System.out.println("move elevator up");
                    myArm.goToPos(hIO.TOP_VAL);
                    myGripper.spinTubeUpSlow();
                    if (rIO.getArmEncoder() == hIO.TOP_VAL) {
                        autoState = RlsGrpr;
                        //myGripper.spinTubeOutAuto();
                        System.out.println("RlsGrpr");
                    }
                    break;
                case RlsGrpr:
                    System.out.println("Release The Kracken!");
                    if (myGripper.spinTubeOutAuto()) {
                        autoState = MoveBack;
                        System.out.println("Going to move arm down");
                    }
                    break;
                case MoveBack:
                    System.out.println("Backing up");
                    if (myBD.TimedGo(0.25, 0, 1)) {
                        autoState = MoveElvDwn;
                        System.out.println("Prepare to stop");
                    }
                    break;
                case MoveElvDwn:
                    System.out.println("Moving Arm Down");
                    myArm.goToPos(hIO.HUMAN_VAL);
                    if (rIO.getArmEncoder() == hIO.HUMAN_VAL) {
                        autoState = Stop;
                        System.out.println("Going to Stop");
                    }
                    break;

                case Stop:
                    System.out.println("STOP!");
                    break;
                default:
                    autoState = Stop;
                    break;
            }
        }

          rIO.setValues();
        }
    }
 */

  
  