/**
 * Created by Kevin on 12/26/2015.
 */
import Jama.*;

public class CompassCalibration {
    public static void main(String[] args) {

        //initialize calibration GUI
        CalibrationGUI calibrationGUI = new CalibrationGUI();
/*

        //if improper inputs (3x coordinates for 9 points), exit program
        if(args.length != 27){
            System.out.println("Requires 9 points of 3 coordinates each for 27 args");
            System.exit(1);
        }

        double[][] inputPoints = new double[args.length / 9][9];

        //move args into a 9x3 matrix - one column of x, one of y, one of z - all doubles
        //inputPoints[xyz][point #]
        for(int i = 0; i < 3; i++){
            double[] temp = new double[9];
            for(int j = 0; j < 9; j++){
                temp[i] = Double.valueOf(args[9 * i + j]);
            }
            inputPoints[i] = temp;
        }

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

        //solve normal system of equations
        //given ellipsoidMatrix = D, solution = v: v = (D' * D)^-1 * (D' * (1xLengthOfD matrix of ones))

        //create flattener - must be double[][] instead of double[] for multiply method
        double[][] flattener = new double[ellipsoidMatrix.length][0];
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
        ellipseQuadratic.set(0,1,ellipseSolved.get(2,0));
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
            translationMatrix.set(4, i, center.get(i, 0));
        }

        //translate ellipse to center - translated = translation * quad * translation'
        Matrix ellipseTranslated = translationMatrix.transpose().times(ellipseQuadratic.times(translationMatrix));

        //check for positive definiteness - if not, is not a real ellipse
        CholeskyDecomposition ellipseChol = new CholeskyDecomposition(ellipseTranslated.getMatrix(0,2,0,2));
        if(!ellipseChol.isSPD()){
            System.out.println("Parameters do not define a real ellipse, exiting");
            System.exit(1);
        }

        //calculate eigenvalues and eigenvectors for matrix
        EigenvalueDecomposition ellipseEigen = new EigenvalueDecomposition(ellipseTranslated.getMatrix(0,2,0,2));

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
        radii.arrayRightDivideEquals(ones);

        //find radii
        for(int i = 0; i < 3; i++){
            radii.set(0, i, Math.sqrt(radii.get(0, i)));
        }

        //divide by radii to size correctly
        System.out.println("Radii: x y z");
        for(int i = 0; i < 3; i++){
            System.out.print(radii.get(i,0));
            System.out.print(" ");
        }

        //rotation matrix: derotated point = i * rotation
        System.out.println("Rotation matrix: ");
        for(int i = 0; i < 3; i++){
            System.out.println("");
            for(int j = 0; j < 3; j++){
                System.out.print(eigenvectors.get(i,j));
                System.out.print(" ");
            }
        }
*/


    }


}

