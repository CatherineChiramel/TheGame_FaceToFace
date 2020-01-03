import de.upb.isml.thegamef2f.engine.GameState;
import de.upb.isml.thegamef2f.engine.Placement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MontecarloNode {
    protected final MontecarloNode parentNode;
    protected final Placement lastMove;
    protected final GameState state;
    protected final List<Placement> validPlacements;
    Integer nPlays;
    Integer nWins;
    Map<Integer, Placement> childPlayMap;
    Map<Integer, MontecarloNode> childNodeMap;

    public MontecarloNode(MontecarloNode parentNode, Placement lastMove, GameState state, List<Placement> validPlacements){

        this.parentNode = parentNode;
        this.lastMove = lastMove;
        this.state = state;
        this.validPlacements = validPlacements;
        this.nPlays = 0;
        this.nWins = 0;

        this.childPlayMap = new HashMap<>();
        this.childNodeMap = new HashMap<>();
        for(Placement play: validPlacements){
            childPlayMap.put(play.hashCode(),play);
            childNodeMap.put(play.hashCode(),null);
        }
    }

    public  MontecarloNode childNode(Placement play){
        MontecarloNode child = childNodeMap.get(play.hashCode());
        if(child == null){
            System.out.println("Child is not expanded");
        }
        return child;
    }

    public MontecarloNode expand(Placement play, GameState childState, List<Placement> unexpandedPlays){
        MontecarloNode childNode = new MontecarloNode(this, play, childState, unexpandedPlays);
        return  childNode;
    }

    public List<Placement> allPlays(){
        List<Placement> plays = new ArrayList<>();
        for(Integer key: childPlayMap.keySet()){
            plays.add(childPlayMap.get(key));
        }
        return  plays;
    }

    public  List<Placement> unexpandedPlays(){
        List<Placement> plays = new ArrayList<>();
        for(Integer key: childPlayMap.keySet()){
            if(childNodeMap.get(key) == null)
                plays.add(childPlayMap.get(key));
        }
        return  plays;
    }

    public boolean isFullyExpanded(){
        for(Integer key: childNodeMap.keySet()){
            if(childNodeMap.get(key) == null){
                return false;
            }
        }
        return true;
    }

    public boolean isLeaf(){
        if(childNodeMap.isEmpty()){
            return true;
        }
        return false;
    }

    public double getUCB1(double biasParam){
        return (this.nPlays/this.nWins) + Math.sqrt(biasParam * Math.log(this.parentNode.nPlays)/this.nPlays);
    }

}
