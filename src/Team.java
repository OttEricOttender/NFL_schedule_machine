import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private Division division;
    private Division otherDivision;
    private Division otherOppositeDivision;
    private List<Team> opponents= new ArrayList<>();

    public Team(String name, Division division) {
        this.name = name;
        this.division = division;
    }

    public void addOpponent(Team opponent){
        this.opponents.add(opponent);
    }

    public List<Team> getOpponents() {
        return opponents;
    }

    public String getName() {
        return name;
    }

    public Division getConference() {
        return division;
    }

    public Division getDivision() {
        return division;
    }

    public void setOtherDivision(Division otherDivision) {
        this.otherDivision = otherDivision;
    }

    public void setOtherOppositeDivision(Division otherOppositeDivision) {
        this.otherOppositeDivision = otherOppositeDivision;
    }

    public Division getOtherDivision() {
        return otherDivision;
    }

    public Division getOtherOppositeDivision() {
        return otherOppositeDivision;
    }

}
