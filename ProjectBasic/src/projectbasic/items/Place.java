/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectbasic.items;

/**
 *
 * @author Visual Studio
 */
public class Place {

    private String namePlace;
    
    public Place(String namePlace) {
        this.namePlace = namePlace;
    }

    public String getNamePlace() {
        return namePlace;
    }

    public void setNamePlace(String namePlace) {
        this.namePlace = namePlace;
    }

    @Override
    public String toString() {
        return namePlace; //To change body of generated methods, choose Tools | Templates.
    }
    
}
