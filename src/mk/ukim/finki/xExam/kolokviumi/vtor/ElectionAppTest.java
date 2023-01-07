package mk.ukim.finki.xExam.kolokviumi.vtor;


import java.util.*;
import java.util.stream.Collectors;

class InvalidVotesException extends Exception {

    public InvalidVotesException(int votes, String party, String pollId) {
        super(String.format("%d invalid votes were cast for option %s at poll %s", votes, party, pollId));
    }
}

interface Subscriber {
    void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit);
}

class SeatsApp implements Subscriber {
    private static final int SEATS_PER_UNIT = 20;
    Map<Integer, Map<String, Integer>> votesByPartyAndUnit;

    public SeatsApp() {
        this.votesByPartyAndUnit = new TreeMap<>();
    }

    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {
        this.votesByPartyAndUnit.putIfAbsent(unit, new HashMap<>());
        this.votesByPartyAndUnit.get(unit).putIfAbsent(party, 0);
        this.votesByPartyAndUnit.get(unit).compute(party, (k, v) -> v += votes);
    }

    void printStatistics() {

        Map<String, List<Integer>> totalVotesOfAllUnitsPerParty = new HashMap<>();

        int totalVotes = 0;
        int unitCounter = 0;
        // Party Votes %Votes Seats %Seats. Точниот формат е: %10s %5d %7.2f%% %5d %7.2f%%.
        System.out.println("Party      Votes   %Votes Seats   %Seats");
        for (int unit : votesByPartyAndUnit.keySet()) {

            unitCounter+=1;

            totalVotes += votesByPartyAndUnit.get(unit).values().stream().mapToInt(v -> v).sum();
            Map<String, Integer> seatsPerParty = calculateSeats(votesByPartyAndUnit.get(unit));

            for (String party : seatsPerParty.keySet()) {
                int votes = votesByPartyAndUnit.get(unit).get(party);

                List<Integer> test = new ArrayList<>();
                test.add(0);
                test.add(0);


                totalVotesOfAllUnitsPerParty.putIfAbsent(party, test);
                totalVotesOfAllUnitsPerParty.computeIfPresent(party, (k, v) -> {
                    v.set(0, v.get(0) + votes);
                    v.set(1, v.get(1) + seatsPerParty.get(party));
                    return v;
                });


            }
        }

        Map<Integer, String> result = new TreeMap<>(Comparator.reverseOrder());

        totalVotesOfAllUnitsPerParty.forEach((key, value) -> result.put(value.get(0), key));

        for (String party : result.values()) {
            int votes = totalVotesOfAllUnitsPerParty.get(party).get(0);
            int seats = totalVotesOfAllUnitsPerParty.get(party).get(1);
            System.out.printf("%10s %5d %7.2f%% %5d %7.2f%%\n",
                    party,
                    votes,
                    votes * 100.0 / totalVotes,
                    seats,
                    seats * 100.0 / SEATS_PER_UNIT / unitCounter
            );
        }



    }


    private Map<String, Integer> calculateSeats(Map<String, Integer> votesPerParty) {
        Map<String, Integer> seatsPerParty = new TreeMap<>();
        int totalVotes = votesPerParty.values().stream().mapToInt(v -> v).sum();

        int votesForSeat = totalVotes / SEATS_PER_UNIT;

        for (String party : votesPerParty.keySet()) {
            seatsPerParty.putIfAbsent(party, 0);
            int seats = votesPerParty.get(party) / votesForSeat;
            seatsPerParty.computeIfPresent(party, (k, v) -> v += seats);
        }

        String mostVotesParty = votesPerParty.entrySet().stream().reduce((l, r) -> l.getValue() > r.getValue() ? l : r).get().getKey();
        int assignedSeats = seatsPerParty.values().stream().mapToInt(v -> v).sum();
        if (assignedSeats < SEATS_PER_UNIT) {
            int diff = SEATS_PER_UNIT - assignedSeats;
            seatsPerParty.computeIfPresent(mostVotesParty, (k, v) -> v += diff);
        }

        return seatsPerParty;
    }
}

class VotersTurnoutApp implements Subscriber {
    Map<Integer, Integer> registeredVoters;
    Map<Integer, Integer> castedVotes;

    public VotersTurnoutApp() {
        this.registeredVoters = new HashMap<>();
        this.castedVotes = new HashMap<>();
    }

    @Override
    public void updateVotes(int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit) {
        this.registeredVoters.putIfAbsent(unit, totalVotersPerUnit);
//        this.registeredVoters.compute(unit, (k, v) -> v+=totalVotersPerUnit);

        this.castedVotes.putIfAbsent(unit, 0);
        this.castedVotes.compute(unit, (k, v) -> v += votes);
    }

    void printStatistics() {
        System.out.println("Unit: Casted: Voters: Turnout:");
        for (int unit : registeredVoters.keySet()) {

            int totalVotes = this.registeredVoters.get(unit);
            int castedVoters = this.castedVotes.get(unit);

            System.out.printf("%5d %7d %7d %6.2f%%\n", unit, castedVoters, totalVotes, castedVoters * 100.0 / totalVotes);
        }


    }
}

class VotesController {
    List<String> parties;
    Map<String, Integer> unitPerPoll;
    Map<Integer, ElectionUnit> electionUnitMap;


    public VotesController(List<String> parties, Map<String, Integer> unitPerPoll) {
        this.parties = parties;
        this.unitPerPoll = unitPerPoll;
        this.electionUnitMap = new HashMap<>();
    }

    void addElectionUnit(ElectionUnit electionUnit) {
        this.electionUnitMap.put(electionUnit.unit, electionUnit);
    }

    void addVotes(String pollId, String party, int votes) throws InvalidVotesException {
        if (!parties.contains(party)) throw new InvalidVotesException(votes, party, pollId);
        int unit = unitPerPoll.get(pollId);

        electionUnitMap.get(unit).addVotes(pollId, party, votes);
    }
}

class ElectionUnit {
    int unit;
    Map<String, Integer> votersByPoll;
    List<Subscriber> subscribers;

    public ElectionUnit(int unit, Map<String, Integer> votersByPoll) {
        this.unit = unit;
        this.votersByPoll = votersByPoll;
        this.subscribers = new ArrayList<>();
    }

    //int unit, String pollId, String party, int votes, int totalVotersPerPoll, int totalVotersPerUnit

    void addVotes(String pollId, String party, int votes) {
        this.subscribers.forEach(s -> s.updateVotes(
                this.unit,
                pollId,
                party,
                votes,
                this.votersByPoll.get(pollId),
                this.votersByPoll.values().stream().mapToInt(v -> v).sum()
        ));
    }

    void subscribe(Subscriber subscriber) {
        this.subscribers.add(subscriber);
    }

    void unsubscribe(Subscriber subscriber) {
        this.subscribers.remove(subscriber);
    }
}


public class ElectionAppTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> parties = Arrays.stream(sc.nextLine().split("\\s+")).collect(Collectors.toList());
        Map<String, Integer> unitPerPoll = new HashMap<>();
        Map<Integer, ElectionUnit> electionUnitMap = new TreeMap<>();

        int totalUnits = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < totalUnits; i++) {
            Map<String, Integer> votersPerPoll = new HashMap<>();
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            Integer unit = Integer.parseInt(parts[0]);
            for (int j = 1; j < parts.length; j += 2) {
                String pollId = parts[j];
                int voters = Integer.parseInt(parts[j + 1]);
                unitPerPoll.putIfAbsent(pollId, unit);
                votersPerPoll.put(pollId, voters);
            }

            electionUnitMap.putIfAbsent(unit, new ElectionUnit(unit, votersPerPoll));
        }
        VotesController controller = new VotesController(parties, unitPerPoll);

        electionUnitMap.values().forEach(controller::addElectionUnit);

        VotersTurnoutApp votersTurnoutApp = new VotersTurnoutApp();
        SeatsApp seatsApp = new SeatsApp();


        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String testCase = parts[0];

            if (testCase.equals("subscribe")) { //Example: subscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).subscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).subscribe(seatsApp);
                }
            } else if (testCase.equals("unsubscribe")) { //Example: unsubscribe votersTurnoutApp 1
                int unit = Integer.parseInt(parts[1]);
                String app = parts[2];
                if (app.equals("votersTurnoutApp")) {
                    electionUnitMap.get(unit).unsubscribe(votersTurnoutApp);
                } else {
                    electionUnitMap.get(unit).unsubscribe(seatsApp);
                }
            } else if (testCase.equals("addVotes")) { // Example: addVotes 1234 A 1000
                String pollId = parts[1];
                String party = parts[2];
                int votes = Integer.parseInt(parts[3]);
                try {
                    controller.addVotes(pollId, party, votes);
                } catch (InvalidVotesException e) {
                    System.out.println(e.getMessage());
                }
            } else if (testCase.equals("printStatistics")) {
                String app = parts[1];
                if (app.equals("votersTurnoutApp")) {
                    System.out.println("PRINTING STATISTICS FROM VOTERS TURNOUT APPLICATION");
                    votersTurnoutApp.printStatistics();
                } else {
                    System.out.println("PRINTING STATISTICS FROM SEATS APPLICATION");
                    seatsApp.printStatistics();
                }
            }
        }
    }
}

