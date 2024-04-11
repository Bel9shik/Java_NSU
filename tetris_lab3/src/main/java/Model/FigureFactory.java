package Model;

import Model.Figures.FigureAnnotation;
import Model.Figures.GeneralFigure;

import java.util.*;

public class FigureFactory {
    private List<String> backOfFigures = new ArrayList<>();
    private Map<String, Class<? extends GeneralFigure>> figuresMap = new HashMap<>();
    private List<String> rawClasses = ClassUtils.findAllClasses();

    public FigureFactory() {
        getAllFigures();
        for (Map.Entry<String,Class<? extends GeneralFigure>> figureEntry : figuresMap.entrySet()) {
            backOfFigures.add(figureEntry.getKey());
        }
        Collections.shuffle(backOfFigures);
        System.out.println(backOfFigures);
    }

    public List<String>getBackOfFigure () {
        return backOfFigures;
    }


    private void getAllFigures() {
        for (String rawClass : rawClasses) {
            try {
                Class<?> clazz = Class.forName(rawClass);
                if (clazz.isAnnotationPresent(FigureAnnotation.class)) {
                    FigureAnnotation annotation = clazz.getAnnotation(FigureAnnotation.class);
                    figuresMap.put(annotation.name(), (Class<? extends GeneralFigure>) clazz);
                }
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage() + e);
            } catch (ClassCastException e) {
                continue;
            }
        }
    }

    public GeneralFigure getFigure(String className) { //mb useless. Need to check
        Class<? extends GeneralFigure> operationClass = figuresMap.get(className);
        if (operationClass != null) {
            GeneralFigure tmpClass;
            try {
                tmpClass = operationClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println(e.getMessage() + e);
                return null;
            }
            return tmpClass;
        } else {
            throw new IllegalArgumentException("Unknown figure: " + className);
        }
    }

    public void shuffleBack() {
        Collections.shuffle(backOfFigures);
    }
}
