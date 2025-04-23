/**
 * Marker interface for actionable classes in the system.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action;
/**
 * This interface establishes a common contract for objects that can be viewed
 * or interacted with in the application workflow.
 */
public interface Act {
    /**
     * Returns a string representation of the object for display purposes.
     *
     * @return A formatted string containing the object's details.
     */
    String view();
}
