To detect wall, we need to use a 2-D array to store what object is on the position : 
    int[][] map[HEIGHT/SPRITSIZE][WIDTH/SPRITESIZE]
    concrete, lawn, empty, path in progress, path hit by enemies

# Bugs: 
- Enemy could not be spawn at the occupied space, i.g, on the concrete
- Enemy should have random initial direction

# How to fill the grass
- store the position of the start and end tiles of the path
- if there is another path of which only cosists of safe tiles(concrete or grass), then the path encloses an region
    
# Player class
## Attributes
- x
- y
- sprite
- remainingLives
## Method
- tick(): update player's position every frame
- 
