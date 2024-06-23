/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/** Type énuméré des directions : les directions correspondent à un ensemble borné de valeurs, connu à l'avance
 *
 * @author freder
 */
public enum Direction {
    haut, bas, gauche, droite;
    
    public Direction inverse()
    {
        switch(this)
        {
            case haut: return bas;
            case bas: return haut;
            case gauche: return droite;
            case droite: return gauche;
        }
        return null;
    }
}
