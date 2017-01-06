package com.stedi.gyrshot.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LayersManager {
    private static LayersManager instance;

    private List<Layer> layers;
    private Stack<Layer> backStack;

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
        if (layers == null)
            layers = new ArrayList<>();
        layers.add(layer);
        if (addToBackStack) {
            if (backStack == null)
                backStack = new Stack<>();
            backStack.push(layer);
        }
    }

    public boolean removeLayer(Layer layer) {
        if (layers == null)
            return false;
        if (backStack != null && !backStack.empty() && backStack.search(layer) != -1)
            return false;
        return layers.remove(layer);
    }

    public boolean popBackStack() {
        if (backStack == null || backStack.empty())
            return false;
        Layer layer = backStack.pop();
        return removeLayer(layer);
    }

    public List<Layer> getLayers() {
        return layers;
    }
}
