/**
 * delta is the vector that the object needs to move along to stop intersecting the other object
 * normal are normal vectors of the collision (this faces inside what it collided with)
 */

/***
 *
 * @param delta The vector that the object needs to move along to stop intersecting the other object
 * @param normal The normal vector of the edge that the object hit
 */
public record Hit(Vector2d delta, Vector2d normal) { }