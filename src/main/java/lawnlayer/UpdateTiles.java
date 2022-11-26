package lawnlayer;

import java.lang.Math;
import java.util.*;

import lawnlayer.character.enemy.Enemy;
import lawnlayer.character.player.Player;

/**
 * Methods for updating tiles
 */
public interface UpdateTiles {

    /**
     * Update the tiles
     * @param app The window to draw onto.
     */
    public default void updateTiles(App app) {
        int r = app.player.getRow();
        int c = app.player.getCol();
    
        // 1. player is on the dirt (0, empty tile)
        if (app.tile[r][c] == 0) {

            // Set the tile to path in progress
            if (app.player.getDirection() == 37) {
                c++;   
            }  else if (app.player.getDirection() == 38) {
                r++;    
            }
            
            if (app.tile[r][c] == 0) {
                app.tile[r][c] = 2;
                // add unique tile to path
                if (app.player.getPath().isEmpty() || app.player.getLastTileInPath()[0] != r || app.player.getLastTileInPath()[1] != c) {
                    int[] v = {r, c};
                    app.player.addVertexToPath(v);
                }
            }
            
            // player hit by red tile, respawn and remove paths
            if (app.player.getDirection() == 39) {
                c--;   
            }  else if (app.player.getDirection() == 40) {
                r--;    
            }
            if (app.tile[r][c] == 3) {
                // respawn player
                app.player.respawn(app.tile); 
            }

        // 2. player is on the concrete or grass (safe tiles), fill grass in the enclosed region without enemies
        } else if (app.tile[r][c] == 1 || app.tile[r][c] == 4) {

            if (app.player.getDirection() == 37) {
                if (app.tile[r][c+1] == 0) {
                    app.tile[r][c+1] = 2;
                    // add unique tile to path
                    if (app.player.getPath().isEmpty() || app.player.getLastTileInPath()[0] != r || app.player.getLastTileInPath()[1] != c+1) {
                        int[] v = {r, c+1};
                        app.player.addVertexToPath(v);
                    }
                } 
            }  else if (app.player.getDirection() == 38) {
                if (app.tile[r+1][c] == 0) {
                    app.tile[r+1][c] = 2;    
                    // add unique tile to path
                    if (app.player.getPath().isEmpty() || app.player.getLastTileInPath()[0] != r+1 || app.player.getLastTileInPath()[1] != c) {
                        int[] v = {r+1, c};
                        app.player.addVertexToPath(v);
                    }
                } 
            }
            
            // player return to safe tile from dirt
            if (!app.player.getPath().isEmpty()) {
                int [] safeTiles = {1, 4};
                ArrayList<int[]> sp = shortestPath(app.player.getLastTileInPath(), app.player.getFirstTileInPath(), app.tile, safeTiles);
                
                // fill grass along the path
                for (int[] vertex: app.player.getPath()) {
                    r = vertex[0];
                    c = vertex[1];
                    app.tile[r][c] = 4;
                    app.numOfGrass++;
                }
                // path encloses an region
                if (!sp.isEmpty()) {
                    // System.out.println("Enclosed a region!!!");
                    // add the shortest path via safe tiles from end of the path to start of the path to form a cycle
                    ArrayList<int[]> cycle = new ArrayList<int[]>();
                    cycle.addAll(app.player.getPath());
                    cycle.addAll(sp);
                    
                    int[][] region = identifyRegions(app.player, app.tile, cycle);
                    app.numOfGrass += fillGrass(app.tile, region, app.enemiesList);
                }
                app.player.removePath();
            }
        } 

        // player hits its path
        playerHitOwnPath(app);
        
        // propagate every 3 frames
        if (app.propagateRedTile % 3 == 0) {
            propagateRedPath(app);
            app.propagateRedTile = 0;
        } 
        app.propagateRedTile++;
        
        // enemies hit path
        enemyHitPath(app);

        for (Enemy e : app.enemiesList) {
            // enemies hit player
            if (app.player.getRow() == e.getRow() && app.player.getCol() == e.getCol()) {
                app.player.respawn(app.tile);
            }
        }

    }

    /**
     * Find the shortest path between two tiles via the givien accessible tile types
     * @param startVertex 
     * @param endVertex 
     * @param tile 
     * @param accessibleTileType An integer array containing all the accessible tile types
     * @return An ArrayList of integer array containing the row and column of the tile along the path
     */
    public static ArrayList<int[]> shortestPath(int[] startVertex, int[] endVertex, int[][] tile, int[] accessibleTileType) {
        Queue<int[]> queue = new LinkedList<int[]>();
        ArrayList<int[]> path = new ArrayList<int[]>();
        boolean[][] visited = new boolean[tile.length][tile[0].length];
        queue.add(startVertex);
        visited[startVertex[0]][startVertex[1]] = true;
        Map<int[], int[]> parentMap = new HashMap<>();
        boolean reachEndVertex = false;

        int[] currentVertex = startVertex;
        parentMap.put(startVertex, null); 
        while (!queue.isEmpty()) {
            currentVertex = queue.remove();
            
            // break if the one of the neighbour vertex is the end vertex
            int[] all = {0, 1, 2, 3, 4};
            ArrayList<int[]> adjacentPathTiles = getNeighbours(currentVertex, tile, all);
            for (int[] v: adjacentPathTiles) {
                if (v[0] == endVertex[0] && v[1] == endVertex[1]) {
                    reachEndVertex = true;
                    break;
                }
            }
            if (reachEndVertex) {
                break;
            }

            ArrayList<int[]> adjacentSafeTiles = getNeighbours(currentVertex, tile, accessibleTileType);
            for (int[] v: adjacentSafeTiles) {
                if (!visited[v[0]][v[1]]) {
                    queue.add(v);
                    parentMap.put(v, currentVertex);
                    visited[v[0]][v[1]] = true;
                }
            }
        }

        if (!reachEndVertex) {
            return path;
        }
        while(currentVertex != null) {
            path.add(0, currentVertex);
            currentVertex = parentMap.get(currentVertex);
        }
        path.remove(0);
        return path;
    }
    
    /**
     * Get the adjacent tiles with selected types 
     * @param vertex  The row and column of the tile
     * @param tile  Represent the type of tile of all the grids
     * @param accessibleTileType An integer array containing all the accessible tile types
     * @return An ArrayList of integer array containing the accessible neighbour tiles 
     */
    public static ArrayList<int[]> getNeighbours(int[] vertex, int[][] tile, int[] accessibleTileType) {
        ArrayList<int[]> neighbours = new ArrayList<int[]>();
        int r = vertex[0];
        int c = vertex[1];
        int rMax = tile.length;
        int cMax = tile[0].length;
        int[] row = {r+1, r-1, r, r};  
        int[] col = {c, c, c+1, c-1};  

        for (int i = 0; i < 4; i++) {
            int nr = row[i];
            int nc = col[i];
            // skip out of bound tile
            if (nr < 0 || nr >= rMax || nc < 0 || nc >= cMax) {
                continue;
            }
            // assess the accessibility of the neighbour tile
            boolean accessible = false;
            for (int j = 0; j < accessibleTileType.length; j++) {
                // break the loop if the type of the neighour tile is one on the accessible tile types
                if (tile[nr][nc] == accessibleTileType[j]) {
                    accessible = true;
                    break;
                }
            }
            // add neighbour to list if it is accessiable
            if (accessible) {
                int[] v = {nr, nc};
                neighbours.add(v);
            }
        }
        return neighbours;
    }

    /**
     * A DFS approach to recursively fill assign a value to accessible tiles
     * @param tile Represent the type of tile of all the grids
     * @param r Row of the tile
     * @param c Column of the tile
     * @param type The value to assign
     * @return The number of tiles filled
     */
    public static int recursiveFill(int[][] tile, int r, int c, int type) {
        // tile out of bound
        if (r < 0 || r >= tile.length || c < 0 || c >= tile[0].length) {
            return 0;
        }
        // tile was filled
        if (tile[r][c] != 0) {
            return 0;
        }
        // fill grass
        tile[r][c] = type;
        // dfs find all tile to be filled
        return 1 + recursiveFill(tile, r+1, c, type) + recursiveFill(tile, r-1, c, type) + 
                   recursiveFill(tile, r, c+1, type) + recursiveFill(tile, r, c-1, type); 
    }

    /**
     * If the player circles back and hits their own in-progress path, they lose a life and respawn.
     * @param app The window to draw onto.
     */
    public static void playerHitOwnPath(App app) {
        int r = app.player.getRow();
        int c = app.player.getCol();
        if (((app.player.getDirection() == 37 || app.player.getDirection() == 38) && (app.tile[r][c] == 2 || app.tile[r][c] == 3)) ||
        (app.player.getDirection() == 39 && (app.tile[r][Math.min(c+1, 63)] == 2 || app.tile[r][Math.min(c+1, 63)] == 3)) ||
        (app.player.getDirection() == 40 && (app.tile[Math.min(r+1, 31)][c] == 2 || app.tile[Math.min(r+1, 31)][c] == 3))) {
            // respawn player
            app.player.respawn(app.tile); 
        }
    }
    
    /**
     * If an enemy hits the path, it hits will turn red
     * @param app The window to draw onto.
     */
    public static void enemyHitPath(App app) {
        
        for (Enemy e: app.enemiesList) {
            int r = e.getRow(), c = e.getCol();
            if (!e.isMoveLeft()) {
                c = Math.min(c+1, 63);
            }
            if (!e.isMoveUp()) {
                r = Math.min(r+1, 31);
            }
            // enemy hits path in progress
            if (app.tile[r][c] == 2) {
                // turn it to red
                app.tile[r][c] = 3;
            }
        }  
    }

    /**
     *  Propagate the red tiles outward towards the player at a rate of one tile every 3 frames. 
     * @param app The window to draw onto.
     */
    public static void propagateRedPath(App app) {
        int r = app.player.getRow();
        int c = app.player.getCol();
        if (app.player.getDirection() == 37) {
            c = Math.min(c+1, 63);
        } else if (app.player.getDirection() == 38) {
            r = Math.min(r+1, 31);
        } else if (app.player.getDirection() == 39) {
            c = Math.max(c-1, 0);
        } else if (app.player.getDirection() == 40) {
            r = Math.max(r-1, 0);
        }

        ArrayList<int[]> neighbours = new ArrayList<int[]>();
        // propagate red tile to the neighbour tiles
        for (int i = 0; i < app.tile.length; i++) {

            for (int j = 0; j < app.tile[0].length; j++) {

                // encounter a red tile
                if (app.tile[i][j] == 3) {
                    int [] v = {i, j};
                    int[] type = {2};
                    // get the neighbours path tile (type: 2)
                    neighbours.addAll(getNeighbours(v, app.tile, type));
                }
                /*
                if (app.tile[i][j] == 3) {
                    if (app.tile[Math.max(i-1, 0)][j] == 2) {
                        app.tile[Math.max(i-1, 0)][j] = 3;
                    }
                    if (app.tile[Math.min(i+1, 31)][j] == 2) {
                        app.tile[Math.min(i+1, 31)][j] = 3;
                    }
                    if (app.tile[i][Math.max(j-1, 0)] == 2) {
                        app.tile[i][Math.max(j-1, 0)] = 3;
                    }
                    if (app.tile[i][Math.min(j+1, 63)] == 2) {
                        app.tile[i][Math.min(j+1, 63)] = 3;
                    }
                }
                */

            }
        }
        // make them red
        for (int[] n: neighbours) {
            app.tile[n[0]][n[1]] = 3;
        }
    }

    /**
     * Generate a 2-dimensdion <code>int</code> array which represent the region each tile belongs to.
     * @param player 
     * @param tile Represent the type of tile of all the grids
     * @param cycle List of <code>int[]</code> representing the row and column of the tile on the cycle
     * @return 2-dimensdion <code>int</code> array which represent the region each tile belongs to.
     * <p>
     * 0: unaccessible, 1: filled, 2: path, 3 and 4, two enclosed regions
     */
    public static int[][] identifyRegions(Player player, int[][] tile, ArrayList<int[]> cycle) {
        int[][] region = new int[tile.length][tile[0].length];
        // Filled tiles: 1
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                if (tile[i][j] == 1 || tile[i][j] == 4) { 
                    region[i][j] = 1;
                }
            }
        }
        // Path tiles: 2
        for (int[] v : player.getPath()) {
            region[v[0]][v[1]] = 2;
        }
        // two enclosed regions tiles: 3 or 4
        boolean foundEnclosedRegion = false;
        for (int i = 0; i < region.length; i++) {
            for (int j = 0; j < region[0].length; j++) {
                int[] v = {i,j};
                int[] t = {0, 2};
                int[] p = player.getFirstTileInPath();
                // enclosed region tiles
                if (region[i][j] == 0 && (!shortestPath(v, p, region, t).isEmpty() ||
                (Math.abs(i-p[0]) + Math.abs(j-p[1])) <= 1 )) {
                    if (!foundEnclosedRegion) {
                        recursiveFill(region, i, j, 3);
                        foundEnclosedRegion = true;
                    } else {
                        recursiveFill(region, i, j, 4);

                    }
                }
            }
        }       
        return region;
    }

    /**
     * Fill grass on tile
     * @param tile Represent the type of tile of all the grids
     * @param region Represent the type of region of all the grids
     * @param enemiesList List of <code>Enemy</code> objects
     * @return the number of grass filled
     */
    public static int fillGrass(int[][] tile, int[][] region, List<Enemy> enemiesList) {
        int numOfGrassFilled = 0;
        boolean fillRegion3 = true;
        boolean fillRegion4 = true;
        for (Enemy e : enemiesList) {
            int r = e.getRow();
            int c = e.getCol();
            // Enemy e is in region 3
            if (fillRegion3 && (region[r][c] == 3 || region[r][c] == 2)) {
                fillRegion3 = false;
            // Enemy e is in region 4
            } else if (fillRegion4 && region[r][c] == 4) {
                fillRegion4 = false;
            }
        }
        // Fill grass 
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[0].length; j++) {
                if (fillRegion3 && region[i][j] == 3) {
                    tile[i][j] = 4;
                    numOfGrassFilled++;
                } 
                if (fillRegion4 && region[i][j] == 4) {
                    tile[i][j] = 4;
                    numOfGrassFilled++;
                }
            }
        }
        return numOfGrassFilled;
    }
}
