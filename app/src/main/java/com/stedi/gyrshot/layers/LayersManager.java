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
        return replace(layer, null);
    }

    public boolean replace(Layer what, Layer with) {
        logState("before replace");
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
        logState("after replace");
        return removedFromAll;
    }

    public boolean popBackStack() {
        logState("before popBackStack");
        if (backStack.empty())
            return false;
        Layer layer = backStack.pop();
        boolean result = removeLayer(layer);
        logState("after popBackStack");
        return result;
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
