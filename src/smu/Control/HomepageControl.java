package smu.Control;

public class HomepageControl {

    private boolean isMenuVisible = true;

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public void toggleMenu() {
        isMenuVisible = !isMenuVisible; // Cambia lo stato
    }
}
