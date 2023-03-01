import java.util.ArrayList;
import java.util.List;

public class Division {

    private String conference;
    private String region;
    private int shift;
    private List<Team> teams= new ArrayList<>();
    private Division sameConferencePlayed;
    private Division otherConferencePlayed;

    public Division(String conference, String region, int shift) {
        this.conference = conference;
        this.region=region;
        this.shift=shift;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getShift() {
        return shift;
    }

    public String getRegion() {
        return region;
    }

    public String getConference() {
        return conference;
    }

    public Division getSameConferencePlayed() {
        return sameConferencePlayed;
    }

    public void setSameConferencePlayed(Division sameConferencePlayed) {
        this.sameConferencePlayed = sameConferencePlayed;
    }

    public Division getOtherConferencePlayed() {
        return otherConferencePlayed;
    }

    public void setOtherConferencePlayed(Division otherConferencePlayed) {
        this.otherConferencePlayed = otherConferencePlayed;
    }
}
