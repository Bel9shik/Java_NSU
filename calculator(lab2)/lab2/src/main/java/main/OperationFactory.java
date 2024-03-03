package main;

import java.util.HashMap;
import java.util.Map;

import operations.*;

public class OperationFactory {
    public Map<String, Class<? extends Product>> operationMap = new HashMap<>();

    public OperationFactory() {

        String packageName = "operations";
        try {

            Class[] classes = getAllClasses(packageName);
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(operation.class)) {
                    operation annotation = clazz.getAnnotation(operation.class);
                    operationMap.put(annotation.name(), (Class<? extends Product>) clazz);

                }

            }

//            for (Map.Entry<String, Class<? extends Product>> entry : operationMap.entrySet()) {
//                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Product getOperation(String className) throws IllegalAccessException, InstantiationException {
        Class<? extends Product> operationClass = operationMap.get(className);
        if (operationClass != null) {
            return operationClass.newInstance();
        } else {
            throw new IllegalArgumentException("Unknown operation: " + className);
        }
    }


    private Class[] getAllClasses(String packageName) throws ClassNotFoundException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        java.net.URL resource = classLoader.getResource(path);
        java.io.File directory = new java.io.File(resource.getFile());
        java.util.ArrayList<Class> classes = new java.util.ArrayList<Class>();

        for (java.io.File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }


}
