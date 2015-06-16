package model.MARK_II;

import model.MARK_II.connectTypes.AbstractSensorCellsToRegionConnect;
import model.util.HeapTracker;
import model.util.Rectangle;

import java.awt.*;
import java.io.IOException;

/**
 * PROBLEM: When a user wants to create a NervousSystem object that is too large for
 * the Java Heap. This is usually when you want to build a NervousSystem with 0.5+ million Neurons.
 *
 * SOLUTION: This class provides a easy to use API for creating your NervousSystem as separately
 * saved JSON files.
 *
 * @author Q Liu (quinnliu@vt.edu)
 * @version 6/15/2015.
 */
public class BigNervousSystem {

    private final double MAX_HEAP_USE_PERCENTAGE;
    private BigNeocortex bigNeocortex;

    private HeapTracker heapTracker;

    public BigNervousSystem(int maxSizeOfBigNervousSystemInMB, BigNeocortex bigNeocortex, Dimension retinaDimension,
                            AbstractSensorCellsToRegionConnect opticNerve,
                            String[] retinaConnectionParameterListInOrder,
                            String pathAndRetinaFileName) throws IOException {
        this.heapTracker = new HeapTracker(true);
        double maxHeapSizeInMB = (double) this.heapTracker
                .getHeapMaxSizeInBytes() / 1000000;
        this.MAX_HEAP_USE_PERCENTAGE = (double) maxSizeOfBigNervousSystemInMB
                / maxHeapSizeInMB;
        if (this.MAX_HEAP_USE_PERCENTAGE > 1.0) {
            throw new IllegalArgumentException("maxSizeOfBigNervousSystemInMB " +
                    "is too large making MAX_HEAP_USE_PERCENTAGE > 1.0");
        }

        this.bigNeocortex = bigNeocortex;
        this.heapTracker.updateHeapData();
        System.out.println("AFTER: this.bigNeocortex = bigNeocortex;");

        // create Retina
        BigRetina bigRetina = new BigRetina((int) retinaDimension.getHeight(), (int) retinaDimension.getWidth(), pathAndRetinaFileName);
        this.heapTracker.updateHeapData();
        System.out.println("AFTER: BigRetina bigRetina = new BigRetina((int) retinaDimension.getHeight(), (int) retinaDimension.getWidth(), pathAndRetinaFileName);");

        // connect Retina
        AbstractSensorCellsToRegionConnect opticNerveConnectType = opticNerve;

        for (int i = 0; i < retinaConnectionParameterListInOrder.length; i = i+7) {
            SensorCell[][] sensorCells = bigRetina.getVisionCells(new Rectangle(
                    new Point(
                            Integer.valueOf(retinaConnectionParameterListInOrder[i]),
                            Integer.valueOf(retinaConnectionParameterListInOrder[i+1])),
                    new Point(
                            Integer.valueOf(retinaConnectionParameterListInOrder[i+2]),
                            Integer.valueOf(retinaConnectionParameterListInOrder[i+3]))));

            Region region = this.bigNeocortex.getRegion(
                    retinaConnectionParameterListInOrder[i+4]);
            this.heapTracker.updateHeapData();
            System.out.println("AFTER: Region region = this.bigNeocortex" +
                    ".getRegion(\n" +
                    "                    " +
                    "retinaConnectionParameterListInOrder[i+4]);");
            Column[][] regionColumns = region.getColumns();
            int numberOfColumnsToOverlapAlongXAxisOfSensorCells =
                    Integer.valueOf(retinaConnectionParameterListInOrder[i+5]);
            int numberOfColumnsToOverlapAlongYAxisOfSensorCells =
                    Integer.valueOf(retinaConnectionParameterListInOrder[i+6]);

            opticNerveConnectType.connect(sensorCells, regionColumns,
                    numberOfColumnsToOverlapAlongXAxisOfSensorCells,
                    numberOfColumnsToOverlapAlongYAxisOfSensorCells);

            if (this.heapTracker.isUsedHeapPercentageOver(this
                    .MAX_HEAP_USE_PERCENTAGE)) {
                throw new IllegalArgumentException("your parameters for " +
                        "optic nerve connect type are using too much of the " +
                        "heap and must be decreased");
            }

            this.heapTracker.updateHeapData();
            System.out.println("AFTER: opticNerveConnectType.connect" +
                    "(sensorCells, regionColumns,\n" +
                    "                    " +
                    "numberOfColumnsToOverlapAlongXAxisOfSensorCells,\n" +
                    "                    " +
                    "numberOfColumnsToOverlapAlongYAxisOfSensorCells);");

            // resave used regions in BigNeocortex since each Region has been changed
            this.bigNeocortex.saveRegion(region);
        }
    }
}