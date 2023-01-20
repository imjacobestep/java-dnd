
package dnd;

public class Item {
    
    String name = "";//item name
    String type = "";//item type
    int effect = 0;//item effect (number used to add or subtract from a value)
    int room;//number of room item is taken from
    
    public Item (String n, String t, int e, int r){//item object used for weapons, potions, shields, and armor
        
        name = n;
        type = t;
        effect = e;
        room = r;
        
    }
    
    public Item (String n, String t){//object used for key (doens't need an effect or room
        
        name = n;
        type = t;
        
    }
    
    public Item (){//item used as temp
        
    }
    
    public void takenFrom(int roomName){//sets room number
        room = roomName;
    }
    
    public void addAffect (Character c){//adds effect of item to applicable value
        
        switch (type) {
            case "weapon":
                c.modAP(effect, 1);
                break;
            case "potion":
                c.modHP(effect, 1);
                break;
            case "dPotion":
                c.modDEF(effect, 1);
                break;
            case "aPotion":
                c.modAP(effect, 1);
                break;
            case "armor":
                c.modDEF(effect, 1);
                break;
            case "shield":
                c.modDEF(effect, 1);
        }
        
    }
    
    public void removeAffect (Character c){//subtracts effect from applicable value
        
        switch(type){
            case "weapon":
                c.modAP(effect, 0);
                break;
            case "potion":
                c.modHP(effect, 0);
                break;
            case "dPotion":
                c.modDEF(effect, 0);
                break;
            case "aPotion":
                c.modAP(effect, 0);
                break;
            case "armor":
                c.modDEF(effect, 0);
                break;
            case "shield":
                c.modDEF(effect, 0);
        }
        
    }
    
}
