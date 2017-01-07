package com.stedi.gyrshot.layers;

import com.stedi.gyrshot.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LayersManager {
    private static LayersManager instance;

    private List<Layer> allLayers = new ArrayList<>();
    private List<Layer> visibleLayers = new ArrayList<>();
    private Stack<Layer> backStack = new Stack<>();

    private LayersManager() {
    }

    public static LayersManager getInstance() {
        if (instance == null)
            instance = new LayersManager();
        return instance;
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean addToBackStack) {
        logState("before addLayer");
        allLayers.add(layer);
        visibleLayers.add(layer);
        if (addToBackStack) {
            if (!backStack.empty()) {
                Layer topLayer = backStack.peek();
                visibleLayers.remove(topLayer);
            }
            backStack.push(layer);
        }
        logState("after addLayer");
    }

    public boolean removeLayer(Layer layer) {
        logState("before removeLayer");
        if (!backStack.empty() && backStack.search(layer) != -1)
            throw new IllegalArgumentException("You can't remove layer that exists in the back stack");
        boolean removedFromAll = allLayers.remove(layer);
        boolean removedFromVisible = visibleLayers.remove(layer);
        if (removedFromAll != removedFromVisible)
            throw new IllegalArgumentException("Internal error");
        logState("after removeLayer");
        return removedFromAll;
    }

    public boolean popBackStack() {
        logState("before popBackStack");
        if (backStack.empty())
            return false;
        Layer layer = backStack.pop();
        int indexInVisible = visibleLayers.indexOf(layer);
        if (indexInVisible == -1)
            throw new IllegalArgumentException("Internal error");
        boolean removed = removeLayer(layer);
        if (!removed)
            throw new IllegalArgumentException("Internal error");
        visibleLayers.add(indexInVisible, backStack.peek());
        logState("after popBackStack");
        return true;
    }

    public List<Layer> getVisibleLayers() {
        return visibleLayers;
    }

    public Stack<Layer> getBackStack() {
        return backStack;
    }

    private void logState(String title) {
        App.log(this, title +
                "\nallLayers.size()=" + allLayers.size() +
                "\nvisibleLayers.size()=" + visibleLayers.size() +
                "\nbackStack.size()=" + backStack.size());
    }
}
