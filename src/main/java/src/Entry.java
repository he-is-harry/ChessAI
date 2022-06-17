/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author harryhe
 */
public class Entry {
    long key;
    double value;
    int depth;
    int nodeType;
    public Entry(long key, double value, int depth, int nodeType){
        this.key = key;
        this.value = value;
        this.depth = depth;
        this.nodeType = nodeType;
    }
}
