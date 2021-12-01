package solver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.Timer;

import main.*;
import util.Cell;

public class AStarSolve {

    private final PriorityQueue<Cell> queue;
    private Cell current;
    private final List<Cell> grid;
    private Cell Ng;

    public AStarSolve(List<Cell> grid, MazeGridPanel panel) {
        this.grid = grid;
        this.Ng = grid.get(grid.size() - 1);
        queue = new PriorityQueue<Cell>(new CellDistanceFromGoalComparator());
        current = grid.get(0);

        current.setgCost(0);
        current.sethCost(calculateHCost(current));

        queue.offer(current);
        final Timer timer = new Timer(Maze.speed, null);
        timer.addActionListener(e -> {
            if (current == null) return;
            if (!current.equals(Ng)) {
                flood();
            } else {
                drawPath();
                Maze.solved = true;
                System.out.println("AStar SOLVED");
                timer.stop();
            }
            panel.setCurrent(current);
            panel.repaint();
            timer.setDelay(Maze.speed);
        });
        timer.start();
    }

    private void flood() {
        current.setDeadEnd(true);
        current = queue.poll();
        if (current == null) return;
        List<Cell> adjacentCells = current.getValidMoveNeighbours(grid);
        for (Cell c : adjacentCells) {
            if(!c.isDeadEnd()){
                c.setgCost(current.getgCost() + 1);
                c.sethCost(calculateHCost(c));
                c.setParent(current);
                c.setDeadEnd(true);
                queue.offer(c);
            } else {
                if (current.getgCost() + 1 < c.getgCost()) {
//                    queue.remove(c);
                    c.setParent(current);
                    c.setgCost(current.getgCost() + 1);
                    c.sethCost(calculateHCost(c));
//                    queue.offer(c);
                }
            }
        }
//            if (c.getDistance() == -1) {
//                c.setDistance(current.getDistance() + 1);
//                c.setParent(current);
//                queue.offer(c);
//            }
    }


    private double calculateHCost(Cell cell) {
        return (Math.abs(cell.getX() - Ng.getX()) + Math.abs(cell.getY() - Ng.getY()));
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

    private class CellDistanceFromGoalComparator implements Comparator<Cell> {
        Cell goal = grid.get(grid.size() - 1);

        @Override
        public int compare(Cell arg0, Cell arg1) {
            if (arg0.getfCost() > arg1.getfCost()) {
                return 1;
            } else {
                return arg0.getfCost() < arg1.getfCost() ? -1 : 0;
            }
        }
    }
}