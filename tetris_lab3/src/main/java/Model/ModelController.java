package Model;

import Model.Figures.FigureAnnotation;
import Model.Figures.GeneralFigure;
import View.ControllerView;

import java.io.IOException;
import java.util.*;

public class ModelController { //его вызывает контроллер
    ControllerView view;
    Field field = new Field();
    List<Class<? extends FigureAnnotation>> backOfFigures = new ArrayList<>();
    private Map<String, Class<? extends FigureAnnotation>> figuresMap = new HashMap<>();
    List<String> rawClasses = ClassUtils.findAllClasses();

    public ModelController() throws IOException {
        getAllFigures();
        for (Map.Entry<String,Class<? extends FigureAnnotation>> figureEntry : figuresMap.entrySet()) {
            backOfFigures.add(figureEntry.getValue());
        }
        Collections.shuffle(backOfFigures);
        view = new ControllerView("tetris", "./src/main/java/RESOURCES/titleImage.png", 800, 1000);
        view.createApplicationWindow();
    }


    private void getAllFigures() {
        for (String rawClass : rawClasses) {
            try {
                if (rawClass.contains("org") || rawClass.contains("java")) continue;
                Class<?> clazz = Class.forName(rawClass);
                if (clazz.isAnnotationPresent(FigureAnnotation.class)) {
                    FigureAnnotation annotation = clazz.getAnnotation(FigureAnnotation.class);
                    figuresMap.put(annotation.name(), (Class<? extends FigureAnnotation>) clazz);
                }
//                System.out.println(clazz.getDeclaredConstructor().newInstance().getClass());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage() + e);
            } catch (ClassCastException e) {
                continue;
            }
        }
    }

    private FigureAnnotation getOperation(String className) throws IllegalAccessException { //mb useless. Need to check
        Class<? extends FigureAnnotation> operationClass = figuresMap.get(className);
        if (operationClass != null) {
            FigureAnnotation tmpClass;
            try {
                tmpClass = operationClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println(e.getMessage() + e);
                return null;
            }
            return tmpClass;
        } else {
            throw new IllegalArgumentException("Unknown operation: " + className);
        }
    }
}
