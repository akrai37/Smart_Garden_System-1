

package com.example.ooad_project.Events;

public class PesticideApplicationEvent {
    private final int targetRowPosition;
    private final int targetColumnPosition;
    private final String chemicalSprayType;

    public PesticideApplicationEvent(int row, int col, String pesticideType) {
        this.targetRowPosition = row;
        this.targetColumnPosition = col;
        this.chemicalSprayType = pesticideType;
    }

    public int getRow() {
        return targetRowPosition;
    }

    public int getCol() {
        return targetColumnPosition;
    }

    public String getPesticideType() {
        return chemicalSprayType;
    }
}

