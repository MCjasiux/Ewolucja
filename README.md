# Ewolucja/Evolution

This app simulates life of generations of animals. Every day an animal rotates by a certain amount - based on its genotype. 
Moving costs energy, eating plants gives it back. Meeting with other animals can result in reproduction!

![obraz](https://user-images.githubusercontent.com/19930849/171270386-61a425f2-f2d3-4c08-86ca-09879d3df861.png)

## Running
This is a gradle project. It's ready to run when imported.

## Config
It's possible to adjust the simulation by changing `parameters.json` file. Its content is described as follows:
 * `width` and `height` : dimensions of the map
 * `startEnergy` : energy of first animals
 * `moveEnergy` : movement cost in energy units
 * `plantEnergy` : energy gained from eating a plant
 * `jungleRatio` : size of jungle in relation to whole map. Actual area ratio is squared, since given ratio is a fraction of map sides' length
 * `growthRate` : number of plants appearing every day on savannah AND in jungle
 * `reproductionRatio` : parents give this proportion of energy to their child
 * `resolution` : resolution of a map 1x1 field in pixels

Possible names are stored in `names.txt` file seperated by endline.

## Interface
![obraz](https://user-images.githubusercontent.com/19930849/171271550-feff0924-c19c-4936-a3ae-f556436af109.png)

### Top-left corner - map
  Map represents where animals live and die. Green square in the middle is a jungle, food appears there more frequently.
  Yellow area outside is savannah, more scarse with food. Black dots represent animals, while red dot is an animal currently followed.

### Top - statistics
  Text next to the map shows relevant statistics, updated real-time.
  
### Top-right - controls
  Buttons in top right corner allow user to control simulation. "Skip" button skips number of days, given in the field below. "Start saving" button starts saving statistics to a .csv file.
  
### Middle
  Dropdown list in the middle contains every animal alive - their names, their parents' names (if they had them), and their position on the map. Selecting an animal shows its properties below:
  it's age, current energy, position and direction, genotype and number of children and descendants.
  
### Bottom
  Chart at the bottom visualizes number of animal's children and descendants over time. Chart is being updated even after animals death.
