package solver;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.Timer;

import main.*;
import util.*;

public class LStarSolve {

//    private final DoublyLinkedList[] openList;
    private final HashMap<Integer, DoublyLinkedList> openList;

    private Cell current;
    private final List<Cell> grid;
    private int currentBucketReadIdx = 0;
    private double w, dgmin;
    private Cell Ns, Ng;
    private long openListItemCount;
    private double fCostStart;
    private double df;
    private int Ks;

    public LStarSolve(List<Cell> grid, MazeGridPanel panel, Cell Ns, Cell Ng, double w, double dgmin, double dgmax) {
        this.grid = grid;
        this.w = w;
        this.dgmin = dgmin;
        this.Ns = Ns;
        this.Ng = Ng;
        this.openListItemCount = 1;
        this.fCostStart = calculateHCost(Ns);
        this.df = (1-w) * dgmin;
        this.Ks = (int) Math.round(2 * dgmax) + 2;

//        this.openList = new DoublyLinkedList[1000];
        this.openList = new HashMap<>();
        current = Ns;
        current.setgCost(0);
        current.sethCost(calculateHCost(current));

        openList.put(currentBucketReadIdx, new DoublyLinkedList());
        openList.get(currentBucketReadIdx).insertFirst(current);
//        openList[currentBucketReadIdx] = new DoublyLinkedList();
//        openList[currentBucketReadIdx].insertFirst(current);

        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(e -> {
//            currentBucketReadIdx++;
            if(openListItemCount > 0){
//            while (openListItemCount > 0) {

//                if (openList[currentBucketReadIdx] == null) {
                if (openList.get(currentBucketReadIdx) == null){
//                    openList[currentBucketReadIdx] = new DoublyLinkedList();
//                    continue;
                    currentBucketReadIdx++;
                    return;
                }
                DoublyLinkedList currBucket = openList.get(currentBucketReadIdx);
//                DoublyLinkedList currBucket = openList[currentBucketReadIdx];
//                System.out.println(Arrays.toString(openList));
//                System.out.println("Bucket Id = " + currentBucketReadIdx);
//                System.out.println("Total item = " + openListItemCount);

                while (currBucket != null && currBucket.size > 0) {
                    current = currBucket.popFirst();
                    openListItemCount--;
//                    System.out.println("Bucket size = " + currBucket.size);
//                    System.out.print("A: ");
//                    System.out.println(current);

                    if (current.equals(Ng)) {
                        System.out.println("LStar SOLVED");
                        drawPath();
                        Maze.solved = true;
                        timer.stop();
                        openListItemCount = 0;
                        break;
                    }

                    List<Cell> adjacentCells = current.getValidMoveNeighbours(grid);
                    for (Cell c : adjacentCells) {
                        if (!c.isDeadEnd()) {
//                            System.out.println("Not Visited");
                            c.setgCost(current.getgCost() + 1);
                            c.sethCost(calculateHCost(c));
                            c.setParent(current);

                            int SL = (int) calculateSL(current);
                            openListItemCount++;
                            System.out.println("SL = " + SL);

                            if(openList.get(SL) == null) {
                                openList.put(SL, new DoublyLinkedList());
                            }
                            openList.get(SL).insertFirst(c);
//                            if(openList[SL] == null) {
//                                openList[SL] = new DoublyLinkedList();
//                            }
//                            openList[SL].insertFirst(c);

                            c.bucketIndex = SL;
                            c.setDeadEnd(true);

                        } else {
//                            System.out.println("Visited");
                            if (current.getgCost() + 1 < c.getgCost()) {
//                                System.out.println("Smaller value");
                                c.setParent(current);
                                c.setgCost(current.getgCost() + 1);
                                c.sethCost(calculateHCost(c));

                                DoublyLinkedList prevList = openList.get(c.bucketIndex);
//                                DoublyLinkedList prevList = openList[c.bucketIndex];

                                prevList.deleteNode(c);
                                int SL = (int) calculateSL(current);
                                System.out.println("SL = " + SL);

                                if(openList.get(SL) == null) {
                                    openList.put(SL, new DoublyLinkedList());
                                }
                                openList.get(SL).insertFirst(c);
//                                if(openList[SL] == null) {
//                                    openList[SL] = new DoublyLinkedList();
//                                }
//                                openList[SL].insertFirst(c);

                                c.bucketIndex = SL;

                            }
                        }

                    }
                }
//                panel.setCurrent(current);
//                panel.repaint();
//                timer.setDelay(Maze.speed);
                currentBucketReadIdx++;
            }

            panel.setCurrent(current);
            panel.repaint();
            timer.setDelay(Maze.speed);
        });
        timer.start();
    }

    private long calculateSL(Cell neighbour) {
//        System.out.println((neighbour.getfCost()));
        return Math.round((neighbour.getfCost() - fCostStart) / df) + 1;
    }

    private double calculateHCost(Cell cell) {
        return this.w * (Math.abs(cell.getX() - Ng.getX()) + Math.abs(cell.getY() - Ng.getY()));
    }
    private void drawPath() {
        long pathLength = 0;
        while (current != grid.get(0)) {
            current.setPath(true);
            current = current.getParent();
            pathLength++;
        }
        System.out.println("Path Length = " + pathLength);
    }
}