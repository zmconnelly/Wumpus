
import java.util.ArrayList;

public class LogicExplorer extends Agent {

    private final World world;
    private final KnowledgeBase kb;
    private int arrowCount;
    private int t = 0;              //time variable
    private int previousAction;
    private Position curPos;
    private ArrayList<Position> frontier = new ArrayList<>();
    private boolean[][] searchedPositions;
    private boolean currentlyNavigatingToSafeSquare;
    private final int worldSize;
    private Position goalPosition;
    private boolean gameOver = false;

    private final byte BREEZE = 0b00000001;
    private final byte STENCH = 0b0000010;
    private final byte BUMP = 0b00000100;
    private final byte GLITTER = 0b00001000;
    private final byte DEATH = 0b00010000;
    private final byte SCREAM = 0b01000000;

    public LogicExplorer(World world) {
        this.world = world;
        kb = new KnowledgeBase();
        kb.initializeRules();
        this.arrowCount = world.arrowCount;
        this.searchedPositions = new boolean[World.size][World.size];
        this.worldSize = World.size;
        run();
    }

    private void run() {

        while (!gameOver) {
            decideNextAction((byte) world.getPercepts());
        }
    }

    private void move(int action) {

        switch (action) {
            case 1:
                world.action(action);
                gameOver = true;
            case 2:
                kb.tell(encodePercepts(world.action(action)));
                break;
            case 5:
                arrowCount--;
                kb.tell(encodePercepts(world.action(action)));
                break;
            default:
                world.action(action);
                break;
        }
    }

    private Clause encodePercepts(int percepts) {

        Clause clause = new Clause();

        //conversion logic goes here
        return clause;
    }

    private void decideNextAction(byte percepts) {

        if (frontier.isEmpty()) {
            move(World.QUIT);
        }
        processPosition(percepts);
        updateKB(percepts);
        if ((percepts & GLITTER) != 0) {//maybe just kb.ask("Holding(Gold,Result(Grab,CurrentPosition))"): is better, no percept based logic within agent.
            //return World.GRAB;    //grab gold and end game
            move(1);
        } else if (currentlyNavigatingToSafeSquare) {       // im pretty sure this is all handled by RHW method, so thers no need to call decide next actions while its traversing
            //return continueNavigatingToSafeSquare();
        } else if (kb.ask("!Wumpus(forwardspot)AND!Pit(forwardSpot)&!Obstical(forwardSpot)")) {
            move(World.MOVE);
        } else if ("safeSpotInFrontier?" == "") {
            RHWTraversal("!Wumpus(adjacent)AND!Pit(adjacent)");
            //return continueNavigatingToSafeSquare();
        } else if ("KnownWumpusSpotInFrontier" == "") {
            //kill wumpus
            RHWTraversal("Wumpus(adjacent)");
        } else {
            //go to random spot in frontier that is not definite death
        }
    }

    //i dont think we need this, RHW takes to the space and then turns to face already...its all done in a look so new percepts arent being processed
    //since the agent has already been to all of the spaces in will be traveling through
    private int continueNavigatingToSafeSquare() {
        //this should basically be RHW Traversal until adjacent to to goal state, then turn to face it
        return -1;
    }

    private void processPosition(byte percepts) {
        if ((percepts & BUMP) == 0) {//did not bump
            if (previousAction == World.MOVE) {
                curPos.moveDidMove();
            }
        }
    }

    private void updateKB(byte percepts) {
        if ((percepts & STENCH) != 0) {
            Clause clause = new Clause();
            kb.tell(new Clause(new Fact("Stench", curPos.x, false, curPos.y,false,  true, null, null)));//Stench(x,y,t)
        } else {
            kb.tell(new Clause(new Fact("Stench", curPos.x, false, curPos.y,false, false, null, null)));//!Stench(x,y,t)
        }
        if ((percepts & BREEZE) != 0) {
            kb.tell(new Clause(new Fact("Breeze", curPos.x, false, curPos.y,false, true, null, null)));
        } else {
            kb.tell(new Clause(new Fact("Breeze", curPos.x, false, curPos.y,false, false, null, null)));
        }
        if ((percepts & SCREAM) != 0) {
            //need to deal with this
        }
    }

    private void RHWTraversal(String stopCondition) {

        do {
            if (kb.ask("right is safe")) {
                move(4);        //turn right
            } else {
                if (kb.ask("forward is safe")) {
                    move(2);   //go forward
                } else {
                    move(3);    //turn left
                }
            }
        } while (!kb.ask(stopCondition));

        //face stop condition
        while (!kb.ask("am i facing the stop condition?")) {
            move(3);   //turn until facing stp condition
        }
        move(2);
    }
}
