package smu.Control;

public class MenuControl {
    private boolean isMenuVisible = true;

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public void toggleMenu() {
        isMenuVisible = !isMenuVisible; // Cambia lo stato
    }
}
