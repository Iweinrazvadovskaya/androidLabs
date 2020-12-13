package by.bstu.razvod.lab4.extendes;

import by.bstu.razvod.lab4.MainViewPresentation;

public interface ContextMenuListener {
    void remove(MainViewPresentation presentation);
    void favorite(MainViewPresentation presentation);
    void copy(MainViewPresentation presentation);
}