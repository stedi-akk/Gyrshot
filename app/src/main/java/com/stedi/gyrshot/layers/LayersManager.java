package com.stedi.gyrshot.layers;

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
        allLayers.add(layer);
        visibleLayers.add(layer);
        if (addToBackStack) {
            if (!backStack.empty()) {
                Layer topLayer = backStack.peek();
                visibleLayers.remove(topLayer);
            }
            backStack.push(layer);
        }
    }

    public boolean removeLayer(Layer layer) {
        return replace(layer, null);
    }

    public boolean replace(Layer what, Layer with) {
        if (!backStack.empty() && backStack.search(what) != -1)
            throw new IllegalArgumentException("You can't remove or replace layer that exists in the back stack");
        boolean removedFromAll = allLayers.remove(what);
        boolean removedFromVisible;
        if (with == null) {
            removedFromVisible = visibleLayers.remove(what);
        } else {
            int replaceIndex = visibleLayers.indexOf(what);
            removedFromVisible = replaceIndex != -1;
            if (removedFromVisible)
                visibleLayers.set(replaceIndex, with);
        }
        if (removedFromAll != removedFromVisible)
            throw new IllegalArgumentException("Internal error");
        return removedFromAll;
    }

    public boolean popBackStack() {
        if (backStack.empty())
            return false;
        Layer layer = backStack.pop();
        return removeLayer(layer);
    }

    public List<Layer> getVisibleLayers() {
        return visibleLayers;
    }
}
