package acronym;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children;
    String expandedForm;

    TrieNode() {
        children = new HashMap<>();
        expandedForm = null;
    }
}

class AcronymExpander {
    TrieNode root;

    AcronymExpander() {
        root = new TrieNode();
    }

    void insert(String acronym, String expandedForm) {
        TrieNode node = root;

        for (char c : acronym.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.expandedForm = expandedForm;
    }

    String expand(String text) {
        StringBuilder expandedText = new StringBuilder();
        int i = 0;

        while (i < text.length()) {
            TrieNode node = root;
            TrieNode altNode = root;
            int j = i;
            boolean altNodeActive = true;

            while (j < text.length() && (node.children.containsKey(Character.toLowerCase(text.charAt(j))) || (altNodeActive && altNode.children.containsKey(Character.toUpperCase(text.charAt(j)))))) {
                if (node.children.containsKey(Character.toLowerCase(text.charAt(j)))) {
                    node = node.children.get(Character.toLowerCase(text.charAt(j)));
                } else {
                    altNodeActive = false;
                }

                if (altNodeActive && altNode.children.containsKey(Character.toUpperCase(text.charAt(j)))) {
                    altNode = altNode.children.get(Character.toUpperCase(text.charAt(j)));
                } else {
                    altNodeActive = false;
                }

                j++;
            }

            boolean isWordBoundaryBefore = i == 0 || !Character.isLetter(text.charAt(i - 1));
            boolean isWordBoundaryAfter = j == text.length() || !Character.isLetter(text.charAt(j));

            if (isWordBoundaryBefore && isWordBoundaryAfter && node.expandedForm != null) {
                if (i == 0) {
                    expandedText.append(Character.toUpperCase(node.expandedForm.charAt(0)));
                    expandedText.append(node.expandedForm.substring(1));
                } else {
                    expandedText.append(node.expandedForm);
                }
                i = j;
            } else if (isWordBoundaryBefore && isWordBoundaryAfter && altNodeActive && altNode.expandedForm != null) {
                if (i == 0) {
                    expandedText.append(Character.toUpperCase(altNode.expandedForm.charAt(0)));
                    expandedText.append(altNode.expandedForm.substring(1));
                } else {
                    expandedText.append(altNode.expandedForm);
                }
                i = j;
            } else {
                expandedText.append(text.charAt(i));
                i++;
            }
        }

        return expandedText.toString();
    }
}