/**
 * Created by Kevin on 12/26/2015.
 * Contains main method, also contains test cases
 */

public class CompassCalibration {
    public static void main(String[] args) {

        //initialize calibration GUI, comment out if using test cases
        new CalibrationGUI();


        //test case w/o compass values
        /*CalibrationGUI calibrationGUI = new CalibrationGUI();

        EllipseInformation ellipseInformation = calibrationGUI.ellipseCharacteristicCalculation(
                calibrationGUI.ellipseSolver(
                        calibrationGUI.pointInputFromCSV("C:/Users/Kevin/Documents/Octave/compass/data.txt")));

        ellipseInformation.print();

        //test case w/ compass values
        EllipseInformation ellipseInformation1 = calibrationGUI.ellipseCharacteristicCalculation(
                calibrationGUI.ellipseSolver(
                        calibrationGUI.pointInputFromCSV("C:/Users/Kevin/Documents/Octave/compass/data2.txt")));

        ellipseInformation1.print();*/
    }


}

