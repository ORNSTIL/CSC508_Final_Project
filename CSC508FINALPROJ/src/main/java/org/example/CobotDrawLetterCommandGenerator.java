package org.example;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobotDrawLetterCommandGenerator implements PropertyChangeListener {
    private static final Logger logger = LoggerFactory.getLogger(CobotDrawLetterCommandGenerator.class);
    private final LetterDataProcessor ldp;
    private int charactersDrawn = 0;
    private final int rowCharacterWidth; // how many characters to draw before moving to the next row
    private final double rowHeightM;
    private final double characterWidthM;
    private final double characterHeightM;
    private final double characterPaddingM;
    private final double writingStartPosHorizontal;
    private final double writingStartPosLength;
    private final double writingPaperHeight;
    private final double penLiftAmount;
    private final int interPointPause;
    private final int interLinePause;
    private final int interLetterPause;

    Blackboard blackboard;

    private final String baseMoveCommand = "movel(p[%5f, %5f, %5f, 0.005, -3.134, -0.007], a=1.0, v=0.2)";

    public CobotDrawLetterCommandGenerator(
            int rowCharacterWidth,
            double rowHeightM,
            double characterWidthM,
            double characterHeightM,
            double characterPaddingM,
            double writingStartPosHorizontal,
            double writingStartPosLength,
            double writingPaperHeight,
            double penLiftAmount,
            int interPointPause,
            int interLinePause,
            int interLetterPause
    ) {
        this.rowCharacterWidth = rowCharacterWidth;
        this.rowHeightM = rowHeightM;
        this.characterWidthM = characterWidthM;
        this.characterHeightM = characterHeightM;
        this.characterPaddingM = characterPaddingM;
        this.writingStartPosHorizontal = writingStartPosHorizontal;
        this.writingStartPosLength = writingStartPosLength;
        this.writingPaperHeight = writingPaperHeight;
        this.penLiftAmount = penLiftAmount;
        this.interPointPause = interPointPause;
        this.interLinePause = interLinePause;
        this.interLetterPause = interLetterPause;

        blackboard = Blackboard.getInstance();
        blackboard.addPropertyChangeListener(this);
        logger.info("CobotDrawLetterCommandGenerator registered as a listener to Blackboard.");
        ldp = new LetterDataProcessor();
    }

    public CobotDrawLetterCommandGenerator() {
        this(
            20,
            0.040,
            0.030,
            0.030,
            0.005,
            -0.300,
            0.300,
            0.000,
            0.010,
            500,
            800,
            1000
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.info("Draw Letter Command Generator detected property change");
        if ("keyboardLetter".equals(evt.getPropertyName())) {
            String letter = (String) evt.getNewValue();
            logger.info("Received data from Blackboard: {}", letter);
            if (letter == null || Objects.equals(letter, "")) return;
            genLetter(letter);
        }
    }

    private void genLetter(String letter) {
        // x is back/forward
        // y is left / right
        // z is up / down
        try {
            ArrayList<ArrayList<ArrayList<Double>>> letterLines = ldp.getLetterLines(letter);

            for (ArrayList<ArrayList<Double>> line : letterLines) {
                int rowNumber = charactersDrawn / rowCharacterWidth;
                int columnNumber = charactersDrawn % rowCharacterWidth;
                double baseHorizontalPose = writingStartPosHorizontal + (characterWidthM + characterPaddingM) * columnNumber;
                double baseLengthPose = writingStartPosLength + rowHeightM * rowNumber;

                double curHorizontalPose;
                double curLengthPose;

                // hover the pen...
                curHorizontalPose = clampAdjustNumber(line.get(0).get(0), 5.0, characterWidthM);
                curLengthPose = clampAdjustNumber(line.get(0).get(1), 5.0, characterHeightM);
                String command = String.format(
                        baseMoveCommand,
                        baseLengthPose + curLengthPose,
                        baseHorizontalPose + curHorizontalPose,
                        writingPaperHeight + penLiftAmount);

                blackboard.addToCobotCommands(command);
                Thread.sleep(interLinePause);

                // draw a line, then lift...
                for (ArrayList<Double> point: line) {
                    curHorizontalPose = clampAdjustNumber(point.get(0), 5.0, characterWidthM);
                    curLengthPose = clampAdjustNumber(point.get(1), 5.0, characterHeightM);
                    command = String.format(
                            baseMoveCommand,
                            baseLengthPose + curLengthPose,
                            baseHorizontalPose + curHorizontalPose,
                            writingPaperHeight);

                    blackboard.addToCobotCommands(command);
                    Thread.sleep(interPointPause);
                }
                command = String.format(
                        baseMoveCommand,
                        baseLengthPose + curLengthPose,
                        baseHorizontalPose + curHorizontalPose,
                        writingPaperHeight + penLiftAmount);
                logger.info("Lifting the pen...");

                blackboard.addToCobotCommands(command);
                Thread.sleep(interLinePause);
            }

            charactersDrawn++;
            Blackboard.getInstance().setKeyboardLetter("");
            Thread.sleep(interLetterPause);
        } catch (InterruptedException e) {
            logger.error("Interrupted while waiting: {}", e.getMessage());
        }
    }

    private double clampAdjustNumber(double val, double originalLim, double targetLim) {
        if (val < -1.0 * originalLim) val = -1.0 * originalLim;
        if (val > originalLim) val = -1.0 * originalLim;

        val = targetLim * val / (originalLim);
        return val;
    }
}
