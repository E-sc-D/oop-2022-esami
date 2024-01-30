package a01a.e2;

public interface Logic {
    /** 
     * gives the content of the button at
     * the specified cordinates, returns " " 
     * if the button is not present
     */
    public String getValue(int x, int y);

    /**
     * Signals the logic that a the button with x,y location
     * has been pressed
     */
    public void pressButton(int x, int y);

    
}
