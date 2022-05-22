# Projeto PA 2021/22 - Época Normal

# Project developed by:

Eduardo Ferreira nº201100372 

Marco Martins nº201601467

Rui Almeida nº201601035

# Introduction

The main goal of this project consists on developing a java application that is capable of using a graph as a way to store its data 
and assist in having a better understanding/grasp of the locations of each vertex which in this case they will be considered Hubs
containing its name and its weight value, this value will pretty much be the "weight" or "amount" of certain element/product that exists
in the Hub, and its edges that in this particular project will be seen/represent as a distance and necessary travel route between the various existing Hubs.

Basically it must be capable of providing an accurate representation of its logistic network so that we can visualize it, 
obtain the necessary information about each and every vertex/location and edges/routes and manipulate it if needed be whenever certain changes are required.

# How to use

To run the application the user must simply run its main file. Upon running a window will be displayed asking the user to
type the desired dataset that he wishes to read/analyze as well as how to type it.

For example: If the user wishes to read a determined sgb he must type the desired sgb and the specific route, like this "sgb128 routes_2"
even if there's only 1 route in that sgb he still must type the desired route so if we were to choose sgb32 it would have to be "sgb32 routes_1"
we made it this way so theres a common ground when it comes to find a certain sgb and its route so if we in the future were to add another sgb or another route
the user would just need to type it without requiring any changes in the code.

Upon getting the desired route a pop up will show containing the graph, this part of the code is the one that uses the [JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph).
However that is not the end of out application, on the main screen will now be a title on the top showing which sgb we are viewing and
on the left side there will be numerous options that the user can pick (for the 1st milestone only 2 options exist), 
the user simply has to type the number that identifies one of these options in order to activate them.

These options are:

`1 - New Dataset:` By using this one the user can "backtrack" and from the current dataset in order to view another one.

`2 - Adjacency List:` By using this option a javafx Observable list will appear on the center and right side of the main screen displaying all the vertexes and the incident edges attached to each one.

We opted to have the application work this way instead of just applying and Hbox onto the [JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph) and display it
on the main screen because this way in the future we can display any sort of necessary information onto our main screen, effectively turning it into a
gatherer of intel of sorts while keeping the visual part of it separated on another window to ease the observation of the graph without any other distractions surrounding it.


# Classes used

`Hub:` This class will contain the name and weight of a Hub, basically storing the name of the hub
plus its ammount/weight, other than that the methods are simply getters and setters.

`Route:` This class will contain the distance, storing the distance and having its getters and setters.

`FileReader:` This class will focus solely on reading the inputs of the user and apply them, it's where the graph will be stored as a variable/object as well as other variables that we deemed
 necessary such as Logistic Network (will be further explain on the class below why we created this)

`Logistic Network:` This class will contain the information obtained from all the text files, basically storing everything so that later may be used
on building the graph, though it might seem rather unnecessary since we could just read and build the graph using simply local variables on the FileReader this way provides an ease of access
for future implementations and if we ever need to have a way to change the values of the current text files
we can easily apply the alterations here and then write them back onto the current textfiles so next time we read them it will display an updated graph.

`Alerts:` This class is rather peculiar, it will pretty much function as a pop-up window to display errors / messages, so it works kinda like an exception but instead this alert 
is thrown showing the error that is happening to the user so he may he correct himself.
 
`GraphicalUI:` As the name suggests, this class will contain the methods that will manipulate what will be displayed onto the user.

`GraphAdjacencyList:` This is our graph class, it uses the data structure of an adjacency list, the template was obtained from the Advanced Programing Class pdf file
that discusses the 3 types of structures that can be used (matrix / edgelist / adjacencyList). It contains all the methods to be able to manipulate the information and create our graph.

# Important Methods Explanation

## FileReader

`readInput:` This method will be purely dedicated in reading the inputs of the user, at first it will detect if a graph is already being displayed,
if so then it will skip that part and go into the switch case condition, this switch case uses an int to choose which case to go to,
the int is tied to the user's input when it comes to choose one of the many options that will be implemented to manipulate/affect the graph, for now
he can only read another dataset(reset the app in a way), or choose to display an the list of adjacencies.

`createNetwork:` This method will be responsible to instantiate the logisticNetwork object and place what is read from the text files onto it so that it may be
at a later time used to create our graph.

`createVertexes:` This method will create the vertexes of hour graph.

`createEdges:` After the edges have been created this method will implement all of its edges, passing the information down to our GraphAdjacencyList
to update our already existing vertexes.

## Alerts

`Display` This method will be used to create a pop-up window to warn the user that something he types is incorrect and the files couldn't be found,
it extends from FiLeNotFoundException and a super with the message so it also displays on console.

## GraphicalUI

`addTopContents` This will be responsible for adding the title of our dataset so we know which sgb we have chosen, its an Hbox that will be manipulated into being on top of the screen.

`addBottomContents` This will be responsible for adding the input box onto our screen, so that we may receive the user input, it also creates an enter button to send the information though pressing enter on the keyboard works too.

`addLeftContents` This will be responsible in displaying the numerous options that will exist on our application. it will pretty much show a list of all available choices the user can do after it has loaded
the graph.

`designGraph` This method will be responsible for designing the graph after all the information has been obtained.

`setGraphPositions` This method is called from `designGraph` and will place all the vertexes on the correct positions.

`displayAdjacency` This method is one of the options that the user will have after the graph has been loaded, and pretty much
serves to show the list of adjacency to our user in a frontend approach.

## GraphAdjacencyList

Every method from this class already exists on another class called GraphEdgeList which was a class already built in that would produce
the graph with a different data structure, so basically GraphAdjacencyList does what GraphEdgeList does but the methods were
changed/developed to work with an entirely new data structure where each vertex holds a list of incident edges.



# File and folder structure

- `/dataset` - Contains the *datasets* that we can import with the application
- `/src` - Contains the source code of the application
    - `com.brunomnsilva.smartgraph` - *Package* contains the library [JavaFXSmartGraph](https://github.com/brunomnsilva/JavaFXSmartGraph).
    - `pt.pa` - *Package* where the project will be developed on.
        - `*.graph` - *Package* contains an ADT Graph and a functional implementation based on the edgeList structure.
        - `*.model` - *Package* contains the model classes for the application.
        - `*.view` - *Package* contains the graphical part of the application.
    - `Main.java` - The main file for the application, where the app will be run.
- `/test` - Contains the unit tests of our application
- `smartgraph.css` - *Stylesheet* used by the library JavaFXSmartGraph
- `smartgraph.properties` - *Properties* used by the library JavaFXSmartGraph
- `README.md` - Our readme file.
-  `READMEPT` - Our readme file translated into Portuguese.
# Data to import

The files are in text format, and they can contain commentary in certain lines, those lines are to be disregarded. Commentaries have a `"#"` symbol before it.

The *datasets* are in the local `dataset` folder, so any file located in that folder can be opened with, e.g., `new FileReader("dataset/<folder>/<file>.txt")`, `<file>` being the respective file to open.
Theres two specific *datasets*, `sbg32` e `sbg128`, containing 32 e 128 hubs respectively.

Each dataset contains the following files:

- `name.txt` - Names of the cities where the *hubs* are located
- `weight.txt` - Population of the cities where the *hubs* are located
- `xy.txt` - coordinates of the cities where the *hubs* are located

- `routes_*.txt` - Distance matrix([wikipedia](https://pt.wikipedia.org/wiki/Matriz_de_dist%C3%A2ncias)) responsible for charting all the hubs - there can be different routes, they are mainly identified with the suffix `"_1"` e `"_2"`.

# Desired result of the pre-existing SGBs

### sgb32

`routes_1.txt` (Split graph - 2 components):

![](sgb32_1.png)

### sgb128

`routes_1.txt` (1 component):

![](sgb128_1.png)

`routes_2.txt` (Split graph - 3 componenes):

![](sgb128_2.png)
