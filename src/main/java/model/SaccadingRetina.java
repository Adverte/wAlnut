package model;

import java.awt.Point;
import model.Retina;

/**
 * @author Quinn Liu (quinnliu@vt.edu)
 * @version Apr 28, 2014
 */
public class SaccadingRetina extends Retina {
    private Point position;
    private double distanceBetweenImageAndRetina;

    public SaccadingRetina(int numberOfVisionCellsAlongYAxis,
	    int numberOfVisionCellsAlongXAxis, Point retinaPosition,
	    double distanceBetweenImageAndRetina) {
	super(numberOfVisionCellsAlongYAxis, numberOfVisionCellsAlongXAxis);

	this.position = retinaPosition;
	this.distanceBetweenImageAndRetina = distanceBetweenImageAndRetina;
    }

    public Point getPosition() {
	return position;
    }

    public void setRetinaPosition(Point retinaPosition) {
	this.position = retinaPosition;
    }

    public double getDistanceBetweenImageAndRetina() {
	return distanceBetweenImageAndRetina;
    }

    public void setDistanceBetweenImageAndRetina(
	    int distanceBetweenImageAndRetina) {
	this.distanceBetweenImageAndRetina = distanceBetweenImageAndRetina;
    }

    public int[][] getDoubleIntArrayRepresentationOfVisionCells() {
	int numberOfRows = super.visionCells.length;
	int numberOfColumns = super.visionCells[0].length;
	int[][] retina = new int[numberOfRows][numberOfColumns];

	for (int x = 0; x < numberOfColumns; x++) {
	    for (int y = 0; y < numberOfRows; y++) {
		if (super.visionCells[x][y].getActiveState()) {
		    retina[x][y] = 1;
		} else {
		    retina[x][y] = 0;
		}
	    }
	}

	return retina;
    }

}
