import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 * 
 * You must add code for the 1 member and 5 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
  private DecTreeNode root;
  //ordered list of class labels
  private List<String> labels; 
  //ordered list of attributes
  private List<String> attributes; 
  //map to ordered discrete values taken by attributes
  private Map<String, List<String>> attributeValues; 
  /**
   * Answers static questions about decision trees.
   */
  DecisionTreeImpl() {
    // no code necessary this is void purposefully
  }

  /**
   * Build a decision tree given only a training set.
   * 
   * @param train: the training set
   */
  DecisionTreeImpl(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    if (train.instances.isEmpty()){
    	System.out.println("Empty training set!");
    	System.exit(-1);
    }
    int sum1 = 0;
    int sum2 = 0;
    String label;
    for (int i = 0; i < train.instances.size(); i++)
    	if (train.instances.get(i).label.equals(labels.get(0)))
    		sum1++;
    	else sum2++;
    if (sum1 >= sum2)
    	label = labels.get(0);
    else label = labels.get(1);
    root = buildTree(train.instances, attributes, label, null);
  }

  /**
   * Build a decision tree given a training set then prune it using a tuning set.
   * 
   * @param train: the training set
   * @param tune: the tuning set
   */
  DecisionTreeImpl(DataSet train, DataSet tune) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    if (train.instances.isEmpty()){
    	System.out.println("Empty training set!");
    	System.exit(-1);
    }
    int sum1 = 0;
    int sum2 = 0;
    String label;
    for (int i = 0; i < train.instances.size(); i++)
    	if (train.instances.get(i).label.equals(labels.get(0)))
    		sum1++;
    	else sum2++;
    if (sum1 >= sum2)
    	label = labels.get(0);
    else label = labels.get(1);
    root = buildTree(train.instances, attributes, label, null);
    int a = 0;
    double accT = getAccuracy(tune);
    double accT2 = 0;
    String label2;
    DecTreeNode root2 = null;
    while (accT >= accT2){
        ArrayList<DecTreeNode> nodes = new ArrayList<DecTreeNode>();
    	accT2 = accT;
    	accT = 0;
    	root2 = copyTree(root);
    	nodes = tree2queue(root2);
    	for (int i = 1; i < nodes.size(); i++){
    		ArrayList<DecTreeNode> nodes2 = tree2queue(root2);
	    	label2 = getMajority(nodes2.get(i), train);
	    	nodes2.get(i).terminal = true;
	    	nodes2.get(i).label = label2;
	    	nodes2.get(i).attribute = null;
	    	nodes2.get(i).children = null;
	    	
	    	
	    	if(accT < getAccuracy(tune, root2)){
	    		accT = getAccuracy(tune, root2);
	    		a = i;
	    	}
	    	root2 = copyTree(root);
    	}
    	if (accT >= accT2){
    	   ArrayList<DecTreeNode> nodes2 = tree2queue(root2);
           nodes2.get(a).terminal = true;
           nodes2.get(a).label = getMajority(nodes.get(a), train);
           nodes2.get(a).attribute = null;
           nodes2.get(a).children = null;
           root = copyTree(root2);
    	}
    }
  }

  @Override
  public String classify(Instance instance) {
	  DecTreeNode node = root;
	  int a;
	  while(!node.terminal){
		  a = getAttributeIndex(node.attribute);
		  for (int i = 0; i < node.children.size(); i++){
			  if (instance.attributes.get(a).equals(node.children.get(i).parentAttributeValue)){
				  node = node.children.get(i);
				  break;
			  }
		  }
	  }
	  return node.label;
  }
  
  public String classify(Instance instance, DecTreeNode node) {
	  int a;
	  while(!node.terminal){
		  a = getAttributeIndex(node.attribute);
		  for (int i = 0; i < node.children.size(); i++){
			  if (instance.attributes.get(a).equals(node.children.get(i).parentAttributeValue)){
				  node = node.children.get(i);
				  break;
			  }
		  }
	  }
	  return node.label;
  }

  @Override
  public void rootInfoGain(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    int size = attributes.size();
    double infoGain;
    double Entropy0;
    double sum1 = 0, sum2 = 0;
    for (int i = 0; i < train.instances.size(); i++){
    	if(train.instances.get(i).label.equals(labels.get(0)))
    		sum1++;
    	else if(train.instances.get(i).label.equals(labels.get(1)))
    		sum2++;
    	else{
    		System.out.println("bad instance1");
    		System.exit(-1);
    	}
    }
    Entropy0 = entropy(sum1/(sum1+sum2));
    for (int i = 0; i < size; i++){
    	double Entropy1 = 0;
    	String attribute = attributes.get(i);
    	for (int j = 0; j < attributeValues.get(attribute).size(); j++){
    		sum1 = 0;
    		sum2 = 0;
    		for (int k = 0; k < train.instances.size(); k++){
    			if (attributeValues.get(attribute).get(j).equals(train.instances.get(k).attributes.get(i))){
    				if(train.instances.get(k).label.equals(labels.get(0)))
    		    		sum1++;
    		    	else if(train.instances.get(k).label.equals(labels.get(1)))
    		    		sum2++;
    		    	else{
    		    		System.out.println("bad instance2");
    		    		System.exit(-1);
    		    	}
    			}
    		}
    		if(sum1 != 0 || sum2 != 0)
    		Entropy1 += entropy(sum1/(sum1+sum2))*(sum1+sum2)/train.instances.size();
    	}
    	infoGain = Entropy0 - Entropy1;
    	System.out.print(attribute + " ");
    	System.out.format("%.5f", infoGain);
    	System.out.println();
    }
  }
  
  @Override
  /**
   * Print the decision tree in the specified format
   */
  public void print() {

    printTreeNode(root, null, 0);
  }

  /**
   * Prints the subtree of the node with each line prefixed by 4 * k spaces.
   */
  public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < k; i++) {
      sb.append("    ");
    }
    String value;
    if (parent == null) {
      value = "ROOT";
    } else {
      int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
      value = attributeValues.get(parent.attribute).get(attributeValueIndex);
    }
    sb.append(value);
    if (p.terminal) {
      sb.append(" (" + p.label + ")");
      System.out.println(sb.toString());
    } else {
      sb.append(" {" + p.attribute + "?}");
      System.out.println(sb.toString());
      for (DecTreeNode child : p.children) {
        printTreeNode(child, p, k + 1);
      }
    }
  }

  /**
   * Helper function to get the index of the label in labels list
   */
  private int getLabelIndex(String label) {
    for (int i = 0; i < this.labels.size(); i++) {
      if (label.equals(this.labels.get(i))) {
        return i;
      }
    }
    return -1;
  }
 
  /**
   * Helper function to get the index of the attribute in attributes list
   */
  private int getAttributeIndex(String attr) {
    for (int i = 0; i < this.attributes.size(); i++) {
      if (attr.equals(this.attributes.get(i))) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Helper function to get the index of the attributeValue in the list for the attribute key in the attributeValues map
   */
  private int getAttributeValueIndex(String attr, String value) {
    for (int i = 0; i < attributeValues.get(attr).size(); i++) {
      if (value.equals(attributeValues.get(attr).get(i))) {
        return i;
      }
    }
    return -1;
  }
  
  
/**
   /* Returns the accuracy of the decision tree on a given DataSet.
   */
  @Override
  public double getAccuracy(DataSet ds){
	 double sum = 0;
	 double size = ds.instances.size();
	 for (int i = 0; i < size; i++)
		 if (ds.instances.get(i).label.equals(classify(ds.instances.get(i))))
			 sum++;
	 double accuracy = sum/size;
	 return accuracy;
  }
  
  public double getAccuracy(DataSet ds, DecTreeNode node){
		 double sum = 0;
		 double size = ds.instances.size();
		 for (int i = 0; i < size; i++)
			 if (ds.instances.get(i).label.equals(classify(ds.instances.get(i), node)))
				 sum++;
		 double accuracy = sum/size;
		 return accuracy;
	  }
  private double entropy(double p){
	  double entropy;
	  if (p == 0 || p == 1)
		  return 0;
	  entropy = -1*p*Math.log(p)/Math.log(2)-(1-p)*Math.log((1-p))/Math.log((2));
	  return entropy;
	  
  }
 private String getbestAttribute(List<Instance> ds, List<String> attributes){
	    int size = attributes.size();
	    double infoGain = 0;
	    double Entropy0;
	    double sum1 = 0, sum2 = 0;
	    String bestAttribute = null;
	    for (int i = 0; i < ds.size(); i++){
	    	if(ds.get(i).label.equals(labels.get(0)))
	    		sum1++;
	    	else if(ds.get(i).label.equals(labels.get(1)))
	    		sum2++;
	    	else{
	    		System.out.println("bad instance3");
	    		System.exit(-1);
	    	}
	    }
	    Entropy0 = entropy(sum1/(sum1+sum2));
	    for (int i = 0; i < size; i++){
	    	double Entropy1 = 0;
	    	String attribute = attributes.get(i);
	    	for (int j = 0; j < attributeValues.get(attribute).size(); j++){
	    		sum1 = 0;
	    		sum2 = 0;
	    		for (int k = 0; k < ds.size(); k++){
	    			if (attributeValues.get(attribute).get(j).equals(ds.get(k).attributes.get(getAttributeIndex(attribute)))){
	    				if(ds.get(k).label.equals(labels.get(0)))
	    		    		sum1++;
	    		    	else if(ds.get(k).label.equals(labels.get(1)))
	    		    		sum2++;
	    		    	else{
	    		    		System.out.println("bad instance4");
	    		    		System.exit(-1);
	    		    	}
	    			}
	    		}
	    		if(sum1 != 0 || sum2 != 0)
	    		Entropy1 += entropy(sum1/(sum1+sum2))*(sum1+sum2)/ds.size();
	    	}
	    	if (bestAttribute == null){
	    		infoGain = Entropy0 - Entropy1;
	    		bestAttribute = attribute;
	    	}
	    	if (Entropy0 - Entropy1 > infoGain){
	    	infoGain = Entropy0 - Entropy1;
	    	bestAttribute = attribute;
	    	}
	    }
	    return bestAttribute;
 }
private DecTreeNode buildTree(List<Instance> examples, List<String> attributes, String default_label, String parent_attribute_value){
	if (examples.isEmpty() || attributes.isEmpty() || examples.size() == 1)
		return (new DecTreeNode(default_label, null, parent_attribute_value, true));
	for (int i = 0; i < examples.size() - 1; i++){
		if (!examples.get(i).label.equals(examples.get(i+1).label))
			break;
		if (i == examples.size() - 2)
		return (new DecTreeNode(default_label, null, parent_attribute_value, true));
	}
	List<String> newattributes = new ArrayList<String>();
	newattributes.addAll(attributes);
	String bestAttribute = getbestAttribute(examples, attributes);
	newattributes.remove(bestAttribute);
	int a = getAttributeIndex(bestAttribute);
	DecTreeNode tree = new DecTreeNode(default_label, bestAttribute, parent_attribute_value, false);
	for (int i = 0; i < attributeValues.get(bestAttribute).size(); i++){
		List<Instance> newexamples = new ArrayList<Instance>();
		newexamples.addAll(examples);
		String parent = attributeValues.get(bestAttribute).get(i);
		String label;
		int sum1 = 0;
		int sum2 = 0;
		int k = 0;
		for (int j = 0; j < examples.size(); j++){
			if (newexamples.get(j-k).attributes.get(a).equals(parent)){
				if (newexamples.get(j-k).label.equals(labels.get(0)))
					sum1++;
				else if (newexamples.get(j-k).label.equals(labels.get(1)))
					sum2++;
				else{
		    		System.out.println("bad instance5");
		    		System.exit(-1);
		    	}
			}
			else {
				newexamples.remove(j-k);
				k++;
			}	
		}
		if (sum1 >= sum2)
			label = labels.get(0);
		else 
			label = labels.get(1);
		tree.addChild(buildTree(newexamples, newattributes, label, parent));
	}
	return tree;
}
private DecTreeNode copyTree(DecTreeNode node){
	if (node.terminal)
		return (new DecTreeNode(node.label, node.attribute, node.parentAttributeValue, node.terminal));
	DecTreeNode newnode = new DecTreeNode(node.label, node.attribute, node.parentAttributeValue, node.terminal);
	for (DecTreeNode child: node.children)
		newnode.addChild(copyTree(child));
	return newnode;
}
private String classify2(Instance instance, String attribute){
	 DecTreeNode node = root;
	  int a;
	  if (attribute == null)
		  return classify(instance);
	  while(!node.attribute.equals(attribute)){
		  a = getAttributeIndex(node.attribute);
		  for (int i = 0; i < node.children.size(); i++){
			  if (instance.attributes.get(a).equals(node.children.get(i).parentAttributeValue)){
				  node = node.children.get(i);
				  break;
			  }
		  }
		  if (node.terminal)
			  return "no";
	  }
	  return node.label;
}
private String getMajority(DecTreeNode node, DataSet train){
	List<Instance> instances = train.instances;
	int sum1 = 0, sum2 = 0;
	for (int i = 0; i < instances.size(); i++){
		if (getLabelIndex(classify2(instances.get(i), node.attribute)) == 0)
			sum1++;
		else if (getLabelIndex(classify2(instances.get(i), node.attribute)) == 1)
			sum2++;
	}
	if(sum1 >= sum2)
		return labels.get(0);
	else return labels.get(1);
}
private ArrayList<DecTreeNode> tree2queue(DecTreeNode node){
	 ArrayList<DecTreeNode> nodes = new ArrayList<DecTreeNode>();
     ArrayList<DecTreeNode> frontier = new ArrayList<DecTreeNode>();
     frontier.add(node);
 	 while(!frontier.isEmpty()){
 	    	int size = frontier.size();
 	    	for (int i = 0; i < size; i++){
 	    		node = frontier.remove(0);
 	    		if (node.children != null)
 	    		for (int j = 0; j < node.children.size(); j++)
 	    			if (!node.children.get(j).terminal)
 	    			frontier.add(node.children.get(j));
 	    		if (!node.terminal)
 	    		nodes.add(node);
 	    	}
 	    }
 	 return nodes;
}
}
