// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;


public class ExampleSubsystem extends SubsystemBase {
  
  Servo exampleServo = new Servo(0);

  AddressableLED led = new AddressableLED(1); 

  AddressableLEDBuffer ledBuffer = new AddressableLEDBuffer(5);

  XboxController controller = new XboxController(0);

  public DoublePublisher leftXStick;

  

  public ExampleSubsystem() {
    led.setLength(ledBuffer.getLength());

    led.setData(ledBuffer);
    led.start();
  }

  
  public Command servoRight() {
    return new FunctionalCommand(

      //INIT - CALLED ONCE AT START
      () -> {},

      //EXEC - CALLED AT LEAST ONCE.
      () -> {
        exampleServo.set(0.0);
      },

      //INTERRUPT
      interrupted-> {

      },

      //Exit condition
      () -> false,

    this);
   // return new InstantCommand(() -> {exampleServo.set(0.0);}, this);
  }

  public Command servoLeft() {
    return new FunctionalCommand(

      () -> {
        exampleServo.set(1.0);
      }, 

      () -> {}, 

      interrupted -> {
        
      }, 

      () -> false, 

      this);
    // return new InstantCommand(() -> {exampleServo.set(1.0);}, this);
  }

  public double controllerXDirection() {
    return (1 - ((controller.getLeftX() + 1) / 2));
  }

  public boolean controllerXDirectionBoolRed() {
    return controllerXDirection() > 0.5;
  }

  public boolean controllerXDirectionBoolGreen() {
    return controllerXDirection() < 0.5;
  }
  
  public void setRed(long redBrightness) {
    for (var i = 0; i < ledBuffer.getLength(); i++) {
        // Sets the specified LED to the RGB values for red
          
          ledBuffer.setHSV(i, 0, 255, (int) ((redBrightness) - 5) * 2);

          led.setData(ledBuffer);
    } 
  }

  public void setGreen(long greenBrightness) {
    for (var i = 0; i < ledBuffer.getLength(); i++) {
        // Sets the specified LED to the RGB values for green
          
          ledBuffer.setHSV(i, 60, 255, (int) ((greenBrightness) - 5) * 2);

          led.setData(ledBuffer);
    } 
  }

  public void setLEDOff() {
    for (var i = 0; i < ledBuffer.getLength(); i++) {
        // Sets the specified LED to the RGB values for red
        
          ledBuffer.setHSV(i, 0, 255, 0);

          led.setData(ledBuffer);
        } 
  }

  public void redFrame1() {
    ledBuffer.setHSV(0, 0, 255, 255);
    ledBuffer.setHSV(1, 0, 255, 255);
    ledBuffer.setHSV(2, 0, 255, 255);

  }

  public void redFrame2() {
    ledBuffer.setHSV(1, 0, 255, 255);
    ledBuffer.setHSV(2, 0, 255, 255);
    ledBuffer.setHSV(3, 0, 255, 255);

  }

  public void redFrame3() {
    ledBuffer.setHSV(2, 0, 255, 255);
    ledBuffer.setHSV(3, 0, 255, 255);
    ledBuffer.setHSV(4, 0, 255, 255);

  }

  public void redFrame4() {
    ledBuffer.setHSV(3, 0, 255, 255);
    ledBuffer.setHSV(4, 0, 255, 255);
    ledBuffer.setHSV(0, 0, 255, 255);

  }

  public void redFrame5() {
    ledBuffer.setHSV(4, 0, 255, 255);
    ledBuffer.setHSV(0, 0, 255, 255);
    ledBuffer.setHSV(1, 0, 255, 255);

  }

  public Command servoShiftRight() {
    return new FunctionalCommand(
      () -> {

      },

      () -> {

          redFrame1();
          redFrame2();
          redFrame3();
          redFrame4();
          redFrame5();
        },

      interrupted -> {

      },

      () -> false,

    this);
  
  };

  public void servoRGB() {
    

    long redBrightness = Math.round(((128 * controllerXDirection())));
    long greenBrightness = Math.round(128 * (0.6 - controllerXDirection()));

    //System.out.println(redBrightness);
    //System.out.println(greenBrightness);

    if (Math.round(controller.getLeftX() * 10) != 0) {
      exampleServo.set(controllerXDirection());
      //System.out.println((1 - ((controller.getLeftX() + 1) / 2)) * 10);


      if (controllerXDirectionBoolGreen()) {
        //System.out.println(System.out.format("Green Brightness: %d", greenBrightness));
        setGreen(greenBrightness);
          
      }

      if (controllerXDirectionBoolRed()) {
        //System.out.println(System.out.format("Red Brightness: %d", redBrightness));
        setRed(redBrightness);
      }
     
            
        //set to some color when not moving X
        //higher range where it goes black when not moving
        //moving leds
        //git
        //clean up code
        
    } else {
        exampleServo.set(0.475);
        setLEDOff();
        
    }
  }

  public Command servoStick() { 
    return new FunctionalCommand(
      () -> {

      },

      () -> {

        //check to see if method works..
          servoRGB();
        },

      interrupted -> {

      },

      () -> false,

    this);
  }

  public Command servoOff() {
    // return new FunctionalCommand(
      
    //   () -> {
    //     exampleServo.set(0.475);
    //   }, 
      
    //   () -> {}, 
      
    //   interrupted -> {}, 
      
    //   () -> false, 
      
    // this);

    return new InstantCommand(() -> {exampleServo.set(0.475);}, this);
  }

  public Command yellow_color() {
    //return new FunctionalCommand(

      
    //   () -> {

    //   },

    //   () -> {

    //   },

    //   interrupted -> {

    //   },

    //   () -> false,

    // this);

    return new InstantCommand(() -> {
      for (var i = 0; i < ledBuffer.getLength(); i++) {
        // Sets the specified LED to the RGB values for red
        ledBuffer.setRGB(i, 255, 255, 0);
     }
     
      led.setData(ledBuffer);
    });
  }
 
  private boolean end()
  {
    exampleServo.set(0.475);
    return true;
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
