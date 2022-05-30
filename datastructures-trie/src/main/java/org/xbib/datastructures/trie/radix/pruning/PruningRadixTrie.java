package org.xbib.datastructures.trie.radix.pruning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PruningRadixTrie {
	
    public long termCount = 0;

    private final Node trie;

    public PruningRadixTrie() {
        this.trie = new Node(0);
    }

    public void addTerm(String term, long termFrequencyCount) {
        List<Node> nodeList = new ArrayList<>();
        addTerm(trie, term, termFrequencyCount, 0, 0, nodeList);
    }

    public void updateMaxCounts(List<Node> nodeList, long termFrequencyCount) {
    	for (Node node : nodeList) {
    		if (termFrequencyCount > node.getTermFrequencyCountChildMax()) {
    			node.setTermFrequencyCountChildMax(termFrequencyCount);
    		}
    	}
    }

    public void addTerm(Node curr, String term, long termFrequencyCount, int id, int level, List<Node> nodeList)
    {
        try {
            nodeList.add(curr);
            int common = 0;
            List<NodeChild> currChildren = curr.getChildren();
            if (currChildren != null) { 
                for (int j = 0; j < currChildren.size(); j++) {
                    String key = currChildren.get(j).getKey();
                    Node node = currChildren.get(j).getNode();

                    for (int i = 0; i < Math.min(term.length(), key.length()); i++) {
                    	if (term.charAt(i) == key.charAt(i)) common = i + 1;
                    	else break;
                    }

                    if (common > 0) {
                        //term already existed
                        //existing ab
                        //new      ab
                        if ((common == term.length()) && (common == key.length())) {
                            if (node.getTermFrequencyCount() == 0) termCount++;
                            node.setTermFrequencyCount(node.getTermFrequencyCount() + termFrequencyCount);
                            updateMaxCounts(nodeList, node.getTermFrequencyCount());
                        }
                        //new is subkey
                        //existing abcd
                        //new      ab
                        //if new is shorter (== common), then node(count) and only 1. children add (clause2)
                        else if (common == term.length()) {
                            //insert second part of oldKey as child 
                            Node child = new Node(termFrequencyCount);
                            List<NodeChild> l = new ArrayList<>();
                            l.add(new NodeChild(key.substring(common), node));
                            child.setChildren(l);
                            
                            child.setTermFrequencyCountChildMax( 
                            		Math.max(node.getTermFrequencyCountChildMax(), node.getTermFrequencyCount()));
                            updateMaxCounts(nodeList, termFrequencyCount);

                            //insert first part as key, overwrite old node
                            currChildren.set(j, new NodeChild(term.substring(0, common), child));
                            //sort children descending by termFrequencyCountChildMax to start lookup with most promising branch
                            Collections.sort(currChildren, Comparator.comparing(
                            		(NodeChild e) -> e.getNode().getTermFrequencyCountChildMax()).reversed());
                            //increment termcount by 1
                            termCount++;
                        }
                        //if oldkey shorter (==common), then recursive addTerm (clause1)
                        //existing: te
                        //new:      test
                        else if (common == key.length()) {
                            addTerm(node, term.substring(common), termFrequencyCount, id, level + 1, nodeList);
                        }
                        //old and new have common substrings
                        //existing: test
                        //new:      team
                        else {
                            //insert second part of oldKey and of s as child 
                            Node child = new Node(0);//count
                            List<NodeChild> l = new ArrayList<>();
                            l.add(new NodeChild(key.substring(common), node));
                            l.add(new NodeChild(term.substring(common), new Node(termFrequencyCount)));
                            child.setChildren(l);

                            child.setTermFrequencyCountChildMax(
                            		Math.max(node.getTermFrequencyCountChildMax(), Math.max(termFrequencyCount, node.getTermFrequencyCount())));
                            updateMaxCounts(nodeList, termFrequencyCount);
                            
                            //insert first part as key, overwrite old node
                            currChildren.set(j, new NodeChild(term.substring(0, common), child));
                            //sort children descending by termFrequencyCountChildMax to start lookup with most promising branch
                            Collections.sort(currChildren, Comparator.comparing(
                            		(NodeChild e) -> e.getNode().getTermFrequencyCountChildMax()).reversed());
                            //increment termcount by 1
                            termCount++;
                        }
                        return;
                    }
                }
            }

            // initialize dictionary if first key is inserted 
            if (currChildren == null) {
                List<NodeChild> l = new ArrayList<>();
                l.add(new NodeChild(term, new Node(termFrequencyCount)));
                curr.setChildren(l);
            }
            else {
            	currChildren.add(new NodeChild(term, new Node(termFrequencyCount)));
                //sort children descending by termFrequencyCountChildMax to start lookup with most promising branch
                currChildren.sort(Comparator.comparing(
                        (NodeChild e) -> e.getNode().getTermFrequencyCountChildMax()).reversed());
            }
            termCount++;
            updateMaxCounts(nodeList, termFrequencyCount);
        } catch (Exception e) { System.out.println("exception: " + term + " " + e.getMessage()); }
    }

    public void findAllChildTerms(String prefix, int topK, String prefixString, List<TermAndFrequency> results, Boolean pruning) {
        findAllChildTerms(prefix, trie, topK, prefixString, results, pruning);
    }

    public void findAllChildTerms(String prefix, Node curr, int topK, String prefixString, List<TermAndFrequency> results, Boolean pruning)
    {
        try {
            //pruning/early termination in radix trie lookup
            if (pruning && (topK > 0) && (results.size() == topK) && 
            		(curr.getTermFrequencyCountChildMax() <= results.get(topK - 1).getTermFrequencyCount())) { 
            	return;
            }

            //test for common prefix (with possibly different suffix)
            boolean noPrefix = prefix.equals("");

            if (curr.getChildren() != null) {
                for (NodeChild nodeChild : curr.getChildren()) {
                	String key = nodeChild.getKey();
                	Node node = nodeChild.getNode();
                    //pruning/early termination in radix trie lookup
                    if (pruning && (topK > 0) && (results.size() == topK) &&
                    		(node.getTermFrequencyCount() <= results.get(topK - 1).getTermFrequencyCount()) && 
                    		(node.getTermFrequencyCountChildMax() <= results.get(topK - 1).getTermFrequencyCount())) {
                        if (!noPrefix) break; 
                        else continue;
                    }                     

                    if (noPrefix || key.startsWith(prefix)) {
                        if (node.getTermFrequencyCount() > 0) {
                            	if (topK > 0) addTopKSuggestion(prefixString + key, node.getTermFrequencyCount(), topK, results);
                            	else results.add(new TermAndFrequency(prefixString + key, node.getTermFrequencyCount()));  
                        }

                        if ((node.getChildren() != null) && (node.getChildren().size() > 0)) { 
                        	findAllChildTerms("", node, topK, prefixString + key, results, pruning);
                        }
                        if (!noPrefix) break;
                    } else if (prefix.startsWith(key)) {
                        if ((node.getChildren() != null) && (node.getChildren().size() > 0)) {
                        	findAllChildTerms(prefix.substring(key.length()), node, topK, prefixString + key, results, pruning);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) { System.out.println("exception: " + prefix + " " + e.getMessage()); }
    }
    
    public List<TermAndFrequency> getTopkTermsForPrefix(String prefix, int topK) {
    	return getTopkTermsForPrefix(prefix, topK, true);
    }
    
    public List<TermAndFrequency> getTopkTermsForPrefix(String prefix, int topK, Boolean pruning) {
        List<TermAndFrequency> results = new ArrayList<>();
        findAllChildTerms(prefix, topK, "", results, pruning);
        return results;
    }

    public void addTopKSuggestion(String term, long termFrequencyCount, int topK, List<TermAndFrequency> results) 
    {
        //at the end/highest index is the lowest value
        // >  : old take precedence for equal rank   
        // >= : new take precedence for equal rank 
        if ((results.size() < topK) || (termFrequencyCount >= results.get(topK - 1).getTermFrequencyCount())) {
        	TermAndFrequency termAndFrequency = new TermAndFrequency(term, termFrequencyCount);
        	int index = Collections.binarySearch(results, termAndFrequency, Comparator.comparing(
                    TermAndFrequency::getTermFrequencyCount).reversed()); // descending order
            if (index < 0) results.add(~index, termAndFrequency); 
            else results.add(index, termAndFrequency); 

            if (results.size() > topK) results.remove(topK);
        }
    }
}