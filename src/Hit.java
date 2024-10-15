/**
 * posX and posY are the point of collision.
 * deltaX and DeltaY are the amount the colliding object overstepped the other objects bounds 
 * normalX and normalY are normal vectors of the collision (this faces inside what it collided with)
 */
public record Hit(int posX, int posY, int deltaX, int deltaY, int normalX, int normalY) { }