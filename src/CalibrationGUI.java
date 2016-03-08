/**
 * Created by Kevin on 12/27/2015.
 * Builds GUI and does the math for for the calibration program.
 * Uses math from http://www.mathworks.com/matlabcentral/fileexchange/45356-fitting-quadratic-curves-and-surfaces/
 * and http://www.mathworks.com/matlabcentral/fileexchange/24693-ellipsoid-fit to calculate the fitted
 * ellipse and its characteristics.
 */
import Jama.CholeskyDecomposition;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import gnu.io.CommPortIdentifier;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;

public class CalibrationGUI extends JFrame implements ActionListener, WindowListener, ListSelectionListener, SerialEventInterface{

    private TextField txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9;
    private TextField txt10, txt11, txt12, txt13, txt14, txt15, txt16, txt17, txt18, txt19, txt20;
    private TextField txt21, txt22, txt23, txt24, txt25, txt26, txt27;
    private TextField centerText, radiiText;

    private TextArea rotationMatrix;

    private JList comPortList;

    private String[] comPorts;

    private ArrayList<double[]> data;

    private Serial serial;

    public CalibrationGUI(){
        setLayout(new FlowLayout());
        setResizable(false);

        JPanel jPanel1 = new JPanel(new FlowLayout());
        Label lblPt1 = new Label("Point 1");
        jPanel1.add(lblPt1);

        txt1 = new TextField(6);
        txt1.setEditable(true);
        jPanel1.add(txt1);

        txt2 = new TextField(6);
        txt2.setEditable(true);
        jPanel1.add(txt2);

        txt3 = new TextField(6);
        txt3.setEditable(true);
        jPanel1.add(txt3);

        JPanel jPanel2 = new JPanel(new FlowLayout());
        Label lblPt2 = new Label("Point 2");
        jPanel2.add(lblPt2);

        txt4 = new TextField(6);
        txt4.setEditable(true);
        jPanel2.add(txt4);

        txt5 = new TextField(6);
        txt5.setEditable(true);
        jPanel2.add(txt5);

        txt6 = new TextField(6);
        txt6.setEditable(true);
        jPanel2.add(txt6);

        JPanel jPanel3 = new JPanel(new FlowLayout());
        Label lblPt3 = new Label("Point 3");
        jPanel3.add(lblPt3);

        txt7 = new TextField(6);
        txt7.setEditable(true);
        jPanel3.add(txt7);

        txt8 = new TextField(6);
        txt8.setEditable(true);
        jPanel3.add(txt8);

        txt9 = new TextField(6);
        txt9.setEditable(true);
        jPanel3.add(txt9);

        JPanel jPanel4 = new JPanel(new FlowLayout());
        Label lblPt4 = new Label("Point 4");
        jPanel4.add(lblPt4);

        txt10 = new TextField(6);
        txt10.setEditable(true);
        jPanel4.add(txt10);

        txt11 = new TextField(6);
        txt11.setEditable(true);
        jPanel4.add(txt11);

        txt12 = new TextField(6);
        txt12.setEditable(true);
        jPanel4.add(txt12);

        JPanel jPanel5 = new JPanel(new FlowLayout());
        Label lblPt5 = new Label("Point 5");
        jPanel5.add(lblPt5);

        txt13 = new TextField(6);
        txt13.setEditable(true);
        jPanel5.add(txt13);

        txt14 = new TextField(6);
        txt14.setEditable(true);
        jPanel5.add(txt14);

        txt15 = new TextField(6);
        txt15.setEditable(true);
        jPanel5.add(txt15);

        JPanel jPanel6 = new JPanel(new FlowLayout());
        Label lblPt6 = new Label("Point 6");
        jPanel6.add(lblPt6);

        txt16 = new TextField(6);
        txt16.setEditable(true);
        jPanel6.add(txt16);

        txt17 = new TextField(6);
        txt17.setEditable(true);
        jPanel6. add(txt17);

        txt18 = new TextField(6);
        txt18.setEditable(true);
        jPanel6.add(txt18);

        JPanel jPanel7 = new JPanel(new FlowLayout());
        Label lblPt7 = new Label("Point 7");
        jPanel7.add(lblPt7);

        txt19 = new TextField(6);
        txt19.setEditable(true);
        jPanel7.add(txt19);

        txt20 = new TextField(6);
        txt20.setEditable(true);
        jPanel7.add(txt20);

        txt21 = new TextField(6);
        txt21.setEditable(true);
        jPanel7.add(txt21);

        JPanel jPanel8 = new JPanel(new FlowLayout());
        Label lblPt8 = new Label("Point 8");
        jPanel8.add(lblPt8);

        txt22 = new TextField(6);
        txt22.setEditable(true);
        jPanel8.add(txt22);

        txt23 = new TextField(6);
        txt23.setEditable(true);
        jPanel8.add(txt23);

        txt24 = new TextField(6);
        txt24.setEditable(true);
        jPanel8.add(txt24);

        JPanel jPanel9 = new JPanel(new FlowLayout());
        Label lblPt9 = new Label("Point 9");
        jPanel9.add(lblPt9);

        txt25 = new TextField(6);
        txt25.setEditable(true);
        jPanel9.add(txt25);

        txt26 = new TextField(6);
        txt26.setEditable(true);
        jPanel9.add(txt26);

        txt27 = new TextField(6);
        txt27.setEditable(true);
        jPanel9.add(txt27);

        JPanel jPanelSubmit = new JPanel(new FlowLayout());

        Button submitButton = new Button("Submit Points");
        jPanelSubmit.add(submitButton);

        submitButton.addActionListener(this);
        submitButton.setActionCommand("text input");

        Button teensyButton = new Button("Teensy Input");
        jPanelSubmit.add(teensyButton);

        teensyButton.addActionListener(this);
        teensyButton.setActionCommand("teensy input");

        JPanel jPanelList = new JPanel(new FlowLayout());

        comPorts = getComPorts();

        comPortList = new JList(comPorts);

        comPortList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        comPortList.setLayoutOrientation(JList.VERTICAL);
        comPortList.setVisibleRowCount(-1);
        comPortList.setVisible(true);

        JScrollPane listScroller = new JScrollPane(comPortList);
        listScroller.setPreferredSize(new Dimension(100,100));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        jPanelList.add(listScroller);

        setTitle("Compass Calibrator");
        setSize(300, 1000);

        setVisible(true);

        JPanel jPanelCenter = new JPanel(new FlowLayout());
        Label centerLabel = new Label("Center");
        jPanelCenter.add(centerLabel);

        centerText = new TextField(15);
        centerText.setEditable(false);
        jPanelCenter.add(centerText);

        JPanel jPanelRadii = new JPanel(new FlowLayout());
        Label radiiLabel = new Label("Radii");
        jPanelRadii.add(radiiLabel);

        radiiText = new TextField(20);
        radiiText.setEditable(false);
        jPanelRadii.add(radiiText);

        JPanel jPanelRotation = new JPanel(new FlowLayout());
        Label rotationLabel = new Label("Rotation Matrix");
        jPanelRotation.add(rotationLabel);

        rotationMatrix = new TextArea(3, 17);
        rotationMatrix.setEditable(false);
        jPanelRotation.add(rotationMatrix);

        add(jPanel1);
        add(jPanel2);
        add(jPanel3);
        add(jPanel4);
        add(jPanel5);
        add(jPanel6);
        add(jPanel7);
        add(jPanel8);
        add(jPanel9);
        add(jPanelSubmit);
        add(jPanelList);
        add(jPanelCenter);
        add(jPanelRadii);
        add(jPanelRotation);

        data = new ArrayList<>();


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt){
        //send entered values here
        if(evt.getActionCommand().equals("text input")) {
            double[][] inputPoints = new double[3][9];
            try {
                inputPoints[0][0] = Double.valueOf(txt1.getText());
                inputPoints[0][1] = Double.valueOf(txt4.getText());
                inputPoints[0][2] = Double.valueOf(txt7.getText());
                inputPoints[0][3] = Double.valueOf(txt10.getText());
                inputPoints[0][4] = Double.valueOf(txt13.getText());
                inputPoints[0][5] = Double.valueOf(txt16.getText());
                inputPoints[0][6] = Double.valueOf(txt19.getText());
                inputPoints[0][7] = Double.valueOf(txt22.getText());
                inputPoints[0][8] = Double.valueOf(txt25.getText());
                inputPoints[1][0] = Double.valueOf(txt2.getText());
                inputPoints[1][1] = Double.valueOf(txt5.getText());
                inputPoints[1][2] = Double.valueOf(txt8.getText());
                inputPoints[1][3] = Double.valueOf(txt11.getText());
                inputPoints[1][4] = Double.valueOf(txt14.getText());
                inputPoints[1][5] = Double.valueOf(txt17.getText());
                inputPoints[1][6] = Double.valueOf(txt20.getText());
                inputPoints[1][7] = Double.valueOf(txt23.getText());
                inputPoints[1][8] = Double.valueOf(txt26.getText());
                inputPoints[2][0] = Double.valueOf(txt3.getText());
                inputPoints[2][1] = Double.valueOf(txt6.getText());
                inputPoints[2][2] = Double.valueOf(txt9.getText());
                inputPoints[2][3] = Double.valueOf(txt12.getText());
                inputPoints[2][4] = Double.valueOf(txt15.getText());
                inputPoints[2][5] = Double.valueOf(txt18.getText());
                inputPoints[2][6] = Double.valueOf(txt21.getText());
                inputPoints[2][7] = Double.valueOf(txt24.getText());
                inputPoints[2][8] = Double.valueOf(txt27.getText());
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(this, "One or more input fields are blank",
                        "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }


            //find an ellipse that fits the points if such an ellipse exists
            Matrix ellipseSolved = ellipseSolver(inputPoints);

            //find the center, radii, rotation matrices of that ellipse
            EllipseInformation ellipseInformation = ellipseCharacteristicCalculation(ellipseSolved);

            Matrix center = ellipseInformation.getCenter();
            Matrix radii = ellipseInformation.getRadii();
            Matrix rotation = ellipseInformation.getRotation();

            //display those matrices
            //set a rounder to round all numbers to 3 decimal places
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);

            //print center
            double[][] centerArray = center.getArray();
            String centerString = "";
            for (double[] internalArray : centerArray) {
                for (double coordinate : internalArray) {
                    centerString = centerString + String.valueOf(df.format(coordinate)) + " ";
                }
            }

            centerText.setText(centerString);

            System.out.println("Radii: x y z");
            radii.print(1, 3);

            double[][] radiiArray = radii.getArray();
            String radiiString = "";
            for (double[] internalArray : radiiArray) {
                for (double coordinate : internalArray) {
                    radiiString = radiiString + String.valueOf(df.format(coordinate)) + " ";
                }
            }

            radiiText.setText(radiiString);

            //rotation matrix: derotated point = rotation * i
            System.out.println("Rotation matrix: ");
            rotation.print(1, 5);

            double[][] rotationArray = rotation.getArray();
            String rotationString = "";
            for (double[] internalArray : rotationArray) {
                for (double coordinate : internalArray) {
                    rotationString = rotationString + String.valueOf(df.format(coordinate)) + " ";
                }
                rotationString = rotationString + "\n";
            }

            rotationMatrix.setText(rotationString);
        } else {
            try {
                pointInputFromTeensy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //REQUIRES: nx3 array, column one is x, column two is y, column three is z
    //EFFECTS: returns an 1x8 vector representing an ellipse in the form
    //Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
    public Matrix ellipseSolver(double[][] pointArray){
        if(pointArray.length != 3){
            JOptionPane.showMessageDialog(this, "Improper points inputted - not in xyz format",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: Points not in xyz format");
        }

        if(pointArray[0].length < 9){
            JOptionPane.showMessageDialog(this, "Need at least 9 points to calculate a good ellipse",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: need at least 9 points for ellipse calculation");
        }

        double[][] ellipsoidMatrix = new double[9][pointArray[0].length];

        //fitting ellipsoid in form Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
        for(int i = 0; i < pointArray[0].length; i++){
            ellipsoidMatrix[0][i] = pointArray[0][i] * pointArray[0][i];
            ellipsoidMatrix[1][i] = pointArray[1][i] * pointArray[1][i];
            ellipsoidMatrix[2][i] = pointArray[2][i] * pointArray[2][i];
            ellipsoidMatrix[3][i] = 2 * pointArray[0][i] * pointArray[1][i];
            ellipsoidMatrix[4][i] = 2 * pointArray[0][i] * pointArray[2][i];
            ellipsoidMatrix[5][i] = 2 * pointArray[1][i] * pointArray[2][i];
            ellipsoidMatrix[6][i] = 2 * pointArray[0][i];
            ellipsoidMatrix[7][i] = 2 * pointArray[1][i];
            ellipsoidMatrix[8][i] = 2 * pointArray[2][i];
        }

        Matrix ellipse = new Matrix(ellipsoidMatrix);

        ellipse = ellipse.transpose();


        //solve normal system of equations
        //given ellipsoidMatrix = D, solution = v: v = (D' * D)^-1 * (D' * (1xLengthOfD matrix of ones))
        Matrix flat = new Matrix(ellipsoidMatrix[0].length, 1, 1);


        Matrix ellipseSolved = new Matrix(0,0);

        try {
            ellipseSolved = ellipse.transpose().times(ellipse).inverse().times(ellipse.transpose().times(flat));
        } catch(RuntimeException e){
            JOptionPane.showMessageDialog(this, "Points do not define a real ellipse - check the inputs",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return ellipseSolved;
    }

    //REQUIRES: 1x8 matrix in the form Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
    //EFFECTS: returns object containing center, radii, and rotation of ellipse
    public EllipseInformation ellipseCharacteristicCalculation(Matrix ellipse){

        //create algebraic form of ellipsoid (quadratic form matrix, symmetric)
        //from equation Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
        //[ D(1) D(3) D(5) D(7) ]
        //[ D(4) D(2) D(6) D(8) ]
        //[ D(5) D(6) D(3) D(9) ]
        //[ D(7) D(8) D(9)  -1  ]
        Matrix ellipseQuadratic = new Matrix(4,4);
        ellipseQuadratic.set(0,0,ellipse.get(0,0));
        ellipseQuadratic.set(0,1,ellipse.get(3,0));
        ellipseQuadratic.set(0,2,ellipse.get(4,0));
        ellipseQuadratic.set(0,3,ellipse.get(6,0));
        ellipseQuadratic.set(1,0,ellipse.get(3,0));
        ellipseQuadratic.set(1,1,ellipse.get(1,0));
        ellipseQuadratic.set(1,2,ellipse.get(5,0));
        ellipseQuadratic.set(1,3,ellipse.get(7,0));
        ellipseQuadratic.set(2,0,ellipse.get(4,0));
        ellipseQuadratic.set(2,1,ellipse.get(5,0));
        ellipseQuadratic.set(2,2,ellipse.get(2,0));
        ellipseQuadratic.set(2,3,ellipse.get(8,0));
        ellipseQuadratic.set(3,0,ellipse.get(6,0));
        ellipseQuadratic.set(3,1,ellipse.get(7,0));
        ellipseQuadratic.set(3,2,ellipse.get(8,0));
        ellipseQuadratic.set(3,3,-1);


        //find center of ellipse - use center = quad(1:3, 1:3) ^ -1 * -solved(7:9)
        //create quad(1:3, 1:3) and -solved(7:9)
        Matrix ellipseQuadraticCenter = ellipseQuadratic.getMatrix(0,2,0,2);
        Matrix ellipseSolvedCenter = ellipse.getMatrix(6,8,0,0);

        Matrix center = ellipseQuadraticCenter.inverse().times(ellipseSolvedCenter.times(-1));

        //calculate ellipse rotation

        //form translation matrix
        Matrix translationMatrix = Matrix.identity(4,4);
        for(int i = 0; i < 3; i++){
            translationMatrix.set(3, i, center.get(i, 0));
        }

        //translate ellipse to center - translated = translation * quad * translation'
        Matrix ellipseTranslated = translationMatrix.times(ellipseQuadratic).times(translationMatrix.transpose());

        //matrix for calculating eigenvalues/eigenvectors is translated(1:3, 1:3)/-translated(4,4)
        Matrix ellipseTranslatedSubmatrix = ellipseTranslated.getMatrix(0,2,0,2);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                ellipseTranslatedSubmatrix.set(i, j, ellipseTranslatedSubmatrix.get(i,j) / -ellipseTranslated.get(3,3));
            }
        }

        //check for positive definiteness - if not, is not a real ellipse and may produce negative eigenvalues
        CholeskyDecomposition ellipseChol = new CholeskyDecomposition(ellipseTranslatedSubmatrix);
        if(!ellipseChol.isSPD()){
            System.out.println("Parameters do not define a real ellipse, exiting");
            JOptionPane.showMessageDialog(this, "Points do not define a real ellipse - check the inputs",
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: Matrix is not single positive definite, check the inputs");
        }


        //calculate eigenvalues and eigenvectors for matrix
        EigenvalueDecomposition ellipseEigen = new EigenvalueDecomposition(ellipseTranslatedSubmatrix);

        //eigenvalues stored on diagonals, everything else zero
        Matrix eigenvalueMatrix = ellipseEigen.getD();
        Matrix eigenvectors = ellipseEigen.getV();


        //matrix of only eigenvalues, equivalent to diag(eigenvalueMatrix)
        Matrix eigenvalues = new Matrix(1,3);
        for(int i = 0; i < 3; i++){
            eigenvalues.set(0,i,eigenvalueMatrix.get(i,i));
        }

        //radii found using radii = sqrt(1/eigenvalues)
        Matrix radii = eigenvalues.copy();
        //create matrix of ones to find the reciprocal of everything in eigenvalues
        Matrix ones = new Matrix(1,3,1);
        //do (1/eigenvalues)
        radii.arrayLeftDivideEquals(ones);

        //do sqrt(1/eigenvalues)
        for(int i = 0; i < 3; i++){
            radii.set(0, i, Math.sqrt(radii.get(0, i)));
        }

        return new EllipseInformation(center, radii, eigenvectors);
    }

    //REQUIRES: filepath representing a file in CSV format
    //this CSV file can either be all accelerometer values in xyz or can also be
    //compass values in xyz followed by accelerometer values in xyz, where the
    //compass values are ignored.
    //EFFECTS: returns double[][] populated with points ready to pass to ellipseSolver()
    public double[][] pointInputFromCSV(String filepath){

        //initialize variables
        String line;
        String separator = ",";
        //use array list to support unspecified number of points
        ArrayList<double[]> pointList = new ArrayList<>();

        try {

            //read file
            BufferedReader br = new BufferedReader(new FileReader(filepath));

            while((line =  br.readLine()) != null){

                //split by comma
                String[] point = line.split(separator);
                double[] pointValue = new double[3];
                //if only accelerometer values
                if(point.length == 3) {
                    for (int i = 0; i < point.length; i++) {
                        pointValue[i] = Double.valueOf(point[i]);
                    }
                //else ignore the compass and take the accelerometer values
                } else {
                    for (int i = 3; i < point.length; i++) {
                        pointValue[i - 3] = Double.valueOf(point[i]);
                    }
                }
                pointList.add(pointValue);
            }
        } catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(this, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "IO Error", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        //convert from dynamically growing arraylist to statically sized double[][] that ellipseSolver() can use
        double[][] points = new double[3][pointList.size()];

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < pointList.size(); j++){
                points[i][j] = pointList.get(j)[i];
            }
        }

        return points;
    }

    public void pointInputFromTeensy() throws IOException{
        serial = new Serial(this);
        serial.initialize();
        Thread t = new Thread() {
            public void run(){
                //the following line will keep this app alive for 1000 seconds,
                //waiting for events to occur and responding to them (printing incoming messages to console).
                try {Thread.sleep(1000000);} catch (InterruptedException ie) {ie.printStackTrace();}
            }
        };

        t.start();
        System.out.println("started");

    }

    public void calculateEllipse(String s){
        data.addAll(stringToArrayList(s));
        System.out.println(data.size());
        if(data.size() > 20){
            serial.close();
            Matrix matrix = ellipseSolver(arrayListToDouble(data));
            EllipseInformation ellipseInformation = ellipseCharacteristicCalculation(matrix);
            ellipseInformation.print();
        }
    }

    public double[][] stringToDoubleArray(String s){
        String[] point = s.split(",");
        double[] pointValue = new double[3];
        ArrayList<double[]> pointList = new ArrayList<>();
        for(int j = 0; j < point.length/3; j++) {
            for (int i = 0; i < 3; i++) {
                pointValue[i] = Double.valueOf(point[j*3 + i]);
            }
            pointList.add(pointValue);
        }

        double[][] finalPoints = new double[3][pointList.size()];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < pointList.size(); j++){
                    finalPoints[i][j] = pointList.get(j)[i];
            }
        }

        return finalPoints;
    }

    public ArrayList<double[]> stringToArrayList(String s){
        String[] point = s.split(",");
        double[] pointValue = new double[3];
        ArrayList<double[]> pointList = new ArrayList<>();
        for(int j = 0; j < point.length/3; j++) {
            for (int i = 0; i < 3; i++) {
                pointValue[i] = Double.valueOf(point[j*3 + i]);
            }
            pointList.add(pointValue);
        }

        return pointList;
    }

    public double[][] arrayListToDouble(ArrayList<double[]> pointList){
        double[][] finalPoints = new double[3][pointList.size()];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < pointList.size(); j++){
                finalPoints[i][j] = pointList.get(j)[i];
            }
        }

        return finalPoints;
    }

    public String[] getComPorts(){
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> ports = new ArrayList<>();
        while(portList.hasMoreElements()){
            CommPortIdentifier portIdentifier = (CommPortIdentifier) portList.nextElement();
            ports.add(portIdentifier.getName());
            System.out.println(portIdentifier.getName());
        }

        String[] comPorts = new String[ports.size()];
        for(int i = 0; i < ports.size(); i++){
            comPorts[i] = ports.get(i);
        }
        return comPorts;
    }

    @Override
    public void valueChanged(ListSelectionEvent e){
        if(!e.getValueIsAdjusting()){
            if(comPortList.getSelectedIndex() != -1){
                String portName = comPorts[comPortList.getSelectedIndex()];
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e){
        System.exit(0);
    }

    //unused, but need to implement because interface
    @Override
    public void windowOpened(WindowEvent e){}
    @Override
    public void windowClosed(WindowEvent e){}
    @Override
    public void windowIconified(WindowEvent e){}
    @Override
    public void windowDeiconified(WindowEvent e){}
    @Override
    public void windowActivated(WindowEvent e){}
    @Override
    public void windowDeactivated(WindowEvent e){}
}

//container object for ellipse center, radii, and rotation matrices
class EllipseInformation {
    Matrix center, radii, rotation;

    //constructor
    //REQUIRES: center, radii, rotation matrices of ellipse
    //EFFECTS: creates object to hold these matrices
    public EllipseInformation(Matrix ellipseCenter, Matrix ellipseRadii, Matrix ellipseRotation){
        center = ellipseCenter;
        radii = ellipseRadii;
        rotation = ellipseRotation;
    }

    //EFFECTS: returns center, radii, or rotation matrices
    public Matrix getCenter(){
        return center;
    }

    public Matrix getRadii(){
        return radii;
    }

    public Matrix getRotation(){
        return rotation;
    }

    //EFFECTS: prints center, radii, and rotation to console
    public void print(){
        center.print(1, 5);
        radii.print(1, 5);
        rotation.print(1, 5);
    }
}