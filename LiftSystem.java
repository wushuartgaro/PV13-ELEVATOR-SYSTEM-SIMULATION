/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment;

import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Hard-
 */
public class LiftSystem {

    //for getting statistics from data
    static int noOfButtonPressed = 0, countIntervalPassngerArrival = 0, countTotalPassngerArrival = 0, countTotalStoppedEvent = 0, totalInterArrivalTime = 0,
            beginTime = 0, departureTime = 0, countU = 0, countStopped = 0, countTotalStoppedTime = 0;
    static int[] noOfFirstPassngerArrival = new int[200];
    static int[] noOfIntervalPassngerArrival = new int[200];
    static int[] noOfPassngerArrival = new int[200];
    static int[] noOfAllUpDelayed = new int[200];
    static int[] AllUpDecision = new int[200];
    static int[] noOfLiftStopped = new int[200];
    static int[] noOfCountIntervalPassngerArrival = new int[200];
    static int[] waitingTime = new int[200];
    static int[] noOfPassengerDelayed = new int[200];
    static double[] speedDirect = new double[200];
    static double[] speedStopped = new double[200];
    static int[] allArrivalTime = new int[1000];
    static int[] allArrivalNumber = new int[1000];
    static int[] allStoppedTime = new int[1000];
    static int[][] liftsFloorRecords = new int[200][3];
    static char[][] liftsStateRecords = new char[200][3];
    static int[] departureLiftRecords = new int[200];
    static int[] departureFloorRecords = new int[200];
    static List<ArrayList<LiftState>> liftStateList = new ArrayList<>();
    static int lastPassngerArrivalTime, TotalTimeTravelled, supposeTotalTimeTravelled, averageStoppedTimePerEvent, totalStoppedTime;
    static double[][] speedDirectCategory = new double[200][4];
    static double[][] speedStoppedCategory = new double[200][4];
    static int[] totalNextTripInterval = new int[200];
    static int[] totalInterArrivalAllUp = new int[200];
    
    //for creating empirical distribution
    static double cdf;
    static double[] interArrivalPdfArr, interArrivalCdfArr, noOfPassngerPdfArr, noOfPassngerCdfArr,
            intervalPassengerArrivalPdfArr, intervalPassengerArrivalCdfArr, liftFloorPdfArr, liftFloorCdfArr,
            liftStatePdfArr, liftStateCdfArr, allLiftUpPdfArr, allLiftUpCdfArr, allLiftUpDecisionPdfArr, allLiftUpDecisionCdfArr,
            allLiftUpDelayedPdfArr, allLiftUpDelayedCdfArr, noOfLiftStoppedPdfArr, noOfLiftStoppedCdfArr, liftStoppedTimePdfArr,
            liftStoppedTimeCdfArr, nextTripInvertalPdfArr, nextTripInvertalCdfArr, interArrivalAllUpPdfArr, interArrivalAllUpCdfArr;
    static int[] intervals;
    static int[] x1, x2, x3, x4, x5, x6, x7, x8, x9;
    static int[] averageTimePerFloor;
    static int[] averageTimePerFloorStopped;
    static double[] randomVariate;

    //for uniformity and independen test
    static double[] randomNumberTest;
    static int numOfIntervals;
    private static double significanceLevel, upperD, lowerD, D, criticalValue;
    private static boolean h0Accepted;
    private static int i,l,N,M;
    private static double sumR,rho,sigma,lowerSignificanceValue,upperSignificanceValue,Z;

    //simulation;
    public static final int Q_LIMIT = 1000;
    static char[] possibleLiftState = {'U', 'D', 'I', 'G'};
    public static int liftCapacity = 13;
    static int startTime, endTime;
    static LiftState[] lift;
    static Random random;
    static int arrivalCount;
    static int noOfStoppedEvent;
    static int noOfInvertalArrival;
    private static List<Event> eventList;
    private static String[] nextRecord;
    private static Event e;
    private static DateFormat sdf;
    private static final String FILE_PATH = "src/assignment/modellingdata.csv";
    private static int zCurrent, z0, a, c, m, numOfRandomNum, zC;
    static int z0Passenger;
    static ArrayList<Event> list;
    
    //calculate point estiamte for output analysis
    static int[] sumLiftTrips,sumNumPassngers,sumWaitTime,sumPassengerDelayed,sumInterArrival;
    static double[] sumAvgWaitTime,sumProbabilityDelayed,sumProbabilityPassengerDelayed,sumAvgInterArrival,sumActualWaiting;
    static int numOfInterval,noOfRuns,numOfRuns;
    static double sampleMean, sampleVariance, confidenceLevel, sum, difference,confidenceInterval;

    public static class LiftState {

        private int liftNo;
        private int floor;
        private char state;

        LiftState() {
            this(0, 0, '\u0000');
        }

        LiftState(int liftNo, int floor, char state) {
            this.liftNo = liftNo;
            this.floor = floor;
            this.state = state;
        }

        public int getLiftNo() {
            return liftNo;
        }

        public String displayLiftNo() {
            return "L" + liftNo;
        }

        public void setLiftNo(int liftNo) {
            this.liftNo = liftNo;
        }

        public int getFloor() {
            return floor;
        }

        public String displayFloor() {
            return Integer.toString(floor);
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public char getState() {
            return state;
        }

        public void setState(char state) {
            this.state = state;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final LiftState other = (LiftState) obj;
            if (!Objects.equals(this.floor, other.floor)) {
                return false;
            } else if (this.state != other.state) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "L" + liftNo + "(" + this.displayFloor() + "," + state + ")";
        }

        public void display() {
            System.out.print("LiftState{Lift No:L" + liftNo + "floor:" + this.displayFloor() + ", state:" + state + "}");
        }
    }

    public static class Event implements Comparable<Event> {

        private String time;
        private String event;
        private String details;
        private String details2;

        public Event() {
            this("", "", "");
        }

        public Event(String time, String event, String details) {
            this.time = time;
            this.event = event;
            this.details = details;
        }

        public static Comparator<Event> getEventComparator() {
            return EventComparator;
        }

        public static void setEventComparator(Comparator<Event> EventComparator) {
            Event.EventComparator = EventComparator;
        }

        public String getTime() {
            return time;
        }

        public int getTimeInInteger() {
            return Integer.parseInt(time);
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getDetails2() {
            return details2;
        }

        public void setDetails2(String details2) {
            this.details2 = details2;
        }

        @Override
        public int compareTo(Event compareEvent) {
            return this.time.compareTo(compareEvent.getTime());
        }

        public static Comparator<Event> EventComparator
                = new Comparator<Event>() {
            @Override
            public int compare(Event event1, Event event2) {
                return event1.time.compareTo(event2.getTime());
            }
        };

        public String[] csvWriter() {

            String[] writeline = new String[4];
            writeline[0] = this.time;
            writeline[1] = this.event;
            writeline[2] = this.details;

            return writeline;
        }

        public void display() {
            System.out.println("(" + time + ", " + event + ", " + details + ")"); //event notices
        }
    }

    public static class State {

        static int numOfPassengersInQueue, numOfPassngersDelayed, clock;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Main started");
        readDataFile();
        processData();
        initialize();

        for (int i = 0; i < numOfRandomNum; i++) {
            randomNumberTest[i] = (double) zCurrent / m;
            //System.out.print("="+i+"+"+zCurrent+","+randomNumberTest[i]);
            zCurrent = generateNextRandomNumbersLCG();
        }
        
        //Random Number Uniformity and Independence Test
        //h0Accepted = kolmogorovSmirnovTest();
        //uniformityTestResult();
        //testForAutocorrelation();
        noOfRuns = 1;
        
        //Output Analysis
        sumLiftTrips = new int[noOfRuns];
        sumNumPassngers = new int[noOfRuns];
        sumWaitTime = new int[noOfRuns];
        sumPassengerDelayed = new int[noOfRuns];
        sumInterArrival = new int[noOfRuns];
        
        sumAvgWaitTime = new double[noOfRuns];
        sumProbabilityDelayed = new double[noOfRuns];
        sumProbabilityPassengerDelayed = new double[noOfRuns];
        sumAvgInterArrival = new double[noOfRuns];
        sumActualWaiting = new double[noOfRuns];
        
        numOfRuns = noOfRuns;
        while (numOfRuns > 0) {
            System.out.println("===============Runs： " + (11 - numOfRuns) + "===============");
            initialize();
            zCurrent = zC;
            simulate();
            report();
            numOfRuns--;
        }
        
        System.out.println("\nOutput Analysis\n===================\nConfidence Level: "+confidenceLevel +" Number of Runs: "+noOfRuns);
        System.out.println("Confidence Interval: "+confidenceLevel);
        System.out.println("Number of Lift Trips");
        calculatePointEstimate(sumLiftTrips);
        System.out.println("Number of Passengers");
        calculatePointEstimate(sumNumPassngers);
        System.out.println("Waiting Time");
        calculatePointEstimate(sumWaitTime);
        System.out.println("Number of Passengers Delayed");
        calculatePointEstimate(sumPassengerDelayed);
        System.out.println("Passenger Interarrival Time");
        calculatePointEstimate(sumInterArrival);
        
        System.out.println("\nAverage Waiting Time for a lift trip");
        calculatePointEstimate(sumAvgWaitTime);
        System.out.println("\nProbability that a round of passenger delayed");
        calculatePointEstimate(sumProbabilityDelayed);
        System.out.println("\nProbability that a passenger delayed for the next lift trip");
        calculatePointEstimate(sumProbabilityPassengerDelayed);
        System.out.println("\nAverage time between arrival");
        calculatePointEstimate(sumAvgInterArrival);
        System.out.println("\nAverage waiting time for the passenger who waits");
        calculatePointEstimate(sumActualWaiting);
        
        report2();
       
    }

    public static void readDataFile() throws IOException {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(FILE_PATH));
                CSVReader csvReader = new CSVReader(reader);) {
            // Reading Records One by One in a String array

            eventList = new ArrayList<>();
            sdf = new SimpleDateFormat("HH:mm:ss");

            while ((nextRecord = csvReader.readNext()) != null) {
                e = new Event();

                String[] h1 = nextRecord[0].split(":");

                int hour = Integer.parseInt(h1[0]);
                int minute = Integer.parseInt(h1[1]);
                int second = Integer.parseInt(h1[2]);

                int clockSecond = second + (60 * minute) + (3600 * hour);

                e.setTime(Integer.toString(clockSecond));
                e.setEvent(nextRecord[1]);
                e.setDetails(nextRecord[2]);
                e.setDetails2(nextRecord[3]);
                eventList.add(e);
            }
            /*
            for (Event event : eventList) {
                System.out.print(event.getTime());
                System.out.print("|" + event.getEvent());
                System.out.print("|" + event.getDetails());
                if (event.getDetails2() != null) {
                    System.out.print("|" + event.getDetails2() + "\r\n");
                } else {
                    System.out.print("\r\n");
                }
            }
             */
        }
    }

    public static void initialize() {
        list = new ArrayList<>();
        startTime = 0;
        endTime = 21600;
        random = new Random();// create random value with specified seed
        z0Passenger = 1;
        //Event endevent = new Event("21600", "END", "Simulation Ended");
        //list.add(endevent);

        //Event firstEvent = new Event("0","Passenger Arrival","1");
        //list.add(firstEvent);
        State.clock = 0;
        State.numOfPassengersInQueue = 0;
        State.numOfPassngersDelayed = 0;
        arrivalCount = 0;

        z0 = 7;
        zCurrent = z0;
        a = 5;
        c = 3;
        m = 8192;
        numOfRandomNum = 600;
        numOfIntervals = 10;
        randomNumberTest = new double[numOfRandomNum];
        significanceLevel = 0.05;
        
        i = 3;
        l = 5;
        N = 30;
        M = (int)Math.round(((double)(N-i)/l)-1);
        sumR = 0.0;
        rho = 0.0;
        sigma = 0.0;
        upperSignificanceValue = 1.96;
        lowerSignificanceValue = -1.96;
        Z = 0.0;
        
        sum = 0.0;
        difference = 0.0;
        numOfInterval = 10;
        confidenceLevel = 1-((1-(90.0/100.0))/2);//0.05

    }

    public static void simulate() {
        int cCount = 0;
        int currentPointer = 0;

        while (State.clock < endTime) {
            passengerArrival(startTime);
            for (int i = currentPointer; i < list.size(); i++) {
                cCount++;
                Event currentEvent = list.get(i);
                State.clock = currentEvent.getTimeInInteger();
                System.out.println("\n---"+cCount + "-------------------------------------------------\n Clock " + State.clock);
                currentEvent.display();
                currentPointer = i + 1;
            }
            startTime = randomNextTripTime();
        }
        zC = zCurrent;
    }

    public static void passengerArrival(int randomInterarrivalTime) {
        State.clock += randomInterarrivalTime;
        int currentPassengerNumber = randomPassengerNumber();
        list.add(new Event(Integer.toString(State.clock), "Passenger Arrival", Integer.toString(currentPassengerNumber)));
        State.numOfPassengersInQueue += currentPassengerNumber;
        arrivalCount++;

        if (State.clock > 21600) {
            return;
        }
        if (arrivalCount == 1) {
            buttonPressed();
        } else {
            if (State.numOfPassengersInQueue > Q_LIMIT) {
                /* The queue has overflowed, so stop the simulation. */
                System.out.printf("\nOverflow of queue at");
                System.out.printf(" time %d", State.clock);
                System.exit(2);
            }
        }
    }

    public static void buttonPressed() {
        int countU = 0, countD = 0, countI = 0;
        lift = new LiftState[4];
        StringBuilder allLiftsStates = new StringBuilder();
        for (int i = 1; i < 4; i++) {
            lift[i] = new LiftState(i, randomLiftFloor(), randomLiftState());
            if (lift[i].getState() == 'G') {
                lift[i].setFloor(0);
            }
            allLiftsStates.append(lift[i].toString());
        }

        list.add(new Event(Integer.toString(State.clock), "Button Pressed", allLiftsStates.toString()));

        
        //Current Lift Assignment Mechanism
        //Assign which lift to go to ground floor
        int minFloor = 0;

        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'G') {
                //if there is lift at ground floor then directly assign and depart
                liftDeparture(lift[i].getLiftNo(), State.clock);
                break;
            }
            if (lift[i].getState() == 'D') {
                //initialize the minfloor with the floor of the first lift which is going down 
                minFloor = lift[i].getFloor();
                break;
            }
        }

        //find which lift is currently going down and is at the lowest floor
        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'D' && lift[i].getFloor() < minFloor) {
                minFloor = lift[i].getFloor();
            }
        }

        //assign the lift which is currently going down and is at the lowest floor
        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'D') {
                countD++;
                if (lift[i].getFloor() == minFloor) {
                    liftDeparture(lift[i].getLiftNo(), State.clock);
                    break;
                }
            } else if (lift[i].getState() == 'U') {
                //count how many lift is currently going up
                countU++;
            }
        }

        if (countU == 3) {
            //delayed after the button pressed because all lifts are going up
            State.clock += randomAllLiftUpDelayed();

            noOfInvertalArrival = randomInterArrivalAllUp();
            for (int i = 0; i < noOfInvertalArrival; i++) {
                passengerArrival(randomInterarrival());
            }
            liftDeparture(lift[randomPickLiftForNextDeparture()].getLiftNo(), State.clock);

        } else if (countD == 0) {
            for (int i = 1; i < 4; i++) {
                //assign the lift which is idle
                if (lift[i].getState() == 'I') {
                    liftDeparture(lift[i].getLiftNo(), State.clock);
                    break;
                }
            }
        }
        
        /*
        //Proposed Lift Assignment Mechanism
        int minFloorD = 25, minFloorI = 25;

        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'G') {
                //if there is lift at ground floor then directly assign and depart
                liftDeparture(lift[i].getLiftNo(), State.clock);
                break;
            }

            if (lift[i].getState() == 'I') {
                //initialize the minfloor with the floor of the first lift which is going down 
                minFloorI = lift[i].getFloor();
                break;
            }

            if (lift[i].getState() == 'D') {
                //initialize the minfloor with the floor of the first lift which is going down 
                minFloorD = lift[i].getFloor();
                break;
            }
        }

        //find which lift is currently idle and is at the lowest floor
        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'I' && lift[i].getFloor() < minFloorI) {
                minFloorI = lift[i].getFloor();
            }
        }

        //find which lift is currently going down and is at the lowest floor
        for (int i = 1; i < 4; i++) {
            if (lift[i].getState() == 'D' && lift[i].getFloor() < minFloorD) {
                minFloorD = lift[i].getFloor();
            }
        }

        if (minFloorI < minFloorD) {
            //assign the lift which is idle
            for (int i = 1; i < 4; i++) {
                if (lift[i].getState() == 'I') {
                    if (lift[i].getFloor() == minFloorI) {
                        liftDeparture(lift[i].getLiftNo(), State.clock);
                        break;
                    }
                }
            }
        } else {
            //assign the lift which is currently going down and is at the lowest floor
            for (int i = 1; i < 4; i++) {
                if (lift[i].getState() == 'D') {
                    countD++;
                    if (lift[i].getFloor() == minFloorD) {
                        liftDeparture(lift[i].getLiftNo(), State.clock);
                        break;
                    }
                } else if (lift[i].getState() == 'U') {
                    //count how many lift is currently going up
                    countU++;
                }
            }

            if (countU == 3) {
                //delayed after the button pressed because all lifts are going up
                State.clock += randomAllLiftUpDelayed();

                noOfInvertalArrival = randomInterArrivalAllUp();
                for (int i = 0; i < noOfInvertalArrival; i++) {
                    passengerArrival(randomInterarrival());
                }
                liftDeparture(lift[randomPickLiftForNextDeparture()].getLiftNo(), State.clock);
            }
        }
        */
    }

    public static void liftDeparture(int liftNo, int departureTime) {
        list.add(new Event(Integer.toString(departureTime), "Lift Departure", lift[liftNo].displayLiftNo() + " , " + lift[liftNo].displayFloor()));

        if (lift[liftNo].getState() == 'G') {
            enterLift(liftNo);
        } else {
            schduleIntervalPassengerArrivalAndStoppedEvent(liftNo);
            enterLift(liftNo);
        }
    }

    public static void schduleIntervalPassengerArrivalAndStoppedEvent(int liftNo) {

        noOfInvertalArrival = randomNoOfIntervalPassngerArrival();
        for (int i = 0; i < noOfInvertalArrival; i++) {
            passengerArrival(randomInterarrival());
        }

        noOfStoppedEvent = randomNoOfLiftStopped();

        if (noOfStoppedEvent < lift[liftNo].getFloor()) {
            for (int i = 0; i < noOfStoppedEvent; i++) {
                State.clock += randomStoppedTime();
                liftStopped(liftNo, State.clock);
            }
        }
    }

    public static void liftStopped(int liftNo, int stoppedTime) {
        list.add(new Event(Integer.toString(stoppedTime), "Lift Stopped", lift[liftNo].displayLiftNo()));
    }

    public static void enterLift(int liftNo) {

        if (lift[liftNo].getState() != 'G') {
            State.clock += liftTravelTime(lift[liftNo].getFloor());
        }

        int numOfCurrentDelayed = State.numOfPassengersInQueue - liftCapacity;

        if (numOfCurrentDelayed > 0) {
            State.numOfPassngersDelayed += numOfCurrentDelayed;
        } else {
            numOfCurrentDelayed = 0;
        }

        list.add(new Event(Integer.toString(State.clock), "Enter Lift", lift[liftNo].displayLiftNo() + "," + Integer.toString(numOfCurrentDelayed)));

        State.numOfPassengersInQueue = 0;
        arrivalCount = 0;
    }

    public static void report() {

        int buttonPressedTime = 0, enterLiftTime, numberOfPassengersArrived = 0, waitingTime, numOfDelayed;
        int totalPassengerArrived = 0, totalPassengerDelayed = 0, totalWaitingTime = 0, totalInterArrival = 0, totalPassengerWaited = 0,
                totalNumOfTrip = 0, totalNumofInterarrival = 0, totalDelayed = 0;
        System.out.printf("\n%15s|%12s|%15s|%15s|%12s|%12s\n", "Clock(seconds)", "Number of Passengers Arrived", "Button Pressed Time", "Enter Lift Time", "Waiting Time", "Number of Passengrs Delayed");

        for (int i = 0; i < list.size(); i++) {

            switch (list.get(i).getEvent()) {
                case "Passenger Arrival":
                    numberOfPassengersArrived += Integer.parseInt(list.get(i).getDetails());
                    totalPassengerArrived += numberOfPassengersArrived;
                    totalInterArrival += list.get(i).getTimeInInteger();
                    totalNumofInterarrival++;
                    break;
                case "Button Pressed":
                    buttonPressedTime = list.get(i).getTimeInInteger();
                    break;
                case "Enter Lift":
                    enterLiftTime = list.get(i).getTimeInInteger();
                    waitingTime = enterLiftTime - buttonPressedTime;
                    if (waitingTime != 0) {
                        totalPassengerWaited += numberOfPassengersArrived;
                    }
                    numOfDelayed = Integer.parseInt(list.get(i).getDetails().substring(3, list.get(i).getDetails().length()));
                    //System.out.printf("%15s|%28d|%19s|%15s|%12d|%12d\n", buttonPressedTime, numberOfPassengersArrived, buttonPressedTime, enterLiftTime, waitingTime, numOfDelayed);
                    
                    if(numOfDelayed !=0){
                        totalDelayed++;
                    }
                    numberOfPassengersArrived = 0;
                    totalPassengerDelayed += numOfDelayed;
                    totalWaitingTime += waitingTime;
                    totalNumOfTrip++;
                    break;
            }

        }
        
        
        sumLiftTrips[numOfRuns-1] = totalNumOfTrip;
        sumNumPassngers[numOfRuns-1] = totalPassengerArrived;
        sumWaitTime[numOfRuns-1] = totalWaitingTime;
        sumPassengerDelayed[numOfRuns-1] = totalPassengerDelayed;
        sumInterArrival[numOfRuns-1] = totalInterArrival;
        
        
        System.out.printf("\n\nTotal Number of Lift Trips:%15d\nTotal Number of Passengers:%15d \nTotal Waiting Time:%24d seconds \nTotal Numper of Passenger Delayed: %6d\n"
                + "Total Interrarrival: %23d seconds\n" + "Total Number of Interrarrival: %23d \n",
                totalNumOfTrip, totalPassengerArrived, totalWaitingTime, totalPassengerDelayed, totalInterArrival, totalNumofInterarrival);
        System.out.printf("\n\nAverage waiting time for a lift trip %26.3f seconds\n\n",
                (double) totalWaitingTime / totalNumOfTrip);
        System.out.printf("Probability that a round of passenger delayed %7.3f\n\n",
                (double) totalDelayed / totalNumOfTrip);
        System.out.printf("Probability that a passenger delayed for the next lift %7.3f\n\n",
                (double) totalPassengerDelayed / totalPassengerArrived);
        System.out.printf("Average time between arrival and expected time arrival %9.3f seconds\n\n",
                (double) totalInterArrival / totalPassengerArrived);
        System.out.printf("Average waiting time for the passenger who waits %16.3f seconds\n\n",
                (double) totalWaitingTime / totalPassengerWaited);
        
        //System.out.println("****"+numOfRuns);
        sumAvgWaitTime[numOfRuns-1] = (double)Math.round(((double) totalWaitingTime / totalNumOfTrip)*1000)/1000;
        sumProbabilityDelayed[numOfRuns-1] =  (double)Math.round(((double) totalDelayed / totalNumOfTrip)*1000)/1000;
        sumProbabilityPassengerDelayed[numOfRuns-1] =  (double)Math.round(((double) totalPassengerDelayed / totalPassengerArrived)*1000)/1000;
        sumAvgInterArrival[numOfRuns-1] =  (double)Math.round(((double) totalInterArrival / totalPassengerArrived)*1000)/1000;
        sumActualWaiting[numOfRuns-1] =  (double)Math.round(((double) totalWaitingTime / totalPassengerWaited)*1000)/1000;
      
    }

    public static void report2() {
        System.out.println("\n===============Existing System===============\n");
        int buttonPressedTime = 0, enterLiftTime, numberOfPassengersArrived = 0, waitingTime, numOfDelayed;
        int totalPassengerArrived = 0, totalPassengerDelayed = 0, totalWaitingTime = 0, totalInterArrival = 0, totalPassengerWaited = 0,
                totalNumOfTrip = 0, totalNumofInterarrival = 0, totalDelayed = 0;
        System.out.printf("\n%15s|%12s|%15s|%15s|%12s|%12s\n", "Clock(seconds)", "Number of Passengers Arrived", "Button Pressed Time", "Enter Lift Time", "Waiting Time", "Number of Passengrs Delayed");

        for (int i = 0; i < eventList.size(); i++) {

            switch (eventList.get(i).getEvent()) {
                case "Passenger Arrival":
                    numberOfPassengersArrived += Integer.parseInt(eventList.get(i).getDetails());
                    totalPassengerArrived += numberOfPassengersArrived;
                    totalInterArrival += eventList.get(i).getTimeInInteger();
                    totalNumofInterarrival++;
                    break;
                case "Button Pressed":
                    buttonPressedTime = eventList.get(i).getTimeInInteger();
                    break;
                case "Enter Lift":
                    enterLiftTime = eventList.get(i).getTimeInInteger();
                    waitingTime = enterLiftTime - buttonPressedTime;
                    if (waitingTime != 0) {
                        totalPassengerWaited += numberOfPassengersArrived;
                    }
                    numOfDelayed = Integer.parseInt(eventList.get(i).getDetails2());
                    //System.out.printf("%15s|%28d|%19s|%15s|%12d|%12d\n", buttonPressedTime, numberOfPassengersArrived, buttonPressedTime, enterLiftTime, waitingTime, numOfDelayed);
                    if(numOfDelayed !=0){
                        totalDelayed++;
                    }
                    
                    numberOfPassengersArrived = 0;
                    totalPassengerDelayed += numOfDelayed;
                    totalWaitingTime += waitingTime;
                    totalNumOfTrip++;
                    break;
            }

        }

        System.out.printf("\n\nTotal Number of Lift Trips:%15d\nTotal Number of Passengers:%15d \nTotal Waiting Time:%24d seconds \nTotal Numper of Passenger Delayed: %6d\n"
                + "Total Interrarrival: %23d seconds\n" + "Total Number of Interrarrival: %23d \n",
                totalNumOfTrip, totalPassengerArrived, totalWaitingTime, totalPassengerDelayed, totalInterArrival, totalNumofInterarrival);
        System.out.printf("\n\nAverage waiting time for a lift trip %26.3f seconds\n\n",
                (double) totalWaitingTime / totalNumOfTrip);
        System.out.printf("Probability that a round of passenger delayed %7.3f\n\n",
                (double) totalDelayed / totalNumOfTrip);
        System.out.printf("Probability that a passenger delayed for the next lift trip %7.3f\n\n",
                (double) totalPassengerDelayed / totalPassengerArrived);
        System.out.printf("Average time between arrival %9.3f seconds\n\n",
                (double) totalInterArrival / totalPassengerArrived);
        System.out.printf("Average waiting time for the passenger who waits %16.3f seconds\n\n",
                (double) totalWaitingTime / totalPassengerWaited);

    }

    public static int randomInterarrival() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println("Interarrival: "+randomDouble);
        if (randomDouble < interArrivalCdfArr[0]) {
            return (int) Math.round(1 + (interArrivalPdfArr[0] / x1[0]) * (randomDouble - 0));
        } else if (randomDouble < interArrivalCdfArr[1]) {
            return (int) Math.round(x1[0] + (interArrivalPdfArr[1] / x1[0]) * (randomDouble - interArrivalCdfArr[0]));
        } else if (randomDouble < interArrivalCdfArr[2]) {
            return (int) Math.round(x1[1] + (interArrivalPdfArr[2] / x1[0]) * (randomDouble - interArrivalCdfArr[1]));
        } else if (randomDouble < interArrivalCdfArr[3]) {
            return (int) Math.round(x1[2] + (interArrivalPdfArr[3] / x1[0]) * (randomDouble - interArrivalCdfArr[2]));
        } else if (randomDouble <= interArrivalCdfArr[4]) {
            return (int) Math.round(x1[3] + (interArrivalPdfArr[4] / x1[0]) * (randomDouble - interArrivalCdfArr[3]));
        } else if (randomDouble <= interArrivalCdfArr[5]) {
            return (int) Math.round(x1[4] + (interArrivalPdfArr[5] / x1[0]) * (randomDouble - interArrivalCdfArr[4]));
        } else if (randomDouble <= interArrivalCdfArr[6]) {
            return (int) Math.round(x1[5] + (interArrivalPdfArr[6] / x1[0]) * (randomDouble - interArrivalCdfArr[5]));
        } else if (randomDouble <= interArrivalCdfArr[7]) {
            return (int) Math.round(x1[6] + (interArrivalPdfArr[7] / x1[0]) * (randomDouble - interArrivalCdfArr[6]));
        }
        return 0;
    }

    public static int randomPassengerNumber() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;

        if (randomDouble < noOfPassngerCdfArr[0]) {
            return (int) Math.round(1 + (noOfPassngerPdfArr[0] / x2[0]) * (randomDouble - 0));
        } else if (randomDouble < interArrivalCdfArr[1]) {
            return (int) Math.round(x2[0] + (noOfPassngerPdfArr[1] / x2[0]) * (randomDouble - noOfPassngerCdfArr[0]));
        } else if (randomDouble < interArrivalCdfArr[2]) {
            return (int) Math.round(x2[1] + (noOfPassngerPdfArr[2] / x2[0]) * (randomDouble - noOfPassngerCdfArr[1]));
        } else if (randomDouble < interArrivalCdfArr[3]) {
            return (int) Math.round(x2[2] + (noOfPassngerPdfArr[3] / x2[0]) * (randomDouble - noOfPassngerCdfArr[2]));
        }

        return 0;
    }

    public static int randomNoOfIntervalPassngerArrival() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < intervalPassengerArrivalCdfArr[0]) {
            return (int) Math.round(0 + (intervalPassengerArrivalPdfArr[0] / x3[0]) * (randomDouble - 0));
        } else if (randomDouble < intervalPassengerArrivalCdfArr[1]) {
            return (int) Math.round(x3[0] + (intervalPassengerArrivalPdfArr[1] / x3[0]) * (randomDouble - intervalPassengerArrivalCdfArr[0]));
        } else if (randomDouble < intervalPassengerArrivalCdfArr[2]) {
            return (int) Math.round(x3[1] + (intervalPassengerArrivalPdfArr[2] / x3[0]) * (randomDouble - intervalPassengerArrivalCdfArr[1]));
        } else if (randomDouble < intervalPassengerArrivalCdfArr[3]) {
            return (int) Math.round(x3[2] + (intervalPassengerArrivalPdfArr[3] / x3[0]) * (randomDouble - intervalPassengerArrivalCdfArr[2]));
        } else if (randomDouble <= intervalPassengerArrivalCdfArr[4]) {
            return (int) Math.round(x3[3] + (intervalPassengerArrivalPdfArr[4] / x3[0]) * (randomDouble - intervalPassengerArrivalCdfArr[3]));
        }
        return 0;
    }

    public static int randomLiftFloor() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < liftFloorCdfArr[0]) {
            return (int) Math.round(1 + (liftFloorPdfArr[0] / x4[0]) * (randomDouble - 0));
        } else if (randomDouble < liftFloorCdfArr[1]) {
            return (int) Math.round(x4[0] + (liftFloorPdfArr[1] / x4[0]) * (randomDouble - liftFloorCdfArr[0]));
        } else if (randomDouble < liftFloorCdfArr[2]) {
            return (int) Math.round(x4[1] + (liftFloorPdfArr[2] / x4[0]) * (randomDouble - liftFloorCdfArr[1]));
        } else if (randomDouble <= liftFloorCdfArr[3]) {
            return (int) Math.round(x4[2] + (liftFloorPdfArr[3] / x4[0]) * (randomDouble - liftFloorCdfArr[2]));
        }
        return 0;
    }

    public static char randomLiftState() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < liftStateCdfArr[0]) {
            return possibleLiftState[0];
        } else if (randomDouble < liftStateCdfArr[1]) {
            return possibleLiftState[1];
        } else if (randomDouble < liftStateCdfArr[2]) {
            return possibleLiftState[2];
        } else if (randomDouble <= liftStateCdfArr[3]) {
            return possibleLiftState[3];
        }
        return '\u0000';
    }

    public static int randomPickLiftForNextDeparture() {
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < allLiftUpDecisionCdfArr[0]) {
            return 1;
        } else if (randomDouble < allLiftUpDecisionCdfArr[1]) {
            return 2;
        } else if (randomDouble <= allLiftUpDecisionCdfArr[2]) {
            return 3;
        }
        return 0;
    }

    public static int randomAllLiftUpDelayed() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < allLiftUpDelayedCdfArr[0]) {
            return (int) Math.round(1 + (allLiftUpDelayedPdfArr[0] / x5[0]) * (randomDouble - 0));
        } else if (randomDouble < allLiftUpDelayedCdfArr[1]) {
            return (int) Math.round(x5[0] + (allLiftUpDelayedPdfArr[1] / x5[0]) * (randomDouble - allLiftUpDelayedCdfArr[0]));
        } else if (randomDouble < allLiftUpDelayedCdfArr[2]) {
            return (int) Math.round(x5[1] + (allLiftUpDelayedPdfArr[2] / x5[0]) * (randomDouble - allLiftUpDelayedCdfArr[1]));
        } else if (randomDouble < allLiftUpDelayedCdfArr[3]) {
            return (int) Math.round(x5[2] + (allLiftUpDelayedPdfArr[3] / x5[0]) * (randomDouble - allLiftUpDelayedCdfArr[2]));
        } else if (randomDouble <= allLiftUpDelayedCdfArr[4]) {
            return (int) Math.round(x5[3] + (allLiftUpDelayedPdfArr[4] / x5[0]) * (randomDouble - allLiftUpDelayedCdfArr[3]));
        }
        return 0;
    }

    public static int liftTravelTime(int floorNo) {
        
        if (floorNo < 6) {
            return averageTimePerFloor[0] * floorNo;
        } else if (floorNo < 12) {
            return averageTimePerFloor[1] * floorNo;
        } else if (floorNo < 18) {
            return averageTimePerFloor[2] * floorNo;
        } else if (floorNo < 24) {
            return averageTimePerFloor[3] * floorNo;
        }
        
        /*
        //Proposed Solution
        if (floorNo < 6) {
            return 5 * floorNo;
        } else if (floorNo < 12) {
            return 3 * floorNo;
        } else if (floorNo < 18) {
            return 3 * floorNo;
        } else if (floorNo < 24) {
            return (int)Math.round(1.5 * floorNo);
        }
        */
        return 0;
        
    }
    
    public static int randomNoOfLiftStopped() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < noOfLiftStoppedCdfArr[0]) {
            return x6[0];
        } else if (randomDouble < noOfLiftStoppedCdfArr[1]) {
            return x6[1];
        } else if (randomDouble < noOfLiftStoppedCdfArr[2]) {
            return x6[2];
        } else if (randomDouble < noOfLiftStoppedCdfArr[3]) {
            return x6[3];
        } else if (randomDouble < noOfLiftStoppedCdfArr[3]) {
            return x6[3];
        } else if (randomDouble < noOfLiftStoppedCdfArr[4]) {
            return x6[4];
        } else if (randomDouble < noOfLiftStoppedCdfArr[5]) {
            return x6[5];
        } else if (randomDouble < noOfLiftStoppedCdfArr[6]) {
            return x6[6];
        } else if (randomDouble < noOfLiftStoppedCdfArr[7]) {
            return x6[7];
        }
        return 0;
    }

    public static int randomStoppedTime() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < liftStoppedTimeCdfArr[0]) {
            return (int) Math.round(1 + (liftStoppedTimePdfArr[0] / x7[0]) * (randomDouble - 0));
        } else if (randomDouble < liftStoppedTimeCdfArr[1]) {
            return (int) Math.round(x7[0] + (liftStoppedTimePdfArr[1] / x7[0]) * (randomDouble - liftStoppedTimeCdfArr[0]));
        } else if (randomDouble < liftStoppedTimeCdfArr[2]) {
            return (int) Math.round(x7[1] + (liftStoppedTimePdfArr[2] / x7[0]) * (randomDouble - liftStoppedTimeCdfArr[1]));
        } else if (randomDouble < liftStoppedTimeCdfArr[3]) {
            return (int) Math.round(x7[2] + (liftStoppedTimePdfArr[3] / x7[0]) * (randomDouble - liftStoppedTimeCdfArr[2]));
        } else if (randomDouble <= liftStoppedTimeCdfArr[4]) {
            return (int) Math.round(x7[3] + (liftStoppedTimePdfArr[4] / x7[0]) * (randomDouble - liftStoppedTimeCdfArr[3]));
        }
        return 0;
    }

    public static int randomNextTripTime() {
        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < nextTripInvertalCdfArr[0]) {
            return (int) Math.round(1 + (nextTripInvertalPdfArr[0] / x8[0]) * (randomDouble - 0));
        } else if (randomDouble < nextTripInvertalCdfArr[1]) {
            return (int) Math.round(x8[0] + (nextTripInvertalPdfArr[1] / x8[0]) * (randomDouble - nextTripInvertalCdfArr[0]));
        } else if (randomDouble < nextTripInvertalCdfArr[2]) {
            return (int) Math.round(x8[1] + (nextTripInvertalPdfArr[2] / x8[0]) * (randomDouble - nextTripInvertalCdfArr[1]));
        } else if (randomDouble < nextTripInvertalCdfArr[3]) {
            return (int) Math.round(x8[2] + (nextTripInvertalPdfArr[3] / x8[0]) * (randomDouble - nextTripInvertalCdfArr[2]));
        } else if (randomDouble <= nextTripInvertalCdfArr[4]) {
            return (int) Math.round(x8[3] + (nextTripInvertalPdfArr[4] / x8[0]) * (randomDouble - nextTripInvertalCdfArr[3]));
        }
        return 0;
    }

    public static int randomInterArrivalAllUp() {

        //using Empirical Distribution
        zCurrent = generateNextRandomNumbersLCG();
        double randomDouble = (double) zCurrent / m;
        //System.out.println(randomDouble);
        if (randomDouble < interArrivalAllUpCdfArr[0]) {
            return (int) Math.round(1 + (interArrivalAllUpPdfArr[0] / x9[0]) * (randomDouble - 0));
        } else if (randomDouble < interArrivalAllUpCdfArr[1]) {
            return (int) Math.round(x9[0] + (interArrivalAllUpPdfArr[1] / x9[0]) * (randomDouble - interArrivalAllUpCdfArr[0]));
        } else if (randomDouble < interArrivalAllUpCdfArr[2]) {
            return (int) Math.round(x9[1] + (interArrivalAllUpPdfArr[2] / x9[0]) * (randomDouble - interArrivalAllUpCdfArr[1]));
        } else if (randomDouble < interArrivalAllUpCdfArr[3]) {
            return (int) Math.round(x9[2] + (interArrivalAllUpPdfArr[3] / x9[0]) * (randomDouble - interArrivalAllUpCdfArr[2]));
        } else if (randomDouble <= interArrivalAllUpCdfArr[4]) {
            return (int) Math.round(x9[3] + (interArrivalAllUpPdfArr[4] / x9[0]) * (randomDouble - interArrivalAllUpCdfArr[3]));
        }
        return 0;
    }

    public static int generateNextRandomNumbersLCG() {
        return (a * zCurrent + c) % m;
    }

    public static void processData() {
        for (int i = 0; i < 200; i++) {
            noOfFirstPassngerArrival[i] = 0;
            noOfIntervalPassngerArrival[i] = 0;
            noOfPassngerArrival[i] = 0;
            noOfAllUpDelayed[i] = 0;
            AllUpDecision[i] = 0;
            noOfLiftStopped[i] = 0;
            noOfCountIntervalPassngerArrival[i] = 0;
            waitingTime[i] = 0;
            noOfPassengerDelayed[i] = 0;
            speedDirect[i] = 0.0;
            speedStopped[i] = 0.0;
            totalNextTripInterval[i] = 0;
            totalInterArrivalAllUp[i] = 0;

            for (int a = 0; a < 3; a++) {
                liftsFloorRecords[i][a] = 0;
                liftsStateRecords[i][a] = ' ';
            }

            for (int a = 0; a < 4; a++) {
                speedDirectCategory[i][a] = 0.0;
                speedStoppedCategory[i][a] = 0.0;
            }

            departureLiftRecords[i] = 0;
            departureFloorRecords[i] = 0;
        }

        lastPassngerArrivalTime = 0;
        totalStoppedTime = 0;

        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getEvent().equals("Passenger Arrival")) {
                if (eventList.get(i + 1).getEvent().equals("Button Pressed")) {
                    noOfButtonPressed++;
                    noOfFirstPassngerArrival[noOfButtonPressed] = Integer.parseInt(eventList.get(i).getDetails());
                    beginTime = Integer.parseInt(eventList.get(i).getTime());
                    if (i != 0) {
                        totalNextTripInterval[noOfButtonPressed] += Integer.parseInt(eventList.get(i).getTime()) - Integer.parseInt(eventList.get(i - 1).getTime());
                    }
                } else {
                    countIntervalPassngerArrival++;
                    noOfIntervalPassngerArrival[noOfButtonPressed] += (Integer.parseInt(eventList.get(i).getDetails()));
                }
                noOfPassngerArrival[noOfButtonPressed] += Integer.parseInt(eventList.get(i).getDetails());
                allArrivalTime[countTotalPassngerArrival] = Integer.parseInt(eventList.get(i).getTime()) - lastPassngerArrivalTime;
                allArrivalNumber[countTotalPassngerArrival] = Integer.parseInt(eventList.get(i).getDetails());
                countTotalPassngerArrival++;
                lastPassngerArrivalTime = Integer.parseInt(eventList.get(i).getTime());
            } else if (eventList.get(i).getEvent().equals("Button Pressed")) {
                String input = eventList.get(i).getDetails();
                ArrayList<LiftState> allLiftsStates = new ArrayList<>();

                int liftNo = 3;
                int lastDirIndex = -1;
                countU = 0;
                noOfAllUpDelayed[noOfButtonPressed] = 0;

                for (int a = 0; a < input.length() && liftNo > 0; a++) {
                    liftsStateRecords[noOfButtonPressed][liftNo - 1] = input.charAt(a);
                    if (input.charAt(a) == 'G') {
                        allLiftsStates.add(new LiftState(liftNo, 0, input.charAt(a)));
                        liftsFloorRecords[noOfButtonPressed][liftNo - 1] = 0;
                        liftNo--;
                        lastDirIndex = a;
                    } else if (input.charAt(a) == 'U' || input.charAt(a) == 'D' || input.charAt(a) == 'I') {
                        if (input.charAt(a) == 'U') {
                            countU++;
                        }

                        allLiftsStates.add(new LiftState(liftNo, Integer.parseInt(input.substring(lastDirIndex + 1, a)), input.charAt(a)));
                        liftsFloorRecords[noOfButtonPressed][liftNo - 1] = Integer.parseInt(input.substring(lastDirIndex + 1, a));
                        liftNo--;
                        lastDirIndex = a;
                    }
                }

                if (countU == 3) {
                    for (int j = i; j < 1000; j++) {
                        if (eventList.get(j).getEvent().equals("Lift Departure")) {
                            break;
                        }
                        if (eventList.get(j).getEvent().equals("Passenger Arrival")) {
                            totalInterArrivalAllUp[noOfButtonPressed]++;
                        }
                    }
                }

                liftStateList.add(allLiftsStates);
            } else if (eventList.get(i).getEvent().equals("Lift Departure")) {

                departureTime = Integer.parseInt(eventList.get(i).getTime());

                if (countU == 3) {
                    noOfAllUpDelayed[noOfButtonPressed] = Integer.parseInt(eventList.get(i).getTime()) - beginTime;
                    AllUpDecision[noOfButtonPressed] = Integer.parseInt(String.valueOf(eventList.get(i).getDetails().charAt(1)));
                }
                departureLiftRecords[noOfButtonPressed] = Integer.parseInt(String.valueOf(eventList.get(i).getDetails().charAt(1)));
                if (eventList.get(i).getDetails2().equals("G")) {
                    departureFloorRecords[noOfButtonPressed] = 0;
                } else {
                    departureFloorRecords[noOfButtonPressed] = Integer.parseInt(eventList.get(i).getDetails2());
                }

            } else if (eventList.get(i).getEvent().equals("Lift Stopped")) {
                countStopped++;
                countTotalStoppedEvent++;
                if (!eventList.get(i + 1).getEvent().equals("Passenger Arrival")) {
                    allStoppedTime[countTotalStoppedEvent] = Integer.parseInt(eventList.get(i + 1).getTime()) - Integer.parseInt(eventList.get(i).getTime());
                } else {
                    for (int a = i + 2; a < eventList.size(); a++) {
                        if (!eventList.get(a).getEvent().equals("Passenger Arrival")) {
                            allStoppedTime[countTotalStoppedEvent] = Integer.parseInt(eventList.get(a).getTime()) - Integer.parseInt(eventList.get(i).getTime());
                            break;
                        }
                    }
                }
            } else if (eventList.get(i).getEvent().equals("Enter Lift")) {

                //if departure from G floor then no need count speed.
                if (countStopped == 0) {
                    if (departureFloorRecords[noOfButtonPressed] != 0) {
                        speedDirect[noOfButtonPressed] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        if (departureFloorRecords[noOfButtonPressed] < 6) {
                            speedDirectCategory[noOfButtonPressed][0] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        } else if (departureFloorRecords[noOfButtonPressed] < 12) {
                            speedDirectCategory[noOfButtonPressed][1] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        } else if (departureFloorRecords[noOfButtonPressed] < 18) {
                            speedDirectCategory[noOfButtonPressed][2] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        } else if (departureFloorRecords[noOfButtonPressed] < 24) {
                            speedDirectCategory[noOfButtonPressed][3] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        }
                    }
                } else {
                    if (departureFloorRecords[noOfButtonPressed] != 0) {
                        supposeTotalTimeTravelled = 0;
                        speedStopped[noOfButtonPressed] = ((double) Integer.parseInt(eventList.get(i).getTime()) - departureTime) / departureFloorRecords[noOfButtonPressed];
                        if (departureFloorRecords[noOfButtonPressed] < 6) {
                            supposeTotalTimeTravelled = departureFloorRecords[noOfButtonPressed] * 7;
                        } else if (departureFloorRecords[noOfButtonPressed] < 12) {
                            supposeTotalTimeTravelled = departureFloorRecords[noOfButtonPressed] * 4;
                        } else if (departureFloorRecords[noOfButtonPressed] < 18) {
                            supposeTotalTimeTravelled = departureFloorRecords[noOfButtonPressed] * 4;
                        } else if (departureFloorRecords[noOfButtonPressed] < 24) {
                            supposeTotalTimeTravelled = departureFloorRecords[noOfButtonPressed] * 2;
                        }
                        TotalTimeTravelled = Integer.parseInt(eventList.get(i).getTime()) - startTime;
                        averageStoppedTimePerEvent = (int) Math.round((double) (TotalTimeTravelled - supposeTotalTimeTravelled) / countStopped);
                        totalStoppedTime += averageStoppedTimePerEvent;
                    }
                }

                noOfLiftStopped[noOfButtonPressed] = countStopped;
                countStopped = 0;
                noOfCountIntervalPassngerArrival[noOfButtonPressed] = countIntervalPassngerArrival;
                countIntervalPassngerArrival = 0;
                waitingTime[noOfButtonPressed] = Integer.parseInt(eventList.get(i).getTime()) - beginTime;
                noOfPassengerDelayed[noOfButtonPressed] = Integer.parseInt(eventList.get(i).getDetails2());

            }
        }

        /*        
        System.out.println("No|noOfFirstPassngerArrival|noOfIntervalPassngerArrival|noOfPassngerArrival|noOfAllUpDelayed|AllUpDecision|"
                + "noOfLiftStopped|noOfCountIntervalPassngerArrival|waitingTime|noOfPassengerDelayed|speedDirect|speedStopped|"
                + "liftsFloorRecords|liftsStateRecords|departureLiftRecords|departureFloorRecords\n");
        
        for (int i = 1; i <= noOfButtonPressed; i++) {
            if (departureFloorRecords[i] == departureFloorRecords[i - 1]) {
                System.out.println("**************************************************");
            }
            System.out.printf("%d|%d|%d|%d|%d|%d|%d|%d|%d|%d|%.2f|%.2f|%d|%d|%d|%c|%c|%c|%d|%d\n", i, noOfFirstPassngerArrival[i], noOfIntervalPassngerArrival[i], noOfPassngerArrival[i], noOfAllUpDelayed[i],
                    AllUpDecision[i], noOfLiftStopped[i], noOfCountIntervalPassngerArrival[i], waitingTime[i], noOfPassengerDelayed[i], speedDirect[i], speedStopped[i], liftsFloorRecords[i][0], liftsFloorRecords[i][1], liftsFloorRecords[i][2],
                    liftsStateRecords[i][0], liftsStateRecords[i][1], liftsStateRecords[i][2], departureLiftRecords[i], departureFloorRecords[i]);

            if (departureFloorRecords[i] == departureFloorRecords[i - 1]) {
                System.out.println("**************************************************");
            }
        }
         */
        //for create empirical distribution from input data
        createInterarrivalTimeEmpiricalDistribution();
        createNoOfPassngerEmpiricalDistribution();
        createAverageTimeLiftStayedAtEachFloor();
        createIntervalPassengerArrivalEmpiricalDistribution();
        createLiftFloorEmpiricalDistribution();
        createLiftStateEmpiricalDistribution();
        createAllLiftUpEmpiricalDistribution();
        createNoOfLiftStoppedEmpiricalDistribution();
        createRandomLiftStoppedTimeEmpiricalDistribution();
        createRandomNextTripIntervalEmpiricalDistribution();
        createRamdomInterArrivalAllUpEmpiricalDistribution();
    }

    public static void createInterarrivalTimeEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < countTotalPassngerArrival; i++) {

            if (allArrivalTime[i] < 25) {
                intervals[0]++;
            } else if (allArrivalTime[i] < 50) {
                intervals[1]++;
            } else if (allArrivalTime[i] < 75) {
                intervals[2]++;
            } else if (allArrivalTime[i] < 100) {
                intervals[3]++;
            } else if (allArrivalTime[i] < 125) {
                intervals[4]++;
            } else if (allArrivalTime[i] < 150) {
                intervals[5]++;
            } else if (allArrivalTime[i] < 175) {
                intervals[6]++;
            } else if (allArrivalTime[i] < 565) {
                intervals[7]++;
            }
        }

        cdf = 0.0;
        interArrivalPdfArr = new double[intervals.length];
        interArrivalCdfArr = new double[intervals.length];
        x1 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x1[i] = (i + 1) * 20;
        }
        //System.out.println("Interval | Frequency | Relative Frequency (PDF) | Cumulative Frequency (CDF)"); 
        for (int i = 0; i < intervals.length; i++) {
            interArrivalPdfArr[i] = (double) intervals[i] / countTotalPassngerArrival;
            cdf += interArrivalPdfArr[i];
            interArrivalCdfArr[i] = cdf;
            
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], interArrivalPdfArr[i], interArrivalCdfArr[i]);
        }

    }

    public static void createNoOfPassngerEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0};
        for (int i = 0; i < countTotalPassngerArrival; i++) {
            if (allArrivalNumber[i] < 3) {
                intervals[0]++;
            } else if (allArrivalNumber[i] < 6) {
                intervals[1]++;
            } else if (allArrivalNumber[i] < 9) {
                intervals[2]++;
            } else if (allArrivalNumber[i] <= 15) {
                intervals[3]++;
            }

        }

        cdf = 0.0;
        noOfPassngerPdfArr = new double[intervals.length];
        noOfPassngerCdfArr = new double[intervals.length];
        x2 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x2[i] = (i + 1) * 3;
        }

        for (int i = 0; i < intervals.length; i++) {
            noOfPassngerPdfArr[i] = (double) intervals[i] / countTotalPassngerArrival;
            cdf += noOfPassngerPdfArr[i];
            noOfPassngerCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], noOfPassngerPdfArr[i], noOfPassngerCdfArr[i]);
        }
    }

    public static void createAverageTimeLiftStayedAtEachFloor() {
        int[] timePerFloorDirect = {0, 0, 0, 0};//floor from 1 to 5,6-11,12-17,18-23
        double[] totalDirectSpeed = {0.0, 0.0, 0.0, 0.0};
        averageTimePerFloor = new int[]{0, 0, 0, 0};

        for (int i = 0; i < speedDirectCategory.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (speedDirectCategory[i][j] != 0.0) {
                    timePerFloorDirect[j]++;
                    totalDirectSpeed[j] += speedDirectCategory[i][j];
                }
            }
        }

        for (int j = 0; j < 4; j++) {
            averageTimePerFloor[j] = (int) Math.round((double) totalDirectSpeed[j] / timePerFloorDirect[j]);
            //System.out.println(totalDirectSpeed[j] + ", " + timePerFloodDirect[j] + ", " + averageTimePerFloor[j]);
        }
    }

    public static void createIntervalPassengerArrivalEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0, 0};
        for (int i = 0; i <= noOfButtonPressed; i++) {
            if (noOfCountIntervalPassngerArrival[i] < 2) {
                intervals[0]++;
            } else if (noOfCountIntervalPassngerArrival[i] < 4) {
                intervals[1]++;
            } else if (noOfCountIntervalPassngerArrival[i] < 6) {
                intervals[2]++;
            } else if (noOfCountIntervalPassngerArrival[i] < 9) {
                intervals[3]++;
            } else if (noOfCountIntervalPassngerArrival[i] <= 15) {
                intervals[4]++;
            }
        }

        cdf = 0.0;
        intervalPassengerArrivalPdfArr = new double[intervals.length];
        intervalPassengerArrivalCdfArr = new double[intervals.length];
        x3 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x3[i] = (i + 1) * 2;
        }
        for (int i = 0; i < intervals.length; i++) {
            intervalPassengerArrivalPdfArr[i] = (double) intervals[i] / noOfButtonPressed;
            cdf += intervalPassengerArrivalPdfArr[i];
            intervalPassengerArrivalCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], intervalPassengerArrivalPdfArr[i], intervalPassengerArrivalCdfArr[i]);
        }
    }

    public static void createLiftFloorEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0};
        for (int i = 1; i <= noOfButtonPressed; i++) {
            for (int j = 0; j < 3; j++) {
                if (liftsFloorRecords[i][j] < 6) {
                    intervals[0]++;
                } else if (liftsFloorRecords[i][j] < 12) {
                    intervals[1]++;
                } else if (liftsFloorRecords[i][j] < 18) {
                    intervals[2]++;
                } else if (liftsFloorRecords[i][j] < 24) {
                    intervals[3]++;
                }
            }
        }

        cdf = 0.0;
        liftFloorPdfArr = new double[intervals.length];
        liftFloorCdfArr = new double[intervals.length];
        x4 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x4[i] = (i + 1) * 7;
        }

        for (int i = 0; i < intervals.length; i++) {
            liftFloorPdfArr[i] = (double) intervals[i] / (noOfButtonPressed * 3);
            cdf += liftFloorPdfArr[i];
            liftFloorCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], liftFloorPdfArr[i], liftFloorCdfArr[i]);
        }
    }

    public static void createLiftStateEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0};
        for (int i = 1; i <= noOfButtonPressed; i++) {
            for (int j = 0; j < 3; j++) {
                if (liftsStateRecords[i][j] == possibleLiftState[0]) {
                    intervals[0]++;
                } else if (liftsStateRecords[i][j] == possibleLiftState[1]) {
                    intervals[1]++;
                } else if (liftsStateRecords[i][j] == possibleLiftState[2]) {
                    intervals[2]++;
                } else if (liftsStateRecords[i][j] == possibleLiftState[3]) {
                    intervals[3]++;
                }
            }
        }

        cdf = 0.0;
        liftStatePdfArr = new double[intervals.length];
        liftStateCdfArr = new double[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            liftStatePdfArr[i] = (double) intervals[i] / (noOfButtonPressed * 3);
            cdf += liftStatePdfArr[i];
            liftStateCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], liftStatePdfArr[i], liftStateCdfArr[i]);
        }
    }

    public static void createAllLiftUpEmpiricalDistribution() {
        int[] intervalAllUp = {0, 0};
        int[] intervalAllUpDecision = {0, 0, 0};
        int[] intervalAllUpDelayed = {0, 0, 0, 0, 0};
        int countAllUpDelayed = 0;
        for (int i = 1; i <= noOfButtonPressed; i++) {
            if (noOfAllUpDelayed[i] != 0) {
                countAllUpDelayed++;
                intervalAllUp[0]++;
                switch (AllUpDecision[i]) {
                    case 1:
                        intervalAllUpDecision[0]++;
                        break;
                    case 2:
                        intervalAllUpDecision[1]++;
                        break;
                    case 3:
                        intervalAllUpDecision[2]++;
                        break;
                }

                if (noOfAllUpDelayed[i] < 31) {
                    intervalAllUpDelayed[0]++;
                } else if (noOfAllUpDelayed[i] < 62) {
                    intervalAllUpDelayed[1]++;
                } else if (noOfAllUpDelayed[i] < 93) {
                    intervalAllUpDelayed[2]++;
                } else if (noOfAllUpDelayed[i] < 124) {
                    intervalAllUpDelayed[3]++;
                } else if (noOfAllUpDelayed[i] < 155) {
                    intervalAllUpDelayed[4]++;
                }
            } else {
                intervalAllUp[1]++;
            }
        }

        cdf = 0.0;
        allLiftUpPdfArr = new double[intervalAllUp.length];
        allLiftUpCdfArr = new double[intervalAllUp.length];

        for (int i = 0; i < intervalAllUp.length; i++) {
            allLiftUpPdfArr[i] = (double) intervalAllUp[i] / (noOfButtonPressed);
            cdf += allLiftUpPdfArr[i];
            allLiftUpCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervalAllUp[i], allLiftUpPdfArr[i], allLiftUpCdfArr[i]);
        }

        cdf = 0.0;
        allLiftUpDecisionPdfArr = new double[intervalAllUpDecision.length];
        allLiftUpDecisionCdfArr = new double[intervalAllUpDecision.length];

        for (int i = 0; i < intervalAllUpDecision.length; i++) {
            allLiftUpDecisionPdfArr[i] = (double) intervalAllUpDecision[i] / (countAllUpDelayed);
            cdf += allLiftUpDecisionPdfArr[i];
            allLiftUpDecisionCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervalAllUpDecision[i], allLiftUpDecisionPdfArr[i], allLiftUpDecisionCdfArr[i]);
        }

        cdf = 0.0;
        allLiftUpDelayedPdfArr = new double[intervalAllUpDelayed.length];
        allLiftUpDelayedCdfArr = new double[intervalAllUpDelayed.length];
        x5 = new int[intervalAllUpDelayed.length];

        for (int i = 0; i < intervalAllUpDelayed.length; i++) {
            x5[i] = (i + 1) * 31;
        }
        for (int i = 0; i < intervalAllUpDelayed.length; i++) {
            allLiftUpDelayedPdfArr[i] = (double) intervalAllUpDelayed[i] / (countAllUpDelayed);
            cdf += allLiftUpDelayedPdfArr[i];
            allLiftUpDelayedCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervalAllUpDelayed[i], allLiftUpDelayedPdfArr[i], allLiftUpDelayedCdfArr[i]);
        }
    }

    public static void createNoOfLiftStoppedEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
        int countTotalNoOfLiftStopped = 0;
        for (int i = 1; i <= noOfButtonPressed; i++) {
            if (noOfLiftStopped[i] != 0) {
                countTotalNoOfLiftStopped++;
                switch (noOfLiftStopped[i]) {
                    case 1:
                        intervals[0]++;
                        break;
                    case 2:
                        intervals[1]++;
                        break;
                    case 3:
                        intervals[2]++;
                        break;
                    case 4:
                        intervals[3]++;
                        break;
                    case 5:
                        intervals[4]++;
                        break;
                    case 6:
                        intervals[5]++;
                        break;
                    case 7:
                        intervals[6]++;
                        break;
                    case 8:
                        intervals[7]++;
                        break;
                }
            }

        }

        cdf = 0.0;
        noOfLiftStoppedPdfArr = new double[intervals.length];
        noOfLiftStoppedCdfArr = new double[intervals.length];
        x6 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x6[i] = (i + 1);
        }

        for (int i = 0; i < intervals.length; i++) {
            noOfLiftStoppedPdfArr[i] = (double) intervals[i] / countTotalNoOfLiftStopped;
            cdf += noOfLiftStoppedPdfArr[i];
            noOfLiftStoppedCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], noOfLiftStoppedPdfArr[i], noOfLiftStoppedCdfArr[i]);
        }
    }

    public static void createRandomLiftStoppedTimeEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0, 0};
        for (int i = 1; i <= countTotalStoppedEvent; i++) {
            if (allStoppedTime[i] < 17) {
                intervals[0]++;
            } else if (allStoppedTime[i] < 34) {
                intervals[1]++;
            } else if (allStoppedTime[i] < 51) {
                intervals[2]++;
            } else if (allStoppedTime[i] < 68) {
                intervals[3]++;
            } else if (allStoppedTime[i] <= 85) {
                intervals[4]++;
            }
        }

        cdf = 0.0;
        liftStoppedTimePdfArr = new double[intervals.length];
        liftStoppedTimeCdfArr = new double[intervals.length];
        x7 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x7[i] = (i + 1) * 17;
        }

        for (int i = 0; i < intervals.length; i++) {
            liftStoppedTimePdfArr[i] = (double) intervals[i] / countTotalStoppedEvent;
            cdf += liftStoppedTimePdfArr[i];
            liftStoppedTimeCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], liftStoppedTimePdfArr[i], liftStoppedTimeCdfArr[i]);
        }
    }

    public static void createRandomNextTripIntervalEmpiricalDistribution() {
        intervals = new int[]{0, 0, 0, 0, 0};
        for (int i = 1; i <= noOfButtonPressed; i++) {
            if (totalNextTripInterval[i] < 20) {
                intervals[0]++;
            } else if (totalNextTripInterval[i] < 40) {
                intervals[1]++;
            } else if (totalNextTripInterval[i] < 60) {
                intervals[2]++;
            } else if (totalNextTripInterval[i] < 80) {
                intervals[3]++;
            } else if (totalNextTripInterval[i] <= 542) {
                intervals[4]++;
            }
        }

        cdf = 0.0;
        nextTripInvertalPdfArr = new double[intervals.length];
        nextTripInvertalCdfArr = new double[intervals.length];
        x8 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x8[i] = (i + 1) * 20;
        }

        for (int i = 0; i < intervals.length; i++) {
            nextTripInvertalPdfArr[i] = (double) intervals[i] / noOfButtonPressed;
            cdf += nextTripInvertalPdfArr[i];
            nextTripInvertalCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], nextTripInvertalPdfArr[i], nextTripInvertalCdfArr[i]);
        }
    }

    public static void createRamdomInterArrivalAllUpEmpiricalDistribution() {

        intervals = new int[]{0, 0, 0, 0};
        for (int i = 1; i <= noOfButtonPressed; i++) {
            if (totalInterArrivalAllUp[i] < 1) {
                intervals[0]++;
            } else if (totalInterArrivalAllUp[i] < 2) {
                intervals[1]++;
            } else if (totalInterArrivalAllUp[i] < 3) {
                intervals[2]++;
            } else if (totalInterArrivalAllUp[i] < 12) {
                intervals[3]++;
            }
        }

        cdf = 0.0;
        interArrivalAllUpPdfArr = new double[intervals.length];
        interArrivalAllUpCdfArr = new double[intervals.length];
        x9 = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            x9[i] = (i + 1);
        }

        for (int i = 0; i < intervals.length; i++) {
            interArrivalAllUpPdfArr[i] = (double) intervals[i] / noOfButtonPressed;
            cdf += interArrivalAllUpPdfArr[i];
            interArrivalAllUpCdfArr[i] = cdf;
            //System.out.printf("%d|%d|%.4f|%.4f\n", (i + 1), intervals[i], interArrivalAllUpPdfArr[i], interArrivalAllUpCdfArr[i]);
        }
    }

    public static boolean checkFullPeriod() {
        for (int i = 0; i < primeFactors(m).size(); i++) {
            //System.out.println(primeFactors(m).get(i) + "," + a + "," + (a - 1) % primeFactors(m).get(i));
            if ((a - 1) % primeFactors(m).get(i) != 0) {
                return false;
            }
        }
        //System.out.println(gcd(m, c)+","+m % 4+","+(a - 1) % 4); 

        if ((gcd(m, c) == 1)) {
            if (m % 4 == 0) {
                if ((a - 1) % 4 != 0) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static int gcd(int num1, int num2) {
        if (num2 == 0) {
            return num1;
        }
        return gcd(num2, num1 % num2);
    }

    public static List<Integer> primeFactors(int num) {
        List<Integer> primefactors = new ArrayList<>();
        int currentNum = num;

        for (int i = 2; i <= currentNum; i++) {
            if (currentNum % i == 0) {
                primefactors.add(i);
                currentNum /= i;
                i--;
            }
        }
        return primefactors;
    }

    public static boolean chiSquareTest() {
        //double intervalRange = ((double)m / numOfIntervals)/m;
        double intervalRange = ((double) numOfRandomNum / numOfIntervals) / numOfRandomNum;
        int ei = numOfRandomNum / numOfIntervals;
        int[] intervals = new int[numOfIntervals];
        double[] differences = new double[numOfIntervals];
        double difference, differenceSquare, sum = 0.0;

        for (int i = 0; i < randomNumberTest.length; i++) {
            for (int a = 1; a <= numOfIntervals; a++) {
                if (randomNumberTest[i] < (double) ((int) ((intervalRange * a) * 100)) / 100) {
                    intervals[a - 1]++;
                    //System.out.print(a + ":" + randomNumberTest[i] + ",");
                    break;
                }
            }

        }

        System.out.println("\nChi-Square Test\nInterval|Oi|Ei|Oi-Ei|(Oi-Ei)^2|(Oi-Ei)^2/Ei\n");

        for (int i = 0; i < intervals.length; i++) {
            difference = intervals[i] - ei;
            differenceSquare = Math.pow(difference, 2);
            differences[i] = differenceSquare / ei;
            System.out.printf("%d|%d|%d|%.2f|%.2f|%.2f\n", i + 1, intervals[i], ei, difference, differenceSquare, differences[i]);
            sum += differences[i];
        }
        System.out.printf("Sum|\t\t\t\t%.2f\n", sum);

        if (sum < 43.8) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean kolmogorovSmirnovTest() {
        Arrays.sort(randomNumberTest);
        /*
        System.out.print("\n");
        for (int i = 0; i < randomNumberTest.length - 1; i++) {
            System.out.print(randomNumberTest[i] + "^^");
        }
        */
        upperD = calculateUpperD();
        lowerD = calculateLowerD();

        if (upperD > lowerD) {
            D = upperD;
        } else {
            D = lowerD;
        }

        criticalValue = getCriticalValue();

        if (D <= criticalValue) {
            return true;
        } else {
            return false;
        }
    }

    public static double calculateUpperD() {
        double maximumValue = ((double) (1 / numOfRandomNum) - randomNumberTest[0]);
        double currentValue = 0.0;
        System.out.print("\n");
        for (int i = 0; i < numOfRandomNum; i++) {
            currentValue = Math.round((((double) (i + 1) / numOfRandomNum) - randomNumberTest[i]) * 100.00) / 100.00;

            //System.out.print(currentValue + "|");
            if (currentValue > maximumValue) {
                maximumValue = currentValue;
            }
        }

        return maximumValue;
    }

    public static double calculateLowerD() {
        double maximumValue = (randomNumberTest[0] - ((double) 0 / numOfRandomNum));
        double currentValue = 0.0;
        System.out.print("\n");
        for (int i = 0; i < numOfRandomNum; i++) {
            currentValue = Math.round((randomNumberTest[i] - ((double) (i) / numOfRandomNum)) * 100.00) / 100.00;

            //System.out.print(currentValue + "-");
            if (currentValue > maximumValue) {
                maximumValue = currentValue;
            }
        }

        return maximumValue;
    }

    public static double getCriticalValue() {
        if (numOfRandomNum == 5) {
            if (significanceLevel == 0.1) {
                return 0.510;
            } else if (significanceLevel == 0.05) {
                return 0.565;
            } else if (significanceLevel == 0.01) {
                return 0.669;
            }
        } else if (numOfRandomNum == 10) {
            if (significanceLevel == 0.1) {
                return 0.368;
            } else if (significanceLevel == 0.05) {
                return 0.410;
            } else if (significanceLevel == 0.01) {
                return 0.490;
            }
        } else if (numOfRandomNum == 15) {
            if (significanceLevel == 0.1) {
                return 0.304;
            } else if (significanceLevel == 0.05) {
                return 0.338;
            } else if (significanceLevel == 0.01) {
                return 0.404;
            }
        } else if (numOfRandomNum == 20) {
            if (significanceLevel == 0.1) {
                return 0.264;
            } else if (significanceLevel == 0.05) {
                return 0.294;
            } else if (significanceLevel == 0.01) {
                return 0.356;
            }
        } else if (numOfRandomNum == 25) {
            if (significanceLevel == 0.1) {
                return 0.24;
            } else if (significanceLevel == 0.05) {
                return 0.27;
            } else if (significanceLevel == 0.01) {
                return 0.32;
            }
        } else if (numOfRandomNum == 30) {
            if (significanceLevel == 0.1) {
                return 0.22;
            } else if (significanceLevel == 0.05) {
                return 0.24;
            } else if (significanceLevel == 0.01) {
                return 0.29;
            }
        } else if (numOfRandomNum == 35) {
            if (significanceLevel == 0.1) {
                return 0.21;
            } else if (significanceLevel == 0.05) {
                return 0.23;
            } else if (significanceLevel == 0.01) {
                return 0.27;
            }
        } else if (numOfRandomNum > 35) {
            if (significanceLevel == 0.1) {
                return 1.22 / (double) (Math.sqrt(numOfRandomNum));
            } else if (significanceLevel == 0.05) {
                return 1.36 / (double) (Math.sqrt(numOfRandomNum));
            } else if (significanceLevel == 0.01) {
                return 1.63 / (double) (Math.sqrt(numOfRandomNum));
            }
        }

        return 0;
    }

    public static void uniformityTestResult() {
        //System.out.print("\n(");
        for (int i = 0; i < randomNumberTest.length - 1; i++) {
            //System.out.print(randomNumberTest[i] + ", ");
        }
        //System.out.print(randomNumberTest[randomNumberTest.length - 1] + ")\n");
        System.out.print("\nIs Full Period? " + checkFullPeriod() + "\n");
        System.out.print("\nIs Uniform? " + chiSquareTest() + "\n");

        System.out.println("\nKolmogorov-Smirnov Test");
        System.out.printf("\nD+ = %.2f", upperD);
        System.out.printf("\nD- = %.2f\n", lowerD);
        System.out.printf("\nD  = %.2f\n", D);
        System.out.printf("\nCritical Value  = %.3f\n", criticalValue);

        if (h0Accepted) {
            System.out.printf("\nSince D<= crtical value, H0 accepted\n");
        } else {
            System.out.printf("\nSince D> crtical value, H0 rejected\n");
        }
    }
    
    public static void calculatePointEstimate(int[] arr) {
        sum = 0;
        difference = 0;
        for(int i=0;i<arr.length;i++){
            sum += arr[i];
            System.out.print(arr[i]+",");
        }
        System.out.print("\n");
        sampleMean = (double)Math.round((sum/numOfInterval)*1000)/1000;
        
        for(int i=0;i<arr.length;i++){
            difference += Math.pow((arr[i]-sampleMean),2);
        }
        
        sampleVariance = difference/(numOfInterval-1);
        
        System.out.println("Mean: "+sampleMean+", Variance: "+sampleVariance);
        
        confidenceInterval = (double)Math.round((getCriticalPoint()*Math.sqrt((sampleVariance/numOfInterval)))*1000)/1000;
 
        System.out.println("Point Estimate: "+sampleMean+"+-"+confidenceInterval);
        
    }
    
    
    public static void calculatePointEstimate(double[] arr) {
        sum = 0;
        difference = 0;
        for(int i=0;i<arr.length;i++){
            sum += arr[i];
            System.out.print(arr[i]+",");
        }
        System.out.print("\n");
        sampleMean = (double)Math.round((sum/numOfInterval)*1000)/1000;
        
        for(int i=0;i<arr.length;i++){
            difference += Math.pow((arr[i]-sampleMean),2);
        }
        
        sampleVariance = difference/(numOfInterval-1);
        
        System.out.println("Mean: "+sampleMean+", Variance: "+sampleVariance);
        
        
        confidenceInterval = (double)Math.round((getCriticalPoint()*Math.sqrt((sampleVariance/numOfInterval)))*1000)/1000;
 
        System.out.println("Point Estimate: "+sampleMean+"+-"+confidenceInterval);
        
    }

    public static double getCriticalPoint() {

        int v =  numOfInterval - 1;
        switch (v) {
            case 1:
                if (confidenceLevel == 0.9) {
                    return 3.078;
                } else if (confidenceLevel == 0.95) {
                    return 6.314;
                } else if (confidenceLevel == 0.975) {
                    return 12.706;
                }
            case 2:
                if (confidenceLevel == 0.9) {
                    return 1.886;
                } else if (confidenceLevel == 0.95) {
                    return 2.920;
                } else if (confidenceLevel == 0.975) {
                    return 4.303;
                }
            case 3:
                if (confidenceLevel == 0.9) {
                    return 1.638;
                } else if (confidenceLevel == 0.95) {
                    return 2.353;
                } else if (confidenceLevel == 0.975) {
                    return 3.182;
                }
            case 4:
                if (confidenceLevel == 0.9) {
                    return 1.533;
                } else if (confidenceLevel == 0.95) {
                    return 2.132;
                } else if (confidenceLevel == 0.975) {
                    return 2.776;
                }
            case 5:
                if (confidenceLevel == 0.9) {
                    return 1.476;
                } else if (confidenceLevel == 0.95) {
                    return 2.015;
                } else if (confidenceLevel == 0.975) {
                    return 2.571;
                }
            case 6:
                if (confidenceLevel == 0.9) {
                    return 1.440;
                } else if (confidenceLevel == 0.95) {
                    return 1.943;
                } else if (confidenceLevel == 0.975) {
                    return 2.447;
                }
            case 7:
                if (confidenceLevel == 0.9) {
                    return 1.415;
                } else if (confidenceLevel == 0.95) {
                    return 1.895;
                } else if (confidenceLevel == 0.975) {
                    return 2.365;
                }
            case 8:
                if (confidenceLevel == 0.9) {
                    return 1.397;
                } else if (confidenceLevel == 0.95) {
                    return 1.860;
                } else if (confidenceLevel == 0.975) {
                    return 2.306;
                }
            case 9:
                if (confidenceLevel == 0.9) {
                    return 1.383;
                } else if (confidenceLevel == 0.95) {
                    return 1.833;
                } else if (confidenceLevel == 0.975) {
                    return 2.262;
                }
            case 10:
                if (confidenceLevel == 0.9) {
                    return 1.372;
                } else if (confidenceLevel == 0.95) {
                    return 1.812;
                } else if (confidenceLevel == 0.975) {
                    return 2.228;
                }
        }
        return 0;
    }
    
    public static void testForAutocorrelation(){
        System.out.println("\nTest for Autocorrelation\n");
        double previousValue = randomNumberTest[i-1];
        for(int a=i+l;a<=N;a=a+l){      
            System.out.print(randomNumberTest[a-1]+",");
            sumR+=(randomNumberTest[a-1]*previousValue);
            previousValue = randomNumberTest[a-1];
        }
        sumR = (double)Math.round((int)(sumR*10000))/10000;
        System.out.println(sumR);
        
        rho = sumR/(M+1) - 0.25;
        rho = (double)Math.round((int)(rho*10000))/10000;
        sigma = Math.sqrt((13*M)+7)/(12*(M+1));
        sigma = (double)Math.round((int)(sigma*10000))/10000;
        Z = rho/sigma;
        Z = (double)Math.round((int)(Z*10000))/10000;
        
        System.out.println("rho= "+rho+",sigma= "+sigma+",Z0= "+Z);
        
        if(Z>=lowerSignificanceValue&&Z<=upperSignificanceValue){
            System.out.println("Since "+lowerSignificanceValue+" <= Z0="+Z+" <= "+upperSignificanceValue+", therefore the hypothesis of independence cannot be rejected.");
        }
        else if(Z<lowerSignificanceValue){
            System.out.println("Since "+lowerSignificanceValue+" > Z0="+Z+", therefore the hypothesis of independence is rejected.");

        }
        else if(Z>upperSignificanceValue){
            System.out.println("Since Z0="+Z+" > "+upperSignificanceValue+", therefore the hypothesis of independence is rejected.");
        }
           
    }
}
