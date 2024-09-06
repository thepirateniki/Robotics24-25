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
  
  int[] LEDShift = {1, 1, 1, 0, 0};
  long counter = 0;
  
  
  
  public ExampleSubsystem() {
    led.setLength(ledBuffer.getLength());
    
    led.setData(ledBuffer);
    led.start();
  }
  
  
  public double controllerXDirection() {
    return (1 - ((controller.getLeftX() + 1) / 2));
  }

  public double controllerRightXDirection() {
    return (1 - ((controller.getRightX() + 1) / 2));
  }


  
  public boolean controllerXDirectionBoolRed() {
    return controllerXDirection() > 0.5;
  }
  
  public boolean controllerXDirectionBoolGreen() {
    return controllerXDirection() < 0.5;
  }

  public boolean controllerRightXDirectionBoolRed() {
    return controllerRightXDirection() > 0.5;
  }
  
  public boolean controllerRightXDirectionBoolGreen() {
    return controllerRightXDirection() < 0.5;
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
  //choose between different modes based on what is called
  //right x controls one mode, left x controls other
  //speed of joystick controls speed of the new mode
  public void servoRGB() {
    
    
    long redBrightness = Math.round(((128 * controllerXDirection())));
    long greenBrightness = Math.round(128 * (0.6 - controllerXDirection()));
    
    //System.out.println(redBrightness);
    //System.out.println(greenBrightness);
    if (Math.round(controller.getLeftX() * 10) != 0 || Math.round(controller.getRightX() * 10) != 0) {
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
          
          //moving leds that move in the direction of the stick, while moving the servo, 
          //while moving the servo and controlling brightness at variable speed

          //clean up code
        
        
      }

      if (Math.round(controller.getRightX() * 10) != 0) {
        exampleServo.set(controllerRightXDirection());
        //System.out.println((1 - ((controller.getLeftX() + 1) / 2)) * 10);
        
        if (controllerRightXDirectionBoolGreen()) {
          //System.out.println(System.out.format("Green Brightness: %d", greenBrightness));
          LEDShiftIterateRight();
            
            
        }
          
        if (controllerRightXDirectionBoolRed()) {
          //System.out.println(System.out.format("Red Brightness: %d", redBrightness));
          LEDShiftIterateLeft();
            
        }
          
          //moving leds that move in the direction of the stick, while moving the servo, 
          //while moving the servo and controlling brightness at variable speed

          //clean up code
        
        
      } 
    } else {
      
      exampleServo.set(0.475);
      setLEDOff();
        
    }
      
  }
  
  public int[] LEDShiftRight(int[] array) {
    
    int[] newNumbers = {0,0,0,0,0};

    int first = array[0];
    
    for (int i = 1; i < array.length; i++) {
      newNumbers[i-1] = array[i];
    }
    //{1, 1, 1, 0, 0}
    //{0, 1, 1, 1, 0}
    newNumbers[array.length - 1] = first;    

    return newNumbers;
  }

  public int[] LEDShiftLeft(int[] array) {
    int[] newNumbers = {0,0,0,0,0};
    
    int last = array[array.length - 1];
    
    for (int i = 0; i < array.length-1; i++) {
      newNumbers[i+1] = array[i];
    }

    //{0, 1, 1, 1, 0}
    //{0, 0, 1, 1, 1}
    //{1, 0, 0, 1, 1}
    newNumbers[0] = last;    

    return newNumbers;
  }
  
  // MAIN ENTRY
  public void LEDShiftIterateRight() {

    
    if ((counter % 10) == 0) {
      LEDShift = LEDShiftRight(LEDShift);

      for (int i = 0; i < LEDShift.length; i++) {
        if (LEDShift[i] == 1) {
          ledBuffer.setHSV(i, 0, 255, 255);
        } else {
          ledBuffer.setHSV(i, 0, 255, 0);
        }
        
        
        
       
          
          //System.out.println(counter);
        if (counter > 1000) { 
          counter = 0;  
        }
      }
    }

      counter += 1;
    
      led.setData(ledBuffer);
  }
  
  public void LEDShiftIterateLeft() {

    
    if ((counter % 10) == 0) {
      LEDShift = LEDShiftLeft(LEDShift);

      for (int i = 0; i < LEDShift.length; i++) {
        if (LEDShift[i] == 1) {
          ledBuffer.setHSV(i, 0, 255, 255);
        } else {
          ledBuffer.setHSV(i, 0, 255, 0);
        }
        
        
        
       
          
          //System.out.println(counter);
        if (counter > 1000) { 
          counter = 0;  
        }
      }
    }

      counter += 1;
    
      led.setData(ledBuffer);
  }
  
  
  public Command servoStick() { 
    return new FunctionalCommand(
    () -> {
      
    },
    
    () -> {
      
      //check to see if method works..
      servoRGB();
      //LEDShiftIterateRight();
      //LEDShiftIterateLeft();
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

        }
