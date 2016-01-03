/**
 * Created by Kevin on 12/27/2015.
 * Builds GUI and does the math for for the calibration program.
 * Uses math from http://www.mathworks.com/matlabcentral/fileexchange/45356-fitting-quadratic-curves-and-surfaces/
 * and http://www.mathworks.com/matlabcentral/fileexchange/24693-ellipsoid-fit to calculate the fitted
 * ellipse characteristics.
 */
import Jama.CholeskyDecomposition;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CalibrationGUI extends JFrame implements ActionListener, WindowListener{
    private Label lblPt1, lblPt2, lblPt3, lblPt4, lblPt5, lblPt6, lblPt7, lblPt8, lblPt9;
    private Label centerLabel, radiiLabel, rotationLabel;

    private TextField txt1, txt2, txt3, txt4, txt5, txt6, txt7, txt8, txt9;
    private TextField txt10, txt11, txt12, txt13, txt14, txt15, txt16, txt17, txt18, txt19, txt20;
    private TextField txt21, txt22, txt23, txt24, txt25, txt26, txt27;
    private TextField centerText, radiiText;

    private TextArea rotationMatrix;

    private Button submitButton;



    public CalibrationGUI(){
        setLayout(new FlowLayout());
        setResizable(false);

        JPanel jPanel1 = new JPanel(new FlowLayout());
        lblPt1 = new Label("Point 1");
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
        lblPt2 = new Label("Point 2");
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
        lblPt3 = new Label("Point 3");
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
        lblPt4 = new Label("Point 4");
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
        lblPt5 = new Label("Point 5");
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
        lblPt6 = new Label("Point 6");
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
        lblPt7 = new Label("Point 7");
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
        lblPt8 = new Label("Point 8");
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
        lblPt9 = new Label("Point 9");
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

        submitButton = new Button("Submit Points");
        jPanelSubmit.add(submitButton);

        submitButton.addActionListener(this);


        setTitle("Compass Calibrator");
        setSize(300, 600);

        setVisible(true);

        JPanel jPanelCenter = new JPanel(new FlowLayout());
        centerLabel = new Label("Center");
        jPanelCenter.add(centerLabel);

        centerText = new TextField(15);
        centerText.setEditable(false);
        jPanelCenter.add(centerText);

        JPanel jPanelRadii = new JPanel(new FlowLayout());
        radiiLabel = new Label("Radii");
        jPanelRadii.add(radiiLabel);

        radiiText = new TextField(20);
        radiiText.setEditable(false);
        jPanelRadii.add(radiiText);

        JPanel jPanelRotation = new JPanel(new FlowLayout());
        rotationLabel = new Label("Rotation Matrix");
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
        add(jPanelCenter);
        add(jPanelRadii);
        add(jPanelRotation);



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
        double[][] inputPoints = new double[3][9];
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

        double[][] ellipsoidMatrix = new double[9][9];

        //fitting ellipsoid in form Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
        for(int i = 0; i < 9; i++){
            ellipsoidMatrix[0][i] = inputPoints[0][i] * inputPoints[0][i];
            ellipsoidMatrix[1][i] = inputPoints[1][i] * inputPoints[1][i];
            ellipsoidMatrix[2][i] = inputPoints[2][i] * inputPoints[2][i];
            ellipsoidMatrix[3][i] = 2 * inputPoints[0][i] * inputPoints[1][i];
            ellipsoidMatrix[4][i] = 2 * inputPoints[0][i] * inputPoints[2][i];
            ellipsoidMatrix[5][i] = 2 * inputPoints[1][i] * inputPoints[2][i];
            ellipsoidMatrix[6][i] = 2 * inputPoints[0][i];
            ellipsoidMatrix[7][i] = 2 * inputPoints[1][i];
            ellipsoidMatrix[8][i] = 2 * inputPoints[2][i];
        }


        Matrix ellipse = new Matrix(ellipsoidMatrix);

        ellipse = ellipse.transpose();


        //solve normal system of equations
        //given ellipsoidMatrix = D, solution = v: v = (D' * D)^-1 * (D' * (1xLengthOfD matrix of ones))

        //create flattener - must be double[][] instead of double[] for multiply method
        double[][] flattener = new double[ellipsoidMatrix.length][1];
        for(double[] d : flattener){
            d[0] = 1;
        }

        Matrix flat = new Matrix(flattener);

        Matrix ellipseSolved = (ellipse.inverse().times(ellipse).solve(ellipse.inverse().times(flat)));

        //create algebraic form of ellipsoid (quadratic form matrix, symmetric)
        //from equation Ax^2 + By^2 + Cz^2 + 2Dxy + 2Exz + 2Fyz + 2Gx + 2Hy + 2Iz - 1 = 0
        //[ D(1) D(3) D(5) D(7) ]
        //[ D(4) D(2) D(6) D(8) ]
        //[ D(5) D(6) D(3) D(9) ]
        //[ D(7) D(8) D(9)  -1  ]
        Matrix ellipseQuadratic = new Matrix(4,4);
        ellipseQuadratic.set(0,0,ellipseSolved.get(0,0));
        ellipseQuadratic.set(0,1,ellipseSolved.get(3,0));
        ellipseQuadratic.set(0,2,ellipseSolved.get(4,0));
        ellipseQuadratic.set(0,3,ellipseSolved.get(6,0));
        ellipseQuadratic.set(1,0,ellipseSolved.get(3,0));
        ellipseQuadratic.set(1,1,ellipseSolved.get(1,0));
        ellipseQuadratic.set(1,2,ellipseSolved.get(5,0));
        ellipseQuadratic.set(1,3,ellipseSolved.get(7,0));
        ellipseQuadratic.set(2,0,ellipseSolved.get(4,0));
        ellipseQuadratic.set(2,1,ellipseSolved.get(5,0));
        ellipseQuadratic.set(2,2,ellipseSolved.get(2,0));
        ellipseQuadratic.set(2,3,ellipseSolved.get(8,0));
        ellipseQuadratic.set(3,0,ellipseSolved.get(6,0));
        ellipseQuadratic.set(3,1,ellipseSolved.get(7,0));
        ellipseQuadratic.set(3,2,ellipseSolved.get(8,0));
        ellipseQuadratic.set(3,3,-1);
        //find center of ellipse - use center = quad(1:3, 1:3) ^ -1 * -solved(7:9)
        //create quad(1:3, 1:3) and -solved(7:9)


        Matrix ellipseQuadraticCenter = ellipseQuadratic.getMatrix(0,2,0,2);
        Matrix ellipseSolvedCenter = ellipseSolved.getMatrix(6,8,0,0);

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
            System.exit(1);
        }


        //calculate eigenvalues and eigenvectors for matrix
        EigenvalueDecomposition ellipseEigen = new EigenvalueDecomposition(ellipseTranslatedSubmatrix);

        //eigenvalues stored on diagonals, everything else zero
        Matrix eigenvalueMatrix = ellipseEigen.getD();
        Matrix eigenvectors = ellipseEigen.getV();


        //matrix of only eigenvalues
        Matrix eigenvalues = new Matrix(1,3);
        for(int i = 0; i < 3; i++){
            eigenvalues.set(0,i,eigenvalueMatrix.get(i,i));
        }

        //radii found using radii = sqrt(1/eigenvalues)
        Matrix radii = eigenvalues.copy();
        //create matrix of ones to find the reciprocal of everything in eigenvalues
        Matrix ones = new Matrix(1,3,1);
        radii.arrayLeftDivideEquals(ones);

        //find radii
        for(int i = 0; i < 3; i++){
            radii.set(0, i, Math.sqrt(radii.get(0, i)));
        }

        //set a rounder to round all numbers to 3 decimal places
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);

        //print center
        double[][] centerArray = center.getArray();
        String centerString = "";
        for(int i = 0; i < centerArray.length; i++){
            for(int j = 0; j < centerArray[i].length; j++){
                centerString = centerString + String.valueOf(df.format(centerArray[i][j])) + " ";
            }
        }

        centerText.setText(centerString);

        //divide by radii to size correctly
        System.out.println("Radii: x y z");
        radii.print(1, 3);



        double[][] radiiArray = radii.getArray();
        String radiiString = "";
        for(int i = 0; i < radiiArray.length; i++){
            for(int j = 0; j < radiiArray[i].length; j++){
                radiiString = radiiString + String.valueOf(df.format(radiiArray[i][j])) + " ";
            }
        }

        radiiText.setText(radiiString);

        //rotation matrix: derotated point = i * rotation
        System.out.println("Rotation matrix: ");
        eigenvectors.print(1, 5);

        double[][] rotationArray = eigenvectors.getArray();
        String rotationString = "";
        for(int i = 0; i < rotationArray.length; i++){
            for(int j = 0; j < rotationArray[i].length; j++){
                rotationString = rotationString + String.valueOf(df.format(rotationArray[i][j])) + " ";
            }
            rotationString = rotationString + "\n";
        }

        rotationMatrix.setText(rotationString);
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
