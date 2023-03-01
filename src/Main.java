import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ORDER OF OPERATIONS:
 * 1) Assign 17 games for each team.
 *      1.1) Set playable divisions for each division.
 *      1.2) Put all the assigned division games into the team's "opponents" list.
 *      1.3) Get 1 random opponent from 3 other divisions for each team and add them to the "opponents" list.
 * 2) Shuffle all the games between weeks so that one team doesn't have multiple games in one week.
 *
 */
public class Main {
    private static List<List<HashMap<Team, Team>>> weeks;
    private static List<Match> matches= new ArrayList<>();

    public static void main(String[] args) {
        //Setup of teams and divisions
        List<Division> divisions= divisions();
        List<Team> teams = teamsToList(divisions);
        teamsToDivisions(teams, divisions);

        //Setup of matches
        putSameDivisionGames(teams, divisions);
        System.out.println("Division rivals set");
        pairSameConferenceDivisions(divisions,teams);
        System.out.println("Conference rivals set");
        pairOtherConferenceDivision(divisions,teams);
        System.out.println("Interconference rivals set");
        //sameConferenceSingleOpponents(divisions,teams);
        System.out.println("Conference single opponent set");

        int counter=0;
        for (Match match : matches) {
            if(counter%16==0){
                System.out.println("#".repeat(10) + "[WEEK " + (counter/16)+1 + "]"+"#".repeat(10));
            }
            System.out.println(match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName() + " @ " + match.getLocation());
            counter+=1;
        }

    }

    private static List<Team> teamsToList(List<Division> divisions){
        List<Team> teams= new ArrayList<>();
        
        teams.add(new Team("Green Bay Packers", divisions.get(0)));
        teams.add(new Team("Minnesota Vikings", divisions.get(0)));
        teams.add(new Team("Detroit Lions", divisions.get(0)));
        teams.add(new Team("Chicago Bears", divisions.get(0)));

        teams.add(new Team("San Francisco 49ers", divisions.get(1)));
        teams.add(new Team("Seattle Seahawks", divisions.get(1)));
        teams.add(new Team("Los Angeles Rams", divisions.get(1)));
        teams.add(new Team("Arizona Cardinals", divisions.get(1)));

        teams.add(new Team("New Orleans Saints", divisions.get(2)));
        teams.add(new Team("Tampa Bay Buccaneers",  divisions.get(2)));
        teams.add(new Team("Carolina Panthers", divisions.get(2)));
        teams.add(new Team("Atlanta Falcons", divisions.get(2)));

        teams.add(new Team("Dallas Cowboys", divisions.get(3)));
        teams.add(new Team("New York Giants", divisions.get(3)));
        teams.add(new Team("Washington Commanders", divisions.get(3)));
        teams.add(new Team("Philadelphia Eagles", divisions.get(3)));

        teams.add(new Team("Pittsburgh Steelers", divisions.get(4)));
        teams.add(new Team("Cleveland Browns", divisions.get(4)));
        teams.add(new Team("Baltimore Ravens", divisions.get(4)));
        teams.add(new Team("Cincinnati Bengals", divisions.get(4)));

        teams.add(new Team("Los Angeles Chargers", divisions.get(5)));
        teams.add(new Team("Las Vegas Raiders", divisions.get(5)));
        teams.add(new Team("Kansas City Chiefs", divisions.get(5)));
        teams.add(new Team("Denver Broncos", divisions.get(5)));

        teams.add(new Team("Houston Texans", divisions.get(6)));
        teams.add(new Team("Indianapolis Colts", divisions.get(6)));
        teams.add(new Team("Tennessee Titans", divisions.get(6)));
        teams.add(new Team("Jacksonville Jaguars", divisions.get(6)));

        teams.add(new Team("New England Patriots", divisions.get(7)));
        teams.add(new Team("Miami Dolphins", divisions.get(7)));
        teams.add(new Team("New York Jets", divisions.get(7)));
        teams.add(new Team("Buffalo Bills", divisions.get(7)));

        return teams;
    }

    private static List<Division> divisions(){
        List<Division> divisions= new ArrayList<>();
        divisions.add(new Division("NFC", "North", 0));
        divisions.add(new Division("NFC", "West", 4));
        divisions.add(new Division("NFC","South", 8));
        divisions.add(new Division("NFC", "East", 12));
        divisions.add(new Division("AFC", "North", 16));
        divisions.add(new Division("AFC", "West", 20));
        divisions.add(new Division("AFC", "South", 24));
        divisions.add(new Division("AFC", "East", 28));
        return divisions;
    }

    private static void teamsToDivisions(List<Team> teams, List<Division> divisions){

        for (Division division : divisions) {
            for(int i=0; i<4; i++){
                division.addTeam(teams.get(i+division.getShift()));
            }
            Collections.shuffle(division.getTeams());
        }

    }
    
    private static void putSameDivisionGames(List<Team> teams, List<Division> divisions){
        /**
         * @Function: Add divsion rivals into the playable matches.
         */

        for (Division division : divisions) {
            for (Team divisionTeam : division.getTeams()) {
                for (int i = 0; i < 4; i++) {
                    if (divisionTeam!=division.getTeams().get(i)){
                        divisionTeam.addOpponent(division.getTeams().get(i));
                        division.getTeams().get(i).addOpponent(divisionTeam);
                        matches.add(new Match(divisionTeam,division.getTeams().get(i)));
                    }
                }
            }
        }
    }

    private static void pairSameConferenceDivisions(List<Division> divisionsMain, List<Team> teams){
        /**
         * @Function: Method's aim is to pair same-conference divisions with each other and add them to the playable matches.
         */
        List<Division> divisions = new ArrayList<>(divisionsMain);

        for(int i=0; i<3;i+=2){
            Division division = divisions.get(i);
            int random = ThreadLocalRandom.current().nextInt(1+i,3+i+1);
            division.setSameConferencePlayed(divisions.get(random));
            divisions.get(random).setSameConferencePlayed(division);
            divisions.remove(i);
            divisions.remove(random-1);
        }

        for(int i=0;i<2;i++){
            divisions.get(0).setSameConferencePlayed(divisions.get(1));
            divisions.get(1).setSameConferencePlayed(divisions.get(0));
            divisions.remove(0);
            divisions.remove(0);
        }

        //TODO: Pointless work by looking through all the teams, see if you can fix.

        for (Team team : teams) {
            Division oppDivison= team.getDivision().getSameConferencePlayed();
            team.setOtherDivision(oppDivison);
            for (int i=0;i<4;i++){

                if(i>1 && !team.getOpponents().contains(oppDivison.getTeams().get(i))){
                    matches.add(new Match(team, oppDivison.getTeams().get(i)));
                    team.getOpponents().add(oppDivison.getTeams().get(i));
                    oppDivison.getTeams().get(i).getOpponents().add(team);
                }
                else if(!team.getOpponents().contains(oppDivison.getTeams().get(i))){
                    matches.add(new Match(oppDivison.getTeams().get(i), team));
                    team.getOpponents().add(oppDivison.getTeams().get(i));
                    oppDivison.getTeams().get(i).getOpponents().add(team);
                }

            }
            int j=1; //For debugging purposes.
        }
    }

    private static void pairOtherConferenceDivision(List<Division> divisions, List<Team> teams){
        /**
         * @Function: Method's aim is to pair other-conference divisions with each other and add them to the playable matches.
         */

        List<Division> divisionsNFC= divisions.subList(0,4);
        Collections.shuffle(divisionsNFC);
        List<Division> divisionsAFC= divisions.subList(4,8);
        Collections.shuffle(divisionsAFC);


        for (int i = 0; i < 4; i++) {
            divisionsNFC.get(i).setOtherConferencePlayed(divisionsAFC.get(i));
            divisionsAFC.get(i).setOtherConferencePlayed(divisionsNFC.get(i));

        }

        for (Team team : teams.subList(0,teams.size()/2)) {
            Division oppDivison = team.getDivision().getOtherConferencePlayed();
            team.setOtherDivision(oppDivison);
            for (int i = 0; i < 4; i++) {

                if (i > 1) {
                    matches.add(new Match(team, oppDivison.getTeams().get(i)));
                } else {
                    matches.add(new Match(oppDivison.getTeams().get(i), team));
                }
                team.getOpponents().add(oppDivison.getTeams().get(i));
                oppDivison.getTeams().get(i).getOpponents().add(team);

            }
            int j = 1; //For debugging purposes.
        }
        int j = 1; //For debugging purposes.
    }

    private static void sameConferenceSingleOpponents(List<Division> divisions, List<Team> teamsMain){
        List<Team> teams = new ArrayList<>(teamsMain);

        for (int i = 0; i < 3; i++) {
            for (Team team : teams) {
                if(i<2){
                    while (true){

                        if (team.getOpponents().size() == 17) {
                            break;
                        }

                        int random= ThreadLocalRandom.current().nextInt(0,32);

                        if(teams.get(random)!=team && !team.getOpponents().contains(teams.get(random))
                                && team.getConference().equals(teams.get(random).getConference())
                                && !team.getOpponents().get(team.getOpponents().size()-1).getDivision().equals(teams.get(random).getDivision())) {


                            team.getOpponents().add(teams.get(random));
                            teams.get(random).getOpponents().add(team);

                            if (i == 0) {
                                matches.add(new Match(team, teams.get(random)));
                            } else {
                                matches.add(new Match(teams.get(random), team));
                            }
                            break;
                        }
                    }
                }
                else{
                    while(true) {

                        if (team.getOpponents().size() == 17) {
                            break;
                        }

                        int random = ThreadLocalRandom.current().nextInt(0, 32);


                        if (teams.get(random) != team && !team.getOpponents().contains(teams.get(random))
                                && !team.getConference().equals(teams.get(random).getConference())
                                && !team.getOtherOppositeDivision().equals(teams.get(random).getDivision())) {


                            team.getOpponents().add(teams.get(random));
                            teams.get(random).getOpponents().add(team);

                            if (true) {
                                matches.add(new Match(team, teams.get(random)));
                            } else {
                                matches.add(new Match(teams.get(random), team));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

}
