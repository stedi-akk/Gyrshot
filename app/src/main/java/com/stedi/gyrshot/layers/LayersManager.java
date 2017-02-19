package com.stedi.gyrshot.layers;

import com.stedi.gyrshot.App;
import com.stedi.gyrshot.constants.CoreConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class LayersManager {
    private static LayersManager instance;

    private List<Layer> visibleLayers = new ArrayList<>();
    private Stack<Layer> backStack = new Stack<>();

    private Layer topVisibleLayer;
    private Layer bottomVisibleLayer;

    private LayersManager() {
    }

    public static LayersManager getInstance() {
        if (instance == null)
            instance = new LayersManager();
        return instance;
    }

    public void attachLayerToTheTop(Layer layer) {
        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("before attachLayerToTheTop");

        topVisibleLayer = null;
        addLayer(layer);
        topVisibleLayer = layer;

        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("after attachLayerToTheTop");
    }

    public void attachLayerToTheBottom(Layer layer) {
        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("before attachLayerToTheBottom");

        if (visibleLayers.isEmpty())
            addLayer(layer);
        else
            visibleLayers.add(0, layer);
        bottomVisibleLayer = layer;

        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("after attachLayerToTheBottom");
    }

    public void addLayer(Layer layer) {
        addLayer(layer, false);
    }

    public void addLayer(Layer layer, boolean addToBackStack) {
        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("before addLayer");

        if (topVisibleLayer == null)
            visibleLayers.add(layer);
        else
            visibleLayers.add(visibleLayers.size() - 1, layer);
        if (addToBackStack) {
            if (!backStack.empty())
                visibleLayers.remove(backStack.peek());
            backStack.push(layer);
        }

        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("after addLayer");
    }

    public boolean removeLayer(Layer layer) {
        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("before removeLayer");

        if (!backStack.empty() && backStack.search(layer) != -1)
            throw new IllegalArgumentException("You can't remove layer that exists in the back stack");
        boolean removedFromVisible = visibleLayers.remove(layer);
        if (removedFromVisible) {
            if (layer == topVisibleLayer)
                topVisibleLayer = null;
            else if (layer == bottomVisibleLayer)
                bottomVisibleLayer = null;
        }

        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("after removeLayer");

        return removedFromVisible;
    }

    public boolean popBackStack() {
        if (CoreConfig.DEBUG_LAYERS_MANAGER)
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

        if (CoreConfig.DEBUG_LAYERS_MANAGER)
            logState("after popBackStack");

        return true;
    }

    public List<Layer> getVisibleLayers() {
        return visibleLayers;
    }

    public Stack<Layer> getBackStack() {
        return backStack;
    }

    public void clear() {
        visibleLayers.clear();
        backStack.clear();
        topVisibleLayer = null;
        bottomVisibleLayer = null;
    }

    private void logState(String title) {
        App.log(this, title +
                "\nvisibleLayers.size()=" + visibleLayers.size() +
                "\nbackStack.size()=" + backStack.size() +
                "\nbottomVisibleLayer=" + bottomVisibleLayer +
                "\ntopVisibleLayer=" + topVisibleLayer);
        App.log(this, "visibleLayers :");
        for (Layer layer : visibleLayers)
            App.log(this, String.valueOf(layer));
    }
}
