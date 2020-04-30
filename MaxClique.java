
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

class Maxclique
{

    static int MAXSIZE = 4039;
    static int []store = new int[MAXSIZE];
    static int numberOfNodes = 100, newNode;
    static int [][]graph = new int [MAXSIZE][MAXSIZE];
    static int [][] edges = new int[15120][15120];
    static ArrayList<HashSet<Integer>> cliques = new ArrayList<>();
    static HashSet<Integer> network = new HashSet<>();
    static HashSet<Integer> suggestions = new HashSet<>();


    // Degree of the vertices
    static int []degree = new int[MAXSIZE];

    // Determine if given subset of nodes is clique or not
    static boolean isClique(int b)
    {
        // Run a loop for all the set of edges
        // for the select vertex
        for (int i = 1; i < b; i++)
        {
            for (int j = i + 1; j < b; j++)
                if (graph[store[i]][store[j]] == 0)
                    return false;
        }
        return true;
    }

    // Prints the entire clique of size k
    static void printCliques(int n,int size)
    {
        HashSet<Integer> temp = new HashSet<>();
        System.out.println("Clique of size "+size+" in the graph network");
        for (int i = 1; i < n; i++){
            System.out.print(store[i] + " ");
            temp.add(store[i]);
        }
        cliques.add(temp);
        System.out.println();
    }

    // Recursive function to find cliques(communities) of size k
    static void findCliques(int i, int presentLength, int size)
    {
        // Check if any vertices from i+1 can be inserted
        for (int j = i + 1; j <= numberOfNodes - (size - presentLength); j++)

            // If the degree of the graph is sufficient
            if (degree[j] >= size - 1)
            {
                // Add the vertex to store
                store[presentLength] = j;

                // If the graph is not a clique of size k
                // then it cannot be a clique
                // by adding another edge
                if (isClique(presentLength + 1))
                    if (presentLength < size)
                        findCliques(j, presentLength + 1, size);
                    else
                        printCliques(presentLength + 1,size);
            }
    }

    // Generate adjacency matrix for all the inout nodes from dataset
    static void generateAdjacencyMatrix(int size){
        for (int i = 0; i < size; i++)
        {
            graph[edges[i][0]][edges[i][1]] = 1;
            graph[edges[i][1]][edges[i][0]] = 1;
            degree[edges[i][0]]++;
            degree[edges[i][1]]++;
        }
    }

    // Add new node into the network
    static void addNewNode() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter new node which enters the system");
        newNode = Integer.parseInt(br.readLine());
        System.out.println("Enter space separated nodes "+newNode+" is friends with");
        String [] friends = br.readLine().split(" ");

        for(String node: friends){
            network.add(Integer.parseInt(node));
        }
    }

    // Find uncommon friends between new node and existing cliques
    static HashSet<Integer> findUncommonFriends(HashSet<Integer> a, HashSet<Integer> b){
        HashSet<Integer> result = new HashSet<>();

        for (Integer el: a) {
            if (!b.contains(el)) {
                result.add(el);
            }
        }
        for (Integer el: b) {
            if (!a.contains(el)) {
                result.add(el);
            }
        }
        return result;
    }

    // Suggest new friends to the network
    static void findSuggestions(){

        for(HashSet<Integer> singleClique: cliques){
            HashSet<Integer> result = findUncommonFriends(singleClique,network);

            for(int n:result){
                if(!network.contains(n))
                    suggestions.add(n);
            }
        }
    }

    // Print new friend suggestions
    static void printSuggestions(){
        System.out.println("Friend suggestions for "+newNode+"\n"+suggestions);
    }

    public static void main(String[] args) throws Exception
    {

        URL path = ClassLoader.getSystemResource("facebook_combined.txt");
        File file = new File(path.toURI());
        BufferedReader br = new BufferedReader(new FileReader(file));

        String input;
        int e = 0;

        // Accept input by parsing dataset
        while((input=br.readLine())!=null){
            edges[e][0] = Integer.parseInt(input.split(" ")[0]);
            edges[e][1] = Integer.parseInt(input.split(" ")[1]);
            e++;
        }

        // Input edges
        int size = edges.length;

        // Generate Adjacency matrix and calculate degree
        generateAdjacencyMatrix(size);


        // Determine cliques of size k from 3 to numberOfNodes
        for(int k=3;k<numberOfNodes;k++){
            findCliques(0, 1, k);
        }

        // Add new node to the network
        addNewNode();

        //Find new friend suggestions from existing cliques
        findSuggestions();

        // Print all the suggestions for newly added node in the network
        printSuggestions();

    }
}