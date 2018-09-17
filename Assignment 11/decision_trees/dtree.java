
/**
 * @author liquidf4ntasy
 * Satyajit Deshmukhh
 * UTA ID: 1001417727
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class dtree {
    public static int node_cnt = 0;
    private class Node {
        public int nodeType;
        double entropy;
        public int id = 0;
        public double currentAttribute;
        public double threshold;
		public int classLabel;
        public Node leftTree;
        public Node rightTree;
        ArrayList<Double> distribution = new ArrayList<Double>();
    }

    @SuppressWarnings("resource")
	public static void main(String[] args) throws IOException  {

        if (args.length != 4) {
            System.out.println("Usage: dtree <training_file> <test_file> <option> <pruning_thr>\n");
            exit_function(0);
        }

        String filename = args[0];
        String testMatrix = args[1];
        String option = args[2];
        int pruningThreshold = Integer.parseInt(args[3]);

        /*
        // get outputs to text file to generate answers.pdf
        String output= option + " " +  pruningThreshold + ".txt";
    	System.setOut(new PrintStream(new FileOutputStream(output)));
         */
        ArrayList<Double> target = new ArrayList();
        ArrayList<ArrayList<Double>> ipMatrix = new ArrayList<ArrayList<Double>>();
        File file = new File(filename);
        Scanner input = new Scanner(file);
        
        ArrayList<Double> minValues = new ArrayList<Double>();
        ArrayList<Double> maxValues = new ArrayList<Double>();

        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            ArrayList<Double> row = new ArrayList();
            while (colReader.hasNextDouble()) {
                row.add(colReader.nextDouble());
            }
            ipMatrix.add(row);
            target.add(row.get(row.size() - 1));
        }

        dtree obj = new dtree();

        minValues.addAll(ipMatrix.get(0));
        maxValues.addAll(ipMatrix.get(0));

        for (int i = 0;
                i < ipMatrix.size();
                i++) {
            for (int j = 0; j < ipMatrix.get(0).size() - 1; j++) {

                if (ipMatrix.get(i).get(j) < minValues.get(j)) {
                    minValues.add(j, ipMatrix.get(i).get(j));
                }
                if (ipMatrix.get(i).get(j) > maxValues.get(j)) {
                    maxValues.add(j, ipMatrix.get(i).get(j));
                }
            }
        }

        ArrayList<Double> currentAttributes = new ArrayList<Double>();
        for (int i = 0;
                i < 16; i++) {
            currentAttributes.add((double) i);
        }

        ArrayList<Node> decisionTree = new ArrayList<Node>();

        if (option.equalsIgnoreCase(
                "optimized") || option.equalsIgnoreCase("randomized")) {
            
            Node node = obj.DecisionTree(ipMatrix, currentAttributes, null, pruningThreshold, option);

           
            ArrayList<Node> nodes = obj.mainBFS(node, 0);

            decisionTree.add(node);
        }

        if (option.equalsIgnoreCase(
                "forest3")) {
            for (int i = 0; i < 3; i++) {
                Node node = obj.DecisionTree(ipMatrix, currentAttributes, null, pruningThreshold, option);
                ArrayList<Node> nodes = obj.mainBFS(node, i);
                decisionTree.add(node);
            }
        }

        if (option.equalsIgnoreCase(
                "forest15")) {
            for (int i = 0; i < 15; i++) {
                Node node = obj.DecisionTree(ipMatrix, currentAttributes, null, pruningThreshold, option);
                ArrayList<Node> nodes = obj.mainBFS(node, i);
                decisionTree.add(node);
            }
        }
        
        ArrayList<Double> target_test = new ArrayList();
        ArrayList<ArrayList<Double>> testMarix = new ArrayList<ArrayList<Double>>();
       
        File file2 = new File(testMatrix);
        Scanner test = new Scanner(file2);
        
        while (test.hasNextLine()) {
            Scanner colReader = new Scanner(test.nextLine());
            ArrayList<Double> row = new ArrayList<Double>();
            while (colReader.hasNextDouble()) {
                row.add(colReader.nextDouble());
            }
            testMarix.add(row);
            target_test.add(row.get(row.size() - 1));

        }

      
        int correct_classification_count = 0;
        int total_records = testMarix.size();
        for (int object_id = 0;
                object_id < total_records;
                object_id++) {

            ArrayList<ArrayList<Double>> dists = new ArrayList<ArrayList<Double>>();
            double predicted_class;
            double actual_class = target_test.get(object_id);
            double accuracy = 0;

            for (Node node : decisionTree) {
                ArrayList<Double> dist = obj.classify(testMarix.get(object_id), node);
                dists.add(dist);
            }

            if (dists.size() == 1) {
                ArrayList<Double> dist1 = dists.get(0);
                double max = Collections.max(dist1);
               
                ArrayList<Integer> positions = occurance_of_dist(dist1, max);
                
                int last_index = positions.size() - 1;
                int count = positions.get(last_index);
                positions.remove(last_index);

                if (last_index >= 2) {
                    int r = (int) (Math.random() * 1000) % count;
                    predicted_class = positions.get(r);
                } else {
                    predicted_class = positions.get(0);
                }
            } else {
                int size_dist = dists.size();
                ArrayList<Double> sum_dist = new ArrayList<Double>();

                for (int i = 0; i < 10; i++) {
                    sum_dist.add(0.0);
                }

                for (int i = 0; i < dists.size(); i++) {
                    ArrayList<Double> dist_intr = dists.get(i);

                    for (int j = 0; j < dist_intr.size(); j++) {
                        double sum = sum_dist.get(j);
                        sum = sum + dist_intr.get(j);
                        sum_dist.set(j, sum);
                    }
                }

                for (Double sum : sum_dist) {
                    sum = sum / (double) size_dist;
                }

                double max = Collections.max(sum_dist);
                
                ArrayList<Integer> positions = occurance_of_dist(sum_dist, max);
                
                int storesCountOfArrayIndex = positions.size() - 1;
                int count = positions.get(storesCountOfArrayIndex);
                positions.remove(storesCountOfArrayIndex);

                if (storesCountOfArrayIndex >= 2) {
                    int r = (int) (Math.random() * 1000) % count;
                    predicted_class = positions.get(r);
                } else {
                    predicted_class = positions.get(0);
                }
            }

            if (Double.compare(predicted_class, actual_class) == 0) {
                accuracy = 1;
                correct_classification_count++;
            }
            System.out.printf("ID=%5d, predicted=%3d, true=%3d, accuracy=%4.2f\n", object_id, (int) predicted_class, (int) actual_class, accuracy);
        }

        double classification_accuracy = correct_classification_count / (double) total_records;

        System.out.printf(
                "classification accuracy=%6.4f\n", classification_accuracy);

    }

    public ArrayList<Node> mainBFS(Node node, int treeNumber) {
        int id = 1;
        ArrayList<Node> ascendingNodesList = new ArrayList<Node>();
        node.id = 1;
        ascendingNodesList.add(node);
        BFS(ascendingNodesList, treeNumber);
        return ascendingNodesList;
    }

    public void BFS(ArrayList<Node> ascendingNodesList, int treeNumber) {

        ArrayList<Node> node_ch = new ArrayList<Node>();

        for (Node node : ascendingNodesList) {
            int feature = (int) node.currentAttribute;
            double threshold = node.threshold;
            if (node.nodeType == 1 || node.nodeType == 2) {
                feature = -1;
                threshold = -1;
            }
            System.out.printf("tree=%2d, node=%3d, feature=%2d, thr=%6.2f, gain=%f\n", treeNumber, node.id, feature,threshold , node.entropy);

            if (node.nodeType == 0) {
                node.leftTree.id = 2 * node.id;
                node_ch.add(node.leftTree);
                node.rightTree.id = 2 * node.id + 1;
                node_ch.add(node.rightTree);
            }
        }

        if (!node_ch.isEmpty()) {
            BFS(node_ch, treeNumber);
        }

    }

    public ArrayList<Double> selectColumn(ArrayList<ArrayList<Double>> currentMatrix, int currentAttribute) {
        ArrayList<Double> attributeValues = new ArrayList<Double>();
        for (int p = 0; p < currentMatrix.size(); p++) {
            attributeValues.add(currentMatrix.get(p).get((int) currentAttribute));
        }
        return attributeValues;
    }

    public static ArrayList<Integer> occurance_of_dist(ArrayList<Double> dist, Double max) {
        int count = 0;
        ArrayList<Integer> positions = new ArrayList<Integer>();
        for (int i = 0; i < dist.size(); i++) {
            Double array_element = dist.get(i);
            if (Double.compare(array_element, max) == 0) {
                count++;
                positions.add(i);
            }
        }
        positions.add(count);
        return positions;
    }

    public ArrayList<Double> classify(ArrayList<Double> row, Node node) {
        ArrayList<Double> value;
        switch (node.nodeType) {
            case 2: {
                ArrayList<Double> dist = new ArrayList<Double>();
                for (int i = 0; i < 10; i++) {
                    dist.add(0.0);
                }
                dist.set(node.classLabel, 1.0);
                return dist;
            }
            case 1: {
                ArrayList<Double> dist = node.distribution;
                return dist;
            }
            default:
                int attribute = (int) node.currentAttribute;
                double threshold = (double) node.threshold;
                double attribute_value = (double) row.get(attribute);
                Node child;
                if (Double.compare(attribute_value, threshold) == -1) {
                    child = node.leftTree;
                } else {
                    child = node.rightTree;
                }
                value = classify(row, child);
                break;
        }

        return value;

    }

    public ArrayList<Double> gen_attr_col(ArrayList<ArrayList<Double>> currentMatrix, int attribute) {
        ArrayList<Double> attributeValues = new ArrayList<Double>();
        for (int p = 0; p < currentMatrix.size(); p++) {
            attributeValues.add(currentMatrix.get(p).get(attribute));
        }
        return attributeValues;
    }

    public ArrayList<Double> chooseAttributeOptimized(ArrayList<ArrayList<Double>> currentMatrix,
            ArrayList<Double> currentAttributes) {

        ArrayList<Double> attributeValues;
        double maxGain = -1, bestAttribute = -1, bestThreshold = -1;
        double currentEntropy = calcEntropy(currentMatrix);

        for (int i = 0; i < currentAttributes.size(); i++) {

            attributeValues = gen_attr_col(currentMatrix, (int) i);

            double min = attributeValues.get(0);
            double max = attributeValues.get(0);
            for (Double value : attributeValues) {
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }

            for (int thresholdFactor = 1; thresholdFactor < 50; thresholdFactor++) {
                double threshold = min + thresholdFactor * (max - min) / 51;
                double gain = calculateInformationGain(currentMatrix, i, threshold, currentEntropy);

                if (gain > maxGain) {
                    maxGain = gain;
                    bestAttribute = i;
                    bestThreshold = threshold;
                }
            }
        }
        ArrayList<Double> returnValue = new ArrayList<Double>();
        returnValue.add(bestAttribute);
        returnValue.add(bestThreshold);
        returnValue.add(maxGain);

        return returnValue;
    }

    public ArrayList<Double> chooseAttributeRandomized(ArrayList<ArrayList<Double>> currentMatrix,
            ArrayList<Double> currentAttributes) {

        double maxGain = -1, bestAttribute = -1, bestThreshold = -1;
        double currentEntropy = calcEntropy(currentMatrix);
       
        int bestAttribute1 = (int) (Math.random() * 1000) % 10;
        bestAttribute = (double) bestAttribute1;

        ArrayList<Double> attributeValues = gen_attr_col(currentMatrix, bestAttribute1);

      
        double min = attributeValues.get(0);
        double max = attributeValues.get(0);
        for (Double value : attributeValues) {
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        for (int k = 1; k <= 50; k++) {

            double threshold = min + k * (max - min) / 51;

            double gain = calculateInformationGain(currentMatrix, bestAttribute1, threshold, currentEntropy);
           
            if (gain > maxGain) {
                maxGain = gain;
                bestThreshold = threshold;
            }
        }
        ArrayList<Double> returnValue = new ArrayList<Double>();
        returnValue.add(bestAttribute);
        returnValue.add(bestThreshold);
        returnValue.add(maxGain);

        return returnValue;
    }

    public double calculateInformationGain(ArrayList<ArrayList<Double>> currentMatrix, int currentAttribute,
            double threshold, double parentEntropy) {

        double informationGain;

        ArrayList<ArrayList<Double>> lessThanThresholdMatrix = lessThanThresholdMatrix(currentMatrix, (double) currentAttribute, threshold);
        ArrayList<ArrayList<Double>> greaterThanThresholdMatrix = greaterThanThresholdMatrix(currentMatrix, (double) currentAttribute, threshold);

        double lessThanMatrixEntropy = calcEntropy(lessThanThresholdMatrix);
        double greaterThanMatrixEntropy = calcEntropy(greaterThanThresholdMatrix);

        informationGain = parentEntropy - (lessThanMatrixEntropy * (lessThanThresholdMatrix.size() / (double) currentMatrix.size()) + greaterThanMatrixEntropy * (greaterThanThresholdMatrix.size() / (double) currentMatrix.size()));

        return informationGain;
    }

    public boolean dupe_class_check(ArrayList<ArrayList<Double>> currentMatrix) {
        boolean check = true;
        int columnSize = currentMatrix.get(0).size() - 1;
        double classValue = currentMatrix.get(0).get(columnSize);
        for (int i = 0; i < currentMatrix.size(); i++) {
            if (currentMatrix.get(i).get(columnSize) != classValue) {
                check = false; 
                break;
            }
        }

        return check;
    }

    public Node DecisionTree(ArrayList<ArrayList<Double>> currentMatrix, ArrayList<Double> currentAttributes,
            ArrayList<Double> defaultValue, int pruningValue, String option) {

     
        if (currentMatrix.size() < pruningValue) {
            Node tree = new Node();
            tree.distribution = defaultValue;
            tree.nodeType = 1;
            tree.id = 0;
            node_cnt++;
            return tree;
        } else if (dupe_class_check(currentMatrix)) {    

            int columnSize = currentMatrix.get(0).size() - 1;
            double classValue = currentMatrix.get(0).get(columnSize);
            Node tree = new Node();
            tree.classLabel = (int) classValue;

            tree.nodeType = 2;
            tree.id = 0;
            node_cnt++;
            return tree;
        } else {    
            ArrayList<Double> returnValue;
            ArrayList<ArrayList<Double>> leftChildMatrix;
            ArrayList<ArrayList<Double>> rightChildMatrix;

            Node tree = new Node();
            tree.nodeType = 0;
            node_cnt++;
            tree.id = 0;

            if (option.equalsIgnoreCase("optimized")) {
                returnValue = chooseAttributeOptimized(currentMatrix, currentAttributes);
            } else {
                returnValue = chooseAttributeRandomized(currentMatrix, currentAttributes);
            }
            tree.currentAttribute = returnValue.get(0);
            tree.threshold = returnValue.get(1);
            tree.entropy = returnValue.get(2);

            leftChildMatrix = lessThanThresholdMatrix(currentMatrix, returnValue.get(0), returnValue.get(1));
            rightChildMatrix = greaterThanThresholdMatrix(currentMatrix, returnValue.get(0), returnValue.get(1));

            ArrayList<Double> default_value = null;
            if (leftChildMatrix.size() < pruningValue || rightChildMatrix.size() < pruningValue) {
                default_value = distribution(currentMatrix);

            }
            tree.leftTree = DecisionTree(leftChildMatrix, currentAttributes, default_value, pruningValue, option);
            tree.rightTree = DecisionTree(rightChildMatrix, currentAttributes, default_value, pruningValue, option);
            return tree;
        }

    }

   
    public ArrayList<ArrayList<Double>> lessThanThresholdMatrix(ArrayList<ArrayList<Double>> currentMatrix,
            double bestAttribute, double bestThreshold) {
        int newBestAttribute = (int) bestAttribute;
        ArrayList<ArrayList<Double>> returnMatrix = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < currentMatrix.size(); i++) {
            if (currentMatrix.get(i).get(newBestAttribute) < bestThreshold) {
                returnMatrix.add(currentMatrix.get(i));
            }
        }
        return returnMatrix;
    }

 
    public ArrayList<ArrayList<Double>> greaterThanThresholdMatrix(ArrayList<ArrayList<Double>> currentMatrix,
            double bestAttribute, double bestThreshold) {
        int newBestAttribute = (int) bestAttribute;
        ArrayList<ArrayList<Double>> returnMatrix = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < currentMatrix.size(); i++) {
            if (currentMatrix.get(i).get(newBestAttribute) >= bestThreshold) {
                returnMatrix.add(currentMatrix.get(i));
            }
        }
        return returnMatrix;
    }

 
    public ArrayList<Double> distribution(ArrayList<ArrayList<Double>> currentMatrix) {
        ArrayList<Double> returnMatrix = new ArrayList<Double>();
        int classesCount[] = find_total_classes(currentMatrix);
        for (int i : classesCount) {
            double finalValue = i / (double) currentMatrix.size();
            returnMatrix.add(finalValue);
        }
        return returnMatrix;
    }


    public int[] find_total_classes(ArrayList<ArrayList<Double>> currentMatrix) {
        int classesCount[] = new int[10];
        int columnSize = currentMatrix.get(0).size() - 1;
        for (int i = 0; i < currentMatrix.size(); i++) {
            double currentClass = currentMatrix.get(i).get(columnSize);
            int myCurrentClass = (int) currentClass;
            classesCount[myCurrentClass]++;
        }
        return classesCount;

    }

    public double calcEntropy(ArrayList<ArrayList<Double>> currentMatrix) {
        double entropy = 0.0;
        if (!currentMatrix.isEmpty()) {
            int classesCount[] = find_total_classes(currentMatrix);
            int current_mat_size = currentMatrix.size();
            for (int i : classesCount) {
                double finalValue = i / (double) current_mat_size;
                if (finalValue != 0.0) {
                    entropy = entropy + (-1.0) * finalValue * (Math.log(finalValue));
                }
            }

        }
        return entropy;
    }
    
    private static void exit_function(int value) {
        System.out.println("Exiting from dtree!");
        System.exit(value);
    }
}