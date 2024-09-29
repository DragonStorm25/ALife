package src.main;

import src.util.QuadTree;
import src.util.Vector2D;

public class Main {
    
    public static void main(String[] args) {
        QuadTree qt = new QuadTree(Vector2D.ZERO(), new Vector2D(128, 128));
        System.out.println(qt);
    }
}
