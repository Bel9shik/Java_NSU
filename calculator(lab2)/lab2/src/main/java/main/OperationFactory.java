package main;

import operations.Product;
import operations.operation;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class OperationFactory {
    private Map<String, Class<? extends Product>> operationMap = new HashMap<>();
    List<String> rawClasses = ClassUtils.findAllClasses();

    public OperationFactory() {
        getAllOperations();
    }

    public void getAllOperations() {
        for (String rawClass : rawClasses) {
            try {
                if (rawClass.contains("org") || rawClass.contains("java")) continue;
                Class<?> clazz = Class.forName(rawClass);
                if (clazz.isAnnotationPresent(operation.class)) {
                    operation annotation = clazz.getAnnotation(operation.class);
                    operationMap.put(annotation.name(), (Class<? extends Product>) clazz);
                }
//                System.out.println(clazz.getDeclaredConstructor().newInstance().getClass());
            } catch (ClassNotFoundException e) {
                ControlBlock.logger.error(e.getMessage(), e);
            } catch (ClassCastException e) {
                continue;
            }
        }
    }

    public Product getOperation(String className) throws  IllegalAccessException {
        Class<? extends Product> operationClass = operationMap.get(className);
        if (operationClass != null) {
            Product tmpClass;
            try {
                tmpClass = operationClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                ControlBlock.logger.error(e.getMessage(), e);
                return null;
            }
            return tmpClass;
        } else {
            throw new IllegalArgumentException("Unknown operation: " + className);
        }
    }
}
